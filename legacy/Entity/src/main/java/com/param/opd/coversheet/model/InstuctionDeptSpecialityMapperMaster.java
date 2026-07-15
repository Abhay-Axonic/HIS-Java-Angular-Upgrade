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

@Entity
@Table(name = "m_instuction_dept_speciality_mapper", schema = "public")
@SequenceGenerator(name = "m_seq_instuction_dept_speciality_mapper", sequenceName = "public.m_seq_instuction_dept_speciality_mapper", allocationSize = 1)
public class InstuctionDeptSpecialityMapperMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_instuction_dept_speciality_mapper")
	@Column(name = "instuction_dept_speciality_mapper_id")
	private Integer instuctionDeptSpecialityMapperId ;

	
	@Column(name = "instruction_id")
	private Integer instructionId; 
	
	@Column(name = "dept_speciality_id")
	private Integer deptSpecialityId;


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

	@Column(name = "org_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getInstuctionDeptSpecialityMapperId() {
		return instuctionDeptSpecialityMapperId;
	}

	public void setInstuctionDeptSpecialityMapperId(Integer instuctionDeptSpecialityMapperId) {
		this.instuctionDeptSpecialityMapperId = instuctionDeptSpecialityMapperId;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
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

	public Integer getDeptSpecialityId() {
		return deptSpecialityId;
	}

	public void setDeptSpecialityId(Integer deptSpecialityId) {
		this.deptSpecialityId = deptSpecialityId;
	}

}
