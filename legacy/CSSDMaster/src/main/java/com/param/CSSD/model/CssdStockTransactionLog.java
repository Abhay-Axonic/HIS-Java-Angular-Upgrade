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
@Table(name = "t_cssd_stock_transaction_log", schema = "cssd")
@SequenceGenerator(name = "t_cssd_stock_transaction_seq", sequenceName = "cssd.t_cssd_stock_transaction_seq", allocationSize = 1)
public class CssdStockTransactionLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_cssd_stock_transaction_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "sterilized_quantity")
	private Integer sterilizedQuantity;
	
	@Column(name = "in_transit_quantity")
	private Integer inTransitQuantity;
	
	@Column(name = "not_returned_quantity")
	private Integer notReturnedQuantity;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;

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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getSterilizedQuantity() {
		return sterilizedQuantity;
	}

	public void setSterilizedQuantity(Integer sterilizedQuantity) {
		this.sterilizedQuantity = sterilizedQuantity;
	}

	public Integer getInTransitQuantity() {
		return inTransitQuantity;
	}

	public void setInTransitQuantity(Integer inTransitQuantity) {
		this.inTransitQuantity = inTransitQuantity;
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

	public Integer getNotReturnedQuantity() {
		return notReturnedQuantity;
	}

	public void setNotReturnedQuantity(Integer notReturnedQuantity) {
		this.notReturnedQuantity = notReturnedQuantity;
	}
}
