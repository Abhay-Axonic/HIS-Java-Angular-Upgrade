package com.param.global.dto;


public class DrugMasterDto {

	private int drugId;
	private String genericName;
	private String medicationName;
	private char status;
	private String createdDate;
	private String updatedDate;
	private int createdBy;
	private int updatedBy;
	private int organizationId;
	private int unitId;
	private char isVaccineStatus;
	private String dose;
	private Integer roa_id;
	private String roa_details;
	
	public int getDrugId() {
		return drugId;
	}
	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	public String getMedicationName() {
		return medicationName;
	}
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public char getIsVaccineStatus() {
		return isVaccineStatus;
	}
	public void setIsVaccineStatus(char isVaccineStatus) {
		this.isVaccineStatus = isVaccineStatus;
	}
	public String getDose() {
		return dose;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}
	public Integer getRoa_id() {
		return roa_id;
	}
	public void setRoa_id(Integer roa_id) {
		this.roa_id = roa_id;
	}
	public String getRoa_details() {
		return roa_details;
	}
	public void setRoa_details(String roa_details) {
		this.roa_details = roa_details;
	}
	
	
}
