package com.param.assetManagement.fixedAssetOpeningBalance.dto;

public class PMSFrequencies {
	
	private Integer id;
	
	private String code;
	
	private String description;

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

	public void setDescription(String description) {
		this.description = description;
	}

	public PMSFrequencies(Integer id, String code, String description) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
	}

	public PMSFrequencies() {
		super();
	}
	
}