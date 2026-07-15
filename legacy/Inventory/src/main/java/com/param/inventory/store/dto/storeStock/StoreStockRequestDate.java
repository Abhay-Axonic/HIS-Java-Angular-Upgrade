package com.param.inventory.store.dto.storeStock;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.service.global.model.CookieObjectDto;

public class StoreStockRequestDate extends CookieObjectDto{

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	protected Date fromDate;
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	protected Date toDate;
	protected Integer itemNo;
	protected List<MultipleStoreRequest> multipleStore;
	protected Integer storeId;

	protected Integer assetTypeId;

	protected Integer assetCategoryId;

	protected Integer productCategoryId;

	protected Boolean isConsignment;
	
	protected Integer itemId;

	protected String itemCode;

	protected String itemName;
	
	protected Integer vedId;
	
	protected Integer offset;
	
	protected Integer recordPerPage;
	
	protected String search;

	public StoreStockRequestDate() {
		super();
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getVedId() {
		return vedId;
	}

	public void setVedId(Integer vedId) {
		this.vedId = vedId;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getItemNo() {
		return itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}

	public List<MultipleStoreRequest> getMultipleStore() {
		return multipleStore;
	}

	public void setMultipleStore(List<MultipleStoreRequest> multipleStore) {
		this.multipleStore = multipleStore;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}


}
