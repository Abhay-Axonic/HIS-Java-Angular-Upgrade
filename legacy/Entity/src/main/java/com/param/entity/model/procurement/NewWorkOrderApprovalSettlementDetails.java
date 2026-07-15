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
@Table(name = "t_new_work_order_settlement_details", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_settlement_details_seq", sequenceName = "procurement.t_new_work_order_settlement_details_seq", allocationSize = 1)

public class NewWorkOrderApprovalSettlementDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_settlement_details_seq")
	@Column(name = "workorder_settlement_details_id")
	private Integer workorderSettlementDetailsId;

	@Column(name = "workorder_settlement_id")
	private Integer workorderSettlementId;

	@Column(name = "workorder_request_approval_id")
	private Integer workorderRequestApprovalId;

	@Column(name = "workorder_request_id")
	private Integer workorderRequestId;

	@Column(name = "paid_amount")
	private Double paidAmount;

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

	public Integer getWorkorderSettlementDetailsId() {
		return workorderSettlementDetailsId;
	}

	public void setWorkorderSettlementDetailsId(Integer workorderSettlementDetailsId) {
		this.workorderSettlementDetailsId = workorderSettlementDetailsId;
	}

	public Integer getWorkorderSettlementId() {
		return workorderSettlementId;
	}

	public void setWorkorderSettlementId(Integer workorderSettlementId) {
		this.workorderSettlementId = workorderSettlementId;
	}

	public Integer getWorkorderRequestApprovalId() {
		return workorderRequestApprovalId;
	}

	public void setWorkorderRequestApprovalId(Integer workorderRequestApprovalId) {
		this.workorderRequestApprovalId = workorderRequestApprovalId;
	}

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
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