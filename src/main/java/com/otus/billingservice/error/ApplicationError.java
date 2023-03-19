package com.otus.billingservice.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApplicationError {

    SUCCESS("", 0),
    CLIENT_NOT_FOUND("Client not found", 2000),
    NOT_ENOUGH_MONEY("Not enough money", 2001),
    INTERNAL_ERROR("Internal error", -9000);

    private final String message;
    private final int errorCode;
}
