package com.param.global.dto.kit.Items;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class KitItemsRequest extends CookieObjectDto {
	
	private Integer storeId;
	
	private List<Integer> itemIds;

	private Integer assetTypeId;

	private Integer assetCategoryId;

	private Integer productCategoryId;

	private Integer fromStoreId;

	private Integer toStoreId;

	private Boolean isConsignment;
	
	private Boolean isImplant;

	private Integer vedId;

	private String itemCode;

	private String itemName;

	protected Date fromDate;
	
	private Integer screenId;

	protected Date toDate;
	
	protected Integer vendorId;
	
	protected Integer itemId;
	
	protected List<KitItemsDetail> items;

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
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

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Integer getVedId() {
		return vedId;
	}

	public void setVedId(Integer vedId) {
		this.vedId = vedId;
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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public List<KitItemsDetail> getItems() {
		return items;
	}

	public void setItems(List<KitItemsDetail> items) {
		this.items = items;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

}
