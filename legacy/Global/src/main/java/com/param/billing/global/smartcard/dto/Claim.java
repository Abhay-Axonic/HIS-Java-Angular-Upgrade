package com.param.billing.global.smartcard.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name="Claim")  

public class Claim  implements Serializable{

	 Claim_Header claim_Header;
	
	 Member member;
	
	 Patient patient;
	
	 Claim_Data claim_Data;
	 
	 private String uhidNumber;
	 

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Claim_Header getClaim_Header() {
		return claim_Header;
	}

	public void setClaim_Header(Claim_Header claim_Header) {
		this.claim_Header = claim_Header;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}


	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Claim_Data getClaim_Data() {
		return claim_Data;
	}

	public void setClaim_Data(Claim_Data claim_Data) {
		this.claim_Data = claim_Data;
	}
	 
	

	
	 
}
