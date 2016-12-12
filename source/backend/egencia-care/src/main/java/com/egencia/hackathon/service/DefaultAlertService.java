package com.egencia.hackathon.service;

import com.egencia.hackathon.event.EventManager;
import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.NotifyCareModel;
import com.egencia.hackathon.model.Traveler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAlertService implements AlertService {

    private EventManager eventManager;
    private NotifyCareService notifyCareService;
    private DeviceService deviceService;

    @Autowired
    public DefaultAlertService(EventManager eventManager, NotifyCareService notifyCareService,
                               DeviceService deviceService) {
        this.eventManager = eventManager;
        this.notifyCareService = notifyCareService;
        this.deviceService = deviceService;
    }

    @Override
    public void queueAlert(Alert alert) {
        if (alert != null) {
            eventManager.append(alert);
        }
    }

    @Async
    @Override
    public void handleAlert(final Alert alert) {
        final List<NotifyCareModel> applicableTrips = notifyCareService.findApplicableTrips(alert);
        if (applicableTrips != null && applicableTrips.size() > 0) {
            for (NotifyCareModel applicableTrip : applicableTrips) {
                final List<Traveler> travelers = applicableTrip.getTravelers();

                if (travelers != null && travelers.size() >0) {
                    for (Traveler traveler : travelers) {
                        deviceService.notifyDevice(traveler.getNumber(), alert);
                    }
                }
            }
        }
    }
}
