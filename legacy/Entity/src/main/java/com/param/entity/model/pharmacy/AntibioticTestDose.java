package com.param.entity.model.pharmacy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_antibiotic_test_dose", schema="pharmacy")
@SequenceGenerator(name = "antibiotic_test_dose_seq", sequenceName = " pharmacy.antibiotic_test_dose_seq", allocationSize = 1)

public class AntibioticTestDose {

	@Id
	@Column(name="antibiotic_test_dose_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "antibiotic_test_dose_seq")
	private Integer antibioticTestDoseId;

	@Column(name="sale_details_id")
	private Integer saleDetailsId;
	
	@Column(name="roa")
	private Integer roa;
	
	@Column(name="admin_date")
	private Date adminDate;
	
	@Column(name="admin_time")
	private String adminTime;
	
	@Column(name="observation_date")
	private Date observationDate;
	
	@Column(name="observation_time")
	private String observationTime;
	
	@Column(name="un_eventful")
	private Boolean unEventful;
	
	@Column(name="redness")
	private Boolean redness;
	
	@Column(name="swelling")
	private Boolean swelling;
	
	@Column(name="other")
	private Boolean other;
	
	@Column(name="other_effect")
	private String otherEffect;
	
	@Column(name="stop_administration")
	private Boolean stopAdministration;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="test_dose")
	private String testDose; 
	
	@Column(name="allergy_id")
	private Integer allergyId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="prescription_detail_id")
	private Integer prescriptionDetailId;

	public Integer getAntibioticTestDoseId() {	
		return antibioticTestDoseId;
	}

	public void setAntibioticTestDoseId(Integer antibioticTestDoseId) {
		this.antibioticTestDoseId = antibioticTestDoseId;
	}

	public Integer getSaleDetailsId() {
		return saleDetailsId;
	}

	public void setSaleDetailsId(Integer saleDetailsId) {
		this.saleDetailsId = saleDetailsId;
	}

	public Integer getRoa() {
		return roa;
	}

	public void setRoa(Integer roa) {
		this.roa = roa;
	}

	public Date getAdminDate() {
		return adminDate;
	}

	public void setAdminDate(Date adminDate) {
		this.adminDate = adminDate;
	}

	public String getAdminTime() {
		return adminTime;
	}

	public void setAdminTime(String adminTime) {
		this.adminTime = adminTime;
	}

	public Date getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}

	public String getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}

	public Boolean getUnEventful() {
		return unEventful;
	}

	public void setUnEventful(Boolean unEventful) {
		this.unEventful = unEventful;
	}

	public String getOtherEffect() {
		return otherEffect;
	}

	public void setOtherEffect(String otherEffect) {
		this.otherEffect = otherEffect;
	}

	public Boolean getStopAdministration() {
		return stopAdministration;
	}

	public void setStopAdministration(Boolean stopAdministration) {
		this.stopAdministration = stopAdministration;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getTestDose() {
		return testDose;
	}

	public void setTestDose(String testDose) {
		this.testDose = testDose;
	}

	public Boolean getRedness() {
		return redness;
	}

	public void setRedness(Boolean redness) {
		this.redness = redness;
	}

	public Boolean getSwelling() {
		return swelling;
	}

	public void setSwelling(Boolean swelling) {
		this.swelling = swelling;
	}

	public Boolean getOther() {
		return other;
	}

	public void setOther(Boolean other) {
		this.other = other;
	}

	public Integer getAllergyId() {
		return allergyId;
	}

	public void setAllergyId(Integer allergyId) {
		this.allergyId = allergyId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Integer prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}
	
	
}
