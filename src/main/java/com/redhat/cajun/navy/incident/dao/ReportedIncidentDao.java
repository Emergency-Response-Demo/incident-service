package com.redhat.cajun.navy.incident.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.redhat.cajun.navy.incident.entity.ReportedIncident;
import org.springframework.stereotype.Component;

@Component
public class ReportedIncidentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(ReportedIncident reportedIncident) {
        entityManager.persist(reportedIncident);
    }

    @SuppressWarnings("unchecked")
    public ReportedIncident findByIncidentId(String incidentId) {
        if (incidentId == null || incidentId.isEmpty()) {
            return null;
        }
        List<ReportedIncident> reportedIncidents = entityManager.createQuery("SELECT r FROM ReportedIncident r WHERE r.incidentId = :incidentId")
                .setParameter("incidentId", incidentId)
                .getResultList();
        if (reportedIncidents.isEmpty()) {
            return null;
        }
        return reportedIncidents.get(0);
    }

    public ReportedIncident merge(ReportedIncident reportedIncident) {
        ReportedIncident r = entityManager.merge(reportedIncident);
        entityManager.flush();
        return r;
    }

    void deleteAll() {
        Query deleteAll = entityManager.createQuery("DELETE FROM ReportedIncident");
        deleteAll.executeUpdate();
    }

}
