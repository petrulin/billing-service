package com.otus.billingservice.repository;

import com.otus.billingservice.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findClientByUserNameAndCurrency(String userName, Integer currency);
}
