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
@Table(name = "t_new_work_order_rejection_details", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_rejection_details_seq", sequenceName = "procurement.t_new_work_order_rejection_details_seq", allocationSize = 1)

public class NewWorkOrderRejectionDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_rejection_details_seq")
	@Column(name = "work_order_rejection_details_id")
	private Integer workorderRejectionDetailsId;
	
	@Column(name = "workorder_request_id")
	private Integer workorderRequestId;
	
	
	@Column(name = "cancel_reason_Id")
	private Integer  cancelReasonId;
	
	@Column(name="reject_note")
	private String  rejectNote;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="status")
	private String status;

	public Integer getWorkorderRejectionDetailsId() {
		return workorderRejectionDetailsId;
	}

	public void setWorkorderRejectionDetailsId(Integer workorderRejectionDetailsId) {
		this.workorderRejectionDetailsId = workorderRejectionDetailsId;
	}

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

	
}