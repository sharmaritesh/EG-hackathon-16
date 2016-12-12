package com.egencia.hackathon.service;

import com.egencia.hackathon.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gurssingh on 12/12/16.
 */
@Service
public class AlertHandler {

    private NotifyCareService notifyCareService;
    private DeviceService deviceService;
    private SMSSendMessageImpl smsSendMessage;
    private ConcurrentHashMap<String, Traveler> notifiedTravelersMap = new ConcurrentHashMap<>();

    @Autowired
    public AlertHandler(NotifyCareService notifyCareService,
                               DeviceService deviceService, @Qualifier("SMSSendMessageImpl") SMSSendMessageImpl smsSendMessage) {
        this.notifyCareService = notifyCareService;
        this.deviceService = deviceService;
        this.smsSendMessage = smsSendMessage;
    }

    public void handleAlert(final Alert alert) {
        final List<NotifyCareModel> applicableTrips = notifyCareService.findApplicableTrips(alert);
        if (applicableTrips != null && applicableTrips.size() > 0) {
            for (NotifyCareModel applicableTrip : applicableTrips) {
                final List<Traveler> travelers = applicableTrip.getTravelers();

                if (travelers != null && travelers.size() >0) {
                    for (Traveler traveler : travelers) {
                        notifiedTravelersMap.put(traveler.getNumber(), traveler);
                        boolean status = deviceService.notifyDevice(traveler.getNumber(), alert);
                        if(status) {
                            AlertReply alertReply = new AlertReply();
                            alertReply.setStatus(status);
                            alertReply.setNumber(traveler.getNumber());
                            alertReply.setUserId(traveler.getName());
                            handleAlertReply(alertReply);
                        }
                    }
                }
            }
        }
    }

    public void handleAlertReply(AlertReply alertReply) {
        Traveler notifiedTraveler = notifiedTravelersMap.get(alertReply.getNumber());
        if(notifiedTraveler == null) {
            Message message = new Message();
            message.setNumber("+918802381988");
            message.setText("Hello Gursharan, We were able to contact: "+ alertReply.getUserId()+ " and he is safe.");
            smsSendMessage.send(message);
        }else if(alertReply.isStatus()) {
            if(notifiedTraveler.getContacts() != null) {
                for(EmergencyContact contact : notifiedTraveler.getContacts()) {
                    Message message = new Message();
                    message.setNumber(contact.getNumber());
                    message.setNumber(contact.getNumber());
                    message.setText("Hello " +contact.getName() + ", We were able to contact: "+ alertReply.getUserId()+ " and he is safe.");
                    smsSendMessage.send(message);
                }
            }
        } else {
            if(notifiedTraveler.getContacts() != null) {
                for(EmergencyContact contact : notifiedTraveler.getContacts()) {
                    Message message = new Message();
                    message.setNumber(contact.getNumber());
                    message.setNumber(contact.getNumber());
                    message.setText("Hello " +contact.getName() + ", We were not able to contact: "+ alertReply.getUserId()+ ". We have escalated this to our care team.");
                    smsSendMessage.send(message);
                }
            }
        }
        notifiedTravelersMap.remove(alertReply.getNumber());
    }
}
