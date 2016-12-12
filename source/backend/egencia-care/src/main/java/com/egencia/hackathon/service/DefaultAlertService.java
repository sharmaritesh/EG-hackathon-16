package com.egencia.hackathon.service;

import com.egencia.hackathon.event.EventManager;
import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.respository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DefaultAlertService implements AlertService {

    private EventManager eventManager;
    private NotifyCareService notifyCareService;

    private AlertRepository alertRepository;

    @Autowired
    public DefaultAlertService(EventManager eventManager, NotifyCareService notifyCareService,
                               DeviceService deviceService, @Qualifier("SMSSendMessageImpl") SMSSendMessageImpl smsSendMessage,
                               AlertRepository alertRepository) {
        this.eventManager = eventManager;
        this.notifyCareService = notifyCareService;
        this.alertRepository = alertRepository;
    }

    @Override
    public String queueAlert(Alert alert) {
        if (alert != null) {
            Alert alertResponse = alertRepository.save(alert);
            eventManager.append(alert);
            return alertResponse.getId();
        }
        return null;
    }
}
