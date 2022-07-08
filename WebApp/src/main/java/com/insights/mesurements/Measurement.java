package com.insights.mesurements;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement

public class Measurement {

	int id;
	String msisdn;
	long cell_id;
	String cell_type;
	int mcc;
	int mnc;
	String country;
	String operator;
	int signal_strength_level;
	String imei;
	String imsi;
	String latitude;
	String longitude;
	String device_model;

	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	public Measurement() {
		
	}
	public Measurement(int id, String msisdn, long cell_id, String cell_type, int mcc, int mnc, String country,
			String operator, int signal_strength_level, String imei, String imsi, String latitude, String longitude,String device_model) {
		super();
		this.device_model = device_model;
		this.id = id;
		this.msisdn = msisdn;
		this.cell_id = cell_id;
		this.cell_type = cell_type;
		this.mcc = mcc;
		this.mnc = mnc;
		this.country = country;
		this.operator = operator;
		this.signal_strength_level = signal_strength_level;
		this.imei = imei;
		this.imsi = imsi;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getMsisdn() {
		return msisdn;
	}
	
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	public String  getLatitude() {
		return latitude;
	}
	public void setLatitude(String  latitude) {
		this.latitude = latitude;
	}
	public String  getLongitude() {
		return longitude;
	}
	public void setLongitude(String  longitude) {
		this.longitude = longitude;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCell_id() {
		return cell_id;
	}
	public void setCell_id(long cell_id) {
		this.cell_id = cell_id;
	}
	
	public String getCell_type() {
		return cell_type;
	}
	public void setCell_type(String cell_type) {
		this.cell_type = cell_type;
	}
	
	public int getMcc() {
		return mcc;
	}
	public void setMcc(int mcc) {
		this.mcc = mcc;
	}
	public int getMnc() {
		return mnc;
	}
	public void setMnc(int mnc) {
		this.mnc = mnc;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public int getSignal_strength_level() {
		return signal_strength_level;
	}
	public void setSignal_strength_level(int signal_strength_level) {
		this.signal_strength_level = signal_strength_level;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	
	
	public int insertMeasurement(Measurement measurement) {
		DataBase db = new DataBase();
		int result = -1;
		db.connect();
		  
		   try {
			   result = db.DML("Insert into  measurements (cell_id,msisdn_id,cell_type,"
					   + "mcc,mnc,country,operator,signal_strength_level,imei,imsi,latitude,longitude,device_model)"
					   + " values("+measurement.getCell_id()+",'"+measurement.getMsisdn()+"','"+
					   measurement.getCell_type()+"',"+
					   measurement.getMcc()+","+measurement.getMnc()+",'"+measurement.getCountry()+
					   "','"+measurement.getOperator()+
					   "',"+measurement.getSignal_strength_level()+",'"+measurement.getImei()+
					   "',"+measurement.getImsi()+","+measurement.getLatitude()+","+
					   measurement.getLongitude()+",'"+measurement.getDevice_model()+"');");

//			result = db.DML("Insert into  measurements (cell_id,msisdn,cell_type,"
//					+ "mcc,mnc,country,operator,signal_strength_level,imei,imsi,latitude,longitude,device_model)"
//					+ " values("+measurement.getCell_id()+",'"+measurement.getMsisdn()+"','"+
//					measurement.getCell_type()+"',"+
//					measurement.getMcc()+","+measurement.getMnc()+",'"+measurement.getCountry()+
//					"','"+measurement.getOperator()+
//					"',"+measurement.getSignal_strength_level()+",'"+measurement.getImei()+
//					"',"+measurement.getImsi()+","+measurement.getLatitude()+","+
//					measurement.getLongitude()+",'"+measurement.getDevice_model()+"');");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			   System.out.println("i am here");
			e.printStackTrace();
		}
		   db.disconnect();
		   return result;
	}

	
public static List<Measurement> getAllMeasurements()
{

	DataBase db = new DataBase();
	db.connect();
	List<Measurement> measurements = new ArrayList<Measurement>();
	
	try {
		ResultSet rs = db.select("select id,cell_id,msisdn_id,cell_type,mcc,mnc,country,operator,avg(CAST(signal_strength_level as int)) as signal_strength_level,imei,imsi,latitude,longitude,device_model from measurements group by latitude,longitude,cell_id,msisdn_id,id,cell_type,mcc,mnc,country,operator,imei,imsi,device_model;");
		while (rs.next()) {
			measurements.add(new Measurement(rs.getInt("id"),rs.getString("msisdn_id"), rs.getLong("cell_id"),rs.getString("cell_type"),rs.getInt("mcc"),rs.getInt("mnc"), rs.getString("country"),
					rs.getString("operator"), rs.getInt("signal_strength_level"),rs.getString("imei"), rs.getString("imsi"), rs.getString("latitude"), rs.getString("longitude"),rs.getString("device_model")));	
	         
	    }
	     

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	db.disconnect();

	return measurements;
}

public List<Measurement> getMeasurementsByOperator(String operator) {
	DataBase db = new DataBase();
	db.connect();
	List<Measurement> measurements = new ArrayList<Measurement>();
	
	try {
		ResultSet rs = db.select("select id,cell_id,msisdn_id,cell_type,mcc,mnc,country,operator,avg(CAST(signal_strength_level as int)) as signal_strength_level,"
				+ "imei,imsi,latitude,longitude,device_model from measurements where "
				+ "operator LIKE '"+operator+"' group by latitude,longitude,cell_id,msisdn_id,id,cell_type,mcc,mnc,country,operator,imei,imsi,device_model;");
		while (rs.next()) {
			measurements.add(new Measurement(rs.getInt("id"),rs.getString("msisdn_id"), rs.getLong("cell_id"),rs.getString("cell_type"),rs.getInt("mcc"),rs.getInt("mnc"), rs.getString("country"),
					rs.getString("operator"), rs.getInt("signal_strength_level"),rs.getString("imei"), rs.getString("imsi"), rs.getString("latitude"), rs.getString("longitude"),rs.getString("device_model"))); 
	    }
	     
	} catch (SQLException e) {
		e.printStackTrace();
	}
	db.disconnect();

	return measurements;
}
}
