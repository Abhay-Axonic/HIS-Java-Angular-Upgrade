package com.param.opd.coversheet.dto;

import java.util.Date;
import java.util.List;

/*
 * 
 * Created By Tushar
 */
public class InstructionDTO {
	
	private int instruction_id;
	private int instruction_details_id;
	private String instruction_details;
	private List<ListOfMasterInstructions> listOfMasterInstructions;
	private String notes;
	private Integer patient_id;
	private Date created_date;
	private Integer created_by;
	private Date updated_date;
	private Integer updated_by;
	private Integer encounter_id;
	private Integer unit_id;
	private Integer organisation_id;
	
	public int getInstruction_id() {
		return instruction_id;
	}
	public void setInstruction_id(int instruction_id) {
		this.instruction_id = instruction_id;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Integer getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(Integer patient_id) {
		this.patient_id = patient_id;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public Date getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	public Integer getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}
	public Integer getEncounter_id() {
		return encounter_id;
	}
	public void setEncounter_id(Integer encounter_id) {
		this.encounter_id = encounter_id;
	}
	public Integer getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}
	public Integer getOrganisation_id() {
		return organisation_id;
	}
	public void setOrganisation_id(Integer organisation_id) {
		this.organisation_id = organisation_id;
	}
	
	public int getInstruction_details_id() {
		return instruction_details_id;
	}
	public void setInstruction_details_id(int instruction_details_id) {
		this.instruction_details_id = instruction_details_id;
	}
	public List<ListOfMasterInstructions> getInstructionDetails() {
		return listOfMasterInstructions;
	}
	public void setInstructionDetails(List<ListOfMasterInstructions> instructionDetails) {
		this.listOfMasterInstructions = instructionDetails;
	}
	public String getInstruction_details() {
		return instruction_details;
	}
	public void setInstruction_details(String instruction_details) {
		this.instruction_details = instruction_details;
	}

}
