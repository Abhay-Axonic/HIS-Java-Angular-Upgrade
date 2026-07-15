package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_drug_db", schema = "pharman")
@SequenceGenerator(name = "patient_registration_seq", sequenceName = "patient.patient_registration_seq", allocationSize = 1)
public class PharmanDrugModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_registration_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "indicationsanddoses")
	private String indicationsanddoses;

	@Column(name = "cautions")
	private String cautions;

	@Column(name = "interactions")
	private String interactions;

	@Column(name = "sideeffects")
	private Character sideeffects;

	@Column(name = "allergyandcrosssensitivity")
	private String allergyandcrosssensitivity;

	@Column(name = "pregnancy")
	private String pregnancy;

	@Column(name = "hepaticimpairment")
	private String hepaticimpairment;
	
	@Column(name = "renalimpairment")
	private String renalimpairment;
	
	@Column(name = "pretreatmentscreeninginformation")
	private String pretreatmentscreeninginformation;

	@Column(name = "patientandcareradvice")
	private String patientandcareradvice;

	@Column(name = "medicinalforms")
	private String medicinalforms;

	@Column(name = "medicine")
	private String medicine;

	@Column(name = "contraindications")
	private String contraindications;

	@Column(name = "conceptionandcontraception")
	private String conceptionandcontraception;

	@Column(name = "breastfeeding")
	private String breastfeeding;

	@Column(name = "nationalfunding")
	private String nationalfunding;

	@Column(name = "directionsforadministration")
	private String directionsforadministration;

	@Column(name = "drugaction")
	private String drugaction;

	@Column(name = "importantsafetyinformations")
	private String importantsafetyinformations;

	@Column(name = "treatmentcessationinformation")
	private String treatmentcessationinformation;
	
	@Column(name = "unlicenseduse")
	private String unlicenseduse;
	
	@Column(name = "professionspecificinformation")
	private String professionspecificinformation;
	
	@Column(name = "exceptionstolegalcategory")
	private String exceptionstolegalcategory;
	
	@Column(name = "handlingandstorages")
	private String handlingandstorages;
	
	@Column(name = "lesssuitableforprescribings")
	private String lesssuitableforprescribings;
	
	@Column(name = "effectsonlaboratorytests")
	private String effectsonlaboratorytests;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIndicationsanddoses() {
		return indicationsanddoses;
	}

	public void setIndicationsanddoses(String indicationsanddoses) {
		this.indicationsanddoses = indicationsanddoses;
	}

	public String getCautions() {
		return cautions;
	}

	public void setCautions(String cautions) {
		this.cautions = cautions;
	}

	public String getInteractions() {
		return interactions;
	}

	public void setInteractions(String interactions) {
		this.interactions = interactions;
	}

	public Character getSideeffects() {
		return sideeffects;
	}

	public void setSideeffects(Character sideeffects) {
		this.sideeffects = sideeffects;
	}

	public String getAllergyandcrosssensitivity() {
		return allergyandcrosssensitivity;
	}

	public void setAllergyandcrosssensitivity(String allergyandcrosssensitivity) {
		this.allergyandcrosssensitivity = allergyandcrosssensitivity;
	}

	public String getPregnancy() {
		return pregnancy;
	}

	public void setPregnancy(String pregnancy) {
		this.pregnancy = pregnancy;
	}

	public String getHepaticimpairment() {
		return hepaticimpairment;
	}

	public void setHepaticimpairment(String hepaticimpairment) {
		this.hepaticimpairment = hepaticimpairment;
	}

	public String getRenalimpairment() {
		return renalimpairment;
	}

	public void setRenalimpairment(String renalimpairment) {
		this.renalimpairment = renalimpairment;
	}

	public String getPretreatmentscreeninginformation() {
		return pretreatmentscreeninginformation;
	}

	public void setPretreatmentscreeninginformation(String pretreatmentscreeninginformation) {
		this.pretreatmentscreeninginformation = pretreatmentscreeninginformation;
	}

	public String getPatientandcareradvice() {
		return patientandcareradvice;
	}

	public void setPatientandcareradvice(String patientandcareradvice) {
		this.patientandcareradvice = patientandcareradvice;
	}

	public String getMedicinalforms() {
		return medicinalforms;
	}

	public void setMedicinalforms(String medicinalforms) {
		this.medicinalforms = medicinalforms;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	public String getContraindications() {
		return contraindications;
	}

	public void setContraindications(String contraindications) {
		this.contraindications = contraindications;
	}

	public String getConceptionandcontraception() {
		return conceptionandcontraception;
	}

	public void setConceptionandcontraception(String conceptionandcontraception) {
		this.conceptionandcontraception = conceptionandcontraception;
	}

	public String getBreastfeeding() {
		return breastfeeding;
	}

	public void setBreastfeeding(String breastfeeding) {
		this.breastfeeding = breastfeeding;
	}

	public String getNationalfunding() {
		return nationalfunding;
	}

	public void setNationalfunding(String nationalfunding) {
		this.nationalfunding = nationalfunding;
	}

	public String getDirectionsforadministration() {
		return directionsforadministration;
	}

	public void setDirectionsforadministration(String directionsforadministration) {
		this.directionsforadministration = directionsforadministration;
	}

	public String getDrugaction() {
		return drugaction;
	}

	public void setDrugaction(String drugaction) {
		this.drugaction = drugaction;
	}

	public String getImportantsafetyinformations() {
		return importantsafetyinformations;
	}

	public void setImportantsafetyinformations(String importantsafetyinformations) {
		this.importantsafetyinformations = importantsafetyinformations;
	}

	public String getTreatmentcessationinformation() {
		return treatmentcessationinformation;
	}

	public void setTreatmentcessationinformation(String treatmentcessationinformation) {
		this.treatmentcessationinformation = treatmentcessationinformation;
	}

	public String getUnlicenseduse() {
		return unlicenseduse;
	}

	public void setUnlicenseduse(String unlicenseduse) {
		this.unlicenseduse = unlicenseduse;
	}

	public String getProfessionspecificinformation() {
		return professionspecificinformation;
	}

	public void setProfessionspecificinformation(String professionspecificinformation) {
		this.professionspecificinformation = professionspecificinformation;
	}

	public String getExceptionstolegalcategory() {
		return exceptionstolegalcategory;
	}

	public void setExceptionstolegalcategory(String exceptionstolegalcategory) {
		this.exceptionstolegalcategory = exceptionstolegalcategory;
	}

	public String getHandlingandstorages() {
		return handlingandstorages;
	}

	public void setHandlingandstorages(String handlingandstorages) {
		this.handlingandstorages = handlingandstorages;
	}

	public String getLesssuitableforprescribings() {
		return lesssuitableforprescribings;
	}

	public void setLesssuitableforprescribings(String lesssuitableforprescribings) {
		this.lesssuitableforprescribings = lesssuitableforprescribings;
	}

	public String getEffectsonlaboratorytests() {
		return effectsonlaboratorytests;
	}

	public void setEffectsonlaboratorytests(String effectsonlaboratorytests) {
		this.effectsonlaboratorytests = effectsonlaboratorytests;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
	
}
