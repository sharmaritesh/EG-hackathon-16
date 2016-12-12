package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.DeviceRegistrationModel;
import com.egencia.hackathon.respository.DeviceRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultDeviceService implements DeviceService {

    private DeviceRegistrationRepository deviceRegistrationRepository;

    @Autowired
    public DefaultDeviceService(DeviceRegistrationRepository deviceRegistrationRepository) {
        this.deviceRegistrationRepository = deviceRegistrationRepository;
    }

    @Override
    public void registerDevice(DeviceRegistrationModel registrationModel) {
        DeviceRegistrationModel device = getDevice(registrationModel.getNumber());

        // Check is this device is already is registered with System or not?
        if (device != null) {
            // If YES that means its a REFRESH token request. So just update token
            device.setFcmToken(registrationModel.getFcmToken());
        } else {
            device = registrationModel;
        }
        deviceRegistrationRepository.save(device);
    }

    @Override
    public DeviceRegistrationModel getDevice(final String phoneNumber) {
        return deviceRegistrationRepository.findByNumber(phoneNumber);
    }

    @Override
    public void notifyDevice(final String phoneNumber, final Alert alert) {
        final DeviceRegistrationModel device = getDevice(phoneNumber);
        if (device != null) {
            //TODO : Notify Device with FCM
        }
    }
}
