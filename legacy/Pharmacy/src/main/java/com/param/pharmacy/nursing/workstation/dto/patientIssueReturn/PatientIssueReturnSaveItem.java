package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import com.param.service.global.model.CookieObjectDto;

public class PatientIssueReturnSaveItem extends CookieObjectDto
{
	private Integer batchMapperId;
	
	private Integer returnQuantity;
	
	private Integer returnReasonId;
	private Integer id;
	private Integer itemId;
	private Integer saleId;
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getReturnReasonId() {
		return returnReasonId;
	}

	public void setReturnReasonId(Integer returnReasonId) {
		this.returnReasonId = returnReasonId;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}
	
}
