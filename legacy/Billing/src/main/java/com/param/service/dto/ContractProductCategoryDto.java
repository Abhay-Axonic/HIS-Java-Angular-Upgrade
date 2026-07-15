package com.param.service.dto;

public class ContractProductCategoryDto {

	private String productCategory;
	
	private Float quantity;

	private Integer productCategoryId;
	
	private Double amount;
	
	private Double prodCatCoShare;
	
	private Integer assetCategoryId;
	
	

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Double getProdCatCoShare() {
		return prodCatCoShare;
	}

	public void setProdCatCoShare(Double prodCatCoShare) {
		this.prodCatCoShare = prodCatCoShare;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
