package com.redhat.cajun.navy.responder;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/responders")
public class IncidentsController {

    @RequestMapping("/stats")
    public IncidentStats stats() {

        IncidentStats incidentStats = new IncidentStats();

        incidentStats.setCancelled(10);
        incidentStats.setClaimed(20);
        incidentStats.setPickedUp(30);
        incidentStats.setRescued(40);
        incidentStats.setRequested(50);

        return incidentStats;
    }

}
