package com.redhat.cajun.navy.incident.model;

public class ReportedIncident {

    private String id;

    private String lat;

    private String lon;

    private int numberOfPeople;

    private boolean medicalNeeded;

    private String victimName;

    private String victimPhoneNumber;

    private long timestamp;

    private String status;

    public String getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public boolean isMedicalNeeded() {
        return medicalNeeded;
    }

    public String getVictimName() {
        return victimName;
    }

    public String getVictimPhoneNumber() {
        return victimPhoneNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }
}
