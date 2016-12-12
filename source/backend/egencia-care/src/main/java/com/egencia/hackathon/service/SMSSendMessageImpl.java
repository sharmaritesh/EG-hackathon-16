package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by gurssingh on 12/12/16.
 */
@Component
@Qualifier("SMSSendMessageImpl")
public class SMSSendMessageImpl implements SendMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSSendMessageImpl.class);

    public SMSSendMessageImpl() {
        super();
    }

    @Override
    public boolean send(Message message) {
        boolean status = false;
        try {
            com.twilio.rest.api.v2010.account.Message messageTwillio =
                    com.twilio.rest.api.v2010.account.Message.creator(new PhoneNumber(message.getNumber()),
                            new PhoneNumber(TwillioConstants.FROM_PHONE_NUMBER),
                            message.getText()).create();
            LOGGER.info(messageTwillio.getSid());
            status = true;
        } catch (Exception e) {
            LOGGER.error("Unable to send SMS to emergency contact os user", e);
        }
        return status;
    }
}
