package com.param.global.doctor.dto;

import java.math.BigDecimal;

public class OrdersetMedicationDetailsDto {
	private int ordersetMedicationDetailsId;
	private Integer ordersetMasterId;
	private Integer itemMasterId;
	private Integer dose;
	private Integer doseUnitId;
	private Integer routeId;
	private Integer frequencyId;
	private Integer genericId;
	private Integer formulationTypeId;
	private String genericName;
	private Integer instructionId;
	private String instructionDesc;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private String frequencyDesc;
	private String strengthDesc;
	private String roaDesc;
	private String itemName;
	private Integer noOfAdministrationTime;
	private String diagnosisName;
	private Character isDeleted;
	private String ordersetName;
	private BigDecimal rate;

	private String doctorName;
	private String specialityName;
	private String unit;
	private String formulationType;
	
	public int getOrdersetMedicationDetailsId() {
		return ordersetMedicationDetailsId;
	}

	public void setOrdersetMedicationDetailsId(int ordersetMedicationDetailsId) {
		this.ordersetMedicationDetailsId = ordersetMedicationDetailsId;
	}

	public Integer getOrdersetMasterId() {
		return ordersetMasterId;
	}

	public void setOrdersetMasterId(Integer ordersetMasterId) {
		this.ordersetMasterId = ordersetMasterId;
	}

	public Integer getItemMasterId() {
		return itemMasterId;
	}

	public void setItemMasterId(Integer itemMasterId) {
		this.itemMasterId = itemMasterId;
	}

	public Integer getDose() {
		return dose;
	}

	public void setDose(Integer dose) {
		this.dose = dose;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getDoseUnitId() {
		return doseUnitId;
	}

	public void setDoseUnitId(Integer doseUnitId) {
		this.doseUnitId = doseUnitId;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public String getStrengthDesc() {
		return strengthDesc;
	}

	public void setStrengthDesc(String strengthDesc) {
		this.strengthDesc = strengthDesc;
	}

	public String getRoaDesc() {
		return roaDesc;
	}

	public void setRoaDesc(String roaDesc) {
		this.roaDesc = roaDesc;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public Integer getNoOfAdministrationTime() {
		return noOfAdministrationTime;
	}

	public void setNoOfAdministrationTime(Integer noOfAdministrationTime) {
		this.noOfAdministrationTime = noOfAdministrationTime;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrdersetName() {
		return ordersetName;
	}

	public void setOrdersetName(String ordersetName) {
		this.ordersetName = ordersetName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getFormulationType() {
		return formulationType;
	}

	public void setFormulationType(String formulationType) {
		this.formulationType = formulationType;
	}
}
