package com.param.assetManagement.itemForAuction.dao;

import java.util.List;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionDetails;
import com.param.entity.model.asset.AuctionRequisitionDetail;
import com.param.entity.model.asset.ItemAuction;
import com.param.entity.model.asset.ItemAuctionDetail;
import com.param.entity.model.asset.VendorAuctionMapper;

public interface IItemForAuctionDao {

	List<ItemAuction> searchItemAuction(FixedAssetOpeningBalanceRequest request, Integer screenId);
	
	ItemAuction getItemAuctionById(Integer id);

	List<ItemAuctionDetails> getItemAuctionDetail(Integer itemAuctionId);
	
	ItemAuctionDetail getItemAuctionDetailById(Integer itemAuctionDetailId);
	
	AuctionRequisitionDetail getAuctionRequisitionDetail(Integer id);

	ItemAuction saveItemAuction(ItemAuction itemAuction);
	
	VendorAuctionMapper getVendorAuctionMapper(Integer id);
	
}