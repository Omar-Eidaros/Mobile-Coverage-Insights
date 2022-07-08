package com.insights.verification;

import com.insights.mesurements.DataBase;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/verification")
public class VerificationAPI {
    private static final String ACCOUNT_SID = "ACc73541e70d0f014ad651ed5b2830ab6f";
    private static final String AUTH_TOKEN = "f0a6d693e4a83912aa3db8e3c05626fd";
    private static final String PHONE_NUM = "+19897955893";

    @POST
    @Path("/sendCode")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendCode(Verification verification){
        //generate the user id and verification code
        verification.setVerifCode(verification.generateCode(5));
        verification.setId(Integer.parseInt(verification.generateCode(3)));
        String state = null;
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+2"+verification.getMsisdn()), new PhoneNumber(PHONE_NUM), "Your Verification Code is: " + verification.getVerifCode()).create();
      System.out.println(message.getSid());

       if(message.getSid().equals(null))
        //if (1==2)
            state = "Failed to send Message:";
        else {
            state   = "Message Sent Successfully:";

            // Insert generated id and code with msisdn to db
           // DataBase db = new DataBase();
           // db.connect();
            int result = -1;
  //              try {
                   System.out.println("id : "+verification.getId() +"msisdn :" + verification.getMsisdn() +"verfication code "+verification.getVerifCode());
                //    result = db.DML("Insert into verfication (id) values (" + verification.getId() + ");");
                    verification.insertVerif(verification);
//                } catch (SQLException e) {
//                    System.out.println("There is error in Insert Statement");
//                    e.printStackTrace();
//                }
             //   db.disconnect();
        }

        return Response.status(200).entity(state).build();
    }

    @POST
    @Path("/isCodeValid")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkCode(Verification verification){
        String result = "{\"verified\":\"false\"}";
        String state = verification.checkCode(verification);

        if (state.equals("1")){
            result = "{\"verified\":\"true\"}";
            return Response.status(200).entity(result).type(MediaType.APPLICATION_JSON).build();
        } else {
            System.out.println("Verification Code is not exist");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
