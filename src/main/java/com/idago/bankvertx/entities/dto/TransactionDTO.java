/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.entities.dto;

import com.idago.bankvertx.entities.dto.enums.TransactionType;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class TransactionDTO implements java.io.Serializable{
    private Integer depositAccout_ID;
    private Integer withdrawAccount_ID;
    private Double amount;      
    private TransactionType transactionType;
    
    public Integer getDepositAccout_ID() {
        return depositAccout_ID;
    }

    public void setDepositAccout_ID(Integer depositAccout_ID) {
        this.depositAccout_ID = depositAccout_ID;
    }

    public Integer getWithdrawAccount_ID() {
        return withdrawAccount_ID;
    }

    public void setWithdrawAccount_ID(Integer withdrawAccount_ID) {
        this.withdrawAccount_ID = withdrawAccount_ID;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionDTO setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }
}