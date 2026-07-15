package com.param.bloodBank.model;

import java.util.Date;

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
	
	@NamedQuery(name="GET_EXAMINATION_DETAILS_BY_COLLECTION_ID",
	query="SELECT mstr.examinationId as examinationId, "
				+ " mstr.bloodBagCollectionId as bloodBagCollectionId, "
				+ " mstr.pulse as pulse, "
				+ " mstr.bpSystolic as bpSystolic, "
				+ " mstr.bpDiastolic as bpDiastolic, "
				+ " to_char(mstr.examinationDate,'DD/MM/YYYY') as examinationDate, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.unitId as unitId, "
				+ " mstr.organizationId as organizationId "
				+ " FROM BloodBagCollectionExaminationDetails mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodBagCollectionId= :bloodBagCollectionId ")
})

@Entity
@Table(name="t_blood_bag_collection_examination_details", schema="blood_bank")
@SequenceGenerator(name="blood_bag_collection_examination_details_seq", sequenceName="blood_bank.blood_bag_collection_examination_details_seq", allocationSize= 1)
public class BloodBagCollectionExaminationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_bag_collection_examination_details_seq")

	@Column(name = "examination_id")
	private Integer examinationId;
	
	@Column(name = "blood_bag_collection_id")
	private Integer bloodBagCollectionId;
	
	@Column(name = "pulse")
	private Double pulse;
	
	@Column(name = "bp_systolic")
	private Double bpSystolic;
	 	
	@Column(name = "bp_diastolic")
	private Double bpDiastolic;
	
	@Column(name = "examination_date")
	private Date examinationDate;
	
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

	public Integer getBloodBagCollectionId() {
		return bloodBagCollectionId;
	}

	public void setBloodBagCollectionId(Integer bloodBagCollectionId) {
		this.bloodBagCollectionId = bloodBagCollectionId;
	}

	public Double getPulse() {
		return pulse;
	}

	public void setPulse(Double pulse) {
		this.pulse = pulse;
	}

	public Double getBpSystolic() {
		return bpSystolic;
	}

	public void setBpSystolic(Double bpSystolic) {
		this.bpSystolic = bpSystolic;
	}

	public Double getBpDiastolic() {
		return bpDiastolic;
	}

	public void setBpDiastolic(Double bpDiastolic) {
		this.bpDiastolic = bpDiastolic;
	}

	public Date getExaminationDate() {
		return examinationDate;
	}

	public void setExaminationDate(Date examinationDate) {
		this.examinationDate = examinationDate;
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

	public Integer getExaminationId() {
		return examinationId;
	}

	public void setExaminationId(Integer examinationId) {
		this.examinationId = examinationId;
	}
	
}
