package com.param.global.dto.generic;

public class Generic {

	protected Integer id;

	protected String name;
	
	protected Boolean isCombination;

	public Generic(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Generic(Integer id, String name, Boolean isCombination) {
		super();
		this.id = id;
		this.name = name;
		this.isCombination = isCombination;
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

	public Boolean getIsCombination() {
		return isCombination;
	}

	public void setIsCombination(Boolean isCombination) {
		this.isCombination = isCombination;
	}

}
