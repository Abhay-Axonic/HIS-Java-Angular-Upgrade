package com.param.pharmacy.master.global.service.item;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.global.dto.generic.Generic;
import com.param.global.dto.global.FormulationType;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.MultiItemsRequest;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.item.ItemDetailsResponse;
import com.param.pharmacy.master.global.dto.item.ItemDropdownResponse;
import com.param.pharmacy.master.global.dto.item.ItemListResponse;
import com.param.pharmacy.master.global.dto.item.ItemRequest;

public interface IItemService {

	ItemDropdownResponse getDropdowns(Integer orgId, Integer unitId);

	List<ItemListResponse> getAll(Integer orgId, Integer offset,Integer noOfRecordsPerPage,String searchKeyward) throws Throwable;
	
	List<ItemListResponse> getIsVaccineEnabledItems();

	ItemDetailsResponse getById(Integer id, Integer orgId);

	CommonResponse create(ItemRequest itemRequest);

	CommonResponse update(ItemRequest itemRequest) throws CustomeException;

	List<FormulationType> getFormulationTypeList(Integer orgId);

	List<Generic> getGenericList(Integer productCategoryId, Integer orgId, String searchGenericName);
	
	List<ItemListResponse> itemsList(ItemsRequest itemRequest);

	Object itemSearch(String name);

	List<ItemListResponse> multipleStoreList(MultiItemsRequest itemsRequest);

	List<ItemListResponse> itemTypeaheadSearch(MultiItemsRequest itemsRequest);

	Response uploadItemMaster(ItemRequest dto);

	Response updatePreviousItemInActive(ItemRequest itemRequest);

	ItemDropdownResponse searchClassificationName(Integer orgId, Integer unitId, String searchClassificationName);

	ItemDropdownResponse searchManufactureName(Integer orgId, Integer unitId, String searchManufactureName);

	CommonResponse checkForUniqueItemName(ItemRequest dto);

	//SessionRequest getStoreFromSession(SessionRequest sessionRequest);

}
