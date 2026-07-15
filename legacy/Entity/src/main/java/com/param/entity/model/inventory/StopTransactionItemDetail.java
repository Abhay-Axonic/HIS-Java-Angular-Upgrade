package com.param.entity.model.inventory;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

/**
 * The persistent class for the t_stop_transaction_item_detail database table.
 * 
 */
@Entity
@Table(name = "t_stop_transaction_item_detail", schema = "inventory")
public class StopTransactionItemDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;
		
	@Column(name = "item_id", nullable = false)
	private Integer itemId; 	

	// bi-directional many-to-one association to TStopTransaction
	@ManyToOne
	@JoinColumn(name = "stop_transaction_id")
	private StopTransaction StopTransaction;

	public StopTransactionItemDetail() {
	}

//	public StopTransactionItemDetail(Batch batch, Status status, UomType uomType, UomUnit uomUnit) {
//		super();
//		this.batch = batch;
//		this.status = status;
//		this.uomType = uomType;
//		this.uomUnit = uomUnit;
//	}
	public StopTransactionItemDetail(Batch batch, Status status, UomType uomType, UomUnit uomUnit,Integer itemId) {
		super();
		this.batch = batch;
		this.status = status;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.itemId = itemId;
	} 

	

	public StopTransactionItemDetail(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public StopTransactionItemDetail(UomType uomType) {
		this.uomType = uomType;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Batch getBatch() {
		return this.batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatusId(Status status) {
		this.status = status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public StopTransaction getStopTransaction() {
		return this.StopTransaction;
	}

	public void setStopTransaction(StopTransaction StopTransaction) {
		this.StopTransaction = StopTransaction;
	}

	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
}