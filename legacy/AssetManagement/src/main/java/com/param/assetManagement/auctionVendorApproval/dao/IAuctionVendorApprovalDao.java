package com.param.assetManagement.auctionVendorApproval.dao;

import java.util.List;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionDetails;
import com.param.entity.model.asset.AuctionVendorApproval;
import com.param.entity.model.asset.AuctionVendorApprovalDetail;
import com.param.entity.model.asset.VendorApprovalAuctionMapper;

public interface IAuctionVendorApprovalDao {

	List<AuctionVendorApproval> searchAuctionVendorApproval(FixedAssetOpeningBalanceRequest request, Integer screenId);
	
	AuctionVendorApproval getAuctionVendorApprovalById(Integer id);

	List<ItemAuctionDetails> getAuctionVendorApprovalDetail(Integer id);
	
	AuctionVendorApprovalDetail getAuctionAuctionVendorApprovalDetail(Integer id);

	AuctionVendorApproval saveAuctionVendorApproval(AuctionVendorApproval auctionVendorApproval);
	
	VendorApprovalAuctionMapper getVendorApprovalAuctionMapper(Integer id);
	
}