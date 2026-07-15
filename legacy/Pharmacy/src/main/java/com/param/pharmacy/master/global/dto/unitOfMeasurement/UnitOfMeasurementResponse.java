package com.param.pharmacy.master.global.dto.unitOfMeasurement;

import com.param.service.global.model.CookieObjectDto;

public abstract class UnitOfMeasurementResponse extends CookieObjectDto{

	protected Integer id;

	protected Boolean status;

	
	public UnitOfMeasurementResponse(Integer id, Boolean status) {
		super();
		this.id = id;
		this.status = status;
	}
	
	public UnitOfMeasurementResponse() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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