package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Claim_Header")  
public class Claim_Header {

	 private String Invoice_Number;
	
	 private String Claim_Date;
	 
	 private String Claim_Time;
	
	 private String Pool_Number;
	
	 private String Total_Services;
	
	 private String Gross_Amount;
	 
	 Provider Provider;
	
	 Authorization Authorization;
	 
	 Payment_Modifiers Payment_Modifiers;
	 
	 @XmlElement(name="Invoice_Number") 
	public String getInvoice_Number() {
		return Invoice_Number;
	}

	public void setInvoice_Number(String invoice_Number) {
		Invoice_Number = invoice_Number;
	}

	@XmlElement(name="Claim_Date") 
	public String getClaim_Date() {
		return Claim_Date;
	}

	public void setClaim_Date(String claim_Date) {
		Claim_Date = claim_Date;
	}

	@XmlElement(name="Claim_Time") 
	public String getClaim_Time() {
		return Claim_Time;
	}

	public void setClaim_Time(String claim_Time) {
		Claim_Time = claim_Time;
	}

	@XmlElement(name="Pool_Number") 
	public String getPool_Number() {
		return Pool_Number;
	}

	public void setPool_Number(String pool_Number) {
		Pool_Number = pool_Number;
	}

	@XmlElement(name="Total_Services") 
	public String getTotal_Services() {
		return Total_Services;
	}

	public void setTotal_Services(String total_Services) {
		Total_Services = total_Services;
	}

	@XmlElement(name="Gross_Amount") 
	public String getGross_Amount() {
		return Gross_Amount;
	}

	public void setGross_Amount(String gross_Amount) {
		Gross_Amount = gross_Amount;
	}

	public Provider getProvider() {
		return Provider;
	}

	public void setProvider(Provider provider) {
		Provider = provider;
	}

	public Authorization getAuthorization() {
		return Authorization;
	}

	public void setAuthorization(Authorization authorization) {
		Authorization = authorization;
	}

	public Payment_Modifiers getPayment_Modifiers() {
		return Payment_Modifiers;
	}

	public void setPayment_Modifiers(Payment_Modifiers payment_Modifiers) {
		Payment_Modifiers = payment_Modifiers;
	}

	
	 
}
