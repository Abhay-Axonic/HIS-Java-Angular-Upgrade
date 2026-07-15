package com.param.opd.coversheet.dto;

import java.util.List;

import javax.persistence.Column;

public class PatientHabitDetailsDto {

	private Integer patientId;
	private Integer habitId;
	private Integer habitTypeId;
	private String remark;
	private Integer cigarettesPerDay;
	private double yearsSmoked;
	private Integer gmsPerDay;
	private double yearsUsed;
	private Integer mlsPerDay;
	private String frequency;
	private Integer leftWhen;
	private double packYear;
	private String drug;
	private String typeOfExercise;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String updatedDate;
	private Character status;
	private Integer unitId;
	private Integer organizationId;
	private String durationOfLeftWhen;
	/*private Integer exerciseWhen;*/
	private Integer encounterId;
	private Integer admissionId;
	private Integer roleId;
	private Character isReviewedFlag;
	private Character isEnterInErrorStatus;
	private Integer patientHabitDetailsId;
	private String fromDate;
	private String toDate;
	private String habitDesc;
	private String habitTypeDesc;
	private String searchKeyword;

	private List<ListOfPatientHabitDetailsDto> listOfPatientHabitDetailsDto;
	private Integer covTemplateId;

	
	
	
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getPatientHabitDetailsId() {
		return patientHabitDetailsId;
	}
	public void setPatientHabitDetailsId(Integer patientHabitDetailsId) {
		this.patientHabitDetailsId = patientHabitDetailsId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Character getIsReviewedFlag() {
		return isReviewedFlag;
	}
	public void setIsReviewedFlag(Character isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}
	public Character getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}
	public void setIsEnterInErrorStatus(Character isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
	}
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	/*public Integer getExerciseWhen() {
		return exerciseWhen;
	}
	public void setExerciseWhen(Integer exerciseWhen) {
		this.exerciseWhen = exerciseWhen;
	}*/
	public List<ListOfPatientHabitDetailsDto> getListOfPatientHabitDetailsDto() {
		return listOfPatientHabitDetailsDto;
	}
	public void setListOfPatientHabitDetailsDto(
			List<ListOfPatientHabitDetailsDto> listOfPatientHabitDetailsDto) {
		this.listOfPatientHabitDetailsDto = listOfPatientHabitDetailsDto;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getHabitId() {
		return habitId;
	}
	public void setHabitId(Integer habitId) {
		this.habitId = habitId;
	}
	public Integer getHabitTypeId() {
		return habitTypeId;
	}
	public void setHabitTypeId(Integer habitTypeId) {
		this.habitTypeId = habitTypeId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCigarettesPerDay() {
		return cigarettesPerDay;
	}
	public void setCigarettesPerDay(Integer cigarettesPerDay) {
		this.cigarettesPerDay = cigarettesPerDay;
	}
	public double getYearsSmoked() {
		return yearsSmoked;
	}
	public void setYearsSmoked(double yearsSmoked) {
		this.yearsSmoked = yearsSmoked;
	}
	public Integer getGmsPerDay() {
		return gmsPerDay;
	}
	public void setGmsPerDay(Integer gmsPerDay) {
		this.gmsPerDay = gmsPerDay;
	}
	public double getYearsUsed() {
		return yearsUsed;
	}
	public void setYearsUsed(double yearsUsed) {
		this.yearsUsed = yearsUsed;
	}
	public Integer getMlsPerDay() {
		return mlsPerDay;
	}
	public void setMlsPerDay(Integer mlsPerDay) {
		this.mlsPerDay = mlsPerDay;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public double getPackYear() {
		return packYear;
	}
	public void setPackYear(double packYear) {
		this.packYear = packYear;
	}
	public String getTypeOfExercise() {
		return typeOfExercise;
	}
	public void setTypeOfExercise(String typeOfExercise) {
		this.typeOfExercise = typeOfExercise;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getLeftWhen() {
		return leftWhen;
	}
	public void setLeftWhen(Integer leftWhen) {
		this.leftWhen = leftWhen;
	}
	public String getDurationOfLeftWhen() {
		return durationOfLeftWhen;
	}
	public void setDurationOfLeftWhen(String durationOfLeftWhen) {
		this.durationOfLeftWhen = durationOfLeftWhen;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public Integer getCovTemplateId() {
		return covTemplateId;
	}
	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	public String getHabitDesc() {
		return habitDesc;
	}
	public void setHabitDesc(String habitDesc) {
		this.habitDesc = habitDesc;
	}
	public String getHabitTypeDesc() {
		return habitTypeDesc;
	}
	public void setHabitTypeDesc(String habitTypeDesc) {
		this.habitTypeDesc = habitTypeDesc;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	
	
}
