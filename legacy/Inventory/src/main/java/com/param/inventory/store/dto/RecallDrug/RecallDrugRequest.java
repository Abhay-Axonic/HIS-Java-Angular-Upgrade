package com.param.inventory.store.dto.RecallDrug;

import java.util.Date;

import com.param.service.global.model.CookieObjectDto;

public class RecallDrugRequest extends CookieObjectDto {

	protected Date fromDate;

	protected Date toDate;

	protected Integer storeId;

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

}
