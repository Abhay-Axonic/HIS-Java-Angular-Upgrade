package com.param.pharmacy.master.global.dto.instruction;

public class InstructionRequest {

	protected Integer instructionId;

	protected String instructionCode;

	protected String instructionDesc;

	protected Character isMedication;

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public String getInstructionCode() {
		return instructionCode;
	}

	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public Character getIsMedication() {
		return isMedication;
	}

	public void setIsMedication(Character isMedication) {
		this.isMedication = isMedication;
	}

}
