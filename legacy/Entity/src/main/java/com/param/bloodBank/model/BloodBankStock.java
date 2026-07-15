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
	@NamedQuery(name="SEARCH_BLOOD_BAG_STOCK_BY_MULIPLE_CRITERIA",
	query="SELECT mstr.bloodBankStockId as bloodBankStockId, "
				+ " mstr.donorId as donorId, "
				+ " mstr.segmentNumber as segmentNumber, "
				+ " mstr.bloodBankId as bloodBankId, "
				+ " mstr.bloodBagTypeId as bloodBagTypeId, "
				+ " mstr.bloodGroupId as bloodGroupId, "
				+ " mstr.bloodBagCapacityId as bloodBagCapacityId, "
				+ " mstr.loanNumber as loanNumber, "
				+ " mstr.stockStatusId as stockStatusId, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.shortName as shortName, "
				+ " mstr.batchNo as batchNo, "
				+ " mstr.tTDCheck as tTDCheck, "
				+ " to_char(mstr.expiryDate,'DD-MM-YYYY' ) as expiryDate, "
				+ " mstr.unitId as unitId, "
				+ " mstr.status as status, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.bloodBagNo as bloodBagNo, "
				+ " mstr.donorNumber as donorNumber, "
				+ " bloodBankStatusMaster.name as loanInStatus, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacity, "
				+ " bloodBagTypeMaster.bloodBagTypeDesc as bloodBagType, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
				+ " bloodBankMaster.bloodBankDesc as bloodBank "
				+ " FROM BloodBankStock mstr "
				+ " INNER JOIN  mstr.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " INNER JOIN  mstr.bloodBagTypeMaster bloodBagTypeMaster "
				+ " LEFT JOIN  mstr.bloodBankMaster bloodBankMaster "
				+ " INNER JOIN  mstr.bloodGroupMaster bloodGroupMaster "
				+ " INNER JOIN  mstr.bloodBankStatusMaster bloodBankStatusMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.loanInId= :loanInID"
				+ " AND lower(mstr.loanNumber)= :loanInNo"),
	
	@NamedQuery(name="SEARCH_BLOOD_BAG_STOCK_DETAILS_BY_TTD_STATUS",
	query="SELECT mstr.bloodBankStockId as bloodBankStockId, "
				+ " mstr.bloodBankId as bloodBankId, "
				+ " mstr.bloodBagTypeId as bloodBagTypeId, "
				+ " mstr.bloodGroupId as bloodGroupId, "
				+ " mstr.bloodBagCapacityId as bagCapacityId, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as collectionDate, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.batchNo as batchNo, "
				+ " mstr.tTDCheck as tTDCheck, "
				+ " mstr.unitId as unitId, "
				+ " mstr.loanNumber as donorNumber, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.bloodBagNo as bloodBagNo, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacity, "
				+ " bloodBagTypeMaster.bloodBagTypeDesc as bloodBagType, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
				+ " bloodBankMaster.bloodBankDesc as bloodBank "
				+ " FROM BloodBankStock mstr "
				+ " INNER JOIN  mstr.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " INNER JOIN  mstr.bloodBagTypeMaster bloodBagTypeMaster "
				+ " LEFT JOIN  mstr.bloodBankMaster bloodBankMaster "
				+ " INNER JOIN  mstr.bloodGroupMaster bloodGroupMaster "  
				+ " INNER JOIN  mstr.bloodBankStatusMaster bloodBankStatusMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.stockStatusId= 9 "
				+ " AND mstr.tTDCheck= true "
				+ " AND mstr.exportedToTtd= false"),
	
	@NamedQuery(name="SEARCH_LOAN_IN_BY_MULIPLE_CRITERIA",
	query="SELECT mstr.bloodBankStockId as bloodBankStockId, "
				+ " mstr.donorId as donorId, "
				+ " mstr.segmentNumber as segmentNumber, "
				+ " mstr.bloodBankId as bloodBankId, "
				+ " mstr.bloodBagTypeId as bloodBagTypeId, "
				+ " mstr.bloodGroupId as bloodGroupId, "
				+ " mstr.bloodBagCapacityId as bloodBagCapacityId, "
				+ " mstr.loanNumber as loanNumber, "
				+ " mstr.stockStatusId as stockStatusId, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.shortName as shortName, "
				+ " mstr.batchNo as batchNo, "
				+ " to_char(mstr.expiryDate,'DD-MM-YYYY' ) as expiryDate, "
				+ " mstr.unitId as unitId, "
				+ " mstr.tTDCheck as tTDCheck, "
				+ " mstr.status as status, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.bloodBagNo as bloodBagNo, "
				+ " mstr.donorNumber as donorNumber, "
				+ " bloodBankStatusMaster.name as loanInStatus, "
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacity, "
				+ " bloodBagTypeMaster.bloodBagTypeDesc as bloodBagType, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
				+ " bloodBankMaster.bloodBankDesc as bloodBank "
				+ " FROM BloodBankStock mstr "
				+ " INNER JOIN  mstr.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " INNER JOIN  mstr.bloodBagTypeMaster bloodBagTypeMaster "
				+ " LEFT JOIN  mstr.bloodBankMaster bloodBankMaster "
				+ " INNER JOIN  mstr.bloodGroupMaster bloodGroupMaster "
				+ " INNER JOIN  mstr.bloodBankStatusMaster bloodBankStatusMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.status= 'A' "
				+ " AND mstr.loanNumber= :loanInNO"
				+ " AND mstr.loanInId= :LoanInId"),
	
	@NamedQuery(name="GET_AVAILABLE_BLOOD_BY_BLOOD_BANK_ID",
	query="SELECT distinct "
				+ " mstr.bloodBankId as bloodBankId, "
				+ " mstr.bloodGroupId as bloodGroupId, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroup "
				+ " FROM BloodBankStock mstr "
				+ " INNER JOIN  mstr.bloodGroupMaster bloodGroupMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodBankId= :bloodBankId "
				+ " AND mstr.stockStatusId= 6 "),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_BLOOD_BAG_NUMBER",
	query="SELECT  mstr.bloodBankStockId as bloodBankStockId,"
				+ " mstr.bloodBagNo as bloodBagNo "
				+ " FROM BloodBankStock mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodBagNo= :bloodBagNo "),
	
	@NamedQuery(name="GET_AVAILABLE_BLOOD_BAG_BY_COMPONENT_AND_GROUP_ID",
	query="SELECT  mstr.bloodBankStockId as bloodBankStockId,"
				+ " mstr.segmentNumber as segmentNumber, "
				+ " mstr.bloodBagNo as bloodBagNo "
				+ " FROM BloodBankStock mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodComponentId= :bloodComponentId "
				+ " AND mstr.stockStatusId= 6 "
				+ " AND mstr.bloodGroupId= :bloodGroupId "
				+ " AND (lower(mstr.bloodBagNo) LIKE :bloodBagNo) "
				+ " AND to_char(mstr.expiryDate,'YYYY-MM-DD') > :currentDate"),
	
	@NamedQuery(name="GET_AVAILABLE_BLOOD_BAG_LIST_BY_MULTIPLE_CRITERIA",
	query="SELECT  mstr.bloodBankStockId as bloodBankStockId,"
				+ " mstr.segmentNumber as segmentNumber, "
				+ " mstr.bloodBagNo as bloodBagNo "
				+ " FROM BloodBankStock mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodComponentId= :bloodComponentId "
				+ " AND mstr.bloodBagCapacityId= :bloodBagCapacityId "
				+ " AND mstr.bloodBagTypeId= :bloodBagTypeId "
				+ " AND mstr.bloodBankId= :bloodBankId "
				+ " AND mstr.stockStatusId= 6 "
				+ " AND mstr.bloodGroupId= :bloodGroupId "
				+ " AND (lower(mstr.bloodBagNo) LIKE :bloodBagNo) "
				+ " AND to_char(mstr.expiryDate,'YYYY-MM-DD') > :currentDate")
})

