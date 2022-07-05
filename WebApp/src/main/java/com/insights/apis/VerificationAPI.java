package com.insights.apis;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.insights.verification.*;

@Path("/verification")
public class VerificationAPI {

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
        return Response.status(200).entity(state).build();
    }

    @GET
    @Path("isCodeValid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkCode(Verification verification){

        String result = verification.checkCode(verification);

        if (result.equals("1")){
            System.out.println("Verification Code is Valid");
            return Response.status(200).entity(result).build();
        } else {
            System.out.println("Verification Code is not exist");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
