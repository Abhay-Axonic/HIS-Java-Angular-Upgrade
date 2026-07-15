package com.param.inventory.store.dto.storeStock;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class ExpiredStoreStockRequest extends CookieObjectDto{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	protected Date fromDate;
	protected Date toDate;

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	protected Integer storeId;

	protected Integer assetTypeId;

	protected Integer assetCategoryId;

	protected Integer productCategoryId;

	protected Boolean isConsignment;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Integer vedId;
	protected List<MultipleStoreRequest> multipleStore;
	
	protected Integer month;
	protected Integer transitQty;
	
	protected Integer offset;
	
	protected Integer recordPerPage;
	
	protected String search;

	public Integer getTransitQty() {
		return transitQty;
	}

	public void setTransitQty(Integer transitQty) {
		this.transitQty = transitQty;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public List<MultipleStoreRequest> getMultipleStore() {
		return multipleStore;
	}

	public void setMultipleStore(List<MultipleStoreRequest> multipleStore) {
		this.multipleStore = multipleStore;
	}

	public ExpiredStoreStockRequest() {
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
