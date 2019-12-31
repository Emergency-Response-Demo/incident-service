package com.redhat.cajun.navy.incident.message;

import java.math.BigDecimal;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class IncidentReportedMessageTest {



    @Test
    public void testIncidentReportedMessageSerializationTest() {

        Message<IncidentReportedEvent> message = new Message.Builder<>("IncidentReportedEvent", "IncidentService",
                new IncidentReportedEvent.Builder("qwertyuiop")
                        .lat(new BigDecimal("34.1234"))
                        .lon(new BigDecimal("-77.9876"))
                        .medicalNeeded(true)
                        .numberOfPeople(5)
                        .timestamp(System.currentTimeMillis())
                        .victimName("John Doe")
                        .victimPhoneNumber("111-222-333")
                        .status("REPORTED")
                        .build())
                .build();

        String expected = "{" +
                "\"id\":\"" + message.getId() + "\"," +
                "\"messageType\":\"IncidentReportedEvent\"," +
                "\"invokingService\":\"IncidentService\"," +
                "\"timestamp\":" + message.getTimestamp() + "," +
                "\"body\":{" +
                "\"id\":\"qwertyuiop\"," +
                "\"lat\":34.1234," +
                "\"lon\":-77.9876," +
                "\"numberOfPeople\":5," +
                "\"medicalNeeded\":true," +
                "\"timestamp\":" + message.getBody().getTimestamp() + "," +
                "\"victimName\":\"John Doe\"," +
                "\"victimPhoneNumber\":\"111-222-333\"," +
                "\"status\":\"REPORTED\"" +
                "}" +
                "}";

        JsonSerializer<Message<IncidentReportedEvent>> serializer = new JsonSerializer<>();
        byte[] serialized = serializer.serialize("test", message);
        MatcherAssert.assertThat(serialized, CoreMatchers.notNullValue());
        String serializedAsString = new String(serialized);
        MatcherAssert.assertThat(serializedAsString, CoreMatchers.equalTo(expected));
    }

}
