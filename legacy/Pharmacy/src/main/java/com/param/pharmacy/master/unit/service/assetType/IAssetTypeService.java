package com.param.pharmacy.master.unit.service.assetType;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeDetailsResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeListResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeRequest;

public interface IAssetTypeService {

	List<AssetTypeListResponse> getAll();

	AssetTypeDetailsResponse getById(Integer id);

	CommonResponse create(AssetTypeRequest assetTypeRequest);

	CommonResponse update(AssetTypeRequest assetTypeRequest);

}
