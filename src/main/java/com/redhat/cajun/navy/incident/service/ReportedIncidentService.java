package com.redhat.cajun.navy.incident.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.redhat.cajun.navy.incident.dao.ReportedIncidentDao;
import com.redhat.cajun.navy.incident.message.IncidentReportedEvent;
import com.redhat.cajun.navy.incident.message.Message;
import com.redhat.cajun.navy.incident.model.IncidentStatus;
import com.redhat.cajun.navy.incident.model.ReportedIncident;
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
public class ReportedIncidentService {

    private static final Logger log = LoggerFactory.getLogger(ReportedIncidentService.class);

    @Autowired
    private KafkaTemplate<String, Message<?>> kafkaTemplate;

    @Autowired
    private ReportedIncidentDao reportedIncidentDao;

    @Value("${sender.destination.incident-reported-event}")
    private String destination;

    @Transactional
    public void sendIncidentReportedEventMessage(ReportedIncident incident) {

        String reportedIncidentId = UUID.randomUUID().toString();
        long reportedTimestamp = System.currentTimeMillis();

        com.redhat.cajun.navy.incident.entity.ReportedIncident reportedIncidentEntity =
                new com.redhat.cajun.navy.incident.entity.ReportedIncident.Builder()
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

        reportedIncidentDao.create(reportedIncidentEntity);

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

    @Transactional
    public void updateIncident(ReportedIncident incident) {
        com.redhat.cajun.navy.incident.entity.ReportedIncident current = reportedIncidentDao.findByIncidentId(incident.getId());
        if (current == null) {
            log.warn("ReportedIncident with id '" + incident.getId() + "' not found in the database");
            return;
        }
        com.redhat.cajun.navy.incident.entity.ReportedIncident toUpdate = from(incident, current);
        try {
            reportedIncidentDao.merge(toUpdate);
        } catch (Exception e) {
            log.warn("Exception '" + e.getClass() + "' when updating ReportedIncident with id '" + incident.getId() + "'. ReportedIncident record is not updated.");
        }
    }

    @Transactional
    public List<ReportedIncident> incidents() {

        return reportedIncidentDao.findAll().stream().map(this::to).collect(Collectors.toList());

    }

    private com.redhat.cajun.navy.incident.entity.ReportedIncident from(ReportedIncident incident, com.redhat.cajun.navy.incident.entity.ReportedIncident current) {
        if (incident == null) {
            return null;
        }
        return new com.redhat.cajun.navy.incident.entity.ReportedIncident.Builder(current.getId(), current.getVersion())
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

    private ReportedIncident to(com.redhat.cajun.navy.incident.entity.ReportedIncident r) {

        if (r == null) {
            return null;
        }

        return new ReportedIncident.Builder(r.getIncidentId())
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
