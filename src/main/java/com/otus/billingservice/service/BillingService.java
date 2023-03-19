package com.otus.billingservice.service;

import com.otus.billingservice.domain.request.BalanceClientRequest;
import com.otus.billingservice.domain.request.RegisterClientRequest;
import com.otus.billingservice.error.ClientNotFoudException;
import com.otus.billingservice.error.NotEnoughMoneyException;
import com.otus.billingservice.rabbitmq.domain.dto.CancelDTO;
import com.otus.billingservice.rabbitmq.domain.dto.TrxDTO;


public interface BillingService {
    void createClient(RegisterClientRequest clientRequest);
    void balanceAdd(BalanceClientRequest balanceClientRequest) throws ClientNotFoudException;
    void balanceWithdraw(BalanceClientRequest balanceClientRequest) throws ClientNotFoudException, NotEnoughMoneyException;
}
