package com.param.entity.model.procurement;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;


@Entity
@Table(name = "t_new_work_order_settlement", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_settlement_seq", sequenceName = "procurement.t_new_work_order_settlement_seq", allocationSize = 1)

public class NewWorkOrderApprovalSettlement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_settlement_seq")
	@Column(name = "workorder_settlement_id")
	private Integer workorderSettlementId;

	@Column(name = "workorder_settlement_number")
	private String workorderSettlementNumber;

	@Column(name = "settlement_amount")
	private Double settlementAmount;

	@Column(name = "vendor_id")
	private Integer vendorId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "status")
	private String status;

	public Integer getWorkorderSettlementId() {
		return workorderSettlementId;
	}

	public void setWorkorderSettlementId(Integer workorderSettlementId) {
		this.workorderSettlementId = workorderSettlementId;
	}

	public String getWorkorderSettlementNumber() {
		return workorderSettlementNumber;
	}

	public void setWorkorderSettlementNumber(String workorderSettlementNumber) {
		this.workorderSettlementNumber = workorderSettlementNumber;
	}

	public Double getSettlementAmount() {
		return settlementAmount;
	}

	public void setSettlementAmount(Double settlementAmount) {
		this.settlementAmount = settlementAmount;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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