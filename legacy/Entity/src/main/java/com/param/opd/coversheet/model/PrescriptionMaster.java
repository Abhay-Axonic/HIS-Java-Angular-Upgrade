package com.param.opd.coversheet.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
@NamedNativeQuery(name="GET_COUNT_OF_MEDICATIONS_BY_PRESCIPTION_ID",
query="select count(prescription_id) as \"prescriptionCount\" from pharmacy.t_prescription_detail "
		+ " where prescription_id=:prescriptionId "
		+ " and organization_id=:organizationId "
		+ " and status_id !=9")
@Entity
@Table(name="t_prescription",schema="pharmacy")
public class PrescriptionMaster implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer prescriptionId;

	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="doctor_id")
	private Integer doctorId;

	@Column(name="prescription_no")
	private String prescriptionNo;
	
	@Column(name="prescription_date")
	private Date prescriptionDate;
	
	@Column(name="authorised_by")
	private Integer authorisedBy;

	@Column(name="authorised_date")
	private Date authorisedDate;

	@Column(name="is_intervention")
	private Boolean isIntervention;
	  
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Date updatedDate; 

	@Column(name="added_by")
	private Integer addedBy;
	
	@Column(name="added_date")
	private Date addedDate; 
	
	@Column(name="is_deleted")
	private Boolean isDeleted; 

	@Column(name="status_id")
	private Integer statusId;

	@Column(name="priority_id")
	private Integer priorityId;
	
	@Column(name="is_validate")
	private Boolean isValidate;
	
	@Column(name="is_discharge")
	private Boolean isDischarge;
	
	@Column(name="employee_id")
	private Integer employeeId;

	@Column(name="dept_id")
	private Integer deptId;
	
	@Column(name="justification_stop")
	private String justificationStop;
	
	@Column(name="is_medication_stop")
	private Character isMedicationStop;
	
	@Column(name="reconciliation_status_id")
	private Integer reconciliationStatusId;
	
	@Column(name="reconciliation_done")
	private Boolean reconciliationDone;
	
	@Column(name="is_non_formulary")
	private Character isNonFormulary;
	
	@Column(name="is_otims")
	private Boolean isOtims;
	
	@Column(name="medication_type")
	private Integer medicationType;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	public Boolean getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Boolean isDischarge) {
		this.isDischarge =(isDischarge==null)?false:isDischarge;
	}

	
	
	public Character getIsMedicationStop() {
		return isMedicationStop;
	}

	public void setIsMedicationStop(Character isMedicationStop) {
		this.isMedicationStop = isMedicationStop;
	}

	public Character getIsNonFormulary() {
		return isNonFormulary;
	}

	public void setIsNonFormulary(Character isNonFormulary) {
		this.isNonFormulary = isNonFormulary;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	/*public Character getPrescriptionNo() {
		return prescriptionNo;
	}

	public void setPrescriptionNo(Character prescriptionNo) {
		this.prescriptionNo = prescriptionNo;
	}*/
	
	

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Integer getAuthorisedBy() {
		return authorisedBy;
	}

	public void setAuthorisedBy(Integer authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public Date getAuthorisedDate() {
		return authorisedDate;
	}

	public void setAuthorisedDate(Date authorisedDate) {
		this.authorisedDate = authorisedDate;
	}

	public Boolean getIsIntervention() {
		return isIntervention;
	}

	public void setIsIntervention(Boolean isIntervention) {
		this.isIntervention = isIntervention;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Boolean getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(Boolean isValidate) {
		this.isValidate = isValidate;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	/*public Character getIsMedicationStop() {
		return isMedicationStop;
	}

	public void setIsMedicationStop(Character isMedicationStop) {
		this.isMedicationStop = isMedicationStop;
	}*/

	public String getJustificationStop() {
		return justificationStop;
	}

	public void setJustificationStop(String justificationStop) {
		this.justificationStop = justificationStop;
	}

	

	public String getPrescriptionNo() {
		return prescriptionNo;
	}

	public void setPrescriptionNo(String prescriptionNo) {
		this.prescriptionNo = prescriptionNo;
	}

	public Integer getReconciliationStatusId() {
		return reconciliationStatusId;
	}

	public void setReconciliationStatusId(Integer reconciliationStatusId) {
		this.reconciliationStatusId = reconciliationStatusId;
	}

	public Boolean getReconciliationDone() {
		return reconciliationDone;
	}

	public void setReconciliationDone(Boolean reconciliationDone) {
		this.reconciliationDone = reconciliationDone;
	}

	public Boolean getIsOtims() {
		return isOtims;
	}

	public void setIsOtims(Boolean isOtims) {
		this.isOtims = isOtims;
	}

	public Integer getMedicationType() {
		return medicationType;
	}

	public void setMedicationType(Integer medicationType) {
		this.medicationType = medicationType;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	
	

	
	
	
}
