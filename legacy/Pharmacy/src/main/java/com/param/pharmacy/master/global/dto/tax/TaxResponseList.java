package com.param.pharmacy.master.global.dto.tax;

public class TaxResponseList {

	protected Integer id;

	protected String name;

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

	public TaxResponseList(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
