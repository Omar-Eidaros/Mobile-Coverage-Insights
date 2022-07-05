package com.iti.measurement;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("/DML")
public class MeasurementAPI {

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
