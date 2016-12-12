package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.DeviceRegistrationModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FCMService {

    private ConcurrentHashMap<String, List<Alert>> deviceAlerts = new ConcurrentHashMap<>();

    public void notifyDeviceOverFCM(final DeviceRegistrationModel device, final Alert alert) {
        List<Alert> alerts = deviceAlerts.get(device.getNumber());
        if (alerts == null) {
            alerts = new ArrayList<>();
        }
        alerts.add(alert);
        deviceAlerts.put(device.getNumber(), alerts);
    }

    public List<Alert> getPendingAlerts(final String phoneNumber) {
        final List<Alert> alerts = deviceAlerts.get(phoneNumber);
        if (alerts != null && alerts.size() > 0) {
            deviceAlerts.remove(phoneNumber);
        }
        return alerts;
    }
}
