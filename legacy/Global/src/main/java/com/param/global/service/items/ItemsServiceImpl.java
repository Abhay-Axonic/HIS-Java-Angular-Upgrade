package com.param.global.service.items;

import in.param.exception.ApplicationException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Rack;
import com.param.entity.model.master.Shelf;
import com.param.entity.model.master.StoreItemMapper;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.BatchAccessoriesMapperDto;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.BatchResponse1;
import com.param.global.dto.items.BatchSearchResponse;
import com.param.global.dto.items.ItemListCompareMinResponse;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsDetail;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsDetailsResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.ItemsResponse;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.dto.items.OpeningBalanceItem;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.RackResponse;
import com.param.global.dto.items.ShelfResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;

@Service
public class ItemsServiceImpl implements IItemsService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(ItemsServiceImpl.class);

	@Autowired
	IItemsDao ItemsDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;

	@Autowired
	IGlobalDao IGlobalDao;

	@Override
	@Transactional
	public List<ItemsSearchResponse> itemsSearch(Integer unitId, boolean isOtc, String search, Integer storeId) {
		return ItemsDao.itemsSearch(unitId, isOtc, search, storeId);
	}

	@Override
	@Transactional
	public List<BatchSearchResponse> batchSearch(boolean isOtc, String search) {
		return ItemsDao.batchSearch(isOtc, search);
	}

	@Override
	@Transactional
	public List<BatchSearchResponse> expiryAndAvailableQuantityWisebatches(boolean isOtc, String search, Integer unitd,
			Integer storeId) {
		return ItemsDao.expiryAndAvailableQuantityWisebatches(isOtc, search, unitd, storeId);
	}

	@Override
	@Transactional
	public ItemsDetailsResponse itemDetail(int id, int unitId, int storeId) throws CustomException {

		BatchProperty batchProperty = new BatchProperty();
		batchProperty.setIsIndent(false);
		List<Batch> batchList = GlobalDao.getBatchDetails(id, unitId, storeId, batchProperty);
		ItemsDetailsResponse response = new ItemsDetailsResponse();
		try {
			if (batchList.isEmpty()) {
				throw new CustomException("Selected item is out of stock");
				// response.setMessage("Selected item is out of stock");
				// return response;
			} else {
				response = GlobalDao.itemsDetail(id);
				Integer currentStock = 0;

				for (Batch batch : batchList) {
					response.addBatch(Util.PopulateBatch(batch));
					currentStock = currentStock + batch.getCurrentQuantity();
				}

				response.setCurrentStock(currentStock);

				return response;
			}
		} catch (CustomException e) {
			throw new CustomException("Selected item is out of stock");
		} catch (Exception e) {
			logger.error("itemDetail failed", e);
		}
		return response;
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> itemsDetailsForDirectPurchaseRequest(ItemsRequest itemsRequest) {

		ItemsDetail itemsDetails = new ItemsDetail();
		long count = ItemsDao.productCategoryCount(itemsRequest.getItemIds());
		List<ItemsDetailsListResponse> response = null;
		if (count == 0) {
			response = ItemsDao.itemsDetails(itemsRequest.getItemIds(), itemsRequest.getStoreId(),
					itemsRequest.getUnitId());
		} else if ((count != 0 && count > 0)) {
			response = ItemsDao.itemsDetailsFixedAsset(itemsRequest.getItemIds(), itemsRequest.getStoreId());
		}

		for (ItemsDetailsListResponse item : response) {
			Long storeStock = ItemsDao.getStockAvailable1(itemsRequest.getStoreId(),item.getItemId(),itemsRequest.getUnitId());
			item.setStockAvailable(storeStock);
			Item i = GlobalDao.getItemById(item.getItemId());
			item.setIsBatchRequired(i.getIsBatchRequired());
			item.setIsThreeUOMApplicable(i.getIsThreeUOMApplicable());
			// Long storeStock = ItemsDao.getStockAvailable(itemsRequest.getStoreId(),
			// item.getItemId());
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(itemsRequest.getStoreId(), item.getItemId());
			// Long lastMonthSale = GlobalDao.getLastMonthSale(itemsRequest.getStoreId(),
			// item.getItemId());
			// Long currentMonthSale =
			// GlobalDao.getCurrentMonthSale(itemsRequest.getStoreId(), item.getItemId());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getOrganizationId());

			// Long approvedPRAndPOQuantity =
			// ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(),item.getItemId());
			// Long approvedPOQuantity =
			// ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(),item.getItemId());

			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,
					ItemsDao.getStockAvailable(itemsRequest.getStoreId(), item.getItemId(), itemsRequest.getUnitId()),
					stockInTransit,
					ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(), item.getItemId(),
							itemsRequest.getUnitId()),
					ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(), item.getItemId(),
							itemsRequest.getUnitId()));
			// item.setStockAvailable(maxOrderQuantity);
			item.setStockInTransit(stockInTransit);
			item.setLastMonthSale(
					GlobalDao.getLastMonthSale(itemsRequest.getStoreId(), item.getItemId(), itemsRequest.getUnitId()));
			item.setCurrentMonthSale(GlobalDao.getCurrentMonthSale(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId()));
			item.setMaxOrderQuantity(maxOrderQuantity);
			item.setMaxStoreQuantity(maxStoreQuantity);
			item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(), itemsRequest.getOrganizationId()));
			item.setTaxList(getTaxList(itemsRequest.getOrganizationId()));
		}
		return response;
	}

	@Override
	@Transactional
	public ItemsDetailsResponse itemsDetail(int id, int entitlementId, int unitId, int orgId, int storeId)
			throws Exception {

		BatchProperty batchProperty = new BatchProperty();
		batchProperty.setIsIndent(false);
		// List<Batch> batchList = GlobalDao.getBatchDetails(id, batchProperty);
		List<Batch> batchList = GlobalService.getBatchDetailsGlobal(id, unitId, batchProperty, storeId);
		ItemsDetailsResponse response = new ItemsDetailsResponse();

		try {
			if (batchList.isEmpty()) {
				throw new CustomException("Selected item is out of stock");
				// response.setMessage("Selected item is out of stock");
				// return response;
			} else {
				response = GlobalDao.itemsDetailTariff(id);
				List<Integer> prodCatIds = new ArrayList<Integer>();
				prodCatIds.add(response.getProductCategoryId());
				response.setColorCode(GlobalDao.getColorCode(response.getGenericId(),orgId,unitId));
				Integer assetCatId = 0;
				if (batchList.get(0).getItem().getProductCategory() != null) {
					assetCatId = batchList.get(0).getItem().getProductCategory().getAssetCategory().getId();
				} else {
					assetCatId = batchList.get(0).getItem().getGeneric().getProductCategory().getAssetCategory()
							.getId();
				}

				response.setAssetCategoryId(assetCatId);
				List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, entitlementId);
				if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
					if (pharmacyTariffs.size() > 1) {
						// throw exception?
						// Don't throw - get first object and set data.
					}

					PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
					response.setMarkUpDown(pharmacyTariff.getMarkUpDown());
					response.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
				}

				Integer currentStock = 0;

				for (Batch batch : batchList) {
					BatchResponse batchResponse = new BatchResponse();
					batchResponse = Util.PopulateBatch(batch);
					batchResponse.setAvailableStockQuantity(batch.getCurrentQuantity());
					batchResponse.setTaxId(batch.getTax().getId());
					batchResponse.setTaxPercentage(batch.getTaxPercentage());
					response.addBatch(batchResponse);
					if (batch.getBatchNo() != null) {
						currentStock = currentStock + batch.getCurrentQuantity();
					} else {
						currentStock = batch.getCurrentQuantity();
					}
				}
				response.setCurrentStock(currentStock);
				StoreItemMapper resData = GlobalDao.getItemMinMaxData(id, unitId, storeId);
				if (resData != null) {
					response.setMinQty(resData.getMinQty());
					response.setMaxQty(resData.getMaxQty());
					response.setSellable(resData.getIsSellable());

				}
				return response;
			}
		} catch (CustomException e) {
			throw new CustomException("Selected item is out of stock");
		} catch (Exception e) {
			logger.error("itemsDetail failed", e);
		}
		return response;
	}

	@Override
	@Transactional
	public ItemsDetailsResponse itemsDetailSalesWorkList(int id, int entitlementId, Integer encounterId,
			Integer patientCategoryId, Integer patientClassId, int unitId, int orgId, int storeId) throws Exception {

		BatchProperty batchProperty = new BatchProperty();
		batchProperty.setIsIndent(false);
		// List<Batch> batchList = GlobalDao.getBatchDetails(id, batchProperty);
		List<Batch> batchList = GlobalService.getBatchDetailsGlobal(id, unitId, batchProperty, storeId);
		Integer patientCatId = patientCategoryId != null ? patientCategoryId
				: GlobalDao.getPatientCategoryId(encounterId);

		ItemsDetailsResponse response = new ItemsDetailsResponse();
		try {
			if (batchList.isEmpty()) {
				throw new CustomException("Selected item is out of stock");
				// response.setMessage("Selected item is out of stock");
				// return response;
			} else {
				response = GlobalDao.itemsDetailTariff(id);
				List<Integer> prodCatIds = new ArrayList<Integer>();
				prodCatIds.add(response.getProductCategoryId());

				List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, entitlementId);
				if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
					if (pharmacyTariffs.size() > 1) {
						// throw exception?
						// Don't throw - get first object and set data.
					}

					PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
					response.setMarkUpDown(pharmacyTariff.getMarkUpDown());
					response.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
				}

				/*
				 * if (encounterId != null && encounterId>0) {
				 */
				Integer assetCatId = 0;
				Integer prodCatId = 0;
				if (batchList.get(0).getItem().getProductCategory() != null) {
					assetCatId = batchList.get(0).getItem().getProductCategory().getAssetCategory().getId();
					prodCatId = batchList.get(0).getItem().getProductCategory().getId();
				} else {
					assetCatId = batchList.get(0).getItem().getGeneric().getProductCategory().getAssetCategory()
							.getId();
					prodCatId = batchList.get(0).getItem().getGeneric().getProductCategory().getId();
				}

				response.setAssetCategoryId(assetCatId);
				PatientConsession patientConsession = IGlobalDao.getPatientConsessionEncounter(id, encounterId,
						patientCatId, assetCatId, prodCatId, patientClassId, entitlementId);
				if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
					response.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
					response.setMarkupStatus(patientConsession.isMarkupStatus());
					response.setPriorityId(patientConsession.getPriorityId());
				} else {
					response.setPatientClassPercentage(0.0D);
					response.setMarkupStatus(false);
					response.setPriorityId(null);
				}

				/*
				 * } else { response.setPatientClassPercentage(0.0D);
				 * response.setMarkupStatus(false); response.setPriorityId(null); }
				 */
				response.setColorCode(GlobalDao.getColorCode(response.getGenericId(),orgId,unitId));
				Integer currentStock = 0;
				for (Batch batch : batchList) {
					response.addBatch(Util.PopulateBatch(batch));
					if (batch.getBatchNo() != null) {
						currentStock = currentStock + batch.getCurrentQuantity();
					} else {
						currentStock = batch.getCurrentQuantity();
					}
				}
				response.setCurrentStock(currentStock);
				return response;
			}
		} catch (CustomException e) {
			throw new CustomException("Selected item is out of stock");
		} catch (Exception e) {
			logger.error("itemsDetailSalesWorkList failed", e);
		}
		return response;
	}

	@Override
	@Transactional
	public ItemsDetailsResponse itemsDetailSalesWorkListByAdmission(int id, int entitlementId, Integer admissionId,
			int storeId, int unitId) throws Exception {

		BatchProperty batchProperty = new BatchProperty();
		batchProperty.setIsIndent(false);
		List<Batch> batchList = GlobalDao.getBatchDetails(id, unitId, storeId, batchProperty);
		Integer patientCatId = GlobalDao.getPatientCategoryIdAdmission(admissionId);

		ItemsDetailsResponse response = new ItemsDetailsResponse();
		try {
			if (batchList.isEmpty()) {
				throw new CustomException("Selected item is out of stock");
				// response.setMessage("Selected item is out of stock");
				// return response;
			} else {
				response = GlobalDao.itemsDetailTariff(id);
				List<Integer> prodCatIds = new ArrayList<Integer>();
				prodCatIds.add(response.getProductCategoryId());

				List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, entitlementId);
				if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
					if (pharmacyTariffs.size() > 1) {
						// throw exception?
						// Don't throw - get first object and set data.
					}

					PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
					response.setMarkUpDown(pharmacyTariff.getMarkUpDown());
					response.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
				}

				if (admissionId != null) {
					Integer assetCatId = 0;
					Integer prodCatId = 0;
					if (batchList.get(0).getItem().getProductCategory() != null) {
						assetCatId = batchList.get(0).getItem().getProductCategory().getAssetCategory().getId();
						prodCatId = batchList.get(0).getItem().getProductCategory().getId();
					} else {
						assetCatId = batchList.get(0).getItem().getGeneric().getProductCategory().getAssetCategory()
								.getId();
						prodCatId = batchList.get(0).getItem().getGeneric().getProductCategory().getId();
					}
					response.setAssetCategoryId(assetCatId);
					PatientConsession patientConsession = IGlobalDao.getPatientConsessionAdmission(id, admissionId,
							patientCatId, assetCatId, prodCatId);

					if (patientConsession.isMarkupStatus() == false) {
						response.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
						response.setMarkupStatus(patientConsession.isMarkupStatus());
						response.setPriorityId(patientConsession.getPriorityId());
					} else {
						response.setPatientClassPercentage(0.0D);
						response.setMarkupStatus(false);
						response.setPriorityId(null);
					}
				} else {
					response.setPatientClassPercentage(0.0D);
					response.setMarkupStatus(false);
					response.setPriorityId(null);
				}

				Integer currentStock = 0;
				for (Batch batch : batchList) {
					response.addBatch(Util.PopulateBatch(batch));
					currentStock = currentStock + batch.getCurrentQuantity();
				}
				response.setCurrentStock(currentStock);
				return response;
			}
		} catch (CustomException e) {
			throw new CustomException("Selected item is out of stock");
		} catch (Exception e) {
			logger.error("itemsDetailSalesWorkListByAdmission failed", e);
		}
		return response;
	}

	@Override
	@Transactional
	public List<ItemListResponse> itemsList(ItemsRequest itemRequest) {
		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.itemsList(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetItemsList(itemRequest);
		}
		return null;
	}
	
	

	@Override
	@Transactional
	public List<ItemListResponse> getItemsForStoreIndentList(ItemsRequest itemRequest) {
		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.itemsListForStoreIndent(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetItemsListForAssetIndent(itemRequest);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ItemListResponse> itemsListWithTax(ItemsRequest itemRequest) {
		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.itemsListWithTax(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetItemsList(itemRequest);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ItemsResponse> itemsListByBatchExpiry(ItemsRequest itemsRequest) {
		List<ItemsResponse> response = ItemsDao.itemsListByBatchExpiry(itemsRequest);

		for (ItemsResponse batch : response) {
			List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());

			if (uomList != null && !uomList.isEmpty()) {
				UomResponse sUom = Util.getStoreUom(uomList);
				UomResponse dUom = Util.getOpDispenseUom(uomList);

				Integer conversion = sUom.getConversion() * dUom.getConversion();

				batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(), sUom.getUomUnit());
				batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
			}
		}
		return response;
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> itemsDetails(ItemsRequest itemsRequest) {

		ItemsDetail itemsDetails = new ItemsDetail();
		long count = ItemsDao.productCategoryCount(itemsRequest.getItemIds());
		List<ItemsDetailsListResponse> response = null;
		if (count == 0) {
			response = ItemsDao.itemsDetails(itemsRequest.getItemIds(), itemsRequest.getStoreId(),
					itemsRequest.getUnitId());
		} else if ((count != 0 && count > 0)) {
			response = ItemsDao.itemsDetailsFixedAsset(itemsRequest.getItemIds(), itemsRequest.getStoreId());
		}

		for (ItemsDetailsListResponse item : response) {
			Item i = GlobalDao.getItemById(item.getItemId());
			item.setIsBatchRequired(i.getIsBatchRequired());
			item.setIsThreeUOMApplicable(i.getIsThreeUOMApplicable());
			// System.out.println("itemsRequest.itemId() :-" + itemsRequest.getItemIds()+
			// "\nItem.itemId : " + item.getItemId());

			Long storeStock = ItemsDao.getStockAvailable(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			item.setStockAvailable(storeStock);

			// System.out.println("storeStock:-" + storeStock);

			/*
			 * Long storeStockNew = ItemsDao.getStockAvailable(itemsRequest.getStoreId(),
			 * itemsRequest.getItemId()); //System.out.println("storeStockNew:-" +
			 * storeStockNew);
			 */
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(itemsRequest.getStoreId(), item.getItemId());
			// System.out.println("stockInTransit:-" + stockInTransit);

			Long lastMonthSale = GlobalDao.getLastMonthSale(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Long lastMonthSaleNew = GlobalDao.getLastMonthSaleNew(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			lastMonthSale = lastMonthSaleNew;
			Long currentMonthSale = GlobalDao.getCurrentMonthSale(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());

			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getOrganizationId());
			// System.out.println("maxStoreQuantity:-" + maxStoreQuantity);

			Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(),
					item.getItemId(), itemsRequest.getUnitId());
			// System.out.println("approvedPRAndPOQuantity:-" + approvedPRAndPOQuantity);

			Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(),
					item.getItemId(), itemsRequest.getUnitId());
			// System.out.println("approvedPOQuantity:-" + approvedPOQuantity);

			// System.out.println("maxStoreQuantity:-" + maxStoreQuantity + "\nstoreStock :
			// " + storeStock+"\nstockInTransit :
			// "+stockInTransit+"\napprovedPRAndPOQuantity : "+approvedPRAndPOQuantity+"\n
			// approvedPOQuantity : "+approvedPOQuantity);

			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
					approvedPRAndPOQuantity, approvedPOQuantity);
			// System.out.println("maxOrderQuantity:-" + maxOrderQuantity);

			if (itemsRequest.getScreenId() != null
					&& (itemsRequest.getScreenId().equals(Screen.PurchaseRequest.getScreen())
							|| itemsRequest.getScreenId().equals(Screen.LocalPurchaseRequest.getScreen()))) {
				maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
						approvedPRAndPOQuantity, approvedPOQuantity);
				// System.out.println(" Inside If maxOrderQuantity:-" + maxOrderQuantity);
				/*
				 * maxStoreQuantity - (item.getStockAvailable() + stockInTransit +
				 * ItemsDao.getLeaseQunatity(itemsRequest.getStoreId(), item.getItemId()));
				 */
			}
			if (maxOrderQuantity < 0) {
				maxOrderQuantity = 0L;
			}
			// item.setStockAvailable(maxOrderQuantity);
			item.setStockInTransit(stockInTransit);
			item.setLastMonthSale(lastMonthSale);
			item.setCurrentMonthSale(currentMonthSale);
			item.setMaxOrderQuantity(maxOrderQuantity);
			item.setMaxStoreQuantity(maxStoreQuantity);
			item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(), itemsRequest.getOrganizationId()));
			item.setTaxList(getTaxList(itemsRequest.getOrganizationId()));

			List<RackResponse> racks = new ArrayList<RackResponse>();
			for (Rack rack : GlobalDao.getRacksForOpeningBalance(itemsRequest.getStoreId(),
					itemsRequest.getOrganizationId())) {
				if (rack.getShelfList() != null && rack.getShelfList().size() > 0) {
					List<ShelfResponse> shelfs = new ArrayList<ShelfResponse>();
					if (rack.getShelfList() != null && rack.getShelfList().size() > 0) {
						for (Shelf shelf : rack.getShelfList()) {
							List<OpeningBalanceItem> itemList = new ArrayList<OpeningBalanceItem>();
							if (shelf.getShelfItem() != null && shelf.getShelfItem().size() > 0) {
								for (ShelfItem shelfItem : shelf.getShelfItem()) {
									OpeningBalanceItem itemObject = new OpeningBalanceItem(
											shelfItem.getBatch().getItem().getCode(),
											shelfItem.getBatch().getItem().getName(),
											shelfItem.getBatch().getBatchNo());
									itemList.add(itemObject);
								}
								ShelfResponse shelfObject = new ShelfResponse(shelf.getId(), shelf.getName(), itemList);
								shelfs.add(shelfObject);
							} else {
								// Shelf s=GlobalDao.getShelf();
								ShelfResponse shelfObject = new ShelfResponse(shelf.getId(), shelf.getName(), itemList);
								shelfs.add(shelfObject);
							}
						}
						RackResponse rackObject = new RackResponse(rack.getId(), rack.getName(), shelfs);
						racks.add(rackObject);
					}
				}
			}
			item.setRacks(racks);
		}

		return response;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public List<ItemsDetailsListResponse>
	 * itemsDetails(ItemsRequest itemsRequest) {
	 * 
	 * ItemsDetail itemsDetails = new ItemsDetail(); long count =
	 * ItemsDao.productCategoryCount(itemsRequest.getItemIds());
	 * List<ItemsDetailsListResponse> response = null; if (count == 0) { response =
	 * ItemsDao.itemsDetails(itemsRequest.getItemIds(), itemsRequest.getStoreId());
	 * } else if ((count != 0 && count > 0)) { response =
	 * ItemsDao.itemsDetailsFixedAsset(itemsRequest.getItemIds(),
	 * itemsRequest.getStoreId()); }
	 * 
	 * for (ItemsDetailsListResponse item : response) { Item i =
	 * GlobalDao.getItemById(item.getItemId());
	 * item.setIsBatchRequired(i.getIsBatchRequired());
	 * 
	 * <<<<<<< HEAD System.out.println( "itemsRequest.itemId()  :-" +
	 * itemsRequest.getItemIds() + "\nItem.itemId : " + item.getItemId()); =======
	 * //System.out.println("itemsRequest.itemId()  :-" + itemsRequest.getItemIds()+
	 * "\nItem.itemId : " + item.getItemId());
	 * 
	 * Long storeStock =
	 * ItemsDao.getStockAvailable(itemsRequest.getStoreId(),item.getItemId());
	 * >>>>>>> dev
	 * 
	 * Long storeStock = ItemsDao.getStockAvailable(itemsRequest.getStoreId(),
	 * item.getItemId());
	 * 
	 * <<<<<<< HEAD System.out.println("storeStock:-" + storeStock);
	 * 
	 * 
	 * Long storeStockNew = ItemsDao.getStockAvailable(itemsRequest.getStoreId(),
	 * itemsRequest.getItemId()); System.out.println("storeStockNew:-" +
	 * storeStockNew);
	 * 
	 * ======= //System.out.println("storeStock:-" + storeStock);
	 * 
	 * Long storeStockNew = ItemsDao.getStockAvailable(itemsRequest.getStoreId(),
	 * itemsRequest.getItemId()); //System.out.println("storeStockNew:-" +
	 * storeStockNew);
	 * 
	 * >>>>>>> dev Long stockInTransit =
	 * GlobalDao.getStoreStockInTransitByItem(itemsRequest.getStoreId(),
	 * item.getItemId()); //System.out.println("stockInTransit:-" + stockInTransit);
	 * 
	 * Long lastMonthSale = GlobalDao.getLastMonthSale(itemsRequest.getStoreId(),
	 * item.getItemId()); Long currentMonthSale =
	 * GlobalDao.getCurrentMonthSale(itemsRequest.getStoreId(), item.getItemId());
	 * 
	 * Integer maxStoreQuantity =
	 * GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(),
	 * item.getItemId()); <<<<<<< HEAD System.out.println("maxStoreQuantity:-" +
	 * maxStoreQuantity);
	 * 
	 * Long approvedPRAndPOQuantity =
	 * ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(),
	 * item.getItemId()); System.out.println("approvedPRAndPOQuantity:-" +
	 * approvedPRAndPOQuantity);
	 * 
	 * Long approvedPOQuantity =
	 * ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(),
	 * item.getItemId()); System.out.println("approvedPOQuantity:-" +
	 * approvedPOQuantity);
	 * 
	 * System.out.println("maxStoreQuantity:-" + maxStoreQuantity +
	 * "\nstoreStock : " + storeStock + "\nstockInTransit : " + stockInTransit +
	 * "\napprovedPRAndPOQuantity : " + approvedPRAndPOQuantity +
	 * "\n approvedPOQuantity : " + approvedPOQuantity);
	 * 
	 * Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,
	 * storeStock, stockInTransit, approvedPRAndPOQuantity, approvedPOQuantity);
	 * System.out.println("maxOrderQuantity:-" + maxOrderQuantity); =======
	 * //System.out.println("maxStoreQuantity:-" + maxStoreQuantity);
	 * 
	 * Long approvedPRAndPOQuantity =
	 * ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(),
	 * item.getItemId()); //System.out.println("approvedPRAndPOQuantity:-" +
	 * approvedPRAndPOQuantity);
	 * 
	 * Long approvedPOQuantity =
	 * ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(),
	 * item.getItemId()); //System.out.println("approvedPOQuantity:-" +
	 * approvedPOQuantity);
	 * 
	 * //System.out.println("maxStoreQuantity:-" + maxStoreQuantity +
	 * "\nstoreStock : " + storeStock+"\nstockInTransit : "
	 * +stockInTransit+"\napprovedPRAndPOQuantity : "
	 * +approvedPRAndPOQuantity+"\n approvedPOQuantity : "+approvedPOQuantity);
	 * 
	 * Long maxOrderQuantity =
	 * GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit ,
	 * approvedPRAndPOQuantity , approvedPOQuantity);
	 * //System.out.println("maxOrderQuantity:-" + maxOrderQuantity); >>>>>>> dev
	 * 
	 * if (itemsRequest.getScreenId() != null &&
	 * (itemsRequest.getScreenId().equals(Screen.PurchaseRequest.getScreen()) ||
	 * itemsRequest.getScreenId().equals(Screen.LocalPurchaseRequest.getScreen())))
	 * { <<<<<<< HEAD maxOrderQuantity =
	 * GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
	 * approvedPRAndPOQuantity, approvedPOQuantity);
	 * System.out.println(" Inside If maxOrderQuantity:-" + maxOrderQuantity);
	 * 
	 * maxStoreQuantity - (item.getStockAvailable() + stockInTransit +
	 * ItemsDao.getLeaseQunatity(itemsRequest.getStoreId(), item.getItemId()));
	 * 
	 * ======= maxOrderQuantity =
	 * GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit ,
	 * approvedPRAndPOQuantity , approvedPOQuantity);
	 * //System.out.println(" Inside If maxOrderQuantity:-" + maxOrderQuantity);
	 * maxStoreQuantity - (item.getStockAvailable() + stockInTransit +
	 * ItemsDao.getLeaseQunatity(itemsRequest.getStoreId(), item.getItemId()));
	 * >>>>>>> dev } if (maxOrderQuantity < 0) { maxOrderQuantity = 0L; } //
	 * item.setStockAvailable(maxOrderQuantity);
	 * item.setStockInTransit(stockInTransit); item.setLastMonthSale(lastMonthSale);
	 * item.setCurrentMonthSale(currentMonthSale);
	 * item.setMaxOrderQuantity(maxOrderQuantity);
	 * item.setMaxStoreQuantity(maxStoreQuantity);
	 * item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId()));
	 * item.setTaxList(getTaxList());
	 * 
	 * List<RackResponse> racks = new ArrayList<RackResponse>(); for (Rack rack :
	 * GlobalDao.getRacksForOpeningBalance(itemsRequest.getStoreId())) {
	 * List<ShelfResponse> shelfs = new ArrayList<ShelfResponse>(); for (Shelf shelf
	 * : rack.getShelfList()) { List<OpeningBalanceItem> itemList = new
	 * ArrayList<OpeningBalanceItem>(); for (ShelfItem shelfItem :
	 * shelf.getShelfItem()) { OpeningBalanceItem itemObject = new
	 * OpeningBalanceItem(shelfItem.getBatch().getItem().getCode(),
	 * shelfItem.getBatch().getItem().getName(), shelfItem.getBatch().getBatchNo());
	 * itemList.add(itemObject); } ShelfResponse shelfObject = new
	 * ShelfResponse(shelf.getId(), shelf.getName(), itemList);
	 * shelfs.add(shelfObject); } RackResponse rackObject = new
	 * RackResponse(rack.getId(), rack.getName(), shelfs); racks.add(rackObject); }
	 * item.setRacks(racks); }
	 * 
	 * return response; }
	 */

	/*
	 * @Override
	 * 
	 * @Transactional public List<ItemsDetailsListResponse>
	 * itemsDetailsForStoreIndent(ItemsRequest itemsRequest) {
	 * 
	 * 
	 * ItemsDetail itemsDetails = new ItemsDetail(); long count =
	 * ItemsDao.productCategoryCount(itemsRequest.getItemIds());
	 * List<ItemsDetailsListResponse> response = null; if (count == 0) { response =
	 * ItemsDao.itemsDetails(itemsRequest.getItemIds(), itemsRequest.getStoreId());
	 * } else if ((count != 0 && count > 0)) { response =
	 * ItemsDao.itemsDetailsFixedAsset(itemsRequest.getItemIds(),
	 * itemsRequest.getStoreId()); }
	 * 
	 * for (ItemsDetailsListResponse item : response) { Item i =
	 * GlobalDao.getItemById(item.getItemId());
	 * item.setIsBatchRequired(i.getIsBatchRequired()); Long storeStock =
	 * GlobalDao.getStoreStockByItem(itemsRequest.getStoreId(), item.getItemId());
	 * Long stockInTransit =
	 * GlobalDao.getStoreStockInTransitByItem(itemsRequest.getStoreId(),
	 * item.getItemId()); Long lastMonthSale =
	 * GlobalDao.getLastMonthSale(itemsRequest.getStoreId(), item.getItemId()); Long
	 * currentMonthSale = GlobalDao.getCurrentMonthSale(itemsRequest.getStoreId(),
	 * item.getItemId()); Integer maxStoreQuantity =
	 * GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(),
	 * item.getItemId());
	 * 
	 * Long approvedPRAndPOQuantity =
	 * ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(),
	 * item.getItemId()); Long approvedPOQuantity =
	 * ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(),
	 * item.getItemId()); Long availableStock = 0L; Long maxOrderQuantity =
	 * maxStoreQuantity - (storeStock + stockInTransit + approvedPRAndPOQuantity +
	 * approvedPOQuantity); // item.setStockAvailable(maxOrderQuantity);
	 * //System.out.println("Store Stock : " + storeStock);
	 * //System.out.println("Stock In Transit : " + stockInTransit);
	 * //System.out.println("Approved PO and PR  : " + approvedPRAndPOQuantity);
	 * //System.out.println("Approved PO : " + approvedPOQuantity);
	 * //System.out.println("Max Order  : " + maxOrderQuantity);
	 * item.setStockInTransit(stockInTransit); item.setLastMonthSale(lastMonthSale);
	 * item.setCurrentMonthSale(currentMonthSale);
	 * item.setMaxOrderQuantity(maxOrderQuantity);
	 * item.setMaxStoreQuantity(maxStoreQuantity);
	 * item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId()));
	 * item.setTaxList(getTaxList()); } return response; }
	 */

	private List<TaxDropDown> getTaxList(Integer orgId) {
		List<TaxDropDown> taxList = new ArrayList<TaxDropDown>();
		for (Tax tax : GlobalDao.getTaxList(orgId)) {

			TaxDropDown taxDto = new TaxDropDown(tax.getId(), tax.getName(), tax.getTaxPercentage());
			taxList.add(taxDto);
		}
		return taxList;
	}

	@Override
	@Transactional
	public List<ItemListResponse> workOrderItemsList(ItemsRequest itemRequest) {

		if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.workOrderItemsList(itemRequest);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> itemsDetailsWithBatch(ItemsRequest itemsRequest) {
		List<ItemsDetailsListResponse> response = ItemsDao.itemsDetailsFixedAsset(itemsRequest.getItemIds(),
				itemsRequest.getStoreId());
		for (ItemsDetailsListResponse item : response) {
			item.setBatch(
					ItemsDao.batchListByItemId(item.getItemId(), itemsRequest.getStoreId(), itemsRequest.getUnitId()));

		}
		return response;
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> itemsDetailsWithBatchForWorkOrder(ItemsRequest itemsRequest) {
		List<ItemsDetailsListResponse> response = ItemsDao.itemsDetailsFixedAsset(itemsRequest.getItemIds(),
				itemsRequest.getStoreId());
		for (ItemsDetailsListResponse item : response) {
			item.setBatch(
					ItemsDao.batchListByItemId(item.getItemId(), itemsRequest.getStoreId(), itemsRequest.getUnitId()));
			item.setTaxList(GlobalDao.getTaxListForWorkOrder(itemsRequest.getOrganizationId()));
			item.setCurrentVendorId(1);
			item.setCurrentVendorName("ABC");
		}
		return response;
	}

	@Override
	@Transactional
	public List<BatchListResponse> batchList(ItemsRequest itemRequest) {

		List<BatchListResponse> response = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			response = ItemsDao.batchList(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchList(itemRequest);
		}

		VisitType visitType = null;
		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (itemRequest.getVisitTypeId() != null)
			visitType = GlobalDao.getVisitType(itemRequest.getVisitTypeId());
		if (visitType != null) {
			if (visitType.getVisitTypeCode().equals("OP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
			else if (visitType.getVisitTypeCode().equals("IP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(itemRequest.getAdmissionId());
		}

		for (BatchListResponse batch : response) {
			if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

				List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());

				if (uomList != null && !uomList.isEmpty()) {
					batch.setUomResponses(uomList);
					UomResponse sUom = Util.getStoreUom(uomList);
					UomResponse dUom = Util.getOpDispenseUom(uomList);
					if (sUom != null & dUom != null) {
						Integer conversion = sUom.getConversion() * dUom.getConversion();
						batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(), sUom.getUomUnit());
						batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
					}
				}

				if (patientSponsorDetails != null && batch.getProdCatId() != null) {

					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(batch.getProdCatId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}
			}
			if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
						.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
				batch.setAccessories(batchAccessoriesList);
			}

		}

		return response;
	}

	@Override

	@Transactional
	public List<ItemsDetailsListResponse> batchDetails(ItemsRequest itemsRequest) {

		List<ItemsDetailsListResponse> response = ItemsDao.itemsDetails(itemsRequest.getItemIds(),
				itemsRequest.getStoreId(), itemsRequest.getUnitId());

		for (ItemsDetailsListResponse item : response) {

			Long storeStock = GlobalDao.getStoreStockByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Long stockInTransit = GlobalDao.getstockInTransitByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getOrganizationId());

			// Integer maxStoreQuantity =
			// GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(),
			// item.getItemId(),itemsRequest.getOrganizationId());
			// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);

			Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(),
					item.getItemId(), itemsRequest.getUnitId());
			// System.out.println("approvedPRAndPOQuantity:-" + approvedPRAndPOQuantity);

			Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(),
					item.getItemId(), itemsRequest.getUnitId());
			// System.out.println("approvedPOQuantity:-" + approvedPOQuantity);

			// System.out.println("maxStoreQuantity:-" + maxStoreQuantity + "\nstoreStock :
			// " + storeStock+"\nstockInTransit :
			// "+stockInTransit+"\napprovedPRAndPOQuantity : "+approvedPRAndPOQuantity+"\n
			// approvedPOQuantity : "+approvedPOQuantity);

			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
					approvedPRAndPOQuantity, approvedPOQuantity);

			if (maxOrderQuantity < 0) {
				maxOrderQuantity = 0L;
			}
			item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(), itemsRequest.getOrganizationId()));
			item.setStockAvailable(GlobalDao.getStoreStockByItem(itemsRequest.getToStoreId(), item.getItemId(),
					itemsRequest.getUnitId()));
			item.setMaxOrderQuantity(maxOrderQuantity);
			BatchProperty batchProperty = new BatchProperty();
			batchProperty.setIsIndent(false);

			for (Batch batch : GlobalDao.getBatchDetails(item.getItemId(), itemsRequest.getUnitId(),
					itemsRequest.getToStoreId(), batchProperty)) {
				item.addBatch(Util.PopulateBatchWithStock(batch));
			}
		}

		return response;
	}

	@Override
	@Transactional
	public List<BatchListResponse> batchList(ItemsRequest itemRequest, boolean isPharmacy, boolean isReturn) {
		List<BatchListResponse> response = null;
		BatchProperty batchProperty = new BatchProperty();
		batchProperty.setIsPharmacy(isPharmacy);
		batchProperty.setIsReturn(isReturn);
		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			response = ItemsDao.batchList(itemRequest, batchProperty);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchList(itemRequest, batchProperty);
		}

		for (BatchListResponse batch : response) {
			if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

				List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());

				if (uomList != null && !uomList.isEmpty()) {

					UomResponse sUom = Util.getStoreUom(uomList);
					UomResponse dUom = Util.getOpDispenseUom(uomList);
					if (sUom != null & dUom != null) {
						Integer conversion = sUom.getConversion() * dUom.getConversion();
						batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(), sUom.getUomUnit());
						batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
					}
				}
			}
			if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
						.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
				batch.setAccessories(batchAccessoriesList);
			}

		}

		return response;
	}

	@Override
	@Transactional
	public Object smartIssue(ItemsRequest itemRequest) {

		List<ItemsDetailsListResponse> response = new ArrayList<>();

		List<Integer> itemId = itemRequest.getItems().stream().map(ItemsDetail::getItemId).collect(Collectors.toList());

		List<BatchListResponse> batchList = GlobalDao.smartIssue(itemId, itemRequest.getStoreId());

		for (ItemsDetail item : itemRequest.getItems()) {
			Integer issueQty = item.getIssueQuantity();
			List<BatchListResponse> batches = batchList.stream().filter(i -> i.getItemId() == item.getItemId())
					.collect(Collectors.toList());
			List<BatchResponse> batchResponse = new ArrayList<>();

			for (BatchListResponse batch : batches) {
				if (issueQty > 0) {
					if (batch.getAvailableQuantity() >= issueQty) {
						batchResponse.add(new BatchResponse(batch.getBatchId(), issueQty, batch.getExpiry()));
						break;
					} else {
						batchResponse.add(new BatchResponse(batch.getBatchId(), batch.getAvailableQuantity().intValue(),
								batch.getExpiry()));
						issueQty = issueQty - batch.getAvailableQuantity().intValue();
					}
				} else {
					break;
				}
			}

			response.add(new ItemsDetailsListResponse(item.getItemId(), batchResponse));
		}

		return response;
	}

	@Override
	@Transactional
	public ItemsDetailsResponse itemsDetailsWithStoreId(int id, int storeId, int unitId) throws Exception {

		BatchProperty batchProperty = new BatchProperty();
		batchProperty.setIsIndent(false);
		List<Batch> batchList = GlobalDao.getBatchDetails(id, unitId, storeId, batchProperty);
		ItemsDetailsResponse response = new ItemsDetailsResponse();
		try {
			if (batchList.size() == 0) {
				throw new CustomException("Selected item is out of stock");
				// response.setMessage("Selected item is out of stock");
				// return response;
			} else {
				response = GlobalDao.itemsDetail(id);
				if (batchList != null && batchList.size() > 0) {
					for (Batch batch : batchList) {
						BatchResponse b = Util.PopulateBatchWithStock(batch);
						response.addBatch(b);
					}
				}
				return response;
			}
		} catch (CustomException e) {
			throw new CustomException("Selected item is out of stock");
		} catch (Exception e) {
		logger.error("itemsDetailsWithStoreId failed", e);
		}
		return response;
	}

	@Override
	@Transactional
	public List<ItemListResponse> getIsConsignedItemsList(ItemsRequest itemRequest, boolean isConsignment) {

		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.consignedItemsList(itemRequest, isConsignment);

			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}

			return listResponse;
		}

		return null;
	}

	@Override
	@Transactional
	public List<BatchListResponse> consignedBatchList(ItemsRequest itemRequest, boolean isConsignment) {
		List<BatchListResponse> batchListResponse = new ArrayList<BatchListResponse>();
		List<BatchListResponse> batchListMain = null;
		List<BatchListResponse> batchListItemProductCategory = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			batchListMain = ItemsDao.consignedBatchList(itemRequest, isConsignment);

			for (BatchListResponse batch : batchListMain) {
				batch.setItemId(batch.getItemid());
				batch.setAssetCategory(batch.getAssetcategory());
				batch.setProductCategory(batch.getProductcategory());
				batch.setItemName(batch.getItemname());
				batch.setBatchId(batch.getBatchid().longValue());
				batch.setBatchNo(batch.getBatchno());
				batch.setAvailableQuantity(batch.getAvailablequantity().longValue());
				batch.setLotNo(batch.getLotno());
				batch.setTaxId(batch.getTaxid());
				batch.setTaxName(batch.getTaxname());
				batch.setTaxPercentage(batch.getTaxpercentage());
				batch.setManufactureYear(batch.getManufactureyear());
				batch.setVendorId(batch.getVendorid());

				if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

					List<UomResponse> uomList = GlobalDao.getConsignedInwordBatchUomList(batch.getBatchId());
					Long outwordQuantity = GlobalDao.getConsignemntOutwordBatchWiseQty(batch.getBatchId());
					if (uomList != null && !uomList.isEmpty()) {

						UomResponse sUom = Util.getStoreUom(uomList);
						UomResponse dUom = Util.getOpDispenseUom(uomList);
						if (sUom != null & dUom != null) {
							Integer conversion = sUom.getConversion() * dUom.getConversion();
							batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(),
									sUom.getUomUnit());
							batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
						}
					}
					if (batch.getAvailableQuantity() > outwordQuantity) {
						batchListResponse.add(batch);
					}
				}

			}
		}

		return batchListResponse;
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> batchDetailsCSSD(ItemsRequest itemsRequest) {
		List<ItemsDetailsListResponse> response = ItemsDao.itemsDetails(itemsRequest.getItemIds(),
				itemsRequest.getStoreId(), itemsRequest.getUnitId());

		for (ItemsDetailsListResponse item : response) {

			Long storeStock = GlobalDao.getStoreStockByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Long stockInTransit = GlobalDao.getstockInTransitByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getOrganizationId());
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);

			item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(), itemsRequest.getOrganizationId()));
			item.setStockAvailable(storeStock);
			item.setMaxOrderQuantity(maxOrderQuantity);
			BatchProperty batchProperty = new BatchProperty();
			batchProperty.setIsIndent(false);

			for (Batch batch : GlobalDao.getBatchDetailsCSSD(item.getItemId(), itemsRequest.getUnitId(),
					itemsRequest.getStoreId(), batchProperty)) {
				item.addBatch(Util.PopulateBatchWithStock(batch));
			}
		}

		return response;
	}

	@Override
	@Transactional
	public List<ItemListResponse> getItemsListOnTemplet(ItemsRequest itemsRequest) {

		List<ItemListResponse> listResponse = ItemsDao.getItemsListOnTemplet(itemsRequest);
		// System.out.println(listResponse.size());
		for (ItemListResponse itemListResponse : listResponse) {
			itemListResponse.setItemId(itemListResponse.getItemid());
			itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
			itemListResponse.setItemCode(itemListResponse.getItemcode());
			itemListResponse.setItemName(itemListResponse.getItemname());
			itemListResponse.setProductCategory(itemListResponse.getProductcategory());
		}
		return listResponse;
	}

	@Override
	public List<BatchListResponse> batchListWithTariff(ItemsRequest itemRequest) {
		List<BatchListResponse> response = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			response = ItemsDao.batchList(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchList(itemRequest);
		}

		VisitType visitType = null;
		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (itemRequest.getVisitTypeId() != null)
			visitType = GlobalDao.getVisitType(itemRequest.getVisitTypeId());
		if (visitType != null) {
			if (visitType.getVisitTypeCode().equals("OP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
			else if (visitType.getVisitTypeCode().equals("IP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getAdmissionId());
		}

		for (BatchListResponse batch : response) {
			if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

				List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());

				if (uomList != null && !uomList.isEmpty()) {
					batch.setUomResponses(uomList);
					UomResponse sUom = Util.getStoreUom(uomList);
					UomResponse dUom = Util.getOpDispenseUom(uomList);
					if (sUom != null & dUom != null) {
						Integer conversion = sUom.getConversion() * dUom.getConversion();
						batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(), sUom.getUomUnit());
						batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
					}
				}

				if (patientSponsorDetails != null && batch.getProdCatId() != null) {

					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(batch.getProdCatId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}
			}
			if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
						.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
				batch.setAccessories(batchAccessoriesList);
			}

		}

		return response;

	}

	@Transactional
	public List<ItemsDetailsListResponse> itemAndBatchList(ItemsRequest itemsRequest) {
		return ItemsDao.itemAndBatchList(itemsRequest);
	}

	@Override
	@Transactional
	public List<BatchListResponse> batchListWithAvailableQty(ItemsRequest itemRequest) {
		List<BatchListResponse> response = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			response = ItemsDao.batchListWithAvailableQtyCheck(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchListWithAvailableQtyCheck(itemRequest);
		}

		VisitType visitType = null;
		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (itemRequest.getVisitTypeId() != null)
			visitType = GlobalDao.getVisitType(itemRequest.getVisitTypeId());
		if (visitType != null) {
			if (visitType.getVisitTypeCode().equals("OP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
			else if (visitType.getVisitTypeCode().equals("IP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(itemRequest.getAdmissionId());
		}

		List<Long> batchIds = response.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
		List<UomResponse> uomListAll = GlobalDao.getBatchUomListBP(batchIds);

		for (BatchListResponse batch : response) {
			if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

				List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId()))
						.collect(Collectors.toList());

				// GlobalDao.getBatchUomList(batch.getBatchId());

				if (uomList != null && !uomList.isEmpty()) {
					batch.setUomResponses(uomList);
					// UomResponse sUom = Util.getStoreUom(uomList);
					// UomResponse dUom = Util.getOpDispenseUom(uomList);

					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						Integer conversion = Util.getIpUomConversion(uomList);
						batch.setUom(ipUom.getUomTypeId(), ipUom.getUomType(), ipUom.getUomUnitId(),
								ipUom.getUomUnit());
						batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
					}
				}

				if (patientSponsorDetails != null && batch.getProdCatId() != null) {

					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(batch.getProdCatId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}
			}
			if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
						.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
				batch.setAccessories(batchAccessoriesList);
			}

		}

		return response;
	}

	@Override
	@Transactional
	public List<ItemListResponse> itemsListConsumable(ItemsRequest itemRequest) {
		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.consumableItemsList(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetItemsList(itemRequest);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ItemListResponse> consumableItemsListByConsignment(ItemsRequest itemRequest) {
		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.consumableItemsListByConsignment(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetItemsList(itemRequest);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ItemListResponse> consumableItemsListByConsignmentIndent(ItemsRequest itemRequest) {
		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.consumableItemsListByConsignmentIndent(itemRequest);
			for (ItemListResponse itemListResponse : listResponse) {

				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetItemsList(itemRequest);
		}
		return null;
	}

	@Override
	@Transactional
	public List<BatchListResponse> batchListBillableConsumption(ItemsRequest itemRequest) {
		List<BatchListResponse> response = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {
			response = ItemsDao.batchListBillableConsumption(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchList(itemRequest);
		}

		VisitType visitType = null;
		List<PatientSponsorDetails> patientSponsorDetails = null;
		Integer patientCatId = 0;

		if (itemRequest.getVisitTypeId() != null)
			visitType = GlobalDao.getVisitType(itemRequest.getVisitTypeId());
		if (visitType != null) {
			if (visitType.getVisitTypeCode().equals("OP")) {
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
				patientCatId = GlobalDao.getPatientCategoryId(itemRequest.getEncounterId());
			} else if (visitType.getVisitTypeCode().equals("IP")) {
				patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(itemRequest.getAdmissionId());
				patientCatId = GlobalDao.getPatientCategoryIdAdmission(itemRequest.getAdmissionId());
			}
		}

		List<Long> batchIds = response.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
		List<UomResponse> uomListAll = new ArrayList<UomResponse>();
		if (!batchIds.isEmpty()) {
			uomListAll = GlobalDao.getBatchUomListBP(batchIds);
		}
		for (BatchListResponse batch : response) {
			if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

				// List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());
				List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId()))
						.collect(Collectors.toList());

				if (uomList != null && !uomList.isEmpty()) {
					batch.setUomResponses(uomList);
					// UomResponse sUom = Util.getStoreUom(uomList);
					// UomResponse dUom = Util.getOpDispenseUom(uomList);

					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						Integer conversion = Util.getIpUomConversion(uomList);
						batch.setUom(ipUom.getUomTypeId(), ipUom.getUomType(), ipUom.getUomUnitId(),
								ipUom.getUomUnit());
						batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
					}

				}

				if (patientSponsorDetails != null && batch.getProdCatId() != null) {

					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(batch.getProdCatId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}

				PatientConsession patientConsession = null;
				if (visitType.getVisitTypeCode().equals("OP")) {
					patientConsession = IGlobalDao.getPatientConsessionEncounter(batch.getItemId(),
							itemRequest.getEncounterId(), patientCatId, batch.getAssetCategoryId(),
							batch.getProductCategoryId(), null, null);
				} else if (visitType.getVisitTypeCode().equals("IP") || visitType.getVisitTypeCode().equals("DC")
						|| visitType.getVisitTypeCode().equals("ER")) {
					patientConsession = IGlobalDao.getPatientConsessionAdmission(batch.getItemId(),
							itemRequest.getAdmissionId(), patientCatId, batch.getAssetCategoryId(),
							batch.getProductCategoryId());
				}
				if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
					batch.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
					batch.setMarkupStatus(patientConsession.isMarkupStatus());
				} else {
					batch.setPatientClassPercentage(0.0);
					batch.setMarkupStatus(false);
				}

			}
			if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
						.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
				batch.setAccessories(batchAccessoriesList);
			}

		}

		return response;
	}

	@Override
	@Transactional
	public List<BatchListResponse> batchListBillableConsumptionNew(ItemsRequest itemRequest) {
		List<BatchListResponse> response = null;
		if(itemRequest.getScreenName()!=null && itemRequest.getScreenName().equalsIgnoreCase("Stock Adjustment"))
		{
		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {
			response = ItemsDao.batchListStoreWiseForStockAdjustment(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchListNewForStockAdjustment(itemRequest);
		}
		}
		else
		{
			if (itemRequest.getCheckKit() != null && itemRequest.getCheckKit() == true) {
				response = ItemsDao.batchListStoreWiseAndKit(itemRequest);
			} else if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {
				response = ItemsDao.batchListStoreWise(itemRequest);
			} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				response = ItemsDao.FixedTypebatchListNew(itemRequest);
			}
		}

		
		List<PatientSponsorDetails> patientSponsorDetails = null;
		Integer patientCatId = 0;

		if (itemRequest.getVisitTypeId() != null) {
			if (itemRequest.getVisitTypeId().equals(OP)) {
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
				patientCatId = GlobalDao.getPatientCategoryId(itemRequest.getEncounterId());
			} else if (!itemRequest.getVisitTypeId().equals(OP)) {
				patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(itemRequest.getAdmissionId());
				patientCatId = GlobalDao.getPatientCategoryIdAdmission(itemRequest.getAdmissionId());
			}
		}
		
		if(response == null || response.size() == 0){
			return response;
		}

		List<Long> batchIds = response.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
		List<Integer> productIds = response.stream().map(x -> x.getProdCatId()).collect(Collectors.toList());
		List<PharmacyTariff> pharmacyTariffs = new ArrayList<>();
		if(patientSponsorDetails != null && patientSponsorDetails.size()>0){
			pharmacyTariffs = GlobalDao.getTariff(productIds,patientSponsorDetails.get(0).getPaymentEntitlementId());
		}
		
		
		List<UomResponse> uomListAll = new ArrayList<UomResponse>();
		if (!batchIds.isEmpty()) {
			uomListAll = GlobalDao.getBatchUomListBP(batchIds);
		}
		for (BatchListResponse batch : response) {
			
				batch.setRate(batch.getMrp());

				if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

					List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId()))
							.collect(Collectors.toList());

					if (uomList != null && !uomList.isEmpty()) {
						batch.setUomResponses(uomList);

						UomResponse ipUom = Util.getIpUom(uomList);
						if (ipUom != null) {
							Integer conversion = Util.getIpUomConversion(uomList);
							batch.setUom(ipUom.getUomTypeId(), ipUom.getUomType(), ipUom.getUomUnitId(),
									ipUom.getUomUnit());

							batch.setConversion(conversion);

							batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
						}

					}

					if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
						
						if (pharmacyTariffs.size() > 0) {
							
							for (PharmacyTariff pharmacyTariff : pharmacyTariffs) {
								
								if(pharmacyTariff.getProductCategoryId().equals(batch.getProdCatId())){
									batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
									batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
								}
								
							}
						}
					
					}else{
						batch.setMarkUpDown(0);
						batch.setMarkUpDownPer(0d);
					}

					PatientConsession patientConsession = null;
					if (itemRequest.getVisitTypeId() != null) {
						if (itemRequest.getVisitTypeId().equals(OP)) {
							patientConsession = IGlobalDao.getPatientConsessionEncounter(batch.getItemId(),
									itemRequest.getEncounterId(), patientCatId, batch.getAssetCategoryId(),
									batch.getProductCategoryId(), null, null);
						} else if (!itemRequest.getVisitTypeId().equals(OP)) {
							patientConsession = IGlobalDao.getPatientConsessionAdmission(batch.getItemId(),
									(itemRequest.getAdmissionId()!=null ? itemRequest.getAdmissionId() : null), (patientCatId!=null ? patientCatId :null), batch.getAssetCategoryId(),
									batch.getProductCategoryId());
						}

						if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
							batch.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
							batch.setMarkupStatus(patientConsession.isMarkupStatus());
						} else {
							batch.setPatientClassPercentage(0.0);
							batch.setMarkupStatus(false);
						}

					}

				}
				if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

					List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
							.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
					batch.setAccessories(batchAccessoriesList);
				}

		}

			return response;
	}

	@Override
	@Transactional
	public List<BatchListResponse> batchListBillableConsumptionStoreWise(ItemsRequest itemRequest) {
		List<BatchListResponse> response = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			response = ItemsDao.batchListStoreWise(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchList(itemRequest);
		}

		if (response != null && !response.isEmpty()) {

			VisitType visitType = null;
			List<PatientSponsorDetails> patientSponsorDetails = null;
			if (itemRequest.getVisitTypeId() != null)
				visitType = GlobalDao.getVisitType(itemRequest.getVisitTypeId());
			if (visitType != null) {
				if (visitType.getVisitTypeCode().equals("OP"))
					patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
				else if (visitType.getVisitTypeCode().equals("IP"))
					patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(itemRequest.getAdmissionId());
			}

			List<Long> batchIds = response.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
			List<UomResponse> uomListAll = new ArrayList<UomResponse>();
			if (!batchIds.isEmpty()) {
				uomListAll = GlobalDao.getBatchUomListBP(batchIds);
			}
			List<BatchListResponse> newResponse = new ArrayList<BatchListResponse>();
			List<UomResponse> flagList = GlobalDao.getReturnableStatus(batchIds);

			List<UomResponse> discountList = ItemsDao.getBatchwiseDiscount(batchIds);


			for (BatchListResponse batch : response) {
				boolean flag = true;
				for (UomResponse uomResponse : flagList) {
					if (uomResponse.getIntegerBatchId() == batch.getBatchId().intValue() && uomResponse.getCount() > 0) {
						flag = false;
					}
				}

				for (UomResponse uomResponse : discountList) {
					if (uomResponse.getIntegerBatchId() == batch.getBatchId().intValue()) {
						batch.setDiscount(uomResponse.getDiscount());
					}
				}

				if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {
					// List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());
					List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId()))
							.collect(Collectors.toList());

					if (uomList != null && !uomList.isEmpty()) {
						batch.setUomResponses(uomList);
						// UomResponse sUom = Util.getStoreUom(uomList);
						// UomResponse dUom = Util.getOpDispenseUom(uomList);

						UomResponse ipUom = Util.getIpUom(uomList);
						if (ipUom != null) {
							Integer conversion = Util.getIpUomConversion(uomList);
							batch.setUom(ipUom.getUomTypeId(), ipUom.getUomType(), ipUom.getUomUnitId(),
									ipUom.getUomUnit());
							// batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
						}

					}

					if (patientSponsorDetails != null && batch.getProdCatId() != null) {

						for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
							List<Integer> prodCatIds = new ArrayList<Integer>();
							prodCatIds.add(batch.getProdCatId());

							List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
									sponsorDetail.getPaymentEntitlementId());
							if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
								if (pharmacyTariffs.size() > 1) {
									// throw exception?
									// Don't throw - get first object and set data.
								}

								PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
								batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
								batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
								break;
							}
						}

					}
				}
				if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

					List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
							.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
					batch.setAccessories(batchAccessoriesList);
				}
				if (flag == true) {
					newResponse.add(batch);
				}
			}

			return newResponse;
		}
		return response;
	}

	@Override
	@Transactional
	public List<BatchListResponse> batchListBillableConsumptionStoreWiseWithoutGeneric(ItemsRequest itemRequest) {
		List<BatchListResponse> response = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			response = ItemsDao.batchListStoreWiseWithoutGeneric(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchList(itemRequest);
		}

		VisitType visitType = null;
		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (itemRequest.getVisitTypeId() != null)
			visitType = GlobalDao.getVisitType(itemRequest.getVisitTypeId());
		if (visitType != null) {
			if (visitType.getVisitTypeCode().equals("OP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
			else if (visitType.getVisitTypeCode().equals("IP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(itemRequest.getAdmissionId());
		}

		List<Long> batchIds = response.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
		List<UomResponse> uomListAll = new ArrayList<UomResponse>();
		if (!batchIds.isEmpty()) {
			uomListAll = GlobalDao.getBatchUomListBP(batchIds);
		}
		for (BatchListResponse batch : response) {
			if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

				// List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());
				List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId()))
						.collect(Collectors.toList());

				if (uomList != null && !uomList.isEmpty()) {
					batch.setUomResponses(uomList);
					// UomResponse sUom = Util.getStoreUom(uomList);
					// UomResponse dUom = Util.getOpDispenseUom(uomList);

					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						Integer conversion = Util.getIpUomConversion(uomList);
						batch.setUom(ipUom.getUomTypeId(), ipUom.getUomType(), ipUom.getUomUnitId(),
								ipUom.getUomUnit());
						// batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
					}

				}

				if (patientSponsorDetails != null && batch.getProdCatId() != null) {

					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(batch.getProdCatId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}
			}
			if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
						.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
				batch.setAccessories(batchAccessoriesList);
			}

		}

		return response;
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> itemsDetailsForGRNWithPendingQty(ItemsRequest itemsRequest) {

		ItemsDetail itemsDetails = new ItemsDetail();
		long count = ItemsDao.productCategoryCount(itemsRequest.getItemIds());
		List<ItemsDetailsListResponse> response = null;
		if (count == 0) {
			response = ItemsDao.itemsDetails(itemsRequest.getItemIds(), itemsRequest.getStoreId(),
					itemsRequest.getUnitId());
		} else if ((count != 0 && count > 0)) {
			response = ItemsDao.itemsDetailsFixedAsset(itemsRequest.getItemIds(), itemsRequest.getStoreId());
		}

		for (ItemsDetailsListResponse item : response) {
			Item i = GlobalDao.getItemById(item.getItemId());
			item.setIsBatchRequired(i.getIsBatchRequired());
			Long storeStock = GlobalDao.getStoreStockByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(itemsRequest.getStoreId(), item.getItemId());
			Long lastMonthSale = GlobalDao.getLastMonthSale(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Long currentMonthSale = GlobalDao.getCurrentMonthSale(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getOrganizationId());
			Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(),
					item.getItemId(), itemsRequest.getUnitId());
			Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(),
					item.getItemId(), itemsRequest.getUnitId());

			Long maxOrderQuantity = maxStoreQuantity
					- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);

			// item.setStockAvailable(maxOrderQuantity);
			item.setStockInTransit(stockInTransit);
			item.setLastMonthSale(lastMonthSale);
			item.setCurrentMonthSale(currentMonthSale);
			item.setMaxOrderQuantity(maxOrderQuantity);
			item.setMaxStoreQuantity(maxStoreQuantity);
			item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(), itemsRequest.getOrganizationId()));
			item.setTaxList(getTaxList(itemsRequest.getOrganizationId()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<ItemsSearchResponse> searchGeneric(boolean isOtc, String search, Integer storeId, Integer unitId) {
		return ItemsDao.searchGeneric(isOtc, search, storeId, unitId);
	}

	@Override
	@Transactional
	public List<ItemListResponse> getIssuingAndReuestedStoreWiseItemsList(ItemsRequest itemRequest) {
		if (itemRequest != null && itemRequest.getAssetTypeId() != null && itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.getIssuingAndReuestedStoreWiseItemsList(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
				itemListResponse.setIsConsignment(
						itemListResponse.getIsconsignment()); /* added by minesh for consignment (true/false) */
			}
			return listResponse;

		} else if (itemRequest != null && itemRequest.getAssetTypeId() != null && itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetIssuingAndReuestedStoreWiseItemsList(itemRequest);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ItemListResponse> itemsLgetListOfItemsist(ItemsRequest itemRequest) {

		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.geStoreWiseItemsList(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetIssuingAndReuestedStoreWiseItemsList(itemRequest);
		}
		return null;

	}

	@Override
	@Transactional
	public List<BatchListResponse> batchListExpiredMedications(ItemsRequest itemRequest) {

		List<BatchListResponse> response = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			response = ItemsDao.ExpiredMedicationsbatchList(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.ExpiredMedicationsFixedTypebatchList(itemRequest);
		}

		VisitType visitType = null;
		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (itemRequest.getVisitTypeId() != null)
			visitType = GlobalDao.getVisitType(itemRequest.getVisitTypeId());
		if (visitType != null) {
			if (visitType.getVisitTypeCode().equals("OP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
			else if (visitType.getVisitTypeCode().equals("IP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(itemRequest.getAdmissionId());
		}

		List<Long> batchIds = response.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
		List<UomResponse> uomListAll = new ArrayList<UomResponse>();
		if (!batchIds.isEmpty()) {
			uomListAll = GlobalDao.getBatchUomListBP(batchIds);
		}
		for (BatchListResponse batch : response) {
			if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

				// List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());
				List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId()))
						.collect(Collectors.toList());

				if (uomList != null && !uomList.isEmpty()) {
					batch.setUomResponses(uomList);
					// UomResponse sUom = Util.getStoreUom(uomList);
					// UomResponse dUom = Util.getOpDispenseUom(uomList);

					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						Integer conversion = Util.getIpUomConversion(uomList);
						batch.setUom(ipUom.getUomTypeId(), ipUom.getUomType(), ipUom.getUomUnitId(),
								ipUom.getUomUnit());
						batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
					}

				}

				if (patientSponsorDetails != null && batch.getProdCatId() != null) {

					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(batch.getProdCatId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}
			}
			if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
						.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
				batch.setAccessories(batchAccessoriesList);
			}

		}

		return response;

	}

	@Override
	@Transactional
	public List<ItemsResponse> getItemsListByExpired(ItemsRequest itemsRequest) {

		List<ItemsResponse> response = ItemsDao.itemsListByBatchExpired(itemsRequest);

		for (ItemsResponse batch : response) {
			List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());

			if (uomList != null && !uomList.isEmpty()) {
				UomResponse sUom = Util.getStoreUom(uomList);
				UomResponse dUom = Util.getOpDispenseUom(uomList);

				if (sUom != null && dUom != null)

				{
					Integer conversion = sUom.getConversion() * dUom.getConversion();

					batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(), sUom.getUomUnit());
					batch.setUomList(uomList);
				}

				// batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
			}
		}
		return response;

	}

	@Override
	@Transactional
	public List<ItemListResponse> getFlagWiseItemsList(Integer id, ItemsRequest itemRequest) {

		if (itemRequest.getAssetTypeId() == 1) {
			List<ItemListResponse> listResponse = null;
			if (id == 1) {
				listResponse = ItemsDao.AccessoriesWiseItemsList(itemRequest);
			} else {
				listResponse = ItemsDao.SpareWiseItemsList(itemRequest);
			}

			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {

			if (id == 1) {
				// listResponse=ItemsDao.AccessoriesWiseItemsList(itemRequest);
				// return ItemsDao.assetItemsList(itemRequest);
				return ItemsDao.assetAccessoriesWiseItemsList(itemRequest);
			} else {
				return ItemsDao.assetSpareWiseItemsList(itemRequest);
			}

		}
		return null;

	}

	@Override
	@Transactional
	public List<ItemsResponse> getItemsListByDateExpired(ItemsRequest itemsRequest) {

		List<ItemsResponse> response = ItemsDao.getItemsListByDateExpired(itemsRequest);
		List<ItemsResponse> newResponse=new ArrayList<>();
		List<Long> batchIds = response.stream().map(ItemsResponse::getBatchId).collect(Collectors.toList());
		List<UomResponse> AllUomList=new ArrayList<>();
		List<UomResponse> flagList=new ArrayList<>();
		if(batchIds!=null && batchIds.size()>0)
		{
			AllUomList = GlobalDao.getBatchUomListAll(batchIds);
			flagList=GlobalDao.getReturnableStatus(batchIds);
		}
		for (ItemsResponse batch : response) {
			Stream<UomResponse> uomListStream = AllUomList.stream();
			List<UomResponse> uomList=uomListStream
			.filter(str -> str.getBatchId().equals(batch.getBatchId()))
			.collect(Collectors.toList());
			
			boolean flag=true;
			for (UomResponse uomResponse : flagList) {
				if(uomResponse.getIntegerBatchId()==batch.getBatchId().intValue() && uomResponse.getCount()>0)
				{
					flag=false;
				}
			}
			if (uomList != null && !uomList.isEmpty()) {
				UomResponse sUom = Util.getStoreUom(uomList);
//				UomResponse dUom = Util.getOpDispenseUom(uomList);

				Integer conversion = (sUom!=null ? sUom.getConversion() : 1);

				batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(), sUom.getUomUnit());
				batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
				batch.setUomList(uomList);
				batch.setIsThreeUOMApplicable(batch.getIsThreeUOMApplicable());
				batch.setDiscount(batch.getDiscount());
			}
			if(flag==true)
			{
				newResponse.add(batch);
			}
		}
		return newResponse;

	}

	@Override
	@Transactional
	public List<BatchListResponse> batchListBillableConsumptionForLoanOut(ItemsRequest itemRequest) {
		List<BatchListResponse> response = null;

		if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

			response = ItemsDao.batchListForLoanOut(itemRequest);
		} else if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = ItemsDao.FixedTypebatchListForLoanOut(itemRequest);
		}

		VisitType visitType = null;
		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (itemRequest.getVisitTypeId() != null)
			visitType = GlobalDao.getVisitType(itemRequest.getVisitTypeId());
		if (visitType != null) {
			if (visitType.getVisitTypeCode().equals("OP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemRequest.getEncounterId());
			else if (visitType.getVisitTypeCode().equals("IP"))
				patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(itemRequest.getAdmissionId());
		}

		List<Long> batchIds = response.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
		List<UomResponse> uomListAll = new ArrayList<UomResponse>();
		if (!batchIds.isEmpty()) {
			uomListAll = GlobalDao.getBatchUomListBP(batchIds);
		}
		for (BatchListResponse batch : response) {
			if (itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {

				// List<UomResponse> uomList = GlobalDao.getBatchUomList(batch.getBatchId());
				List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId()))
						.collect(Collectors.toList());

				if (uomList != null && !uomList.isEmpty()) {
					batch.setUomResponses(uomList);
					// UomResponse sUom = Util.getStoreUom(uomList);
					// UomResponse dUom = Util.getOpDispenseUom(uomList);

					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						Integer conversion = Util.getIpUomConversion(uomList);
						batch.setUom(ipUom.getUomTypeId(), ipUom.getUomType(), ipUom.getUomUnitId(),
								ipUom.getUomUnit());
						batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
					}

				}

				if (patientSponsorDetails != null && batch.getProdCatId() != null) {

					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(batch.getProdCatId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							batch.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							batch.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}
			}
			if (itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {

				List<BatchAccessoriesMapperDto> batchAccessoriesList = ItemsDao
						.batchAccessoriesList((int) (long) batch.getBatchId(), itemRequest.getUnitId());
				batch.setAccessories(batchAccessoriesList);
			}

		}

		return response;
	}

	@Override
	@Transactional
	public List<ItemsSearchResponse> searchBarcodeScanItem(Integer itemId, Integer batchId) {
		return ItemsDao.searchBarcodeScanItem(itemId, batchId);
	}

	@Override
	@Transactional
	public List<ItemsSearchResponse> searchGenericScanItem(Integer itemId) {
		return ItemsDao.searchGenericScanItem(itemId);
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> itemsDetailsOpeningBalance(ItemsRequest itemsRequest) {
		long count = ItemsDao.productCategoryCount(itemsRequest.getItemIds());
		List<ItemsDetailsListResponse> response = null;
		if (count == 0) {
			response = ItemsDao.itemsDetailsOpeningBalance(itemsRequest.getItemIds(), itemsRequest.getStoreId());
		} else if ((count != 0 && count > 0)) {
			response = ItemsDao.itemsDetailsFixedAssetOpeningBalance(itemsRequest.getItemIds(),
					itemsRequest.getStoreId());
		}

		for (ItemsDetailsListResponse item : response) {
			Item i = GlobalDao.getItemById(item.getItemId());
			item.setIsBatchRequired(i.getIsBatchRequired());

			// System.out.println("itemsRequest.itemId() :-" + itemsRequest.getItemIds()+
			// "\nItem.itemId : " + item.getItemId());

			Long storeStock = ItemsDao.getStockAvailable(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());

			// System.out.println("storeStock:-" + storeStock);

			/*
			 * Long storeStockNew = ItemsDao.getStockAvailable(itemsRequest.getStoreId(),
			 * itemsRequest.getItemId()); //System.out.println("storeStockNew:-" +
			 * storeStockNew);
			 */
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(itemsRequest.getStoreId(), item.getItemId());
			// System.out.println("stockInTransit:-" + stockInTransit);

			Long lastMonthSale = GlobalDao.getLastMonthSale(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());
			Long currentMonthSale = GlobalDao.getCurrentMonthSale(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getUnitId());

			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(), item.getItemId(),
					itemsRequest.getOrganizationId());
			// System.out.println("maxStoreQuantity:-" + maxStoreQuantity);

			Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(),
					item.getItemId(), itemsRequest.getUnitId());
			// System.out.println("approvedPRAndPOQuantity:-" + approvedPRAndPOQuantity);

			Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(),
					item.getItemId(), itemsRequest.getUnitId());
			// System.out.println("approvedPOQuantity:-" + approvedPOQuantity);

			// System.out.println("maxStoreQuantity:-" + maxStoreQuantity + "\nstoreStock :
			// " + storeStock+"\nstockInTransit :
			// "+stockInTransit+"\napprovedPRAndPOQuantity : "+approvedPRAndPOQuantity+"\n
			// approvedPOQuantity : "+approvedPOQuantity);

			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
					approvedPRAndPOQuantity, approvedPOQuantity);
			// System.out.println("maxOrderQuantity:-" + maxOrderQuantity);

			if (itemsRequest.getScreenId() != null
					&& (itemsRequest.getScreenId().equals(Screen.PurchaseRequest.getScreen())
							|| itemsRequest.getScreenId().equals(Screen.LocalPurchaseRequest.getScreen()))) {
				maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
						approvedPRAndPOQuantity, approvedPOQuantity);
				// System.out.println(" Inside If maxOrderQuantity:-" + maxOrderQuantity);
				/*
				 * maxStoreQuantity - (item.getStockAvailable() + stockInTransit +
				 * ItemsDao.getLeaseQunatity(itemsRequest.getStoreId(), item.getItemId()));
				 */
			}
			if (maxOrderQuantity < 0) {
				maxOrderQuantity = 0L;
			}
			// item.setStockAvailable(maxOrderQuantity);
			item.setStockInTransit(stockInTransit);
			item.setLastMonthSale(lastMonthSale);
			item.setCurrentMonthSale(currentMonthSale);
			item.setMaxOrderQuantity(maxOrderQuantity);
			item.setMaxStoreQuantity(maxStoreQuantity);
			item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(), itemsRequest.getOrganizationId()));
			item.setTaxList(getTaxList(itemsRequest.getOrganizationId()));

			List<RackResponse> racks = new ArrayList<RackResponse>();
			for (Rack rack : GlobalDao.getRacksForOpeningBalance(itemsRequest.getStoreId(),
					itemsRequest.getOrganizationId())) {
				List<ShelfResponse> shelfs = new ArrayList<ShelfResponse>();
				for (Shelf shelf : rack.getShelfList()) {
					List<OpeningBalanceItem> itemList = new ArrayList<OpeningBalanceItem>();
					for (ShelfItem shelfItem : shelf.getShelfItem()) {
						OpeningBalanceItem itemObject = new OpeningBalanceItem(shelfItem.getBatch().getItem().getCode(),
								shelfItem.getBatch().getItem().getName(), shelfItem.getBatch().getBatchNo());
						itemList.add(itemObject);
					}
					ShelfResponse shelfObject = new ShelfResponse(shelf.getId(), shelf.getName(), itemList);
					shelfs.add(shelfObject);
				}
				RackResponse rackObject = new RackResponse(rack.getId(), rack.getName(), shelfs);
				racks.add(rackObject);
			}
			item.setRacks(racks);
		}

		return response;
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> itemsDetailsOpeningBalancePT(ItemsRequest itemsRequest) {
		long count = ItemsDao.productCategoryCount(itemsRequest.getItemIds());
		List<ItemsDetailsListResponse> response = null;
		if (count == 0) {
			response = ItemsDao.itemsDetailsOpeningBalance(itemsRequest.getItemIds(), itemsRequest.getStoreId());
		} else if ((count != 0 && count > 0)) {
			response = ItemsDao.itemsDetailsFixedAssetOpeningBalance(itemsRequest.getItemIds(),
					itemsRequest.getStoreId());
		}

		List<Integer> itemIds = response.stream().map(x -> x.getItemId()).collect(Collectors.toList());

		List<RackResponse> racks = getRack(itemsRequest.getStoreId());
		List<TaxDropDown> taxList = getTaxList(itemsRequest.getOrganizationId());

		List<Quantities> storeStocks = ItemsDao.getStockAvailableBP(itemsRequest.getStoreId(), itemIds,itemsRequest.getUnitId());
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(itemsRequest.getStoreId(), itemIds,
				itemsRequest.getUnitId());
		List<Quantities> lastMonthSaleQties = GlobalDao.getLastMonthSaleBP(itemsRequest.getStoreId(), itemIds,
				itemsRequest.getUnitId());
		List<Quantities> currntMonthSaleQties = GlobalDao.getCurrentMonthSaleBP(itemsRequest.getStoreId(), itemIds,
				itemsRequest.getUnitId());
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(itemsRequest.getStoreId(), itemIds,
				itemsRequest.getOrganizationId());
		List<Quantities> approvedPRAndPOQuantities = ItemsDao.getApprovedPRandPOQuantityList(itemsRequest.getStoreId(),
				itemIds, itemsRequest.getUnitId());
		List<Quantities> approvedPOQuantities = ItemsDao.getApprovedPurchaseOrderQuantityList(itemsRequest.getStoreId(),
				itemIds, itemsRequest.getUnitId());

		List<UomResponse> uoms = null;
		if (!itemIds.isEmpty())
			uoms = GlobalDao.getUnitOfMeasurementListByItemBP(itemIds, itemsRequest.getOrganizationId());

		for (ItemsDetailsListResponse item : response) {
			long storeStock = 0;
			long stockInTransit = 0;
			long lastMonthSale = 0;
			long currentMonthSale = 0;
			int maxStoreQuantity = 0;
			long approvedPRAndPOQuantity = 0;
			long approvedPOQuantity = 0;

			Optional<Quantities> storeStockOpt = storeStocks.stream()
					.filter(x -> x.getItemId().equals(item.getItemId())).findFirst();
			if (storeStockOpt.isPresent())
				storeStock = storeStockOpt.get().getQuantity();

			Optional<Quantities> stockInTransitOpt = stockInTransits.stream()
					.filter(x -> x.getItemId().equals(item.getItemId())).findFirst();
			if (stockInTransitOpt.isPresent())
				stockInTransit = stockInTransitOpt.get().getQuantity();

			Optional<Quantities> lastMonthSaleQtys = lastMonthSaleQties.stream()
					.filter(x -> x.getItemId().equals(item.getItemId())).findFirst();
			if (lastMonthSaleQtys.isPresent())
				lastMonthSale = lastMonthSaleQtys.get().getQuantity();

			Optional<Quantities> currntMonthQty = currntMonthSaleQties.stream()
					.filter(x -> x.getItemId().equals(item.getItemId())).findFirst();
			if (currntMonthQty.isPresent())
				currentMonthSale = currntMonthQty.get().getQuantity();

			Optional<Quantities> maxStoreQty = maxStoreQuantitys.stream()
					.filter(x -> x.getItemId().equals(item.getItemId())).findFirst();
			if (maxStoreQty.isPresent())
				maxStoreQuantity = maxStoreQty.get().getIntQuantity();

			Optional<Quantities> approvedPRAndPOQty = approvedPRAndPOQuantities.stream()
					.filter(x -> x.getItemId().equals(item.getItemId())).findFirst();
			if (approvedPRAndPOQty.isPresent())
				approvedPRAndPOQuantity = approvedPRAndPOQty.get().getQuantity();

			Optional<Quantities> approvedPRAndPOQtyOpt = approvedPOQuantities.stream()
					.filter(x -> x.getItemId().equals(item.getItemId())).findFirst();
			if (approvedPRAndPOQtyOpt.isPresent())
				approvedPOQuantity = approvedPRAndPOQtyOpt.get().getQuantity();

			Long maxOrderQuantity = 0L;
			if (itemsRequest.getScreenId() != null
					&& (itemsRequest.getScreenId().equals(Screen.PurchaseRequest.getScreen())
							|| itemsRequest.getScreenId().equals(Screen.LocalPurchaseRequest.getScreen())))
				maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
						approvedPRAndPOQuantity, approvedPOQuantity);

			if (maxOrderQuantity < 0) {
				maxOrderQuantity = 0L;
			}
			item.setStockAvailable(storeStock);
			item.setStockInTransit(stockInTransit);
			item.setLastMonthSale(lastMonthSale);
			item.setCurrentMonthSale(currentMonthSale);
			item.setMaxOrderQuantity(maxOrderQuantity);
			item.setMaxStoreQuantity(maxStoreQuantity);

			if (uoms != null && !uoms.isEmpty())
				item.setUom(
						uoms.stream().filter(x -> x.getItemId().equals(item.getItemId())).collect(Collectors.toList()));

			item.setTaxList(taxList);
			item.setRacks(racks);
		}
		return response;
	}

	private List<RackResponse> getRack(Integer storeId) {
		List<RackResponse> racks = new ArrayList<RackResponse>();
		List<Rack> rackList = ItemsDao.getRackByStoreId(storeId);
		for (Rack rack : rackList) {
			List<ShelfResponse> shelfs = new ArrayList<ShelfResponse>();
			for (Shelf shelf : rack.getShelfList()) {
				List<OpeningBalanceItem> itemList = new ArrayList<OpeningBalanceItem>();
				for (ShelfItem shelfItem : shelf.getShelfItem()) {
					OpeningBalanceItem itemObject = new OpeningBalanceItem(shelfItem.getBatch().getItem().getCode(),
							shelfItem.getBatch().getItem().getName(), shelfItem.getBatch().getBatchNo());
					itemList.add(itemObject);
				}
				ShelfResponse shelfObject = new ShelfResponse(shelf.getId(), shelf.getName(), itemList);
				shelfs.add(shelfObject);
			}
			RackResponse rackObject = new RackResponse(rack.getId(), rack.getName(), shelfs);
			racks.add(rackObject);
		}
		return racks;
	}

	@Override
	@Transactional
	public List<ItemListCompareMinResponse> listWithMappedStoresWithCompareAvalablity(ItemsRequest itemRequest) {
		List<ItemListCompareMinResponse> finallistResponse = new ArrayList<ItemListCompareMinResponse>();
		if (itemRequest.getAssetTypeId() == 1) {
			List<ItemListResponse> listResponse = ItemsDao.getIssuingAndReuestedStoreWiseItemsList(itemRequest);
			// System.out.println(listResponse.size());
			List<Integer> ItemIds = new ArrayList<>();
			for (ItemListResponse itemListResponse : listResponse) {
				ItemIds.add(itemListResponse.getItemid());
			}
			if (ItemIds.size() > 0) {
				List<ItemListCompareMinResponse> compareMinResponse = ItemsDao
						.getItemAvalablityForStore(itemRequest.getStoreId(), ItemIds);
				if (compareMinResponse != null && compareMinResponse.size() > 0) {
					for (ItemListCompareMinResponse responseObj : compareMinResponse) {
						for (ItemListResponse ItemListResponseobj : listResponse) {
							if (responseObj.getItemId().equals(ItemListResponseobj.getItemid())) {
								responseObj.setAssetCategory(ItemListResponseobj.getAssetcategory());
								responseObj.setItemCode(ItemListResponseobj.getItemcode());
								responseObj.setItemName(ItemListResponseobj.getItemname());
								responseObj.setProductCategory(ItemListResponseobj.getProductcategory());
								responseObj.setIsConsignment(ItemListResponseobj.getIsconsignment());
							}
						}
					}
					return compareMinResponse;
				}
			}
			return finallistResponse;
		} else if (itemRequest.getAssetTypeId() == 2) {
			List<ItemListResponse> ItemListResponse = ItemsDao.listWithMappedStoresWithCompareAvalablity(itemRequest);// assetIssuingAndReuestedStoreWiseItemsList
			List<Integer> ItemIds = new ArrayList<>();
			for (ItemListResponse itemListResponse : ItemListResponse) {
				ItemIds.add(itemListResponse.getItemId());
			}
			if (ItemIds.size() > 0) {

				List<ItemListCompareMinResponse> compareMinResponse = ItemsDao
						.getItemAvalablityForStore(itemRequest.getStoreId(), ItemIds);
				if (compareMinResponse != null && compareMinResponse.size() > 0) {
					for (ItemListCompareMinResponse responseObj : compareMinResponse) {
						for (ItemListResponse ItemListResponseobj : ItemListResponse) {
							if (responseObj.getItemId().equals(ItemListResponseobj.getItemId())) {
								responseObj.setAssetCategory(ItemListResponseobj.getAssetCategory());
								responseObj.setItemCode(ItemListResponseobj.getItemCode());
								responseObj.setItemName(ItemListResponseobj.getItemName());
								responseObj.setProductCategory(ItemListResponseobj.getProductCategory());
								responseObj.setIsConsignment(ItemListResponseobj.getIsconsignment());
							}
						}
					}
					return compareMinResponse;
				}

			}
			return finallistResponse;
		}
		return null;
	}

	@Override
	@Transactional
	public List<ItemListResponse> getIssuingAndReuestedStoreWiseItemsListAccessories(ItemsRequest itemRequest) {

		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.getIssuingAndReuestedStoreWiseItemsList(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
				itemListResponse.setIsConsignment(
						itemListResponse.getIsconsignment()); /* added by minesh for consignment (true/false) */
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetIssuingAndReuestedStoreWiseItemsListAccessories(itemRequest);
		}
		return null;

	}
	
