package com.param.entity.model.opd;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_administration_worklist_time_details", schema="pharmacy")
@SequenceGenerator(name = "t_administration_worklist_time_details_seq", sequenceName = " pharmacy.t_administration_worklist_time_details_seq", allocationSize = 1)
public class AdministrationWorklistTimeDetails {

	@Id
	@Column(name="administration_worklist_time_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_administration_worklist_time_details_seq")
	private Integer administrationWorklistTimeDetailsId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="sale_details_id")
	private Integer saleDetailsId;
	
	@Column(name="administration_date")
	private Date administrationDate;
	
	@Column(name="administration_remark")
	private String administrationRemark;
	
	@Column(name="administration_time")
	private String administrationTime;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="administrator_qty")
	private Integer administratorQty;
	
	@Column(name="prescription_detail_id")
	private Integer prescriptionDetailId;
	

	public Integer getAdministrationWorklistTimeDetailsId() {
		return administrationWorklistTimeDetailsId;
	}

	public void setAdministrationWorklistTimeDetailsId(Integer administrationWorklistTimeDetailsId) {
		this.administrationWorklistTimeDetailsId = administrationWorklistTimeDetailsId;
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

	public Integer getSaleDetailsId() {
		return saleDetailsId;
	}

	public void setSaleDetailsId(Integer saleDetailsId) {
		this.saleDetailsId = saleDetailsId;
	}

	public Date getAdministrationDate() {
		return administrationDate;
	}

	public void setAdministrationDate(Date administrationDate) {
		this.administrationDate = administrationDate;
	}

	public String getAdministrationRemark() {
		return administrationRemark;
	}

	public void setAdministrationRemark(String administrationRemark) {
		this.administrationRemark = administrationRemark;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getAdministratorQty() {
		return administratorQty;
	}

	public void setAdministratorQty(Integer administratorQty) {
		this.administratorQty = administratorQty;
	}

	public Integer getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Integer prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}
	
}
