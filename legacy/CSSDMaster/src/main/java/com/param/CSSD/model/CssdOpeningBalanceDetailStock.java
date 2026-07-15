package com.param.CSSD.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.CSSDMaster.model.AutoclaveMaster;
import com.param.entity.model.inventory.OpeningBalanceImplant;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;


@Entity
@Table(name = "t_cssd_opening_balance_detail", schema = "cssd")
@SequenceGenerator(name = "t_cssd_opening_balance_detail_seq", sequenceName = "cssd.t_cssd_opening_balance_detail_seq", allocationSize = 1)
public class CssdOpeningBalanceDetailStock {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_cssd_opening_balance_detail_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "opening_balance_id")
	private Integer openingBalanceId;
	
	@Column(name = "item_id")
	private Integer instrumentId;
	
	@Column(name = "is_sterilize")
	private Boolean isSterilize;
	
	@Column(name = "sterilization_expiry_date")
	private Date sterilizationExpiryDate;
	
	@Column(name = "item_quantity")
	private Integer instrumentQuantity;
	
	@Column(name = "batch_no")
	private String batchNo;
	
	@Column(name = "status_id")
	private Integer statusId;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "added_by")
	private Integer addedBy;
	
	@Column(name = "added_date")
	private Date addedDate;
	
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	
	@Column(name = "approved_by")
	private Integer approvedBy;
	
	@Column(name = "approved_date")
	private Date approveDate;
	
	@Column(name = "is_kit")
	private Character isKit;
	
	@Column(name = "kit_id")
	private Integer kitId;
	
	@Column(name = "kit_quantity")
	private Integer kitQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opening_balance_id", insertable = false, updatable = false)
	private CssdOpeningBalanceStock cssdOpeningBalanceStock;
	

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private Item item;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOpeningBalanceId() {
		return openingBalanceId;
	}

	public void setOpeningBalanceId(Integer openingBalanceId) {
		this.openingBalanceId = openingBalanceId;
	}

	public Integer getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}


	public Boolean getIsSterilize() {
		return isSterilize;
	}

	public void setIsSterilize(Boolean isSterilize) {
		this.isSterilize = isSterilize;
	}



	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}


	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public Integer getInstrumentQuantity() {
		return instrumentQuantity;
	}

	public void setInstrumentQuantity(Integer instrumentQuantity) {
		this.instrumentQuantity = instrumentQuantity;
	}

	public Date getSterilizationExpiryDate() {
		return sterilizationExpiryDate;
	}

	public void setSterilizationExpiryDate(Date sterilizationExpiryDate) {
		this.sterilizationExpiryDate = sterilizationExpiryDate;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public CssdOpeningBalanceStock getCssdOpeningBalanceStock() {
		return cssdOpeningBalanceStock;
	}

	public void setCssdOpeningBalanceStock(CssdOpeningBalanceStock cssdOpeningBalanceStock) {
		this.cssdOpeningBalanceStock = cssdOpeningBalanceStock;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Character getIsKit() {
		return isKit;
	}

	public void setIsKit(Character isKit) {
		this.isKit = isKit;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getKitQuantity() {
		return kitQuantity;
	}

	public void setKitQuantity(Integer kitQuantity) {
		this.kitQuantity = kitQuantity;
	}
	
	
}
