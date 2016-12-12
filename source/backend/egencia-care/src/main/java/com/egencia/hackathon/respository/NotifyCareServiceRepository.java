package com.egencia.hackathon.respository;

import com.egencia.hackathon.model.NotifyCareModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("notifycareRepository")
public interface NotifyCareServiceRepository extends MongoRepository<NotifyCareModel, String> {

    List<NotifyCareModel> findAllByCityAndCountry(String city, String country);
}
