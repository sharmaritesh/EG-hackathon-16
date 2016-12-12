package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.DeviceRegistrationModel;
import com.egencia.hackathon.respository.DeviceRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DefaultDeviceService implements DeviceService {

    private DeviceRegistrationRepository deviceRegistrationRepository;
    private FCMService fcmService;

    @Autowired
    public DefaultDeviceService(DeviceRegistrationRepository deviceRegistrationRepository,
                                FCMService fcmService) {
        this.deviceRegistrationRepository = deviceRegistrationRepository;
        this.fcmService = fcmService;
    }

    @Override
    public String registerDevice(final String phoneNumber) {
        DeviceRegistrationModel device = getDevice(phoneNumber);

        // Check is this device is already is registered with System or not?
        if (device == null) {
            // If YES that means its a REFRESH token request. So just update token
            device.setFcmToken(UUID.randomUUID().toString());
            deviceRegistrationRepository.save(device);
        }

        return device.getFcmToken();
    }

    @Override
    public DeviceRegistrationModel getDevice(final String phoneNumber) {
        return deviceRegistrationRepository.findByNumber(phoneNumber);
    }

    @Override
    public void notifyDevice(final String phoneNumber, final Alert alert) {
        final DeviceRegistrationModel device = getDevice(phoneNumber);
        if (device != null) {
            fcmService.notifyDeviceOverFCM(device, alert);
        }
    }

    @Override
    public List<Alert> getAlerts(String phoneNumber) {
        return fcmService.getPendingAlerts(phoneNumber);
    }
}
