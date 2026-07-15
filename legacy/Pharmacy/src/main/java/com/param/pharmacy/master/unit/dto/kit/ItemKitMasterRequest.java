package com.param.pharmacy.master.unit.dto.kit;

public class ItemKitMasterRequest {
	
	protected Integer id;
	protected Integer itemId;
	
	protected Integer quantity;
	protected Boolean status;
	protected Integer uomUnitId;
	protected Integer uomTypeId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getUomUnitId() {
		return uomUnitId;
	}
	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}
	public Integer getUomTypeId() {
		return uomTypeId;
	}
	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	
	
	

}
