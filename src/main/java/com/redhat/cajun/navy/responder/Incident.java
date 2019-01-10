package com.redhat.cajun.navy.responder;

import java.math.BigDecimal;
import java.util.Objects;

public class Incident {

    private String id;
    private Reporter reporter;
    private BigDecimal lat;
    private BigDecimal lon;
    private int numberOfPeople;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return id == incident.id &&
                numberOfPeople == incident.numberOfPeople &&
                Objects.equals(reporter, incident.reporter) &&
                Objects.equals(lat, incident.lat) &&
                Objects.equals(lon, incident.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reporter, lat, lon, numberOfPeople);
    }

    @Override
    public String toString() {
        return "Incident{" +
                "id=" + id +
                ", reporter=" + reporter +
                ", lat=" + lat +
                ", lon=" + lon +
                ", numberOfPeople=" + numberOfPeople +
                '}';
    }
}
