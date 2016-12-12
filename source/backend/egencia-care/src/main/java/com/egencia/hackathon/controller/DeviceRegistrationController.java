package com.egencia.hackathon.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class DeviceRegistrationController {

    @RequestMapping(value = "notifycare", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Map<String, String>>> notifyCareSystem() {
        return null;
    }
}
