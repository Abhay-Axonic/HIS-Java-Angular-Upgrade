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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.PatientRegistration;
@NamedQueries({

	@NamedQuery(name="GET_BLOOD_BAG_ISSUE_ACCEPTANCE_LIST",
	query="SELECT  issueAcceptance.bloodBagIssueAcceptanceId as bloodBagIssueAcceptanceId, "
				+ " issueAcceptance.issueNo AS issueNo, "
				+ " issueAcceptance.requisitionNumber AS requisitionNumber, "
				+ " issueAcceptance.acceptedBy AS acceptedBy, "
				+ " issueAcceptance.bloodBagNo AS bloodBagNo, "
				+ " to_char(issueAcceptance.issueDate,'DD/MM/YYYY') as issueDate, "
				+ " issueAcceptance.uhid AS uhid, "
				+ " issueAcceptance.patientId AS patientId, "
				+ "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " issueAcceptance.bloodBagComponenId AS bloodBagComponenId, "
				+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc, "
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupName, "
				+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) AS patientName, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacityDesc, "
				+ " issueAcceptance.acceptedBy AS acceptedBy "
				+ " FROM BloodBagIssueAcceptance issueAcceptance "
				+ " inner JOIN  issueAcceptance.patientRegistration patient "
				+ " INNER JOIN  issueAcceptance.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " inner JOIN  issueAcceptance.bloodComponentMaster  bloodComponentMaster "
				+ " inner JOIN  issueAcceptance.bloodGroupMaster bloodGroupMaster "
				+ " INNER JOIN  issueAcceptance.userMaster userMaster "
				+ " LEFT  JOIN  userMaster.employeeMastersList employee "
				+ " LEFT JOIN employee.prefixMaster prefix "
				+ " WHERE issueAcceptance.organizationId= :organizationId "
				+ " AND issueAcceptance.unitId= :unitId "
				+ " AND to_char(issueAcceptance.issueDate,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "
				+ " AND issueAcceptance.bloodBagIssueAcceptanceStatusId = 1 ")
	
})
@Entity
@Table(name="t_blood_bag_issue_acceptance", schema="blood_bank")
@SequenceGenerator(name="blood_bag_issue_acceptance_seq", sequenceName="blood_bank.blood_bag_issue_acceptance_seq", allocationSize= 1)

public class BloodBagIssueAcceptance {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_bag_issue_acceptance_seq")
	@Column(name = "blood_bag_issue_acceptance_id")
	private Integer bloodBagIssueAcceptanceId;
	
	@Column(name = "issue_no")
	private String issueNo;
	
	@Column(name = "uhid")
	private String uhid;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "blood_bag_component_id")
	private Integer bloodBagComponenId;
	
	@Column(name = "blood_bag_no")
	private String bloodBagNo;
	
	@Column(name = "requisition_no")
	private String requisitionNumber;
	
	@Column(name = "blood_bag_type_id")
	private Integer bloodBagTypeId;
	
	@Column(name = "blood_bag_capacity_id")
	private Integer bloodBagCapacityId;
	
	@Column(name = "issue_date")
	private Date issueDate;
	
	@Column(name = "blood_bag_reason_id")
	private Integer bloodBagReasonId;
	
	@Column(name = "blood_bag_issue_acceptance_status_id")
	private Integer bloodBagIssueAcceptanceStatusId;
	
	@Column(name = "blood_bag_reason_note")
	private String bloodBagReasonNote;
	
	@Column(name = "accepted_by")
	private Integer acceptedBy;
	
	@Column(name = "is_exported")
	private boolean isExported;
	
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
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="blood_bag_component_id", insertable=false, updatable=false)
	private BloodComponentMaster bloodComponentMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bag_capacity_id", insertable = false, updatable = false)
	private BloodBagCapacityMaster bloodBagCapacityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accepted_by", insertable = false, updatable = false)
	private UserMaster userMaster;
	
	public Integer getBloodBagIssueAcceptanceId() {
		return bloodBagIssueAcceptanceId;
	}

	public void setBloodBagIssueAcceptanceId(Integer bloodBagIssueAcceptanceId) {
		this.bloodBagIssueAcceptanceId = bloodBagIssueAcceptanceId;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getBloodBagComponenId() {
		return bloodBagComponenId;
	}

	public void setBloodBagComponenId(Integer bloodBagComponenId) {
		this.bloodBagComponenId = bloodBagComponenId;
	}

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public Integer getBloodBagTypeId() {
		return bloodBagTypeId;
	}

	public void setBloodBagTypeId(Integer bloodBagTypeId) {
		this.bloodBagTypeId = bloodBagTypeId;
	}

	public Integer getBloodBagCapacityId() {
		return bloodBagCapacityId;
	}

	public void setBloodBagCapacityId(Integer bloodBagCapacityId) {
		this.bloodBagCapacityId = bloodBagCapacityId;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getBloodBagReasonId() {
		return bloodBagReasonId;
	}

	public void setBloodBagReasonId(Integer bloodBagReasonId) {
		this.bloodBagReasonId = bloodBagReasonId;
	}

	public Integer getBloodBagIssueAcceptanceStatusId() {
		return bloodBagIssueAcceptanceStatusId;
	}

	public void setBloodBagIssueAcceptanceStatusId(Integer bloodBagIssueAcceptanceStatusId) {
		this.bloodBagIssueAcceptanceStatusId = bloodBagIssueAcceptanceStatusId;
	}

	public String getBloodBagReasonNote() {
		return bloodBagReasonNote;
	}

	public void setBloodBagReasonNote(String bloodBagReasonNote) {
		this.bloodBagReasonNote = bloodBagReasonNote;
	}

	public Integer getAcceptedBy() {
		return acceptedBy;
	}

	public void setAcceptedBy(Integer acceptedBy) {
		this.acceptedBy = acceptedBy;
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

	public boolean isExported() {
		return isExported;
	}

	public void setExported(boolean isExported) {
		this.isExported = isExported;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public String getRequisitionNumber() {
		return requisitionNumber;
	}

	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}
	
	
}
