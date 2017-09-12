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
import com.idago.bankvertx.entities.db.RoleDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class RoleDAO implements Serializable {

    private EntityManager entityManager = null;
    
    public RoleDAO(EntityManager em) {
        this.entityManager = em;
    }    

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void create(RoleDB roleDB) {
        if (roleDB.getAppUserDBCollection() == null) {
            roleDB.setAppUserDBCollection(new ArrayList<AppUserDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<AppUserDB> attachedAppUserDBCollection = new ArrayList<AppUserDB>();
            for (AppUserDB appUserDBCollectionAppUserDBToAttach : roleDB.getAppUserDBCollection()) {
                appUserDBCollectionAppUserDBToAttach = em.getReference(appUserDBCollectionAppUserDBToAttach.getClass(), appUserDBCollectionAppUserDBToAttach.getId());
                attachedAppUserDBCollection.add(appUserDBCollectionAppUserDBToAttach);
            }
            roleDB.setAppUserDBCollection(attachedAppUserDBCollection);
            em.persist(roleDB);
            for (AppUserDB appUserDBCollectionAppUserDB : roleDB.getAppUserDBCollection()) {
                RoleDB oldRoleOfAppUserDBCollectionAppUserDB = appUserDBCollectionAppUserDB.getRole();
                appUserDBCollectionAppUserDB.setRole(roleDB);
                appUserDBCollectionAppUserDB = em.merge(appUserDBCollectionAppUserDB);
                if (oldRoleOfAppUserDBCollectionAppUserDB != null) {
                    oldRoleOfAppUserDBCollectionAppUserDB.getAppUserDBCollection().remove(appUserDBCollectionAppUserDB);
                    oldRoleOfAppUserDBCollectionAppUserDB = em.merge(oldRoleOfAppUserDBCollectionAppUserDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoleDB roleDB) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RoleDB persistentRoleDB = em.find(RoleDB.class, roleDB.getId());
            Collection<AppUserDB> appUserDBCollectionOld = persistentRoleDB.getAppUserDBCollection();
            Collection<AppUserDB> appUserDBCollectionNew = roleDB.getAppUserDBCollection();
            Collection<AppUserDB> attachedAppUserDBCollectionNew = new ArrayList<AppUserDB>();
            for (AppUserDB appUserDBCollectionNewAppUserDBToAttach : appUserDBCollectionNew) {
                appUserDBCollectionNewAppUserDBToAttach = em.getReference(appUserDBCollectionNewAppUserDBToAttach.getClass(), appUserDBCollectionNewAppUserDBToAttach.getId());
                attachedAppUserDBCollectionNew.add(appUserDBCollectionNewAppUserDBToAttach);
            }
            appUserDBCollectionNew = attachedAppUserDBCollectionNew;
            roleDB.setAppUserDBCollection(appUserDBCollectionNew);
            roleDB = em.merge(roleDB);
            for (AppUserDB appUserDBCollectionOldAppUserDB : appUserDBCollectionOld) {
                if (!appUserDBCollectionNew.contains(appUserDBCollectionOldAppUserDB)) {
                    appUserDBCollectionOldAppUserDB.setRole(null);
                    appUserDBCollectionOldAppUserDB = em.merge(appUserDBCollectionOldAppUserDB);
                }
            }
            for (AppUserDB appUserDBCollectionNewAppUserDB : appUserDBCollectionNew) {
                if (!appUserDBCollectionOld.contains(appUserDBCollectionNewAppUserDB)) {
                    RoleDB oldRoleOfAppUserDBCollectionNewAppUserDB = appUserDBCollectionNewAppUserDB.getRole();
                    appUserDBCollectionNewAppUserDB.setRole(roleDB);
                    appUserDBCollectionNewAppUserDB = em.merge(appUserDBCollectionNewAppUserDB);
                    if (oldRoleOfAppUserDBCollectionNewAppUserDB != null && !oldRoleOfAppUserDBCollectionNewAppUserDB.equals(roleDB)) {
                        oldRoleOfAppUserDBCollectionNewAppUserDB.getAppUserDBCollection().remove(appUserDBCollectionNewAppUserDB);
                        oldRoleOfAppUserDBCollectionNewAppUserDB = em.merge(oldRoleOfAppUserDBCollectionNewAppUserDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = roleDB.getId();
                if (findRoleDB(id) == null) {
                    throw new NonexistentEntityException("The roleDB with id " + id + " no longer exists.");
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
            RoleDB roleDB;
            try {
                roleDB = em.getReference(RoleDB.class, id);
                roleDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roleDB with id " + id + " no longer exists.", enfe);
            }
            Collection<AppUserDB> appUserDBCollection = roleDB.getAppUserDBCollection();
            for (AppUserDB appUserDBCollectionAppUserDB : appUserDBCollection) {
                appUserDBCollectionAppUserDB.setRole(null);
                appUserDBCollectionAppUserDB = em.merge(appUserDBCollectionAppUserDB);
            }
            em.remove(roleDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Stream<RoleDB> findAll() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoleDB.class));
            return (Stream<RoleDB>) em.createQuery(cq).getResultList().stream();
        } finally {
            em.close();
        }
    }

    public RoleDB findRoleDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoleDB.class, id);
        } finally {
            em.close();
        }
    }    
    
    public RoleDB findByRoleName(String roleName) {
        List<RoleDB> resultList = getEntityManager().createNamedQuery("RoleDB.findByRoleName", RoleDB.class)
                .setParameter("roleName", roleName)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
