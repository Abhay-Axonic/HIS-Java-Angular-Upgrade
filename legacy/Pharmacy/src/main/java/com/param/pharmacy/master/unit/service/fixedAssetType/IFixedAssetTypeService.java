package com.param.pharmacy.master.unit.service.fixedAssetType;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeDetailsResponse;
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeListResponse;
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeRequest;

public interface IFixedAssetTypeService {

	List<FixedAssetTypeListResponse> getAll();

	FixedAssetTypeDetailsResponse getById(Integer id);

	CommonResponse create(FixedAssetTypeRequest fixedAssetTypeRequest);

	CommonResponse update(FixedAssetTypeRequest fixedAssetTypeRequest);

}
