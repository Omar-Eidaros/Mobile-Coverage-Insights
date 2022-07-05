package com.insights.apis;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.insights.verification.*;

@Path("/verification")
public class VerficationAPI {

    @POST
    @Path("/sendCode")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendCode(Verification verification){
        String result = null;

        int state = verification.sendCode(verification);

        if (state == 1){
            result = "Verification Code have been sent";
        } else {
            result ="Verification Code not received yet";
        }
        return Response.status(200).status(state).build();
    }
}
