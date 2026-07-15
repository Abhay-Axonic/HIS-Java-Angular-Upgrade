package com.param.inventory.stockAdjustments.dto.stopTransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StopTransactionResponse {

	protected Integer id;
	
	protected Integer storeId;
	
	protected String store;
	
	protected Integer statusId;

	protected String status;
	
	protected Date startDateTime;

	protected Date stopDateTime;

	protected String requestedBy;

	protected String requestNo;

	protected Date requestDate;

	protected Boolean isVital;

	protected Boolean isEssential;

	protected Boolean isDiseriable;
	
	protected Boolean isA;

	protected Boolean isB;

	protected Boolean isC;

	protected List<StopTransactionItemResponse> items;
	
	protected List<StopTransactionProductCatResponse> productCategoryItems;
	
	protected List<StopTransactionItemExclResponse> exclusionItems;
	protected String remark;
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public StopTransactionResponse() {
		super();
	}

	public StopTransactionResponse(Integer id, Integer storeId, String storeName, Date startDateTime,
			Date stopDateTime, Boolean isVital, Boolean isEssential, Boolean isDiseriable, Boolean isA, Boolean isB,
			Boolean isC) {
		this.id = id;
		this.storeId = storeId;
		this.store = storeName;
		this.startDateTime = startDateTime;
		this.stopDateTime = stopDateTime;
		this.isVital = isVital;
		this.isEssential = isEssential;
		this.isDiseriable = isDiseriable;
		this.isA = isA; 
		this.isB = isB;
		this.isC = isC;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public Date getStopDateTime() {
		return stopDateTime;
	}

	public void setStopDateTime(Date stopDateTime) {
		this.stopDateTime = stopDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
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

	public List<StopTransactionItemResponse> getItems() {
		return items;
	}

	public void setItems(List<StopTransactionItemResponse> items) {
		this.items = items;
	}
	
	public void addItems(StopTransactionItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<StopTransactionItemResponse>();

		this.items.add(item);
	}

	public List<StopTransactionProductCatResponse> getProductCategoryItems() {
		return productCategoryItems;
	}

	public void setProductCategoryItems(List<StopTransactionProductCatResponse> productCategoryItems) {
		this.productCategoryItems = productCategoryItems;
	}
	
	public void addProductCategoryItems(StopTransactionProductCatResponse productItem) {
		if (this.productCategoryItems == null)
			this.productCategoryItems = new ArrayList<StopTransactionProductCatResponse>();

		this.productCategoryItems.add(productItem);
	}

	public List<StopTransactionItemExclResponse> getExclusionItems() {
		return exclusionItems;
	}

	public void setExclusionItems(List<StopTransactionItemExclResponse> exclusionItems) {
		this.exclusionItems = exclusionItems;
	}
	
	public void addExclusionItems(StopTransactionItemExclResponse exclusionItem) {
		if (this.exclusionItems == null)
			this.exclusionItems = new ArrayList<StopTransactionItemExclResponse>();

		this.exclusionItems.add(exclusionItem);
	}
}
