package com.egencia.hackathon.service;

import com.egencia.hackathon.model.DeviceRegistrationModel;

public interface DeviceService {
    void registerDevice(DeviceRegistrationModel registrationModel);
    DeviceRegistrationModel getDevice(String phoneNumber);
}
