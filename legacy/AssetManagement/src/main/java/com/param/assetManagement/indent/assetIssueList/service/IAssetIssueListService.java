package com.param.assetManagement.indent.assetIssueList.service;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsRequest;

public interface IAssetIssueListService {

	Object getAssetIssueList(AssetIssueRequest request);

	Object saveAssetIssueList(AssetIssueRequest request);

	Object getAssetIssueListDetails(Integer id, Integer unitId, Integer orgId);

	AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	List<ItemsDetailsListResponse>  batchDetails(ItemsRequest itemsRequest);

	Object carriersList(Integer orgId, Integer unitId);

}
