package com.redhat.cajun.navy.incident.service;

import com.redhat.cajun.navy.incident.model.ReportedIncident;

public interface ReportedIncidentService {

    void sendIncidentReportedEventMessage(ReportedIncident incident);
}
