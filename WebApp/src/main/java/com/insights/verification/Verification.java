package com.insights.verification;

import com.insights.mesurements.DataBase;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;

@XmlRootElement

public class Verification {

	String msisdn;
	String verifCode;
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getVerifCode() {
		return verifCode;
	}

	public void setVerifCode(String verifCode) {
		this.verifCode = verifCode;
	}

	public int insertVerif(Verification ver) {
		DataBase db = new DataBase();
		int result = -1;
		ResultSet rs = null;

		db.connect();
		try {
			rs = db.select("select count(*) from verification  where msisdn like '" + ver.getMsisdn() + "';");
			if (rs.next() && rs.getString("count").equals("1")) {
				try {
					result = db.DML("Update verification set verifCode = '" + ver.getVerifCode() + "' where msisdn='"
							+ ver.getMsisdn() + "';");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {
				try {
					result = db.DML("Insert into  verification (msisdn,verifCode) VALUES ('" + ver.getMsisdn() + "','"
							+ ver.getVerifCode() + "');");

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		db.disconnect();
		return result;
	}

	String checkCode(Verification ver) {
		DataBase db = new DataBase();
		String y = null;
		db.connect();
		try {
			System.out.println(ver.getMsisdn() + ":" + ver.getVerifCode());
			ResultSet rs = db.select("select count(*) from verification  where msisdn like '" + ver.getMsisdn()
					+ "' and verifcode like '" + ver.getVerifCode() + "';");
			if (rs.next()) {
				y = rs.getString("count");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.disconnect();
		System.out.print(y);
		return y;
	}
}
