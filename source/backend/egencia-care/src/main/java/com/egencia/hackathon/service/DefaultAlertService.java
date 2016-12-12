package com.egencia.hackathon.service;

import com.egencia.hackathon.event.EventManager;
import com.egencia.hackathon.model.*;
import com.egencia.hackathon.respository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultAlertService implements AlertService {

    private EventManager eventManager;
    private NotifyCareService notifyCareService;
    private DeviceService deviceService;
    private SMSSendMessageImpl smsSendMessage;
    private AlertRepository alertRepository;
    private Map<String, Traveler> notifiedTravelersMap = new HashMap<>();

    @Autowired
    public DefaultAlertService(EventManager eventManager, NotifyCareService notifyCareService,
                               DeviceService deviceService, @Qualifier("SMSSendMessageImpl") SMSSendMessageImpl smsSendMessage,
                               AlertRepository alertRepository) {
        this.eventManager = eventManager;
        this.notifyCareService = notifyCareService;
        this.deviceService = deviceService;
        this.smsSendMessage = smsSendMessage;
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

    @Async
    @Override
    public void handleAlert(final Alert alert) {
        final List<NotifyCareModel> applicableTrips = notifyCareService.findApplicableTrips(alert);
        if (applicableTrips != null && applicableTrips.size() > 0) {
            for (NotifyCareModel applicableTrip : applicableTrips) {
                final List<Traveler> travelers = applicableTrip.getTravelers();

                if (travelers != null && travelers.size() >0) {
                    for (Traveler traveler : travelers) {
                        notifiedTravelersMap.put(traveler.getNumber(), traveler);
                        deviceService.notifyDevice(traveler.getNumber(), alert);
                    }
                }
            }
        }
    }

    @Override
    public void handleAlertReply(AlertReply alertReply) {
        Traveler notifiedTraveler = notifiedTravelersMap.get(alertReply.getNumber());
        if(alertReply.isStatus()) {
            if(notifiedTraveler.getContacts() != null) {
                for(EmergencyContact contact : notifiedTraveler.getContacts()) {
                    Message message = new Message();
                    message.setNumber(contact.getNumber());
                    message.setText("Hello " +contact.getName() + ", We were able to contact: "+ alertReply.getUserId()+ " and he is safe.");
                    smsSendMessage.send(message);
                }
            }
        }
        notifiedTravelersMap.remove(alertReply.getNumber());
    }
}
