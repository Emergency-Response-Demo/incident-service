package com.redhat.cajun.navy.incident.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.redhat.cajun.navy.incident.entity.Incident;
import org.springframework.stereotype.Component;

@Component
public class IncidentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(Incident incident) {
        entityManager.persist(incident);
    }

    @SuppressWarnings("unchecked")
    public Incident findByIncidentId(String incidentId) {
        if (incidentId == null || incidentId.isEmpty()) {
            return null;
        }
        List<Incident> incidents = entityManager.createQuery("SELECT r FROM Incident r WHERE r.incidentId = :incidentId")
                .setParameter("incidentId", incidentId)
                .getResultList();
        if (incidents.isEmpty()) {
            return null;
        }
        return incidents.get(0);
    }

    public Incident merge(Incident incident) {
        Incident r = entityManager.merge(incident);
        entityManager.flush();
        return r;
    }

    @SuppressWarnings("unchecked")
    public List<Incident> findAll() {
        return entityManager.createQuery("SELECT r from Incident r").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Incident> findByStatus(String status) {
        return entityManager.createQuery("SELECT r from Incident r WHERE r.status = :status")
                .setParameter("status", status.toUpperCase()).getResultList();
    }

    void deleteAll() {
        Query deleteAll = entityManager.createQuery("DELETE FROM Incident");
        deleteAll.executeUpdate();
    }

}
