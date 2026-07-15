package com.param.assetManagement.nonReturnableGatepass.dto;

import java.util.List;

public class NonReturnableGatepssRequestItem {

	protected Integer id;
	
	protected Integer auctionRequisitionDetailId;
	
	protected Long batchId;
	
	protected Integer quantity;
	
	protected Double amount;
	
	protected String remark;
	
	protected Integer statusId;
	
	protected List<NonReturnableGatepssRequestAccessories> accessories;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAuctionRequisitionDetailId() {
		return auctionRequisitionDetailId;
	}

	public void setAuctionRequisitionDetailId(Integer auctionRequisitionDetailId) {
		this.auctionRequisitionDetailId = auctionRequisitionDetailId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<NonReturnableGatepssRequestAccessories> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<NonReturnableGatepssRequestAccessories> accessories) {
		this.accessories = accessories;
	}

}