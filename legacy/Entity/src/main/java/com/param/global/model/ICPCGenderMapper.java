package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity 
@Table(name = "t_icpc_system_gender_mapper", schema = "emr")
@SequenceGenerator(name = "t_icpc_system_gender_mapper_seq", sequenceName = "emr.t_icpc_system_gender_mapper_seq", allocationSize = 1)
public class ICPCGenderMapper {
	@Id
	@Column(name = "icpc_system_gender_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_icpc_system_gender_mapper_seq")
	private Integer icpcSystemGenderMapperId;

	@Column(name = "icpc_system_mapper_id")
	private Integer icpcSystemMapperId;
	
	@Column(name = "gender_id")
	private Integer genderId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

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

	
	public Integer getIcpcSystemGenderMapperId() {
		return icpcSystemGenderMapperId;
	}

	public void setIcpcSystemGenderMapperId(Integer icpcSystemGenderMapperId) {
		this.icpcSystemGenderMapperId = icpcSystemGenderMapperId;
	}

	public Integer getIcpcSystemMapperId() {
		return icpcSystemMapperId;
	}

	public void setIcpcSystemMapperId(Integer icpcSystemMapperId) {
		this.icpcSystemMapperId = icpcSystemMapperId;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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
	
	
}
