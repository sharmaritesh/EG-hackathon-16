package com.egencia.hackathon.controller;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.AlertReply;
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
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
public class AlertController {

    private AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @RequestMapping(value = "alert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Void>> handleAlert(
            @Valid @RequestBody final Alert alert) {
        return () -> {
            alertService.queueAlert(alert);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        };
    }

    @RequestMapping(value = "reply", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Void>> alertReply(
            @Valid @RequestBody final Map<String, String> alertReplyParams) {
        return () -> {
            AlertReply alertReply = new AlertReply();
            alertReply.setNumber(alertReplyParams.get("number"));
            alertReply.setUserId(alertReplyParams.get("user_id"));
            alertReply.setCity(alertReplyParams.get("city"));
            alertReply.setCountry(alertReplyParams.get("country"));
            if(alertReplyParams.get("latitude") != null)
                alertReply.setLatitude(Float.parseFloat(alertReplyParams.get("latitude")));
            if(alertReplyParams.get("longitude") != null)
                alertReply.setLongitute(Float.parseFloat(alertReplyParams.get("longitude")));
            alertReply.setStatus(Boolean.valueOf(alertReplyParams.get("status")));
            alertReply.setMessage(alertReplyParams.get("message"));
            alertService.handleAlertReply(alertReply);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        };
    }

    
}
