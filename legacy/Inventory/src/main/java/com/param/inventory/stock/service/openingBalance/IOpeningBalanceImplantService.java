package com.param.inventory.stock.service.openingBalance;

import java.util.List;

import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;


public interface IOpeningBalanceImplantService {

	
	
	List<ItemListResponse> implantItemsList(ItemsRequest itemRequest);
	
	List<ItemListResponse> consignmentImplantItemsList(ItemsRequest itemRequest);
	
	Object saveOpeningBalanceImplant(OpeningBalanceRequest request);
	
	Object getOpeningBalanceImplantList(OpeningBalanceRequest request);
	
	Object getOpeningBalanceImplant(Integer id);
	
	Object updateOpeningBalanceImplant(OpeningBalanceRequest request);

	Object approveOpeningBalanceImplant(OpeningBalanceRequest request);

	//Object approveOpeningBalanceImplantDetail(OpeningBalanceRequest request);
	
	Object cancelOpeningBalanceImplant(OpeningBalanceRequest request);



}
