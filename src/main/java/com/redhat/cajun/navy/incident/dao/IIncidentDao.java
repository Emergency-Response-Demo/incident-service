package com.redhat.cajun.navy.incident.dao;

import java.util.List;

import com.redhat.cajun.navy.incident.entity.Incident;

public interface IIncidentDao {

    Incident create(Incident incident);

    Incident findByIncidentId(String incidentId);

    Incident merge(Incident incident);

    List<Incident> findAll();

    List<Incident> findByStatus(String status);

    List<Incident> findByName(String pattern);

    void deleteAll();

}
