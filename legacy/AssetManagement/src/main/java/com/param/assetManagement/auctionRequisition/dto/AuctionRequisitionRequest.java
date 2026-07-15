package com.param.assetManagement.auctionRequisition.dto;

import java.util.Date;

import com.param.service.global.model.CookieObjectDto;

public class AuctionRequisitionRequest extends CookieObjectDto{

	protected Date fromDate;

	protected Date toDate;
	
	protected Integer storeId;

	protected Integer statusId;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
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
	
}
