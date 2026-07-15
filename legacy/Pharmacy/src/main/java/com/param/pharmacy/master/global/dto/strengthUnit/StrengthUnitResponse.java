package com.param.pharmacy.master.global.dto.strengthUnit;

import com.param.service.global.model.CookieObjectDto;

public abstract class StrengthUnitResponse extends CookieObjectDto{

	protected Integer id;

	protected String code;

	protected String unit;

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
