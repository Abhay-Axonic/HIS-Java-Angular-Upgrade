package com.param.assetManagement.indent.assetReturnAcceptance.dao;

import java.util.List;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnResponse;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetReturn;
import com.param.entity.model.asset.AssetReturnDetail;
import com.param.entity.model.asset.RejectedAssetAcceptance;

public interface IAssetReturnAcceptanceDao {

	List<AssetReturnResponse> searchAssetReturnAcceptanceList(AssetIndentRequest request, Integer screenId);

	AssetReturn getAssetReturn(Integer id);

	List<AssetReturnDetail> getAssetReturnDetail(Integer id);

	AssetReturn updateAssetReturn(AssetReturn assetReturn);

	RejectedAssetAcceptance save(RejectedAssetAcceptance rejectedAssetAcceptance);

	void issueUpdate(AssetIssue issue);

}
