package com.param.opd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * 
 * Created by Tushar
 */
@Entity
@Table(name="medication_instruction_master",schema="opd")
@SequenceGenerator(name = "m_seq_medication_intruction_master", sequenceName = "opd.m_seq_medication_intruction_master", allocationSize = 1)
public class MedicationInstructionMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_medication_intruction_master")
	@Column(name="medication_instruction_id")
	private int medication_instruction_id;
	
	@Column(name="medication_instruction_details")
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
