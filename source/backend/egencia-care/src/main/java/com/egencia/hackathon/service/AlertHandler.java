package com.egencia.hackathon.service;

import com.egencia.hackathon.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gurssingh on 12/12/16.
 */
@Service
public class AlertHandler {

    private NotifyCareService notifyCareService;
    private DeviceService deviceService;
    private SMSSendMessageImpl smsSendMessage;
    private Map<String, Traveler> notifiedTravelersMap = new ConcurrentHashMap<>();

    @Autowired
    public AlertHandler(NotifyCareService notifyCareService,
                               DeviceService deviceService, @Qualifier("SMSSendMessageImpl") SMSSendMessageImpl smsSendMessage) {
        this.notifyCareService = notifyCareService;
        this.deviceService = deviceService;
        this.smsSendMessage = smsSendMessage;
    }

    @Async
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
