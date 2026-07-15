package com.param.entity.lis.unit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_header_parameter_mapper", schema = "lab")
@SequenceGenerator(name = "header_parameter_mapper_seq", sequenceName = "lab.header_parameter_mapper_seq", allocationSize = 1)
public class HeaderParameterMapper {

	@Id
	@Column(name = "header_parameter_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "header_parameter_mapper_seq")
	private Integer headerParameterMapperId;

	@Column(name = "parameter_id")
	private Integer parameterId;

	@Column(name = "para_sequence")
	private Integer paraSequence;

	@Column(name = "header_id")
	private Integer headerId;

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
	
	@Column(name = "status")
	private char status;


	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public Integer getParaSequence() {
		return paraSequence;
	}

	public void setParaSequence(Integer paraSequence) {
		this.paraSequence = paraSequence;
	}

	public Integer getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getHeaderParameterMapperId() {
		return headerParameterMapperId;
	}

	public void setHeaderParameterMapperId(Integer headerParameterMapperId) {
		this.headerParameterMapperId = headerParameterMapperId;
	}
	
	
	
}
