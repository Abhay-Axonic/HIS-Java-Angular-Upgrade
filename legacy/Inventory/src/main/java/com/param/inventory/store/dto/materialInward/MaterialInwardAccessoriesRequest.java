package com.param.inventory.store.dto.materialInward;

public class MaterialInwardAccessoriesRequest {

	protected Integer id;
	protected Integer itemId;
	protected Integer approvedQuantity;
	protected Integer returnQuantity;
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
	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}
	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
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
	
}
