package com.param.inventory.stockAdjustments.dto.stopTransaction;

import java.util.Date;
import java.util.List;

public class StopTransactionProcessResponse {
	
	protected Integer itemid;
	
	protected Integer batchid;
	
	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}

	protected Integer productId;
	
	protected Integer storeId;
	
	protected Date startdate;
	
	protected Date stopdate;
	
	protected Double days;
	
	protected Double hours;

	protected Double minutes;
	
	protected List<Integer> itemIds;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	public Date getStartDate() {
		return startdate;
	}

	public void setStartDate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getStopDate() {
		return stopdate;
	}

	public void setStopDate(Date stopdate) {
		this.stopdate = stopdate;
	}
	
	public Double getDays() {
		return days;
	}

	public void setDays(Double days) {
		this.days = days;
	}

	public Double getHours() {
		return hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	public Double getMinutes() {
		return minutes;
	}

	public void setMinutes(Double minutes) {
		this.minutes = minutes;
	}
	
	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public Integer getBatchid() {
		return batchid;
	}
	
}



