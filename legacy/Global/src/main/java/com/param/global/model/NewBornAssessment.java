package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
		@NamedQuery(name = "GET_NEW_BORN_ASSESSMENT_DATA", query = "SELECT assessment.assessmentId as assessmentId, assessment.breastFeeding as breastFeeding, "
				+ "assessment.transferOfBabyTo as transferOfBabyTo, assessment.cordClamping as cordClamping, assessment.remarks as remarks, "
				+ "assessment.bloodPressureSystolic as bloodPressureSystolic, assessment.pulseRate as pulseRate, "
				+ "assessment.weight as weight, assessment.height as height, assessment.bloodPressureDiastolic as bloodPressureDiastolic, "
				+ "assessment.headCircumference as headCircumference, assessment.temperature as temperature, assessment.respiration as respiration, "
				+ "assessment.status as status FROM NewBornAssessment as assessment "
				+ "WHERE assessment.uhidNumberBaby=:uhidNumberBaby  AND assessment.status='A' "
				+ "AND assessment.organizationId=:organizationId AND assessment.unitId=:unitId "),
		
/*		@NamedQuery(name = "GET_SINGLE_NEW_BORN_ASSESSMENT_DATA", query = "SELECT assessment.assessmentId as assessmentId, assessment.breastFeeding as breastFeeding, "
				+ "assessment.uhidNumberBaby as uhidNumberBaby, assessment.transferOfBabyTo as transferOfBabyTo, assessment.cordClamping as cordClamping, "
				+ "assessment.bloodPressureSystolic as bloodPressureSystolic, assessment.pulseRate as pulseRate, assessment.remarks as remarks, "
				+ "assessment.weight as weight, assessment.bloodPressureDiastolic as bloodPressureDiastolic, "
				+ "assessment.headCircumference as headCircumference, assessment.temperature as temperature, assessment.respiration as respiration, "
				+ "assessment.status as status FROM NewBornAssessment as assessment "
				+ "WHERE assessment.uhidNumberBaby=:uhidNumberBaby AND assessment.status='A' "
				+ "AND assessment.organizationId=:organizationId AND assessment.unitId=:unitId "
				+ "ORDER BY assessment.assessmentId DESC "),*/
		
		
})

@Entity
@Table(name = "m_new_born_assessment", schema = "adt")
@SequenceGenerator(name = "m_new_born_assessment_seq", sequenceName = "adt.m_new_born_assessment_seq", allocationSize = 1)
public class NewBornAssessment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_new_born_assessment_seq")
	@Column(name = "m_new_born_assessment_id")
	private int assessmentId;
	
	@Column(name = "uhid_number_baby")
	private String uhidNumberBaby;
	
	@Column(name = "breast_feeding")
	private String breastFeeding;
	
	@Column(name = "transfer_of_baby_to")
	private String transferOfBabyTo;
	
	@Column(name = "cord_clamping")
	private String cordClamping;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "blood_pressure_systolic")
	private String bloodPressureSystolic;
	
	@Column(name = "pulse_rate")
	private String pulseRate;
	
	@Column(name = "weight")
	private String weight;
	
	@Column(name = "height_in_cm")
	private String height;
	
	@Column(name = "head_circumference")
	private String headCircumference;
	
	@Column(name = "temperature")
	private String temperature;
	
	@Column(name = "blood_pressure_diastolic")
	private String bloodPressureDiastolic;
	
	@Column(name = "respiration")
	private String respiration;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private String updatedDate;

	@Column(name="organization_id")
	private int organizationId;
	
	@Column(name="unit_id")
	private int unitId;
	
	
	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public int getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getBreastFeeding() {
		return breastFeeding;
	}

	public void setBreastFeeding(String breastFeeding) {
		this.breastFeeding = breastFeeding;
	}

	public String getTransferOfBabyTo() {
		return transferOfBabyTo;
	}

	public void setTransferOfBabyTo(String transferOfBabyTo) {
		this.transferOfBabyTo = transferOfBabyTo;
	}

	public String getCordClamping() {
		return cordClamping;
	}

	public void setCordClamping(String cordClamping) {
		this.cordClamping = cordClamping;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUhidNumberBaby() {
		return uhidNumberBaby;
	}

	public void setUhidNumberBaby(String uhidNumberBaby) {
		this.uhidNumberBaby = uhidNumberBaby;
	}

	public String getBloodPressureSystolic() {
		return bloodPressureSystolic;
	}

	public void setBloodPressureSystolic(String bloodPressureSystolic) {
		this.bloodPressureSystolic = bloodPressureSystolic;
	}

	public String getPulseRate() {
		return pulseRate;
	}

	public void setPulseRate(String pulseRate) {
		this.pulseRate = pulseRate;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getHeadCircumference() {
		return headCircumference;
	}

	public void setHeadCircumference(String headCircumference) {
		this.headCircumference = headCircumference;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getBloodPressureDiastolic() {
		return bloodPressureDiastolic;
	}

	public void setBloodPressureDiastolic(String bloodPressureDiastolic) {
		this.bloodPressureDiastolic = bloodPressureDiastolic;
	}

	public String getRespiration() {
		return respiration;
	}

	public void setRespiration(String respiration) {
		this.respiration = respiration;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	
	
}
