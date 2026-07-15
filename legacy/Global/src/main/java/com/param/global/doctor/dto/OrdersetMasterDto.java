package com.param.global.doctor.dto;

public class OrdersetMasterDto {

	private int ordersetMasterId;
	private Integer diagnosisId;
	private Integer icpcId;
	private String ordersetName;
	private Integer ordersetTypeId;
	private Integer specialityId;
	private Integer doctorId;
	private char status;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	
	private String doctorName;
	private String specialityName;
	private String ordersetTypeName;
	private String diagnosisName;
	private String icpcDesc;
	private Integer offset;
	private Integer recordPerPage;
	private Integer orderSetActiveTab;
	private Character isDeleted;

	public int getOrdersetMasterId() {
		return ordersetMasterId;
	}
	public void setOrdersetMasterId(int ordersetMasterId) {
		this.ordersetMasterId = ordersetMasterId;
	}
	public Integer getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	public String getOrdersetName() {
		return ordersetName;
	}
	public void setOrdersetName(String ordersetName) {
		this.ordersetName = ordersetName;
	}
	public Integer getOrdersetTypeId() {
		return ordersetTypeId;
	}
	public void setOrdersetTypeId(Integer ordersetTypeId) {
		this.ordersetTypeId = ordersetTypeId;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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
	public String getOrdersetTypeName() {
		return ordersetTypeName;
	}
	public void setOrdersetTypeName(String ordersetTypeName) {
		this.ordersetTypeName = ordersetTypeName;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getRecordPerPage() {
		return recordPerPage;
	}
	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}
	public Character getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getIcpcId() {
		return icpcId;
	}

	public void setIcpcId(Integer icpcId) {
		this.icpcId = icpcId;
	}

	public String getIcpcDesc() {
		return icpcDesc;
	}

	public void setIcpcDesc(String icpcDesc) {
		this.icpcDesc = icpcDesc;
	}

	public Integer getOrderSetActiveTab() {
		return orderSetActiveTab;
	}

	public void setOrderSetActiveTab(Integer orderSetActiveTab) {
		this.orderSetActiveTab = orderSetActiveTab;
	}
}
