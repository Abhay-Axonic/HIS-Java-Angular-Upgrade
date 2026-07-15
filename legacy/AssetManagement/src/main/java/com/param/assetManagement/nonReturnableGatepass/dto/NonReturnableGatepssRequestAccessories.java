package com.param.assetManagement.nonReturnableGatepass.dto;

public class NonReturnableGatepssRequestAccessories {

	protected Integer id;
	
	protected Long batchId;
	
	protected Integer quantity;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
}