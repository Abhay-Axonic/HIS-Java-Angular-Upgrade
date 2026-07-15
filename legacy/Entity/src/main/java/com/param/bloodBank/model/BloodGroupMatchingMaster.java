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


@NamedQueries({
	
	@NamedQuery(name = "GET_BLOOD_GROUP_MATCHING_MASTER_LIST",
			query="select master.bloodGroupMatchingId as bloodGroupMatchingId, "
					+" master.recipientBloodGroupId as recipientBloodGroupId, "
					+" master.donorBloodGroupId as donorBloodGroupId, "
					+" master.availability as availability  , "
					+" master.unitId as unitId  , "
					+" master.componentId as componentId  , " 
					+" master.createdBy as createdBy  , " 
					+" to_char(master.createdDate ,'DD-MM-YYYY HH:ss:mm') as createdDate  , " 
					+" master.status as status  , " 
					+" master.updatedBy as updatedBy  , "
					+" to_char(master.updatedDate,'DD-MM-YYYY HH:ss:mm') as updatedDate  , " 
					+" master.organizationId as organizationId   "
					+" from BloodGroupMatchingMaster master" 
					+" where master.componentId=:componentId and master.unitId=:unitId and master.organizationId=:organizationId and master.status='A'"),
	
	@NamedQuery(name = "GET_BLOOD_GROUP_LIST_BY_COMPONENT_AND_DONOR_BLOOD_GROUP_ID",
	query="select master.recipientBloodGroupId as recipientBloodGroupId, "
			+ " bloodGroupMaster.bloodGroupDesc as bloodGroupDesc "
			+ " from BloodGroupMatchingMaster master " 
			+ " INNER JOIN  master.bloodGroupMaster bloodGroupMaster "
			+ " where master.componentId=:componentId "
			+ "and master.unitId=:unitId "
			+ "and master.organizationId=:organizationId "
			+ " and master.donorBloodGroupId=:donorBloodGroupId ")
})
@Entity
@Table(name = "m_blood_group_matching_master", schema = "blood_bank")
@SequenceGenerator(name = "blood_group_matching_master_seq", sequenceName = "blood_bank.blood_group_matching_master_seq", allocationSize = 1)

public class BloodGroupMatchingMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_group_matching_master_seq")
	@Column(name = "blood_group_matching_id ")
	private Integer bloodGroupMatchingId;
	
	@Column(name = "recipient_blood_group_id  ")
	private Integer recipientBloodGroupId;
	
	@Column(name = "donor_blood_group_id  ")
	private Integer donorBloodGroupId;
	
	@Column(name = "availability")
	private Integer availability;
	
	@Column(name = "component_id")
	private Integer componentId ;
	
	@Column(name = "created_by")
	private Integer createdBy ;
	
	@Column(name = "updated_by")
	private Integer updatedBy  ;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId ;

	@Column(name = "status")
	private Character status ;
	

	@Column(name = "created_date ")
	private Date createdDate  ;

	@Column(name = "updated_date  ")
	private Date updatedDate   ;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient_blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	public Integer getBloodGroupMatchingId() {
		return bloodGroupMatchingId;
	}

	public void setBloodGroupMatchingId(Integer bloodGroupMatchingId) {
		this.bloodGroupMatchingId = bloodGroupMatchingId;
	}

	public Integer getRecipientBloodGroupId() {
		return recipientBloodGroupId;
	}

	public void setRecipientBloodGroupId(Integer recipientBloodGroupId) {
		this.recipientBloodGroupId = recipientBloodGroupId;
	}

	public Integer getDonorBloodGroupId() {
		return donorBloodGroupId;
	}

	public void setDonorBloodGroupId(Integer donorBloodGroupId) {
		this.donorBloodGroupId = donorBloodGroupId;
	}

	public Integer getAvailability() {
		return availability;
	}

	public void setAvailability(Integer availability) {
		this.availability = availability;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	
	
}
