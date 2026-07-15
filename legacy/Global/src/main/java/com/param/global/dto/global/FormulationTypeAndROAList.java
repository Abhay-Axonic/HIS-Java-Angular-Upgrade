package com.param.global.dto.global;

public class FormulationTypeAndROAList {
	protected Integer id;

	protected String description;

	public FormulationTypeAndROAList(Integer id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setTaxPercentage(String description) {
		this.description = description;
	}

}
