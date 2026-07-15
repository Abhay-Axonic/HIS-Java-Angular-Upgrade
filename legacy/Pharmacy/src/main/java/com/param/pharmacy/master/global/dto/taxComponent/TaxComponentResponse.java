package com.param.pharmacy.master.global.dto.taxComponent;

import com.param.service.global.model.CookieObjectDto;

public abstract class TaxComponentResponse extends CookieObjectDto {

    protected Integer id;
	
	protected String code;

	protected String component;
	
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

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
