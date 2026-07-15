package com.param.bloodBank.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_BY_CAMP",
		query="SELECT mstr.bloodBagCollectionId as bloodBagCollectionId "
					+ " FROM BloodBagCollection mstr"
					+ " WHERE mstr.campId= :campId"
					+ " AND mstr.organizationId= :organizationId"
					+ " AND mstr.unitId= :unitId"
					+ " AND mstr.donorId= :donorId "
					+ " AND to_char(mstr.collectionDate ,'dd-MM-yyyy') = :collectionDate "
					),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_BY_BLOOD_BANK",
	query="SELECT mstr.bloodBagCollectionId as bloodBagCollectionId "
				+ " FROM BloodBagCollection mstr"
				+ " WHERE mstr.bloodBankId= :bloodBankId"
				+ " AND mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				+ " AND mstr.donorId= :donorId "
				+ " AND to_char(mstr.collectionDate ,'dd-MM-yyyy') = :collectionDate "
				),
	
	@NamedQuery(name="SEARCH_BLOOD_BAG_BY_MULIPLE_CRITERIA",
	query="SELECT mstr.bloodBagCollectionId as bloodBagCollectionId, "
				+ " mstr.campId as campId, "
				+ " mstr.donorId as donorId, "
				+ " to_char(mstr.collectionDate,'DD-MM-YYYY') as collectionDate, "
				+ " to_char(mstr.fromTime,'HH:mm:ss') as fromTime, "
				+ " to_char(mstr.toTime,'HH:mm:ss') as toTime, "
				+ " mstr.collectedFrom as collectedFrom, "
				+ " mstr.segmentNumber as segmentNumber, "
				+ " mstr.bloodBankId as bloodBankId, "
				+ " mstr.barCode as barCode, "
				+ " mstr.bloodBagTypeId as bloodBagTypeId, "
				+ " mstr.bloodGroupId as bloodGroupId, "
				+ " mstr.bagCapacityId as bagCapacityId, "
				+ " mstr.batchNo as batchNo, "
				+ " mstr.terminationReasonId as terminationReasonId, "
				+ " mstr.bloodBagCollectionStatusId as bloodBagCollectionStatusId, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.unitId as unitId, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.bloodBagNo as bloodBagNo, "
				+ " cMstr.campName as campName, "
				+ " donor.donorNumber as donorNumber, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacity, "
				+ " bloodBagTypeMaster.bloodBagTypeDesc as bloodBagType, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
				+ " bloodBankMaster.bloodBankDesc as bloodBank "
				+ " FROM BloodBagCollection mstr "
				+ " LEFT JOIN  mstr.campMaster cMstr "
				+ " INNER JOIN  mstr.donorRegistrationMaster donor "
				+ " INNER JOIN  mstr.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " INNER JOIN  mstr.bloodBagTypeMaster bloodBagTypeMaster "
				+ " LEFT JOIN  mstr.bloodBankMaster bloodBankMaster "
				+ " INNER JOIN  mstr.bloodGroupMaster bloodGroupMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodBagCollectionStatusId = 1 "
				+ " AND mstr.forwardedToTtdTest = false "),
	
	@NamedQuery(name="GET_SAVED_BLOOD_BAG_DETAIL_LIST",
	query="SELECT mstr.bloodBagCollectionId as bloodBagCollectionId, "
				+ " mstr.campId as campId, "
				+ " mstr.donorId as donorId, "
				+ " to_char(mstr.collectionDate,'DD-MM-YYYY') as collectionDate, "
				+ " to_char(mstr.fromTime,'HH:mm:ss') as fromTime, "
				+ " to_char(mstr.toTime,'HH:mm:ss') as toTime, "
				+ " mstr.collectedFrom as collectedFrom, "
				+ " mstr.segmentNumber as segmentNumber, "
				+ " mstr.bloodBankId as bloodBankId, "
				+ " mstr.barCode as barCode, "
				+ " mstr.bloodBagTypeId as bloodBagTypeId, "
				+ " mstr.bloodGroupId as bloodGroupId, "
				+ " mstr.bagCapacityId as bagCapacityId, "
				+ " mstr.batchNo as batchNo, "
				+ " mstr.terminationReasonId as terminationReasonId, "
				+ " mstr.bloodBagCollectionStatusId as bloodBagCollectionStatusId, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.unitId as unitId, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.bloodBagNo as bloodBagNo, "
				+ " cMstr.campName as campName, "
				+ " donor.donorNumber as donorNumber, "
				+ " concat(coalesce(donor.firstName,'') ,' ', coalesce(donor.middleName,'') ,' ', coalesce(donor.lastName,'')) as donorName, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacity, "
				+ " bloodBagTypeMaster.bloodBagTypeDesc as bloodBagType, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
				+ " bloodBankStatusMaster.name as bloodBagCollectionStatus," 
				+ " genMstr.desc as gender, "
				+ " bloodBankMaster.bloodBankDesc as bloodBank "
				+ " FROM BloodBagCollection mstr "
				+ " LEFT JOIN  mstr.campMaster cMstr "
				+ " INNER JOIN  mstr.donorRegistrationMaster donor "
				+ " INNER JOIN  mstr.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " INNER JOIN  mstr.bloodBagTypeMaster bloodBagTypeMaster "
				+ " LEFT JOIN  mstr.bloodBankMaster bloodBankMaster "
				+ " INNER JOIN  mstr.bloodGroupMaster bloodGroupMaster "
				+ " INNER JOIN mstr.bloodBankStatusMaster  bloodBankStatusMaster "
				+ " INNER JOIN donor.genderMaster  genMstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "),
	
	@NamedQuery(name="GET_BLOOD_BAG_COLLECTION_DETAILS_BY_ID",
	query="SELECT mstr.bloodBagCollectionId as bloodBagCollectionId, "
				+ " mstr.campId as campId, "
				+ " mstr.donorId as donorId, "
				+ " to_char(mstr.collectionDate,'DD/MM/YYYY') as collectionDate, "
				+ " to_char(mstr.fromTime,'HH:mm:ss') as fromTime, "
				+ " to_char(mstr.toTime,'HH:mm:ss') as toTime, "
				+ " mstr.collectedFrom as collectedFrom, "
				+ " mstr.segmentNumber as segmentNumber, "
				+ " mstr.bloodBankId as bloodBankId, "
				+ " mstr.bloodUnitNumber as bloodUnitNumber, "
				+ " mstr.barCode as barCode, "
				+ " mstr.bloodBagTypeId as bloodBagTypeId, "
				+ " mstr.bloodGroupId as bloodGroupId, "
				+ " mstr.bagCapacityId as bagCapacityId, "
				+ " mstr.batchNo as batchNo, "
				+ " mstr.terminationReasonId as terminationReasonId, "
				+ " mstr.bloodBagCollectionStatusId as bloodBagCollectionStatusId, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.unitId as unitId, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.bloodBagNo as bloodBagNo, "
				+ " cMstr.campName as campName, "
				+ " donor.donorNumber as donorNumber, "
				+ " concat(coalesce(donor.firstName,'') ,' ', coalesce(donor.middleName,'') ,' ', coalesce(donor.lastName,'')) as donorName, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacity, "
				+ " bloodBagTypeMaster.bloodBagTypeDesc as bloodBagType, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
				+ " bloodBankStatusMaster.name as bloodBagCollectionStatus," 
				+ " genMstr.desc as gender, "
				+ " bloodBankMaster.bloodBankDesc as bloodBank "
				+ " FROM BloodBagCollection mstr "
				+ " LEFT JOIN  mstr.campMaster cMstr "
				+ " INNER JOIN  mstr.donorRegistrationMaster donor "
				+ " INNER JOIN  mstr.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " INNER JOIN  mstr.bloodBagTypeMaster bloodBagTypeMaster "
				+ " LEFT JOIN  mstr.bloodBankMaster bloodBankMaster "
				+ " INNER JOIN  mstr.bloodGroupMaster bloodGroupMaster "
				+ " INNER JOIN mstr.bloodBankStatusMaster  bloodBankStatusMaster "
				+ " INNER JOIN donor.genderMaster  genMstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodBagCollectionId= :bloodBagCollectionId ")
	
	
	
})

