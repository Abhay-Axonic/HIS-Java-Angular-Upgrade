package com.param.opd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/*
 * 
 * Created by Tushar
 */

@Entity
@Table(name="t_instruction_admission_mapper",schema="opd")
@SequenceGenerator(name = "t_instruction_admission_mapper_seq", sequenceName = "opd.t_instruction_admission_mapper_seq", allocationSize = 1)
public class InstructionAdmissionMapper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_instruction_admission_mapper_seq")
	@Column(name = "instruction_admission_mapper_id", unique = true, nullable = false)
	private int instruction_admission_mapper_id;
	
	@Column(name = "admission_id", nullable = false)
	private Integer admission_id;
	
	@Column(name = "instruction_post_admission", nullable = false)
	private String instruction_post_admission;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
	private AdmissionModel admissionModel;

	public int getInstruction_admission_mapper_id() {
		return instruction_admission_mapper_id;
	}

	public void setInstruction_admission_mapper_id(
			int instruction_admission_mapper_id) {
		this.instruction_admission_mapper_id = instruction_admission_mapper_id;
	}

	public Integer getAdmission_id() {
		return admission_id;
	}

	public void setAdmission_id(Integer admission_id) {
		this.admission_id = admission_id;
	}

	public String getInstruction_post_admission() {
		return instruction_post_admission;
	}

	public void setInstruction_post_admission(String instruction_post_admission) {
		this.instruction_post_admission = instruction_post_admission;
	}

	public AdmissionModel getAdmissionModel() {
		return admissionModel;
	}

	public void setAdmissionModel(AdmissionModel admissionModel) {
		this.admissionModel = admissionModel;
	}

}
