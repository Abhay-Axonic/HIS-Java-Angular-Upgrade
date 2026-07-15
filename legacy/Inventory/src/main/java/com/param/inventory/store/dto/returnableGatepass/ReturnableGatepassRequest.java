package com.param.inventory.store.dto.returnableGatepass;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ReturnableGatepassRequest extends CookieObjectDto {
	
	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected Integer storeId;
	// Integer requestingStoreId;

	protected Integer vendorId;

	protected String vendor;

	protected Integer assetTypeId;

	protected Integer againstId;
	
	protected boolean isMultiBonusBatch;
	
	/*public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}*/

	protected Integer grnId;

	protected String gatepassNo;
	
	protected String referenceNo;

	protected Integer statusId;
	
	protected Integer approvalStatusId;
	
	protected Double totalAmount;
	
	protected Double totalTaxAmount;
	
	protected Double netAmount;
	
	protected String remark;

	protected Integer cancelReasonId;

	protected String cancelNote;
	
	protected List<Integer> gatepassId;
	
	protected List<ItemRequest> items;

	protected boolean checkReturnableGatePassFlag;

	protected Integer requestTypeId;

	protected Integer grnDetailId;

	protected Integer returnableGatePassDetailId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getAgainstId() {
		return againstId;
	}

	public void setAgainstId(Integer againstId) {
		this.againstId = againstId;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public String getGatepassNo() {
		return gatepassNo;
	}

	public void setGatepassNo(String gatepassNo) {
		this.gatepassNo = gatepassNo;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public List<Integer> getGatepassId() {
		return gatepassId;
	}

	public void setGatepassId(List<Integer> gatepassId) {
		this.gatepassId = gatepassId;
	}

	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}

	public boolean getIsMultiBonusBatch() {
		return isMultiBonusBatch;
	}

	public void setIsMultiBonusBatch(boolean isMultiBonusBatch) {
		this.isMultiBonusBatch = isMultiBonusBatch;
	}

	public boolean getCheckReturnableGatePassFlag() {
		return checkReturnableGatePassFlag;
	}

	public void setCheckReturnableGatePassFlag(boolean checkReturnableGatePassFlag) {
		this.checkReturnableGatePassFlag = checkReturnableGatePassFlag;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public Integer getReturnableGatePassDetailId() {
		return returnableGatePassDetailId;
	}

	public void setReturnableGatePassDetailId(Integer returnableGatePassDetailId) {
		this.returnableGatePassDetailId = returnableGatePassDetailId;
	}
}