@Entity
@Table(name="t_blood_bag_collection", schema="blood_bank")
@SequenceGenerator(name="blood_bag_collection_seq", sequenceName="blood_bank.blood_bag_collection_seq", allocationSize= 1)

public class BloodBagCollection {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_bag_collection_seq")

	@Column(name = "blood_bag_collection_id")
	private Integer bloodBagCollectionId;
	
	@Column(name = "camp_id")
	private Integer campId;
	
	@Column(name = "donor_id")
	private Integer donorId;
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	 	
	@Column(name = "collection_date")
	private Date collectionDate;
	
	@Column(name="from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name="to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "collected_from")
	private Integer collectedFrom;
	
	@Column(name = "blood_unit_number")
	private Integer bloodUnitNumber;
	
	@Column(name = "segment_number")
	private String segmentNumber;
	
	@Column(name = "blood_bank_id")
	private Integer bloodBankId;
	
	@Column(name = "barcode")
	private String barCode;
	
	@Column(name = "blood_bag_type_id")
	private Integer bloodBagTypeId;
	
	@Column(name = "bag_capacity_id")
	private Integer bagCapacityId;
	
	@Column(name = "batch_no")
	private String batchNo;
	
	@Column(name = "termination_reason_id")
	private Integer terminationReasonId;
	
	@Column(name = "blood_bag_collection_status_id")
	private Integer bloodBagCollectionStatusId;
	
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
	
	@Column(name = "blood_bag_no")
	private String bloodBagNo;
	
