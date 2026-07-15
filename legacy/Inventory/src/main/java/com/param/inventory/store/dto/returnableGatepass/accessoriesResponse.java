package com.param.inventory.store.dto.returnableGatepass;

public class accessoriesResponse {
	
	protected Integer id;
	protected Integer itemId;
	protected Long batchId;
	protected Integer returnQuantity;
	protected String specification;
	protected Integer approvedQuantity;
	protected String itemCode;
	protected String itemName;	
	
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
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
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
	
	public accessoriesResponse()
	{
		super();
	}
	public accessoriesResponse( Integer id,Long batchId,Integer itemId,String itemCode,String itemName,Integer quantity, String specification, Integer approvedQty) {
		super();
		this.id=id;
		this.batchId=batchId;
		this.itemId=itemId;
		this.returnQuantity = quantity;
		this.specification = specification;
		this.approvedQuantity=approvedQty;
		this.itemCode=itemCode;
		this.itemName=itemName;
		
	}
	
	
}
