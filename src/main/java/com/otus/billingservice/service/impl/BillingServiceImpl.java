package com.otus.billingservice.service.impl;


import com.otus.billingservice.domain.request.BalanceClientRequest;
import com.otus.billingservice.domain.request.RegisterClientRequest;
import com.otus.billingservice.entity.Client;
import com.otus.billingservice.error.ClientNotFoudException;
import com.otus.billingservice.error.NotEnoughMoneyException;
import com.otus.billingservice.rabbitmq.domain.dto.TrxDTO;
import com.otus.billingservice.repository.ClientRepository;
import com.otus.billingservice.service.BillingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@Slf4j
@Service("paymentService")
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {
    private final ClientRepository clientRepository;

    @Override
    public void createClient(RegisterClientRequest clientRequest) {
        clientRepository.save(new Client(clientRequest.getUsername(), 933));
    }

    @Override
    public void balanceAdd(BalanceClientRequest balanceClientRequest) throws ClientNotFoudException {

        Client client = clientRepository.findClientByUserNameAndCurrency(balanceClientRequest.getUsername(), 933);
        if (client == null) {
            throw new ClientNotFoudException();
        }
        client.setBalance(client.getBalance().add(balanceClientRequest.getAmount()));
        clientRepository.save(client);
    }

    @Override
    public void balanceWithdraw(BalanceClientRequest balanceClientRequest) throws ClientNotFoudException, NotEnoughMoneyException {

        Client client = clientRepository.findClientByUserNameAndCurrency(balanceClientRequest.getUsername(), 933);
        if (client == null) {
            throw new ClientNotFoudException();
        }

        if ((client.getBalance().subtract(balanceClientRequest.getAmount()).compareTo(BigDecimal.ZERO) < 0)) {
            throw new NotEnoughMoneyException();
        } else {
            client.setBalance(client.getBalance().subtract(balanceClientRequest.getAmount()));
            clientRepository.save(client);
        }
    }
    @Override
    public Client getBalance(BalanceClientRequest balanceClientRequest) throws ClientNotFoudException {
        Client client = clientRepository.findClientByUserNameAndCurrency(balanceClientRequest.getUsername(), 933);
        if (client == null) {
            throw new ClientNotFoudException();
        }
        return client;
    }


}
