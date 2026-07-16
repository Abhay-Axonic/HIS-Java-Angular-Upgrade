package com.param.entity.model.inventory;

import java.io.Serial;
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
import com.param.entity.model.master.Store;

@Entity(name = "StoreStock")
@Table(name = "t_store_stock", schema = "inventory")
public class StoreStock extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "available_qty")
	private Integer availableQty;
	@Column(name = "total_qty")
	private Integer totalQty;
	
	@Override
	public String toString() {
		return "StoreStock [id=" + id + ", availableQty=" + availableQty + ", totalQty=" + totalQty + ", store=" + store
				+ ", isConsignment=" + isConsignment + ", tQty=" + tQty + ", batch=" + batch + "]";
	}

	/*public Integer getTotalQty() {
		return totalQty;
	}*/
	public Integer getTotalQty() {
		return totalQty==null ? 0 : totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	@Column(name = "is_consignment")
	private Boolean isConsignment;	
	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	@Column(name = "t_qty")
	private Integer tQty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	public StoreStock() {
	}

	public StoreStock(Integer availableQty, Store store, Integer tQty, Batch batch) {
		super();
		this.availableQty = availableQty;
		this.store = store;
		this.tQty = tQty;
		this.batch = batch;
	}

	public StoreStock(Integer tQty) {
		super();
		this.tQty = tQty;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAvailableQty() {
		return availableQty==null?0:availableQty;
	}

	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Integer gettQty() {
		return (tQty==null)?0:tQty;
	}

	public void settQty(Integer tQty) {
		this.tQty = (tQty==null)?0:tQty;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public StoreStock(Integer availableQty, Store store, Batch batch) {
		super();
		this.availableQty = availableQty;
		this.store = store;
		this.batch = batch;
	}
	
	//added by minesh for converted ConsignmentBatch
	public StoreStock(Integer availableQty, Store store, Batch batch, Integer tQty) {
		super();
		this.availableQty = availableQty;
		this.store = store;
		this.batch = batch;
		this.tQty = tQty;
	}
	//end code by minesh
	
	public StoreStock(Integer id, Integer availableQty, Store store, Batch batch) {
		super();
		this.id = id;
		this.availableQty = availableQty;
		this.store = store;
		this.batch = batch;
	}

	public StoreStock(Integer id, Store store, Integer availableQty, Integer tQty, Batch batch) {
		super();
		this.id = id;
		this.store = store;
		this.availableQty = availableQty;
		this.tQty = tQty;
		this.batch = batch;
	}


}