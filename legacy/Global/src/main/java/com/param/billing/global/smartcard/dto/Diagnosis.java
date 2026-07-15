package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Diagnosis") 
public class Diagnosis {

	 private String Stage;
	 
	 private String Code_Type;
	
	 private String Code;

	 @XmlElement(name="Stage") 
	public String getStage() {
		return Stage;
	}

	public void setStage(String stage) {
		Stage = stage;
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
	 
	 
}
