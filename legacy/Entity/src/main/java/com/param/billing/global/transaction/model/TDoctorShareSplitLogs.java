package com.param.billing.global.transaction.model;


import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;



@Entity
@Table(name="t_doctor_share_split_logs", schema="billing")
@SequenceGenerator(name = "t_doctor_share_split_logs_seq", sequenceName = "billing.t_doctor_share_split_logs_seq", allocationSize = 1)
public class TDoctorShareSplitLogs {
	@Id
	@Column(name="doctor_share_split_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_doctor_share_split_logs_seq")
	private Integer doctorShareSplitId;
	
	@Column(name = "parent_doc_share_id")
	private Integer parentDocShareId;
	
	@Column(name = "doctor_share_id")
	private Integer doctorShareId;

	@Column(name = "doc_id")
	private Integer docId;
	
	@Column(name = "old_share_percentage")
	private double oldSharePercentage;
	
	@Column(name = "new_share_percentage")
	private double newSharePercentage;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "share_amount")
	private double shareAmount;
	
	@Column(name = "priority")
	private Integer priority;
	
	public Integer getParentDocShareId() {
		return parentDocShareId;
	}

	public void setParentDocShareId(Integer parentDocShareId) {
		this.parentDocShareId = parentDocShareId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public double getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(double shareAmount) {
		this.shareAmount = shareAmount;
	}

	public Integer getDoctorShareSplitId() {
		return doctorShareSplitId;
	}

	public void setDoctorShareSplitId(Integer doctorShareSplitId) {
		this.doctorShareSplitId = doctorShareSplitId;
	}

	public Integer getDoctorShareId() {
		return doctorShareId;
	}

	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public double getOldSharePercentage() {
		return oldSharePercentage;
	}

	public void setOldSharePercentage(double oldSharePercentage) {
		this.oldSharePercentage = oldSharePercentage;
	}

	public double getNewSharePercentage() {
		return newSharePercentage;
	}

	public void setNewSharePercentage(double newSharePercentage) {
		this.newSharePercentage = newSharePercentage;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	

}
