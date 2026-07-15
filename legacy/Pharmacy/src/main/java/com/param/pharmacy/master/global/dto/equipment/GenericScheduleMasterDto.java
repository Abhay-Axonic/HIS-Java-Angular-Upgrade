package com.param.pharmacy.master.global.dto.equipment;

import java.util.Date;
import java.util.List;

public class GenericScheduleMasterDto {

	private Integer genericScheduleId;
	
	private Integer genericId;

	private Integer scheduleId;
	
	private Integer serviceMasterId;
	
	private Integer  genericScheduleServiceCodeId;

	private String isEmergencyMedicine;
	
	private String isInvestgation;
	
	private Character status;

	private Integer createdBy;

	private Date createdDate;

	private Integer updatedBy;

	private Date updatedDate;

	private Integer orgId;

	private Integer unitId;

	private String serviceStandardName;

	private String serviceStandardCode;
	
	private String scheduleCode;
	
	private String scheduleDescription;
	
	private String name;
	
	private Integer id;
	
	private String labal;
	
	private Integer serMstrId;
	
	
	List<GenericScheduleMasterDto> serviceCodeId;
	
	public Integer getGenericScheduleId() {
		return genericScheduleId;
	}

	public void setGenericScheduleId(Integer genericScheduleId) {
		this.genericScheduleId = genericScheduleId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public String getIsEmergencyMedicine() {
		return isEmergencyMedicine;
	}

	public void setIsEmergencyMedicine(String isEmergencyMedicine) {
		this.isEmergencyMedicine = isEmergencyMedicine;
	}

	public String getIsInvestgation() {
		return isInvestgation;
	}

	public void setIsInvestgation(String isInvestgation) {
		this.isInvestgation = isInvestgation;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public String getScheduleCode() {
		return scheduleCode;
	}

	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}

	public String getScheduleDescription() {
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription) {
		this.scheduleDescription = scheduleDescription;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSerMstrId() {
		return serMstrId;
	}

	public void setSerMstrId(Integer serMstrId) {
		this.serMstrId = serMstrId;
	}

	@Override
	public String toString() {
		return "GenericScheduleMasterDto [genericScheduleId=" + genericScheduleId + ", genericId=" + genericId
				+ ", scheduleId=" + scheduleId + ", serviceMasterId=" + serviceMasterId + ", isEmergencyMedicine="
				+ isEmergencyMedicine + ", isInvestgation=" + isInvestgation + ", status=" + status + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", updatedDate="
				+ updatedDate + ", orgId=" + orgId + ", unitId=" + unitId + ", serviceStandardName="
				+ serviceStandardName + ", serviceStandardCode=" + serviceStandardCode + ", scheduleCode="
				+ scheduleCode + ", scheduleDescription=" + scheduleDescription + ", name=" + name + ", id=" + id
				+ ", serMstrId=" + serMstrId + "]";
	}

	public List<GenericScheduleMasterDto> getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(List<GenericScheduleMasterDto> serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

	public String getLabal() {
		return labal;
	}

	public void setLabal(String labal) {
		this.labal = labal;
	}

	public Integer getGenericScheduleServiceCodeId() {
		return genericScheduleServiceCodeId;
	}

	public void setGenericScheduleServiceCodeId(Integer genericScheduleServiceCodeiId) {
		this.genericScheduleServiceCodeId = genericScheduleServiceCodeiId;
	}

	
}
