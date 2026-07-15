package com.param.pharmacy.op.dto.salesWorkList;

public class FrequencyResponse {

	protected String dose;

	protected String frequency;

	protected String instruction;
	
	protected Long detailId;

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public FrequencyResponse() {
		super();
	}

	public FrequencyResponse(String dose, String frequency, String instruction) {
		super();
		this.dose = dose;
		this.frequency = frequency;
		this.instruction = instruction;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

}
