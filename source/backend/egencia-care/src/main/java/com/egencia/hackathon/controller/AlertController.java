package com.egencia.hackathon.controller;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.AlertReply;
import com.egencia.hackathon.service.AlertHandler;
import com.egencia.hackathon.service.AlertService;
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
public class AlertController {

    private AlertService alertService;
    private AlertHandler alertHandler;

    @Autowired
    public AlertController(AlertService alertService, AlertHandler alertHandler) {
        this.alertService = alertService;
        this.alertHandler = alertHandler;
    }

    @RequestMapping(value = "/alert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Void>> handleAlert(
            @Valid @RequestBody final Alert alert) {
        return () -> {
            alertService.queueAlert(alert);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        };
    }

    @RequestMapping(value = "/reply", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Map>> alertReply(
            @RequestBody final AlertReply alertReply) {
        return () -> {
            alertHandler.handleAlertReply(alertReply);
            Map<String, Boolean> map = new HashMap<>();
            map.put("status", true);
            return new ResponseEntity<Map>(map, HttpStatus.ACCEPTED);
        };
    }

    
}
