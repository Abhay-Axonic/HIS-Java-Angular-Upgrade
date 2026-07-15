package com.param.global.model;

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
	
	@NamedQuery(name="GET_OUTPUT_TYPE_MASTER_LIST",
		query="SELECT outputTypeMaster.outputTypeId AS outputTypeId,outputTypeMaster.outputTypeCode AS outputTypeCode, outputTypeMaster.outputTypeDesc AS outputTypeDesc,"
					+ " outputTypeMaster.organizationId AS organizationId, outputTypeMaster.unitId AS unitId, outputTypeMaster.status AS status"
					+ " FROM OutputTypeMaster outputTypeMaster"
					+ " WHERE  outputTypeMaster.organizationId= :organizationId "
					+ " AND outputTypeMaster.unitId= :unitId"
					+ " ORDER BY outputTypeMaster.outputTypeDesc asc"
					)
	})


@Entity
@Table(name = "m_output_type_master", schema = "public")
@SequenceGenerator(name = "m_output_type_master_seq", sequenceName = "public.m_output_type_master_seq", allocationSize = 1)

public class OutputTypeMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_output_type_master_seq")
	
	@Column(name = "output_type_id")
	private Integer outputTypeId;
	
	@Column(name = "output_type_code")
	private String outputTypeCode;
	
	@Column(name = "output_type_desc")
	private String outputTypeDesc;
	

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
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




	public Integer getOutputTypeId() {
		return outputTypeId;
	}

	public void setOutputTypeId(Integer outputTypeId) {
		this.outputTypeId = outputTypeId;
	}

	public String getOutputTypeCode() {
		return outputTypeCode;
	}

	public void setOutputTypeCode(String outputTypeCode) {
		this.outputTypeCode = outputTypeCode;
	}

	public String getOutputTypeDesc() {
		return outputTypeDesc;
	}

	public void setOutputTypeDesc(String outputTypeDesc) {
		this.outputTypeDesc = outputTypeDesc;
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
