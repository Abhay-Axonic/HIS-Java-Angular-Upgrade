package com.param.pharmacy.master.global.dao.item;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.PharmacologicalClassification;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.Tax;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.global.common.Response;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.MultiItemsRequest;
import com.param.pharmacy.master.global.dto.item.ItemListResponse;
import com.param.pharmacy.master.global.dto.item.ItemRequest;
import com.param.pharmacy.master.global.dto.item.ItemSearchByNameResponse;

import in.param.exception.ApplicationException;

public interface IItemDao {

	List<Item> getAll();

	Item getById(Integer id, Integer orgId);

	Integer create(Item item);
	
	Integer update(Item item);

	FormulationType getFormulationtype(Integer formulationTypeId);

	Generic getGeneric(Integer genericId);

	Manufacturer getManufacturer(Integer manufacturerId);

	StrengthUnit getStrengthUnit(Integer strengthUnitId);

	PharmacologicalClassification getPharmacologicalClassification(Integer pharmacologicalClassificationId);

	Tax getTax(Integer id);

	ItemRequestDetail getItemRequestDetail(Integer id);

	Integer updateItemRequestDetail(ItemRequestDetail itemRequestDetail);

	StorageUnit getStorageUnit(Integer storageUnitId);

	Integer getRouteOfAdministratorByFormulationType(Integer formulationTypeId);

	List<com.param.pharmacy.master.global.dto.item.ItemListResponse> itemsList(ItemsRequest itemsRequest);

	List<com.param.pharmacy.master.global.dto.item.ItemListResponse> assetItemsList(ItemsRequest itemsRequest);

	Long getgenericCount(Integer id);

	Long getstoreWiseCount(Integer id);

	Long getproductCategoryCount(Integer id);

	Long getformulationTypeCount(Integer id);

	Long getmanufactoreCount(Integer id);

	Long getstrengthUnitCount(Integer id);

	Long gettaxCount(Integer id);

	Long getstorageUnitCount(Integer id);

	List<com.param.pharmacy.master.global.dto.item.ItemListResponse>  getIsEnabledVaccinationList();
	

	List<ItemSearchByNameResponse> getItemSearchForAutoComplete(String name);


	List<com.param.pharmacy.master.global.dto.item.ItemListResponse> getAllItems(Integer orgId,Integer offset,Integer noOfRecordsPerPage,String searchKeyward) throws Throwable;

	List<ItemListResponse> MultiItemsList(MultiItemsRequest itemRequest);

	List<ItemListResponse> MultiAssetItemsList(MultiItemsRequest itemRequest);

	CommonResponse checkForUniqueItemCode(ItemRequest itemRequest)throws ApplicationException;

	CommonResponse checkForUniqueItemName(ItemRequest itemRequest)throws ApplicationException;

	List<ItemListResponse> itemTypeaheadSearch(MultiItemsRequest itemsRequest);

	Response getItemIdAndName(Integer organizationId)throws ApplicationException;

	Response getBatchByItemId(Integer itemId, Integer storeId)throws ApplicationException;

	Response getUomType(Integer organizationId)throws ApplicationException;

	Response saveItemMaster(List<Item> itemMaster)throws ApplicationException;

	Response updatePreviousItemInActive(ItemRequest itemRequest)throws ApplicationException;

	String getupdateItemCode(Integer organizationId)throws ApplicationException;


	}
