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
import com.param.entity.model.master.Rack;
import com.param.entity.model.master.Shelf;
import com.param.entity.model.master.Store;
import com.param.utility.TimeZoneComponent;

@Entity(name = "OpeningBalShelfItem")
@Table(name = "t_opening_bal_shelf_item", schema = "inventory")
public class OpeningBalShelfItem extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opening_balance_details_id")
	private OpeningBalanceDetail openingBalanceDetail;

	public OpeningBalanceDetail getOpeningBalanceDetail() {
		return openingBalanceDetail;
	}

	public void setOpeningBalanceDetail(OpeningBalanceDetail openingBalanceDetail) {
		this.openingBalanceDetail = openingBalanceDetail;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shelf_id")
	private Shelf shelf;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rack_id")
	private Rack rack;

	@Column(name = "remark")
	private String remark;

	public OpeningBalShelfItem() {
		super();
	}

	public OpeningBalShelfItem(Store store, Shelf shelf, Rack rack, OpeningBalanceDetail openingBalanceDetail) {
		super();
		this.store = store;
		this.shelf = shelf;
		this.rack = rack;
		this.openingBalanceDetail = openingBalanceDetail;
	}

	public OpeningBalShelfItem(Shelf shelf, Store store, Rack rack, String remark , OpeningBalanceDetail openingBalanceDetail) {
		this(store, shelf, rack, openingBalanceDetail);
	//	this.batch = batch;
		this.remark = remark;
	}

	public void updateShelfItem(Shelf shelf, Rack rack, Boolean status,  Integer userId) {
		this.shelf = shelf;
		this.rack = rack;
		this.isActive = status;
	//	this.updatedBy = this.getUser();
		this.updatedByUser =userId;
		this.updatedDate = TimeZoneComponent.getDateByzone(0);
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	

	public Shelf getShelf() {
		return shelf;
	}

	public void setShelf(Shelf shelf) {
		this.shelf = shelf;
	}

	public Rack getRack() {
		return rack;
	}

	public void setRack(Rack rack) {
		this.rack = rack;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}