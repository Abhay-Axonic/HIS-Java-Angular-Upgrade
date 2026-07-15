package com.param.global.dto.global;

public class InsuranceCompanyDetails {
	
	private Integer id;
	
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public InsuranceCompanyDetails(Integer id, String description) {
		super();
		this.id = id;
		this.description = description;
	}
	
}