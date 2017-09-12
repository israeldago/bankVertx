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
public class ResponseDTO implements java.io.Serializable{
    private Integer id;
    private String message;
    private Boolean requestDone;
    private Object expectedObject;
    private RequestDTO initialRequest;

    public ResponseDTO() {
    }
    
    public ResponseDTO(String message, Boolean requestDone, RequestDTO initialRequest) {
        this.message = message;
        this.requestDone = requestDone;
        this.initialRequest = initialRequest;
    }

    public ResponseDTO(Integer id, String message, Boolean requestDone, RequestDTO initialRequest) {
        this(message, requestDone, initialRequest);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public ResponseDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Boolean isRequestDone() {
        return requestDone;
    }

    public ResponseDTO setRequestDone(Boolean requestDone) {
        this.requestDone = requestDone;
        return this;
    }    

    public Object getExpectedObject() {
        return expectedObject;
    }

    public ResponseDTO setExpectedObject(Object expectedObject) {
        this.expectedObject = expectedObject;
        return this;
    }
    
    public RequestDTO getInitialRequest() {
        return initialRequest;
    }

    public ResponseDTO setInitialRequest(RequestDTO initialRequest) {
        this.initialRequest = initialRequest;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" + "id=" + id + ", message=" + message + ", requestAccomplish=" + requestDone + ", initialRequest=" + initialRequest + '}';
    }
}
