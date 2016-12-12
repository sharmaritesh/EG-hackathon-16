package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;

public interface AlertService {
    void queueAlert(Alert alert);
    void handleAlert(Alert alert);
}
