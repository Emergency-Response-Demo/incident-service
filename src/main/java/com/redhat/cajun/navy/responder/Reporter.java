package com.redhat.cajun.navy.responder;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * The person who reported an Incident
 */
public class Reporter {

    private String id;
    private String fullName;
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss.SSSZ")
    private ZonedDateTime reportTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ZonedDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(ZonedDateTime reportTime) {
        this.reportTime = reportTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reporter reporter = (Reporter) o;
        return Objects.equals(id, reporter.id) &&
                Objects.equals(fullName, reporter.fullName) &&
                Objects.equals(phoneNumber, reporter.phoneNumber) &&
                Objects.equals(reportTime, reporter.reportTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, phoneNumber, reportTime);
    }

    @Override
    public String toString() {
        return "Reporter{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", reportTime=" + reportTime +
                '}';
    }
}
