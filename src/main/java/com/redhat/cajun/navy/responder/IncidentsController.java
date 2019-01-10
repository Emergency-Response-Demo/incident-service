package com.redhat.cajun.navy.responder;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentsController {

    @RequestMapping("/map")
    public List<Incident> map() {

        ArrayList<Incident> incidents = new ArrayList<>();

        Incident incident1 = new Incident();
        incident1.setId("1");
        incident1.setLat(new BigDecimal("34.16877"));
        incident1.setLon(new BigDecimal("-77.87045"));
        incident1.setNumberOfPeople(4);
        Reporter reporter1 = new Reporter();
        reporter1.setFullName("John Smith");
        reporter1.setPhoneNumber("703-999-8336");
        reporter1.setId("1");
        reporter1.setReportTime(ZonedDateTime.parse("2019-01-09T21:40:09.131Z"));
        incident1.setReporter(reporter1);
        incidents.add(incident1);

         Incident incident2 = new Incident();
        incident2.setId("2");
        incident2.setLat(new BigDecimal("34.18323"));
        incident2.setLon(new BigDecimal("-77.84099"));
        incident2.setNumberOfPeople(4);
        Reporter reporter2 = new Reporter();
        reporter2.setFullName("John Smith");
        reporter2.setPhoneNumber("703-999-8336");
        reporter2.setId("2");
        reporter2.setReportTime(ZonedDateTime.parse("2019-01-09T21:50:09.131Z"));
        incident2.setReporter(reporter2);
        incidents.add(incident2);

        return incidents;
    }

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
