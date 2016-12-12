package com.egencia.hackathon.controller;

import com.egencia.hackathon.model.NotifyCareModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class NotificationController {

    @RequestMapping(value = "notifycare", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE
    , produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<NotifyCareModel>> notifyCareSystem() {
        return () -> {
            return new ResponseEntity<NotifyCareModel>(new NotifyCareModel(), HttpStatus.OK);
        };
    }
}
