package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Member") 
public class Member {

	 private String Membership_Number;
	 
	 private String card_serialnumber;
	
	 private String Scheme_Code;
	 
	 private String Scheme_Plan;

	 @XmlElement(name="Discharge_Notes") 
	public String getMembership_Number() {
		return Membership_Number;
	}

	public void setMembership_Number(String membership_Number) {
		Membership_Number = membership_Number;
	}

	 @XmlElement(name="Discharge_Notes") 
	public String getCard_serialnumber() {
		return card_serialnumber;
	}

	public void setCard_serialnumber(String card_serialnumber) {
		this.card_serialnumber = card_serialnumber;
	}

	 @XmlElement(name="Scheme_Code") 
	public String getScheme_Code() {
		return Scheme_Code;
	}

	
	public void setScheme_Code(String scheme_Code) {
		Scheme_Code = scheme_Code;
	}

	 @XmlElement(name="Scheme_Plan") 
	public String getScheme_Plan() {
		return Scheme_Plan;
	}

	public void setScheme_Plan(String scheme_Plan) {
		Scheme_Plan = scheme_Plan;
	}
	 
	 
}
