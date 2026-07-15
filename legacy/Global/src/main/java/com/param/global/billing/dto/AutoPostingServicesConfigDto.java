package com.param.global.billing.dto;

import java.util.List;

public class AutoPostingServicesConfigDto {

	private Integer id;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer serviceId;
	
	private String serviceStandardName;
	
	private Integer postingTime;
	
	private Character isHalfDay;
	
	private Integer halfDayHrs;
	
	private Character bufferTimeApplicable;
	
	private Integer bufferTimeForAdmission;
	
	private Integer bufferTimeForDischarge;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Character isSelected;
	
	private String servicePostingTime;
	
	private Integer specialityId;
	
	private Character isPeriodic;
	
	private Character isDocReq;
	
	private Character isBedChargesService;
	
	public Character getIsBedChargesService() {
		return isBedChargesService;
	}

	public void setIsBedChargesService(Character isBedChargesService) {
		this.isBedChargesService = isBedChargesService;
	}

	public Character getIsDocReq() {
		return isDocReq;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = isDocReq;
	}

	private List<AutoPostingServicesConfigDto> postingServicesConfigDtosList; 
	
	public Character getIsPeriodic() {
		return isPeriodic;
	}

	public void setIsPeriodic(Character isPeriodic) {
		this.isPeriodic = isPeriodic;
	}

	public List<AutoPostingServicesConfigDto> getPostingServicesConfigDtosList() {
		return postingServicesConfigDtosList;
	}

	public void setPostingServicesConfigDtosList(List<AutoPostingServicesConfigDto> postingServicesConfigDtosList) {
		this.postingServicesConfigDtosList = postingServicesConfigDtosList;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPostingTime() {
		return postingTime;
	}

	public void setPostingTime(Integer postingTime) {
		this.postingTime = postingTime;
	}

	public Character getIsHalfDay() {
		return isHalfDay;
	}

	public void setIsHalfDay(Character isHalfDay) {
		this.isHalfDay = isHalfDay;
	}

	public Integer getHalfDayHrs() {
		return halfDayHrs;
	}

	public void setHalfDayHrs(Integer halfDayHrs) {
		this.halfDayHrs = halfDayHrs;
	}

	public Character getBufferTimeApplicable() {
		return bufferTimeApplicable;
	}

	public void setBufferTimeApplicable(Character bufferTimeApplicable) {
		this.bufferTimeApplicable = bufferTimeApplicable;
	}

	public Integer getBufferTimeForAdmission() {
		return bufferTimeForAdmission;
	}

	public void setBufferTimeForAdmission(Integer bufferTimeForAdmission) {
		this.bufferTimeForAdmission = bufferTimeForAdmission;
	}

	public Integer getBufferTimeForDischarge() {
		return bufferTimeForDischarge;
	}

	public void setBufferTimeForDischarge(Integer bufferTimeForDischarge) {
		this.bufferTimeForDischarge = bufferTimeForDischarge;
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

	public Character getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Character isSelected) {
		this.isSelected = isSelected;
	}

	public String getServicePostingTime() {
		return servicePostingTime;
	}

	public void setServicePostingTime(String servicePostingTime) {
		this.servicePostingTime = servicePostingTime;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	
	
	
}
