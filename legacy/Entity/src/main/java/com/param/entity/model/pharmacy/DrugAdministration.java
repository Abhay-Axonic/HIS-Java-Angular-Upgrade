package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
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
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;

@Entity(name="DrugAdministration")
@Table(name="t_drug_administration",schema="pharmacy")
public class DrugAdministration extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="admission_id")
	private Admission admission;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="encounter_id")
	private Encounter encounter;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="visit_type_id")
	private VisitType visitType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="patient_id")
	private PatientRegistration patient;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="prescription_id")
	private Prescription prescription;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="drugAdministration",cascade = CascadeType.ALL)
	private List<DrugAdministrationDetail> drugAdministrationDetail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}

	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	public PatientRegistration getPatient() {
		return patient;
	}

	public void setPatient(PatientRegistration patient) {
		this.patient = patient;
	}

	public List<DrugAdministrationDetail> getDrugAdministrationDetail() {
		return drugAdministrationDetail;
	}

	public void setDrugAdministrationDetail(List<DrugAdministrationDetail> drugAdministrationDetail) {
		this.drugAdministrationDetail = drugAdministrationDetail;
	}
	
	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public DrugAdministrationDetail addDrugAdministrationDetail(DrugAdministrationDetail drugAdministrationDetail) {
		if (getDrugAdministrationDetail() == null)
			this.drugAdministrationDetail = new ArrayList<DrugAdministrationDetail>();
		
		getDrugAdministrationDetail().add(drugAdministrationDetail);
		drugAdministrationDetail.setDrugAdministration(this);

		return drugAdministrationDetail;
	}

	public DrugAdministration() {
		super();
	}

	public DrugAdministration(VisitType visitType, PatientRegistration patient, Prescription prescription) {
		super();
		this.visitType = visitType;
		this.patient = patient;
		this.prescription = prescription;
	}
	
}