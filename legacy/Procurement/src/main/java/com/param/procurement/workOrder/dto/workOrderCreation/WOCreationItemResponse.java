package com.param.procurement.workOrder.dto.workOrderCreation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WOCreationItemResponse {
	
	private Integer id;
	
	private Integer itemId;
	
	private String itemName;
	
	private String itemCode;
	
	private Integer batchId;
	
	private String batchNo;
	
	private Integer quantity;
	
	private Double amount;
	
	private Integer currentVendorId;
	
	private String currentVendor;
	
	private Integer statusId;
	
	private Integer workOrderRequestDetailId;
	
	private String specification;
	
	private String inclusionDetails;
	
	private String exclusionDetails;
	
	private String paymentTerm;
	
	private String remark;
	
	private List<WOCreationBatchResponse> batchList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCurrentVendorId() {
		return currentVendorId;
	}

	public void setCurrentVendorId(Integer currentVendorId) {
		this.currentVendorId = currentVendorId;
	}

	public String getCurrentVendor() {
		return currentVendor;
	}

	public void setCurrentVendor(String currentVendor) {
		this.currentVendor = currentVendor;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getWorkOrderRequestDetailId() {
		return workOrderRequestDetailId;
	}

	public void setWorkOrderRequestDetailId(Integer workOrderRequestDetailId) {
		this.workOrderRequestDetailId = workOrderRequestDetailId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(String inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public String getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(String exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<WOCreationBatchResponse> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<WOCreationBatchResponse> batchList) {
		this.batchList = batchList;
	}
	
	public WOCreationItemResponse() {
		super();
	}

}
