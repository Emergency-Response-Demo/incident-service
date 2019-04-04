package com.redhat.cajun.navy.incident.listener;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.redhat.cajun.navy.incident.message.Message;
import com.redhat.cajun.navy.incident.message.UpdateIncidentCommand;
import com.redhat.cajun.navy.incident.model.ReportedIncident;
import com.redhat.cajun.navy.incident.service.ReportedIncidentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

public class UpdateIncidentCommandListener {

    private final static Logger log = LoggerFactory.getLogger(UpdateIncidentCommandListener.class);

    private static final String UPDATE_INCIDENT_COMMAND = "UpdateIncidentCommand";
    private static final String[] ACCEPTED_MESSAGE_TYPES = {UPDATE_INCIDENT_COMMAND};

    @Autowired
    private ReportedIncidentService reportedIncidentService;

    @KafkaListener(topics = "${listener.destination.update-responder-command}")
    public void processMessage(@Payload String messageAsJson) {

        acceptMessageType(messageAsJson).ifPresent(m -> processUpdateIncidentCommand(messageAsJson));
    }

    private void processUpdateIncidentCommand(String messageAsJson) {

        Message<UpdateIncidentCommand> message;
        try {
            message = new ObjectMapper().readValue(messageAsJson, new TypeReference<Message<UpdateIncidentCommand>>() {});
            ReportedIncident incident = message.getBody().getIncident();

            log.debug("Processing '" + UPDATE_INCIDENT_COMMAND + "' message for responder '" + incident.getId() + "'");
            reportedIncidentService.updateIncident(incident);
        } catch (Exception e) {
            log.error("Error processing msg " + messageAsJson, e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Optional<String> acceptMessageType(String messageAsJson) {
        try {
            String messageType = JsonPath.read(messageAsJson, "$.messageType");
            if (Arrays.asList(ACCEPTED_MESSAGE_TYPES).contains(messageType)) {
                return Optional.of(messageType);
            }
            log.debug("Message with type '" + messageType + "' is ignored");
        } catch (Exception e) {
            log.warn("Unexpected message without 'messageType' field.");
        }
        return Optional.empty();
    }

}
