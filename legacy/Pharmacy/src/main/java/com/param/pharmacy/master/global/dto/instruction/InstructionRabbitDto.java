package com.param.pharmacy.master.global.dto.instruction;

import java.util.Date;

public class InstructionRabbitDto {

	protected Integer instruction_id;

	protected String instruction_code;

	protected String instruction_desc;

	protected Character is_medication;
	
	protected  Character status;
	
	protected Integer created_by;
	
	protected Date created_date;
	
	protected Integer updated_by;
	
	protected Date updated_date;
	
	public InstructionRabbitDto() {
		super();
	}

	public InstructionRabbitDto(Integer instruction_id, String instruction_code, String instruction_desc,
			Character is_medication, Character status, Integer created_by, Date created_date, Integer updated_by,
			Date updated_date) {
		super();
		this.instruction_id = instruction_id;
		this.instruction_code = instruction_code;
		this.instruction_desc = instruction_desc;
		this.is_medication = is_medication;
		this.status = status;
		this.created_by = created_by;
		this.created_date = created_date;
		this.updated_by = updated_by;
		this.updated_date = updated_date;
	}

	public Integer getInstruction_id() {
		return instruction_id;
	}

	public void setInstruction_id(Integer instruction_id) {
		this.instruction_id = instruction_id;
	}

	public String getInstruction_code() {
		return instruction_code;
	}

	public void setInstruction_code(String instruction_code) {
		this.instruction_code = instruction_code;
	}

	public String getInstruction_desc() {
		return instruction_desc;
	}

	public void setInstruction_desc(String instruction_desc) {
		this.instruction_desc = instruction_desc;
	}

	public Character getIs_medication() {
		return is_medication;
	}

	public void setIs_medication(Character is_medication) {
		this.is_medication = is_medication;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	
}
