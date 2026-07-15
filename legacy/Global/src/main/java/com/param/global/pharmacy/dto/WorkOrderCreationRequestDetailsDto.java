package com.param.global.pharmacy.dto;

import java.util.List;

import com.param.entity.model.procurement.NewWorkOrderCreationStagedDetailsMapper;

public class WorkOrderCreationRequestDetailsDto {
	private Integer workorderRequestDetialsId;
	private Integer workorderRequestId;
	private Integer itemId;
	private Integer storeId;
	private String itemName;
	private Integer itemQuantity;
	private Double itemRate;
	private Double itemMrp;
	private Double itemDiscPercentage;
	private Double itemDiscAmount;
	private Integer itemTaxId;
	private String itemTax;
	private Double itemTaxAmount;
	private Double itemNetAmount;
	private String itemRemark;
	private String status;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private Integer unitId;
	private Integer orgId;
	private List<NewWorkOrderCreationStagedDetailsMapper> workOrderCreationStagedListData;
	private Integer vendorId;
	private Object taxpercentObj;
	private Integer orderDetailsId;
	private Integer billDetailsId;
	private String workorderRequestNumber;
	private String serviceName;
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getWorkorderRequestNumber() {
		return workorderRequestNumber;
	}

	public void setWorkorderRequestNumber(String workorderRequestNumber) {
		this.workorderRequestNumber = workorderRequestNumber;
	}

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(Integer itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public Double getItemRate() {
		return itemRate;
	}

	public void setItemRate(Double itemRate) {
		this.itemRate = itemRate;
	}

	public Double getItemMrp() {
		return itemMrp;
	}

	public void setItemMrp(Double itemMrp) {
		this.itemMrp = itemMrp;
	}

	public Double getItemDiscPercentage() {
		return itemDiscPercentage;
	}

	public void setItemDiscPercentage(Double itemDiscPercentage) {
		this.itemDiscPercentage = itemDiscPercentage;
	}

	public Double getItemDiscAmount() {
		return itemDiscAmount;
	}

	public void setItemDiscAmount(Double itemDiscAmount) {
		this.itemDiscAmount = itemDiscAmount;
	}

	public Integer getItemTaxId() {
		return itemTaxId;
	}

	public void setItemTaxId(Integer itemTaxId) {
		this.itemTaxId = itemTaxId;
	}

	public String getItemTax() {
		return itemTax;
	}

	public void setItemTax(String itemTax) {
		this.itemTax = itemTax;
	}

	public Double getItemTaxAmount() {
		return itemTaxAmount;
	}

	public void setItemTaxAmount(Double itemTaxAmount) {
		this.itemTaxAmount = itemTaxAmount;
	}

	public Double getItemNetAmount() {
		return itemNetAmount;
	}

	public void setItemNetAmount(Double itemNetAmount) {
		this.itemNetAmount = itemNetAmount;
	}

	public String getItemRemark() {
		return itemRemark;
	}

	public void setItemRemark(String itemRemark) {
		this.itemRemark = itemRemark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Object getTaxpercentObj() {
		return taxpercentObj;
	}

	public void setTaxpercentObj(Object taxpercentObj) {
		this.taxpercentObj = taxpercentObj;
	}

	public Integer getWorkorderRequestDetialsId() {
		return workorderRequestDetialsId;
	}

	public void setWorkorderRequestDetialsId(Integer workorderRequestDetialsId) {
		this.workorderRequestDetialsId = workorderRequestDetialsId;
	}

	public List<NewWorkOrderCreationStagedDetailsMapper> getWorkOrderCreationStagedListData() {
		return workOrderCreationStagedListData;
	}

	public void setWorkOrderCreationStagedListData(
			List<NewWorkOrderCreationStagedDetailsMapper> workOrderCreationStagedListData) {
		this.workOrderCreationStagedListData = workOrderCreationStagedListData;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getBillDetailsId() {
		return billDetailsId;
	}

	public void setBillDetailsId(Integer billDetailsId) {
		this.billDetailsId = billDetailsId;
	}

}
