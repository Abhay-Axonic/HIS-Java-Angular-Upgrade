package com.param.inventory.stockAdjustments.dto.stopTransaction;

public class StopTransactionProductCatResponse {

	protected Integer id;

	protected String assetCategory;

	protected String productCategory;

	protected Integer pCategoryId;

	protected String pCategoryCode;
	
	//protected Boolean isDeleted;

	public StopTransactionProductCatResponse() {
		super();
	}

	public StopTransactionProductCatResponse(Integer id, String assetCategory, String productCategory) {
		this.id = id;
		this.assetCategory = assetCategory;
		this.productCategory = productCategory;
	}

	public StopTransactionProductCatResponse(Integer id, String assetCategory, String productCategory,
			Integer pCategoryId, String pCategoryCode) {
		super();
		this.id = id;
		this.assetCategory = assetCategory;
		this.productCategory = productCategory;
		this.pCategoryId = pCategoryId;
		this.pCategoryCode = pCategoryCode;
		//this.isDeleted = isDeleted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getpCategoryId() {
		return pCategoryId;
	}

	public void setpCategoryId(Integer pCategoryId) {
		this.pCategoryId = pCategoryId;
	}

	public String getpCategoryCode() {
		return pCategoryCode;
	}

	public void setpCategoryCode(String pCategoryCode) {
		this.pCategoryCode = pCategoryCode;
	}

//
//	public Boolean getIsDeleted() {
//		return isDeleted;
//	}
//
//	public void setIsDeleted (Boolean isDeleted) {
//		this.isDeleted = isDeleted;
//	}

}
