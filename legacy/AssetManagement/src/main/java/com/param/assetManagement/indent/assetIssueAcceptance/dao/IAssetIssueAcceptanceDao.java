package com.param.assetManagement.indent.assetIssueAcceptance.dao;

import java.util.List;

import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.entity.model.asset.RejectedAssetAcceptance;

public interface IAssetIssueAcceptanceDao {

	List<AssetIssueResponse> searchAssetIssueAcceptanceList(AssetIssueRequest request, Integer screenId);

	RejectedAssetAcceptance saveRejectedAssetAcceptance(RejectedAssetAcceptance rejectedAssetAcceptance);

}
