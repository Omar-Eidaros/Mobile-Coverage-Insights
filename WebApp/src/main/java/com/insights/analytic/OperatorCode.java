package com.insights.analytic;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OperatorCode {
 String mcc;
 String mnc;
 String country;
 String operator;
public OperatorCode(String mcc, String mnc, String country, String operator) {
	super();
	this.mcc = mcc;
	this.mnc = mnc;
	this.country = country;
	this.operator = operator;
}

public OperatorCode() {
	super();
}

public String getMcc() {
	return mcc;
}

public void setMcc(String mcc) {
	this.mcc = mcc;
}

public String getMnc() {
	return mnc;
}

public void setMnc(String mnc) {
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


}
