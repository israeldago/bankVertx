/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.dao;

import com.idago.bankvertx.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import com.idago.bankvertx.entities.db.RoleDB;
import com.idago.bankvertx.entities.db.UserRequestDB;
import java.util.ArrayList;
import java.util.Collection;
import com.idago.bankvertx.entities.db.AccountDB;
import com.idago.bankvertx.entities.db.AppUserDB;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class AppUserDAO implements Serializable {
    
    private EntityManagerFactory emf = null;

    public AppUserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }

    public void create(AppUserDB appUserDB) {
        if (appUserDB.getUserRequestDBCollection() == null) {
            appUserDB.setUserRequestDBCollection(new ArrayList<UserRequestDB>());
        }
        if (appUserDB.getAccountDBCollection() == null) {
            appUserDB.setAccountDBCollection(new ArrayList<AccountDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RoleDB role = appUserDB.getRole();
            if (role != null) {
                role = em.getReference(role.getClass(), role.getId());
                appUserDB.setRole(role);
            }
            Collection<UserRequestDB> attachedUserRequestDBCollection = new ArrayList<UserRequestDB>();
            for (UserRequestDB userRequestDBCollectionUserRequestDBToAttach : appUserDB.getUserRequestDBCollection()) {
                userRequestDBCollectionUserRequestDBToAttach = em.getReference(userRequestDBCollectionUserRequestDBToAttach.getClass(), userRequestDBCollectionUserRequestDBToAttach.getId());
                attachedUserRequestDBCollection.add(userRequestDBCollectionUserRequestDBToAttach);
            }
            appUserDB.setUserRequestDBCollection(attachedUserRequestDBCollection);
            Collection<AccountDB> attachedAccountDBCollection = new ArrayList<AccountDB>();
            for (AccountDB accountDBCollectionAccountDBToAttach : appUserDB.getAccountDBCollection()) {
                accountDBCollectionAccountDBToAttach = em.getReference(accountDBCollectionAccountDBToAttach.getClass(), accountDBCollectionAccountDBToAttach.getId());
                attachedAccountDBCollection.add(accountDBCollectionAccountDBToAttach);
            }
            appUserDB.setAccountDBCollection(attachedAccountDBCollection);
            em.persist(appUserDB);
            if (role != null) {
                role.getAppUserDBCollection().add(appUserDB);
                role = em.merge(role);
            }
            for (UserRequestDB userRequestDBCollectionUserRequestDB : appUserDB.getUserRequestDBCollection()) {
                AppUserDB oldRequesterOfUserRequestDBCollectionUserRequestDB = userRequestDBCollectionUserRequestDB.getRequester();
                userRequestDBCollectionUserRequestDB.setRequester(appUserDB);
                userRequestDBCollectionUserRequestDB = em.merge(userRequestDBCollectionUserRequestDB);
                if (oldRequesterOfUserRequestDBCollectionUserRequestDB != null) {
                    oldRequesterOfUserRequestDBCollectionUserRequestDB.getUserRequestDBCollection().remove(userRequestDBCollectionUserRequestDB);
                    oldRequesterOfUserRequestDBCollectionUserRequestDB = em.merge(oldRequesterOfUserRequestDBCollectionUserRequestDB);
                }
            }
            for (AccountDB accountDBCollectionAccountDB : appUserDB.getAccountDBCollection()) {
                AppUserDB oldHolderOfAccountDBCollectionAccountDB = accountDBCollectionAccountDB.getHolder();
                accountDBCollectionAccountDB.setHolder(appUserDB);
                accountDBCollectionAccountDB = em.merge(accountDBCollectionAccountDB);
                if (oldHolderOfAccountDBCollectionAccountDB != null) {
                    oldHolderOfAccountDBCollectionAccountDB.getAccountDBCollection().remove(accountDBCollectionAccountDB);
                    oldHolderOfAccountDBCollectionAccountDB = em.merge(oldHolderOfAccountDBCollectionAccountDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AppUserDB appUserDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AppUserDB persistentAppUserDB = em.find(AppUserDB.class, appUserDB.getId());
            RoleDB roleOld = persistentAppUserDB.getRole();
            RoleDB roleNew = appUserDB.getRole();
            Collection<UserRequestDB> userRequestDBCollectionOld = persistentAppUserDB.getUserRequestDBCollection();
            Collection<UserRequestDB> userRequestDBCollectionNew = appUserDB.getUserRequestDBCollection();
            Collection<AccountDB> accountDBCollectionOld = persistentAppUserDB.getAccountDBCollection();
            Collection<AccountDB> accountDBCollectionNew = appUserDB.getAccountDBCollection();
            if (roleNew != null) {
                roleNew = em.getReference(roleNew.getClass(), roleNew.getId());
                appUserDB.setRole(roleNew);
            }
            Collection<UserRequestDB> attachedUserRequestDBCollectionNew = new ArrayList<UserRequestDB>();
            for (UserRequestDB userRequestDBCollectionNewUserRequestDBToAttach : userRequestDBCollectionNew) {
                userRequestDBCollectionNewUserRequestDBToAttach = em.getReference(userRequestDBCollectionNewUserRequestDBToAttach.getClass(), userRequestDBCollectionNewUserRequestDBToAttach.getId());
                attachedUserRequestDBCollectionNew.add(userRequestDBCollectionNewUserRequestDBToAttach);
            }
            userRequestDBCollectionNew = attachedUserRequestDBCollectionNew;
            appUserDB.setUserRequestDBCollection(userRequestDBCollectionNew);
            Collection<AccountDB> attachedAccountDBCollectionNew = new ArrayList<AccountDB>();
            for (AccountDB accountDBCollectionNewAccountDBToAttach : accountDBCollectionNew) {
                accountDBCollectionNewAccountDBToAttach = em.getReference(accountDBCollectionNewAccountDBToAttach.getClass(), accountDBCollectionNewAccountDBToAttach.getId());
                attachedAccountDBCollectionNew.add(accountDBCollectionNewAccountDBToAttach);
            }
            accountDBCollectionNew = attachedAccountDBCollectionNew;
            appUserDB.setAccountDBCollection(accountDBCollectionNew);
            appUserDB = em.merge(appUserDB);
            if (roleOld != null && !roleOld.equals(roleNew)) {
                roleOld.getAppUserDBCollection().remove(appUserDB);
                roleOld = em.merge(roleOld);
            }
            if (roleNew != null && !roleNew.equals(roleOld)) {
                roleNew.getAppUserDBCollection().add(appUserDB);
                roleNew = em.merge(roleNew);
            }
            for (UserRequestDB userRequestDBCollectionOldUserRequestDB : userRequestDBCollectionOld) {
                if (!userRequestDBCollectionNew.contains(userRequestDBCollectionOldUserRequestDB)) {
                    userRequestDBCollectionOldUserRequestDB.setRequester(null);
                    userRequestDBCollectionOldUserRequestDB = em.merge(userRequestDBCollectionOldUserRequestDB);
                }
            }
            for (UserRequestDB userRequestDBCollectionNewUserRequestDB : userRequestDBCollectionNew) {
                if (!userRequestDBCollectionOld.contains(userRequestDBCollectionNewUserRequestDB)) {
                    AppUserDB oldRequesterOfUserRequestDBCollectionNewUserRequestDB = userRequestDBCollectionNewUserRequestDB.getRequester();
                    userRequestDBCollectionNewUserRequestDB.setRequester(appUserDB);
                    userRequestDBCollectionNewUserRequestDB = em.merge(userRequestDBCollectionNewUserRequestDB);
                    if (oldRequesterOfUserRequestDBCollectionNewUserRequestDB != null && !oldRequesterOfUserRequestDBCollectionNewUserRequestDB.equals(appUserDB)) {
                        oldRequesterOfUserRequestDBCollectionNewUserRequestDB.getUserRequestDBCollection().remove(userRequestDBCollectionNewUserRequestDB);
                        oldRequesterOfUserRequestDBCollectionNewUserRequestDB = em.merge(oldRequesterOfUserRequestDBCollectionNewUserRequestDB);
                    }
                }
            }
            for (AccountDB accountDBCollectionOldAccountDB : accountDBCollectionOld) {
                if (!accountDBCollectionNew.contains(accountDBCollectionOldAccountDB)) {
                    accountDBCollectionOldAccountDB.setHolder(null);
                    accountDBCollectionOldAccountDB = em.merge(accountDBCollectionOldAccountDB);
                }
            }
            for (AccountDB accountDBCollectionNewAccountDB : accountDBCollectionNew) {
                if (!accountDBCollectionOld.contains(accountDBCollectionNewAccountDB)) {
                    AppUserDB oldHolderOfAccountDBCollectionNewAccountDB = accountDBCollectionNewAccountDB.getHolder();
                    accountDBCollectionNewAccountDB.setHolder(appUserDB);
                    accountDBCollectionNewAccountDB = em.merge(accountDBCollectionNewAccountDB);
                    if (oldHolderOfAccountDBCollectionNewAccountDB != null && !oldHolderOfAccountDBCollectionNewAccountDB.equals(appUserDB)) {
                        oldHolderOfAccountDBCollectionNewAccountDB.getAccountDBCollection().remove(accountDBCollectionNewAccountDB);
                        oldHolderOfAccountDBCollectionNewAccountDB = em.merge(oldHolderOfAccountDBCollectionNewAccountDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = appUserDB.getId();
                if (findAppUserDB(id) == null) {
                    throw new NonexistentEntityException("The appUserDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AppUserDB appUserDB;
            try {
                appUserDB = em.getReference(AppUserDB.class, id);
                appUserDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The appUserDB with id " + id + " no longer exists.", enfe);
            }
            RoleDB role = appUserDB.getRole();
            if (role != null) {
                role.getAppUserDBCollection().remove(appUserDB);
                role = em.merge(role);
            }
            Collection<UserRequestDB> userRequestDBCollection = appUserDB.getUserRequestDBCollection();
            for (UserRequestDB userRequestDBCollectionUserRequestDB : userRequestDBCollection) {
                userRequestDBCollectionUserRequestDB.setRequester(null);
                userRequestDBCollectionUserRequestDB = em.merge(userRequestDBCollectionUserRequestDB);
            }
            Collection<AccountDB> accountDBCollection = appUserDB.getAccountDBCollection();
            for (AccountDB accountDBCollectionAccountDB : accountDBCollection) {
                accountDBCollectionAccountDB.setHolder(null);
                accountDBCollectionAccountDB = em.merge(accountDBCollectionAccountDB);
            }
            em.remove(appUserDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Stream<AppUserDB> findAll() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AppUserDB.class));
            return (Stream<AppUserDB>) em.createQuery(cq).getResultList().stream();
        } finally {
            em.close();
        }
    }

    public AppUserDB findAppUserDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AppUserDB.class, id);
        } finally {
            em.close();
        }
    }    
    
    public AppUserDB findByIdCardNumber(String idCardNumber) {
        return findOneByNamedQuery("AppUserDB.findByIdentityCardNumber", "identityCardNumber", idCardNumber);
    }

    public AppUserDB findByUsername(String username) {
        return findOneByNamedQuery("AppUserDB.findByUsername", "username", username);
    }
    
    private AppUserDB findOneByNamedQuery(final String NAMED_QUERY, final String PARAM_NAME, final String PARAM_VALUE) {
        List<AppUserDB> resultList = getEntityManager().createNamedQuery(NAMED_QUERY, AppUserDB.class)
                .setParameter(PARAM_NAME, PARAM_VALUE)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
