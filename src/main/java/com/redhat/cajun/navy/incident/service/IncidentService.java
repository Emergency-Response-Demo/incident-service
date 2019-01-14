package com.redhat.cajun.navy.incident.service;


import com.redhat.cajun.navy.incident.Incident;
import com.redhat.cajun.navy.incident.IncidentStats;

import java.util.List;

public interface IncidentService {

    List<Incident> getIncidentMap();

    IncidentStats getIncidentStats();

}
