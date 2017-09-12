/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.service.gateway;

import com.idago.bankvertx.entities.dto.ResponseDTO;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 * @param <T> represents the reference type of the class which is needed for this service
 */

public interface AccessService<T> {
    public ResponseDTO register(T entityToRegister);
    public ResponseDTO login(String username, String password);
}
