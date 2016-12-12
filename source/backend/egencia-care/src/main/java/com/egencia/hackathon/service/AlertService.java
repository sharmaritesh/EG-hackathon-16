package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.AlertReply;

public interface AlertService {
    String queueAlert(Alert alert);
    void handleAlert(Alert alert);
    void handleAlertReply(AlertReply alertReply);
}
