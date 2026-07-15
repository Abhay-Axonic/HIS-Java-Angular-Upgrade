package com.param.pharmacy.nursing.crashCart.dto;

import com.param.global.billing.dto.CommonBillingItemsDto;

public class ItemRequest extends CommonBillingItemsDto{

	//private Integer id;

	//private Integer itemId;

	//private Integer uomTypeId;

	//private Integer uomUnitId;

	private Integer requiredQuantity;

	//private Integer statusId;

	protected Integer rejectQuantity;

	protected Integer approvedQuantity;

	protected Integer reasonId;

	protected String note;

	//protected Long batchId;

	protected Integer consumedQuantity;

	public Integer getConsumedQuantity() {
		return consumedQuantity;
	}

	public void setConsumedQuantity(Integer consumedQuantity) {
		this.consumedQuantity = consumedQuantity;
	}

	/*public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}*/

	/*public Integer getItemId() {
		return itemId;
	}
	
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}*/

	/*public Integer getUomTypeId() {
		return uomTypeId;
	}
	
	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}*/

	/*public Integer getUomUnitId() {
		return uomUnitId;
	}
	
	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}*/

	public Integer getRequiredQuantity() {
		return requiredQuantity;
	}

	public void setRequiredQuantity(Integer requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}

	/*public Integer getStatusId() {
		return statusId;
	}
	
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}*/

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/*public Long getBatchId() {
		return batchId;
	}
	
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}*/

}
