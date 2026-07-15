package com.param.pharmacy.master.unit.dto.assetType;

import com.param.service.global.model.CookieObjectDto;

public abstract class AssetTypeResponse  extends CookieObjectDto{

	protected Integer id;

	protected String code;

	protected String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
