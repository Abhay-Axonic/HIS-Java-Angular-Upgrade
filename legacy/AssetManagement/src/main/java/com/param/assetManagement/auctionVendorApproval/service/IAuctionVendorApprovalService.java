package com.param.assetManagement.auctionVendorApproval.service;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemForAuctionRequest;

public interface IAuctionVendorApprovalService {

	FixedAssetOpeningBalanceDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	Object getAuctionVendorApprovalList(FixedAssetOpeningBalanceRequest request);
	
	Object getAuctionVendorApprovalById(Integer id);
	
	Object saveAuctionVendorApproval(ItemForAuctionRequest request);

}
