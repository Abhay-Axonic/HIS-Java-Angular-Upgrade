package com.param.inventory.stockAdjustments.dto.stopTransaction;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class StopTransactionRequest extends CookieObjectDto {

	protected Integer storeId;

	protected Integer statusId;

	protected String startDateTime;

	protected String stopDateTime;

	protected Date fromDate;

	protected Date toDate;

	protected Integer id;

	protected Boolean isVital;

	protected Boolean isEssential;

	protected Boolean isDiseriable;

	protected Boolean isA;

	protected Boolean isB;

	protected Boolean isC;

	protected String remark;

	protected Integer assetTypeId;

	protected List<Integer> vedId;

	protected List<StopTransactionItemRequest> items;

	protected List<StopTransactionProductExclusionRequest> productCategoryExclusion;

	protected List<StopTransactionItemExclusionRequest> itemExclusion;

	protected Integer cancelReasonId;

	protected String cancelNote;

	protected List<Integer> stopTransactionId;
	
	protected List<Long> batchId;

	protected List<Integer> itemIds;
	
	protected Integer productId;
	
	protected Integer itemId;	
	protected String itemName;
	protected String batchNo;
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getStopDateTime() {
		return stopDateTime;
	}

	public void setStopDateTime(String stopDateTime) {
		this.stopDateTime = stopDateTime;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsVital() {
		return isVital;
	}

	public void setIsVital(Boolean isVital) {
		this.isVital = isVital;
	}

	public Boolean getIsEssential() {
		return isEssential;
	}

	public void setIsEssential(Boolean isEssential) {
		this.isEssential = isEssential;
	}

	public Boolean getIsDiseriable() {
		return isDiseriable;
	}

	public void setIsDiseriable(Boolean isDiseriable) {
		this.isDiseriable = isDiseriable;
	}

	public Boolean getIsA() {
		return isA;
	}

	public void setIsA(Boolean isA) {
		this.isA = isA;
	}

	public Boolean getIsB() {
		return isB;
	}

	public void setIsB(Boolean isB) {
		this.isB = isB;
	}

	public Boolean getIsC() {
		return isC;
	}

	public void setIsC(Boolean isC) {
		this.isC = isC;
	}

	public List<StopTransactionItemRequest> getItems() {
		return items;
	}

	public void setItems(List<StopTransactionItemRequest> items) {
		this.items = items;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public List<Integer> getVedId() {
		return vedId;
	}

	public void setVedId(List<Integer> vedId) {
		this.vedId = vedId;
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

	public List<Integer> getStopTransactionId() {
		return stopTransactionId;
	}

	public void setStopTransactionId(List<Integer> stopTransactionId) {
		this.stopTransactionId = stopTransactionId;
	}

	public List<StopTransactionProductExclusionRequest> getProductCategoryExclusion() {
		return productCategoryExclusion;
	}

	public void setProductCategoryExclusion(List<StopTransactionProductExclusionRequest> productCategoryExclusion) {
		this.productCategoryExclusion = productCategoryExclusion;
	}

	public List<StopTransactionItemExclusionRequest> getItemExclusion() {
		return itemExclusion;
	}

	public void setItemExclusion(List<StopTransactionItemExclusionRequest> itemExclusion) {
		this.itemExclusion = itemExclusion;
	}

	public List<Long> getBatchId() {
		return batchId;
	}

	public void setBatchId(List<Long> batchId) {
		this.batchId = batchId;
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
}
