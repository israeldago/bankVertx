/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.entities.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
@Entity
@Table(name = "accounts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountDB.findAll", query = "SELECT a FROM AccountDB a")
    , @NamedQuery(name = "AccountDB.findById", query = "SELECT a FROM AccountDB a WHERE a.id = :id")
    , @NamedQuery(name = "AccountDB.findByAmount", query = "SELECT a FROM AccountDB a WHERE a.amount = :amount")
    , @NamedQuery(name = "AccountDB.findByCreationDate", query = "SELECT a FROM AccountDB a WHERE a.creationDate = :creationDate")
    , @NamedQuery(name = "AccountDB.findByIban", query = "SELECT a FROM AccountDB a WHERE a.iban = :iban")})
public class AccountDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Double amount;
    @Column(name = "creationDate")
    private String creationDate;
    @Column(name = "iban")
    private String iban;
    @JoinColumn(name = "holder", referencedColumnName = "id")
    @ManyToOne
    private AppUserDB holder;

    public AccountDB() {
    }

    public AccountDB(String iban, Double amount, String creationDate, AppUserDB holder) {
        this.iban = iban;
        this.amount = amount;
        this.creationDate = creationDate;
        this.holder = holder;
    }

    public AccountDB(Integer id, String iban, Double amount, String creationDate, AppUserDB holder) {
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

    public AccountDB setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public AccountDB setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public AccountDB setCreationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public AppUserDB getHolder() {
        return holder;
    }

    public AccountDB setHolder(AppUserDB holder) {
        this.holder = holder;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountDB)) {
            return false;
        }
        AccountDB other = (AccountDB) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.db.AccountDB[ id=" + id + " ]";
    }    
}
