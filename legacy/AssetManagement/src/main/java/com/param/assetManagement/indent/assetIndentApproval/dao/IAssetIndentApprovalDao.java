package com.param.assetManagement.indent.assetIndentApproval.dao;

import java.util.List;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.entity.model.asset.AssetIndent;
import com.param.entity.model.asset.AssetIndentAccessories;
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.asset.AssetIssue;

public interface IAssetIndentApprovalDao {

	List<AssetIndent> searchAssetIndent(AssetIndentRequest request, Integer screenId);
	
	AssetIndent getAssetIndentById(Integer id);
	
	AssetIndentDetail getAssetIndentDetailById(Integer id);

	List<AssetIndentDetail> getAssetIndentDetail(Integer assetIndentId);
	
	List<AssetIndentAccessories> getAssetIndentAccessories(Integer assetIndentId);
	
	AssetIndentAccessories getAssetIndentAccessoryById(Integer id);
	
	AssetIndent updateAssetIndent(AssetIndent assetIndent);

	AssetIssue saveAssetIssue(AssetIssue assetIssue);
	
}