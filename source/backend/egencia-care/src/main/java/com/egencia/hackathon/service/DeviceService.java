package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.DeviceRegistrationModel;

import java.util.List;

public interface DeviceService {
    String registerDevice(String phoneNumber);
    DeviceRegistrationModel getDevice(String phoneNumber);

    boolean notifyDevice(String phoneNumber, Alert alert);
    List<Alert> getAlerts(String phoneNumber);
}
