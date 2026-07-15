package com.param.bloodBank.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.PatientRegistration;

@NamedQueries({
	
	@NamedQuery(name="SEARCH_CROSS_MATCH_REGISTERED_DETAILS_ON_ISSUE",
	query="SELECT distinct mstr.crossMatchRegisterId as crossMatchRegisterId, "
				//+ "mstr.requisitionNo  as  requisitionNo, "
				+ "bloodComponentOrderMaster.requisitionNumber  as  requisitionNumber, "
				+ "patientRequisitionDetails.patientRequisitionId as patientRequisitionId, "
				+ "mstr.patientBloodGroup  as  patientBloodGroup, "
				+ "mstr.componentId  as componentId, "
				+ "mstr.patientId  as  patientId, "
				+ "mstr.unitId  as  unitId, "
				+ "(mstr.requiredQty - mstr.pendingQty) as qty, "
				+ "mstr.requiredQty as requiredQty, "
				+ "mstr.pendingQty as pendingQty, "
				+ "mstr.organizationId  as  organizationId, "
				+ "mstr.btoId  as  btoId, "
				+ "mstr.technicianId  as  technicianId, "
				+ "mstr.createdBy  as createdBy, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " to_char(mstr.issueDate,'DD-MM-YYYY') as issueDate, "
				+ " to_char(mstr.issueDate,'YYYY-MM-DD') as compairIssueDate, "
				+ " to_char(mstr.requisitionDate,'DD-MM-YYYY') as requisitionDate, "
				+ " patient.uhidNumber AS uhidNumber, "
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupName, "
				+ " visitTypeMaster.visitTypeName AS visitTypeName, "
				+ " patientRequisitionDetails.requisitionTypeId as requisitionTypeId, "
				+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc, "
				+ "concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as userName, "
				+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) AS patientName "
				+ " FROM CrossMatchRegister mstr "
				+ " INNER JOIN mstr.crossMatchRegisterDetails  dtls "
				+ " INNER JOIN mstr.patientRequisitionDetails  patientRequisitionDetails "
				+ " INNER JOIN mstr.userMaster  userMaster "
				+ " INNER JOIN userMaster.employeeMastersList employee "
				+ " inner JOIN  patientRequisitionDetails.patientRegistration patient "
				+ " inner JOIN  patientRequisitionDetails.admission admission "
				+ " inner JOIN  admission.visitTypeMaster visitTypeMaster  "
				+ " inner JOIN  patientRequisitionDetails.bloodComponentMaster  bloodComponentMaster "
				+ " inner JOIN  mstr.bloodComponentOrderMaster  bloodComponentOrderMaster "
				+ " inner JOIN  patientRequisitionDetails.bloodGroupMaster bloodGroupMaster "
				+ " left JOIN patientRequisitionDetails.bloodBankStatusMaster  bloodBankStatusMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND dtls.isExported= false "
				+ " AND dtls.crossMatchRegisterDetailsStatus= 1 ")
	
})

@Entity
@Table(name="t_cross_match_register", schema="blood_bank")
@SequenceGenerator(name="cross_match_register_seq", sequenceName="blood_bank.cross_match_register_seq", allocationSize= 1)
public class CrossMatchRegister {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cross_match_register_seq")
	@Column(name = "cross_match_register_id")
	private Integer crossMatchRegisterId;
	
	@Column(name = "patient_requisition_id")
	private Integer patientRequisitionId;
	
	@Column(name = "blood_component_order_id")
	private Integer bloodComponentOrderId;
	
	@Column(name = "requisition_date")
	private Date requisitionDate;
	
	@Column(name = "component_id")
	private Integer componentId;
	
	@Column(name = "required_blood_group")
	private Integer requiredBloodGroup;
	
	@Column(name = "patient_blood_group")
	private Integer patientBloodGroup;
	
	@Column(name = "required_qty")
	private Integer requiredQty;
	
	@Column(name = "pending_qty")
	private Integer pendingQty;
	
