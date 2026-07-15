package com.param.pharmacy.master.global.dto.equipment;

public class EquipmentRequest extends EquipmentResponse {

	protected String code;

	protected String description;

	public EquipmentRequest() {
		super();
		
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
	
}
