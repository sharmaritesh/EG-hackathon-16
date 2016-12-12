package com.egencia.hackathon.controller;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.DeviceRegistrationModel;
import com.egencia.hackathon.model.NotifyCareModel;
import com.egencia.hackathon.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class DeviceRegistrationController {

    private DeviceService deviceService;

    @Autowired
    public DeviceRegistrationController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @RequestMapping(value = "device/{phoneNumber}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<String>> registerDevice(
            @NotNull @PathVariable("phoneNumber") String phoneNumber) {

        return () -> {
            final String fcmToken = deviceService.registerDevice(phoneNumber);
            return new ResponseEntity<String>(fcmToken, HttpStatus.CREATED);
        };
    }

    @RequestMapping(value = "device/{phoneNumber}/alerts", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Alert>> getAlerts(@NotNull @PathVariable("phoneNumber") String phoneNumber) {
        return new ResponseEntity<List<Alert>>(deviceService.getAlerts(phoneNumber), HttpStatus.OK);
    }
}
