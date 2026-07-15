package com.param.global.otims.dto;

import java.util.List;

public class EquipmentSchedulingDto {

	private Integer equipmentSchedulingId;
	
	private Integer assetCategoryId;
	
	private Integer batchId;
	
	private Integer itemId;
	
	private String equipmentSchedulingDate;
	
	private String fromTime;
	
	private String toTime;

	private Integer dayId;
	
	private Character status;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String color;

	private List<EquipmentSchedulingTimeDetailsDto> batchAndItem;
	
	private List<OtSchedulingTimeDetailsDto> timeDetails;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private String itemName;
	
	private String assetCategory;
	
	private Integer correctiveMaintenanceId;
	
	private Integer proReqId;
	
	private Integer procedureEquipmentScheduleId;
	
	public Integer getEquipmentSchedulingId() {
		return equipmentSchedulingId;
	}

	public void setEquipmentSchedulingId(Integer equipmentSchedulingId) {
		this.equipmentSchedulingId = equipmentSchedulingId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getEquipmentSchedulingDate() {
		return equipmentSchedulingDate;
	}

	public void setEquipmentSchedulingDate(String equipmentSchedulingDate) {
		this.equipmentSchedulingDate = equipmentSchedulingDate;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public List<EquipmentSchedulingTimeDetailsDto> getBatchAndItem() {
		return batchAndItem;
	}

	public void setBatchAndItem(List<EquipmentSchedulingTimeDetailsDto> batchAndItem) {
		this.batchAndItem = batchAndItem;
	}

	public List<OtSchedulingTimeDetailsDto> getTimeDetails() {
		return timeDetails;
	}

	public void setTimeDetails(List<OtSchedulingTimeDetailsDto> timeDetails) {
		this.timeDetails = timeDetails;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Integer getCorrectiveMaintenanceId() {
		return correctiveMaintenanceId;
	}

	public void setCorrectiveMaintenanceId(Integer correctiveMaintenanceId) {
		this.correctiveMaintenanceId = correctiveMaintenanceId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getProcedureEquipmentScheduleId() {
		return procedureEquipmentScheduleId;
	}

	public void setProcedureEquipmentScheduleId(Integer procedureEquipmentScheduleId) {
		this.procedureEquipmentScheduleId = procedureEquipmentScheduleId;
	}

	
}
