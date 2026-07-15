package com.param.pharmacy.master.global.dto.formulationTypeAndROA;

public class FormulationTypeAndROAList {
protected Integer id;
	
    protected String code ;
	
	protected String description;
	
	public FormulationTypeAndROAList (Integer id, String code, String description) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
	}

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

	public void setTaxPercentage(String description) {
		this.description = description;
	}

}
