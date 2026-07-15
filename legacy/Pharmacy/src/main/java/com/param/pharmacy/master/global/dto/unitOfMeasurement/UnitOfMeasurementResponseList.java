package com.param.pharmacy.master.global.dto.unitOfMeasurement;

public abstract class UnitOfMeasurementResponseList {

	protected Integer itemId;

	protected Boolean status;

	
	public UnitOfMeasurementResponseList(Integer itemId, Boolean status) {
		super();
		this.itemId = itemId;
		this.status = status;
	}
	
	public UnitOfMeasurementResponseList() {
	}


	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}

/*
 * "id": 1, "status": true,
 */