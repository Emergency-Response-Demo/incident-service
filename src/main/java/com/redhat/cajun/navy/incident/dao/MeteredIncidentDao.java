package com.redhat.cajun.navy.incident.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import com.redhat.cajun.navy.incident.entity.Incident;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class MeteredIncidentDao implements IIncidentDao {

    @Autowired
    @Qualifier("delegate")
    private IIncidentDao delegate;

    @Autowired
    private MeterRegistry registry;

    private Timer createTimer;

    private Timer findByIncidentIdTimer;

    private Timer mergeTimer;

    private Timer findAllTimer;

    private Timer findByStatusTimer;

    private Timer findByNameTimer;

    private Timer deleteAllTimer;

    @Override
    public Incident create(Incident incident) {
        try {
            return createTimer.recordCallable(() -> delegate.create(incident));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Incident findByIncidentId(String incidentId) {
        try {
            return findByIncidentIdTimer.recordCallable(() -> delegate.findByIncidentId(incidentId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Incident merge(Incident incident) {
        try {
            return mergeTimer.recordCallable(() -> delegate.merge(incident));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Incident> findAll() {
        try {
            return findAllTimer.recordCallable(() -> delegate.findAll());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Incident> findByStatus(String status) {
        try {
            return findByStatusTimer.recordCallable(() -> delegate.findByStatus(status));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Incident> findByName(String pattern) {
        try {
            return findByNameTimer.recordCallable(() -> delegate.findByName(pattern));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            deleteAllTimer.<Void>recordCallable(() -> {
                delegate.deleteAll();
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init() {
        String name = "incident.service.dao";
        createTimer = Timer.builder(name).tag("operation", "create").register(registry);
        findByIncidentIdTimer = Timer.builder(name).tag("operation", "findByIncidentId").register(registry);
        mergeTimer = Timer.builder(name).tag("operation", "merge").register(registry);
        findAllTimer = Timer.builder(name).tag("operation", "findAll").register(registry);
        findByStatusTimer = Timer.builder(name).tag("operation", "findByStatus").register(registry);
        findByNameTimer = Timer.builder(name).tag("operation", "findByName").register(registry);
        deleteAllTimer = Timer.builder(name).tag("operation", "deleteAll").register(registry);
    }
}
