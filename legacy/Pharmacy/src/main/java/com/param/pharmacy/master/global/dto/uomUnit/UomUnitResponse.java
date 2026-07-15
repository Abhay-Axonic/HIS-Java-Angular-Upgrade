package com.param.pharmacy.master.global.dto.uomUnit;

import com.param.service.global.model.CookieObjectDto;

public abstract class UomUnitResponse extends CookieObjectDto{

	protected Integer id;

	protected String code;

	protected String units;

	protected Boolean status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}

/*
 * "id": 1, "code": "unit code", "units": "BOX", "status": "Active"
 */
