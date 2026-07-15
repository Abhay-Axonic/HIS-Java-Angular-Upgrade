package com.param.pharmacy.master.unit.dto.storewiseGenericMapper;

public class VedResponseList {

	protected Integer id;

	protected String name;
	
	protected String code;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public VedResponseList(Integer id, String name, String code) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}

	
}
