package com.param.opd.coversheet.model;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQuery(name = "UPDATE_TRANSLATED_INSTRUCTION",
query = " UPDATE TranslatedInstructionMaster translatedInstructionMaster "
		+ "  SET translatedInstructionMaster.status = :status"
		+ " WHERE translatedInstructionMaster.organizationId= :organizationId "
		+ " AND translatedInstructionMaster.unitId = :unitId"
		+ " AND translatedInstructionMaster.instructionId = :instructionId")

@Entity
@Table(name = "t_translated_instruction", schema = "public")
@SequenceGenerator(name = "t_translated_instruction_seq", sequenceName = "public.t_translated_instruction_seq", allocationSize = 1)
public class TranslatedInstructionMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_translated_instruction_seq")
	@Column(name = "translated_instruction_id")
	private Integer translatedInstructionId;
	
	@Column(name = "instruction_id")
	private Integer instructionId;

	@Column(name = "language_id")
	private Integer languageId;

	@Column(name = "instruction_desc")
	private String desc;

	@Column(name = "converted_text")
	private String convertedText;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getTranslatedInstructionId() {
		return translatedInstructionId;
	}

	public void setTranslatedInstructionId(Integer translatedInstructionId) {
		this.translatedInstructionId = translatedInstructionId;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	

	

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getConvertedText() {
		return convertedText;
	}

	public void setConvertedText(String convertedText) {
		this.convertedText = convertedText;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	
	
}
