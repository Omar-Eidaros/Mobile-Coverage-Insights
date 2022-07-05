package com.insights.verification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioClass {

    private static final String ACCOUNT_SID = "ACc73541e70d0f014ad651ed5b2830ab6f";
    private static final String AUTH_TOKEN = "f45f3bdfe65e789df56878fb289b2372";
    private static final String PHONE_NUM = "+19897955893";

    public static void createMessage(String msisdn, String verifyCode){
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber("+2"+msisdn), new PhoneNumber(PHONE_NUM), "Your Verification Code is: " + verifyCode).create();

        System.out.println(message.getSid());
    }
}