//	Rahul G code------
	
	@Override
	@Transactional
	public List<ItemListResponse> itemsList1(ItemsRequest itemRequest) {
		if (itemRequest.getAssetTypeId() == 1) {

			List<ItemListResponse> listResponse = ItemsDao.itemsList1(itemRequest);
			// System.out.println(listResponse.size());
			for (ItemListResponse itemListResponse : listResponse) {
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) {
			return ItemsDao.assetItemsList(itemRequest);
		}
		return null;
	}

@Override
@Transactional
public List<ItemListResponse> itemsSearchForPendingIssueItemSearch(Integer unitId, boolean isOtc, boolean isNarcotic,
		String search, Integer storeId) {
	return ItemsDao.itemsSearchForPendingIssueItemSearch(unitId, isOtc, isNarcotic, search, storeId);
}

@Override
@Transactional
public List<ItemsDetailsResponse> getKitItemsOnWalkInSales(ItemsRequest itemsRequest) throws CustomException {

		try {
			
			List<ItemsDetailsResponse> list = new ArrayList<ItemsDetailsResponse>();
			
			BatchProperty batchProperty = new BatchProperty();
			batchProperty.setIsIndent(false);
			Integer patientCatId = null;
			if(itemsRequest.getAdmissionId() != null && itemsRequest.getAdmissionId()>0){
				 patientCatId = GlobalDao.getPatientCategoryIdAdmission(itemsRequest.getAdmissionId());
			}else if(itemsRequest.getEncounterId() != null && itemsRequest.getEncounterId()>0){
				 patientCatId = GlobalDao.getPatientCategoryId(itemsRequest.getEncounterId());
			}
			
			List<Batch> batchListForKit = GlobalDao.getKitItemsAndBatchOnWalkInSales(itemsRequest.getItemIds(), itemsRequest.getUnitId(), batchProperty, itemsRequest.getStoreId(), itemsRequest.getFromWalkInPrescription());
			
			if(batchListForKit ==  null || batchListForKit.size() == 0){
				return list;
			}
			
			List<UomResponse> uomList = GlobalDao.getKitItemsUomList(itemsRequest.getItemIds(), itemsRequest.getUnitId());
			
			for (UomResponse uom : uomList) {
				uom.setBatchId(uom.getIntbatchId().longValue());
			}
			
			List<Integer> productIds = new ArrayList<Integer>();
			
			for (Batch batch : batchListForKit) {
				Integer prodCatId = null;
				if (batch.getItem().getProductCategory() != null) {
					prodCatId =batch.getItem().getProductCategory().getId();
				} else {
					prodCatId = batch.getItem().getGeneric().getProductCategory().getId();
				}
				if(prodCatId != null){
					productIds.add(prodCatId);
				}
				
			}
			
			List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(productIds,itemsRequest.getPaymentEntitlementId());
			
			for (Integer id : itemsRequest.getItemIds()) {
				
				ItemsDetailsResponse response = new ItemsDetailsResponse();
				List<Batch> batchList =  new ArrayList<Batch>();
				
				for (Batch batchDto : batchListForKit) {
					
					if(id.equals(batchDto.getItem().getId())){
						
						if (batchDto.getBatchNo() == null) {
							batchDto.setBatchNo(ICommonConstants.NOT_APPLICABLE);
							if (batchDto.getStoreStockList() != null && batchDto.getStoreStockList().size() > 0) {
								batchDto.setCurrentQuantity(batchDto.getStoreStockList().get(0).getAvailableQty() != null ? batchDto.getStoreStockList().get(0).getAvailableQty() : 0);
							}
						}
						batchList.add(batchDto);
					}
					
				}
			
				if(batchList !=  null && batchList.size() >0){

					response = GlobalDao.itemsDetailTariffForKit(id,itemsRequest.getStoreId());
					List<Integer> prodCatIds = new ArrayList<Integer>();
					prodCatIds.add(response.getProductCategoryId());

					Integer assetCatId = 0;
					Integer prodCatId = 0;
					if (batchList.get(0).getItem().getProductCategory() != null) {
						assetCatId = batchList.get(0).getItem().getProductCategory().getAssetCategory().getId();
						prodCatId = batchList.get(0).getItem().getProductCategory().getId();
					} else {
						assetCatId = batchList.get(0).getItem().getGeneric().getProductCategory().getAssetCategory()
								.getId();
						prodCatId = batchList.get(0).getItem().getGeneric().getProductCategory().getId();
					}
					response.setIsLasa(batchList.get(0).getItem().getIsLasa() != null ? batchList.get(0).getItem().getIsLasa() : false);

					response.setAssetCategoryId(assetCatId);
					
					if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
						
						if (pharmacyTariffs.size() > 0) {
							
							for (PharmacyTariff pharmacyTariff : pharmacyTariffs) {
								
								if(pharmacyTariff.getProductCategoryId().equals(prodCatIds.get(0))){
									response.setMarkUpDown(pharmacyTariff.getMarkUpDown());
									response.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
								}
								
							}
						}
					
					}

					Integer currentStock = 0;

					for (Batch batch : batchList) {
						BatchResponse batchResponse = new BatchResponse();
						batchResponse = Util.PopulateBatchForKit(batch);
						batchResponse.setAvailableStockQuantity(batch.getCurrentQuantity());
						
						List<UomResponse> uomL = new ArrayList<>();
						
						for (UomResponse uom : uomList) {
							if(uom.getBatchId().equals(batchResponse.getBatchId())){
								uomL.add(uom);
							}
						}
						batchResponse.setTaxId(batch.getTax().getId());
						batchResponse.setTaxPercentage(batch.getTaxPercentage());
						batchResponse.setUom(uomL);
						response.addBatch(batchResponse);
						if (batch.getBatchNo() != null) {
							currentStock = currentStock + batch.getCurrentQuantity();
						} else {
							currentStock = batch.getCurrentQuantity();
						}
					}
					response.setCurrentStock(currentStock);
					
					Boolean checkIn =false;
					if(itemsRequest.getAdmissionId() != null && itemsRequest.getAdmissionId()>0){
						PatientConsession patientConsession = IGlobalDao.getPatientConsessionAdmission(id, itemsRequest.getAdmissionId(),patientCatId, assetCatId, prodCatId);

						if (patientConsession.isMarkupStatus() == false) {
							response.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
							response.setMarkupStatus(patientConsession.isMarkupStatus());
							response.setPriorityId(patientConsession.getPriorityId());
						} else {
							checkIn =true;
							response.setPatientClassPercentage(0.0D);
							response.setMarkupStatus(false);
							response.setPriorityId(null);
						}
					}else if(itemsRequest.getEncounterId() != null && itemsRequest.getEncounterId()>0){
						PatientConsession patientConsession = IGlobalDao.getPatientConsessionEncounter(id, itemsRequest.getEncounterId(),
								patientCatId, assetCatId, prodCatId, null, itemsRequest.getPaymentEntitlementId());
						if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
							response.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
							response.setMarkupStatus(patientConsession.isMarkupStatus());
							response.setPriorityId(patientConsession.getPriorityId());
						} else {
							checkIn =true;
							response.setPatientClassPercentage(0.0D);
							response.setMarkupStatus(false);
							response.setPriorityId(null);
						}
					}
					
					if(	checkIn == true){
						response.setPatientClassPercentage(0.0D);
						response.setMarkupStatus(false);
						response.setPriorityId(null);
					}
					
					
					list.add(response);
				}
				
			}
			for (ItemsDetailsResponse list1: list) {
				list1.setColorCode(GlobalDao.getColorCode(list1.getGenericId(),itemsRequest.getOrganizationId(),itemsRequest.getUnitId()));
			}
			return list;
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

}

