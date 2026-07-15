package com.param.assetManagement.indent.assetIssue.dao;

import java.util.List;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueDetailResponse;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueAccessory;
import com.param.entity.model.asset.AssetIssueBatchMapper;
import com.param.entity.model.asset.AssetIssueDetail;
import com.param.entity.model.inventory.StoreStock;

public interface IAssetIssueDao {

	List<AssetIssueResponse> searchAssetIssuePendingList(AssetIndentRequest request, Integer screenId);

	AssetIssue getAssetIssue(Integer id);

	List<AssetIssueDetail> getAssetIssueDetail(Integer id);

	List<AssetIssueBatchMapper> getAssetIssueBatchList(Integer id);

	List<AssetIssueAccessory> getAssetIssueAccessoryList(Integer id);

	AssetIssueResponse getAssetIssuePending(Integer id);

	List<AssetIssueDetailResponse> getAssetIssuePendingDetail(Integer id);

	List<AssetIssueBatchMapper> getAsssetIssueBatchList(Integer id);

	AssetIssue updateMaterialIssue(AssetIssue assetIssue);

	StoreStock saveStoreStock(StoreStock storeStockNew);

}
