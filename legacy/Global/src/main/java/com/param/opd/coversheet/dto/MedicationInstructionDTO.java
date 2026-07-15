package com.param.opd.coversheet.dto;


/*
 * 
 * Created by Tushar
 * 
 */
public class MedicationInstructionDTO {

	private int medication_instruction_id;
	private String medication_instruction_details;
	
	public int getMedication_instruction_id() {
		return medication_instruction_id;
	}
	public void setMedication_instruction_id(int medication_instruction_id) {
		this.medication_instruction_id = medication_instruction_id;
	}
	public String getMedication_instruction_details() {
		return medication_instruction_details;
	}
	public void setMedication_instruction_details(
			String medication_instruction_details) {
		this.medication_instruction_details = medication_instruction_details;
	}
}
