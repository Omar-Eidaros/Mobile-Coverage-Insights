package com.insights.mesurements;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/DML")
public class MeasurementAPI {
	@GET
	@Path("/hello")
	@Produces()
	public String hello (){
		return "hello api";
	}
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMeasurement(Measurement measurement) {
		String result =null;
		
		int state = measurement.insertMeasurement(measurement);
		
		if(state == 1) {
	      result = "Measurement is Inserted";
		}
			
		else if(state == 0)	{
			 result = "Measurement is not Inserted";
		}
		return Response.status(200).entity(result).build();
		
              		
	}
	
	@GET
	@Path("/getAllMeasurements")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMeasurements() {
		try {
			List<Measurement> measurements = Measurement.getAllMeasurements();
			GenericEntity<List<Measurement>> genericEntity = new GenericEntity<List<Measurement>>(measurements){};
			return Response.ok(genericEntity, MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("/countAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response countAll() {
		return Response.status(200).entity(Counter.countAll()).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("/getAllMeasurements/{operator}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMeasurementsByOperator(@PathParam("operator") String operator) {
		Measurement operatorMeasurement = new Measurement();
		try {
			List<Measurement> measurements = operatorMeasurement.getMeasurementsByOperator(operator);
			GenericEntity<List<Measurement>> genericEntity = new GenericEntity<List<Measurement>>(measurements){};
			return Response.ok(genericEntity, MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").build();
		}
	}
	
	
}
