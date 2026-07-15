package com.param.entity.model.procurement;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity
@Table(name = "t_new_work_order_creation_othercharge_details", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_creation_othercharge_details_seq", sequenceName = "procurement.t_new_work_order_creation_othercharge_details_seq", allocationSize = 1)

public class NewWorkOrderCreationOtherChargeDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_creation_othercharge_details_seq")
	@Column(name = "workorder_request_othercharge_detials_id")
	private Integer workorderRequestOtherChargeDetialsId;
	
	@Column(name = "workorder_request_id")
	private Integer workorderRequestId;
	
	@Column(name = "othercharge_type_id")
	private Integer otherchargeTypeId ;
	
	@Column(name = "othercharge_amount")
	private Double otherChargeAmount ;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private String status;

	public Integer getWorkorderRequestOtherChargeDetialsId() {
		return workorderRequestOtherChargeDetialsId;
	}

	public void setWorkorderRequestOtherChargeDetialsId(Integer workorderRequestOtherChargeDetialsId) {
		this.workorderRequestOtherChargeDetialsId = workorderRequestOtherChargeDetialsId;
	}

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
	}

	public Integer getOtherchargeTypeId() {
		return otherchargeTypeId;
	}

	public void setOtherchargeTypeId(Integer otherchargeTypeId) {
		this.otherchargeTypeId = otherchargeTypeId;
	}

	public Double getOtherChargeAmount() {
		return otherChargeAmount;
	}

	public void setOtherChargeAmount(Double otherChargeAmount) {
		this.otherChargeAmount = otherChargeAmount;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	
}