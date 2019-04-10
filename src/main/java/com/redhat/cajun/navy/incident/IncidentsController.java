package com.redhat.cajun.navy.incident;

import java.util.List;

import com.redhat.cajun.navy.incident.model.ReportedIncident;
import com.redhat.cajun.navy.incident.service.IncidentService;
import com.redhat.cajun.navy.incident.service.ReportedIncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incidents")
public class IncidentsController {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private ReportedIncidentService reportedIncidentService;

    @RequestMapping("/map")
    public List<Incident> map() {

        return incidentService.getIncidentMap();

    }

    @RequestMapping("/stats")
    public IncidentStats stats() {

        return incidentService.getIncidentStats();

    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity reportIncident(@RequestBody ReportedIncident incident) {

        reportedIncidentService.sendIncidentReportedEventMessage(incident);

        return new ResponseEntity(HttpStatus.CREATED);

    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportedIncident>> incidents() {
        return new ResponseEntity<>(reportedIncidentService.incidents(), HttpStatus.OK);
    }

}
