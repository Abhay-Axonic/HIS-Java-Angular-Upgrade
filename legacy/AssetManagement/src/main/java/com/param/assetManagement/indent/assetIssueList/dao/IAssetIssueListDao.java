package com.param.assetManagement.indent.assetIssueList.dao;

import java.util.List;

import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.entity.model.asset.AssetIssue;

public interface IAssetIssueListDao {

	List<AssetIssue> searchAssetIssueList(AssetIssueRequest request, Integer screenId);

	AssetIssue saveAssetIssueList(AssetIssue assetIssue);

}
