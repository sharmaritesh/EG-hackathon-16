package com.egencia.hackathon.controller;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.NotifyCareModel;
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

    @RequestMapping(value = "alert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<Void>> handleAlert(
            @Valid @RequestBody final Alert alert) {


        return null;
    }
}
