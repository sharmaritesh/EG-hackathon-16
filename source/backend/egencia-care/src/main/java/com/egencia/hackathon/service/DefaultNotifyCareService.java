package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.NotifyCareModel;
import com.egencia.hackathon.respository.NotifyCareServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultNotifyCareService implements NotifyCareService {

    private NotifyCareServiceRepository notifyCareServiceRepository;

    @Autowired
    public DefaultNotifyCareService(NotifyCareServiceRepository notifyCareServiceRepository) {
        this.notifyCareServiceRepository = notifyCareServiceRepository;
    }

    @Override
    public NotifyCareModel create(NotifyCareModel input) {
        return notifyCareServiceRepository.save(input);
    }

    @Override
    public List<NotifyCareModel> findApplicableTrips(final Alert alert) {
        return  notifyCareServiceRepository.findAllByCityAndCountry(alert.getCity(), alert.getCountry());
    }
}
