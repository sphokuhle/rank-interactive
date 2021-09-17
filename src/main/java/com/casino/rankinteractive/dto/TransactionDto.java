package com.casino.rankinteractive.dto;

import com.casino.rankinteractive.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author S'phokuhle on 9/16/2021
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class TransactionDto {
    private Long id;
    private Double balance;
    private String teapot;
    private String code;
    private String status;

    public TransactionDto(Transaction transaction) {
        if(transaction != null) {
            if(transaction.getId() != null) {
                this.id = transaction.getId();
                this.balance = transaction.getBalance();
            }
            if(transaction.getBalance() != null && transaction.getBalance() <= 0) {
                this.teapot = "teapot";
                this.code = "418";
            }
            this.status = transaction.getStatus();
        } else {
            log.info("Transaction is null");
        }
    }
}
