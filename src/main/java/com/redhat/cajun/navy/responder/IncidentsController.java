package com.redhat.cajun.navy.responder;

import com.redhat.cajun.navy.responder.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentsController {

    @Autowired
    private IncidentService incidentService;

    @RequestMapping("/map")
    public List<Incident> map() {

        List<Incident> incidents = incidentService.getIncidentMap();

        return incidents;
    }

    @RequestMapping("/stats")
    public IncidentStats stats() {

        IncidentStats response = incidentService.getIncidentStats();

        return response;
    }

}
