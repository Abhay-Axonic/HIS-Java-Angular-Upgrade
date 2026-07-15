package com.param.assetManagement.indent.assetReturn.service;

import org.springframework.util.MultiValueMap;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnRequest;

public interface IAssetReturnService {

	AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getAssetReturnList(AssetIndentRequest request);

	Object saveAssetReturn(AssetReturnRequest request);

	Object updateAssetReturn(AssetIndentRequest request);

	Object getAssetReturnDetails(Integer id, Integer unitId, Integer orgId);

	Object approveAssetReturn(AssetReturnRequest request);

	Object cancelAssetReturn(AssetReturnRequest request);

	Object getAssetReturnAgainstIssue(AssetIndentRequest request);

}