	@Column(name = "forwarded_to_ttd_test")
	private Boolean forwardedToTtdTest;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "camp_id", insertable = false, updatable = false)
	private CampMaster campMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bank_id", insertable = false, updatable = false)
	private BloodBankMaster bloodBankMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "donor_id", insertable = false, updatable = false)
	private DonorRegistrationMaster donorRegistrationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bag_capacity_id", insertable = false, updatable = false)
	private BloodBagCapacityMaster bloodBagCapacityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bag_type_id", insertable = false, updatable = false)
	private BloodBagTypeMaster bloodBagTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bag_collection_status_id", insertable = false, updatable = false)
	private BloodBankStatusMaster bloodBankStatusMaster;
	
	public Integer getBloodBagCollectionId() {
		return bloodBagCollectionId;
	}

	public void setBloodBagCollectionId(Integer bloodBagCollectionId) {
		this.bloodBagCollectionId = bloodBagCollectionId;
	}

	public Integer getCampId() {
		return campId;
	}

	public void setCampId(Integer campId) {
		this.campId = campId;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	public Integer getCollectedFrom() {
		return collectedFrom;
	}

	public void setCollectedFrom(Integer collectedFrom) {
		this.collectedFrom = collectedFrom;
	}

	public Integer getBloodUnitNumber() {
		return bloodUnitNumber;
	}

	public void setBloodUnitNumber(Integer bloodUnitNumber) {
		this.bloodUnitNumber = bloodUnitNumber;
	}

	public String getSegmentNumber() {
		return segmentNumber;
	}

	public void setSegmentNumber(String segmentNumber) {
		this.segmentNumber = segmentNumber;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(Instant fromTime) {
		this.fromTime = fromTime;
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(Instant toTime) {
		this.toTime = toTime;
	}

	public Integer getBloodBagTypeId() {
		return bloodBagTypeId;
	}

	public void setBloodBagTypeId(Integer bloodBagTypeId) {
		this.bloodBagTypeId = bloodBagTypeId;
	}

	public Integer getBagCapacityId() {
		return bagCapacityId;
	}

	public void setBagCapacityId(Integer bagCapacityId) {
		this.bagCapacityId = bagCapacityId;
	}

	public Integer getBloodBagCollectionStatusId() {
		return bloodBagCollectionStatusId;
	}

	public void setBloodBagCollectionStatusId(Integer bloodBagCollectionStatusId) {
		this.bloodBagCollectionStatusId = bloodBagCollectionStatusId;
	}

	public Integer getBloodBankId() {
		return bloodBankId;
	}

	public void setBloodBankId(Integer bloodBankId) {
		this.bloodBankId = bloodBankId;
	}

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public CampMaster getCampMaster() {
		return campMaster;
	}

	public void setCampMaster(CampMaster campMaster) {
		this.campMaster = campMaster;
	}

	public DonorRegistrationMaster getDonorRegistrationMaster() {
		return donorRegistrationMaster;
	}

	public void setDonorRegistrationMaster(DonorRegistrationMaster donorRegistrationMaster) {
		this.donorRegistrationMaster = donorRegistrationMaster;
	}

	public BloodBagCapacityMaster getBloodBagCapacityMaster() {
		return bloodBagCapacityMaster;
	}

	public void setBloodBagCapacityMaster(BloodBagCapacityMaster bloodBagCapacityMaster) {
		this.bloodBagCapacityMaster = bloodBagCapacityMaster;
	}

	public BloodBagTypeMaster getBloodBagTypeMaster() {
		return bloodBagTypeMaster;
	}

	public void setBloodBagTypeMaster(BloodBagTypeMaster bloodBagTypeMaster) {
		this.bloodBagTypeMaster = bloodBagTypeMaster;
	}

	public BloodGroupMaster getBloodGroupMaster() {
		return bloodGroupMaster;
	}

	public void setBloodGroupMaster(BloodGroupMaster bloodGroupMaster) {
		this.bloodGroupMaster = bloodGroupMaster;
	}

	public BloodBankMaster getBloodBankMaster() {
		return bloodBankMaster;
	}

	public void setBloodBankMaster(BloodBankMaster bloodBankMaster) {
		this.bloodBankMaster = bloodBankMaster;
	}

	public Boolean getForwardedToTtdTest() {
		return forwardedToTtdTest;
	}

	public void setForwardedToTtdTest(Boolean forwardedToTtdTest) {
		this.forwardedToTtdTest = forwardedToTtdTest;
	}

	public Integer getTerminationReasonId() {
		return terminationReasonId;
	}

	public void setTerminationReasonId(Integer terminationReasonId) {
		this.terminationReasonId = terminationReasonId;
	}

	public BloodBankStatusMaster getBloodBankStatusMaster() {
		return bloodBankStatusMaster;
	}

	public void setBloodBankStatusMaster(BloodBankStatusMaster bloodBankStatusMaster) {
		this.bloodBankStatusMaster = bloodBankStatusMaster;
	}
	
	
}
