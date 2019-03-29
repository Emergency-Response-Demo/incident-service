package com.redhat.cajun.navy.incident.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.UUID;

import com.redhat.cajun.navy.incident.entity.ReportedIncident;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ReportedIncidentDao.class))
public class DaoTest {

    @Autowired
    private ReportedIncidentDao reportedIncidentDao;

    @Test
    @Transactional
    public void testSaveReportedIncident() {
        assertThat(reportedIncidentDao, notNullValue());

        ReportedIncident reportedIncident= new ReportedIncident.Builder()
                .incidentId(UUID.randomUUID().toString())
                .latitude("30.12345")
                .longitude("-70.98765")
                .numberOfPeople(3)
                .medicalNeeded(false)
                .victimName("John Doe")
                .victimPhoneNumber("123-456-789")
                .status("Reported")
                .build();

        reportedIncidentDao.create(reportedIncident);
        assertThat(reportedIncident.getId(), not(equalTo(0)));
    }

    @Test
    @Transactional
    public void testFindReportedIncidentById() {

        ReportedIncident reportedIncident= new ReportedIncident.Builder()
                .incidentId(UUID.randomUUID().toString())
                .latitude("30.12345")
                .longitude("-70.98765")
                .numberOfPeople(3)
                .medicalNeeded(false)
                .victimName("John Doe")
                .victimPhoneNumber("123-456-789")
                .status("Reported")
                .reportedTime(System.currentTimeMillis())
                .build();

        reportedIncidentDao.create(reportedIncident);

        ReportedIncident found = reportedIncidentDao.findByIncidentId(reportedIncident.getIncidentId());
        assertThat(found, notNullValue());
        assertThat(found.getId(), equalTo(reportedIncident.getId()));
        assertThat(found.getIncidentId(), equalTo(reportedIncident.getIncidentId()));

    }

}
