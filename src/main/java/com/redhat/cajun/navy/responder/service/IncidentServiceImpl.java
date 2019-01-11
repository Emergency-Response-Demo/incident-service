package com.redhat.cajun.navy.responder.service;

import com.redhat.cajun.navy.responder.IncidentStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private DataSource datasource;

    private JdbcTemplate jdbcTemplate;

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
