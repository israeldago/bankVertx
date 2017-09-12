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
public class RoleDTO implements java.io.Serializable{
    private Integer id;
    private String roleName;

    public RoleDTO(String roleName) {
        this.roleName = roleName;
    }

    public RoleDTO(Integer id, String roleName) {
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

    @Override
    public String toString() {
        return "RoleDTO{" + "id=" + id + ", roleName=" + roleName + '}';
    }
}
