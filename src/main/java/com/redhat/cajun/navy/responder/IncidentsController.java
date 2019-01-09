package com.redhat.cajun.navy.responder;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/responders")
public class IncidentsController {

    @RequestMapping("/stats")
    public IncidentStats stats() {

        IncidentStats incidentStats = new IncidentStats();

        incidentStats.setTotal(100);
        incidentStats.setActive(50);

        return incidentStats;
    }

}
