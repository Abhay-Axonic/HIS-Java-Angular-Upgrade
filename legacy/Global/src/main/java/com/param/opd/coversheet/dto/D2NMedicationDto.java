package com.param.opd.coversheet.dto;

public class D2NMedicationDto {

	private Integer itemId;
	
	private String itemDesc;
	
	private String administrationTime;
	
	private String admissionTime;
	private Integer admissionId;
	private String difference;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public String getAdmissionTime() {
		return admissionTime;
	}

	public void setAdmissionTime(String admissionTime) {
		this.admissionTime = admissionTime;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	
	
}
