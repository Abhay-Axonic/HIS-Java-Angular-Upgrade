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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	

	@NamedQuery(name="TO_CHECK_UNIQUE_REC",
		query=" SELECT TTD.transfusionTransmittedDiseasesId AS transfusionTransmittedDiseasesId "
				+ " FROM TransfusionTransmittedDiseases TTD "
				+ " WHERE TTD.bloodBagNo= :bloodBagNo AND TTD.barcode= :barCode"
				),
	
	@NamedQuery(name="SEARCH_TTD_DETAILS",
	query="SELECT mstr.transfusionTransmittedDiseasesId as transfusionTransmittedDiseasesId, "
				+ " mstr.bloodBagCollectionId as bloodBagCollectionId, "
				+ " bloodBagCollection.donorId as donorId, "
				+ " case when bloodBagCollection.collectionDate != null then "
				+ " to_char(bloodBagCollection.collectionDate,'DD-MM-YYYY') end as collectionDate, "
				+ " case when mstr.createdDate!=null then "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') "
				+ " end as collectionDate, "
				+ " bloodBagCollection.barCode as barCode, "
				+ " bloodBagCollection.bloodGroupId as bloodGroupId, "
				+ " bloodBagCollection.bagCapacityId as bagCapacityId, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.ttdDoneById as ttdDoneById, "
				+ " mstr.unitId as unitId, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.bloodBagNo as bloodBagNo, "
				+ " mstr.transfusionTransmittedDiseasesStatus as transfusionTransmittedDiseasesStatus, "
				+ " donor.donorId as donorId, "
				+ " bloodBankStatusMaster.name as tTDStatusName, "
				+ " donor.donorNumber as donorNumber, "
				+ " mstr.bloodBankStockId as bloodBankStockId,  "
				+ " mstr.tTDCheck as tTDCheck, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacity, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroup "
				+ " FROM TransfusionTransmittedDiseases mstr "
				+ " LEFT JOIN  mstr.bloodBagCollection bloodBagCollection "
				+ " LEFT JOIN  bloodBagCollection.donorRegistrationMaster donor "
				+ " LEFT JOIN  bloodBagCollection.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " LEFT JOIN mstr.bloodBankStatusMaster  bloodBankStatusMaster "
				+ " LEFT JOIN  bloodBagCollection.bloodGroupMaster bloodGroupMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND ( mstr.transfusionTransmittedDiseasesStatus = 5 "
				+ " OR mstr.transfusionTransmittedDiseasesStatus = 8)"),
	
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_ACCEPTED_TTD_LIST_ON_RED_CELL_GROUPING_AND_SERUM",query=""
			+ "select mstr.transfusion_transmitted_diseases_id as \"transfusionTransmittedDiseasesId\"," + 
			"	mstr.blood_bag_collection_id as \"bloodBagCollectionId\"," + 
			"	bloodBagCollection.donor_id as \"donorId\"," + 
			"	case" + 
			"		when mstr.created_date is not null then to_char(mstr.created_date, 'DD-MM-YYYY')" + 
			"	end as \"collectionDate\"," + 
			"	bloodBagCollection.barcode as \"barCode\"," + 
			"	bloodBagCollection.blood_group_id as \"bloodGroupId\"," + 
			"	bloodBagCollection.bag_capacity_id as \"bagCapacityId\"," + 
			"	to_char(mstr.created_date, 'DD-MM-YYYY') as \"createdDate\"," + 
			"	mstr.created_by as \"createdBy\"," + 
			"	mstr.ttd_done_by_id as \"ttdDoneById\"," + 
			"	mstr.unit_id as \"unitId\"," + 
			"	mstr.organization_id as \"organizationId\"," + 
			"	mstr.blood_bag_no as \"bloodBagNo\"," + 
			"	bloodBankStatusMaster.name as \"tTDStatusName\"," + 
			"   donor.donor_number as \"donorNumber\"," + 
			"	mstr.ttd_check as \"tTDCheck\"," + 
			"	bloodBagCapacityMaster.blood_bag_capacity_desc as \"bloodBagCapacity\"," + 
			"   bloodBagCollection.blood_unit_number as \"bloodUnitNumber\","
			+ " tbrc.red_cell_id as \"redCellId\", " + 
			"	bloodGroupMaster.blood_group_desc as \"bloodGroup\" " + 
			"from" + 
			"	blood_bank.t_transfusion_transmitted_diseases mstr " + 
			"left  join blood_bank.t_blood_bag_collection bloodBagCollection on " + 
			"	mstr.blood_bag_collection_id = bloodBagCollection.blood_bag_collection_id " + 
			"left  join blood_bank.t_donor_registration donor on " + 
			"	bloodBagCollection.donor_id = donor.donor_id " + 
			"left  join blood_bank.m_blood_bag_capacity_master bloodBagCapacityMaster on " + 
			"	bloodBagCollection.bag_capacity_id = bloodBagCapacityMaster.blood_bag_capacity_id " + 
			"left  join blood_bank.m_blood_bank_status_master bloodBankStatusMaster on " + 
			"	mstr.transfusion_transmitted_diseases_status = bloodBankStatusMaster.id " + 
			"left  join blood_bank.m_blood_group_master bloodGroupMaster on " + 
			"	mstr.blood_group_id = bloodGroupMaster.blood_group_id " + 
