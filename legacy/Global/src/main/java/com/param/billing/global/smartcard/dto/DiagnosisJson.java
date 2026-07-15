package com.param.billing.global.smartcard.dto;

public class DiagnosisJson {
	
	 private String coding_standard;   
	 private String code;     
	 private String name;    
	 private Boolean is_primary;
	 
	public String getCoding_standard() {
		return coding_standard;
	}
	public void setCoding_standard(String coding_standard) {
		this.coding_standard = coding_standard;
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
	public Boolean getIs_primary() {
		return is_primary;
	}
	public void setIs_primary(Boolean is_primary) {
		this.is_primary = is_primary;
	}
	 
	 

}
