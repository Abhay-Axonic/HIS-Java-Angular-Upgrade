package com.param.pharmacy.master.unit.dto.rack;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public abstract class RackResponse extends CookieObjectDto{

	protected Integer id;

	protected Integer storeId;

	protected String storeName;

	protected String rackCode;

	protected String rackName;

	protected Boolean status;

	protected List<ShelfRequest> items;

	public RackResponse() {
		super();
	}

	public RackResponse(Integer id, Integer storeId, String storeName, String rackCode, String rackName,
			Boolean status) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.storeName = storeName;
		this.rackCode = rackCode;
		this.rackName = rackName;
		this.status = status;
	}

	public List<ShelfRequest> getItems() {
		return items;
	}

	public void setItems(List<ShelfRequest> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getRackCode() {
		return rackCode;
	}

	public void setRackCode(String rackCode) {
		this.rackCode = rackCode;
	}

	public String getRackName() {
		return rackName;
	}

	public void setRackName(String rackName) {
		this.rackName = rackName;
	}


	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void addItems(ShelfRequest item) {
		if (this.items == null)
			this.items = new ArrayList<ShelfRequest>();

		this.items.add(item);
	}

}

/*
 * "id": 1, "storeId": 1, "storeName": "Store1", "rackId":1, "rackCode":"RC1",
 * "rackName":"Rack1", "status": true,
 */