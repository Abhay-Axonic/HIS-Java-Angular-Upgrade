package com.param.opd.coversheet.dto;

import java.util.Date;
import java.util.List;



public class IpdDietOrderDto {

	private Integer ipdDietOrderId;
	
	private Integer doctorId;
	
	private Integer admissionId;
	
	private Integer patientId;
	
	private String dietStatus;
	
	private String dietStartDate;

	private String dietStopDate;
	
	private String dietInstruction;
	
	private Integer dietMealPreferenceId;
	
	private Character isNbm;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String mealPreferenceDesc;
	
	private Integer dietTypeId;
	
	private String dietTypeDescription;

	
	private List<IpdDietOrderDto> ipdDietOrderDtoList;

	public Integer getIpdDietOrderId() {
		return ipdDietOrderId;
	}

	public void setIpdDietOrderId(Integer ipdDietOrderId) {
		this.ipdDietOrderId = ipdDietOrderId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getDietStatus() {
		return dietStatus;
	}

	public void setDietStatus(String dietStatus) {
		this.dietStatus = dietStatus;
	}

	public String getDietStartDate() {
		return dietStartDate;
	}

	public void setDietStartDate(String dietStartDate) {
		this.dietStartDate = dietStartDate;
	}

	public String getDietStopDate() {
		return dietStopDate;
	}

	public void setDietStopDate(String dietStopDate) {
		this.dietStopDate = dietStopDate;
	}

	public String getDietInstruction() {
		return dietInstruction;
	}

	public void setDietInstruction(String dietInstruction) {
		this.dietInstruction = dietInstruction;
	}

	public Integer getDietMealPreferenceId() {
		return dietMealPreferenceId;
	}

	public void setDietMealPreferenceId(Integer dietMealPreferenceId) {
		this.dietMealPreferenceId = dietMealPreferenceId;
	}

	public Character getIsNbm() {
		return isNbm;
	}

	public void setIsNbm(Character isNbm) {
		this.isNbm = isNbm;
	}

	
	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public List<IpdDietOrderDto> getIpdDietOrderDtoList() {
		return ipdDietOrderDtoList;
	}

	public void setIpdDietOrderDtoList(List<IpdDietOrderDto> ipdDietOrderDtoList) {
		this.ipdDietOrderDtoList = ipdDietOrderDtoList;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public String getMealPreferenceDesc() {
		return mealPreferenceDesc;
	}

	public void setMealPreferenceDesc(String mealPreferenceDesc) {
		this.mealPreferenceDesc = mealPreferenceDesc;
	}

	public String getDietTypeDescription() {
		return dietTypeDescription;
	}

	public void setDietTypeDescription(String dietTypeDescription) {
		this.dietTypeDescription = dietTypeDescription;
	}

	

}
