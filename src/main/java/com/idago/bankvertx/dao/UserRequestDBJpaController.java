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
import com.idago.bankvertx.entities.db.AppUserDB;
import com.idago.bankvertx.entities.db.UserRequestDB;
import java.util.stream.Stream;
import javax.persistence.EntityManager;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class UserRequestDBJpaController implements Serializable {

    private EntityManager entityManager = null;
    
    public UserRequestDBJpaController(EntityManager em) {
        this.entityManager = em;
    }
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void create(UserRequestDB userRequestDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AppUserDB requester = userRequestDB.getRequester();
            if (requester != null) {
                requester = em.getReference(requester.getClass(), requester.getId());
                userRequestDB.setRequester(requester);
            }
            em.persist(userRequestDB);
            if (requester != null) {
                requester.getUserRequestDBCollection().add(userRequestDB);
                requester = em.merge(requester);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserRequestDB userRequestDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserRequestDB persistentUserRequestDB = em.find(UserRequestDB.class, userRequestDB.getId());
            AppUserDB requesterOld = persistentUserRequestDB.getRequester();
            AppUserDB requesterNew = userRequestDB.getRequester();
            if (requesterNew != null) {
                requesterNew = em.getReference(requesterNew.getClass(), requesterNew.getId());
                userRequestDB.setRequester(requesterNew);
            }
            userRequestDB = em.merge(userRequestDB);
            if (requesterOld != null && !requesterOld.equals(requesterNew)) {
                requesterOld.getUserRequestDBCollection().remove(userRequestDB);
                requesterOld = em.merge(requesterOld);
            }
            if (requesterNew != null && !requesterNew.equals(requesterOld)) {
                requesterNew.getUserRequestDBCollection().add(userRequestDB);
                requesterNew = em.merge(requesterNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userRequestDB.getId();
                if (findUserRequestDB(id) == null) {
                    throw new NonexistentEntityException("The userRequestDB with id " + id + " no longer exists.");
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
            UserRequestDB userRequestDB;
            try {
                userRequestDB = em.getReference(UserRequestDB.class, id);
                userRequestDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userRequestDB with id " + id + " no longer exists.", enfe);
            }
            AppUserDB requester = userRequestDB.getRequester();
            if (requester != null) {
                requester.getUserRequestDBCollection().remove(userRequestDB);
                requester = em.merge(requester);
            }
            em.remove(userRequestDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private Stream<UserRequestDB> findAll() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserRequestDB.class));
            return (Stream<UserRequestDB>) em.createQuery(cq).getResultList().stream();
        } finally {
            em.close();
        }
    }

    public UserRequestDB findUserRequestDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserRequestDB.class, id);
        } finally {
            em.close();
        }
    }    
}
