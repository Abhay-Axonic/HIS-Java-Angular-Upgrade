package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;



//@NamedQueries({
		
//		@NamedQuery(name = "GET_BABY_DETAILS_BY_ID", query ="SELECT newBornBaby.newBornBabyRegId as newBornBabyRegId, newBornBaby.patientId as patientId, "
//				+ "newBornBaby.uhidNumberBaby as uhidNumberBaby, newBornBaby.fatherName as fatherName, newBornBaby.deliveryType as deliveryType, "
//				+ "newBornBaby.numberOfBabies as numberOfBabies, newBornBaby.babyName as babyName, "
//				+ "newBornBaby.dob as dob, newBornBaby.tob as tob, newBornBaby.sex as sex, newBornBaby.outcome as outcome, "
//				+ "newBornBaby.gestationalAge as gestationalAge, newBornBaby.birthTerm as birthTerm, newBornBaby.status as status "
//				+ "FROM NewBornBaby as newBornBaby WHERE newBornBaby.patientId=:patientId "
//				+ "and newBornBaby.organizationId=:organizationId"),

//})

@Entity
@Table(name = "m_new_born_baby_reg", schema = "public")
@SequenceGenerator(name = "m_new_born_baby_reg_seq", sequenceName = "public.m_new_born_baby_reg_seq", allocationSize = 1)
public class NewBornBaby {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_new_born_baby_reg_seq")
	@Column(name = "new_born_baby_reg_id")
	private int newBornBabyRegId;

	@Column(name = "patient_id")
	private int patientId;

	@Column(name = "uhid_number_baby")
	private String uhidNumberBaby;
	
	@Column(name = "father_name")
	private String fatherName;
	
	@Column(name = "delivery_type")
	private String deliveryType;
	
	@Column(name = "number_of_babies")
	private int numberOfBabies;
	
	@Column(name = "baby_name")
	private String babyName;
	
	@Column(name = "weight")
	private Double weight;
	
	@Column(name = "dob")
	private Date dob;
	
	@Column(name = "tob")
	private String tob;
	
	@Column(name = "sex")
	private Integer sex;
	
	@Column(name = "outcome")
	private String outcome;
	
	@Column(name = "gestational_age")
	private int gestationalAge;
	
	@Column(name = "birth_term")
	private String birthTerm;
	
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
	
	@Transient
	private String gender;

	public int getNewBornBabyRegId() {
		return newBornBabyRegId;
	}

	public void setNewBornBabyRegId(int newBornBabyRegId) {
		this.newBornBabyRegId = newBornBabyRegId;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getUhidNumberBaby() {
		return uhidNumberBaby;
	}

	public void setUhidNumberBaby(String uhidNumberBaby) {
		this.uhidNumberBaby = uhidNumberBaby;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public int getNumberOfBabies() {
		return numberOfBabies;
	}

	public void setNumberOfBabies(int numberOfBabies) {
		this.numberOfBabies = numberOfBabies;
	}

	public String getBabyName() {
		return babyName;
	}

	public void setBabyName(String babyName) {
		this.babyName = babyName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getTob() {
		return tob;
	}

	public void setTob(String tob) {
		this.tob = tob;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

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

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getGestationalAge() {
		return gestationalAge;
	}

	public void setGestationalAge(int gestationalAge) {
		this.gestationalAge = gestationalAge;
	}

	public String getBirthTerm() {
		return birthTerm;
	}

	public void setBirthTerm(String birthTerm) {
		this.birthTerm = birthTerm;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	
	
	
}
