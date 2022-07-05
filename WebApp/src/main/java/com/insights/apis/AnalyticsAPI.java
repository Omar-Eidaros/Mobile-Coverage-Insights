package com.iti.analytics;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.codehaus.jackson.map.ObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iti.measurement.DataBase;
import com.iti.measurement.Measurement;

@Path("/")

public class AnalyticsAPI {

	@GET
	@Path("/getSignalStregnthByLocation/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getSignalStregnthByLocation(@PathParam("latitude") String latitude,@PathParam("longitude") String longitude){
		  DataBase db = new DataBase();
			db.connect();
			HashMap<String, String> result = new HashMap<String, String>();
			ResultSet rs;
			try {
				rs = db.select("select operator,avg(CAST(signal_strength_level as int)) as signal_strength from measurements where latitude='"+latitude+"' and longitude='"+longitude+"' group by operator;");
				while(rs.next()){
			    	result.put(rs.getString("operator"),rs.getString("signal_strength"));
			    }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.disconnect();
		    ObjectMapper objectMapper = new ObjectMapper();

	      String json;
			try {
				 json = objectMapper.writeValueAsString(result);
				 return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
			} catch (JsonProcessingException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} catch (IOException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
	}
	
	@GET
	@Path("/getSignalStregnthPerOperator")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getSignalStregnthByOperator(){
		  DataBase db = new DataBase();
			db.connect();
			HashMap<String, String> result = new HashMap<String, String>();
			
			ResultSet rs;
			try {
				rs = db.select("select operator,avg(CAST(signal_strength_level as int)) as signal_strength from measurements group by operator;");
				while(rs.next()){
			    	result.put(rs.getString("operator"),rs.getString("signal_strength"));
			    }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.disconnect();
		    ObjectMapper objectMapper = new ObjectMapper();

	      String json;
			try {
				 json = objectMapper.writeValueAsString(result);
				 return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
			} catch (JsonProcessingException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} catch (IOException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
	}
	

	@GET
	@Path("/getDevicesInsights")
	@Produces(MediaType.APPLICATION_JSON)  
     public Response getDevicesInsights(){
    	 DataBase db = new DataBase();
		 db.connect();
		 HashMap<String, String> result = new HashMap<String, String>();
		 ResultSet rs;
			try {
				rs = db.select("select device_model,count(distinct imei) from measurements group by device_model;");
				while(rs.next()){
			    	result.put(rs.getString("device_model"),rs.getString("count"));
			    }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.disconnect();
		    ObjectMapper objectMapper = new ObjectMapper();

	      String json;
			try {
				 json = objectMapper.writeValueAsString(result);
				 return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
			} catch (JsonProcessingException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} catch (IOException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
     }
	
	@GET
	@Path("/getDevicesInsightsByLocation/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)  
     public Response getDevicesInsightsByLocation(@PathParam("latitude") String latitude,@PathParam("longitude") String longitude){
    	 DataBase db = new DataBase();
		 db.connect();
		 HashMap<String, String> result = new HashMap<String, String>();
		 ResultSet rs;
			try {
				rs = db.select("select device_model,count(distinct imei) from measurements where latitude='"+latitude+"' and longitude='"+longitude+"' group by device_model;");
				while(rs.next()){
			    	result.put(rs.getString("device_model"),rs.getString("count"));
			    }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.disconnect();
		    ObjectMapper objectMapper = new ObjectMapper();

	      String json;
			try {
				 json = objectMapper.writeValueAsString(result);
				 return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
			} catch (JsonProcessingException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} catch (IOException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
     }

	
	@GET
	@Path("/getDevicesInsightsByOperator/{operator}")
	@Produces(MediaType.APPLICATION_JSON)  
     public Response getDevicesInsightsByOperator(@PathParam("operator") String operator){
    	 DataBase db = new DataBase();
		 db.connect();
		 HashMap<String, String> result = new HashMap<String, String>();
		 ResultSet rs;
			try {
				rs = db.select("select device_model,count(distinct imei) from measurements where operator ILIKE '"+operator+"' group by device_model;");
				while(rs.next()){
			    	result.put(rs.getString("device_model"),rs.getString("count"));
			    }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.disconnect();
		    ObjectMapper objectMapper = new ObjectMapper();

	      String json;
			try {
				 json = objectMapper.writeValueAsString(result);
				 return Response.status(200).entity(json).header("Access-Control-Allow-Origin", "*").build();
			} catch (JsonProcessingException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} catch (IOException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
     }
	
	@GET
	@Path("/getOperatorCode")
	@Produces(MediaType.APPLICATION_JSON)  
	public Response getOperatorCode(){
		  DataBase db = new DataBase();
			db.connect();
			ResultSet rs;
			List<OperatorCode> opertorCode = new ArrayList<OperatorCode>();
			try {
				rs = db.select("select distinct country,operator,mcc,mnc from measurements;");
				while(rs.next()){
					opertorCode.add(new OperatorCode(rs.getString("mcc"),rs.getString("mnc"),rs.getString("country"),rs.getString("operator")));
			    }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.disconnect();
			try{
				GenericEntity<List<OperatorCode>> genericEntity = new GenericEntity<List<OperatorCode>>(opertorCode){};
				return Response.ok(genericEntity, MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
	}
}
