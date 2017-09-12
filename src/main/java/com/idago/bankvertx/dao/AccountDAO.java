/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.dao;

import com.idago.bankvertx.dao.exceptions.NonexistentEntityException;
import com.idago.bankvertx.entities.db.AccountDB;
import java.io.Serializable;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import com.idago.bankvertx.entities.db.AppUserDB;
import java.util.stream.Stream;
import javax.persistence.EntityManager;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class AccountDAO implements Serializable {

    private EntityManager entityManager = null;

    public AccountDAO(EntityManager em) {
        this.entityManager = em;
    }
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void create(AccountDB accountDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AppUserDB holder = accountDB.getHolder();
            if (holder != null) {
                holder = em.getReference(holder.getClass(), holder.getId());
                accountDB.setHolder(holder);
            }
            em.persist(accountDB);
            if (holder != null) {
                holder.getAccountDBCollection().add(accountDB);
                holder = em.merge(holder);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccountDB accountDB) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccountDB persistentAccountDB = em.find(AccountDB.class, accountDB.getId());
            AppUserDB holderOld = persistentAccountDB.getHolder();
            AppUserDB holderNew = accountDB.getHolder();
            if (holderNew != null) {
                holderNew = em.getReference(holderNew.getClass(), holderNew.getId());
                accountDB.setHolder(holderNew);
            }
            accountDB = em.merge(accountDB);
            if (holderOld != null && !holderOld.equals(holderNew)) {
                holderOld.getAccountDBCollection().remove(accountDB);
                holderOld = em.merge(holderOld);
            }
            if (holderNew != null && !holderNew.equals(holderOld)) {
                holderNew.getAccountDBCollection().add(accountDB);
                holderNew = em.merge(holderNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = accountDB.getId();
                if (findAccountDB(id) == null) {
                    throw new NonexistentEntityException("The accountDB with id " + id + " no longer exists.");
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
            AccountDB accountDB;
            try {
                accountDB = em.getReference(AccountDB.class, id);
                accountDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accountDB with id " + id + " no longer exists.", enfe);
            }
            AppUserDB holder = accountDB.getHolder();
            if (holder != null) {
                holder.getAccountDBCollection().remove(accountDB);
                holder = em.merge(holder);
            }
            em.remove(accountDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Stream<AccountDB> findAll() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccountDB.class));
            return (Stream<AccountDB>) em.createQuery(cq).getResultList().stream();
        } finally {
            em.close();
        }
    }

    public AccountDB findAccountDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccountDB.class, id);
        } finally {
            em.close();
        }
    }
}
