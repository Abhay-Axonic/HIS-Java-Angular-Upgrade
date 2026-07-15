package com.param.billing.global.smartcard.dto;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Claim_Data")  
public class Claim_Data {

	 private String Discharge_Notes;
	
	 Service Service;
	 
	 @XmlElement(name="Discharge_Notes") 
	public String getDischarge_Notes() {
		return Discharge_Notes;
	}
	public void setDischarge_Notes(String discharge_Notes) {
		Discharge_Notes = discharge_Notes;
	}
	public Service getService() {
		return Service;
	}
	public void setService(Service service) {
		Service = service;
	}
	
	
	 
	
}
