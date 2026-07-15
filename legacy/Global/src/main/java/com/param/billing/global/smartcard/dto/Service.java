package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Service")
public class Service {

	 private String Number;
	 
	 private String Invoice_Number;
	
	 private String Start_Date;
	
	 private String Start_Time;
	 
	 Provider Provider;
	 
	 Diagnosis Diagnosis;
	 
	 private String Encounter_Type;
	
	 private String Code_Type;
	 
	 private String Code;
	 
	 private String Code_Description;
	 
	 private String Quantity;
	
	 private String Total_Amount;
	
	 private String Reason;

	 @XmlElement(name="Number") 
	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	@XmlElement(name="Invoice_Number") 
	public String getInvoice_Number() {
		return Invoice_Number;
	}

	public void setInvoice_Number(String invoice_Number) {
		Invoice_Number = invoice_Number;
	}

	@XmlElement(name="Start_Date") 
	public String getStart_Date() {
		return Start_Date;
	}

	public void setStart_Date(String start_Date) {
		Start_Date = start_Date;
	}

	@XmlElement(name="Start_Time") 
	public String getStart_Time() {
		return Start_Time;
	}

	public void setStart_Time(String start_Time) {
		Start_Time = start_Time;
	}

	@XmlElement(name="Encounter_Type") 
	public String getEncounter_Type() {
		return Encounter_Type;
	}

	public void setEncounter_Type(String encounter_Type) {
		Encounter_Type = encounter_Type;
	}

	@XmlElement(name="Code_Type") 
	public String getCode_Type() {
		return Code_Type;
	}

	public void setCode_Type(String code_Type) {
		Code_Type = code_Type;
	}

	@XmlElement(name="Code") 
	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	@XmlElement(name="Code_Description") 
	public String getCode_Description() {
		return Code_Description;
	}

	public void setCode_Description(String code_Description) {
		Code_Description = code_Description;
	}

	@XmlElement(name="Quantity") 
	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	@XmlElement(name="Total_Amount") 
	public String getTotal_Amount() {
		return Total_Amount;
	}

	public void setTotal_Amount(String total_Amount) {
		Total_Amount = total_Amount;
	}

	@XmlElement(name="Reason") 
	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public Provider getProvider() {
		return Provider;
	}

	public void setProvider(Provider provider) {
		Provider = provider;
	}

	public Diagnosis getDiagnosis() {
		return Diagnosis;
	}

	public void setDiagnosis(Diagnosis diagnosis) {
		Diagnosis = diagnosis;
	}

	 
}
