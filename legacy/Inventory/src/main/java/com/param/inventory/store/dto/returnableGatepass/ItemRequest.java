package com.param.inventory.store.dto.returnableGatepass;

import java.util.List;

import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.inventory.store.dto.materialInward.MaterialInwardAccessoriesRequest;

public class ItemRequest {

	protected Integer id;

	protected Long batchId;

	protected Integer vendorId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer taxId;

	protected Double taxPercentage;

	protected Integer returnQuantity;
	protected Integer returnedQuantity;

	protected Double totalAmount;
	protected Double lcop;
	protected Double totalDiscount;
    protected List<GrnBonusBatchExpiry> grnBonusBatchExpiry;
    protected List<returnableBonusBatchExpiry> returnableBonusBatchExpiry; 
	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	protected Double totalTaxAmount;

	protected Double netAmount;

	protected Integer statusId;

	protected Integer approvedQuantity;

	protected Integer rejectedQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Integer grnDetailId;
	protected Integer bonusUomTypeId;
	public List<accessoriesReuest> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<accessoriesReuest> accessories) {
		this.accessories = accessories;
	}

	protected List<accessoriesReuest> accessories;
	
	

	public Integer getId() {
		return id;
	}

	

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
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

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	public List<GrnBonusBatchExpiry> getGrnBonusBatchExpiry() {
		return grnBonusBatchExpiry;
	}

	public void setGrnBonusBatchExpiry(List<GrnBonusBatchExpiry> grnBonusBatchExpiry) {
		this.grnBonusBatchExpiry = grnBonusBatchExpiry;
	}

	public List<returnableBonusBatchExpiry> getReturnableBonusBatchExpiry() {
		return returnableBonusBatchExpiry;
	}

	public void setReturnableBonusBatchExpiry(List<returnableBonusBatchExpiry> returnableBonusBatchExpiry) {
		this.returnableBonusBatchExpiry = returnableBonusBatchExpiry;
	}

	public Integer getBonusUomTypeId() {
		return bonusUomTypeId;
	}

	public void setBonusUomTypeId(Integer bonusUomTypeId) {
		this.bonusUomTypeId = bonusUomTypeId;
	}


}