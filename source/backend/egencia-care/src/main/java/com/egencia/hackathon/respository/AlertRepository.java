package com.egencia.hackathon.respository;

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.DeviceRegistrationModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {
}
