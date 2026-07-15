package com.param.assetManagement.maintenance.dto;

import java.math.BigInteger;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class CorrectiveMaintenanceScheduleDto extends CookieObjectDto{
	
	private Integer correctiveMaintenanceScheduleId;
	
	private Integer correctiveMaintenanceWorklistId;
	
	private Integer requestAllocationTypeId;
	
	private String ticketNo;
	
	private String resolutionDetails;
	
	private Integer vendorId;

	private boolean isCompleted;
	
	private String assetUpDate;
	
	private String assetUpTime;
	
	private boolean underObservation;
	
	private String fromDate;
	
	private String toDate;
	
	private boolean isStart;
	
	private Integer maintenanceScheduleStatusId;
	
	private String completionDate;
	
	private String remark;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId ;

	private Integer organizationId;
	
	private Integer correctiveMaintenanceId;

	private String scheduleDate;
	
	private String scheduleTime;
	
	private Integer assignTo;
	
	private String allocationRemark;
	
	private String serialNumber;
	
	private boolean isDown;
	
	private String requestDateTime;
	
	private String downDateTime;
	
	private String resolutionExpectedDateTime;
	
	private String requestDetails;
	
	private String assetName;
	
	private String assetCode;
	
	private String assetcategory;
	
	private String userName;
	
	private String resolutionExpectedTime;
	
	private String downTime;
	
	private String callLogNumber;
	
	private String requestedByName;
	
	private Integer storeId;
	
	private Integer isWorklist;
	
	private Integer requestCriticalityId;
	
	private Integer requestTypeId;
	
	private Integer maintenanceWorklistStatusId;
	
	private BigInteger batchId;
	
	private Integer itemId;
	
	private String reSolutionRemark;
	
	private String reqCompletionDate ;
	
	private String completionRemak ;
	
	private boolean isComplete;
	
	private String completionTime;
	
	private Integer referenceId ;
	
	private String referenceLogNumber ;
	
	private boolean reOpen;
	
	private Integer replaceStatusId;
	
	private String vendorName;
	
	private CorrectiveMaintenanceAssetReplacementDto  correctiveMaintenanceReplacementDto;
	
	
	private List<CorrectiveMaintenanceSparesComsumptionDto>  correctiveMaintenanceSparesComsumptionDto;


	public Integer getCorrectiveMaintenanceScheduleId() {
		return correctiveMaintenanceScheduleId;
	}

	public void setCorrectiveMaintenanceScheduleId(Integer correctiveMaintenanceScheduleId) {
		this.correctiveMaintenanceScheduleId = correctiveMaintenanceScheduleId;
	}

	public Integer getCorrectiveMaintenanceWorklistId() {
		return correctiveMaintenanceWorklistId;
	}

	public void setCorrectiveMaintenanceWorklistId(Integer correctiveMaintenanceWorklistId) {
		this.correctiveMaintenanceWorklistId = correctiveMaintenanceWorklistId;
	}

	public Integer getRequestAllocationTypeId() {
		return requestAllocationTypeId;
	}

	public void setRequestAllocationTypeId(Integer requestAllocationTypeId) {
		this.requestAllocationTypeId = requestAllocationTypeId;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getResolutionDetails() {
		return resolutionDetails;
	}

	public void setResolutionDetails(String resolutionDetails) {
		this.resolutionDetails = resolutionDetails;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getAssetUpDate() {
		return assetUpDate;
	}

	public void setAssetUpDate(String assetUpDate) {
		this.assetUpDate = assetUpDate;
	}

	public String getAssetUpTime() {
		return assetUpTime;
	}

	public void setAssetUpTime(String assetUpTime) {
		this.assetUpTime = assetUpTime;
	}

	public boolean getIsUnderObservation() {
		return underObservation;
	}

	public void setIsUnderObservation(boolean underObservation) {
		this.underObservation = underObservation;
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

	public boolean getIsStart() {
		return isStart;
	}

	public void setIsStart(boolean isStart) {
		this.isStart = isStart;
	}

	public Integer getMaintenanceScheduleStatusId() {
		return maintenanceScheduleStatusId;
	}

	public void setMaintenanceScheduleStatusId(Integer maintenanceScheduleStatusId) {
		this.maintenanceScheduleStatusId = maintenanceScheduleStatusId;
	}

	public String getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getCorrectiveMaintenanceId() {
		return correctiveMaintenanceId;
	}

	public void setCorrectiveMaintenanceId(Integer correctiveMaintenanceId) {
		this.correctiveMaintenanceId = correctiveMaintenanceId;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Integer getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Integer assignTo) {
		this.assignTo = assignTo;
	}

	public String getAllocationRemark() {
		return allocationRemark;
	}

	public void setAllocationRemark(String allocationRemark) {
		this.allocationRemark = allocationRemark;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public boolean getIsDown() {
		return isDown;
	}

	public void setIsDown(boolean isDown) {
		this.isDown = isDown;
	}

	public String getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(String requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public String getDownDateTime() {
		return downDateTime;
	}

	public void setDownDateTime(String downDateTime) {
		this.downDateTime = downDateTime;
	}

	public String getResolutionExpectedDateTime() {
		return resolutionExpectedDateTime;
	}

	public void setResolutionExpectedDateTime(String resolutionExpectedDateTime) {
		this.resolutionExpectedDateTime = resolutionExpectedDateTime;
	}

	public String getRequestDetails() {
		return requestDetails;
	}

	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetcategory() {
		return assetcategory;
	}

	public void setAssetcategory(String assetcategory) {
		this.assetcategory = assetcategory;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getResolutionExpectedTime() {
		return resolutionExpectedTime;
	}

	public void setResolutionExpectedTime(String resolutionExpectedTime) {
		this.resolutionExpectedTime = resolutionExpectedTime;
	}

	public String getDownTime() {
		return downTime;
	}

	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}

	public String getCallLogNumber() {
		return callLogNumber;
	}

	public void setCallLogNumber(String callLogNumber) {
		this.callLogNumber = callLogNumber;
	}

	public String getRequestedByName() {
		return requestedByName;
	}

	public void setRequestedByName(String requestedByName) {
		this.requestedByName = requestedByName;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getIsWorklist() {
		return isWorklist;
	}

	public void setIsWorklist(Integer isWorklist) {
		this.isWorklist = isWorklist;
	}

	public List<CorrectiveMaintenanceSparesComsumptionDto> getCorrectiveMaintenanceSparesComsumptionDto() {
		return correctiveMaintenanceSparesComsumptionDto;
	}

	public void setCorrectiveMaintenanceSparesComsumptionDto(
			List<CorrectiveMaintenanceSparesComsumptionDto> correctiveMaintenanceSparesComsumptionDto) {
		this.correctiveMaintenanceSparesComsumptionDto = correctiveMaintenanceSparesComsumptionDto;
	}

	public Integer getRequestCriticalityId() {
		return requestCriticalityId;
	}

	public void setRequestCriticalityId(Integer requestCriticalityId) {
		this.requestCriticalityId = requestCriticalityId;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getMaintenanceWorklistStatusId() {
		return maintenanceWorklistStatusId;
	}

	public void setMaintenanceWorklistStatusId(Integer maintenanceWorklistStatusId) {
		this.maintenanceWorklistStatusId = maintenanceWorklistStatusId;
	}

	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigInteger getBatchId() {
		return batchId;
	}

	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
	}

	public String getReSolutionRemark() {
		return reSolutionRemark;
	}

	public void setReSolutionRemark(String reSolutionRemark) {
		this.reSolutionRemark = reSolutionRemark;
	}

	public String getReqCompletionDate() {
		return reqCompletionDate;
	}

	public void setReqCompletionDate(String reqCompletionDate) {
		this.reqCompletionDate = reqCompletionDate;
	}

	public String getCompletionRemak() {
		return completionRemak;
	}

	public void setCompletionRemak(String completionRemak) {
		this.completionRemak = completionRemak;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceLogNumber() {
		return referenceLogNumber;
	}

	public void setReferenceLogNumber(String referenceLogNumber) {
		this.referenceLogNumber = referenceLogNumber;
	}

	public boolean isReOpen() {
		return reOpen;
	}

	public void setReOpen(boolean reOpen) {
		this.reOpen = reOpen;
	}

	public CorrectiveMaintenanceAssetReplacementDto getCorrectiveMaintenanceReplacementDto() {
		return correctiveMaintenanceReplacementDto;
	}

	public void setCorrectiveMaintenanceReplacementDto(
			CorrectiveMaintenanceAssetReplacementDto correctiveMaintenanceReplacementDto) {
		this.correctiveMaintenanceReplacementDto = correctiveMaintenanceReplacementDto;
	}

	public Integer getReplaceStatusId() {
		return replaceStatusId;
	}

	public void setReplaceStatusId(Integer replaceStatusId) {
		this.replaceStatusId = replaceStatusId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

}
