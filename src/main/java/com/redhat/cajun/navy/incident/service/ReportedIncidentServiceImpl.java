package com.redhat.cajun.navy.incident.service;

import java.math.BigDecimal;
import java.util.UUID;

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
public class ReportedIncidentServiceImpl implements ReportedIncidentService {

    private static final Logger log = LoggerFactory.getLogger(ReportedIncidentServiceImpl.class);

    @Autowired
    private KafkaTemplate<String, Message<?>> kafkaTemplate;

    @Autowired
    private ReportedIncidentDao reportedIncidentDao;

    @Value("${sender.destination.incident-reported-event}")
    private String destination;

    @Override
    @Transactional
    public void sendIncidentReportedEventMessage(ReportedIncident incident) {

        String reportedIncidentId = UUID.randomUUID().toString();

        com.redhat.cajun.navy.incident.entity.ReportedIncident reportedIncidentEntity =
                new com.redhat.cajun.navy.incident.entity.ReportedIncident.Builder()
                        .incidentId(reportedIncidentId)
                        .latitude(incident.getLat())
                        .longitude(incident.getLon())
                        .medicalNeeded(incident.isMedicalNeeded())
                        .numberOfPeople(incident.getNumberOfPeople())
                        .victimName(incident.getVictimName())
                        .victimPhoneNumber(incident.getVictimPhoneNumber())
                        .timestamp(incident.getTimestamp())
                        .status(IncidentStatus.REPORTED.name())
                        .build();

        reportedIncidentDao.create(reportedIncidentEntity);

        Message<IncidentReportedEvent> message = new Message.Builder<>("IncidentReportedEvent", "IncidentService",
                new IncidentReportedEvent.Builder(reportedIncidentId    )
                        .lat(new BigDecimal(incident.getLat()))
                        .lon(new BigDecimal(incident.getLon()))
                        .medicalNeeded(incident.isMedicalNeeded())
                        .numberOfPeople(incident.getNumberOfPeople())
                        .timestamp(incident.getTimestamp())
                        .build())
                .build();

        ListenableFuture<SendResult<String, Message<?>>> future = kafkaTemplate.send(destination, message.getBody().getId(), message);
        future.addCallback(
                result -> log.debug("Sent 'IncidentReportedEvent' message for incident " + message.getBody().getId()),
                ex -> log.error("Error sending 'IncidentReportedEvent' message for incident " + message.getBody().getId(), ex));

    }
}