/*			"left  join blood_bank.m_blood_bank_stock bloodBankStock on " + 
			"	mstr.blood_bank_stock_id = bloodBankStock.blood_bank_stock_id "*/
			 " left join blood_bank.t_blood_red_cell tbrc   on " + 
			"	tbrc.transfusion_transmitted_diseases_id  = mstr.transfusion_transmitted_diseases_id  " + 
			 " WHERE mstr.organization_id= :organizationId "
			+ " AND mstr.unit_id= :unitId "
			+"	AND mstr.transfusion_transmitted_diseases_status = 1"
			+ " AND bloodBagCollection.donor_id is not null ")
	})
@Entity
@Table(name = "t_transfusion_transmitted_diseases", schema = "blood_bank")
@SequenceGenerator(name = "t_transfusion_transmitted_diseases_seq", sequenceName = "blood_bank.t_transfusion_transmitted_diseases_seq", allocationSize = 1)
public class TransfusionTransmittedDiseases {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_transfusion_transmitted_diseases_seq")
	@Column(name = "transfusion_transmitted_diseases_id")
	private Integer transfusionTransmittedDiseasesId;
	
	@Column(name = "blood_bag_no")
	private String bloodBagNo;
	
	@Column(name = "blood_bag_collection_id")
	private Integer bloodBagCollectionId;
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "barcode")
	private String barcode;
	
	@Column(name = "donor_id")
	private Integer donorId;
	
	@Column(name = "donor_number")
	private String donorNumber;
	
	@Column(name = "ttd_done_by_id")
	private Integer ttdDoneById;
	
	@Column(name = "transfusion_transmitted_diseases_status")
	private Integer transfusionTransmittedDiseasesStatus;
	
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

	@Column(name = "blood_bank_stock_id")
	private Integer bloodBankStockId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "ttd_check")
	private Boolean tTDCheck;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bag_collection_id", insertable = false, updatable = false)
	private BloodBagCollection bloodBagCollection;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transfusion_transmitted_diseases_status", insertable = false, updatable = false)
	private BloodBankStatusMaster bloodBankStatusMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bank_stock_id", insertable = false, updatable = false)
	private BloodBankStock bloodBankStock;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	public Integer getTransfusionTransmittedDiseasesId() {
		return transfusionTransmittedDiseasesId;
	}

	public void setTransfusionTransmittedDiseasesId(Integer transfusionTransmittedDiseasesId) {
		this.transfusionTransmittedDiseasesId = transfusionTransmittedDiseasesId;
	}

	
	public Integer getBloodBagCollectionId() {
		return bloodBagCollectionId;
	}

	public void setBloodBagCollectionId(Integer bloodBagCollectionId) {
		this.bloodBagCollectionId = bloodBagCollectionId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}

	public String getDonorNumber() {
		return donorNumber;
	}

	public void setDonorNumber(String donorNumber) {
		this.donorNumber = donorNumber;
	}

	public BloodBagCollection getBloodBagCollection() {
		return bloodBagCollection;
	}

	public void setBloodBagCollection(BloodBagCollection bloodBagCollection) {
		this.bloodBagCollection = bloodBagCollection;
	}

	public Integer getTransfusionTransmittedDiseasesStatus() {
		return transfusionTransmittedDiseasesStatus;
	}

	public void setTransfusionTransmittedDiseasesStatus(Integer transfusionTransmittedDiseasesStatus) {
		this.transfusionTransmittedDiseasesStatus = transfusionTransmittedDiseasesStatus;
	}

	public Integer getBloodBankStockId() {
		return bloodBankStockId;
	}

	public void setBloodBankStockId(Integer bloodBankStockId) {
		this.bloodBankStockId = bloodBankStockId;
	}

	public Boolean gettTDCheck() {
		return tTDCheck;
	}

	public void settTDCheck(Boolean tTDCheck) {
		this.tTDCheck = tTDCheck;
	}

	public Integer getTtdDoneById() {
		return ttdDoneById;
	}

	public void setTtdDoneById(Integer ttdDoneById) {
		this.ttdDoneById = ttdDoneById;
	}

	public BloodBankStatusMaster getBloodBankStatusMaster() {
		return bloodBankStatusMaster;
	}

	public void setBloodBankStatusMaster(BloodBankStatusMaster bloodBankStatusMaster) {
		this.bloodBankStatusMaster = bloodBankStatusMaster;
	}

}
