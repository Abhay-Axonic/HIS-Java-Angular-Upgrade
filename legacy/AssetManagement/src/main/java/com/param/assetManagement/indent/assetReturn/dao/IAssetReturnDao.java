package com.param.assetManagement.indent.assetReturn.dao;

import java.util.List;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnResponse;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetReturn;
import com.param.entity.model.asset.AssetReturnDetail;

public interface IAssetReturnDao {

	List<AssetReturnResponse> searchAssetReturnList(AssetIndentRequest request, Integer screenId);

	AssetReturn saveASsetReturn(AssetReturn assetReturn);

	AssetReturn getAssetReturn(Integer id);

	AssetReturn updateAssetReturn(AssetReturn assetReturn);

	List<AssetReturnDetail> getAssetReturnDetail(Integer id);

	List<AssetIssue> getAssetReturnIssueList(AssetIndentRequest request);

}
