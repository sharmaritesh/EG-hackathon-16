package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.NotifyCareModel;

import java.util.List;

public interface NotifyCareService {
    NotifyCareModel create(NotifyCareModel input);
    List<NotifyCareModel> findApplicableTrips(Alert alert);
}
