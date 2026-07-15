package com.param.assetManagement.fixedAssetOpeningBalance.service;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.global.service.items.CustomException;

public interface IFixedAssetOpeningBalanceService {

	FixedAssetOpeningBalanceDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	Object getOpeningBalanceList(FixedAssetOpeningBalanceRequest request);
	
	Object getOpeningBalance(Integer unitId, Integer orgId, Integer id);
	
	Object saveOpeningBalance(FixedAssetOpeningBalanceRequest request) throws CustomException;

	Object cancelOpeningBalance(FixedAssetOpeningBalanceRequest request);

	Object approveOpeningBalanceDetail(FixedAssetOpeningBalanceRequest request);
	
	Object approveOpeningBalance(FixedAssetOpeningBalanceRequest request);
	
	Object updateOpeningBalance(FixedAssetOpeningBalanceRequest request);
}
