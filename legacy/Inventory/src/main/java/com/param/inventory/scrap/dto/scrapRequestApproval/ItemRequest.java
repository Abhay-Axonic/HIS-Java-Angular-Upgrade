package com.param.inventory.scrap.dto.scrapRequestApproval;

import com.param.service.global.model.CookieObjectDto;

public class ItemRequest extends CookieObjectDto {

	protected Integer id;

	protected Integer itemId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer quantity;

	protected Integer statusId;
	
	protected Long batchId;

	protected Integer returnQuantity;

	protected Integer returnReasonId;
	
	protected String returnReason;    
	
	protected Integer rejectReasonId;
	
	protected String rejectNote;
	
	protected Integer rejectedQty;
	
	protected Integer acceptedQuantity;
	
	protected Integer scrapQuantity;
	
	protected Integer scrapReasonId;
	
	protected Double amountCop;

	public ItemRequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectedQty() {
		return rejectedQty;
	}

	public void setRejectedQty(Integer rejectedQty) {
		this.rejectedQty = rejectedQty;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getScrapQuantity() {
		return scrapQuantity;
	}

	public void setScrapQuantity(Integer scrapQuantity) {
		this.scrapQuantity = scrapQuantity;
	}

	public Integer getScrapReasonId() {
		return scrapReasonId;
	}

	public void setScrapReasonId(Integer scrapReasonId) {
		this.scrapReasonId = scrapReasonId;
	}

	public Double getAmountCop() {
		return amountCop;
	}

	public void setAmountCop(Double amountCop) {
		this.amountCop = amountCop;
	}

}
