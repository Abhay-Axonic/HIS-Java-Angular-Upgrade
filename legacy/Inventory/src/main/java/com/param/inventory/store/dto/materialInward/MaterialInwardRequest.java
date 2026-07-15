package com.param.inventory.store.dto.materialInward;

import java.util.Date;
import java.util.List;

import com.param.inventory.stock.dto.openingBalance.ItemRequest;
import com.param.service.global.model.CookieObjectDto;

public class MaterialInwardRequest extends CookieObjectDto{

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

	protected Integer assetTypeId;

	protected Integer statusId;

	protected Integer approvalStatusId;

	protected Integer requestTypeId;

	protected String inwardNo;

	protected Integer returnableGetPassId;

	protected Double totalAmount;

	protected Double totalTaxAmount;

	protected Double totalNetAmount;

	protected String remark;

	protected String documentNo;

	protected Date documentDate;

	protected Integer cancelReasonId;

	protected String cancelNote;
	protected int replacementId;

	public int getReplacementId() {
		return replacementId;
	}

	public void setReplacementId(int replacementId) {
		this.replacementId = replacementId;
	}

	protected List<ItemRequest> item;

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

	public Integer getReturnableGetPassId() {
		return returnableGetPassId;
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

	public void setReturnableGetPassId(Integer returnableGetPassId) {
		this.returnableGetPassId = returnableGetPassId;
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

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ItemRequest> getItem() {
		return item;
	}

	public void setItem(List<ItemRequest> item) {
		this.item = item;
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
