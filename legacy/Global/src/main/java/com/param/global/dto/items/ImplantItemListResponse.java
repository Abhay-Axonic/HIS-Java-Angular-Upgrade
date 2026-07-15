package com.param.global.dto.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ImplantItemListResponse {
	
	protected Integer itemid;
	
	protected String itemname;
	
	protected String itemcode;
	
	protected String productcategory;
	
	protected String assetcategory;
	
	protected Integer itemId;
	
	protected String itemName;
	
	protected String itemCode;
	
	protected String assetCategory;
	
	protected String productCategory;
	

	public ImplantItemListResponse()
	{
		super();
	}
	
	public ImplantItemListResponse(Integer itemId, String assetCategory, String productCategory, String itemCode,
			String itemName) {
		super();
		this.itemId = itemId;
		this.assetCategory = assetCategory;
		this.productCategory = productCategory;
		this.itemCode = itemCode;
		this.itemName = itemName;
	}
	
	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getProductcategory() {
		return productcategory;
	}

	public void setProductcategory(String productcategory) {
		this.productcategory = productcategory;
	}

	public String getAssetcategory() {
		return assetcategory;
	}

	public void setAssetcategory(String assetcategory) {
		this.assetcategory = assetcategory;
	}
	

}
