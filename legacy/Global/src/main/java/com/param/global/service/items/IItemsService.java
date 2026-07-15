package com.param.global.service.items;

import java.util.List;

import com.param.global.common.Response;
import org.springframework.util.MultiValueMap;

import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.BatchSearchResponse;
import com.param.global.dto.items.ItemListCompareMinResponse;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsDetailsResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.ItemsResponse;
import com.param.global.dto.items.ItemsSearchResponse;

public interface IItemsService {

	List<ItemsSearchResponse> itemsSearch(Integer unitId, boolean isOtc2, String search,Integer storeId);

	//ItemsDetailsResponse itemsDetail(int id) throws Exception;
	
	ItemsDetailsResponse itemsDetail(int id, int entitlementId,int unitId, int orgId, int storeId) throws Exception;
	
	ItemsDetailsResponse itemsDetailsWithStoreId(int id,int storeId, int unitId) throws Exception;

	List<ItemListResponse> itemsList(ItemsRequest itemRequest);
	
	List<ItemListResponse> getItemsForStoreIndentList(ItemsRequest itemRequest);
	
	List<ItemListResponse> itemsListWithTax(ItemsRequest itemRequest);

	List<ItemsDetailsListResponse> itemsDetails(ItemsRequest itemRequest);

	//List<ItemsDetailsListResponse> itemsDetailsForStoreIndent(ItemsRequest itemRequest);
	
	List<ItemsDetailsListResponse> itemsDetailsForDirectPurchaseRequest(ItemsRequest itemRequest);

	List<BatchListResponse> batchList(ItemsRequest itemRequest);

	List<BatchSearchResponse> batchSearch(boolean isOtc, String search);
	
	List<BatchSearchResponse> expiryAndAvailableQuantityWisebatches(boolean isOtc, String search, Integer unitId, Integer storeId);

	List<ItemsDetailsListResponse> batchDetails(ItemsRequest itemsRequest);

	List<ItemsResponse> itemsListByBatchExpiry(ItemsRequest itemsRequest);

	List<ItemsDetailsListResponse> itemsDetailsWithBatch(ItemsRequest itemsRequest);
	
	List<ItemsDetailsListResponse> itemsDetailsWithBatchForWorkOrder(ItemsRequest itemsRequest);
	
	List<BatchListResponse> batchList(ItemsRequest itemRequest, boolean isPharmacy, boolean isReturn);

	List<ItemListResponse> workOrderItemsList(ItemsRequest itemRequest);
	
	Object smartIssue(ItemsRequest itemRequest);
	
	List<ItemListResponse> getIsConsignedItemsList(ItemsRequest itemsRequest, boolean isConsignment);

	List<BatchListResponse> consignedBatchList(ItemsRequest itemsRequest, boolean isConsignment);

	List<ItemsDetailsListResponse> batchDetailsCSSD(ItemsRequest itemsRequest);

	List<ItemListResponse> getItemsListOnTemplet(ItemsRequest itemsRequest);

	List<BatchListResponse> batchListWithTariff(ItemsRequest itemsRequest);

	List<ItemsDetailsListResponse> itemAndBatchList(ItemsRequest itemsRequest);

	List<BatchListResponse> batchListWithAvailableQty(ItemsRequest itemsRequest);

	List<ItemListResponse> itemsListConsumable(ItemsRequest itemsRequest);

	List<BatchListResponse> batchListBillableConsumption(ItemsRequest itemsRequest);

	List<BatchListResponse> batchListBillableConsumptionNew(ItemsRequest itemsRequest);	

	List<BatchListResponse> batchListBillableConsumptionStoreWise(ItemsRequest itemsRequest);
	List<BatchListResponse> batchListBillableConsumptionStoreWiseWithoutGeneric(ItemsRequest itemsRequest);	
	
	List<ItemListResponse> consumableItemsListByConsignment(ItemsRequest itemsRequest);

	List<ItemListResponse> consumableItemsListByConsignmentIndent(ItemsRequest itemsRequest);

	List<ItemsSearchResponse> searchGeneric(boolean isOtc, String search, Integer storeId, Integer unitId);


	List<ItemsDetailsListResponse> itemsDetailsForGRNWithPendingQty(ItemsRequest itemRequest);

	List<ItemListResponse> getIssuingAndReuestedStoreWiseItemsList(ItemsRequest itemRequest);

	List<ItemListResponse> itemsLgetListOfItemsist(ItemsRequest itemsRequest);

	List<BatchListResponse> batchListExpiredMedications(ItemsRequest itemsRequest);

	List<ItemsResponse> getItemsListByExpired(ItemsRequest itemsRequest);

	List<ItemListResponse>  getFlagWiseItemsList(Integer id, ItemsRequest itemsRequest);
	
	List<BatchListResponse> batchListBillableConsumptionForLoanOut(ItemsRequest itemsRequest);


	List<ItemsResponse>  getItemsListByDateExpired(ItemsRequest itemsRequest);

	List<ItemsSearchResponse> searchBarcodeScanItem( Integer itemId, Integer batchId);

	List<ItemsSearchResponse>  searchGenericScanItem(Integer itemId);

	List<ItemsDetailsListResponse> itemsDetailsOpeningBalance(ItemsRequest itemRequest);

	ItemsDetailsResponse itemsDetailSalesWorkList(int id, int entitlementId, Integer encounterId, Integer patientCategoryId, Integer patientClassId,int unitId, int orgId, int storeId) throws Exception;
	
	ItemsDetailsResponse itemsDetailSalesWorkListByAdmission(int id, int entitlementId, Integer admissionId, int storeId, int unitId) throws Exception;
	
	List<ItemsDetailsListResponse> itemsDetailsOpeningBalancePT(ItemsRequest itemsRequest);
	
	List<ItemListCompareMinResponse> listWithMappedStoresWithCompareAvalablity(ItemsRequest itemRequest);

	ItemsDetailsResponse itemDetail(int id, int unitId, int storeId) throws CustomException;

	List<ItemListResponse> getIssuingAndReuestedStoreWiseItemsListAccessories(ItemsRequest itemsRequest);

	List<ItemListResponse> itemsList1(ItemsRequest itemRequest);

	List<ItemListResponse> itemsSearchForPendingIssueItemSearch(Integer unitId, boolean isOtc,
			boolean isNarcotic, String search, Integer storeId);

	List<ItemsDetailsResponse>  getKitItemsOnWalkInSales(ItemsRequest itemsRequest)throws CustomException;

	List<ItemsDetailsListResponse> getGenericColorMapping(int unitId, int orgId);

	List<ItemsSearchResponse> itemsSearchStockAdjustment(Integer unitId, boolean isOtc, String search,
			Integer storeId, Integer assetTypeId);

	ItemsDetailsResponse itemsDetailWalkInPrescription(Integer id, Integer entitlementId, Integer unitId, Integer orgId,
			Integer storeId);

	List<ItemsDetailsResponse> getUOMForCoversheet(ItemsRequest itemsRequest) throws CustomException;

    public Response updateBatchQuantityForBillable(List<BatchListResponse> batchList)  throws CustomException;
}
