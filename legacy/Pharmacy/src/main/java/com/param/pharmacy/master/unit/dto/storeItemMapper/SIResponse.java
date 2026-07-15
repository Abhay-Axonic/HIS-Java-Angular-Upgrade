package com.param.pharmacy.master.unit.dto.storeItemMapper;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.param.service.global.model.CookieObjectDto;

public class SIResponse extends CookieObjectDto{

	protected BigInteger id;
	
	protected Integer itemId;
	
	protected Integer storeId;
	
	protected Integer minQty;

	protected Integer maxQty;
	
	protected Boolean isSellable;
	protected Integer userId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	protected Date fromDate;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getIsSellable() {
		return isSellable;
	}

	public void setIsSellable(Boolean isSellable) {
		this.isSellable = isSellable;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}
	
}
