package com.param.opd.coversheet.dto;

/*
 * Created by Tushar
 */
public class FrequencyMasterDTO {
	
	private int frequency_id;
	private String frequency;
	private Integer noOfAdministrationTime;
	private String code;
	public Integer getNoOfAdministrationTime() {
		return noOfAdministrationTime;
	}
	public void setNoOfAdministrationTime(Integer noOfAdministrationTime) {
		this.noOfAdministrationTime = noOfAdministrationTime;
	}
	public int getFrequency_id() {
		return frequency_id;
	}
	public void setFrequency_id(int frequency_id) {
		this.frequency_id = frequency_id;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	

}
