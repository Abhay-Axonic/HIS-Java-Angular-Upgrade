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
	@NamedQuery(name="SEARCH_BLOOD_BANK_STOCK_DETAILS",
	query="SELECT mstr.bloodBankStockDetailsId as bloodBankStockDetailsId, "
				+ " mstr.availableStock as availableStock "
				+ " FROM BloodBankStockDetails mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodBankId= :bloodBankId "
				+ " AND mstr.bloodGroupId= :bloodGroupId "
				+ " AND mstr.bloodBagCapacityId= :bloodBagCapacityId "
				+ " AND mstr.bloodComponentId= :bloodComponentId "
				+ " AND mstr.bloodBagTypeId= :bloodBagTypeId "),
	
	@NamedQuery(name="GET_BLOOD_BANK_STOCK_BY_COMPONENTID",
	query="SELECT mstr.bloodBankStockDetailsId as bloodBankStockDetailsId, "
				+ " mstr.availableStock as availableStock ,"
				+ " bloodBagCapacityMaster.bloodBagCapacityDesc as bloodBagCapacityName, "
				+ " bloodBagTypeMaster.bloodBagTypeDesc as bloodBagTypeName, "
				+ " bloodGroupMaster.bloodGroupDesc as bloodGroupName, "
				+ " bloodBankMaster.bloodBankDesc as bloodBankName "
				+ " FROM BloodBankStockDetails mstr "
				+ " INNER JOIN mstr.bloodBankMaster bloodBankMaster "
				+ " INNER JOIN mstr.bloodBagCapacityMaster bloodBagCapacityMaster "
				+ " INNER JOIN mstr.bloodBagTypeMaster bloodBagTypeMaster "
				+ " INNER JOIN mstr.bloodGroupMaster bloodGroupMaster "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.bloodComponentId= :bloodComponentId ")
})
@Entity
@Table(name="t_blood_bank_stock_details", schema="blood_bank")
@SequenceGenerator(name="t_blood_bank_stock_details_seq", sequenceName="blood_bank.t_blood_bank_stock_details_seq", allocationSize= 1)
public class BloodBankStockDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_blood_bank_stock_details_seq")
	
	@Column(name = "blood_bank_stock_details_id")
	private Integer bloodBankStockDetailsId;
	
	@Column(name = "blood_bank_id")
	private Integer bloodBankId;
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "blood_bag_capacity_id")
	private Integer bloodBagCapacityId;
	
	@Column(name = "blood_bag_type_id")
	private Integer bloodBagTypeId;
	
	@Column(name = "blood_component_id ")
	private Integer bloodComponentId ;
	
	@Column(name = "available_stock")
	private Integer availableStock ;
	
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

	public Integer getBloodBankStockDetailsId() {
		return bloodBankStockDetailsId;
	}

	public void setBloodBankStockDetailsId(Integer bloodBankStockDetailsId) {
		this.bloodBankStockDetailsId = bloodBankStockDetailsId;
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

	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
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
	
}
