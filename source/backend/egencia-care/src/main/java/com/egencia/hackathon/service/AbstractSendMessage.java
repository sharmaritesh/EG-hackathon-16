package com.egencia.hackathon.service;

import com.twilio.Twilio;

/**
 * Created by gurssingh on 12/12/16.
 */
public abstract class AbstractSendMessage implements SendMessage {

    public AbstractSendMessage() {
        Twilio.init(TwillioConstants.ACCOUNT_SID, TwillioConstants.AUTH_TOKEN);
    }
}
