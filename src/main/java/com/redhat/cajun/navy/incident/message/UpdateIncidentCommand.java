package com.redhat.cajun.navy.incident.message;

import com.redhat.cajun.navy.incident.model.ReportedIncident;

public class UpdateIncidentCommand {

    private ReportedIncident incident;

    public ReportedIncident getIncident() {
        return incident;
    }
}
