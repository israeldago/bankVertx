/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.service;

import com.idago.bankvertx.dao.AppUserDAO;
import com.idago.bankvertx.dao.RoleDAO;
import com.idago.bankvertx.entities.db.AppUserDB;
import com.idago.bankvertx.entities.db.RoleDB;
import com.idago.bankvertx.entities.dto.AppUserDTO;
import com.idago.bankvertx.entities.dto.ResponseDTO;
import com.idago.bankvertx.entities.dto.RoleDTO;
import com.idago.bankvertx.service.gateway.AccessService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class BankAccessService implements AccessService<AppUserDTO> {

    private final AppUserDAO userDAO;
    private final RoleDAO roleDAO;

    public BankAccessService() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.idago_bankVertx_jar_1.0-SNAPSHOTPU");
        EntityManager entityManager = emf.createEntityManager();
        this.userDAO = new AppUserDAO(entityManager);
        this.roleDAO = new RoleDAO(entityManager);
    }

    @Override
    public ResponseDTO register(AppUserDTO dto) {
        ResponseDTO response = new ResponseDTO();
        Boolean isUsernameAlreadyAssigned = userDAO.findAll().anyMatch(user -> user.getUsername().equals(dto.getUsername()));
        if (isUsernameAlreadyAssigned) {
            return response.setRequestDone(false)
                    .setMessage("Username [ " + dto.getUsername() + " ] already taken");
        }

        AppUserDB userDB = userDAO.findByIdCardNumber(dto.getIdentityCardNumber());
        if (userDB != null) {
            return response.setRequestDone(false)
                    .setMessage("Duplicates not allow : User with IdentityCardNumber " + userDB.getIdentityCardNumber() + " already registered in system");
        }
        //finally register new appUser and return true if all checks are ok
        userDAO.create(mapToDB(dto));
        return response.setRequestDone(true)
                .setMessage("new app user registered");
    }

    @Override
    public ResponseDTO login(String username, String password) {
        AppUserDB userDB = userDAO.findByUsername(username);
        ResponseDTO response = new ResponseDTO();
        if (userDB == null) {
            return response.setRequestDone(false)
                    .setMessage("User not registered yet");
        } else {
            return userDB.getPassword().equals(password)
                    ? response.setRequestDone(true).setMessage("all checks ok - may log in system").setExpectedObject(mapToDTO(userDB))
                    : response.setRequestDone(false).setMessage("wrong password");
        }
    }

    private AppUserDB mapToDB(AppUserDTO dto) {
        String dtoRoleName = dto.getRole().getRoleName();
        ///first check user Role and if roleName inexistent in system, we'll register this new role
        if (roleDAO.findByRoleName(dtoRoleName) == null) {
            roleDAO.create(new RoleDB(dtoRoleName));
        }
        //now, we build and return the db obj     
        return new AppUserDB(dto.getLastName(), dto.getFirstName(), dto.getIdentityCardNumber(),
                dto.getBirthDate(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                dto.getUsername(), dto.getPassword(),
                roleDAO.findByRoleName(dtoRoleName));
    }

    private AppUserDTO mapToDTO(AppUserDB db) {
        return new AppUserDTO(db.getId(), db.getLastName(), db.getFirstName(), db.getIdentityCardNumber(),
                db.getBirthDate(), db.getRegisterDate(), db.getUsername(), db.getPassword(),
                new RoleDTO(db.getRole().getId(), db.getRole().getRoleName()));
    }

    ///////////SINGLETON//////////////////
    private static class SingletonHolder {
        private final static BankAccessService SINGLETON = new BankAccessService();
    }

    public static BankAccessService getInstance() {
        return SingletonHolder.SINGLETON;
    }
}
