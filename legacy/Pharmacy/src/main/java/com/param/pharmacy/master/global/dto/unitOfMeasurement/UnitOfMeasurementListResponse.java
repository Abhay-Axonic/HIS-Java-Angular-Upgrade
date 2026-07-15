package com.param.pharmacy.master.global.dto.unitOfMeasurement;

import java.util.List;

import com.param.global.dto.items.UomResponse;

public class UnitOfMeasurementListResponse extends UnitOfMeasurementResponseList {

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer productCategoryId;

	protected String productCategory;

//	protected Integer itemId;

	protected String itemCode;

	protected String itemName;
	
	protected List<UomResponse> uomList;	
	
	
	public UnitOfMeasurementListResponse() {
		super();
	}

	public UnitOfMeasurementListResponse(Integer itemid, Boolean status) {
		super(itemid, status);
	}

	public UnitOfMeasurementListResponse(Integer id, Boolean status,Integer assetCategoryId, String assetCategory, Integer productCategoryId,
			String productCategory, Integer itemId, String itemCode, String itemName,List<UomResponse> uomList) {
		super(id, status);
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomList = uomList;
	}
	
	
	public UnitOfMeasurementListResponse(Integer itemId,Boolean status,Integer assetCategoryId, String assetCategory, Integer productCategoryId,
			String productCategory,String itemCode, String itemName) {
		super(itemId, status);
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
//		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
	}
	
	public UnitOfMeasurementListResponse(List<UomResponse> unitOfMeasurementListByItem) {
		// TODO Auto-generated constructor stub
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public List<UomResponse> getUomList() {
		return uomList;
	}

	public void setUomList(List<UomResponse> uomList) {
		this.uomList = uomList;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	/*public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}*/

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
	
}

/*"assetCategoryId":"assetcategory":"productCategoryId":"productCategory":"itemId":"itemCode":"itemName":*/