
package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.global.model.SpecialityMaster;

@Entity(name="Prescription")
@Table(name="t_prescription",schema="pharmacy")
public class Prescription extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="admission_id")
	private Admission admission;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="encounter_id")
	private Encounter encounter;

	@Column(name="authorised_by")
	private Integer authorisedBy;

	@Column(name="authorised_date")
	private Timestamp authorisedDate;

	@Column(name="is_non_formulary")
	private char isNonFormulary;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="doctor_id")
	private Doctor doctor;

	@Column(name="is_intervention", nullable=false)
	private Boolean isIntervention;

	@Column(name="is_validate", nullable=false)
	private boolean isValidate;

	@Column(name="prescription_date", nullable=false)
	private Timestamp prescriptionDate;

	@Column(name="prescription_no", nullable=false, length=50)
	private String prescriptionNo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="priority_id")
	private Priority priority;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="status_id", nullable=false)
	private Status status;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="visit_type_id", nullable=false)
	private VisitType visitType;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="prescription",cascade = CascadeType.ALL)
	private List<PrescriptionDetail> prescriptionDetailsList;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="prescription")
	private List<Sale> sales;
	
	@Column(name="prescription_mongo_id", length=100)
	private String prescriptionMongoId; 
	
	@Column(name="is_discharge", nullable=false)
	private Boolean isDischarge;

	@Column(name="cancel_note")
	private String cancelNote;
	
	@Column(name="is_otims")
	private Boolean isOtims;
	
    @Column(name="medication_type")
    private Integer medicationType;

    
	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reconciliation_status_id")
	private Status reconciliationStatus;
	
	@Column(name="reconciliation_done")
	private Boolean reconciliationDone;
	
