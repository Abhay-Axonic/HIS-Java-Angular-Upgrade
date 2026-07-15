package com.param.pharmacy.master.unit.dto.configuration;

public class ResetDto {

	protected Integer id;

	protected String resetName;
	
	public ResetDto() {
		super();
	}
	public ResetDto(Integer id, String resetName) {
		super();
		this.id = id;
		this.resetName = resetName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResetName() {
		return resetName;
	}

	public void setResetName(String resetName) {
		this.resetName = resetName;
	}

}
