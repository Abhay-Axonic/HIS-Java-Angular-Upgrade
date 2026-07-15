package com.param.pharmacy.master.global.dto.formulationType;


public class FormulationTypeResponseList {

	protected Integer id;

	protected String code;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getcode() {
		return code;
	}

	public void setcode(String code) {
		this.code = code;
	}

	public FormulationTypeResponseList(Integer id, String code) {
		super();
		this.id = id;
		this.code = code;
	}
}
