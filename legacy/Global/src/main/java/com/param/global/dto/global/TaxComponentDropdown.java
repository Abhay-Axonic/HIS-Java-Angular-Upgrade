package com.param.global.dto.global;

public class TaxComponentDropdown {

	protected Integer id;
	
	protected String component;
	
	public TaxComponentDropdown(Integer id, String component) {
		super();
		this.id = id;
		this.component = component;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}
	
}