//	@OneToOne
//	@JoinColumn(name="dept_id")
//	SpecialityMaster specialityMaster;
//	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="dept_id")
	private SpecialityMaster specialityMaster;
	
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "temp_doctor_id")
	private TempDoctor tempDoctor;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "temp_patient_id")
	private TempPatientRegistration tempPatient;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="patient_id")
	private PatientRegistration patient;
	
	
	
	
	public PatientRegistration getPatient() {
		return patient;
	}

	public void setPatient(PatientRegistration patient) {
		this.patient = patient;
	}

	public TempDoctor getTempDoctor() {
		return tempDoctor;
	}

	public void setTempDoctor(TempDoctor tempDoctor) {
		this.tempDoctor = tempDoctor;
	}

	public TempPatientRegistration getTempPatient() {
		return tempPatient;
	}

	public void setTempPatient(TempPatientRegistration tempPatient) {
		this.tempPatient = tempPatient;
	}

	public Prescription() {
		super();
	}
	
	public void updatePrescription(Status status) {
		this.status = status;
	}

	public Prescription(Long id) {
		super();
		this.id = id;
	}
	
	public void updatePrescription(Status reconciliationStatus, Boolean reconciliationDone) {
		this.reconciliationStatus = reconciliationStatus;
		this.reconciliationDone = reconciliationDone;
	}

	public Prescription(Integer authorisedBy, Timestamp authorisedDate,Admission admission, Doctor doctor, Boolean isIntervention, Boolean isValidate,
			Timestamp prescriptionDate, String prescriptionNo, Status status, VisitType visitType,
			String prescriptionMongoId, Priority priority,Boolean isActive, User addedBy,User updatedBy, Boolean isDeleted,
			Date addedDate, Date updatedDate) {
		super();
		this.authorisedBy = authorisedBy;
		this.authorisedDate = authorisedDate;
		this.admission = admission;
		this.doctor = doctor;
		this.isIntervention = isIntervention;
		this.isValidate = isValidate;
		this.prescriptionDate = prescriptionDate;
		this.prescriptionNo = prescriptionNo;
		this.status = status;
		this.visitType = visitType;
		this.prescriptionMongoId = prescriptionMongoId;
		this.priority = priority;
		this.isActive = isActive;
		this.addedBy = addedBy;
		this.updatedBy = updatedBy;
		this.isDeleted = isDeleted;
		this.addedDate = addedDate;
		this.updatedDate = updatedDate;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAuthorisedBy() {
		return this.authorisedBy;
	}

	public void setAuthorisedBy(Integer authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public Timestamp getAuthorisedDate() {
		return this.authorisedDate;
	}

	public void setAuthorisedDate(Timestamp authorisedDate) {
		this.authorisedDate = authorisedDate;
	}

	public Boolean getIsIntervention() {
		return this.isIntervention;
	}

	public void setIsIntervention(Boolean isIntervention) {
		this.isIntervention = isIntervention;
	}

	public Timestamp getPrescriptionDate() {
		return this.prescriptionDate;
	}

	public void setPrescriptionDate(Timestamp prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public String getPrescriptionNo() {
		return this.prescriptionNo;
	}

	public void setPrescriptionNo(String prescriptionNo) {
		this.prescriptionNo = prescriptionNo;
	}

	public List<PrescriptionDetail> getPrescriptionDetailsList() {
		return this.prescriptionDetailsList;
	}
	
	public void setPrescriptionDetailsList(List<PrescriptionDetail> prescriptionDetailsList) {
		this.prescriptionDetailsList = prescriptionDetailsList;
	}

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	public boolean getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}

	public PrescriptionDetail addPrescriptionDetailsList(PrescriptionDetail prescriptionDetailsList) {
		getPrescriptionDetailsList().add(prescriptionDetailsList);
		prescriptionDetailsList.setPrescription(this);

		return prescriptionDetailsList;
	}

	public PrescriptionDetail removePrescriptionDetailsList(PrescriptionDetail prescriptionDetailsList) {
		getPrescriptionDetailsList().remove(prescriptionDetailsList);
		prescriptionDetailsList.setPrescription(null);

		return prescriptionDetailsList;
	}
	
	
	public PrescriptionDetail addPrescriptionDetails(PrescriptionDetail prescriptionDetails) {
		if (getPrescriptionDetailsList() == null)
			this.prescriptionDetailsList = new ArrayList<PrescriptionDetail>();
		
		getPrescriptionDetailsList().add(prescriptionDetails);
		prescriptionDetails.setPrescription(this);

		return prescriptionDetails;
	}

	public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}

	public String getPrescriptionMongoId() {
		return prescriptionMongoId;
	}

	public void setPrescriptionMongoId(String prescriptionMongoId) {
		this.prescriptionMongoId = prescriptionMongoId;
	}

	public void updatePrescription(List<PrescriptionDetail> prescriptionDetailsList) {
		this.prescriptionDetailsList = prescriptionDetailsList;
	}

	public Boolean getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Boolean isDischarge) {
		this.isDischarge = isDischarge;
	}

	public Status getReconciliationStatus() {
		return reconciliationStatus;
	}

	public void setReconciliationStatus(Status reconciliationStatus) {
		this.reconciliationStatus = reconciliationStatus;
	}

	public Boolean getReconciliationDone() {
		return reconciliationDone;
	}

	public void setReconciliationDone(Boolean reconciliationDone) {
		this.reconciliationDone = reconciliationDone;
	}
	
	public PrescriptionDetail addMedicationReconsiliationDetailList(PrescriptionDetail prescriptionDetail) {
		if (getPrescriptionDetailsList() == null)
			this.prescriptionDetailsList = new ArrayList<PrescriptionDetail>();
		
		getPrescriptionDetailsList().add(prescriptionDetail);
		prescriptionDetail.setPrescription(this);

		return prescriptionDetail;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public char getIsNonFormulary() {
		return isNonFormulary;
	}

	public void setIsNonFormulary(char isNonFormulary) {
		this.isNonFormulary = isNonFormulary;
	}

	public void updatePrescriptionStatus(Status status) {
		this.status = status;
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

	

	
}