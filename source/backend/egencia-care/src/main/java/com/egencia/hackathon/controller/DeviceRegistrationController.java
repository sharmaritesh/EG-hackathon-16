package com.egencia.hackathon.controller;

import com.egencia.hackathon.model.DeviceRegistrationModel;
import com.egencia.hackathon.model.NotifyCareModel;
import com.egencia.hackathon.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
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

    @RequestMapping(value = "device", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Map<String, String>>> registerDevice(
            @Valid @RequestBody final DeviceRegistrationModel registrationModel) {

        return () -> {
            deviceService.registerDevice(registrationModel);
            return new ResponseEntity<Map<String, String>>(HttpStatus.CREATED);
        };
    }
}
