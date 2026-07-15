package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.PatientRegistration;

@Entity
@Table(name = "t_issue_blood_bag", schema = "blood_bank")
@SequenceGenerator(name = "issue_blood_bag_seq", sequenceName = "blood_bank.issue_blood_bag_seq", allocationSize = 1)
public class IssueBloodBagMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_blood_bag_seq")
	@Column(name = "issue_blood_bag_id")
	private Integer issueBloodBagId ;
	
	@Column(name = "patient_id")
	private Integer patientId ;
	
	@Column(name = "cross_match_register_id ")
	private Integer crossMatchRegisterId ;

	@Column(name = "bto_id")
	private Integer btoId ;
	
	@Column(name = "issuer_id")
	private Integer issuerId;
	
	@Column(name = "received_id")
	private Integer receivedId;
	
	@Column(name = "blood_unit_qty")
	private Integer bloodUnitQty;
	
	@Column(name = "requisition_date")
	private Date requisitionDate;
	
	@Column(name = "issue_date")
	private Date issueDate ;
	
	@Column(name = "issue_status_id")
	private Integer issueStatusId  ;
	
	@Column(name = "is_exported")
	private boolean isExported;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "blood_bag_issue_no")
	private String bloodBagIssueNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cross_match_register_id", insertable = false, updatable = false)
	private CrossMatchRegister crossMatchRegister;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	public Integer getIssueBloodBagId() {
		return issueBloodBagId;
	}

	public void setIssueBloodBagId(Integer issueBloodBagId) {
		this.issueBloodBagId = issueBloodBagId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getCrossMatchRegisterId() {
		return crossMatchRegisterId;
	}

	public void setCrossMatchRegisterId(Integer crossMatchRegisterId) {
		this.crossMatchRegisterId = crossMatchRegisterId;
	}

	public Integer getBtoId() {
		return btoId;
	}

	public void setBtoId(Integer btoId) {
		this.btoId = btoId;
	}

	public Integer getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(Integer issuerId) {
		this.issuerId = issuerId;
	}

	public Integer getReceivedId() {
		return receivedId;
	}

	public void setReceivedId(Integer receivedId) {
		this.receivedId = receivedId;
	}

	public Integer getBloodUnitQty() {
		return bloodUnitQty;
	}

	public void setBloodUnitQty(Integer bloodUnitQty) {
		this.bloodUnitQty = bloodUnitQty;
	}

	public Date getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(Date requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getIssueStatusId() {
		return issueStatusId;
	}

	public void setIssueStatusId(Integer issueStatusId) {
		this.issueStatusId = issueStatusId;
	}

	public boolean isExported() {
		return isExported;
	}

	public void setExported(boolean isExported) {
		this.isExported = isExported;
	}

	public String getBloodBagIssueNo() {
		return bloodBagIssueNo;
	}

	public void setBloodBagIssueNo(String bloodBagIssueNo) {
		this.bloodBagIssueNo = bloodBagIssueNo;
	}
	
}

