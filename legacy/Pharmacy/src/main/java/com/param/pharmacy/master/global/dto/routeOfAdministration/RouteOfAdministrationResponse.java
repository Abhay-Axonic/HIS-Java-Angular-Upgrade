package com.param.pharmacy.master.global.dto.routeOfAdministration;

import com.param.service.global.model.CookieObjectDto;

public abstract class RouteOfAdministrationResponse extends CookieObjectDto {

	protected Integer id;

	protected String code;

	protected String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
