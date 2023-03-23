package com.otus.billingservice.domain.response;


import com.otus.billingservice.error.ApplicationError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceClientResponse extends AResponse {

    private String username;
    private BigDecimal amount;

    public BalanceClientResponse(ApplicationError errorContent) {
        super(errorContent.getErrorCode(), errorContent.getMessage());
    }

}
