package com.param.global.dao.items;

import java.util.List;

import com.param.entity.model.master.Item;
import com.param.entity.model.master.Rack;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.items.BatchAccessoriesMapperDto;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.BatchSearchResponse;
import com.param.global.dto.items.ItemListCompareMinResponse;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.ItemsResponse;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;

public interface IItemsDao {

	List<ItemsSearchResponse> itemsSearch(Integer unitId, boolean isOtc, String search, Integer storeId);

	List<ItemListResponse> itemsList(ItemsRequest itemsRequest);
	
	List<ItemListResponse> itemsListForStoreIndent(ItemsRequest itemsRequest);
	
	List<ItemListResponse> itemsListWithTax(ItemsRequest itemsRequest);

	List<ItemListResponse> assetItemsList(ItemsRequest itemsRequest);

	List<BatchListResponse> batchList(ItemsRequest itemsRequest);
	
	List<BatchListResponse> batchListBillableConsumption(ItemsRequest itemsRequest);

	List<BatchListResponse> batchListStoreWise(ItemsRequest itemsRequest);	
	List<BatchListResponse> batchListStoreWiseWithoutGeneric(ItemsRequest itemsRequest);

	List<BatchListResponse> batchList(ItemsRequest itemsRequest, BatchProperty batchProperty);

	List<BatchSearchResponse> batchSearch(boolean isOtc, String search);

	List<BatchSearchResponse> expiryAndAvailableQuantityWisebatches(boolean isOtc, String search, Integer unitd, Integer storeId);

	List<ItemsDetailsListResponse> itemsDetails(List<Integer> idList, Integer storeId, Integer unitId);

    List<ItemsDetailsListResponse> itemsDetailsOpeningBalance(List<Integer> idList, Integer storeId);

	Long getStockAvailable(Integer storeId,Integer itemId, Integer unitid);
	
	List<ItemsResponse> itemsListByBatchExpiry(ItemsRequest itemsRequest);

	List<BatchAccessoriesMapperDto> batchAccessoriesList(Integer batchId, Integer unitId);

	List<BatchListResponse> FixedTypebatchList(ItemsRequest itemRequest);

	List<BatchListResponse> FixedTypebatchListNew(ItemsRequest itemRequest);

	List<BatchListResponse> FixedTypebatchList(ItemsRequest itemRequest, BatchProperty batchProperty);

	List<ItemsDetailsListResponse> itemsDetailsFixedAsset(List<Integer> idList, Integer storeId);

	Long productCategoryCount(List<Integer> idList);
	
	List<BatchResponse> batchListByItemId(Integer itemId, Integer storeId, Integer unitId);

	Long getLeaseQunatity(Integer storeId, Integer itemId);
	
	Long getGrnLeaseQunatity(Integer storeId, Integer itemId);//For GRN get Total Lease Qty
	
	List<ItemListResponse> workOrderItemsList(ItemsRequest itemsRequest);

	List<ItemListResponse> consignedItemsList(ItemsRequest itemRequest, boolean isConsignment);
	
	List<ItemListResponse> consignedItemsListForInword(ItemsRequest itemRequest, boolean isConsignment);

	List<BatchListResponse> consignedBatchList(ItemsRequest itemRequest, boolean isConsignment);

	List<BatchListResponse> consignedBatchListItemsProductCategory(ItemsRequest itemRequest, boolean isConsignment);

	List<Quantities> getLeaseQunatityBP(Integer storeId, List<Integer> consumabelItemIds);

	List<Quantities> getGrnLeaseQunatityBP(Integer storeId, List<Integer> consumabelItemIds);

	List<ItemListResponse> getItemsListOnTemplet(ItemsRequest itemsRequest);

	List<ItemsDetailsListResponse> itemAndBatchList(ItemsRequest itemsRequest);

	List<BatchListResponse> batchListWithAvailableQtyCheck(ItemsRequest itemRequest);

	List<BatchListResponse> FixedTypebatchListWithAvailableQtyCheck(ItemsRequest itemRequest);

	List<ItemListResponse> consumableItemsList(ItemsRequest itemRequest);
	
	List<ItemListResponse> consumableItemsListByConsignment(ItemsRequest itemRequest);

	List<ItemListResponse> consumableItemsListByConsignmentIndent(ItemsRequest itemRequest);
	
