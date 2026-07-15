package com.param.global.pharmacy.dto;

import java.util.List;

import com.param.global.billing.dto.PharmacyBillCommonDto;

public class BillableConsumptionSave extends PharmacyBillCommonDto
{
	private Integer cookieStoreId;
	
	private Integer wardId;
	
	private Integer bedId;
	
	private Integer statusId;
	
	private Integer approvedStatusId;
	
	private Integer billableConsumptionId;
	
	private Integer billableConsumptionDetailsId;
	
	private Integer itemId;
	
	private Integer consumebleQty;
	
	private String consumableNumber;
	
	private List<BillableConsumptionItem> items;
	  
	private Integer orderId;
	

	public Integer getCookieStoreId() {
		return cookieStoreId;
	}

	public void setCookieStoreId(Integer cookieStoreId) {
		this.cookieStoreId = cookieStoreId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}


	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovedStatusId() {
		return approvedStatusId;
	}

	public void setApprovedStatusId(Integer approvedStatusId) {
		this.approvedStatusId = approvedStatusId;
	}

	public Integer getBillableConsumptionId() {
		return billableConsumptionId;
	}

	public void setBillableConsumptionId(Integer billableConsumptionId) {
		this.billableConsumptionId = billableConsumptionId;
	}

	public List<BillableConsumptionItem> getItems() {
		return items;
	}

	public void setItems(List<BillableConsumptionItem> items) {
		this.items = items;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getBillableConsumptionDetailsId() {
		return billableConsumptionDetailsId;
	}

	public void setBillableConsumptionDetailsId(Integer billableConsumptionDetailsId) {
		this.billableConsumptionDetailsId = billableConsumptionDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getConsumebleQty() {
		return consumebleQty;
	}

	public void setConsumebleQty(Integer consumebleQty) {
		this.consumebleQty = consumebleQty;
	}

	public String getConsumableNumber() {
		return consumableNumber;
	}

	public void setConsumableNumber(String consumableNumber) {
		this.consumableNumber = consumableNumber;
	}

	
}