	@Column(name = "bto_id")
	private Integer btoId;
	
	@Column(name = "technician_id")
	private Integer technicianId;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "cross_match_status")
	private Integer crossMatchStatus;
	
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
	
	@Column(name = "issue_date")
	private Date issueDate;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="component_id", insertable=false, updatable=false)
	private BloodComponentMaster bloodComponentMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_blood_group", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "required_blood_group", insertable = false, updatable = false)
	private BloodGroupMaster requiredBloodGroupMstr;*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_requisition_id", insertable = false, updatable = false)
	private PatientRequisitionDetails patientRequisitionDetails ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bto_id", insertable = false, updatable = false)
	private UserMaster userMaster ;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "crossMatchRegister", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CrossMatchRegisterDetails> crossMatchRegisterDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_component_order_id", insertable = false, updatable = false)
	private BloodComponentOrderMaster bloodComponentOrderMaster;

	public Integer getCrossMatchRegisterId() {
		return crossMatchRegisterId;
	}

	public void setCrossMatchRegisterId(Integer crossMatchRegisterId) {
		this.crossMatchRegisterId = crossMatchRegisterId;
	}

	public Integer getPatientRequisitionId() {
		return patientRequisitionId;
	}

	public void setPatientRequisitionId(Integer patientRequisitionId) {
		this.patientRequisitionId = patientRequisitionId;
	}

	public Date getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(Date requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Integer getRequiredBloodGroup() {
		return requiredBloodGroup;
	}

	public void setRequiredBloodGroup(Integer requiredBloodGroup) {
		this.requiredBloodGroup = requiredBloodGroup;
	}

	public Integer getPatientBloodGroup() {
		return patientBloodGroup;
	}

	public void setPatientBloodGroup(Integer patientBloodGroup) {
		this.patientBloodGroup = patientBloodGroup;
	}

	public Integer getRequiredQty() {
		return requiredQty;
	}

	public void setRequiredQty(Integer requiredQty) {
		this.requiredQty = requiredQty;
	}

	public Integer getPendingQty() {
		return pendingQty;
	}

	public void setPendingQty(Integer pendingQty) {
		this.pendingQty = pendingQty;
	}

	public Integer getBtoId() {
		return btoId;
	}

	public void setBtoId(Integer btoId) {
		this.btoId = btoId;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCrossMatchStatus() {
		return crossMatchStatus;
	}

	public void setCrossMatchStatus(Integer crossMatchStatus) {
		this.crossMatchStatus = crossMatchStatus;
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

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getBloodComponentOrderId() {
		return bloodComponentOrderId;
	}

	public void setBloodComponentOrderId(Integer bloodComponentOrderId) {
		this.bloodComponentOrderId = bloodComponentOrderId;
	}

	public BloodComponentMaster getBloodComponentMaster() {
		return bloodComponentMaster;
	}

	public void setBloodComponentMaster(BloodComponentMaster bloodComponentMaster) {
		this.bloodComponentMaster = bloodComponentMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public BloodGroupMaster getBloodGroupMaster() {
		return bloodGroupMaster;
	}

	public void setBloodGroupMaster(BloodGroupMaster bloodGroupMaster) {
		this.bloodGroupMaster = bloodGroupMaster;
	}

	public PatientRequisitionDetails getPatientRequisitionDetails() {
		return patientRequisitionDetails;
	}

	public void setPatientRequisitionDetails(PatientRequisitionDetails patientRequisitionDetails) {
		this.patientRequisitionDetails = patientRequisitionDetails;
	}

	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}

	public List<CrossMatchRegisterDetails> getCrossMatchRegisterDetails() {
		return crossMatchRegisterDetails;
	}

	public void setCrossMatchRegisterDetails(List<CrossMatchRegisterDetails> crossMatchRegisterDetails) {
		this.crossMatchRegisterDetails = crossMatchRegisterDetails;
	}

	
	
}
