package com.param.pharmacy.master.global.dto.taxComponent;

public class TaxComponentListDto {

	protected Integer id;
	
	protected String component;
	
	public TaxComponentListDto(Integer id, String component) {
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
