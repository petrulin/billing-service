package com.otus.billingservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otus.billingservice.domain.request.BalanceClientRequest;
import com.otus.billingservice.domain.response.SimpeResponse;
import com.otus.billingservice.entity.Message;
import com.otus.billingservice.error.NotEnoughMoneyException;
import com.otus.billingservice.rabbitmq.domain.RMessage;
import com.otus.billingservice.rabbitmq.domain.dto.CancelDTO;
import com.otus.billingservice.rabbitmq.domain.dto.TrxDTO;
import com.otus.billingservice.service.BillingService;
import com.otus.billingservice.service.MessageService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueListener {

    private final MessageService messageService;
    private final BillingService billingService;
    private final RabbitTemplate rt;

    @Value("${spring.rabbitmq.queues.service-answer-queue}")
    private String answerQueue;
    @Value("${spring.rabbitmq.exchanges.service-answer-exchange}")
    private String answerExchange;

    @Transactional
    @RabbitListener(queues = "${spring.rabbitmq.queues.service-queue}", ackMode = "MANUAL")
    public void orderQueueListener(RMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            var om = new ObjectMapper();
            var msg = messageService.findById(message.getMsgId());
            if (msg == null) {
                messageService.save(new Message(message.getMsgId()));
                switch (message.getCmd()) {
                    case "withdraw" ->  {
                        om.getTypeFactory().constructCollectionType(ArrayList.class, TrxDTO.class);
                        var trxDTO = om.convertValue(message.getMessage(), TrxDTO.class);
                        try {
                            billingService.balanceWithdraw(new BalanceClientRequest(trxDTO.getOrder().getUserName(), trxDTO.getOrder().getAmount()));
                            trxDTO.setPayStatus("Ok");
                        } catch (NotEnoughMoneyException ex) {
                            trxDTO.setPayStatus("Недостаточно средств на балансе клиента");
                        } catch (Exception ex) {
                            trxDTO.setPayStatus("Ошибка оплаты");
                            //answer = new SimpeResponse("ERROR", ex.getLocalizedMessage());
                        }
                        rt.convertAndSend(answerExchange, answerQueue,
                                new RMessage(UUID.randomUUID().toString(), "confirmSale", trxDTO)
                        );
                    }
                    case "refund" ->  {
                        om.getTypeFactory().constructCollectionType(ArrayList.class, BalanceClientRequest.class);
                        var balanceClientRequest = om.convertValue(message.getMessage(), BalanceClientRequest.class);
                        billingService.balanceAdd(balanceClientRequest);
                    }
                    default -> log.warn("::BillingService:: rabbitmq listener method. Unknown message type");
                }
            }
            else {
                log.warn("::BillingService:: rabbitmq listener method orderQueueListener duplicate message!");
            }
        } catch (Exception ex) {
            log.error("::BillingService:: rabbitmq listener method orderQueueListener with error message {}", ex.getLocalizedMessage());
            log.error("::BillingService:: rabbitmq listener method orderQueueListener with stackTrace {}", ExceptionUtils.getStackTrace(ex));
        } finally {
            basicAck(channel, tag, true);
        }
    }

    private void basicAck(Channel channel, Long tag, boolean b) {
        try {
            channel.basicAck(tag, b);
        } catch (IOException ex) {
            log.error("::BillingService:: rabbitmq listener method basicAck with stackTrace {}", ExceptionUtils.getStackTrace(ex));
        }
    }
}
