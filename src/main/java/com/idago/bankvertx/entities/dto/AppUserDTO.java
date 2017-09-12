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
public class AppUserDTO implements java.io.Serializable{
    private Integer id;
    private String lastName;
    private String firstName;
    private String identityCardNumber;
    private String birthDate;
    private String registerDate;
    private String username;
    private String password;
    private RoleDTO role;
    private ResponseDTO responseDTO;
    
    public AppUserDTO() {
    }

    public AppUserDTO(String lastName, String firstName, String identityCardNumber, String birthDate, String username, String password, RoleDTO role) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.identityCardNumber = identityCardNumber;
        this.birthDate = birthDate;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AppUserDTO(Integer id, String lastName, String firstName, String identityCardNumber, String birthDate, String registerDate, String username, String password, RoleDTO role) {
        this(lastName, firstName, identityCardNumber, birthDate, username, password, role);
        this.registerDate = registerDate;
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

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public ResponseDTO getResponseDTO() {
        return responseDTO;
    }

    public AppUserDTO setResponseDTO(ResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
        return this;
    }

    @Override
    public String toString() {
        return "AppUserDTO{" + "id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", identityCardNumber=" + identityCardNumber + ", birthDate=" + birthDate + ", registerDate=" + registerDate + ", username=" + username + ", password=" + password + ", role=" + role + '}';
    }
}
