package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.DeviceRegistrationModel;

public interface DeviceService {
    void registerDevice(DeviceRegistrationModel registrationModel);
    DeviceRegistrationModel getDevice(String phoneNumber);

    void notifyDevice(String phoneNumber, Alert alert);
}
