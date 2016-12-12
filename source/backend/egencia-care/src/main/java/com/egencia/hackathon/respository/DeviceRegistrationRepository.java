package com.egencia.hackathon.respository;

import com.egencia.hackathon.model.DeviceRegistrationModel;
import com.egencia.hackathon.model.NotifyCareModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRegistrationRepository extends MongoRepository<DeviceRegistrationModel, String> {
    DeviceRegistrationModel findByNumber(String number);
}
