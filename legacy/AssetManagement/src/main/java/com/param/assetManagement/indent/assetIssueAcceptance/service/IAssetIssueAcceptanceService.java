package com.param.assetManagement.indent.assetIssueAcceptance.service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;

public interface IAssetIssueAcceptanceService {

	AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getAssetIssueAcceptanceList(AssetIssueRequest request);

	Object saveAssetIssueAcceptance(AssetIssueRequest request);

	Object getAssetIssueAcceptanceDetails(Integer id,Integer unitId, Integer orgId);

}
