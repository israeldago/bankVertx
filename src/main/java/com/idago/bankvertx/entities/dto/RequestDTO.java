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
public class RequestDTO implements java.io.Serializable{
    private Integer id;
    private String action;
    private String description;
    private AppUserDTO requester;
    
    public RequestDTO() {
    }
    
    public RequestDTO(String action, AppUserDTO requester) {
        this.action = action;
        this.requester = requester;
    }

    public RequestDTO(String action, String description, AppUserDTO requester) {
        this(action, requester);
        this.description = description;
    }

    public RequestDTO(Integer id, String action, String description, String status, AppUserDTO requester) {
        this(action, description, requester);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public RequestDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getAction() {
        return action;
    }

    public RequestDTO setAction(String action) {
        this.action = action;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RequestDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public AppUserDTO getRequester() {
        return requester;
    }

    public RequestDTO setRequester(AppUserDTO requester) {
        this.requester = requester;
        return this;
    }

    @Override
    public String toString() {
        return "UserRequestDTO{" + "id=" + id + ", action=" + action + ", description=" + description + ", requester=" + requester + '}';
    }
}
