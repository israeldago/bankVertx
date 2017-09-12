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
@Table(name = "roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RoleDB.findAll", query = "SELECT r FROM RoleDB r")
    , @NamedQuery(name = "RoleDB.findById", query = "SELECT r FROM RoleDB r WHERE r.id = :id")
    , @NamedQuery(name = "RoleDB.findByRoleName", query = "SELECT r FROM RoleDB r WHERE r.roleName = :roleName")})
public class RoleDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "roleName")
    private String roleName;
    @OneToMany(mappedBy = "role")
    private Collection<AppUserDB> appUserDBCollection;

    public RoleDB() {
    }

    public RoleDB(String roleName) {
        this.roleName = roleName;
    }

    public RoleDB(Integer id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @XmlTransient
    public Collection<AppUserDB> getAppUserDBCollection() {
        return appUserDBCollection;
    }

    public void setAppUserDBCollection(Collection<AppUserDB> appUserDBCollection) {
        this.appUserDBCollection = appUserDBCollection;
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
        if (!(object instanceof RoleDB)) {
            return false;
        }
        RoleDB other = (RoleDB) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.db.RoleDB[ id=" + id + " ]";
    }    
}
