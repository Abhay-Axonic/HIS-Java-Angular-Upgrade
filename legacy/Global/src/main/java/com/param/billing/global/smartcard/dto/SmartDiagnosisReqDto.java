package com.param.billing.global.smartcard.dto;

public class SmartDiagnosisReqDto {
	
	private String codingStandard;
	private Boolean isPrimary;
	private String code;
	private String name;
	public String getCodingStandard() {
		return codingStandard;
	}
	public void setCodingStandard(String codingStandard) {
		this.codingStandard = codingStandard;
	}
	public Boolean getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
