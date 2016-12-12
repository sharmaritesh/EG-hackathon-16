package com.egencia.hackathon.controller;

import com.egencia.hackathon.model.NotifyCareModel;
import com.egencia.hackathon.service.NotifyCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class NotificationController {

    private NotifyCareService notifyCareService;

    @Autowired
    public NotificationController(NotifyCareService notifyCareService) {
        this.notifyCareService = notifyCareService;
    }

    @RequestMapping(value = "notifycare", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Map<String, String>>> notifyCareSystem(
            @Valid @RequestBody final NotifyCareModel notifyCareModel) {
        return () -> {
            final NotifyCareModel createdNotification = notifyCareService.create(notifyCareModel);

            final HashMap<String, String> payload = new HashMap<>();
            payload.put("id", createdNotification.getId());

            return new ResponseEntity<Map<String, String>>(payload, HttpStatus.CREATED);
        };
    }
}
