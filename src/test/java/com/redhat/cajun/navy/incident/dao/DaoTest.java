package com.redhat.cajun.navy.incident.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
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

    @Test
    @Transactional
    public void testFindAllReportedIncidents() {

        //end the current transaction
        TestTransaction.end();

        new TransactionTemplate(transactionManager).execute(s -> {
            reportedIncidentDao.deleteAll();
            return null;
        });

        ReportedIncident reportedIncident1= new ReportedIncident.Builder()
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

        ReportedIncident reportedIncident2= new ReportedIncident.Builder()
                .incidentId(UUID.randomUUID().toString())
                .latitude("35.12345")
                .longitude("-75.98765")
                .numberOfPeople(4)
                .medicalNeeded(false)
                .victimName("John Foo")
                .victimPhoneNumber("123-456-789")
                .status("Reported")
                .reportedTime(System.currentTimeMillis())
                .build();


        new TransactionTemplate(transactionManager).execute(s -> {
            reportedIncidentDao.create(reportedIncident1);
            reportedIncidentDao.create(reportedIncident2);
            return null;
        });

        new TransactionTemplate(transactionManager).execute(s -> {
            List<ReportedIncident> result = reportedIncidentDao.findAll();
            assertThat(result, notNullValue());
            assertThat(result.size(), equalTo(2));
            return null;
        });
    }

    @Test
    @Transactional
    public void testFindReportedIncidentsByStatus() {

        //end the current transaction
        TestTransaction.end();

        new TransactionTemplate(transactionManager).execute(s -> {
            reportedIncidentDao.deleteAll();
            return null;
        });

        ReportedIncident reportedIncident1= new ReportedIncident.Builder()
                .incidentId(UUID.randomUUID().toString())
                .latitude("30.12345")
                .longitude("-70.98765")
                .numberOfPeople(3)
                .medicalNeeded(false)
                .victimName("John Doe")
                .victimPhoneNumber("123-456-789")
                .status("REPORTED")
                .reportedTime(System.currentTimeMillis())
                .build();

        ReportedIncident reportedIncident2= new ReportedIncident.Builder()
                .incidentId(UUID.randomUUID().toString())
                .latitude("35.12345")
                .longitude("-75.98765")
                .numberOfPeople(4)
                .medicalNeeded(false)
                .victimName("John Foo")
                .victimPhoneNumber("123-456-789")
                .status("ASSIGNED")
                .reportedTime(System.currentTimeMillis())
                .build();


        new TransactionTemplate(transactionManager).execute(s -> {
            reportedIncidentDao.create(reportedIncident1);
            reportedIncidentDao.create(reportedIncident2);
            return null;
        });

        new TransactionTemplate(transactionManager).execute(s -> {
            List<ReportedIncident> result = reportedIncidentDao.findByStatus("reported");
            assertThat(result, notNullValue());
            assertThat(result.size(), equalTo(1));
            assertThat(result.get(0).getVictimName(), equalTo("John Doe"));
            assertThat(result.get(0).getStatus(), equalTo("REPORTED"));
            return null;
        });
    }

    @Test
    @Transactional
    public void testFindReportedIncidentsByStatusEmptyList() {

        //end the current transaction
        TestTransaction.end();

        new TransactionTemplate(transactionManager).execute(s -> {
            reportedIncidentDao.deleteAll();
            return null;
        });

        ReportedIncident reportedIncident1= new ReportedIncident.Builder()
                .incidentId(UUID.randomUUID().toString())
                .latitude("30.12345")
                .longitude("-70.98765")
                .numberOfPeople(3)
                .medicalNeeded(false)
                .victimName("John Doe")
                .victimPhoneNumber("123-456-789")
                .status("REPORTED")
                .reportedTime(System.currentTimeMillis())
                .build();

        ReportedIncident reportedIncident2= new ReportedIncident.Builder()
                .incidentId(UUID.randomUUID().toString())
                .latitude("35.12345")
                .longitude("-75.98765")
                .numberOfPeople(4)
                .medicalNeeded(false)
                .victimName("John Foo")
                .victimPhoneNumber("123-456-789")
                .status("ASSIGNED")
                .reportedTime(System.currentTimeMillis())
                .build();


        new TransactionTemplate(transactionManager).execute(s -> {
            reportedIncidentDao.create(reportedIncident1);
            reportedIncidentDao.create(reportedIncident2);
            return null;
        });

        new TransactionTemplate(transactionManager).execute(s -> {
            List<ReportedIncident> result = reportedIncidentDao.findByStatus("rescued");
            assertThat(result, notNullValue());
            assertThat(result.size(), equalTo(0));
            return null;
        });
    }
}