@Override
@Transactional
public List<ItemsDetailsListResponse> getGenericColorMapping(int unitId, int orgId) {
	return GlobalDao.getGenericColorMapping(unitId,orgId);
}

@Override
@Transactional
public List<ItemsSearchResponse> itemsSearchStockAdjustment(Integer unitId, boolean isOtc, String search,
		Integer storeId, Integer assetTypeId) {
	return ItemsDao.itemsSearchStockAdjustment(unitId, isOtc, search, storeId,assetTypeId);
}

@Override
@Transactional
public ItemsDetailsResponse itemsDetailWalkInPrescription(Integer id, Integer entitlementId, Integer unitId, Integer orgId,
		Integer storeId) {
	BatchProperty batchProperty = new BatchProperty();
	batchProperty.setIsIndent(false);
	// List<Batch> batchList = GlobalDao.getBatchDetails(id, batchProperty);
	List<Batch> batchList = GlobalService.getBatchDetailsGlobalWalkInPrescription(id, unitId, batchProperty, storeId);
	ItemsDetailsResponse response = new ItemsDetailsResponse();

	try {
//		if (batchList.isEmpty()) {
//			throw new CustomException("Selected item is out of stock");
			// response.setMessage("Selected item is out of stock");
			// return response;
//		} else {
			response = GlobalDao.itemsDetailTariff(id);
			List<Integer> prodCatIds = new ArrayList<Integer>();
			prodCatIds.add(response.getProductCategoryId());
			response.setColorCode(GlobalDao.getColorCode(response.getGenericId(),orgId,unitId));
			Integer assetCatId = 0;
			if (!batchList.isEmpty() && batchList.get(0).getItem().getProductCategory() != null) {
				assetCatId = batchList.get(0).getItem().getProductCategory().getAssetCategory().getId();
			} else if ( !batchList.isEmpty() && batchList.get(0).getItem().getGeneric().getProductCategory().getAssetCategory() != null){
				assetCatId = batchList.get(0).getItem().getGeneric().getProductCategory().getAssetCategory()
						.getId();
			}

			response.setAssetCategoryId(assetCatId);
			List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, entitlementId);
			if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
				if (pharmacyTariffs.size() > 1) {
					// throw exception?
					// Don't throw - get first object and set data.
				}

				PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
				response.setMarkUpDown(pharmacyTariff.getMarkUpDown());
				response.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
			}

			Integer currentStock = 0;

			for (Batch batch : batchList) {
				BatchResponse batchResponse = new BatchResponse();
				batchResponse = Util.PopulateBatch(batch);
				batchResponse.setAvailableStockQuantity(batch.getCurrentQuantity());
				response.addBatch(batchResponse);
				if (batch.getBatchNo() != null) {
					currentStock = currentStock + batch.getCurrentQuantity();
				} else {
					currentStock = batch.getCurrentQuantity();
				}
			}
			response.setCurrentStock(currentStock);
			StoreItemMapper resData = GlobalDao.getItemMinMaxData(id, unitId, storeId);
			if (resData != null) {
				response.setMinQty(resData.getMinQty());
				response.setMaxQty(resData.getMaxQty());
				response.setSellable(resData.getIsSellable());

			}
			return response;
//		}
	}  catch (Exception e) {
	throw new ApplicationException(e.getMessage(), e);
	}
}

	@Override
	@Transactional
	public List<ItemsDetailsResponse> getUOMForCoversheet(ItemsRequest itemsRequest) throws CustomException {
		List<ItemsDetailsResponse> resList = new ArrayList<>();
		try{
			List<Integer> list = itemsRequest.getItemIds();
			for(int item: list){
				ItemsDetailsResponse res = itemsDetailWalkInPrescription(item,0, itemsRequest.getUnitId(), itemsRequest.getOrganizationId(), itemsRequest.getStoreId());
				resList.add(res);
			}
			return resList;
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
		}
	}

    @Override
    @Transactional
    public Response updateBatchQuantityForBillable(List<BatchListResponse> batchList) {
        try{
            Response res = GlobalDao.updateBatchQuantityForBillable(batchList);
            return res;

        }catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

}
