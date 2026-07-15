package com.param.pharmacy.master.global.dto.storageUnit;

import com.param.service.global.model.CookieObjectDto;

public abstract class StorageUnitResponse extends CookieObjectDto {

	protected Integer id;

	protected String code;

	protected String unitName;

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

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
