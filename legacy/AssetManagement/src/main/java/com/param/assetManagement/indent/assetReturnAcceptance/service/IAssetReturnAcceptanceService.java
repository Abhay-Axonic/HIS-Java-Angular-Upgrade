package com.param.assetManagement.indent.assetReturnAcceptance.service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnRequest;

public interface IAssetReturnAcceptanceService {

	AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getAssetReturnAcceptanceList(AssetIndentRequest request);

	Object updateAssetReturnAcceptance(AssetReturnRequest request);

	Object getAssetReturnAcceptanceDetails(Integer id, Integer unitId);

}
