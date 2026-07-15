package com.param.global.dto.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import scala.Char;

@JsonInclude(Include.NON_NULL)
public class ItemListResponse {
	
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
	
	//added by minesh
	protected Boolean isConsignment;
	protected Boolean isconsignment;
	
//    private Integer availQuantity;
    
    private Long availquantity;
    
    private boolean isThreeUOMApplicable;
	private Boolean isLasa;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

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

	public ItemListResponse()
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

	public ItemListResponse(Integer itemId, String itemCode, String itemName) {
		super();
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
	}
	
	public ItemListResponse(Integer itemId, String assetCategory, String productCategory, String itemCode,
			String itemName) {
		super();
		this.itemId = itemId;
		this.assetCategory = assetCategory;
		this.productCategory = productCategory;
		this.itemCode = itemCode;
		this.itemName = itemName;
		
	}
	
	public ItemListResponse(Integer itemid, String assetcategory, String productcategory, String itemcode,
			String itemname,Long availquantity) {
		super();
		this.itemid = itemid;
		this.assetcategory = assetcategory;
		this.productcategory = productcategory;
		this.itemcode = itemcode;
		this.itemname = itemname;
		this.availquantity=availquantity;
	}


	public ItemListResponse(Integer itemId, String assetCategory, String productCategory, String itemCode,
			String itemName, String generic) {
		super();
		this.itemId = itemId;
		this.assetCategory = assetCategory;
		this.productCategory = productCategory;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.generic = generic;
	}

	public ItemListResponse(Integer itemId, String generic, String itemCode, String itemName) {
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

	public Long getAvailquantity() {
		return availquantity;
	}

	public void setAvailquantity(Long availquantity) {
		this.availquantity = availquantity;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}
	//	public Integer getAvailQuantity() {
//		return availQuantity;
//	}
//
//	public void setAvailQuantity(Integer availQuantity) {
//		this.availQuantity = availQuantity;
//	}

//	public Integer getAvailquantity() {
//		return availquantity;
//	}
//
//	public void setAvailquantity(Integer availquantity) {
//		this.availquantity = availquantity;
//	}

	
}
