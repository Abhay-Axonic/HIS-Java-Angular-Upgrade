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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "t_blood_bag_loan_out_details", schema = "blood_bank")
@SequenceGenerator(name = "blood_bag_loan_out_details_seq", sequenceName = "blood_bank.blood_bag_loan_out_details_seq", allocationSize = 1)
public class BloodBagLoanOutDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_bag_loan_out_details_seq")
	@Column(name = "blood_bag_loan_out_details_id")
	private Integer bloodBagLoanOutDetailsId;
	
	@Column(name = "blood_bag_loan_out_id")
	private Integer bloodBagLoanOutId;
	
	@Column(name = "blood_bank_id")
	private Integer bloodBankId;
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "bag_type_id")
	private Integer bagTypeId;
	
	@Column(name = "bag_capacity_id")
	private Integer bagCapacityId;
	
	@Column(name = "component_id")
	private Integer componentId;
	
	@Column(name = "blood_bag_no")
	private String bloodBagNo;

	@Column(name = "segment_no")
	private String segmentNo;

	@Column(name = "batch_no")
	private String batchNo;
	
	@Column(name = "short_name")
	private String shortName;
	
	@Column(name = "bag_unit_no")
	private Integer bagUnitNo;
	
	@Column(name = "loan_out_status_id")
	private Integer loanOutStatusId;
	
	@Column(name = "barcode")
	private String barcode;
	
	@Column(name = "remark")
	private String remark;
	
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
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "blood_bag_stock_id")
	private Integer bloodBagStockId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_bank_id", insertable = false, updatable = false)
	private BloodBankMaster bloodBankMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bag_capacity_id", insertable = false, updatable = false)
	private BloodBagCapacityMaster bloodBagCapacityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bag_type_id", insertable = false, updatable = false)
	private BloodBagTypeMaster bloodBagTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id", insertable = false, updatable = false)
	private BloodComponentMaster bloodComponentMaster;

	public Integer getBloodBagLoanOutDetailsId() {
		return bloodBagLoanOutDetailsId;
	}

	public void setBloodBagLoanOutDetailsId(Integer bloodBagLoanOutDetailsId) {
		this.bloodBagLoanOutDetailsId = bloodBagLoanOutDetailsId;
	}

	public Integer getBloodBagLoanOutId() {
		return bloodBagLoanOutId;
	}

	public void setBloodBagLoanOutId(Integer bloodBagLoanOutId) {
		this.bloodBagLoanOutId = bloodBagLoanOutId;
	}

	public Integer getBloodBankId() {
		return bloodBankId;
	}

	public void setBloodBankId(Integer bloodBankId) {
		this.bloodBankId = bloodBankId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Integer getBagTypeId() {
		return bagTypeId;
	}

	public void setBagTypeId(Integer bagTypeId) {
		this.bagTypeId = bagTypeId;
	}

	public Integer getBagCapacityId() {
		return bagCapacityId;
	}

	public void setBagCapacityId(Integer bagCapacityId) {
		this.bagCapacityId = bagCapacityId;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public String getSegmentNo() {
		return segmentNo;
	}

	public void setSegmentNo(String segmentNo) {
		this.segmentNo = segmentNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getBagUnitNo() {
		return bagUnitNo;
	}

	public void setBagUnitNo(Integer bagUnitNo) {
		this.bagUnitNo = bagUnitNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getLoanOutStatusId() {
		return loanOutStatusId;
	}

	public void setLoanOutStatusId(Integer loanOutStatusId) {
		this.loanOutStatusId = loanOutStatusId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getBloodBagStockId() {
		return bloodBagStockId;
	}

	public void setBloodBagStockId(Integer bloodBagStockId) {
		this.bloodBagStockId = bloodBagStockId;
	}

	
}
