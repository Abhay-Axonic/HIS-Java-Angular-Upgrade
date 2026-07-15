package com.param.billing.smartcard.dto;

public class AdmissionDto {

	private String admit_date;
	private String discharge_date;
	private String discharge_summary;
	public String getAdmit_date() {
		return admit_date;
	}
	public void setAdmit_date(String admit_date) {
		this.admit_date = admit_date;
	}
	public String getDischarge_date() {
		return discharge_date;
	}
	public void setDischarge_date(String discharge_date) {
		this.discharge_date = discharge_date;
	}
	public String getDischarge_summary() {
		return discharge_summary;
	}
	public void setDischarge_summary(String discharge_summary) {
		this.discharge_summary = discharge_summary;
	}
	
	
}
