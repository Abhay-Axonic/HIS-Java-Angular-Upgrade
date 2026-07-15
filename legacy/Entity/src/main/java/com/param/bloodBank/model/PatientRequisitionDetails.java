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
import com.param.adt.admission.model.Admission;
import com.param.global.model.PatientRegistration;

@NamedQueries({

	@NamedQuery(name="SEARCH_PATIENTREQUISITION_DETAILS",
	query="SELECT  prd.patientRequisitionId as patientRequisitionId,"
				+ "prd.bloodComponentOrderId  as  bloodComponentOrderId, "
				+ "bloodComponentOrderMaster.requisitionNumber  as  requisitionNumber, "
				+ "prd.requisitionTypeId  as  requisitionTypeId, "
				+ "prd.requiredBloodGroupId  as  requiredBloodGroupId, "
				+ "prd.patientBloodGroupId  as  patientBloodGroupId, "
				+ "prd.componentId  as componentId, "
				+ "prd.bloodUnit  as bloodUnit, "
				+ "prd.patientId  as  patientId, "
				+ "prd.unitId  as  unitId, "
				+ "prd.organizationId  as  organizationId, "
				+ "prd.createdBy  as createdBy, "
				+ " to_char(prd.createdDate,'DD/MM/YYYY') as createdDate, "
				+ " to_char(prd.issueDate,'DD/MM/YYYY') as issueDate, "
				+ " to_char(prd.requisitionDate,'DD/MM/YYYY') as requisitionDate, "
				+ " patient.uhidNumber AS uhidNumber, "
				+ " to_char(patient.dob,'DD/MM/YYYY') AS dob, "
				+ " bloodGroupMaster.bloodGroupDesc AS bloodGroupName, "
				+ " requiredBloodGroupMstr.bloodGroupDesc AS requiredBloodGroup, "
				+ " visitTypeMaster.visitTypeName AS visitTypeName, "
				+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc, "
				+ " bloodComponentMaster.bloodComponentId AS bloodComponentId, "
				+ " bloodComponentMaster.byCrossChecking AS byCrossChecking, "
				+ " bloodBankStatusMaster.name as requisitionType," 
				+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) AS patientName "
				+ " FROM PatientRequisitionDetails prd "
				+ " inner JOIN  prd.patientRegistration patient "
				+ " inner JOIN  prd.bloodComponentMaster  bloodComponentMaster "
				+ " inner JOIN  prd.bloodComponentOrderMaster  bloodComponentOrderMaster "
				+ " inner JOIN  prd.admission admission "
				+ " inner JOIN  admission.visitTypeMaster visitTypeMaster  "
				+ " inner JOIN  prd.bloodGroupMaster bloodGroupMaster "
				+ " inner JOIN  prd.requiredBloodGroupMstr requiredBloodGroupMstr "
				+ " LEFT JOIN prd.bloodBankStatusMaster  bloodBankStatusMaster "
				+ " WHERE prd.organizationId= :organizationId "
				+ " AND prd.unitId= :unitId "
				//+ " AND bloodComponentMaster.byCrossChecking = 'A' " //from component mstr cross match required field
				+ " AND prd.exportedToCrossMatch = false ")
	
})

@Entity
@Table(name = "t_patient_requisition_details", schema = "blood_bank")
@SequenceGenerator(name = "patient_requisition_details_seq", sequenceName = "blood_bank.patient_requisition_details_seq", allocationSize = 1)
public class PatientRequisitionDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_requisition_details_seq")
	
	@Column(name = "patient_requisition_id")
	private Integer patientRequisitionId;
	
	@Column(name = "blood_component_order_id")
	private Integer bloodComponentOrderId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "requisition_type_id")
	private Integer requisitionTypeId;
	
	@Column(name = "hospital_id")
	private Integer hospitalId;
	
	@Column(name = "required_blood_group_id")
	private Integer requiredBloodGroupId;
	
	@Column(name = "diagnosis")
	private String diagnosis;
	
	@Column(name = "patient_blood_group_id")
	private Integer patientBloodGroupId;
	
	@Column(name = "issue_date")
	private Date issueDate;
	
	@Column(name = "requisition_date")
	private Date requisitionDate;
	
	@Column(name = "component_id")
	private Integer componentId;

	@Column(name = "blood_unit")
	private Integer bloodUnit;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "exported_to_cross_match")
	private boolean exportedToCrossMatch;
	
	@Column(name = "exported_to_reservation")
	private boolean exportedToReservation;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="component_id", insertable=false, updatable=false)
	private BloodComponentMaster bloodComponentMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="admission_id", insertable=false, updatable=false)
	private Admission admission;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "required_blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster requiredBloodGroupMstr;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requisition_type_id", insertable = false, updatable = false)
	private BloodBankStatusMaster bloodBankStatusMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_component_order_id", insertable = false, updatable = false)
	private BloodComponentOrderMaster bloodComponentOrderMaster;
	
	public Integer getPatientRequisitionId() {
		return patientRequisitionId;
	}

	public void setPatientRequisitionId(Integer patientRequisitionId) {
		this.patientRequisitionId = patientRequisitionId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getRequisitionTypeId() {
		return requisitionTypeId;
	}

	public void setRequisitionTypeId(Integer requisitionTypeId) {
		this.requisitionTypeId = requisitionTypeId;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getRequiredBloodGroupId() {
		return requiredBloodGroupId;
	}

	public void setRequiredBloodGroupId(Integer requiredBloodGroupId) {
		this.requiredBloodGroupId = requiredBloodGroupId;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Integer getPatientBloodGroupId() {
		return patientBloodGroupId;
	}

	public void setPatientBloodGroupId(Integer patientBloodGroupId) {
		this.patientBloodGroupId = patientBloodGroupId;
	}
	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Integer getBloodUnit() {
		return bloodUnit;
	}

	public void setBloodUnit(Integer bloodUnit) {
		this.bloodUnit = bloodUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isExportedToCrossMatch() {
		return exportedToCrossMatch;
	}

	public void setExportedToCrossMatch(boolean exportedToCrossMatch) {
		this.exportedToCrossMatch = exportedToCrossMatch;
	}

	public boolean isExportedToReservation() {
		return exportedToReservation;
	}

	public void setExportedToReservation(boolean exportedToReservation) {
		this.exportedToReservation = exportedToReservation;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
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

	public Date getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(Date requisitionDate) {
		this.requisitionDate = requisitionDate;
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

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public BloodGroupMaster getBloodGroupMaster() {
		return bloodGroupMaster;
	}

	public void setBloodGroupMaster(BloodGroupMaster bloodGroupMaster) {
		this.bloodGroupMaster = bloodGroupMaster;
	}

	public BloodGroupMaster getRequiredBloodGroupMstr() {
		return requiredBloodGroupMstr;
	}

	public void setRequiredBloodGroupMstr(BloodGroupMaster requiredBloodGroupMstr) {
		this.requiredBloodGroupMstr = requiredBloodGroupMstr;
	}

	public Integer getBloodComponentOrderId() {
		return bloodComponentOrderId;
	}

	public void setBloodComponentOrderId(Integer bloodComponentOrderId) {
		this.bloodComponentOrderId = bloodComponentOrderId;
	}

	public BloodBankStatusMaster getBloodBankStatusMaster() {
		return bloodBankStatusMaster;
	}

	public void setBloodBankStatusMaster(BloodBankStatusMaster bloodBankStatusMaster) {
		this.bloodBankStatusMaster = bloodBankStatusMaster;
	}

	
}
