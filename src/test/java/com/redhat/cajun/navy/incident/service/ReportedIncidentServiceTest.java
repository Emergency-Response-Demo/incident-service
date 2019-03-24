package com.redhat.cajun.navy.incident.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.math.BigDecimal;

import com.redhat.cajun.navy.incident.dao.ReportedIncidentDao;
import com.redhat.cajun.navy.incident.message.IncidentReportedEvent;
import com.redhat.cajun.navy.incident.message.Message;
import com.redhat.cajun.navy.incident.model.IncidentStatus;
import com.redhat.cajun.navy.incident.model.ReportedIncident;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;

public class ReportedIncidentServiceTest {

    @Mock
    private KafkaTemplate<String, Message<?>> kafkaTemplate;

    @Mock
    private ReportedIncidentDao reportedIncidentDao;

    @Captor
    private ArgumentCaptor<com.redhat.cajun.navy.incident.entity.ReportedIncident> entityCaptor;

    @Captor
    private ArgumentCaptor<Message<IncidentReportedEvent>> messageCaptor;

    private ReportedIncidentServiceImpl service;

    @Before
    @SuppressWarnings("unchecked")
    public void init() {
        initMocks(this);
        service = new ReportedIncidentServiceImpl();
        setField(service, null, kafkaTemplate, KafkaTemplate.class);
        setField(service, null, reportedIncidentDao, ReportedIncidentDao.class);
        setField(service, "destination", "test-topic", String.class);
        ListenableFuture future = mock(ListenableFuture.class);
        when(kafkaTemplate.send(anyString(), anyString(), any())).thenReturn(future);
    }

    @Test
    public void testSendIncidentReportEventMessage() {

        ReportedIncident incident = new ReportedIncident.Builder()
                .lat("30.12345")
                .lon("-70.98765")
                .numberOfPeople(2)
                .medicalNeeded(false)
                .victimName("John Doe")
                .victimPhoneNumber("123-456-789")
                .build();

        service.sendIncidentReportedEventMessage(incident);

        verify(reportedIncidentDao).create(entityCaptor.capture());
        com.redhat.cajun.navy.incident.entity.ReportedIncident entity = entityCaptor.getValue();
        String incidentId = entity.getIncidentId();
        assertThat(incidentId, notNullValue());
        assertThat(entity.getLatitude(), equalTo(incident.getLat()));
        assertThat(entity.getLongitude(), equalTo(incident.getLon()));
        assertThat(entity.getNumberOfPeople(), equalTo(incident.getNumberOfPeople()));
        assertThat(entity.isMedicalNeeded(), equalTo(incident.isMedicalNeeded()));
        assertThat(entity.getVictimName(), equalTo(incident.getVictimName()));
        assertThat(entity.getVictimPhoneNumber(), equalTo(incident.getVictimPhoneNumber()));
        assertThat(entity.getTimestamp() <= System.currentTimeMillis(), is(true));
        assertThat(entity.getStatus(), equalTo(IncidentStatus.REPORTED.name()));

        verify(kafkaTemplate).send(eq("test-topic"), eq(incidentId), messageCaptor.capture());
        Message<IncidentReportedEvent> message = messageCaptor.getValue();
        assertThat(message.getId(), notNullValue());
        assertThat(message.getInvokingService(), equalTo("IncidentService"));
        assertThat(message.getBody(), notNullValue());
        IncidentReportedEvent event = message.getBody();
        assertThat(event.getId(), notNullValue());
        assertThat(event.getId(), equalTo(incidentId));
        assertThat(event.getLat(), equalTo(new BigDecimal(incident.getLat())));
        assertThat(event.getLon(), equalTo(new BigDecimal(incident.getLon())));
        assertThat(event.getNumberOfPeople(), equalTo(incident.getNumberOfPeople()));
        assertThat(event.isMedicalNeeded(), equalTo(incident.isMedicalNeeded()));
        assertThat(event.getTimestamp(), equalTo(entity.getTimestamp()));
    }

}
