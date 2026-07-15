package com.param.assetManagement.itemForAuction.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class ItemAuctionResponse {

	protected Integer id;

	protected String requestNo;
	
	protected Date requestDate;

	protected String requisitionNo;
	
	protected Integer storeId;

	protected String storeName;

	protected String requestedBy;

	protected Integer statusId;

	protected String status;
	
	protected List<ItemAuctionDetails> items;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getRequisitionNo() {
		return requisitionNo;
	}

	public void setRequisitionNo(String requisitionNo) {
		this.requisitionNo = requisitionNo;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ItemAuctionDetails> getItems() {
		return items;
	}

	public void setItems(List<ItemAuctionDetails> items) {
		this.items = items;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public ItemAuctionResponse() {
		super();
	}

	public ItemAuctionResponse(Integer id, Date requestDate, String requestNo, String requisitionNo, Integer storeId,
			String storeName, String requestedBy, Integer statusId, String status) {
		super();
		this.id = id;
		this.requestDate = requestDate;
		this.requestNo = requestNo;
		this.requisitionNo = requisitionNo;
		this.storeId = storeId;
		this.storeName = storeName;
		this.requestedBy = requestedBy;
		this.statusId = statusId;
		this.status = status;
	}

}