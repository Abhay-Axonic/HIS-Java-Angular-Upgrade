package com.param.global.dto.global;

import java.util.Date;

import com.param.service.global.model.CookieObjectDto;

public class DateRequest extends CookieObjectDto{
	protected Date fromDate;
	protected Date toDate;
	protected Integer assetTypeId;
	public Integer getAssetTypeId() {
		return assetTypeId;
	}
	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
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
}
