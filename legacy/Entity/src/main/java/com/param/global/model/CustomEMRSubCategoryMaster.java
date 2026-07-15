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
@Table(name = "m_coversheet_access_master", schema = "patient")
@SequenceGenerator(name = "m_coversheet_access_master_seq", sequenceName = "patient.m_coversheet_access_master_seq", allocationSize = 1)
public class CustomEMRSubCategoryMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_coversheet_access_master_seq")
	
	@Column(name = "cus_emr_sub_category_id")
	private Integer cusEmrSubCategoryId;
	
	@Column(name = "cus_emr_category_desc")
	private String cusEmrCategoryDesc;
	
	@Column(name = "cus_emr_category_id")
	private Integer cusEmrCategoryId;
	
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

	public Integer getCusEmrSubCategoryId() {
		return cusEmrSubCategoryId;
	}

	public void setCusEmrSubCategoryId(Integer cusEmrSubCategoryId) {
		this.cusEmrSubCategoryId = cusEmrSubCategoryId;
	}

	public String getCusEmrCategoryDesc() {
		return cusEmrCategoryDesc;
	}

	public void setCusEmrCategoryDesc(String cusEmrCategoryDesc) {
		this.cusEmrCategoryDesc = cusEmrCategoryDesc;
	}

	public Integer getCusEmrCategoryId() {
		return cusEmrCategoryId;
	}

	public void setCusEmrCategoryId(Integer cusEmrCategoryId) {
		this.cusEmrCategoryId = cusEmrCategoryId;
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
