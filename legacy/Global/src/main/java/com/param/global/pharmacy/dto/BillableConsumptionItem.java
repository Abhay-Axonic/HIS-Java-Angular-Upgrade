package com.param.global.pharmacy.dto;

import java.util.Date;

import com.param.global.billing.dto.CommonBillingItemsDto;

public class BillableConsumptionItem extends CommonBillingItemsDto{



	private Integer billbleConsumptionDetailId;

	private Integer returnQuantity;
	
	private Integer  billableConsumptionId;


	public Integer getBillbleConsumptionDetailId() {
		return billbleConsumptionDetailId;
	}

	public void setBillbleConsumptionDetailId(Integer billbleConsumptionDetailId) {
		this.billbleConsumptionDetailId = billbleConsumptionDetailId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}


	public Date getDateAdministrationDate() {
		return dateAdministrationDate;
	}

	public void setDateAdministrationDate(Date dateAdministrationDate) {
		this.dateAdministrationDate = dateAdministrationDate;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCreditItemQuantity() {
		return creditItemQuantity;
	}

	public void setCreditItemQuantity(Integer creditItemQuantity) {
		this.creditItemQuantity = creditItemQuantity;
	}

	public Integer getBillableConsumptionId() {
		return billableConsumptionId;
	}

	public void setBillableConsumptionId(Integer billableConsumptionId) {
		this.billableConsumptionId = billableConsumptionId;
	}


}