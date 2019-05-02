package com.redhat.cajun.navy.incident.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.redhat.cajun.navy.incident.dao.IncidentDao;
import com.redhat.cajun.navy.incident.message.IncidentReportedEvent;
import com.redhat.cajun.navy.incident.message.Message;
import com.redhat.cajun.navy.incident.model.IncidentStatus;
import com.redhat.cajun.navy.incident.model.Incident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class IncidentService {

    private static final Logger log = LoggerFactory.getLogger(IncidentService.class);

    @Autowired
    private KafkaTemplate<String, Message<?>> kafkaTemplate;

    @Autowired
    private IncidentDao incidentDao;

    @Value("${sender.destination.incident-reported-event}")
    private String destination;

    @Transactional
    public void sendIncidentReportedEventMessage(Incident incident) {

        String reportedIncidentId = UUID.randomUUID().toString();
        long reportedTimestamp = System.currentTimeMillis();

        com.redhat.cajun.navy.incident.entity.Incident incidentEntity =
                new com.redhat.cajun.navy.incident.entity.Incident.Builder()
                        .incidentId(reportedIncidentId)
                        .latitude(incident.getLat())
                        .longitude(incident.getLon())
                        .medicalNeeded(incident.isMedicalNeeded())
                        .numberOfPeople(incident.getNumberOfPeople())
                        .victimName(incident.getVictimName())
                        .victimPhoneNumber(incident.getVictimPhoneNumber())
                        .reportedTime(reportedTimestamp)
                        .status(IncidentStatus.REPORTED.name())
                        .build();

        incidentDao.create(incidentEntity);

        Message<IncidentReportedEvent> message = new Message.Builder<>("IncidentReportedEvent", "IncidentService",
                new IncidentReportedEvent.Builder(reportedIncidentId)
                        .lat(new BigDecimal(incident.getLat()))
                        .lon(new BigDecimal(incident.getLon()))
                        .medicalNeeded(incident.isMedicalNeeded())
                        .numberOfPeople(incident.getNumberOfPeople())
                        .timestamp(reportedTimestamp)
                        .build())
                .build();

        ListenableFuture<SendResult<String, Message<?>>> future = kafkaTemplate.send(destination, message.getBody().getId(), message);
        future.addCallback(
                result -> log.debug("Sent 'IncidentReportedEvent' message for incident " + message.getBody().getId()),
                ex -> log.error("Error sending 'IncidentReportedEvent' message for incident " + message.getBody().getId(), ex));

    }

    public Incident getIncident(String incidentId){
        return to(incidentDao.findByIncidentId(incidentId));
    }

    @Transactional
    public void updateIncident(Incident incident) {
        com.redhat.cajun.navy.incident.entity.Incident current = incidentDao.findByIncidentId(incident.getId());
        if (current == null) {
            log.warn("Incident with id '" + incident.getId() + "' not found in the database");
            return;
        }
        com.redhat.cajun.navy.incident.entity.Incident toUpdate = from(incident, current);
        try {
            incidentDao.merge(toUpdate);
        } catch (Exception e) {
            log.warn("Exception '" + e.getClass() + "' when updating Incident with id '" + incident.getId() + "'. Incident record is not updated.");
        }
    }

    @Transactional
    public List<Incident> incidents() {

        return incidentDao.findAll().stream().map(this::to).collect(Collectors.toList());

    }

    @Transactional
    public List<Incident> incidentsByStatus(String status) {

        return incidentDao.findByStatus(status).stream().map(this::to).collect(Collectors.toList());

    }

    @Transactional
    public List<Incident> incidentsByName(String name) {

        return incidentDao.findByName(name).stream().map(this::to).collect(Collectors.toList());

    }

    @Transactional
    public void reset() {
        incidentDao.deleteAll();
    }

    private com.redhat.cajun.navy.incident.entity.Incident from(Incident incident, com.redhat.cajun.navy.incident.entity.Incident current) {
        if (incident == null) {
            return null;
        }
        return new com.redhat.cajun.navy.incident.entity.Incident.Builder(current.getId(), current.getVersion())
                .incidentId(incident.getId())
                .latitude(incident.getLat() == null? current.getLatitude() : incident.getLat())
                .longitude(incident.getLon() == null? current.getLongitude() : incident.getLon())
                .medicalNeeded(incident.isMedicalNeeded() == null? current.isMedicalNeeded() : incident.isMedicalNeeded())
                .numberOfPeople(incident.getNumberOfPeople() == null ? current.getNumberOfPeople() : incident.getNumberOfPeople())
                .victimName(incident.getVictimName() == null? current.getVictimName() : incident.getVictimName())
                .victimPhoneNumber(incident.getVictimPhoneNumber() == null? current.getVictimPhoneNumber() : incident.getVictimPhoneNumber())
                .status(incident.getStatus() == null? current.getStatus() : incident.getStatus())
                .reportedTime(current.getTimestamp())
                .build();
    }

    private Incident to(com.redhat.cajun.navy.incident.entity.Incident r) {

        if (r == null) {
            return null;
        }

        return new Incident.Builder(r.getIncidentId())
                .lat(r.getLatitude())
                .lon(r.getLongitude())
                .medicalNeeded(r.isMedicalNeeded())
                .numberOfPeople(r.getNumberOfPeople())
                .victimName(r.getVictimName())
                .victimPhoneNumber(r.getVictimPhoneNumber())
                .status(r.getStatus())
                .timestamp(r.getTimestamp())
                .build();
    }

}
