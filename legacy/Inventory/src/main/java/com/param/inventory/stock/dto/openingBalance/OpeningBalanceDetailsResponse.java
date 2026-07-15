package com.param.inventory.stock.dto.openingBalance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.global.dto.global.TaxDropDown;

public class OpeningBalanceDetailsResponse {

	protected Integer id;

	protected Date opbDate;

	protected String opbNo;

	protected Integer storeId;

	protected String store;

	protected String requestedBy;
	
	protected String status;
	
	protected Integer statusId;

	protected List<ItemResponse> items;
	
	List<RackResponse> racks;


	public OpeningBalanceDetailsResponse() {
		super();
	}

	public OpeningBalanceDetailsResponse(Integer id, Date opbDate, String opbNo, Integer storeId, String store) {
		super();
		this.id = id;
		this.opbDate = opbDate;
		this.opbNo = opbNo;
		this.storeId = storeId;
		this.store = store;
	}

	public OpeningBalanceDetailsResponse(Integer id, Date opbDate, String opbNo, Integer storeId, String store,
			String requestedBy, String status, Integer statusId) {
		super();
		this.id = id;
		this.opbDate = opbDate;
		this.opbNo = opbNo;
		this.storeId = storeId;
		this.store = store;
		this.requestedBy = requestedBy;
		this.status = status;
		this.statusId = statusId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getOpbDate() {
		return opbDate;
	}

	public void setOpbDate(Date opbDate) {
		this.opbDate = opbDate;
	}

	public String getOpbNo() {
		return opbNo;
	}

	public void setOpbNo(String opbNo) {
		this.opbNo = opbNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return store;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public List<ItemResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemResponse> items) {
		this.items = items;
	}

	public void addItems(ItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ItemResponse>();

		this.items.add(item);
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
		
	public List<RackResponse> getRacks() {
		return racks;
	}

	public void setRacks(List<RackResponse> racks) {
		this.racks = racks;
	}

	public void add(ItemResponse itemresponse)
	{
		if (this.items == null)
			this.items = new ArrayList<ItemResponse>();

		this.items.add(itemresponse);

		
	}

}

/*
 * "id": "1", "opbDate": "2018-12-31", "opbNo": "OPB-1", "storeId": "1",
 * "storeName": "Pharmacy Store",
 */
