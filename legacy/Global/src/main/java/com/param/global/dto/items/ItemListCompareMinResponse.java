package com.param.global.dto.items;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemListCompareMinResponse {
	
	protected Integer itemId;
	
	protected Integer itemid;

	protected String assetCategory;
	
	protected String assetcategory;

	protected String productCategory;
	
	protected String productcategory;

	protected String generic;

	protected String itemCode;
	
	protected String itemcode;

	protected String itemName;
	
	protected String itemname;

	protected String itemDetails;
	
	protected Long availableQuantity;
	private Integer minQty;
	private Integer maxQty;
	private BigInteger availableQty;
	
	
	//added by minesh
	protected Boolean isConsignment;
	protected Boolean isconsignment;
	
	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsconsignment() {
		return isconsignment;
	}

	public void setIsconsignment(Boolean isconsignment) {
		this.isconsignment = isconsignment;
	}

	//end added by minesh

	public ItemListCompareMinResponse()
	{
		super();
	}
	
	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}
	
	public String getAssetcategory() {
		return assetcategory;
	}

	public void setAssetcategory(String assetcategory) {
		this.assetcategory = assetcategory;
	}

	public String getProductcategory() {
		return productcategory;
	}

	public void setProductcategory(String productcategory) {
		this.productcategory = productcategory;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public ItemListCompareMinResponse(Integer itemId, String itemCode, String itemName) {
		super();
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
	}
	
	public ItemListCompareMinResponse(Integer itemId, String assetCategory, String productCategory, String itemCode,
			String itemName) {
		super();
		this.itemId = itemId;
		this.assetCategory = assetCategory;
		this.productCategory = productCategory;
		this.itemCode = itemCode;
		this.itemName = itemName;
	}


	public ItemListCompareMinResponse(Integer itemId, String assetCategory, String productCategory, String itemCode,
			String itemName, String generic) {
		super();
		this.itemId = itemId;
		this.assetCategory = assetCategory;
		this.productCategory = productCategory;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.generic = generic;
	}

	public ItemListCompareMinResponse(Integer itemId, String generic, String itemCode, String itemName) {
		super();
		this.itemId = itemId;
		this.generic = generic;
		this.itemCode = itemCode;
		this.itemName = itemName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public String getGeneric() {
		return generic;
	}

	public void setGeneric(String generic) {
		this.generic = generic;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
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

	public String getItemDetails() {
		if(this.itemCode != null && this.itemName != null) {
			return this.itemCode + " | " + this.itemName;
		}
		
		return null;
	}

	public void setItemDetails(String itemDetails) {
		this.itemDetails = itemDetails;
	}

	/*public Long getAvailableQuantity() {
		 return (availableQty != null && availableQty.longValue() > 0) ? availableQty.longValue() : availableQuantity;
	}*/
	
	//start changes for resolved HIS-9611 Po Reorder item not working
	public Long getAvailableQuantity() {
		return (availableQty != null && availableQty.longValue() > 0) ? availableQty.longValue() : 
			availableQuantity!= null ? availableQuantity: 0;
	}
	//end changes for resolved HIS-9611 Po Reorder item not working

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	public BigInteger getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(BigInteger availableQty) {
		this.availableQty = availableQty;
	}

}
