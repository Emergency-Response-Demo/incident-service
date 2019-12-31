package com.redhat.cajun.navy.incident.message;

import java.math.BigDecimal;

public class IncidentReportedEvent {

    private String id;

    private BigDecimal lat;

    private BigDecimal lon;

    private int numberOfPeople;

    private boolean medicalNeeded;

    private long timestamp;

    private String victimName;

    private String victimPhoneNumber;

    private String status;

    public String getId() {
        return id;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public boolean isMedicalNeeded() {
        return medicalNeeded;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getVictimName() {
        return victimName;
    }

    public String getVictimPhoneNumber() {
        return victimPhoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public static class Builder {

        private IncidentReportedEvent ire;

        public Builder(String id) {
            ire = new IncidentReportedEvent();
            ire.id = id;
        }

        public Builder lat(BigDecimal lat) {
            ire.lat = lat;
            return this;
        }

        public Builder lon(BigDecimal lon) {
            ire.lon = lon;
            return this;
        }

        public Builder numberOfPeople(int numberOfPeople) {
            ire.numberOfPeople = numberOfPeople;
            return this;
        }

        public Builder medicalNeeded(boolean medicalNeeded) {
            ire.medicalNeeded = medicalNeeded;
            return this;
        }

        public Builder timestamp(long timestamp) {
            ire.timestamp = timestamp;
            return this;
        }

        public Builder victimName(String victimName) {
            ire.victimName = victimName;
            return this;
        }

        public Builder victimPhoneNumber(String victimPhoneNumber) {
            ire.victimPhoneNumber = victimPhoneNumber;
            return this;
        }

        public Builder status(String status) {
            ire.status = status;
            return this;
        }

        public IncidentReportedEvent build() {
            return ire;
        }
    }
}
