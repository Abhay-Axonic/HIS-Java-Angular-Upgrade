package com.param.pharmacy.master.global.dto.formulationType;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.AssetCategory;
import com.param.global.dto.global.AssetType;
import com.param.global.dto.global.ProductCategory;

public class FormulationTypeDropdownResponse {

	protected List<AssetType> assetType;

	protected List<AssetCategory> assetCategory;

	protected List<ProductCategory> productCategory;

	public FormulationTypeDropdownResponse() {
		super();
	}

	public List<AssetType> getAssetType() {
		return assetType;
	}

	public void setAssetType(List<AssetType> assetType) {
		this.assetType = assetType;
	}

	public void addAssetType(AssetType assetType) {
		if (this.assetType == null)
			this.assetType = new ArrayList<AssetType>();

		this.assetType.add(assetType);
	}

	public List<AssetCategory> getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(List<AssetCategory> assetCategory) {
		this.assetCategory = assetCategory;
	}

	public void addAssetCategory(AssetCategory assetCategory) {
		if (this.assetCategory == null)
			this.assetCategory = new ArrayList<AssetCategory>();

		this.assetCategory.add(assetCategory);
	}

	public List<ProductCategory> getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(List<ProductCategory> productCategory) {
		this.productCategory = productCategory;
	}

	public void addProductCategory(ProductCategory productCategory) {
		if (this.productCategory == null)
			this.productCategory = new ArrayList<ProductCategory>();

		this.productCategory.add(productCategory);
	}
}
