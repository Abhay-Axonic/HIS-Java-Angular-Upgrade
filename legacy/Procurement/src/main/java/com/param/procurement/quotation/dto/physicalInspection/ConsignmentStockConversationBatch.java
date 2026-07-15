package com.param.procurement.quotation.dto.physicalInspection;

import java.util.List;

import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;

public class ConsignmentStockConversationBatch {
	
	private Long batchId;
	
	private Integer uomTypeId;
	
	private Integer uomUnitId;
	
	private Integer consumableQuantity;
	
	private Integer statusId;
	
	private Integer billbleConsumptionDetailId;
	
	private Integer returnQuantity;
	
	protected Integer leaseQuantity;
	
	protected Double netAmount;
	
	protected Integer quantity;
	
	protected Double rate;
	
	protected Double taxAmount;
	
	protected Integer taxId;
	
	protected Double taxPercentage;
	
	protected Double totalAmount;
	
	protected Integer itemId;

	protected boolean isConsumed;

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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

	public Integer getConsumableQuantity() {
		return consumableQuantity;
	}

	public void setConsumableQuantity(Integer consumableQuantity) {
		this.consumableQuantity = consumableQuantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

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

	public Integer getLeaseQuantity() {
		return leaseQuantity;
	}

	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public boolean isConsumed() {
		return isConsumed;
	}

	public void setConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}
	
	

}
