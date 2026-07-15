package com.param.inventory.store.dto.returnableGatepass;

public class accessoriesReuest {
	protected Integer id;
	protected Integer itemId;
	protected Integer batchId;
	protected Integer returnQuantity;
	protected Integer approvedQuantity;
	
	protected String specification;
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
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Integer getReturnQuantity() {
		return returnQuantity;
	}
	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}
	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}
	

}
