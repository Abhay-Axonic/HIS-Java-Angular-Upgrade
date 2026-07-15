package com.param.inventory.store.dto;

import java.util.Date;
import java.util.List;

import com.param.inventory.stock.dto.openingBalance.ItemRequest;
import com.param.inventory.stock.dto.openingBalance.ItemRequestNew;
import com.param.service.global.model.CookieObjectDto;

public class MaterialInwardRequestNew extends CookieObjectDto{


	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected Integer storeId;
	//protected Integer requestingStoreId;

	/*public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}*/
	protected Integer vendorId;

	public Integer getReturnableGetPassId() {
		return returnableGetPassId;
	}

	public void setReturnableGetPassId(Integer returnableGetPassId) {
		this.returnableGetPassId = returnableGetPassId;
	}

	public List<ItemRequestNew> getItem() {
		return item;
	}

	public void setItem(List<ItemRequestNew> item) {
		this.item = item;
	}

	protected Integer assetTypeId;

	protected Integer statusId;

	protected Integer approvalStatusId;

	protected Integer requestTypeId;

	protected String inwardNo;

	protected Integer returnableGetPassId;

	//protected Double totalAmount;

  //	protected Double totalTaxAmount;

	//protected Double totalNetAmount;

	protected String remark;

	protected String documentNo;

	protected Date documentDate;

	protected Integer cancelReasonId;

	protected String cancelNote;
	protected int replacementId;

	protected Double totalAmount;

	protected Double totalTaxAmount;

	protected Double netAmount;

	protected Double returnAmt;

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

	public Double getReturnAmt() {
		return returnAmt;
	}

	public void setReturnAmt(Double returnAmt) {
		this.returnAmt = returnAmt;
	}

	public int getReplacementId() {
		return replacementId;
	}

	public void setReplacementId(int replacementId) {
		this.replacementId = replacementId;
	}

	protected List<ItemRequestNew> item;

	protected List<Integer> itemsId;

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

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
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

	public String getInwardNo() {
		return inwardNo;
	}

	public void setInwardNo(String inwardNo) {
		this.inwardNo = inwardNo;
	}

	
	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
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

	public List<Integer> getItemsId() {
		return itemsId;
	}

	public void setItemsId(List<Integer> itemsId) {
		this.itemsId = itemsId;
	}


}
