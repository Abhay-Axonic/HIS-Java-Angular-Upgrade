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

		/*@NamedQuery(name = "GET_BABY_DETAILS_BY_NAME", query = "SELECT babyStillAssmnt.newBornBabyRegId as newBornBabyRegId, babyStillAssmnt.patientId as patientId, "
				+ "babyStillAssmnt.uhidNumberBaby as uhidNumberBaby, babyStillAssmnt.fatherName as fatherName, babyStillAssmnt.deliveryType as deliveryType, babyStillAssmnt.numberOfBabies as numberOfBabies, babyStillAssmnt.babyName as babyName, "
				+ "babyStillAssmnt.dob as dob, babyStillAssmnt.tob as tob, babyStillAssmnt.sex as sex, babyStillAssmnt.outcome as outcome, "
				+ "babyStillAssmnt.status as status "
				+ "FROM NewBornBaby as babyStillAssmnt "
				+ "WHERE (LOWER(babyStillAssmnt.babyName)=:babyName OR babyStillAssmnt.babyName=:babyName) "
				+ "AND babyStillAssmnt.patientId=:patientId AND babyStillAssmnt.organizationId=:organizationId "),*/
		
	@NamedQuery(name = "GET_SINGLE_STILL_BABY_REC", query ="SELECT babyStillAssmnt.newBornBabyStillId as newBornBabyStillId, babyStillAssmnt.patientId as patientId, "
			+ "babyStillAssmnt.uhidNumberBaby as uhidNumberBaby, babyStillAssmnt.stillType as stillType, babyStillAssmnt.weight as weight,"
			+ "babyStillAssmnt.status as status "
			+ "FROM BabyStillAssessment as babyStillAssmnt WHERE babyStillAssmnt.newBornBabyStillId=:newBornBabyStillId "
			+ "and babyStillAssmnt.organizationId=:organizationId AND babyStillAssmnt.unitId=:unitId"),
	
	@NamedQuery(name = "GET_STILL_BABY_UHID", query ="SELECT babyStillAssmnt.newBornBabyStillId as newBornBabyStillId, babyStillAssmnt.patientId as patientId, "
			+ "babyStillAssmnt.uhidNumberBaby as uhidNumberBaby, babyStillAssmnt.stillType as stillType, babyStillAssmnt.weight as weight,"
			+ "babyStillAssmnt.status as status "
			+ "FROM BabyStillAssessment as babyStillAssmnt WHERE (LOWER(babyStillAssmnt.uhidNumberBaby)=:uhidNumberBaby OR babyStillAssmnt.uhidNumberBaby=:uhidNumberBaby)"
			+ "AND babyStillAssmnt.organizationId=:organizationId AND babyStillAssmnt.unitId=:unitId"),

})

@Entity
@Table(name = "m_baby_still_assessment", schema = "public")
@SequenceGenerator(name = "m_baby_still_assessment_seq", sequenceName = "public.m_baby_still_assessment_seq", allocationSize = 1)
public class BabyStillAssessment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_baby_still_assessment_seq")
	@Column(name = "new_born_baby_still_id")
	private int newBornBabyStillId;

	@Column(name = "patient_id")
	private int patientId;

	@Column(name = "uhid_number_baby")
	private String uhidNumberBaby;
	
	@Column(name = "still_type")
	private String stillType;
	
	@Column(name = "weight")
	private String weight;
	
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

	public int getNewBornBabyStillId() {
		return newBornBabyStillId;
	}

	public void setNewBornBabyStillId(int newBornBabyStillId) {
		this.newBornBabyStillId = newBornBabyStillId;
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

	public String getStillType() {
		return stillType;
	}

	public void setStillType(String stillType) {
		this.stillType = stillType;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
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


	
	

	
	
}
