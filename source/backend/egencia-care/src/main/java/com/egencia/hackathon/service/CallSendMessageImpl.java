package com.egencia.hackathon.service;

import com.egencia.hackathon.model.Message;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Created by gurssingh on 12/12/16.
 */
@Component
@Qualifier("CallSendMessageImpl")
public class CallSendMessageImpl extends AbstractSendMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallSendMessageImpl.class);

    public CallSendMessageImpl() {
        super();
    }

    @Override
    public boolean send(Message message) {
        boolean status = false;
        try {
            Call call = Call.creator(new PhoneNumber(message.getNumber()), new PhoneNumber(TwillioConstants.FROM_PHONE_NUMBER),
                    new URI("https://demo.twilio.com/docs/voice.xml")).create();
                    //new URI("https://github.com/sharmaritesh/EG-hackathon-16/blob/master/source/backend/egencia-care/src/main/resources/RecordedMessage.xml")).create();
            LOGGER.info(call.getSid());
            status = true;
        } catch (Exception e) {
                LOGGER.error("Unable to call emergency contact os user", e);
        }
        return status;
    }
}
