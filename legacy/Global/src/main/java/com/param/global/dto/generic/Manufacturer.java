package com.param.global.dto.generic;

public class Manufacturer {

	protected Integer id;

	protected String name;
	
	

	public Manufacturer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Manufacturer(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

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

}
