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
@Table(name = "user_requests")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserRequestDB.findAll", query = "SELECT u FROM UserRequestDB u")
    , @NamedQuery(name = "UserRequestDB.findById", query = "SELECT u FROM UserRequestDB u WHERE u.id = :id")
    , @NamedQuery(name = "UserRequestDB.findByDescription", query = "SELECT u FROM UserRequestDB u WHERE u.description = :description")
    , @NamedQuery(name = "UserRequestDB.findByStatus", query = "SELECT u FROM UserRequestDB u WHERE u.status = :status")
    , @NamedQuery(name = "UserRequestDB.findByTask", query = "SELECT u FROM UserRequestDB u WHERE u.task = :task")})
public class UserRequestDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;
    @Column(name = "task")
    private String task;
    @JoinColumn(name = "requester", referencedColumnName = "id")
    @ManyToOne
    private AppUserDB requester;

    public UserRequestDB() {
    }

    public UserRequestDB(String task, AppUserDB requester) {
        this.task = task;
        this.requester = requester;
    }

    public UserRequestDB(String task, String description, AppUserDB requester) {
        this(task, requester);
        this.description = description;
    }

    public UserRequestDB(Integer id, String task, String description, String status, AppUserDB requester) {
        this(task, description, requester);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppUserDB getRequester() {
        return requester;
    }

    public void setRequester(AppUserDB requester) {
        this.requester = requester;
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
        if (!(object instanceof UserRequestDB)) {
            return false;
        }
        UserRequestDB other = (UserRequestDB) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.db.UserRequestDB[ id=" + id + " ]";
    }    
}
