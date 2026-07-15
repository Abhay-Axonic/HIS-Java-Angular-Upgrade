package com.param.inventory.stock.dto.openingBalance;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.UomResponse;
import com.param.inventory.store.dto.materialInward.MaterialInwardAccessoriesRequest;

public class ItemRequestNew {

	protected Integer id;

	protected Integer itemId;
	
	protected Integer manufacturerId;

	protected Integer opbUomId;

	protected Integer mrpUomTypeId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected String batchNo;

	protected Date expiryDate;

	protected Integer quantity;

	protected Double cop;

	protected Double mrp;

	protected Double copAmount;

	protected Double markup;

	protected Integer taxId;

	protected Double taxPercent;
	
	protected Double markupPercent;

	protected Double taxAmount;

	protected Double netAmount;

	protected Double totalTaxAmount;

	protected Double totalAmount;

	protected Double totalNetAmount;

	protected String remark;

	protected String specification;

	protected Double creditNoteAmount;

	protected Double creditTaxAmount;

	protected Double creditNetAmount;

	protected Integer statusId;
	protected Integer returnableGetPassId;

	//protected Double totalAmount;

	//protected Double totalTaxAmount;

	//protected Double totalNetAmount;
	protected Integer returnableGetPassDetailId;
	/* start code for resolved HIS-5496 Material Inward - Replacement - Not working */
	protected Integer returnableGatepassDetailId;
	public Integer getReturnableGatepassDetailId() {
		return returnableGatepassDetailId;
	}
	public void setReturnableGatepassDetailId(Integer returnableGatepassDetailId) {
		this.returnableGatepassDetailId = returnableGatepassDetailId;
	}
	/* end code for resolved HIS-5496 Material Inward - Replacement - Not working */

	protected List<UomResponse> uom;
	
	 private List<StorageDetails> storageDetails;

	protected Integer rejectReasonId;

	protected Integer rejectQuantity;

	protected String rejectNote;

	protected Integer approvedQuantity;

	protected Integer receivedQuantity;

	protected Integer returnQuantity;

	protected Double returnAmount;

	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

	protected Long batchId;
	
	protected List<MaterialInwardAccessoriesRequest> materialInwardAccessoriesRequest;

	

	public List<MaterialInwardAccessoriesRequest> getMaterialInwardAccessoriesRequest() {
		return materialInwardAccessoriesRequest;
	}

	public void setMaterialInwardAccessoriesRequest(
			List<MaterialInwardAccessoriesRequest> materialInwardAccessoriesRequest) {
		this.materialInwardAccessoriesRequest = materialInwardAccessoriesRequest;
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

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Double getCreditNoteAmount() {
		return creditNoteAmount;
	}

	public void setCreditNoteAmount(Double creditNoteAmount) {
		this.creditNoteAmount = creditNoteAmount;
	}

	public Double getCreditTaxAmount() {
		return creditTaxAmount;
	}

	public void setCreditTaxAmount(Double creditTaxAmount) {
		this.creditTaxAmount = creditTaxAmount;
	}

	public Double getCreditNetAmount() {
		return creditNetAmount;
	}

	public void setCreditNetAmount(Double creditNetAmount) {
		this.creditNetAmount = creditNetAmount;
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

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Double getMarkupPercent() {
		return markupPercent;
	}

	public void setMarkupPercent(Double markupPercent) {
		this.markupPercent = markupPercent;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOpbUomId() {
		return opbUomId;
	}

	public void setOpbUomId(Integer opbUomId) {
		this.opbUomId = opbUomId;
	}

	public Integer getMrpUomTypeId() {
		return mrpUomTypeId;
	}

	public void setMrpUomTypeId(Integer mrpUomTypeId) {
		this.mrpUomTypeId = mrpUomTypeId;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getCopAmount() {
		return copAmount;
	}

	public void setCopAmount(Double copAmount) {
		this.copAmount = copAmount;
	}

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(Double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getReturnableGetPassDetailId() {
		return returnableGetPassDetailId;
	}

	public void setReturnableGetPassDetailId(Integer returnableGetPassDetailId) {
		this.returnableGetPassDetailId = returnableGetPassDetailId;
	}

	public Integer getReturnableGetPassId() {
		return returnableGetPassId;
	}

	public void setReturnableGetPassId(Integer returnableGetPassId) {
		this.returnableGetPassId = returnableGetPassId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public List<StorageDetails> getStorageDetails() {
		return storageDetails;
	}

	public void setStorageDetails(List<StorageDetails> storageDetails) {
		this.storageDetails = storageDetails;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	


}
