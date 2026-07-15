package com.param.global.doctor.dto;

public class OrdersetInvestigationDetailsDto {
	private int ordersetInvestigationDetailsId;
	private Integer ordersetMasterId;
	private Integer serviceId;
	private String serviceStandardName;
	private Integer specialityId;
	private String specialityName;
	private String subSpecialityName;
	private Integer subSpecialityId;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Character isRefDoctorShare;
	private Character isAppointable;
	private Character isDeleted;
	
	public int getOrdersetInvestigationDetailsId() {
		return ordersetInvestigationDetailsId;
	}
	public void setOrdersetInvestigationDetailsId(int ordersetInvestigationDetailsId) {
		this.ordersetInvestigationDetailsId = ordersetInvestigationDetailsId;
	}
	public Integer getOrdersetMasterId() {
		return ordersetMasterId;
	}
	public void setOrdersetMasterId(Integer ordersetMasterId) {
		this.ordersetMasterId = ordersetMasterId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}
	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public String getSubSpecialityName() {
		return subSpecialityName;
	}
	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}
	public Character getIsRefDoctorShare() {
		return isRefDoctorShare;
	}
	public void setIsRefDoctorShare(Character isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}
	public Character getIsAppointable() {
		return isAppointable;
	}
	public void setIsAppointable(Character isAppointable) {
		this.isAppointable = isAppointable;
	}
	public Character getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}	
	
	
}
