/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.entities.db;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
@Entity
@Table(name = "app_users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppUserDB.findAll", query = "SELECT a FROM AppUserDB a")
    , @NamedQuery(name = "AppUserDB.findById", query = "SELECT a FROM AppUserDB a WHERE a.id = :id")
    , @NamedQuery(name = "AppUserDB.findByBirthDate", query = "SELECT a FROM AppUserDB a WHERE a.birthDate = :birthDate")
    , @NamedQuery(name = "AppUserDB.findByFirstName", query = "SELECT a FROM AppUserDB a WHERE a.firstName = :firstName")
    , @NamedQuery(name = "AppUserDB.findByIdentityCardNumber", query = "SELECT a FROM AppUserDB a WHERE a.identityCardNumber = :identityCardNumber")
    , @NamedQuery(name = "AppUserDB.findByLastName", query = "SELECT a FROM AppUserDB a WHERE a.lastName = :lastName")
    , @NamedQuery(name = "AppUserDB.findByPassword", query = "SELECT a FROM AppUserDB a WHERE a.password = :password")
    , @NamedQuery(name = "AppUserDB.findByRegisterDate", query = "SELECT a FROM AppUserDB a WHERE a.registerDate = :registerDate")
    , @NamedQuery(name = "AppUserDB.findByUsername", query = "SELECT a FROM AppUserDB a WHERE a.username = :username")})
public class AppUserDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "birthDate")
    private String birthDate;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "identityCardNumber")
    private String identityCardNumber;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "password")
    private String password;
    @Column(name = "registerDate")
    private String registerDate;
    @Column(name = "username")
    private String username;
    @JoinColumn(name = "role", referencedColumnName = "id")
    @ManyToOne
    private RoleDB role;
    @OneToMany(mappedBy = "requester")
    private Collection<UserRequestDB> userRequestDBCollection;
    @OneToMany(mappedBy = "holder")
    private Collection<AccountDB> accountDBCollection;

    public AppUserDB() {
    }

    public AppUserDB(String lastName, String firstName, String identityCardNumber, String birthDate, String registerDate, String username, String password, RoleDB role) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.identityCardNumber = identityCardNumber;
        this.birthDate = birthDate;
        this.registerDate = registerDate;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AppUserDB(Integer id, String lastName, String firstName, String identityCardNumber, String birthDate, String registerDate, String username, String password, RoleDB role) {
        this(lastName, firstName, identityCardNumber, birthDate, registerDate, username, password, role);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleDB getRole() {
        return role;
    }

    public void setRole(RoleDB role) {
        this.role = role;
    }

    @XmlTransient
    public Collection<UserRequestDB> getUserRequestDBCollection() {
        return userRequestDBCollection;
    }

    public void setUserRequestDBCollection(Collection<UserRequestDB> userRequestDBCollection) {
        this.userRequestDBCollection = userRequestDBCollection;
    }

    @XmlTransient
    public Collection<AccountDB> getAccountDBCollection() {
        return accountDBCollection;
    }

    public void setAccountDBCollection(Collection<AccountDB> accountDBCollection) {
        this.accountDBCollection = accountDBCollection;
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
        if (!(object instanceof AppUserDB)) {
            return false;
        }
        AppUserDB other = (AppUserDB) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.db.AppUserDB[ id=" + id + " ]";
    }    
}
