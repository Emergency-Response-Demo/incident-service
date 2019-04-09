package com.redhat.cajun.navy.incident.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.UUID;

import com.redhat.cajun.navy.incident.entity.ReportedIncident;
import com.redhat.cajun.navy.incident.model.IncidentStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ReportedIncidentDao.class))
public class DaoTest {

    @Autowired
    private ReportedIncidentDao reportedIncidentDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void init() {
      reportedIncidentDao.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveReportedIncident() {
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

    @Test
    public void testUpdateReportedIncident() {

        //end the current transaction
        TestTransaction.end();

        ReportedIncident incident = new ReportedIncident.Builder()
                .incidentId("testId")
                .victimName("John Doe")
                .victimPhoneNumber("111-222-333")
                .latitude("30.12345")
                .longitude("-77.98765")
                .numberOfPeople(2)
                .medicalNeeded(true)
                .reportedTime(System.currentTimeMillis())
                .status(IncidentStatus.REPORTED.name())
                .build();

        new TransactionTemplate(transactionManager).execute(s -> {
            reportedIncidentDao.create(incident);
            return null;
        });

        ReportedIncident updated = new ReportedIncident.Builder(incident.getId(), incident.getVersion())
                .incidentId("testId")
                .victimName("John Doe")
                .victimPhoneNumber("111-222-333")
                .latitude("30.12345")
                .longitude("-77.98765")
                .numberOfPeople(2)
                .medicalNeeded(true)
                .reportedTime(incident.getTimestamp())
                .status(IncidentStatus.PICKEDUP.name())
                .build();

        new TransactionTemplate(transactionManager).execute(s -> {
            ReportedIncident current = reportedIncidentDao.findByIncidentId("testId");
            assertThat(current, notNullValue());
            assertThat(current.getStatus(), equalTo("REPORTED"));
            reportedIncidentDao.merge(updated);
            return null;
        });

        new TransactionTemplate(transactionManager).execute(s -> {
            ReportedIncident current = reportedIncidentDao.findByIncidentId("testId");
            assertThat(current, notNullValue());
            assertThat(current.getStatus(), equalTo("PICKEDUP"));
            return null;
        });
    }
}
