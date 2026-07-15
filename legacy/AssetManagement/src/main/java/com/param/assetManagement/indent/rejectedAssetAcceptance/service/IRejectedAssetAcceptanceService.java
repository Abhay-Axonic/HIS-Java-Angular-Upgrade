package com.param.assetManagement.indent.rejectedAssetAcceptance.service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;

public interface IRejectedAssetAcceptanceService {

	AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getRejectAssetAcceptanceList(AssetIssueRequest request);

	Object getRejectAssetAcceptanceDetails(Integer id);

	Object saveRejectAssetAcceptance(AssetIssueRequest request);

}