@Entity
@Table(name="m_blood_bank_stock", schema="blood_bank")
@SequenceGenerator(name="m_blood_bank_stock_seq", sequenceName="blood_bank.m_blood_bank_stock_seq", allocationSize= 1)
public class BloodBankStock {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_blood_bank_stock_seq")
	
	@Column(name = "blood_bank_stock_id")
	private Integer bloodBankStockId;
	
	@Column(name = "blood_bank_id")
	private Integer bloodBankId;
	
	@Column(name = "short_name")
	private String shortName;
	
	@Column(name = "blood_bag_no")
	private String bloodBagNo;
	
	@Column(name = "segment_number")
	private String segmentNumber;
	
	@Column(name = "batch_no")
	private String batchNo;
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "blood_bag_capacity_id")
	private Integer bloodBagCapacityId;
	
	@Column(name = "blood_bag_type_id")
	private Integer bloodBagTypeId;
	
	@Column(name = "blood_unit")
	private Integer bloodUnit;
	
	@Column(name = "donor_id")
	private Integer donorId;
	
	@Column(name = "donor_number")
	private String donorNumber;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
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
	
	@Column(name = "stock_status_id")
	private Integer stockStatusId;

	@Column(name = "loan_number")
	private String loanNumber;
	
	@Column(name = "blood_component_id")
	private Integer bloodComponentId;
	
	@Column(name = "TTD_check")
	private Boolean tTDCheck;
	
	@Column(name = "exported_to_ttd")
	private boolean exportedToTtd;
	
	@Column(name = "loan_in_id")
	private Integer loanInId;
	
	@Column(name = "approved_by")
	private Integer approvedBy;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bank_id", insertable = false, updatable = false)
	private BloodBankMaster bloodBankMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bag_capacity_id", insertable = false, updatable = false)
	private BloodBagCapacityMaster bloodBagCapacityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bag_type_id", insertable = false, updatable = false)
	private BloodBagTypeMaster bloodBagTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_status_id", insertable = false, updatable = false)
	private BloodBankStatusMaster bloodBankStatusMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_in_id", insertable = false, updatable = false)
	private BloodBagLoanIn bloodBagLoanIn ;
	
	
	public Integer getBloodBankStockId() {
		return bloodBankStockId;
	}

	public void setBloodBankStockId(Integer bloodBankStockId) {
		this.bloodBankStockId = bloodBankStockId;
	}

	public Integer getBloodBankId() {
		return bloodBankId;
	}

	public void setBloodBankId(Integer bloodBankId) {
		this.bloodBankId = bloodBankId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public String getSegmentNumber() {
		return segmentNumber;
	}

	public void setSegmentNumber(String segmentNumber) {
		this.segmentNumber = segmentNumber;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Integer getBloodBagCapacityId() {
		return bloodBagCapacityId;
	}

	public void setBloodBagCapacityId(Integer bloodBagCapacityId) {
		this.bloodBagCapacityId = bloodBagCapacityId;
	}

	public Integer getBloodBagTypeId() {
		return bloodBagTypeId;
	}

	public void setBloodBagTypeId(Integer bloodBagTypeId) {
		this.bloodBagTypeId = bloodBagTypeId;
	}

	public Integer getBloodUnit() {
		return bloodUnit;
	}

	public void setBloodUnit(Integer bloodUnit) {
		this.bloodUnit = bloodUnit;
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

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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

	public Integer getStockStatusId() {
		return stockStatusId;
	}

	public void setStockStatusId(Integer stockStatusId) {
		this.stockStatusId = stockStatusId;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public BloodBankMaster getBloodBankMaster() {
		return bloodBankMaster;
	}

	public void setBloodBankMaster(BloodBankMaster bloodBankMaster) {
		this.bloodBankMaster = bloodBankMaster;
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

	public BloodBankStatusMaster getBloodBankStatusMaster() {
		return bloodBankStatusMaster;
	}

	public void setBloodBankStatusMaster(BloodBankStatusMaster bloodBankStatusMaster) {
		this.bloodBankStatusMaster = bloodBankStatusMaster;
	}

	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public Boolean gettTDCheck() {
		return tTDCheck;
	}

	public void settTDCheck(Boolean tTDCheck) {
		this.tTDCheck = tTDCheck;
	}

	public boolean getExportedToTtd() {
		return exportedToTtd;
	}

	public void setExportedToTtd(boolean exportedToTtd) {
		this.exportedToTtd = exportedToTtd;
	}

	public Integer getLoanInId() {
		return loanInId;
	}

	public void setLoanInId(Integer loanInId) {
		this.loanInId = loanInId;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	

	

}
