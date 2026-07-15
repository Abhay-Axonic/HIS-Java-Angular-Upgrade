package com.param.pharmacy.master.global.dto.manufacturer;

import com.param.service.global.model.CookieObjectDto;

public abstract class ManufacturerResponse extends CookieObjectDto{

	protected Integer id;

	protected String code;

	protected String name;

	protected String address;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}

/*
 * "id":1, "code": "MAN-1001", "name": "manufacturer name", "address":
 * "address of manufacturer", "status": true
 */