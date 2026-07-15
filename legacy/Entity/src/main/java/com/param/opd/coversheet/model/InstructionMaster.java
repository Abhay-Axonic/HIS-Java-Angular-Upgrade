package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedQueries({
	@NamedQuery(name="GET_INSPECTION_MASTER_LIST",
			query="SELECT im.instructionId AS instructionId,"
					+ " im.instructionCode AS instructionCode,"
					+ " im.instructionDesc AS instructionDesc,"
					+ " im.isMedication AS isMedication,"
					+ " im.status AS status "
					+ " FROM InstructionMaster im "
					+ " WHERE im.organizationId=:orgId "
					+ " ORDER BY im.instructionId DESC "),
		@NamedQuery(name="GET_INSPECTION_MASTER_BY_NAME",
		query="SELECT im.instructionId AS instructionId "			
				+ " FROM InstructionMaster im "
				+ " WHERE LOWER(im.instructionDesc)=:name "
				+ "AND im.organizationId=:orgId"),//minesh
		@NamedQuery(name="GET_INSPECTION_MASTER_BY_NAME_NOT_BY_ID",
		query="SELECT im.instructionId AS instructionId "			
				+ " FROM InstructionMaster im "
				+ " WHERE LOWER(im.instructionDesc)=:name "
				+ " AND im.instructionId!=:inspectionId AND im.organizationId=:orgId "),
		@NamedQuery(name="GET_INSPECTION_MASTER_LIST_BY_ID",
		query="SELECT im.instructionId AS instructionId,"
				+ " im.instructionCode AS instructionCode,"
				+ " im.instructionDesc AS instructionDesc,"
				+ " im.isMedication AS isMedication,"
				+ " im.status AS status "
				+ " FROM InstructionMaster im "
				+ " WHERE im.instructionId=:inspectionId "
				+ " AND im.organizationId=:orgId "),
		
		@NamedQuery(name="GET_ACTIVE_INSPECTION_MASTER_LIST",
		query="SELECT im.instructionId AS instructionId,"
				+ " im.instructionCode AS instructionCode,"
				+ " im.instructionDesc AS instructionDesc,"
				+ " im.isMedication AS isMedication,"
				+ " im.status AS status, "
				+ " im.organizationId AS organizationId "
				+ " FROM InstructionMaster im "
				+ " WHERE im.organizationId=:orgId "
				+ " AND im.status='A' "
				+ " AND im.isMedication=:isMedication "
				+ " ORDER BY im.instructionDesc ASC")
})
@Entity
@Table(name = "m_instruction", schema = "public")
@SequenceGenerator(name = "instruction_master_seq", sequenceName = "public.instruction_master_seq", allocationSize = 1)
public class InstructionMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instruction_master_seq")
	@Column(name = "instruction_id")
	private Integer instructionId;

	@Column(name = "instruction_code")
	private String instructionCode;

	@Column(name = "instruction_desc")
	private String instructionDesc;

	@Column(name = "is_medication")
	private Character isMedication;

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
	
	
	

}
