package com.param.inventory.stock.dao.openingBalance;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.OpeningBalance;
import com.param.entity.model.inventory.OpeningBalanceImplant;
import com.param.entity.model.inventory.OpeningBalanceImplantDetail;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;

public interface IOpeningBalanceImplantDao {
	

	List<ItemListResponse> implantItemsList(ItemsRequest itemRequest); //For Opening Balance Implant get List
	
	List<ItemListResponse> consignmentImplantItemsList(ItemsRequest itemRequest);

	OpeningBalanceImplant saveOpeningBalanceImplant(OpeningBalanceImplant openingBalanceImplant);  //Save 
	

	OpeningBalanceImplant getOpeningBalaceImplant(Integer id); //GetByIdParentMethod

	List<OpeningBalanceImplantDetail> getOpeningbalaceImplantDetail(Integer openingBalaceId);//getByIdChildMethod
	
	OpeningBalanceImplant updateOpeningBalanceImplant(OpeningBalanceImplant openingBalance); //For Update Item Implant 
	
	Batch saveBatch(Batch batch);//For Approved Item Implant 
	
	List<OpeningBalanceImplant> searchOpeningBalanceImplant(OpeningBalanceRequest request); //For Main Search

	}
