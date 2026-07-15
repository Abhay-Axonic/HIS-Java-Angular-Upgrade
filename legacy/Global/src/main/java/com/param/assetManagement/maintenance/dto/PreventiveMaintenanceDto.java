package com.param.assetManagement.maintenance.dto;

import java.util.Date;
import java.util.List;


public class PreventiveMaintenanceDto {

	private Integer preventiveMaintenanceId;
	
	private Integer assetCategoryId;
	
	private String assetCategory;
	
	private String itemCode;
	
	private Integer itemNameId;
	
	private String itemName;
	
	private String serialNo;
	
	private Integer vendorId;
	
	private String vendor;
	
	private Integer pmsFrequencyId;
	
	private String pmsFrequency;
	
	private Integer pmsTypeId;
	
	private String pmsType;
	
	private Date startDate;
	
	private Date endDate;
	
	private Date nextDate;
	
	private String remark;
	
	private Integer storeId;
	
	private Integer manufacturerId;
	
	private Integer productCategoryId;
	
	private String productCategory;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId ;

	private Integer organizationId;
	
	private Integer assetScheduleChecklistId;
	
	private String checklistCode;
	
	private String checklistName;
	
	private Integer checklistNameId;

	private Integer assetScheduleId;
	
	private Date scheduledDate;
	
	private String scheduledTime;
	
	private String scheduledRemark;
	
	private Date completionDate;
	
	private String completionTime;
	
	private String completionRemark;
	
	private String month;
	
	private String year;
	
	private Integer processStatus;
	
	private String assignRemark;
	
	private Date fromDate;
	
	private Date toDate;
	
	private List<AssetScheduleChecklistDto> getChecklistData;
	
	private String storeName;
	
	private Integer assignTo;
	
	private Date assignOn;
	
	private String assignTime;
	
	private Integer pmsDetailsId;
	
	private String ticketNo;
	
	private String pmsDetails;
	
	private String pmsRemarks;

	private Date underObservationDate;
	
	private String underObservationTime;
	
	private List<SpareConsumptionDto> getSpareConsumptionList;
	
	private Integer spareConsumptionId;
	
	private Integer assetId;
	
	private String assetCode;
	
	private String assetName;
	
	private Integer assetQty;

	private Character workOrderStatus;

	public Integer getPreventiveMaintenanceId() {
		return preventiveMaintenanceId;
	}

	public void setPreventiveMaintenanceId(Integer preventiveMaintenanceId) {
		this.preventiveMaintenanceId = preventiveMaintenanceId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getItemNameId() {
		return itemNameId;
	}

	public void setItemNameId(Integer itemNameId) {
		this.itemNameId = itemNameId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getPmsFrequencyId() {
		return pmsFrequencyId;
	}

	public void setPmsFrequencyId(Integer pmsFrequencyId) {
		this.pmsFrequencyId = pmsFrequencyId;
	}

	public String getPmsFrequency() {
		return pmsFrequency;
	}

	public void setPmsFrequency(String pmsFrequency) {
		this.pmsFrequency = pmsFrequency;
	}

	public Integer getPmsTypeId() {
		return pmsTypeId;
	}

	public void setPmsTypeId(Integer pmsTypeId) {
		this.pmsTypeId = pmsTypeId;
	}

	public String getPmsType() {
		return pmsType;
	}

	public void setPmsType(String pmsType) {
		this.pmsType = pmsType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
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

	public Integer getAssetScheduleChecklistId() {
		return assetScheduleChecklistId;
	}

	public void setAssetScheduleChecklistId(Integer assetScheduleChecklistId) {
		this.assetScheduleChecklistId = assetScheduleChecklistId;
	}

	public String getChecklistCode() {
		return checklistCode;
	}

	public void setChecklistCode(String checklistCode) {
		this.checklistCode = checklistCode;
	}

	public String getChecklistName() {
		return checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
	}

	public Integer getChecklistNameId() {
		return checklistNameId;
	}

	public void setChecklistNameId(Integer checklistNameId) {
		this.checklistNameId = checklistNameId;
	}

	public Integer getAssetScheduleId() {
		return assetScheduleId;
	}

	public void setAssetScheduleId(Integer assetScheduleId) {
		this.assetScheduleId = assetScheduleId;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getScheduledRemark() {
		return scheduledRemark;
	}

	public void setScheduledRemark(String scheduledRemark) {
		this.scheduledRemark = scheduledRemark;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public String getCompletionRemark() {
		return completionRemark;
	}

	public void setCompletionRemark(String completionRemark) {
		this.completionRemark = completionRemark;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public List<AssetScheduleChecklistDto> getGetChecklistData() {
		return getChecklistData;
	}

	public void setGetChecklistData(List<AssetScheduleChecklistDto> getChecklistData) {
		this.getChecklistData = getChecklistData;
	}

	public Integer getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Integer assignTo) {
		this.assignTo = assignTo;
	}

	public Date getAssignOn() {
		return assignOn;
	}

	public void setAssignOn(Date assignOn) {
		this.assignOn = assignOn;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}


	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getPmsDetailsId() {
		return pmsDetailsId;
	}

	public void setPmsDetailsId(Integer pmsDetailsId) {
		this.pmsDetailsId = pmsDetailsId;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getPmsDetails() {
		return pmsDetails;
	}

	public void setPmsDetails(String pmsDetails) {
		this.pmsDetails = pmsDetails;
	}

	public String getPmsRemarks() {
		return pmsRemarks;
	}

	public void setPmsRemarks(String pmsRemarks) {
		this.pmsRemarks = pmsRemarks;
	}
//
//	public Character getIsCompleted() {
//		return isCompleted;
//	}
//
//	public void setIsCompleted(Character isCompleted) {
//		this.isCompleted = isCompleted;
//	}
//
//	public Character getIsUnderObservation() {
//		return isUnderObservation;
//	}
//
//	public void setIsUnderObservation(Character isUnderObservation) {
//		this.isUnderObservation = isUnderObservation;
//	}
//
//	public Character getIsSendFor() {
//		return isSendFor;
//	}
//
//	public void setIsSendFor(Character isSendFor) {
//		this.isSendFor = isSendFor;
//	}

	public Date getUnderObservationDate() {
		return underObservationDate;
	}

	public void setUnderObservationDate(Date underObservationDate) {
		this.underObservationDate = underObservationDate;
	}

	public String getUnderObservationTime() {
		return underObservationTime;
	}

	public void setUnderObservationTime(String underObservationTime) {
		this.underObservationTime = underObservationTime;
	}

	public List<SpareConsumptionDto> getGetSpareConsumptionList() {
		return getSpareConsumptionList;
	}

	public void setGetSpareConsumptionList(List<SpareConsumptionDto> getSpareConsumptionList) {
		this.getSpareConsumptionList = getSpareConsumptionList;
	}

	public Integer getSpareConsumptionId() {
		return spareConsumptionId;
	}

	public void setSpareConsumptionId(Integer spareConsumptionId) {
		this.spareConsumptionId = spareConsumptionId;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Integer getAssetQty() {
		return assetQty;
	}

	public void setAssetQty(Integer assetQty) {
		this.assetQty = assetQty;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getAssignRemark() {
		return assignRemark;
	}

	public void setAssignRemark(String assignRemark) {
		this.assignRemark = assignRemark;
	}

	public Character getWorkOrderStatus() {
		return workOrderStatus;
	}

	public void setWorkOrderStatus(Character workOrderStatus) {
		this.workOrderStatus = workOrderStatus;
	}
}
