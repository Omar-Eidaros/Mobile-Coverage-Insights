package com.insights.mesurements;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;
@XmlRootElement
public class Counter {
	int countriesNo;
	int operatorsNo;
	int cellsNo;
	int measurementsNo;
	public int getMeasurementsNo() {
		return measurementsNo;
	}
	public void setMeasurementsNo(int measurementsNo) {
		this.measurementsNo = measurementsNo;
	}
	String date;
	public int getCountriesNo() {
		return countriesNo;
	}
	public void setCountriesNo(int countriesNo) {
		this.countriesNo = countriesNo;
	}
	public int getOperatorsNo() {
		return operatorsNo;
	}
	public void setOperatorsNo(int operatorsNo) {
		this.operatorsNo = operatorsNo;
	}
	public int getCellsNo() {
		return cellsNo;
	}
	public void setCellsNo(int cellsNo) {
		this.cellsNo = cellsNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Counter(int countriesNo, int operatorsNo, int cellsNo,int measurementsNo, String date) {
		super();
		this.countriesNo = countriesNo;
		this.operatorsNo = operatorsNo;
		this.cellsNo = cellsNo;
		this.measurementsNo = measurementsNo;
		this.date = date;
	}
	public Counter() {
		
	}
	public static Counter countAll()
	{
        
		DataBase db = new DataBase();
		db.connect();
		Counter counter = new Counter();
		try {
			db.DML("update measurements set operator=REPLACE (operator, ' EG', '') where operator Like '% EG'");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			db.DML("update measurements set operator=LOWER(operator);");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			ResultSet rs = db.select("select DATE(now()) As Date,count(id) As \"measurements no\",COUNT(Distinct cell_id) As \"cell no\",count(Distinct operator) As \"operator no\",count(Distinct country) As \"contry no\" from measurements;");
			while (rs.next()) {
				counter.setCountriesNo(rs.getInt("contry no"));
				counter.setOperatorsNo(rs.getInt("operator no"));
				counter.setCellsNo(rs.getInt("cell no"));
				counter.setMeasurementsNo(rs.getInt("measurements no"));
				counter.setDate(rs.getString("date"));	
		      }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.disconnect();

		return counter;
	}
	

}
