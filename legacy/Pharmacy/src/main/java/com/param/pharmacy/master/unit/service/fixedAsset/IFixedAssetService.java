package com.param.pharmacy.master.unit.service.fixedAsset;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetListResponse;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetRequest;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetResponse;

public interface IFixedAssetService {

	List<FixedAssetListResponse> getAll();

	FixedAssetResponse getById(Integer id);

	CommonResponse create(FixedAssetRequest fixedAssetRequest);

	CommonResponse update(FixedAssetRequest fixedAssetRequest);

	List<FixedAssetListResponse> getAssetNameListByAssetCategoryId(Integer assetCategoryId);

}
