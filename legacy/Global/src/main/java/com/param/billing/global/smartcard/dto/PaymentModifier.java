package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PaymentModifier")  
public class PaymentModifier {

	 private String Type;
	
	 private String NHIF_Member_Nr;
	 
	 private String NHIF_Contributor_Nr;
	 
	 private String NHIF_Employer_Code;
	
	 private String NHIF_Site_Nr;
	 
	 private String NHIF_Patient_Relation;
	 
	 private String Diagnosis_Code;
	
	 private String Admit_Date;
	
	 private String Discharge_Date;
	
	 private String Days_Used;
	 
	 private String Amount;
	 
	 private String Receipt;

	 @XmlElement(name="Type") 
	public String getType() {
		return Type;
	}
	 
	
	public void setType(String type) {
		Type = type;
	}
	 
	 @XmlElement(name="NHIF_Member_Nr") 
	public String getNHIF_Member_Nr() {
		return NHIF_Member_Nr;
	}

	public void setNHIF_Member_Nr(String nHIF_Member_Nr) {
		NHIF_Member_Nr = nHIF_Member_Nr;
	}

	@XmlElement(name="NHIF_Contributor_Nr") 
	public String getNHIF_Contributor_Nr() {
		return NHIF_Contributor_Nr;
	}

	public void setNHIF_Contributor_Nr(String nHIF_Contributor_Nr) {
		NHIF_Contributor_Nr = nHIF_Contributor_Nr;
	}

	@XmlElement(name="NHIF_Employer_Code") 
	public String getNHIF_Employer_Code() {
		return NHIF_Employer_Code;
	}

	public void setNHIF_Employer_Code(String nHIF_Employer_Code) {
		NHIF_Employer_Code = nHIF_Employer_Code;
	}

	@XmlElement(name="NHIF_Site_Nr") 
	public String getNHIF_Site_Nr() {
		return NHIF_Site_Nr;
	}

	public void setNHIF_Site_Nr(String nHIF_Site_Nr) {
		NHIF_Site_Nr = nHIF_Site_Nr;
	}

	@XmlElement(name="NHIF_MeNHIF_Patient_Relationmber_Nr") 
	public String getNHIF_Patient_Relation() {
		return NHIF_Patient_Relation;
	}

	public void setNHIF_Patient_Relation(String nHIF_Patient_Relation) {
		NHIF_Patient_Relation = nHIF_Patient_Relation;
	}

	@XmlElement(name="Diagnosis_Code") 
	public String getDiagnosis_Code() {
		return Diagnosis_Code;
	}

	public void setDiagnosis_Code(String diagnosis_Code) {
		Diagnosis_Code = diagnosis_Code;
	}

	@XmlElement(name="Admit_Date") 
	public String getAdmit_Date() {
		return Admit_Date;
	}

	public void setAdmit_Date(String admit_Date) {
		Admit_Date = admit_Date;
	}

	@XmlElement(name="Discharge_Date") 
	public String getDischarge_Date() {
		return Discharge_Date;
	}

	public void setDischarge_Date(String discharge_Date) {
		Discharge_Date = discharge_Date;
	}

	@XmlElement(name="Days_Used") 
	public String getDays_Used() {
		return Days_Used;
	}

	public void setDays_Used(String days_Used) {
		Days_Used = days_Used;
	}

	@XmlElement(name="Amount") 
	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	@XmlElement(name="Receipt") 
	public String getReceipt() {
		return Receipt;
	}

	public void setReceipt(String receipt) {
		Receipt = receipt;
	}
	 
	 
}
