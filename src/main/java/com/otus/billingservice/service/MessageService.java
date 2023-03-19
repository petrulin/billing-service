package com.otus.billingservice.service;

import com.otus.billingservice.entity.Message;


public interface MessageService {

    Message save(Message user);
    Message findById(String msgId);

}
