package com.param.inventory.stockAdjustments.dto.stopTransaction;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.Generic;

public class ProductCategoryResponse {

	protected Integer id;

	protected String category;

	protected String code;		

	protected String AssetCategory;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_category_id")
	private AssetCategory assetCategory;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetCategory")
	private List<Generic> assetCategoryList;
	
	public ProductCategoryResponse() {
		super();
	}

	public ProductCategoryResponse(Integer id, String category, String code,String AssetCategory) {
		super();
		this.id = id;
		this.category = category;
		this.code = code;	
		this.AssetCategory = AssetCategory;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAssetCategory() {
		return AssetCategory;
	}

	public void setAssetCategory(String AssetCategory) {
		this.AssetCategory = AssetCategory;
	}


}
