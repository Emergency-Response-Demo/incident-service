package com.redhat.cajun.navy.responder.service;

import com.redhat.cajun.navy.responder.Incident;
import com.redhat.cajun.navy.responder.IncidentStats;
import com.redhat.cajun.navy.responder.Reporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private DataSource datasource;

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Incident> getIncidentMap() {

        jdbcTemplate = new JdbcTemplate(datasource);

        String sql = "SELECT * FROM incident JOIN reporter ON reporter.reporter_id = incident.report_id";

        LinkedList<Incident> incidents = new LinkedList<>();

        List<Map<String, Object>> queryResults = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : queryResults) {
            Incident incident = new Incident();

            System.out.println(row);

            Reporter reporter = new Reporter();
            reporter.setId(
                    ((Integer) row.get("reporter_id")).toString()
            );
//            reporter.setReportTime(
//                    (ZonedDateTime) row.get("report_time")
//            );
            reporter.setPhoneNumber(
                    (String) row.get("reporter_phone_number")
            );
            reporter.setFullName(
                    (String) row.get("reporter_name")
            );
            incident.setReporter(reporter);

            incident.setNumberOfPeople(
                    (Integer) row.get("number_of_people")
            );
            incident.setId(
                    ((Integer) row.get("incident_id")).toString()
            );
            incident.setLat(
                    parseCoordinate(row.get("gps_lat"))
            );
            incident.setLon(
                    parseCoordinate(row.get("gps_long"))
            );
            incident.setMedicalNeeded(
                    (Boolean) row.get("medical_need")
            );

            incidents.add(incident);
        }

        return incidents;
    }

    private BigDecimal parseCoordinate(Object coordinate) {
        Double doubleCoordinate = (Double) coordinate;
        BigDecimal result = new BigDecimal(doubleCoordinate);
        return result;
    }

    @Override
    public IncidentStats getIncidentStats() {

        jdbcTemplate = new JdbcTemplate(datasource);
        IncidentStats stats = new IncidentStats();

        String sql = "SELECT current_status, count(*) as num_incidents FROM mission GROUP BY current_status";

        List<Map<String, Object>> queryResults = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : queryResults) {
            Long num_incidents = (Long) row.get("num_incidents");

            if (row.get("current_status").equals("Rescued")) {
                stats.setRescued(num_incidents);
            }
            if (row.get("current_status").equals("Requested")) {
                stats.setRequested(num_incidents);
            }
            if (row.get("current_status").equals("Pickedup")) {
                stats.setPickedUp(num_incidents);
            }
            if (row.get("current_status").equals("Assigned")) {
                stats.setAssigned(num_incidents);
            }
            if (row.get("current_status").equals("Cancelled")) {
                stats.setCancelled(num_incidents);
            }
        }

        return stats;
    }


}
