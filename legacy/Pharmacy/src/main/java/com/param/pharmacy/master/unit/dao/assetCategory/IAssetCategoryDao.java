package com.param.pharmacy.master.unit.dao.assetCategory;

import java.util.List;

import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.AssetType;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryListResponse;

public interface IAssetCategoryDao {

	List<AssetCategory> getAll(Integer orgId);

	AssetCategory getById(Integer id);

	Integer create(AssetCategory assetCategory);
	
	AssetType getAssetType(Integer id);

	Integer update(AssetCategory assetCategory);

	Long getProductCategory(Integer id);

	List<AssetCategoryListResponse> getAllAssetCat(Integer organizationId);
}
