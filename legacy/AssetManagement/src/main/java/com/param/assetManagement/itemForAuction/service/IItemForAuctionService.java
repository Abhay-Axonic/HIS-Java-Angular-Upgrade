package com.param.assetManagement.itemForAuction.service;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemForAuctionRequest;

public interface IItemForAuctionService {

	FixedAssetOpeningBalanceDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	Object getItemAuctionList(FixedAssetOpeningBalanceRequest request);
	
	Object getItemAuctionById(Integer id);
	
	Object saveItemAuction(ItemForAuctionRequest request);

	Object approveItemAuction(ItemForAuctionRequest request);
	
}
