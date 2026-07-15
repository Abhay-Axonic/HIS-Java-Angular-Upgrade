package com.param.assetManagement.indent.assetIndent.service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;

public interface IAssetIndentService {

	AssetIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	Object getAssetIndentList(AssetIndentRequest request);
	
	Object getAssetIndentById(Integer id, Integer unitId, Integer orgId);
	
	Object saveAssetIndent(AssetIndentRequest request);
	
	Object updateAssetIndent(AssetIndentRequest request);
	
	Object cancelAssetIndent(AssetIndentRequest request);

	Object approveAssetIndent(AssetIndentRequest request);
	
}
