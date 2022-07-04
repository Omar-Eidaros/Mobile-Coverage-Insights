package com.insights.verification;

import com.insights.mesurements.DataBase;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

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
					e.printStackTrace();
				}
			} else {
				try {
					result = db.DML("Insert into  verification (msisdn,verifCode) VALUES ('" + ver.getMsisdn() + "','"
							+ ver.getVerifCode() + "');");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
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
			e.printStackTrace();
		}
		db.disconnect();
		System.out.print(y);
		return y;
	}

	public void sendCode(Verification ver){
		ver.setVerifCode(ver.generateCode(5));
		System.out.println(ver.verifCode);
		insertVerif(ver);
		String content = "Your Verfication Code is: " + ver.getVerifCode();
		TwilioClass.createMessage(ver.getMsisdn(), content);

	}

	public String generateCode(int length){

		String numbers = "0123456789";
		Random random = new Random();
		char[] code = new char[length];

		for (int i=0; i < length; i++)
		{
			code[i] = numbers.charAt(random.nextInt(numbers.length()));
		}
		return code.toString();
	}
}
