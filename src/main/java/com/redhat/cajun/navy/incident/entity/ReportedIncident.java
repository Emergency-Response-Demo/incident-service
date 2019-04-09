package com.redhat.cajun.navy.incident.entity;

import java.time.Instant;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Access(AccessType.FIELD)
@SequenceGenerator(name="ReportedIncidentSeq", sequenceName="REPORTED_INCIDENT_SEQ")
@Table(name = "reported_incident")
public class ReportedIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="ReportedIncidentSeq")
    private long id;

    @Column(name = "incident_id")
    private String incidentId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "number_of_people")
    private int numberOfPeople;

    @Column(name = "medical_needed")
    private boolean medicalNeeded;

    @Column(name = "victim_name")
    private String victimName;

    @Column(name = "victim_phone")
    private String victimPhoneNumber;

    @Basic
    @Column(name = "reported_time")
    private Instant reportedTime;

    @Column(name = "incident_status")
    private String status;

    @Column(name = "version")
    @Version
    private long version;

    public long getId() {
        return id;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public Boolean isMedicalNeeded() {
        return medicalNeeded;
    }

    public String getVictimName() {
        return victimName;
    }

    public String getVictimPhoneNumber() {
        return victimPhoneNumber;
    }

    public long getTimestamp() {
        return reportedTime.toEpochMilli();
    }

    public Instant getReportedTime() {
        return reportedTime;
    }

    public String getStatus() {
        return status;
    }

    public long getVersion() {
        return version;
    }

    public static class Builder {

        private final ReportedIncident reportedIncident;

        public Builder() {
            reportedIncident = new ReportedIncident();
        }

        public Builder(long id, long version) {
            reportedIncident = new ReportedIncident();
            reportedIncident.id = id;
            reportedIncident.version = version;
        }

        public Builder incidentId(String incidentId) {
            reportedIncident.incidentId = incidentId;
            return this;
        }

        public Builder latitude(String latitude) {
            reportedIncident.latitude = latitude;
            return this;
        }

        public Builder longitude(String longitude) {
            reportedIncident.longitude = longitude;
            return this;
        }

        public Builder numberOfPeople(Integer numberOfPeople) {
            reportedIncident.numberOfPeople = numberOfPeople;
            return this;
        }

        public Builder medicalNeeded(Boolean medicalNeeded) {
            reportedIncident.medicalNeeded = medicalNeeded;
            return this;
        }

        public Builder victimName(String victimName) {
            reportedIncident.victimName = victimName;
            return this;
        }

        public Builder victimPhoneNumber(String victimPhoneNumber) {
            reportedIncident.victimPhoneNumber = victimPhoneNumber;
            return this;
        }

        public Builder reportedTime(long timestamp) {
            reportedIncident.reportedTime = Instant.ofEpochMilli(timestamp);
            return this;
        }

        public Builder status(String status) {
            reportedIncident.status = status;
            return this;
        }

        public ReportedIncident build() {
            return reportedIncident;
        }

    }

}
