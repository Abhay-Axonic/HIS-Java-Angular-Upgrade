package com.param.assetManagement.maintenance.dto;

public class CorrectiveMaintenanceSparesComsumptionDto {
	
	private Integer correctiveMaintenanceSparesComsumptionId;
	
	private Integer correctiveMaintenanceScheduleId;
	
	private Integer itemId;
	
	private String itemCode;
	
	private String itemName;
	
	private Integer qty;
	
	private String sparesComsumptionRemark;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;

	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId ;

	private Integer organizationId;
	
	private Integer stock;

	public Integer getCorrectiveMaintenanceSparesComsumptionId() {
		return correctiveMaintenanceSparesComsumptionId;
	}

	public void setCorrectiveMaintenanceSparesComsumptionId(Integer correctiveMaintenanceSparesComsumptionId) {
		this.correctiveMaintenanceSparesComsumptionId = correctiveMaintenanceSparesComsumptionId;
	}

	public Integer getCorrectiveMaintenanceScheduleId() {
		return correctiveMaintenanceScheduleId;
	}

	public void setCorrectiveMaintenanceScheduleId(Integer correctiveMaintenanceScheduleId) {
		this.correctiveMaintenanceScheduleId = correctiveMaintenanceScheduleId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getSparesComsumptionRemark() {
		return sparesComsumptionRemark;
	}

	public void setSparesComsumptionRemark(String sparesComsumptionRemark) {
		this.sparesComsumptionRemark = sparesComsumptionRemark;
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

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	
}
