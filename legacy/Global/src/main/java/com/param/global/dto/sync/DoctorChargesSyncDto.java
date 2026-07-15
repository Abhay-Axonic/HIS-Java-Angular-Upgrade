package com.param.global.dto.sync;

public class DoctorChargesSyncDto {

	private Integer docConsultationMasterId;
	
	private Integer doctorId;
	
	private Integer visitTypeId;
	
	private Double charges;
	
	private Integer hospitalId;
	
	private Integer unitId;
	
	private Integer applicationId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer localDoctorId;
	
	private String registrationNumber;
	
	private Integer globalUnitId;
	
	private Integer globalOrgId;
	
	private Integer globalDocId;
	
	private Double newVisitRate;
	
	private Double followUpVisitRate;
	
	private String currency;
	
	private Integer classificationId;
		
	private String classificationDesc;
	private Integer channelId;
	public Double getNewVisitRate() {
		return newVisitRate;
	}

	public void setNewVisitRate(Double newVisitRate) {
		this.newVisitRate = newVisitRate;
	}

	public Double getFollowUpVisitRate() {
		return followUpVisitRate;
	}

	public void setFollowUpVisitRate(Double followUpVisitRate) {
		this.followUpVisitRate = followUpVisitRate;
	}

	public Integer getGlobalUnitId() {
		return globalUnitId;
	}

	public void setGlobalUnitId(Integer globalUnitId) {
		this.globalUnitId = globalUnitId;
	}

	public Integer getGlobalOrgId() {
		return globalOrgId;
	}

	public void setGlobalOrgId(Integer globalOrgId) {
		this.globalOrgId = globalOrgId;
	}

	public Integer getGlobalDocId() {
		return globalDocId;
	}

	public void setGlobalDocId(Integer globalDocId) {
		this.globalDocId = globalDocId;
	}

	public Integer getDocConsultationMasterId() {
		return docConsultationMasterId;
	}

	public void setDocConsultationMasterId(Integer docConsultationMasterId) {
		this.docConsultationMasterId = docConsultationMasterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Double getCharges() {
		return charges;
	}

	public void setCharges(Double charges) {
		this.charges = charges;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
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

	public Integer getLocalDoctorId() {
		return localDoctorId;
	}

	public void setLocalDoctorId(Integer localDoctorId) {
		this.localDoctorId = localDoctorId;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(Integer classificationId) {
		this.classificationId = classificationId;
	}

	public String getClassificationDesc() {
		return classificationDesc;
	}

	public void setClassificationDesc(String classificationDesc) {
		this.classificationDesc = classificationDesc;
	}
	
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
}
