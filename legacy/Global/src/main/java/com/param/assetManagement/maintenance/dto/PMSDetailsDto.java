package com.param.assetManagement.maintenance.dto;

import java.util.Date;
import java.util.List;

public class PMSDetailsDto {
	
	private Integer pmsDetailsId;
	
	private Integer assetScheduleId;
	
	private String pmsType;

	private String ticketNo;

	private String pmsRemarks;
	
	private String pmsDetails;
	
	private Character isCompleted;
	
	private Character isUnderObservation;
	
	private Character isSendFor;
	
	private String completionTime;
	
	private String underObservationTime;
	
	private Date completionDate;
	
	private Date underObservationDate;
	
	private  List<SpareConsumptionDto> getSpareConsumptionList;
	
	private Character status;
	
	private Integer createdBy;

	private Date createdDate;

	private Integer updatedBy;

	private Date updatedDate;

	private Integer unitId;
	
	private Integer organizationId;

	private Character workOrderStatus;

	public Integer getPmsDetailsId() {
		return pmsDetailsId;
	}

	public void setPmsDetailsId(Integer pmsDetailsId) {
		this.pmsDetailsId = pmsDetailsId;
	}

	public Integer getAssetScheduleId() {
		return assetScheduleId;
	}

	public void setAssetScheduleId(Integer assetScheduleId) {
		this.assetScheduleId = assetScheduleId;
	}

	public String getPmsType() {
		return pmsType;
	}

	public void setPmsType(String pmsType) {
		this.pmsType = pmsType;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getPmsRemarks() {
		return pmsRemarks;
	}

	public void setPmsRemarks(String pmsRemarks) {
		this.pmsRemarks = pmsRemarks;
	}

	public String getPmsDetails() {
		return pmsDetails;
	}

	public void setPmsDetails(String pmsDetails) {
		this.pmsDetails = pmsDetails;
	}

	public Character getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Character isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Character getIsUnderObservation() {
		return isUnderObservation;
	}

	public void setIsUnderObservation(Character isUnderObservation) {
		this.isUnderObservation = isUnderObservation;
	}

	public Character getIsSendFor() {
		return isSendFor;
	}

	public void setIsSendFor(Character isSendFor) {
		this.isSendFor = isSendFor;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public String getUnderObservationTime() {
		return underObservationTime;
	}

	public void setUnderObservationTime(String underObservationTime) {
		this.underObservationTime = underObservationTime;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public Date getUnderObservationDate() {
		return underObservationDate;
	}

	public void setUnderObservationDate(Date underObservationDate) {
		this.underObservationDate = underObservationDate;
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
	
	public List<SpareConsumptionDto> getGetSpareConsumptionList() {
		return getSpareConsumptionList;
	}

	public void setGetSpareConsumptionList(List<SpareConsumptionDto> getSpareConsumptionList) {
		this.getSpareConsumptionList = getSpareConsumptionList;
	}

	public Character getWorkOrderStatus() {
		return workOrderStatus;
	}

	public void setWorkOrderStatus(Character workOrderStatus) {
		this.workOrderStatus = workOrderStatus;
	}
}
