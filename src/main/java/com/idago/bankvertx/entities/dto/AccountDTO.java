/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.entities.dto;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class AccountDTO implements java.io.Serializable{
    private Integer id;
    private String iban;
    private Double amount;
    private String creationDate;
    private AppUserDTO holder;
    
    public AccountDTO() {
    }

    public AccountDTO(String iban, Double amount, String creationDate, AppUserDTO holder) {
        this.iban = iban;
        this.amount = amount;
        this.creationDate = creationDate;
        this.holder = holder;
    }

    public AccountDTO(Integer id, String iban, Double amount, String creationDate, AppUserDTO holder) {
        this(iban, amount, creationDate, holder);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public AppUserDTO getHolder() {
        return holder;
    }

    public void setHolder(AppUserDTO holder) {
        this.holder = holder;
    }

    @Override
    public String toString() {
        return "AccountDTO{" + "id=" + id + ", iban=" + iban + ", amount=" + amount + ", creationDate=" + creationDate + ", holder=" + holder + '}';
    }
}
