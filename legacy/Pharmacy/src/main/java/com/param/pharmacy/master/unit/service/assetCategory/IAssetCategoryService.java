package com.param.pharmacy.master.unit.service.assetCategory;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryDetailsResponse;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryListResponse;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryRequest;

public interface IAssetCategoryService {

	List<AssetCategoryListResponse> getAll(Integer orgId);

	AssetCategoryDetailsResponse getById(Integer id);

	CommonResponse create(AssetCategoryRequest assetCategoryRequest);

	CommonResponse update(AssetCategoryRequest assetCategoryRequest) throws CustomeException;

}
