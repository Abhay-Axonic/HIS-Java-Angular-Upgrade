package com.param.assetManagement.indent.assetIssue.service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;

public interface IAssetIssueService {

	AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getAssetIssuePendingList(AssetIndentRequest request);

	Object saveAssetIssuePending(AssetIssueRequest request);

	Object getAssetIssuePendingDetails(Integer id, Integer unitId, Integer orgId);

}
