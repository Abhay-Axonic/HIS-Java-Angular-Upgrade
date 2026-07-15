package com.param.assetManagement.indent.assetIndent.dao;

import java.util.List;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.entity.model.asset.AssetIndent;
import com.param.entity.model.asset.AssetIndentAccessories;
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.asset.AuctionRequisitionDetail;

public interface IAssetIndentDao {

	List<AssetIndent> searchAssetIndent(AssetIndentRequest request, Integer screenId);
	
	AssetIndent getAssetIndentById(Integer id);

	List<AssetIndentDetail> getAssetIndentDetail(Integer assetIndentId);
	
	List<AssetIndentAccessories> getAssetIndentAccessories(Integer assetIndentId);
	
	AuctionRequisitionDetail getAuctionRequisitionDetail(Integer id);

	AssetIndent saveAssetIndent(AssetIndent assetIndent);
	
	AssetIndent updateAssetIndent(AssetIndent assetIndent);
	
}