	Long getApprovedPRandPOQuantity(Integer storeId, Integer itemId, Integer unitId);
	
	Long getApprovedPurchaseOrderQuantity(Integer storeId, Integer itemId, Integer unitId);

	List<Quantities> getApprovedPRandPOQuantityList(Integer storeId, List<Integer> itemId, Integer unitId);

	List<Quantities> getApprovedPurchaseOrderQuantityList(Integer storeId, List<Integer> itemId, Integer unitId);


	List<ItemsSearchResponse> searchGeneric(boolean isOtc, String search,Integer  storeId, Integer unitId);
	
	List<ItemListResponse> getIssuingAndReuestedStoreWiseItemsList(ItemsRequest itemsRequest);
	
	List<ItemListResponse> assetIssuingAndReuestedStoreWiseItemsList(ItemsRequest itemsRequest);

	List<ItemListResponse> geStoreWiseItemsList(ItemsRequest itemRequest);

	List<BatchListResponse> ExpiredMedicationsbatchList(ItemsRequest itemRequest);

	List<BatchListResponse> ExpiredMedicationsFixedTypebatchList(ItemsRequest itemRequest);

	List<ItemsResponse> itemsListByBatchExpired(ItemsRequest itemsRequest);

	List<ItemListResponse> AccessoriesWiseItemsList(ItemsRequest itemRequest);

	List<ItemListResponse> SpareWiseItemsList(ItemsRequest itemRequest);

	List<ItemListResponse> assetAccessoriesWiseItemsList(ItemsRequest itemRequest);

	List<ItemListResponse> assetSpareWiseItemsList(ItemsRequest itemRequest);


	List<ItemListResponse> assetItemsListForAssetIndent(ItemsRequest itemRequest);

	List<ItemsResponse> getItemsListByDateExpired(ItemsRequest itemsRequest);
	
	List<BatchListResponse> batchListForLoanOut(ItemsRequest itemsRequest);
	
	List<Rack> getRackByStoreId(Integer storeId);
	
	List<BatchListResponse> FixedTypebatchListForLoanOut(ItemsRequest itemRequest);

	Long getApprovedStoreIndentQuantity(Integer storeId, Integer itemId);

	List<ItemsSearchResponse> searchBarcodeScanItem(Integer itemId, Integer batchId);

	List<ItemsSearchResponse> searchGenericScanItem(Integer itemId);

	List<ItemsDetailsListResponse> itemsDetailsFixedAssetOpeningBalance(List<Integer> idList, Integer storeId);

	List<Quantities> getStockAvailableBP(Integer storeId, List<Integer> itemIds,Integer unitId);

	List<ItemListResponse> listWithMappedStoresWithCompareAvalablity(ItemsRequest itemRequest);

	List<ItemListCompareMinResponse> getItemAvalablityForStore(Integer storeId, List<Integer> itemIds);

	List<ItemsDetailsListResponse> fixedItemsDetails(List<Integer> itemIds, Integer storeId, Integer unitId);

	List<ItemListResponse> assetIssuingAndReuestedStoreWiseItemsListAccessories(ItemsRequest itemRequest);

	List<ItemListResponse> itemsList1(ItemsRequest itemsRequest);

	List<ItemListResponse> itemsSearchForPendingIssueItemSearch(Integer unitId, boolean isOtc, boolean isNarcotic,
			String search, Integer storeId);

	Long getStockAvailable1(Integer storeId, Integer itemId, Integer unitId);

	List<BatchListResponse> batchListStoreWiseAndKit(ItemsRequest itemRequest);

	List<BatchListResponse> batchListStoreWiseForStockAdjustment(ItemsRequest itemRequest);

	List<BatchListResponse> FixedTypebatchListNewForStockAdjustment(ItemsRequest itemRequest);

	List<UomResponse> getApprovedPRandPOQuantityAll(Integer storeId, Integer unitId);

	List<UomResponse> getApprovedPurchaseOrderQuantityAll(Integer storeId, Integer unitId);

	List<ItemsSearchResponse> itemsSearchStockAdjustment(Integer unitId, boolean isOtc, String search, Integer storeId,
			Integer assetTypeId);

	List<UomResponse> getBatchwiseDiscount(List<Long> batchIds);

}