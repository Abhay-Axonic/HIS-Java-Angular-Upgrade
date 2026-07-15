package com.param.inventory.store.dto.RecallDrug;

import java.util.Date;

public class RecallDrugResponse {

	protected Integer id;

	protected Date drugRecallDate;

	protected String itemCode;

	protected String itemName;

	protected String recallAgainst;

	protected String batchNo;

	protected Date expiryDate;

	protected Integer availableQuantity;

	protected String reason;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDrugRecallDate() {
		return drugRecallDate;
	}

	public void setDrugRecallDate(Date drugRecallDate) {
		this.drugRecallDate = drugRecallDate;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getRecallAgainst() {
		return recallAgainst;
	}

	public void setRecallAgainst(String recallAgainst) {
		this.recallAgainst = recallAgainst;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
