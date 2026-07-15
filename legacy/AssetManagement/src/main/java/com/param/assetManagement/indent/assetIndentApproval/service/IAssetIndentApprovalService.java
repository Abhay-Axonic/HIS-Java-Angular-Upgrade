package com.param.assetManagement.indent.assetIndentApproval.service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;

public interface IAssetIndentApprovalService {

	AssetIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	Object getAssetIndentApprovalList(AssetIndentRequest request);
	
	Object getAssetIndentApprovalById(Integer id, Integer unitId, Integer orgId);
	
	Object saveAssetIndentApproval(AssetIndentRequest request);
	
	Object cancelAssetIndentApproval(AssetIndentRequest request);

}
