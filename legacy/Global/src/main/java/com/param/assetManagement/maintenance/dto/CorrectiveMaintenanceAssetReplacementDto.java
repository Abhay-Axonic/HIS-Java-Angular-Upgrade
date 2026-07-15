package com.param.assetManagement.maintenance.dto;

public class CorrectiveMaintenanceAssetReplacementDto {

	private Integer assetReplacementId;
	
	private Integer correctiveMaintenanceId;
	
	private Integer correctiveMaintenanceSchedulingId;
	
	private Integer itemId;
	
	private String  itemName;
	
	private String itemCode;
	
	private Integer requestedStoreId;
	
	private Integer requestedSerialNoId;
	
	private Integer newSerialNoId;
	
	private Integer newStoreId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId ;

	private Integer organizationId;
	
	private String newSerialNo;
	
	private String requestedSerialNo;
	
	private String reasonNote;
	
	private Integer reasonId ;

	private Integer replaceStatusId;
	
	private String callLogNumber;
	
	private String requestDateTime;
	
	private String completionDate;
	
	private String assetName;
	
	private String resolutionExpectedDateTime;

	public Integer getAssetReplacementId() {
		return assetReplacementId;
	}

	public void setAssetReplacementId(Integer assetReplacementId) {
		this.assetReplacementId = assetReplacementId;
	}

	public Integer getCorrectiveMaintenanceId() {
		return correctiveMaintenanceId;
	}

	public void setCorrectiveMaintenanceId(Integer correctiveMaintenanceId) {
		this.correctiveMaintenanceId = correctiveMaintenanceId;
	}

	public Integer getCorrectiveMaintenanceSchedulingId() {
		return correctiveMaintenanceSchedulingId;
	}

	public void setCorrectiveMaintenanceSchedulingId(Integer correctiveMaintenanceSchedulingId) {
		this.correctiveMaintenanceSchedulingId = correctiveMaintenanceSchedulingId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getRequestedStoreId() {
		return requestedStoreId;
	}

	public void setRequestedStoreId(Integer requestedStoreId) {
		this.requestedStoreId = requestedStoreId;
	}

	public Integer getRequestedSerialNoId() {
		return requestedSerialNoId;
	}

	public void setRequestedSerialNoId(Integer requestedSerialNoId) {
		this.requestedSerialNoId = requestedSerialNoId;
	}

	public Integer getNewSerialNoId() {
		return newSerialNoId;
	}

	public void setNewSerialNoId(Integer newSerialNoId) {
		this.newSerialNoId = newSerialNoId;
	}

	public Integer getNewStoreId() {
		return newStoreId;
	}

	public void setNewStoreId(Integer newStoreId) {
		this.newStoreId = newStoreId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getNewSerialNo() {
		return newSerialNo;
	}

	public void setNewSerialNo(String newSerialNo) {
		this.newSerialNo = newSerialNo;
	}

	public String getRequestedSerialNo() {
		return requestedSerialNo;
	}

	public void setRequestedSerialNo(String requestedSerialNo) {
		this.requestedSerialNo = requestedSerialNo;
	}

	public String getReasonNote() {
		return reasonNote;
	}

	public void setReasonNote(String reasonNote) {
		this.reasonNote = reasonNote;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getReplaceStatusId() {
		return replaceStatusId;
	}

	public void setReplaceStatusId(Integer replaceStatusId) {
		this.replaceStatusId = replaceStatusId;
	}

	public String getCallLogNumber() {
		return callLogNumber;
	}

	public void setCallLogNumber(String callLogNumber) {
		this.callLogNumber = callLogNumber;
	}

	public String getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(String requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public String getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getResolutionExpectedDateTime() {
		return resolutionExpectedDateTime;
	}

	public void setResolutionExpectedDateTime(String resolutionExpectedDateTime) {
		this.resolutionExpectedDateTime = resolutionExpectedDateTime;
	}

}
