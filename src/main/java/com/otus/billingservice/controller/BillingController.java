package com.otus.billingservice.controller;


import com.otus.billingservice.domain.request.BalanceClientRequest;
import com.otus.billingservice.domain.request.RegisterClientRequest;
import com.otus.billingservice.domain.response.SimpeResponse;
import com.otus.billingservice.service.BillingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/billing-service/api/v1/internal/")
@RestController
public class BillingController {

    private final ModelMapper modelMapper;

    private final BillingService bs;

    public BillingController(BillingService bs) {
        this.bs = bs;
        this.modelMapper = new ModelMapper();
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<SimpeResponse> newClient(@RequestBody RegisterClientRequest client) {
        try {
            bs.createClient(client);
            return ResponseEntity.ok(new SimpeResponse("OK", ""));
        } catch (Exception ex) {
            return ResponseEntity.ok(new SimpeResponse("ERROR", ex.getLocalizedMessage()));
        }
    }

    @PostMapping(path = "/balance/add")
    public ResponseEntity<SimpeResponse> balanceAdd(@RequestBody BalanceClientRequest balanceClientRequest) {
        try {
            bs.balanceAdd(balanceClientRequest);
            return ResponseEntity.ok(new SimpeResponse("OK", ""));
        } catch (Exception ex) {
            return ResponseEntity.ok(new SimpeResponse("ERROR", ex.getLocalizedMessage()));
        }
    }

    @PostMapping(path = "/balance/withdraw")
    public ResponseEntity<SimpeResponse> balanceWithdraw(@RequestBody BalanceClientRequest balanceClientRequest) {
        try {
            bs.balanceWithdraw(balanceClientRequest);
            return ResponseEntity.ok(new SimpeResponse("OK", ""));
        } catch (Exception ex) {
            return ResponseEntity.ok(new SimpeResponse("ERROR", ex.getLocalizedMessage()));
        }
    }

}
