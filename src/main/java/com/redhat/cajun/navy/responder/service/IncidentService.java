package com.redhat.cajun.navy.responder.service;


import com.redhat.cajun.navy.responder.Incident;
import com.redhat.cajun.navy.responder.IncidentStats;

import java.util.List;

public interface IncidentService {

    List<Incident> getIncidentMap();

    IncidentStats getIncidentStats();

}
