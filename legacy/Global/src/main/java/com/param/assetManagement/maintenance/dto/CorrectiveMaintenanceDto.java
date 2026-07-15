package com.param.assetManagement.maintenance.dto;

import java.math.BigInteger;
import java.util.List;

public class CorrectiveMaintenanceDto {

	private Integer correctiveMaintenanceId;
	
	private String callLogNumber;
	
	private Integer itemId;
	
	private Integer storeId;
	
	private Integer requestTypeId;
	
	private Integer requestCriticalityId;
	
	private String serialNumber;
	
	private boolean isDown;
	
	private String requestDateTime;
	
	private String downDateTime;
	
	private String resolutionExpectedDateTime;
	
	private String requestDetails;
	
	private Integer maintenanceStatusId;
	
	private String remark;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId ;

	private Integer organizationId;
	
	private String fromDate;
	
	private String toDate;
	
	private String assetName;
	
	private String assetCode;
	
	private String assetcategory;
	
	private String userName;
	
	private String resolutionExpectedTime;
	
	private String downTime;
	
	private boolean exportedToWorklist;
	
	private Integer isWorklist;
	
	private String completionDate ;
	
	private String completionRemak ;
	
	private boolean isComplete;
	
	private Integer referenceId ;
	
	private String referenceLogNumber ;
	
	private String completionTime;
	
	private Integer editflag;
	
	private boolean check;
	
	private BigInteger batchId ;
	
	private boolean reOpen;
	
	private List<VendorDto> manufacturerList;
	
	private List<VendorDto> vendorList;
	
	private List<CorrectiveMaintenanceDto> accessoryList;
	
	private List<CorrectiveMaintenanceDto> insuranceList;
	
	private Integer quantity;
	
	public Integer getCorrectiveMaintenanceId() {
		return correctiveMaintenanceId;
	}

	public void setCorrectiveMaintenanceId(Integer correctiveMaintenanceId) {
		this.correctiveMaintenanceId = correctiveMaintenanceId;
	}

	public String getCallLogNumber() {
		return callLogNumber;
	}

	public void setCallLogNumber(String callLogNumber) {
		this.callLogNumber = callLogNumber;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getRequestCriticalityId() {
		return requestCriticalityId;
	}

	public void setRequestCriticalityId(Integer requestCriticalityId) {
		this.requestCriticalityId = requestCriticalityId;
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

	public Integer getMaintenanceStatusId() {
		return maintenanceStatusId;
	}

	public void setMaintenanceStatusId(Integer maintenanceStatusId) {
		this.maintenanceStatusId = maintenanceStatusId;
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

	public boolean isExportedToWorklist() {
		return exportedToWorklist;
	}

	public void setExportedToWorklist(boolean exportedToWorklist) {
		this.exportedToWorklist = exportedToWorklist;
	}

	public Integer getIsWorklist() {
		return isWorklist;
	}

	public void setIsWorklist(Integer isWorklist) {
		this.isWorklist = isWorklist;
	}

	public String getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}

	public String getCompletionRemak() {
		return completionRemak;
	}

	public void setCompletionRemak(String completionRemak) {
		this.completionRemak = completionRemak;
	}

	public boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public Integer getEditflag() {
		return editflag;
	}

	public void setEditflag(Integer editflag) {
		this.editflag = editflag;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public BigInteger getBatchId() {
		return batchId;
	}

	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
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

	public List<CorrectiveMaintenanceDto> getAccessoryList() {
		return accessoryList;
	}

	public void setAccessoryList(List<CorrectiveMaintenanceDto> accessoryList) {
		this.accessoryList = accessoryList;
	}

	public List<VendorDto> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<VendorDto> vendorList) {
		this.vendorList = vendorList;
	}

	public List<VendorDto> getManufacturerList() {
		return manufacturerList;
	}

	public void setManufacturerList(List<VendorDto> manufacturerList) {
		this.manufacturerList = manufacturerList;
	}

	public List<CorrectiveMaintenanceDto> getInsuranceList() {
		return insuranceList;
	}

	public void setInsuranceList(List<CorrectiveMaintenanceDto> insuranceList) {
		this.insuranceList = insuranceList;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
