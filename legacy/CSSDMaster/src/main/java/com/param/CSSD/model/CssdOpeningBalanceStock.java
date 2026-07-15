package com.param.CSSD.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;


@Entity
@Table(name = "t_cssd_opening_balance", schema = "cssd")
@SequenceGenerator(name = "t_cssd_opening_balance_seq", sequenceName = "cssd.t_cssd_opening_balance_seq", allocationSize = 1)
public class CssdOpeningBalanceStock {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_cssd_opening_balance_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "store_id")
	private Integer storeId;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", insertable = false, updatable = false)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;

	@Column(name = "cancel_note")
	private String cancelNote;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "added_by", insertable = false, updatable = false)
	private UserMaster userMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id", insertable = false, updatable = false)
	private CancelReason cancelReason;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cssdOpeningBalanceStock", cascade = CascadeType.ALL)
	private List<CssdOpeningBalanceDetailStock> cssdOpeningBalanceDetailStockList;
	
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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


	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<CssdOpeningBalanceDetailStock> getCssdOpeningBalanceDetailStockList() {
		return cssdOpeningBalanceDetailStockList;
	}

	public void setCssdOpeningBalanceDetailStockList(
			List<CssdOpeningBalanceDetailStock> cssdOpeningBalanceDetailStockList) {
		this.cssdOpeningBalanceDetailStockList = cssdOpeningBalanceDetailStockList;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	
	
	
}
