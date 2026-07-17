package com.param.procurement.purchase.service.goodReceiptNoteApproval;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.OtherCharge;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.GRNStagedQuantity;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.GrnAccessory;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.entity.model.procurement.GrnBonusUomMapper;
import com.param.entity.model.procurement.GrnOtherChargesMapper;
import com.param.entity.model.procurement.GrnUomMapper;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PhysicalInspectionDetail;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.org.dao.ITaxComponentDetailsDao;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.procurement.purchase.dto.goodReceiptNote.CreditNoteAmountList;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDetailResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDetailsRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDropdownDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNOtherChargesMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSearchResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSearchResponse1;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSettledDetailsResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSettledSearchResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStagedDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStatusDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNUomMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.GrnAccessoriesMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.IGrnBonusBatchExpiryDao;
import com.param.procurement.purchase.dto.goodReceiptNote.PurchasedOrderResponse;
import com.param.procurement.quotation.dao.physicalInspection.IPhysicalInspectionDao;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSave;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSaveItem;
import com.param.procurement.quotation.service.physicalInspection.IPhysicalInspectionService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class GoodReceiptNoteApprovalService implements IGoodReceiptNoteApprovalService {

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.GoodReceiptNoteApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IItemsDao itemsDao;

	@Autowired
	IGoodReceiptNoteDao grnDao;

	@Autowired
	IPhysicalInspectionService PhysicalInspectionService;
	@Autowired
	IPhysicalInspectionDao PhysicalInspectionDao;
	
	@Autowired
	ITaxComponentDetailsDao iTaxComponentDetailsDao;
	
	@Autowired
	IGrnBonusBatchExpiryDao BonusBatchExpiryDao;

	@Override
	@Transactional
	public GRNDropdownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		GRNDropdownDto grnDropDownDto = new GRNDropdownDto();

		grnDropDownDto.setRequestingStores(GlobalDao.getUserStoreListBP(userId, unitId));
		grnDropDownDto.setDeliveryStores(GlobalDao.getStoreListBP(unitId));
		grnDropDownDto.setStatus(GlobalDao.getScreenStatusBP(screenId));
		grnDropDownDto.setAssetType(GlobalDao.getAssetTypeListBP());

		/*
		 * Original Code for (Store store : GlobalDao.getUserStoreList()) {
		 * grnDropDownDto.addStore(new
		 * com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		 * }
		 * 
		 * for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
		 * grnDropDownDto.addStatus(new
		 * com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
		 * screenStatus.getStatus().getStatus())); }
		 * 
		 * for (AssetType assetType : GlobalDao.getAssetTypeList()) { grnDropDownDto
		 * .addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(),
		 * assetType.getType())); }
		 */
		return grnDropDownDto;
	}

	@Override
	@Transactional
	public Object getGRNList(GRNRequest grnRequest) {
		grnRequest.setToDate((new DateTime(grnRequest.getToDate())).plusDays(1).toDate());

		List<GRNSearchResponse> grnSearchListBP = grnDao.searchGRNListBP(grnRequest, screenId);

		return grnSearchListBP;

		/*
		 * List<GRNSearchResponse> grnSearchList = new ArrayList<GRNSearchResponse>();
		 * 
		 * for (GoodReceiptNote goodReceiptNote : grnDao.searchGRNList(grnRequest,
		 * screenId)) {
		 * 
		 * GRNSearchResponse grnSearch = new GRNSearchResponse( goodReceiptNote.getId(),
		 * goodReceiptNote.getStore().getStoreId(),
		 * goodReceiptNote.getStore().getStoreName(), goodReceiptNote.getGrnNumber(),
		 * goodReceiptNote.getAddedDate(), goodReceiptNote.getInvoiceNumber(),
		 * goodReceiptNote.getInvoiceDate(), goodReceiptNote.getDcNumber(),
		 * goodReceiptNote.getDcDate(), goodReceiptNote.getVendor().getName(),
		 * goodReceiptNote.getAddedBy().getUserName(),
		 * goodReceiptNote.getStatus().getStatus(),
		 * goodReceiptNote.getApprovalStatus().getStatus(),
		 * goodReceiptNote.getApprovedBy()!=null
		 * ?goodReceiptNote.getApprovedBy().getUserName() : null,
		 * goodReceiptNote.getApprovedDate());
		 * 
		 * grnSearchList.add(grnSearch);
		 * 
		 * } return grnSearchList;
		 */
	}

	@Override
	@Transactional
	public Object getGoodReceiptNote(Integer id, Integer unitId, Integer orgId) {

		GRNResponse grnResponseBP = grnDao.getGoodReceiptNoteSettledBP(id);
		Boolean isSkipPhysicalInspection=grnDao.getIsSkipPhysicalInspection(unitId,orgId);
		grnResponseBP.setIsSkipPhysicalInspection(isSkipPhysicalInspection);
		List<GRNDetailResponse> grnDetails = grnDao.getGoodReceiptNoteDetailsBP(id);
		boolean isPRNRequired=GlobalDao.isPRNRequired(unitId);
		String pharmacyPRNFormat=GlobalDao.pharmacyPRNFormat(unitId);
		
		grnResponseBP.setIsPRNRequired(isPRNRequired);
		grnResponseBP.setPharmacyPRNFormat(pharmacyPRNFormat);
		List<Integer> consumableDetailIds = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId())).map(GRNDetailResponse::getId)
				.collect(Collectors.toList());
		List<Integer> fixedDetaildId = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId())).map(GRNDetailResponse::getId)
				.collect(Collectors.toList());
		List<Integer> consumabelItemIds = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId())).map(GRNDetailResponse::getItemId)
				.collect(Collectors.toList());

		// Required fields for consumable id's
		List<GRNUomMapper> grnUomBP = null;
		List<GRNUomMapper> bonusUomBP = null;
		List<UomResponse> uomResponses = null;

		if (consumableDetailIds.size() > 0) {
			grnUomBP = grnDao.getGRNUomMapperBP(consumableDetailIds);
			bonusUomBP = grnDao.getGRNBonusUomMapperBP(consumableDetailIds);
			uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds, orgId);

		}

		// Required fields for fixed asset id's
		List<GrnAccessoriesMapper> grnMapperListBP = new ArrayList<GrnAccessoriesMapper>();
		if (fixedDetaildId != null && !fixedDetaildId.isEmpty())
			grnMapperListBP = grnDao.getGrnAccessoryBP(fixedDetaildId);

		// These are the common fields
		List<Integer> allDetailIds = new ArrayList<Integer>();
		allDetailIds.addAll(consumableDetailIds);
		allDetailIds.addAll(fixedDetaildId);
		List<PurchasedOrderResponse> poListBP = grnDao.getPurchaseOrderIdBP(allDetailIds);
		// List<GRNOtherChargesMapper> otherChargesBP =
		// grnDao.getGRNOtherChargesMapperBP(allDetailIds);
		List<GRNStagedDto> grnStagedQunatityBP = grnDao.getGRNStagedQuantityForApprovalBP(allDetailIds);
		List<GRNOtherChargesMapper> otherChargesBP = grnDao.getGRNOtherChargesMapperBP(id);
		// These are the common fields too. With quantities
		consumabelItemIds.addAll(grnDetails.stream().filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(GRNDetailResponse::getItemId).collect(Collectors.toList()));
		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemBP(grnResponseBP.getStoreId(), consumabelItemIds,
				unitId);
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(grnResponseBP.getStoreId(),
				consumabelItemIds, unitId);
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(grnResponseBP.getStoreId(),
				consumabelItemIds, orgId);
		List<Quantities> leaseQuantities = itemsDao.getGrnLeaseQunatityBP(grnResponseBP.getStoreId(),
				consumabelItemIds);
		List<Quantities> approvedPRAndPOQuantities = itemsDao.getApprovedPRandPOQuantityList(grnResponseBP.getStoreId(),
				consumabelItemIds, unitId);
		List<Quantities> approvedPOQuantities = itemsDao
				.getApprovedPurchaseOrderQuantityList(grnResponseBP.getStoreId(), consumabelItemIds, unitId);
		List<CreditNoteAmountList> creditNoteAmountlists = grnDao.getCreditNoteAmt(id);
		grnResponseBP.setCreditNoteAmountList(creditNoteAmountlists);
		grnResponseBP.setGrnOtherChargesMapper(otherChargesBP);
		for (GRNDetailResponse grnDetailResponse : grnDetails) {
			// Get stock quantities populated
			long storeStock = 0;
			long stockInTransit = 0;
			int maxStoreQuantity = 0;
			long leaseQty = 0;
			Long approvedPRAndPOQuantity = 0l;
			Long approvedPOQuantity = 0l;

			if (grnDetailResponse.getBonusApprovedQuantity() != null
					&& grnDetailResponse.getBonusApprovedQuantity() > 0 && grnDetailResponse.getBonusReceivedQuantity() !=null) {
				if (grnDetailResponse.getBonusReceivedQuantity() <= grnDetailResponse.getBonusApprovedQuantity()) {
					grnDetailResponse.setBonusApprovedQuantity(grnDetailResponse.getBonusApprovedQuantity());
				}

			}
			List<Quantities> storeStock1 = storeStocks.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> stockInTransit1 = stockInTransits.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> maxStoreQuantity1 = maxStoreQuantitys.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> leaseQuantity = leaseQuantities.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			if (storeStock1 != null && !storeStock1.isEmpty())
				storeStock = storeStock1.getFirst().getQuantity();
			if (stockInTransit1 != null && !stockInTransit1.isEmpty())
				stockInTransit = stockInTransit1.getFirst().getQuantity();
			if (maxStoreQuantity1 != null && !maxStoreQuantity1.isEmpty())
				maxStoreQuantity = maxStoreQuantity1.getFirst().getIntQuantity();
			if (leaseQuantity != null && !leaseQuantity.isEmpty())
				leaseQty = leaseQuantity.getFirst().getQuantity();
			// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
			// leaseQty);
			// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
					approvedPRAndPOQuantity, approvedPOQuantity);
//					Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit
//							+ itemsDao.getGrnLeaseQunatity(grnDetailResponse.getStoreId(), grnDetailResponse.getItemId()));

			grnDetailResponse.setPoList(poListBP.stream()
					.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.toList()));
			/*
			 * grnDetailResponse.setOtherCharges(otherChargesBP.stream() .filter(x ->
			 * x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.
			 * toList()));
			 */
			grnDetailResponse.setGrnStagedQuantity(grnStagedQunatityBP.stream()
					.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.toList()));
			grnDetailResponse.setMaxStoreQuantity(maxStoreQuantity);

			if (grnDetailResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
				grnDetailResponse
						.setGrnUom(grnUomBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));
				grnDetailResponse.setBonusUom(
						bonusUomBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));

				List<UomResponse> uomResponse = uomResponses.stream()
						.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
				List<PurchaseOrderUomMapper> prUomResponse = new ArrayList<PurchaseOrderUomMapper>();
				for (UomResponse uom : uomResponse) {
					PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new UomType(uom.getUomTypeId()),
							new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
							uom.getStoreUom());
					prUom.setCreateDefaults(orgId != null ? orgId : sessionService.getOrganizationId(),
							unitId != null ? unitId : sessionService.getUnitId(), sessionService.getUserId());

					prUomResponse.add(prUom);
				}
				Long pendingQuantity = (long) Util.GetLeaseQuantityPurchase(maxOrderQuantity.intValue(), prUomResponse,
						new UomType(grnDetailResponse.getGrnUomTypeId()));
				if (approvedPRAndPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPRAndPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).getFirst().getQuantity();
				}
				if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPOQuantity = approvedPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).getFirst().getQuantity();
				}
				maxOrderQuantity = maxStoreQuantity
						- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);
				grnDetailResponse.setMaxOrderQuantity(maxOrderQuantity);

				if (grnDetailResponse.getPoPendingQuantity() != null)
					grnDetailResponse.setPoPendingQuantity(
							grnDetailResponse.getPoPendingQuantity() - grnDetailResponse.getPoGrnQuantity());
				else
					grnDetailResponse.setPoPendingQuantity(0);

				grnDetailResponse.setUom(uomResponse);
				grnDetailResponse.setBatch(grnDetailResponse.getBatch());
				if(grnResponseBP.getApprovalStatusId()==5)
				{
					grnResponseBP.setRoundOff(grnDetailResponse.getRoundOffAmount()!=null ? grnDetailResponse.getRoundOffAmount() : grnResponseBP.getRoundOff());
					grnResponseBP.setTotalNetAmount(grnDetailResponse.getTotalNetAmount() !=null ? grnDetailResponse.getTotalNetAmount() : grnResponseBP.getNetAmount());
				}
				else {
					grnResponseBP.setTotalNetAmount(grnDetailResponse.getTotalNetAmount() !=null ? grnDetailResponse.getTotalNetAmount() : 0.00);
				}
                List<GrnBonusBatchExpiry> getList= BonusBatchExpiryDao.getGrnBonusBatchExpiryByGrnDetailId(grnDetailResponse.getId());
                List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
                for (GrnBonusBatchExpiry getList1 : getList) {
                    GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
                    getList2.setBatchNo(getList1.getBatchNo());
                    getList2.setBonusQuantity(getList1.getBonusQuantity());
                    getList2.setExpiryDate(getList1.getExpiryDate());
                    getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
                    getList3.add(getList2);
                }
                grnDetailResponse.setGrnBonusBatchExpiry(getList3);
				grnResponseBP.addItems(grnDetailResponse);
			} else if (grnDetailResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				grnDetailResponse.setGrnAccessoriesMapper(
						grnMapperListBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));
				if (approvedPRAndPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPRAndPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).getFirst().getQuantity();
				}
				if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPOQuantity = approvedPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).getFirst().getQuantity();
				}
				maxOrderQuantity = maxStoreQuantity
						- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);
				grnDetailResponse.setMaxOrderQuantity(maxOrderQuantity);
				grnDetailResponse.setBatch(grnDetailResponse.getBatch());
				if(grnResponseBP.getApprovalStatusId()==5)
				{
					grnResponseBP.setRoundOff(grnDetailResponse.getRoundOffAmount() != null ? grnDetailResponse.getRoundOffAmount() : grnResponseBP.getRoundOff());
					grnResponseBP.setTotalNetAmount(grnDetailResponse.getTotalNetAmount() !=null ? grnDetailResponse.getTotalNetAmount() : grnResponseBP.getNetAmount());
				}
				else {
					grnResponseBP.setTotalNetAmount(grnDetailResponse.getTotalNetAmount()!=null ? grnDetailResponse.getTotalNetAmount() : 0.00);
				}
				List<GrnBonusBatchExpiry> getList= BonusBatchExpiryDao.getGrnBonusBatchExpiryByGrnDetailId(grnDetailResponse.getId());
                List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
                for (GrnBonusBatchExpiry getList1 : getList) {
                    GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
                    getList2.setBatchNo(getList1.getBatchNo().toUpperCase());
                    getList2.setBonusQuantity(getList1.getBonusQuantity());
                    getList2.setExpiryDate(getList1.getExpiryDate());
                    getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
                    getList3.add(getList2);
                }
                grnDetailResponse.setGrnBonusBatchExpiry(getList3);
				grnResponseBP.addItems(grnDetailResponse);
			}
		}
		grnResponseBP.setOtherChargesList(GlobalDao.getOtherChargesBP(unitId));
		grnResponseBP.setTaxList(GlobalDao.getTaxListBP(orgId));
		return grnResponseBP;

		// Original Code
		/*
		 * GoodReceiptNote goodReceiptNote = grnDao.getGoodReceiptNote(id);
		 * 
		 * GRNResponse grnResponse = new
		 * GRNResponse(goodReceiptNote.getId(),goodReceiptNote.getAssetType().getId(),
		 * goodReceiptNote.getAssetType().getType(),goodReceiptNote.getRequestType().
		 * getId(),
		 * goodReceiptNote.getRequestType().getType(),goodReceiptNote.getGrnNumber(),
		 * goodReceiptNote.getAddedDate(), goodReceiptNote.getDiscountType()!=null ?
		 * goodReceiptNote.getDiscountType().getId() : null,
		 * goodReceiptNote.getDiscountType()!=null
		 * ?goodReceiptNote.getDiscountType().getType() :
		 * null,goodReceiptNote.getStatus().getId(),
		 * goodReceiptNote.getStatus().getStatus(),goodReceiptNote.getApprovalStatus().
		 * getId(),goodReceiptNote.getApprovalStatus().getStatus(),goodReceiptNote.
		 * getStore().getStoreId(),
		 * goodReceiptNote.getStore().getStoreName(),goodReceiptNote.getDcDate(),
		 * goodReceiptNote.getDcNumber(),goodReceiptNote.getDiscount(),goodReceiptNote.
		 * getInvoiceDate(),
		 * goodReceiptNote.getInvoiceNumber(),goodReceiptNote.getInwardDate(),
		 * goodReceiptNote.getInwardNumber(),goodReceiptNote.getIsConsignment(),
		 * goodReceiptNote.getNetAmount(),goodReceiptNote.getRoundOff(),
		 * goodReceiptNote.getTotalAmount(),goodReceiptNote.getTotalDiscount(),
		 * goodReceiptNote.getTotalOtherAmount(),goodReceiptNote.getTotalTaxAmmount(),
		 * goodReceiptNote.getVendor()!=null ? goodReceiptNote.getVendor().getId() :
		 * null,goodReceiptNote.getVendor()!=null ?
		 * goodReceiptNote.getVendor().getName() : null,
		 * goodReceiptNote.getRemark(),goodReceiptNote.getAddedBy().getUserName());
		 * 
		 * for(GoodReceiptNoteDetail detail :
		 * goodReceiptNote.getGoodReceiptNoteDetailsList()) {
		 * if(detail.getIsActive()!=false && detail.getIsDeleted()!=true) {
		 * List<PurchasedOrderResponse> poList =
		 * grnDao.getPurchaseOrderId(detail.getId()); List<GRNOtherChargesMapper>
		 * otherCharges = getGRNOtherChargesList(detail.getId()); List<GRNStagedDto>
		 * grnStagedQunatity = grnDao.getGRNStagedQuantityForApproval(detail.getId());
		 * 
		 * Long storeStock =
		 * GlobalDao.getStoreStockByItem(detail.getGoodReceiptNote().getStore().
		 * getStoreId(), detail.getItem().getId()); Long stockInTransit =
		 * GlobalDao.getStoreStockInTransitByItem(detail.getGoodReceiptNote().getStore()
		 * .getStoreId(), detail.getItem().getId()); Long lastMonthSale =
		 * GlobalDao.getLastMonthSale(detail.getGoodReceiptNote().getStore().getStoreId(
		 * ), detail.getItem().getId()); Long currentMonthSale =
		 * GlobalDao.getCurrentMonthSale(detail.getGoodReceiptNote().getStore().
		 * getStoreId(), detail.getItem().getId()); Integer maxStoreQuantity =
		 * GlobalDao.getStoreMaxQuantityByItem(
		 * detail.getGoodReceiptNote().getStore().getStoreId(),
		 * detail.getItem().getId()); Long maxOrderQuantity = maxStoreQuantity -
		 * (storeStock + stockInTransit + itemsDao.getLeaseQunatity
		 * (detail.getGoodReceiptNote().getStore().getStoreId(),
		 * detail.getItem().getId())); // Integer maxStoreQuantity =
		 * GlobalDao.getStoreMaxQuantityByItem(detail.getGoodReceiptNote().getStore().
		 * getStoreId(), detail.getItem().getId()); //Long maxOrderQuantity =
		 * maxStoreQuantity - (storeStock + stockInTransit +
		 * detail.getGrnReceivedQuantity() + detail.getBonusReceivedQuantity()); // Long
		 * maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
		 * (detail.getGrnApprovedQuantity()!=null ? detail.getGrnApprovedQuantity() :
		 * detail.getGrnReceivedQuantity()));
		 * if(goodReceiptNote.getAssetType().getId().equals(Constants.getConsumableId())
		 * ) { List<GRNUomMapper> grnUom = getGRNUomList(detail.getId());
		 * List<GRNUomMapper> bonusUom = getBonusUomList(detail.getId());
		 * 
		 * // Integer leaseQuantity =
		 * Util.GetLeaseQuantity(detail.getGrnApprovedQuantity(),
		 * detail.getGrnUomMappersList(), detail.getGrnUomType()) + //
		 * Util.GetLeaseQuantity(detail.getBonusApprovedQuantity(),
		 * detail.getGrnBonusUomMappersList(), detail.getBonusUomType()); Integer
		 * leaseQuantity = null;
		 * 
		 * List<UomResponse> uomResponse =
		 * GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId());
		 * List<PurchaseOrderUomMapper> prUomResponse = new
		 * ArrayList<PurchaseOrderUomMapper>(); for(UomResponse uom : uomResponse) {
		 * PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new
		 * UomType(uom.getUomTypeId()), new UomUnit(uom.getUomUnitId()),
		 * uom.getConversion(), uom.getIpUom(), uom.getOpUom(), uom.getStoreUom());
		 * sessionService.setEntityDefaults(prUom); prUomResponse.add(prUom);
		 * 
		 * } // Integer maxQuantity = Util.GetLeaseQuantity(maxStoreQuantity,
		 * prUomResponse, detail.getGrnUomType());
		 * ////System.out.println("maxQuantity:-"+maxQuantity); Long pendingQuantity =
		 * (long) Util.GetLeaseQuantityPurchase(maxOrderQuantity.intValue(),
		 * prUomResponse,detail.getGrnUomType());
		 * //System.out.println("pendingQuantity:-"+pendingQuantity);
		 * 
		 * grnResponse.addItems(new GRNDetailResponse(detail.getId(),
		 * detail.getItem().getId(),detail.getGrnApprovedQuantity(),detail.
		 * getBonusApprovedQuantity(),detail.getPurchaseOrderDetail()!=null ?
		 * detail.getPurchaseOrderDetail().getPoApprovedQuantity() : null,
		 * detail.getPurchaseOrderDetail()!=null ?
		 * detail.getPurchaseOrderDetail().getBonusApprovedQuantity() : null,
		 * leaseQuantity,maxStoreQuantity,pendingQuantity,
		 * detail.getGrnReceivedQuantity(), detail.getBonusReceivedQuantity(),
		 * detail.getBatchNo(), detail.getGrnUomType().getId(),
		 * detail.getGrnUomUnit().getId(), detail.getBonusUomType() != null ?
		 * detail.getBonusUomType().getId() : null, detail.getBonusUomUnit() != null ?
		 * detail.getBonusUomUnit().getId() : null, detail.getCop(), detail.getMrp(),
		 * detail.getDiscount(), detail.getExpiryDate(), detail.getLotNumber(),
		 * detail.getManufacturer()!=null ? detail.getManufacturer().getId() : null,
		 * detail.getTax()!=null ? detail.getTax().getId() : null,
		 * detail.getTaxPercentage(), detail.getTotalAmount(), detail.getTaxAmount(),
		 * detail.getNetAmount(), detail.getTotalDiscount(),
		 * detail.getTotalOtherAmount(), detail.getStatus().getId(),
		 * detail.getSpecification(), detail.getPurchaseOrderDetail()!=null ?
		 * detail.getPurchaseOrderDetail().getSpecification() : null,
		 * detail.getRemark(),detail.getRejectedQuantity(), grnUom, bonusUom,
		 * otherCharges,grnStagedQunatity, detail.getItem().getName(),
		 * detail.getItem().getCode(), detail.getGrnUomType().getType(),
		 * detail.getGrnUomUnit().getUnits(), detail.getBonusUomType() != null ?
		 * detail.getBonusUomType().getType() : null, detail.getBonusUomUnit() != null ?
		 * detail.getBonusUomUnit().getUnits() : null, detail.getManufacturer()!=null ?
		 * detail.getManufacturer().getName() : null, detail.getTax()!=null ?
		 * detail.getTax().getCode() : null, detail.getStatus().getStatus(),
		 * GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId()),poList,
		 * detail.getGrnReceivedQuantity(), null)); }
		 * 
		 * else
		 * if(goodReceiptNote.getAssetType().getId().equals(Constants.getFixedAssetId())
		 * ){
		 * 
		 * List<GrnAccessoriesMapper> grnMapperList =
		 * getGrnAccessoriesMapper(detail.getId()); grnResponse.addItems(new
		 * GRNDetailResponse(detail.getId(),
		 * detail.getItem().getId(),detail.getGrnApprovedQuantity(),
		 * detail.getBonusApprovedQuantity(),detail.getPurchaseOrderDetail()!=null ?
		 * detail.getPurchaseOrderDetail().getPoApprovedQuantity() :
		 * null,detail.getPurchaseOrderDetail()!=null ?
		 * detail.getPurchaseOrderDetail().getBonusApprovedQuantity() : null,
		 * maxStoreQuantity,maxOrderQuantity,detail.getGrnReceivedQuantity(),
		 * detail.getBonusReceivedQuantity(), detail.getBatchNo(), detail.getCop(),
		 * detail.getMrp(), detail.getDiscount(), detail.getExpiryDate(),
		 * detail.getLotNumber(), detail.getManufacturer()!=null ?
		 * detail.getManufacturer().getId() : null, detail.getTax()!=null ?
		 * detail.getTax().getId() : null, detail.getTaxPercentage(),
		 * detail.getTotalAmount(), detail.getTaxAmount(), detail.getNetAmount(),
		 * detail.getTotalDiscount(), detail.getTotalOtherAmount(),
		 * detail.getStatus().getId(), detail.getSpecification(),
		 * detail.getPurchaseOrderDetail()!=null ?
		 * detail.getPurchaseOrderDetail().getSpecification() : null,
		 * detail.getRemark(),detail.getRejectedQuantity(),detail.getWarrentyPeriod(),
		 * detail.getSupportTerm(),detail.getTechnicalSpec(),detail.getExpectedPayment()
		 * ,detail.getVendor()!=null ? detail.getVendor().getId() :
		 * null,detail.getVendor()!=null ?detail.getVendor().getName() :
		 * null,detail.getWarrentyDate(), otherCharges,grnStagedQunatity,
		 * detail.getItem().getName(), detail.getItem().getCode(),
		 * detail.getManufacturer()!=null ? detail.getManufacturer().getName(): null,
		 * detail.getTax()!=null ? detail.getTax().getCode() : null,
		 * detail.getStatus().getStatus(), poList,grnMapperList, null)); } } }
		 * 
		 * grnResponse.setOtherChargesList(getOtherCharges()); return grnResponse;
		 */
	}

	private List<GrnAccessoriesMapper> getGrnAccessoriesMapper(Integer grnDetailId) {

		List<GrnAccessoriesMapper> response = new ArrayList<GrnAccessoriesMapper>();
		for (GrnAccessory mapper : grnDao.getGrnAccessory(grnDetailId)) {
			if (mapper.getIsActive() != false && mapper.getIsDeleted() != true) {
				GrnAccessoriesMapper mapperDto = new GrnAccessoriesMapper(mapper.getId(), mapper.getItem().getId(),
						mapper.getItem().getName(), mapper.getItem().getCode(), mapper.getSerialNo(),
						mapper.getWarrentyFrom(), mapper.getWarrentyTo(), mapper.getQuantity(),
						mapper.getSpecification());
				response.add(mapperDto);
			}
		}
		return response;
	}

	/*
	 * @Transactional private String getPoSpecification(GoodReceiptNoteDetail
	 * detail) { String poSpecification = null; List<PurchaseOrderDetail>
	 * purchaseOrderDetailList = detail.getPurchaseOrderDetailList();
	 * for(PurchaseOrderDetail poDetail : purchaseOrderDetailList) {
	 * if(detail.getItem().getId().equals(poDetail.getItem().getId())) {
	 * poSpecification = poDetail.getSpecification(); } } return poSpecification;
	 * 
	 * }
	 */

	@Transactional
	private List<OtherChargesResponse> getOtherCharges() {

		List<OtherChargesResponse> otherChargesList = new ArrayList<OtherChargesResponse>();
		for (OtherCharge otherCharges : GlobalDao.getOtherCharges()) {
			if (otherCharges.getIsActive() != false && otherCharges.getIsDeleted() != true) {
				OtherChargesResponse response = new OtherChargesResponse(otherCharges.getId(),
						otherCharges.getDescription());
				otherChargesList.add(response);
			}
		}

		return otherChargesList;
	}

	@Transactional
	private List<GRNUomMapper> getGRNUomList(Integer grnDetailId) {
		List<GRNUomMapper> response = new ArrayList<GRNUomMapper>();
		for (GrnUomMapper grnUomMapper : grnDao.getGRNUomMapper(grnDetailId)) {
			if (grnUomMapper.getIsActive() != false && grnUomMapper.getIsDeleted() != true) {
				response.add(new GRNUomMapper(grnUomMapper.getId(), grnUomMapper.getUomType().getId(),
						grnUomMapper.getUomType().getType(), grnUomMapper.getUomUnit().getId(),
						grnUomMapper.getUomUnit().getUnits(), grnUomMapper.getConversions(), grnUomMapper.getIpUom(),
						grnUomMapper.getOpUom(), grnUomMapper.getStoreUom()));
			}
		}
		return response;
	}

	@Transactional
	private List<GRNUomMapper> getBonusUomList(Integer grnDetailId) {
		List<GRNUomMapper> response = new ArrayList<GRNUomMapper>();
		for (GrnBonusUomMapper bonusUomMapper : grnDao.getGRNBonusUomMapper(grnDetailId)) {
			if (bonusUomMapper.getIsActive() != false && bonusUomMapper.getIsDeleted() != true) {
				response.add(new GRNUomMapper(bonusUomMapper.getId(), bonusUomMapper.getUomType().getId(),
						bonusUomMapper.getUomType().getType(), bonusUomMapper.getUomUnit().getId(),
						bonusUomMapper.getUomUnit().getUnits(), bonusUomMapper.getConversions(),
						bonusUomMapper.getIpUom(), bonusUomMapper.getOpUom(), bonusUomMapper.getStoreUom()));

			}
		}
		return response;
	}

	@Transactional
	private List<GRNOtherChargesMapper> getGRNOtherChargesList(Integer grnDetailId) {
		List<GRNOtherChargesMapper> grnOtherChargesList = new ArrayList<GRNOtherChargesMapper>();

		for (GrnOtherChargesMapper grnOtherChargesMapper : grnDao.getGRNOtherChargesMapper(grnDetailId)) {
			if (grnOtherChargesMapper.getIsActive() != false && grnOtherChargesMapper.getIsDeleted() != true) {
				grnOtherChargesList.add(new GRNOtherChargesMapper(grnOtherChargesMapper.getId(),
						grnOtherChargesMapper.getOtherCharge().getId(),
						grnOtherChargesMapper.getOtherCharge().getDescription(), grnOtherChargesMapper.getAmount()));
			}
		}
		return grnOtherChargesList;
	}

	@Override
	@Transactional
	public CommonResponse approveGoodReceiptNote(List<GRNStatusDto> grnStatusDtoList) {
		boolean isSkipPhysicalInspection = false;
          HashMap<Integer, Long> map=new HashMap<>();
		List<PhysicalInspection> physicalInspectionList = new ArrayList<>();
		isSkipPhysicalInspection = grnDao.getApplicationConfiguration(grnStatusDtoList.getFirst().getUnitId()).getIsSkipPhysicalInspection();
        List<Integer> grnIds = grnStatusDtoList.stream().map(GRNStatusDto::getGrnId).collect(Collectors.toList());
        List<GoodReceiptNote> goodReceiptNoteAll=grnDao.getGoodReceiptNoteAll(grnIds);
		for (GRNStatusDto grnStatusDto : grnStatusDtoList) {
			GoodReceiptNote goodReceiptNote = goodReceiptNoteAll.stream()
            .filter(order -> grnStatusDto.getGrnId().equals(order.getId()))
            .findAny()
            .orElse(null);
			if (grnStatusDto.getStatusId().equals(com.param.common.constants.Status.Pending.getStatus())) // 2 statusId
																											// for
																											// Approved
			{
				goodReceiptNote.setStatus(new Status(grnStatusDto.getStatusId()));
				goodReceiptNote.setApprovalStatus(new Status(grnStatusDto.getApprovalStatusId()));

				PhysicalInspection physicalInspectionObject = new PhysicalInspection(
						new GoodReceiptNote(grnStatusDto.getGrnId()),
						new Status(com.param.common.constants.Status.Pending.getStatus()));

				physicalInspectionObject.setAddedByUser(grnStatusDto.getUserId());
				physicalInspectionObject.setUpdatedByUser(grnStatusDto.getUserId());
				physicalInspectionObject.setAddedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
				physicalInspectionObject.setUpdatedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
				physicalInspectionObject.setOrganizationId(grnStatusDto.getOrganizationId());
				physicalInspectionObject.setUnitId(grnStatusDto.getUnitId());
				// sessionService.setEntityDefaults(physicalInspectionObject);

				for (GoodReceiptNoteDetail detail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
					
					if(detail.getIsActive() == true && detail.getIsDeleted() == false) {
						
						String mainLineItemBatchNo=detail.getBatchNo();
						Date mainLineItemBatchExpiry=detail.getExpiryDate();
						Integer mainLineItemApprovedQuantity=detail.getGrnReceivedQuantity();
						GRNDetailsRequest grnDetailResponse= new GRNDetailsRequest();
		                List<GrnBonusBatchExpiry> getList= BonusBatchExpiryDao.getGrnBonusBatchExpiryByGrnDetailId(detail.getId());
		                List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
		                for (GrnBonusBatchExpiry getList1 : getList) {
		                    GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
		                    getList2.setBatchNo(getList1.getBatchNo());
		                    getList2.setBonusQuantity(getList1.getBonusQuantity());
		                    getList2.setExpiryDate(getList1.getExpiryDate());
		                    getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
		                    getList3.add(getList2);
		                }
		                grnDetailResponse.setGrnBonusBatchExpiry(getList3);
						detail.setStatus(new Status(grnStatusDto.getStatusId()));
						detail.setGrnApprovedQuantity(detail.getGrnReceivedQuantity());
						detail.setBonusApprovedQuantity(
								detail.getBonusReceivedQuantity() != null ? detail.getBonusReceivedQuantity() : 0);
						detail.setRejectedQuantity(0);
						detail.setRejectReason(new RejectReason(grnStatusDto.getRejectReasonId()));
						detail.setRejectNote(grnStatusDto.getRejectNote());

						/*
						 * Batch batch = grnDao.saveBatch(InsertApprovedItems(detail,
						 * goodReceiptNote.getStore().getStoreId(),
						 * goodReceiptNote.getAssetType().getId()));
						 */
						Integer grnBonusApprovedQty=0;
						Integer grnBonusReceivedQty=0;
						if(grnDetailResponse.getGrnBonusBatchExpiry().size()>0)
						{
							for (GrnBonusBatchExpiry bonusBatches : grnDetailResponse.getGrnBonusBatchExpiry()) {
								grnBonusApprovedQty=grnBonusApprovedQty+bonusBatches.getBonusQuantity();
								grnBonusReceivedQty=grnBonusReceivedQty+bonusBatches.getBonusQuantity();
							detail.setGrnApprovedQuantity(0);
							detail.setBonusApprovedQuantity(bonusBatches.getBonusQuantity());
							detail.setBonusReceivedQuantity(bonusBatches.getBonusQuantity());
							detail.setBatchNo(bonusBatches.getBatchNo());
							detail.setExpiryDate(grnDao.getexpiry(bonusBatches.getGrnBonusBatchExpiryId()));
							Batch batchToSave = InsertApprovedItems(detail, goodReceiptNote.getStore().getStoreId(),
									goodReceiptNote.getAssetType().getId(), grnStatusDto,isSkipPhysicalInspection);
							batchToSave.setIsConsignment(goodReceiptNote.getIsConsignment());
							Batch batchBonus = grnDao.saveOrUpdateBatch(batchToSave);
//							boolean batchIdUpdate=grnDao.updateBatchId(batchBonus.getId(),bonusBatches.getGrnBonusBatchExpiryId());
							map.put(bonusBatches.getGrnBonusBatchExpiryId(), batchBonus.getId());
							detail.setBatch(batchBonus);
							GrnBonusBatchExpiry grnBatchExpiry1 = new GrnBonusBatchExpiry();
							grnBatchExpiry1.setBatchId(batchBonus.getId());
	                        grnBatchExpiry1.setBatchNo(bonusBatches.getBatchNo());
	                        grnBatchExpiry1.setExpiryDate(bonusBatches.getExpiryDate());
	                        grnBatchExpiry1.setBonusQuantity(bonusBatches.getBonusQuantity());
	                        grnBatchExpiry1.setGrnDetailId(detail.getId());
	                        grnBatchExpiry1.setOrganizationId(grnStatusDto.getOrganizationId());
	                        grnBatchExpiry1.setUnitId(grnStatusDto.getUnitId());
	                        grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
	                        grnBatchExpiry1.setUpdatedByUser(grnStatusDto.getUserId()!=null ? grnStatusDto.getUserId() : sessionService.getUserId());
	                        grnBatchExpiry1.setAddedByUser(grnStatusDto.getUserId()!=null ? grnStatusDto.getUserId() : sessionService.getUserId());
	                        grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
	                        grnBatchExpiry1.setGrnBonusBatchExpiryId(bonusBatches.getGrnBonusBatchExpiryId());
//	                        BonusBatchExpiryDao.updateBonusBatchExpiryApproval(grnBatchExpiry1);
							}
							
						}
						if(grnDetailResponse.getGrnBonusBatchExpiry().size()>0)
						{
							detail.setBonusApprovedQuantity(0);
							detail.setGrnApprovedQuantity(mainLineItemApprovedQuantity);
							detail.setBatchNo(mainLineItemBatchNo);
							detail.setExpiryDate(mainLineItemBatchExpiry);
						}
						/* added by atmiya 20191114 : isConsignment flag in batch update */
						Batch batchToSave = InsertApprovedItems(detail, goodReceiptNote.getStore().getStoreId(),
								goodReceiptNote.getAssetType().getId(), grnStatusDto,isSkipPhysicalInspection);
						batchToSave.setIsConsignment(goodReceiptNote.getIsConsignment());
						/*
						 * batchToSave.setAddedByUser(grnStatusDto.getUserId());
						 * batchToSave.setUpdatedByUser(grnStatusDto.getUserId());
						 * batchToSave.setAddedDate(TimeZoneComponent.getDateByzone(unitId)); batchToSave.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
						 * batchToSave.setOrganizationId(grnStatusDto.getOrganizationId());
						 * batchToSave.setUnitId(grnStatusDto.getUnitId());
						 */
						// Batch batch = grnDao.saveBatch(batchToSave);
						Batch batch = grnDao.saveOrUpdateBatch(batchToSave);
						detail.setBatch(batch);
						if(grnDetailResponse.getGrnBonusBatchExpiry().size()>0)
						{
						detail.setBonusReceivedQuantity(grnBonusReceivedQty);
						detail.setBonusApprovedQuantity(grnBonusApprovedQty);
						}
						/* end added by atmiya 20191114 */
						/*
						 * if(batchToSave.getIsOldBatch()!=null &&
						 * batchToSave.getIsOldBatch().equals(true)) {
						 * 
						 * } else { detail.setBatch(batch); }
						 */

						PhysicalInspectionDetail physicalInspectionDetail = new PhysicalInspectionDetail(
								new GoodReceiptNoteDetail(detail.getId()),
								new Status(com.param.common.constants.Status.Pending.getStatus()));

						physicalInspectionDetail.setAddedByUser(grnStatusDto.getUserId());
						physicalInspectionDetail.setUpdatedByUser(grnStatusDto.getUserId());
						physicalInspectionDetail.setAddedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
						physicalInspectionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
						physicalInspectionDetail.setOrganizationId(grnStatusDto.getOrganizationId());
						physicalInspectionDetail.setUnitId(grnStatusDto.getUnitId());
						// sessionService.setEntityDefaults(physicalInspectionDetail);
						physicalInspectionObject.addPhysicalInspectionDetail(physicalInspectionDetail);

						for (GRNStagedQuantity stagedQuantity : detail.getGrnStagedQuantityList()) {
							stagedQuantity.setApprovedQuantity(stagedQuantity.getReceivedQuantity());
							stagedQuantity.setUpdatedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
							stagedQuantity.setUpdatedByUser(grnStatusDto.getUserId());
						}

						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
						detail.setUpdatedByUser(grnStatusDto.getUserId());
						
					}
					
					
				}

				goodReceiptNote.setApprovedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
				goodReceiptNote.setApprovedBy(new User(grnStatusDto.getUserId()));
				PhysicalInspection phy = grnDao.savePhysicalInspection(physicalInspectionObject);

				if (isSkipPhysicalInspection) {
					// response.setObject(phyInspection);
					phy.setDeliveryStoreId(goodReceiptNote.getStore().getStoreId());
					phy.setUnitIdforAutoPhysicalInspection(grnStatusDto.getUnitId());
					physicalInspectionList.add(phy);
					// this.saveAutoPhysicalInspection(phy,
					// goodReceiptNote.getStore().getStoreId());

				}
			} // 8 status Id for Reject create a Enum for Status
			else if (grnStatusDto.getStatusId().equals(com.param.common.constants.Status.Closed.getStatus())) {
				goodReceiptNote.setStatus(new Status(grnStatusDto.getStatusId()));
				goodReceiptNote.setApprovalStatus(new Status(grnStatusDto.getApprovalStatusId()));
				for (GoodReceiptNoteDetail detail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
					detail.setStatus(new Status(grnStatusDto.getStatusId()));
					detail.setRejectedQuantity(detail.getGrnReceivedQuantity());
					detail.setRejectReason(new RejectReason(grnStatusDto.getRejectReasonId()));
					detail.setRejectNote(grnStatusDto.getRejectNote());

				}
			}

			goodReceiptNote.setUpdatedDate(TimeZoneComponent.getDateByzone(goodReceiptNote.getUnitId()));
			goodReceiptNote.setUpdatedByUser(grnStatusDto.getUserId());
			grnDao.updateGoodReceiptNote(goodReceiptNote);

		}

		CommonResponse response = new CommonResponse();
		if (isSkipPhysicalInspection) {
			response.setListObject(physicalInspectionList);
		}
		if(map!=null)
		{
			response.setObject(map);
		}

		response.setMessage("Good Receipt Note approved successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse approveGoodReceiptNoteDetail(GRNRequest grnRequest) {
		CommonResponse response = new CommonResponse();
		boolean isSkipPhysicalInspection = false;
		isSkipPhysicalInspection = grnDao.getApplicationConfiguration(grnRequest.getUnitId())
				.getIsSkipPhysicalInspection();
		GoodReceiptNote goodReceiptNote = grnDao.getGoodReceiptNote(grnRequest.getId());
		goodReceiptNote.setTotalOtherAmount(grnRequest.getTotalOtherAmount() != null ? grnRequest.getTotalOtherAmount()
				: goodReceiptNote.getTotalOtherAmount());
		goodReceiptNote.setNetAmount(grnRequest.getNetAmount()!=null ?grnRequest.getNetAmount() : goodReceiptNote.getNetAmount());
		goodReceiptNote.setTotalAmount(grnRequest.getTotalAmount()!=null ?grnRequest.getTotalAmount() : goodReceiptNote.getTotalAmount());
		goodReceiptNote.setRoundOff(grnRequest.getRoundOff()!=null ? grnRequest.getRoundOff() :goodReceiptNote.getRoundOff() );
		goodReceiptNote.setTotalDiscount(grnRequest.getTotalDiscount()!=null ? grnRequest.getTotalDiscount() : goodReceiptNote.getTotalDiscount());
		goodReceiptNote.setTotalTaxAmmount(grnRequest.getTotalTaxAmount()!=null ? grnRequest.getTotalTaxAmount() : goodReceiptNote.getTotalTaxAmmount());
		goodReceiptNote.setBalanceAmount(grnRequest.getTotalNetAmount()!=null ?grnRequest.getTotalNetAmount() : goodReceiptNote.getNetAmount());
		PhysicalInspection physicalInspectionObject = new PhysicalInspection(new GoodReceiptNote(grnRequest.getId()),
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		// sessionService.setEntityDefaults(physicalInspectionObject);
		physicalInspectionObject.setAddedByUser(grnRequest.getUserId());
		physicalInspectionObject.setUpdatedByUser(grnRequest.getUserId());
		physicalInspectionObject.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		physicalInspectionObject.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		physicalInspectionObject.setOrganizationId(grnRequest.getOrganizationId());
		physicalInspectionObject.setUnitId(grnRequest.getUnitId());
		HashMap<Integer, Long> map=new HashMap<>();
		for (GoodReceiptNoteDetail detail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
			String mainLineItemBatchNo=detail.getBatchNo();
			Date mainLineItemBatchExpiry=detail.getExpiryDate();
			for (GRNDetailsRequest item : grnRequest.getItems()) {
				Integer mainLineItemRejectedQty=item.getRejectedQuantity()!=null ? item.getRejectedQuantity():0;
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					
					detail.approveGoodReceiptNoteDetail((new User(grnRequest.getUserId())), TimeZoneComponent.getDateByzone(grnRequest.getUnitId()),
							item.getGrnApprovedQuantity(),
							item.getBonusApprovedQuantity() != null ? item.getBonusApprovedQuantity() : 0,
							item.getRejectedQuantity() != null ? item.getRejectedQuantity() : 0,
									item.getCop(),
									item.getMrp(),
									item.getDiscount(),
//									item.getTaxId(),
									new Tax(item.getTaxId() != null ? item.getTaxId() : null),
									item.getTaxPercentage(),
									item.getTaxAmount(),
									item.getTotalAmount(),
									item.getTotalDiscount(),
									item.getNetAmount(),
							new RejectReason(item.getRejectReasonId()), item.getRejectNote(),
							new Status(grnRequest.getStatusId()));
					detail.setTotalOtherAmount(item.getTotalOtherAmount() != null ? item.getTotalOtherAmount() : null);
					detail.setRoundOffAmount(grnRequest.getRoundOffAmount());
					detail.setTotalNetAmount(grnRequest.getTotalNetAmount());
					/* comment by atmiya 20191114 */
					/*
					 * Batch batch = grnDao.saveBatch(InsertApprovedItems(detail,
					 * goodReceiptNote.getStore().getStoreId(),
					 * goodReceiptNote.getAssetType().getId()));
					 */
					/* end comment by atmiya 20191114 */

					/* added by atmiya 20191114 : isConsignment flag in batch update */
					
					Integer fromGrnOrreturn = 0;   // fromGrnOrreturn is used for saparate the API call in which screen 
					List<TaxComponentDetailsDto> taxDto = iTaxComponentDetailsDao.getTaxComponentDetailsById(detail.getId(),fromGrnOrreturn);
					if(taxDto != null && taxDto.size()>0) {
						Double discount = 0d;
						Double netAmount = detail.getTotalAmount() + (detail.getDiscount()!= null ? detail.getDiscount() :discount) ;   // total Amount of grnDetails Amount ItemWise
						Double approvedNetAmount = item.getGrnApprovedQuantity() *(netAmount/detail.getGrnReceivedQuantity());  // total Amount of approved  grnDetails Amount ItemWise
						
						for (TaxComponentDetailsDto taxComponentDetailsDto : taxDto) {
							
							Double taxComponentAmount = (approvedNetAmount / 100)*taxComponentDetailsDto.getTaxComponentPercentage();   // calculation of  tax Component Amount
							
							Double taxAmount =(approvedNetAmount / 100)*taxComponentDetailsDto.getTaxPercentage();
							
							taxComponentDetailsDto.setTaxComponentAmount(taxComponentAmount);
							taxComponentDetailsDto.setTaxAmount(taxAmount);
							
							iTaxComponentDetailsDao.updateTaxComponentDetails(taxComponentDetailsDto);
							
						}
					}
					Integer grnBonusApprovedQty=0;
					Integer grnBonusReceivedQty=0;
					if(item.getGrnBonusBatchExpiry().size()>0)
					{
						for (GrnBonusBatchExpiry bonusBatches : item.getGrnBonusBatchExpiry()) {
							grnBonusApprovedQty=grnBonusApprovedQty+bonusBatches.getBonusQuantity();
							grnBonusReceivedQty=grnBonusReceivedQty+bonusBatches.getBonusQuantity();
						detail.setGrnApprovedQuantity(0);
						detail.setBonusApprovedQuantity(bonusBatches.getBonusQuantity());
						detail.setBonusReceivedQuantity(bonusBatches.getBonusQuantity());
						detail.setBatchNo(bonusBatches.getBatchNo());
						detail.setExpiryDate(bonusBatches.getExpiryDate());
						detail.setRejectedQuantity(0);
						Batch batchToSaveForBonus = InsertApprovedItems(detail, goodReceiptNote.getStore().getStoreId(),
								goodReceiptNote.getAssetType().getId(), grnRequest,isSkipPhysicalInspection);
						batchToSaveForBonus.setIsConsignment(goodReceiptNote.getIsConsignment());
						batchToSaveForBonus.setAddedByUser(grnRequest.getUserId());
						batchToSaveForBonus.setUpdatedByUser(grnRequest.getUserId());
						batchToSaveForBonus.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						batchToSaveForBonus.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						batchToSaveForBonus.setOrganizationId(grnRequest.getOrganizationId());
						batchToSaveForBonus.setUnitId(grnRequest.getUnitId());
						Batch batchBonus = grnDao.saveOrUpdateBatch(batchToSaveForBonus);
//						boolean batchIdUpdate=grnDao.updateBatchId(batchBonus.getId(),bonusBatches.getGrnBonusBatchExpiryId());
						map.put(bonusBatches.getGrnBonusBatchExpiryId(), batchBonus.getId());
						detail.setBatch(batchBonus);
						
						GrnBonusBatchExpiry grnBatchExpiry1 = new GrnBonusBatchExpiry();
						grnBatchExpiry1.setBatchId(batchBonus.getId());
                        grnBatchExpiry1.setBatchNo(bonusBatches.getBatchNo());
                        grnBatchExpiry1.setExpiryDate(bonusBatches.getExpiryDate());
                        grnBatchExpiry1.setBonusQuantity(bonusBatches.getBonusQuantity());
                        grnBatchExpiry1.setGrnDetailId(detail.getId());
                        grnBatchExpiry1.setOrganizationId(grnRequest.getOrganizationId());
                        grnBatchExpiry1.setUnitId(grnRequest.getUnitId());
                        grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
                        grnBatchExpiry1.setUpdatedByUser(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
                        grnBatchExpiry1.setAddedByUser(grnRequest.getUserId()!=null ? grnRequest.getUserId() : sessionService.getUserId());
                        grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
                        grnBatchExpiry1.setGrnBonusBatchExpiryId(bonusBatches.getGrnBonusBatchExpiryId());
                        BonusBatchExpiryDao.updateBonusBatchExpiryApproval(grnBatchExpiry1);
						}
						
					}
					if(item.getGrnBonusBatchExpiry().size()>0)
					{
						detail.setBonusApprovedQuantity(0);
						detail.setGrnApprovedQuantity(item.getGrnApprovedQuantity());
						detail.setBatchNo(mainLineItemBatchNo);
						detail.setExpiryDate(mainLineItemBatchExpiry);
					}
					detail.setRejectedQuantity(0);
					Batch batchToSave = InsertApprovedItems(detail, goodReceiptNote.getStore().getStoreId(),
							goodReceiptNote.getAssetType().getId(), grnRequest,isSkipPhysicalInspection);
					batchToSave.setIsConsignment(goodReceiptNote.getIsConsignment());
					batchToSave.setAddedByUser(grnRequest.getUserId());
					batchToSave.setUpdatedByUser(grnRequest.getUserId());
					batchToSave.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					batchToSave.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					batchToSave.setOrganizationId(grnRequest.getOrganizationId());
					batchToSave.setUnitId(grnRequest.getUnitId());
					Batch batch = grnDao.saveOrUpdateBatch(batchToSave);
					detail.setBatch(batch);
					detail.setRejectedQuantity(mainLineItemRejectedQty);
					if(item.getGrnBonusBatchExpiry().size()>0)
					{
					detail.setBonusReceivedQuantity(grnBonusReceivedQty);
					detail.setBonusApprovedQuantity(grnBonusApprovedQty);
					}
					/* end added by atmiya 20191114 */
					/*
					 * if(batchToSave.getIsOldBatch()!=null &&
					 * batchToSave.getIsOldBatch().equals(true)) {
					 * 
					 * } else { detail.setBatch(batch); }
					 */
					PhysicalInspectionDetail physicalInspectionDetail = new PhysicalInspectionDetail(
							new GoodReceiptNoteDetail(detail.getId()),
							new Status(com.param.common.constants.Status.Pending.getStatus()));

					physicalInspectionDetail.setAddedByUser(grnRequest.getUserId());
					physicalInspectionDetail.setUpdatedByUser(grnRequest.getUserId());
					physicalInspectionDetail.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					physicalInspectionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					physicalInspectionDetail.setOrganizationId(grnRequest.getOrganizationId());
					physicalInspectionDetail.setUnitId(grnRequest.getUnitId());
					// sessionService.setEntityDefaults(physicalInspectionDetail);
					physicalInspectionObject.addPhysicalInspectionDetail(physicalInspectionDetail);

					for (com.param.entity.model.procurement.GRNStagedQuantity grnStagedQuantity : detail
							.getGrnStagedQuantityList()) {
						for (GRNStagedDto grnQuant : item.getGrnStagedQuantity()) {
							if (grnQuant.getId() != null && grnQuant.getId().equals(grnStagedQuantity.getId())) {
								grnStagedQuantity.setApprovedQuantity(grnQuant.getApprovedQuantity());
							}
						}
					}

				}
			}
		}
		goodReceiptNote.setStatus(new Status(grnRequest.getStatusId()));
		goodReceiptNote.setApprovalStatus(new Status(grnRequest.getApprovalStatusId()));

		goodReceiptNote.setApprovedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		goodReceiptNote.setApprovedBy(new User(grnRequest.getUserId()));

		goodReceiptNote.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		goodReceiptNote.setUpdatedByUser(grnRequest.getUserId());

		grnDao.updateGoodReceiptNote(goodReceiptNote);
		PhysicalInspection phyInspection = grnDao.savePhysicalInspection(physicalInspectionObject);
		grnDao.updateOtherCharges(goodReceiptNote.getId());
		if (grnRequest.getGrnOtherChargesMapper() != null) {
			for (GRNOtherChargesMapper otherCharges : grnRequest.getGrnOtherChargesMapper()) {
				if (otherCharges.getId() != null) {
					GrnOtherChargesMapper grnOtherChargesMapper = new GrnOtherChargesMapper(
							new OtherCharge(otherCharges.getOtherChargesId()), otherCharges.getAmount());

					grnOtherChargesMapper.setAddedByUser(grnRequest.getUserId());
					grnOtherChargesMapper.setUpdatedByUser(grnRequest.getUserId());
					grnOtherChargesMapper.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					grnOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					grnOtherChargesMapper.setOrganizationId(grnRequest.getOrganizationId());
					grnOtherChargesMapper.setUnitId(grnRequest.getUnitId());
					grnOtherChargesMapper.setIsFromGrn(otherCharges.getIsFromGrn());
					// sessionService.setEntityDefaults(grnOtherChargesMapper);
					goodReceiptNote.addGRNOtherChargesMapperList(grnOtherChargesMapper);
//					GrnOtherChargesMapper OtherChargesmapper = grnDao.getGRNOtherChargesMapperById(otherCharges.getId());
//					OtherChargesmapper.setAmount(otherCharges.getAmount());
//					OtherChargesmapper.setOtherCharge(new OtherCharge(otherCharges.getOtherChargesId()));
					// OtherChargesmapper.setOtherChargesId(otherCharges.getOtherChargesId());
					// sessionService.setEntityUpdateDefaults(OtherChargesmapper);
//					grnDao.updateOtherCharges(OtherChargesmapper);
				} else {
					if(otherCharges.getOtherChargesId()!=null)
					{
					GrnOtherChargesMapper grnOtherChargesMapper = new GrnOtherChargesMapper(
							new OtherCharge(otherCharges.getOtherChargesId()), otherCharges.getAmount());

					grnOtherChargesMapper.setAddedByUser(grnRequest.getUserId());
					grnOtherChargesMapper.setUpdatedByUser(grnRequest.getUserId());
					grnOtherChargesMapper.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					grnOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
					grnOtherChargesMapper.setOrganizationId(grnRequest.getOrganizationId());
					grnOtherChargesMapper.setUnitId(grnRequest.getUnitId());
					grnOtherChargesMapper.setIsFromGrn(otherCharges.getIsFromGrn());
					// sessionService.setEntityDefaults(grnOtherChargesMapper);
					goodReceiptNote.addGRNOtherChargesMapperList(grnOtherChargesMapper);
					}

				}

			}

		}

		// check flag
		if (isSkipPhysicalInspection) {
			phyInspection.setUnitIdforAutoPhysicalInspection(grnRequest.getUnitId());
			response.setObject(phyInspection);
			response.setObject1(map);
			// this.saveAutoPhysicalInspection(phyInspection,
			// grnRequest.getDeliveryStoreId());

		}

		response.setMessage("Good Receipt Note Details approved successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse saveAutoPhysicalInspection(PhysicalInspection phy, Integer storeId) {
		CommonResponse response = new CommonResponse();
		PhysicalInspection phyInspection = PhysicalInspectionDao.getPhysicalnspectionDetailsForAuto(phy.getId(),
				phy.getUnitIdforAutoPhysicalInspection(), storeId);
		PhysicalInspectionSave physicalInspectionSaveDto = new PhysicalInspectionSave();
		physicalInspectionSaveDto.setAutoPhysicalInspectionSave(true);
		physicalInspectionSaveDto.setId(phyInspection.getId());
		physicalInspectionSaveDto.setUnitId(phyInspection.getUnit().getUnitId());

		if (storeId == 0) {
			storeId = phyInspection.getGoodReceiptNote().getStore().getStoreId();
		}

		physicalInspectionSaveDto.setStoreId(storeId);
		physicalInspectionSaveDto.setStatusId(8);
		physicalInspectionSaveDto.setUserId(sessionService.getUserId());
		physicalInspectionSaveDto.setOrganizationId(phyInspection.getOrganization().getOrganizationId());
		// physicalInspectionSaveDto.setStoreId(phyInspection.getGoodReceiptNote().getStore().getStoreId());
		List<PhysicalInspectionSaveItem> pItemList = new ArrayList<>();
		for (PhysicalInspectionDetail physicalInspectionDetails : phyInspection.getPhysicalInspectionDetails()) {
			PhysicalInspectionSaveItem itemData = new PhysicalInspectionSaveItem();
			itemData.setId(physicalInspectionDetails.getId());
			if (physicalInspectionDetails.getGoodReceiptNoteDetail().getGrnApprovedQuantity() != null) {
				itemData.setApprovedQuantity(
						physicalInspectionDetails.getGoodReceiptNoteDetail().getGrnApprovedQuantity());
			}
			if (physicalInspectionDetails.getGoodReceiptNoteDetail().getRejectedQuantity() != null) {
				itemData.setRejectQuantity(physicalInspectionDetails.getGoodReceiptNoteDetail().getRejectedQuantity());
				itemData.setRejectReasonId(
						physicalInspectionDetails.getGoodReceiptNoteDetail().getRejectReason().getId());
				itemData.setRejectNote(physicalInspectionDetails.getGoodReceiptNoteDetail().getRejectNote());
			}
			itemData.setLcop(physicalInspectionDetails.getGoodReceiptNoteDetail().getLcop());
			itemData.setStatusId(2);
			if (physicalInspectionDetails.getGoodReceiptNoteDetail().getBonusApprovedQuantity() != null
					&& physicalInspectionDetails.getGoodReceiptNoteDetail().getBonusApprovedQuantity() > 0) {
				itemData.setBonusApprovedQuantity(
						physicalInspectionDetails.getGoodReceiptNoteDetail().getBonusApprovedQuantity());
			}

			pItemList.add(itemData);

		}
		physicalInspectionSaveDto.setItems(pItemList);
		PhysicalInspectionService.savePhysicalnspection(physicalInspectionSaveDto);

		response.setMessage("Good Receipt Note Details approved successfully");

		return response;

	}

	private Batch InsertApprovedItems(GoodReceiptNoteDetail detail, Integer storeId, Integer assetTypeId,
			GRNRequest grnRequest, boolean isSkipPhysicalInspection) {

		Batch batch = null;

		Integer leaseQuantity = 0;
		Double copLeaseAmount = 0.0;
		Double mrpLeaseAmount = 0.0;
		Double taxLeaseAmount = 0.0;
		Double lcopLeaseAmount = 0.0;
		Integer storeQty = 0;
		Integer rejectedQuantity = 0;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			Integer leaseApprovedQuantity = Util.GetLeaseQuantity(
					detail.getGrnApprovedQuantity() != null ? detail.getGrnApprovedQuantity() : 0,
					detail.getGrnUomMappersList(), detail.getGrnUomType());
			Integer leaseBonusQuantity = Util.GetLeaseQuantity(
					detail.getBonusApprovedQuantity() != null ? detail.getBonusApprovedQuantity() : 0,
					detail.getGrnBonusUomMappersList(), detail.getBonusUomType());
			
			 rejectedQuantity = Util.GetLeaseQuantity(
					detail.getRejectedQuantity() != null ? detail.getRejectedQuantity() : 0,
							detail.getGrnUomMappersList(), detail.getGrnUomType());

			leaseQuantity = leaseApprovedQuantity + leaseBonusQuantity;

			copLeaseAmount = Util.GetMrpLeaseQuantity(detail.getCop(), detail.getGrnUomMappersList(),
					detail.getGrnUomType());

			mrpLeaseAmount = Util.GetMrpLeaseQuantity(detail.getMrp(), detail.getGrnUomMappersList(),
					detail.getGrnUomType());

			taxLeaseAmount = Util.GetMrpLeaseQuantity(detail.getTaxAmount(), detail.getGrnUomMappersList(),
					detail.getGrnUomType());
			
			if(detail.getLcop()!=null)
			{
				lcopLeaseAmount = detail.getLcop();
			}
			
			
			Batch oldBatch = GlobalDao.getBatchForNa(detail.getBatchNo() != null ? detail.getBatchNo() : null,
					detail.getItem().getId(), detail.getGoodReceiptNote().getStore().getStoreId(), copLeaseAmount,
					mrpLeaseAmount, detail.getUnitId(), detail.getExpiryDate() != null ? detail.getExpiryDate() : null);
			if (oldBatch != null) {
				oldBatch.setQuarantineQuantity(
						(oldBatch.getQuarantineQuantity() != null ? oldBatch.getQuarantineQuantity() : 0)
								+ leaseQuantity);
				oldBatch.setIsOldBatch(true);
				if(oldBatch.isQuarantine().equals(false))
				{
					//oldBatch.setQuantity(oldBatch.getQuantity());
				}
				else
				{
					oldBatch.setQuantity(oldBatch.getQuantity() + leaseQuantity);
				}
				
				Boolean batchFlag = false;
				for (StoreStock s : oldBatch.getStoreStockList()) {
					if (s.getStore().getStoreId().equals(detail.getGoodReceiptNote().getStore().getStoreId())) {
						
						if(oldBatch.isQuarantine().equals(false))
						{
							//s.setTotalQty(s.getTotalQty() + leaseQuantity);
							//s.setAvailableQty(s.getAvailableQty() + leaseQuantity);
							storeQty = s.getAvailableQty();
						}
						else
						{
							s.setTotalQty(s.getTotalQty() + leaseQuantity);
							storeQty = s.getAvailableQty();
							s.setAvailableQty(s.getAvailableQty() + leaseQuantity);
						}
						
						batchFlag = true;
						if (batchFlag.equals(true)) {
							break;
						}

					}

				}

				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
				StockTransaction stockTransaction = new StockTransaction(copLeaseAmount,oldBatch.getQuarantineQuantity(),
						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
						leaseQuantity, mrpLeaseAmount, new Store(storeId), oldBatch,detail.getId());
				// sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setTransitNo(detail.getGoodReceiptNote().getGrnNumber());
				stockTransaction.setTransactionId(detail.getGoodReceiptNote().getId());
				stockTransaction.setTransactionDetialsId(detail.getId());
				stockTransaction.setInQty(leaseQuantity);
				if(isSkipPhysicalInspection == true) {
					if(rejectedQuantity != null && rejectedQuantity>0) {
						stockTransaction.setOutQty(rejectedQuantity);
						stockTransaction.setIsTransit(true);
					}else {
						stockTransaction.setIsTransit(false);
					}
					
				}else {
					stockTransaction.setIsTransit(true);
					if(rejectedQuantity != null && rejectedQuantity>0) {
						stockTransaction.setOutQty(rejectedQuantity);
					}
					stockTransaction.setCurrentBal(storeQty+oldBatch.getQuarantineQuantity());
				}
				
				stockTransaction.setItem(detail.getItem());
				stockTransaction.setCreateDefaults(detail.getOrganizationId(), detail.getUnitId(),
						detail.getAddedByUser());
				oldBatch.addStockTransaction(stockTransaction);
				return oldBatch;
			} else {

				batch = new Batch(mrpLeaseAmount, detail.getBatchNo(), copLeaseAmount, leaseQuantity,
						detail.getExpiryDate(), new Item(detail.getItem().getId()), mrpLeaseAmount,
						detail.getLotNumber(), 0.0, leaseQuantity, mrpLeaseAmount, mrpLeaseAmount, taxLeaseAmount,
						new Tax(detail.getTax().getId()), detail.getTaxPercentage(),
						detail.getGrnUomUnit() != null ? new UomUnit(detail.getGrnUomUnit().getId()) : null, true,
						new Vendor(detail.getGoodReceiptNote().getVendor().getId()), true);
				batch.setQuarantineQuantity(leaseQuantity);
				batch.setQuantity(leaseQuantity);
				batch.setCurrentQuantity(leaseQuantity);
				batch.setActualLendingPrice(lcopLeaseAmount);
				batch.setLcop(lcopLeaseAmount);

				if (detail.getGrnApprovedQuantity() > 0) {
					for (GrnUomMapper grnUom : detail.getGrnUomMappersList()) {
						BatchUomMapper batchUomMapper = new BatchUomMapper(grnUom.getConversions(), grnUom.getIpUom(),
								grnUom.getOpUom(), grnUom.getStoreUom(), new UomType(grnUom.getUomType().getId()),
								new UomUnit(grnUom.getUomUnit().getId()));
						batchUomMapper.setAddedByUser(grnRequest.getUserId());
						batchUomMapper.setUpdatedByUser(grnRequest.getUserId());
						batchUomMapper.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						batchUomMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						batchUomMapper.setOrganizationId(grnRequest.getOrganizationId());
						batchUomMapper.setUnitId(grnRequest.getUnitId());
						batch.addBatchUom(batchUomMapper);
					}
				} else if (detail.getBonusApprovedQuantity() > 0) {
					for (GrnBonusUomMapper bonusUom : detail.getGrnBonusUomMappersList()) {
						BatchUomMapper batchUomMapper = new BatchUomMapper(bonusUom.getConversions(),
								bonusUom.getIpUom(), bonusUom.getOpUom(), bonusUom.getStoreUom(),
								new UomType(bonusUom.getUomType().getId()), new UomUnit(bonusUom.getUomUnit().getId()));
						batchUomMapper.setAddedByUser(grnRequest.getUserId());
						batchUomMapper.setUpdatedByUser(grnRequest.getUserId());
						batchUomMapper.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						batchUomMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
						batchUomMapper.setOrganizationId(grnRequest.getOrganizationId());
						batchUomMapper.setUnitId(grnRequest.getUnitId());
						batch.addBatchUom(batchUomMapper);
					}
				}
			}

		} else if (assetTypeId.equals(Constants.getFixedAssetId())) {
			// leaseQuantity = detail.getGrnApprovedQuantity() +
			// detail.getBonusApprovedQuantity();
			Integer bonusRQuantity = 0;
			if (detail.getBonusReceivedQuantity() != null) {
				bonusRQuantity = detail.getBonusReceivedQuantity();
			}
			leaseQuantity = detail.getGrnReceivedQuantity() + bonusRQuantity;
			 rejectedQuantity = detail.getRejectedQuantity();
			
			Batch oldBatch = GlobalDao.getBatchForNa(detail.getBatchNo() != null ? detail.getBatchNo() : null,
					detail.getItem().getId(), detail.getGoodReceiptNote().getStore().getStoreId(), copLeaseAmount,
					mrpLeaseAmount, detail.getUnitId(), detail.getExpiryDate() != null ? detail.getExpiryDate() : null);
			if (oldBatch != null) {
				oldBatch.setQuarantineQuantity(
						(oldBatch.getQuarantineQuantity() != null ? oldBatch.getQuarantineQuantity() : 0)
								+ leaseQuantity);
				oldBatch.setIsOldBatch(true);
				oldBatch.setQuantity(oldBatch.getQuantity() + leaseQuantity);
				Boolean batchFlag = false;
				for (StoreStock s : oldBatch.getStoreStockList()) {
					if (s.getStore().getStoreId().equals(detail.getGoodReceiptNote().getStore().getStoreId())) {
						s.setTotalQty(s.getTotalQty() + leaseQuantity);
						storeQty = s.getAvailableQty();
						s.setAvailableQty(s.getAvailableQty() + leaseQuantity);
						batchFlag = true;
						if (batchFlag.equals(true)) {
							break;
						}

					}

				}

				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
				StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, oldBatch.getQuarantineQuantity() ,
						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
						leaseQuantity, mrpLeaseAmount, new Store(storeId), oldBatch,detail.getId());
				// sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setTransitNo(detail.getGoodReceiptNote().getGrnNumber());
				stockTransaction.setCreateDefaults(detail.getOrganizationId(), detail.getUnitId(),
						detail.getAddedByUser());
				stockTransaction.setTransactionId(detail.getGoodReceiptNote().getId());
				stockTransaction.setInQty(leaseQuantity);
				if(isSkipPhysicalInspection == true) {
					if(rejectedQuantity != null && rejectedQuantity>0) {
						stockTransaction.setOutQty(rejectedQuantity);
						stockTransaction.setIsTransit(true);
					}else {
						stockTransaction.setIsTransit(false);
					}
					
				}else {
					stockTransaction.setIsTransit(true);
					if(rejectedQuantity != null && rejectedQuantity>0) {
						stockTransaction.setOutQty(rejectedQuantity);
					}
					stockTransaction.setCurrentBal(storeQty+oldBatch.getQuarantineQuantity());
				}
				stockTransaction.setItem(detail.getItem());
				stockTransaction.setTransactionDetialsId(detail.getId());
				oldBatch.addStockTransaction(stockTransaction);
				return oldBatch;
			} else {
				batch = new Batch(detail.getMrp(), detail.getBatchNo(), detail.getCop(), leaseQuantity,
						detail.getExpiryDate(), new Item(detail.getItem().getId()), detail.getMrp(),
						detail.getLotNumber(), 0.0, leaseQuantity, detail.getMrp(), detail.getMrp(),
						detail.getTaxAmount(), new Tax(detail.getTax().getId()), detail.getTaxPercentage(),
						detail.getGrnUomUnit() != null ? new UomUnit(detail.getGrnUomUnit().getId()) : null, true,
						new Vendor(detail.getGoodReceiptNote().getVendor().getId()), true);
				batch.setQuarantineQuantity(leaseQuantity);
				batch.setQuantity(leaseQuantity); // HIS-8741
				batch.setCurrentQuantity(leaseQuantity); // HIS-8741
				batch.setActualLendingPrice(detail.getLcop() != null ? detail.getLcop() : 0);
				batch.setLcop(detail.getLcop() != null ? detail.getLcop() : 0);
			}

		}
		StoreStock storeStock = new StoreStock(leaseQuantity,
				new Store(detail.getGoodReceiptNote().getStore().getStoreId()), 0, new Batch(batch.getId()));
		storeStock.setIsConsignment(detail.getGoodReceiptNote().getIsConsignment());
		storeStock.setTotalQty(leaseQuantity);
		storeQty = storeStock.getAvailableQty();
		storeStock.setAvailableQty(leaseQuantity); // HIS-8741
		// sessionService.setEntityDefaults(storeStock);
		storeStock.setAddedByUser(grnRequest.getUserId());
		storeStock.setUpdatedByUser(grnRequest.getUserId());
		storeStock.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		storeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		storeStock.setOrganizationId(grnRequest.getOrganizationId());
		storeStock.setUnitId(grnRequest.getUnitId());
		batch.addStoreStock(storeStock);
		batch.setReturnedQuantity(0);
		
		StoreStock storeStockDto = GlobalDao.getStoreStockByBatchAndStore1(batch.getId(),storeId,grnRequest.getUnitId() != null ? grnRequest.getUnitId() : sessionService.getUnitId());
		
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, leaseQuantity,
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
				leaseQuantity, mrpLeaseAmount, new Store(storeId), new Batch(batch.getId()),detail.getId());
		stockTransaction.setCreateDefaults(
				grnRequest.getOrganizationId() != null ? grnRequest.getOrganizationId()
						: sessionService.getOrganizationId(),
				grnRequest.getUnitId() != null ? grnRequest.getUnitId() : sessionService.getUnitId(),
				grnRequest.getUserId() != null ? grnRequest.getUserId() : sessionService.getUserId());
		stockTransaction.setTransitNo(detail.getGoodReceiptNote().getGrnNumber());
		stockTransaction.setTransactionDetialsId(detail.getId());
		stockTransaction.setTransactionId(detail.getGoodReceiptNote().getId());
		stockTransaction.setTransactionDetialsId(detail.getId());
		stockTransaction.setInQty(leaseQuantity);
		if(isSkipPhysicalInspection == true) {
			if(rejectedQuantity != null && rejectedQuantity>0) {
				stockTransaction.setOutQty(rejectedQuantity);
				stockTransaction.setIsTransit(true);
			}else {
				stockTransaction.setIsTransit(false);
			}
			
		}else {
			stockTransaction.setIsTransit(true);
			if(rejectedQuantity != null && rejectedQuantity>0) {
				stockTransaction.setOutQty(rejectedQuantity);
			}
			stockTransaction.setCurrentBal((storeStockDto!= null ?storeStockDto.getAvailableQty():0)+leaseQuantity);
		}
		stockTransaction.setItem(detail.getItem());
		batch.addStockTransaction(stockTransaction);
		batch.setCreateDefaults(
				grnRequest.getOrganizationId() != null ? grnRequest.getOrganizationId()
						: sessionService.getOrganizationId(),
				grnRequest.getUnitId() != null ? grnRequest.getUnitId() : sessionService.getUnitId(),
				grnRequest.getUserId() != null ? grnRequest.getUserId() : sessionService.getUserId());
		return batch;
	}

	private Batch InsertApprovedItems(GoodReceiptNoteDetail detail, Integer storeId, Integer assetTypeId,
			GRNStatusDto grnStatusDto, boolean isSkipPhysicalInspection) {

		Batch batch = null;

		Integer leaseQuantity = 0;
		Double copLeaseAmount = 0.0;
		Double mrpLeaseAmount = 0.0;
		Double taxLeaseAmount = 0.0;
		Double lcopLeaseAmount = 0.0;
		Integer storeQty = 0;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			Integer leaseApprovedQuantity = Util.GetLeaseQuantity(
					detail.getGrnApprovedQuantity() != null ? detail.getGrnApprovedQuantity() : 0,
					detail.getGrnUomMappersList(), detail.getGrnUomType());
			Integer leaseBonusQuantity = Util.GetLeaseQuantity(
					detail.getBonusApprovedQuantity() != null ? detail.getBonusApprovedQuantity() : 0,
					detail.getGrnBonusUomMappersList(), detail.getBonusUomType());

			leaseQuantity = leaseApprovedQuantity + leaseBonusQuantity;

			copLeaseAmount = Util.GetMrpLeaseQuantity(detail.getCop(), detail.getGrnUomMappersList(),
					detail.getGrnUomType());

			mrpLeaseAmount = Util.GetMrpLeaseQuantity(detail.getMrp(), detail.getGrnUomMappersList(),
					detail.getGrnUomType());

			taxLeaseAmount = Util.GetMrpLeaseQuantity(detail.getTaxAmount(), detail.getGrnUomMappersList(),
					detail.getGrnUomType());
			

			 if(detail.getLcop()!=null)
			 {
				 lcopLeaseAmount = detail.getLcop(); 
			 }
			
			Batch oldBatch = GlobalDao.getBatchForNa(detail.getBatchNo() != null ? detail.getBatchNo() : null,
					detail.getItem().getId(), detail.getGoodReceiptNote().getStore().getStoreId(), copLeaseAmount,
					mrpLeaseAmount, detail.getUnitId(), detail.getExpiryDate() != null ? detail.getExpiryDate() : null);
			if (oldBatch != null) {
				oldBatch.setQuarantineQuantity(
						(oldBatch.getQuarantineQuantity() != null ? oldBatch.getQuarantineQuantity() : 0)
								+ leaseQuantity);
				oldBatch.setIsOldBatch(true);
				// oldBatch.setQuantity(oldBatch.getQuantity() + leaseQuantity);
				Boolean batchFlag = false;
				
				  for (StoreStock s : oldBatch.getStoreStockList()) { 
					  if (s.getStore().getStoreId().equals(detail.getGoodReceiptNote().getStore().getStoreId())) { 
						  
						  if(oldBatch.isQuarantine().equals(false))
							{
								storeQty = s.getAvailableQty();
							}
							else
							{
								s.setTotalQty(s.getTotalQty() + leaseQuantity);
								storeQty = s.getAvailableQty();
								s.setAvailableQty(s.getAvailableQty() + leaseQuantity);
							}
							
							batchFlag = true;
							if (batchFlag.equals(true)) {
								break;
							}
						  
						  }
				  
				  }
				 
				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
				StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, oldBatch.getQuarantineQuantity(),
						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
						leaseQuantity, mrpLeaseAmount, new Store(storeId), oldBatch,detail.getId());
				// sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setTransitNo(detail.getGoodReceiptNote().getGrnNumber());
				stockTransaction.setTransactionId(detail.getGoodReceiptNote().getId());
				stockTransaction.setTransactionDetialsId(detail.getId());
				stockTransaction.setInQty(leaseQuantity);
				if(isSkipPhysicalInspection == true) {
					stockTransaction.setIsTransit(false);
				}else {
					stockTransaction.setIsTransit(true);
					stockTransaction.setCurrentBal(storeQty+oldBatch.getQuarantineQuantity());
				}
				stockTransaction.setItem(detail.getItem());
				stockTransaction.setCreateDefaults(detail.getOrganizationId(), detail.getUnitId(),
						detail.getAddedByUser());
				stockTransaction.setTransactionDetialsId(detail.getId());
				oldBatch.addStockTransaction(stockTransaction);
				return oldBatch;
			} else {
				batch = new Batch(mrpLeaseAmount, detail.getBatchNo(), copLeaseAmount, leaseQuantity,
						detail.getExpiryDate(), new Item(detail.getItem().getId()), mrpLeaseAmount,
						detail.getLotNumber(), 0.0, leaseQuantity, mrpLeaseAmount, mrpLeaseAmount, taxLeaseAmount,
						new Tax(detail.getTax().getId()), detail.getTaxPercentage(),
						detail.getGrnUomUnit() != null ? new UomUnit(detail.getGrnUomUnit().getId()) : null, true,
						new Vendor(detail.getGoodReceiptNote().getVendor().getId()), true);

				batch.setQuarantineQuantity(leaseQuantity);
				batch.setLcop(lcopLeaseAmount);
				batch.setActualLendingPrice(lcopLeaseAmount);
				if (detail.getGrnApprovedQuantity() > 0) {
					for (GrnUomMapper grnUom : detail.getGrnUomMappersList()) {
						BatchUomMapper batchUomMapper = new BatchUomMapper(grnUom.getConversions(), grnUom.getIpUom(),
								grnUom.getOpUom(), grnUom.getStoreUom(), new UomType(grnUom.getUomType().getId()),
								new UomUnit(grnUom.getUomUnit().getId()));
						batchUomMapper.setCreateDefaults(
								grnStatusDto.getOrganizationId() != null ? grnStatusDto.getOrganizationId()
										: sessionService.getOrganizationId(),
								grnStatusDto.getUnitId() != null ? grnStatusDto.getUnitId()
										: sessionService.getUnitId(),
								grnStatusDto.getUserId() != null ? grnStatusDto.getUserId()
										: sessionService.getUserId());

						batch.addBatchUom(batchUomMapper);
					}
				} else if (detail.getBonusApprovedQuantity() > 0) {
					for (GrnBonusUomMapper bonusUom : detail.getGrnBonusUomMappersList()) {
						BatchUomMapper batchUomMapper = new BatchUomMapper(bonusUom.getConversions(),
								bonusUom.getIpUom(), bonusUom.getOpUom(), bonusUom.getStoreUom(),
								new UomType(bonusUom.getUomType().getId()), new UomUnit(bonusUom.getUomUnit().getId()));
						batchUomMapper.setCreateDefaults(
								grnStatusDto.getOrganizationId() != null ? grnStatusDto.getOrganizationId()
										: sessionService.getOrganizationId(),
								grnStatusDto.getUnitId() != null ? grnStatusDto.getUnitId()
										: sessionService.getUnitId(),
								grnStatusDto.getUserId() != null ? grnStatusDto.getUserId()
										: sessionService.getUserId());

						batch.addBatchUom(batchUomMapper);
					}
				}
			}

		} else if (assetTypeId.equals(Constants.getFixedAssetId())) {
			// leaseQuantity = detail.getGrnApprovedQuantity() +
			// detail.getBonusApprovedQuantity();
			Integer bonusRQuantity = 0;
			if (detail.getBonusReceivedQuantity() != null) {
				bonusRQuantity = detail.getBonusReceivedQuantity();
			}
			leaseQuantity = detail.getGrnReceivedQuantity() + bonusRQuantity;
			Batch oldBatch = GlobalDao.getBatchForNa(detail.getBatchNo() != null ? detail.getBatchNo() : null,
					detail.getItem().getId(), detail.getGoodReceiptNote().getStore().getStoreId(), copLeaseAmount,
					mrpLeaseAmount, detail.getUnitId(), detail.getExpiryDate() != null ? detail.getExpiryDate() : null);
			if (oldBatch != null) {
				oldBatch.setQuarantineQuantity(
						(oldBatch.getQuarantineQuantity() != null ? oldBatch.getQuarantineQuantity() : 0)
								+ leaseQuantity);
				oldBatch.setIsOldBatch(true);
				// oldBatch.setQuantity(oldBatch.getQuantity() + leaseQuantity);
				Boolean batchFlag = false;
				/*
				 * for (StoreStock s : oldBatch.getStoreStockList()) { if
				 * (s.getStore().getStoreId().equals(detail.getGoodReceiptNote().getStore().
				 * getStoreId())) { s.setTotalQty(s.getTotalQty() + leaseQuantity);
				 * s.setAvailableQty(s.getAvailableQty() + leaseQuantity); batchFlag=true;
				 * if(batchFlag.equals(true)) { break; }
				 * 
				 * }
				 * 
				 * }
				 */

				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
				StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, oldBatch.getQuarantineQuantity(),
						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
						leaseQuantity, mrpLeaseAmount, new Store(storeId), oldBatch,detail.getId());
				// sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setTransitNo(detail.getGoodReceiptNote().getGrnNumber());
				stockTransaction.setCreateDefaults(detail.getOrganizationId(), detail.getUnitId(),
						detail.getAddedByUser());
				stockTransaction.setTransactionId(detail.getGoodReceiptNote().getId());
				stockTransaction.setInQty(leaseQuantity);
				if(isSkipPhysicalInspection == true) {
					stockTransaction.setIsTransit(false);
				}else {
					stockTransaction.setIsTransit(true);
				}
				stockTransaction.setItem(detail.getItem());
				stockTransaction.setTransactionDetialsId(detail.getId());
				oldBatch.addStockTransaction(stockTransaction);
				return oldBatch;
			} else {
				batch = new Batch(detail.getMrp(), detail.getBatchNo(), detail.getCop(), leaseQuantity,
						detail.getExpiryDate(), new Item(detail.getItem().getId()), detail.getMrp(),
						detail.getLotNumber(), 0.0, leaseQuantity, detail.getMrp(), detail.getMrp(),
						detail.getTaxAmount(), new Tax(detail.getTax().getId()), detail.getTaxPercentage(),
						detail.getGrnUomUnit() != null ? new UomUnit(detail.getGrnUomUnit().getId()) : null, true,
						new Vendor(detail.getGoodReceiptNote().getVendor().getId()), true);
				batch.setLcop(detail.getLcop() != null ? detail.getLcop() : 0);
				batch.setQuarantineQuantity(leaseQuantity);
				batch.setActualLendingPrice(detail.getLcop() != null ? detail.getLcop() : 0);
			}

		}
		StoreStock storeStock = new StoreStock(leaseQuantity,
				new Store(detail.getGoodReceiptNote().getStore().getStoreId()), 0, new Batch(batch.getId()));
		storeStock.setIsConsignment(detail.getGoodReceiptNote().getIsConsignment());
		storeStock.setTotalQty(leaseQuantity);
		// sessionService.setEntityDefaults(storeStock);
		storeStock.setAddedByUser(grnStatusDto.getUserId());
		storeStock.setUpdatedByUser(grnStatusDto.getUserId());
		storeStock.setAddedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
		storeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(grnStatusDto.getUnitId()));
		storeStock.setOrganizationId(grnStatusDto.getOrganizationId());
		storeStock.setUnitId(grnStatusDto.getUnitId());
		batch.addStoreStock(storeStock);
		batch.setReturnedQuantity(0);
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, leaseQuantity,
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
				leaseQuantity, mrpLeaseAmount, new Store(storeId), new Batch(batch.getId()),detail.getId());
		stockTransaction.setTransitNo(detail.getGoodReceiptNote().getGrnNumber());
		stockTransaction.setTransactionDetialsId(detail.getId());
		stockTransaction.setTransactionId(detail.getGoodReceiptNote().getId());
		stockTransaction.setTransactionDetialsId(detail.getId());
		stockTransaction.setInQty(leaseQuantity);
		if(isSkipPhysicalInspection == true) {
			stockTransaction.setIsTransit(false);
		}else {
			stockTransaction.setIsTransit(true);
		}
		stockTransaction.setItem(detail.getItem());
		stockTransaction.setCreateDefaults(
				grnStatusDto.getOrganizationId() != null ? grnStatusDto.getOrganizationId()
						: sessionService.getOrganizationId(),
				grnStatusDto.getUnitId() != null ? grnStatusDto.getUnitId() : sessionService.getUnitId(),
				grnStatusDto.getUserId() != null ? grnStatusDto.getUserId() : sessionService.getUserId());

		batch.addStockTransaction(stockTransaction);
		batch.setCreateDefaults(
				grnStatusDto.getOrganizationId() != null ? grnStatusDto.getOrganizationId()
						: sessionService.getOrganizationId(),
				grnStatusDto.getUnitId() != null ? grnStatusDto.getUnitId() : sessionService.getUnitId(),
				grnStatusDto.getUserId() != null ? grnStatusDto.getUserId() : sessionService.getUserId());

		return batch;
	}

	@Override
	@Transactional
	public Object getGRNSettlementList(GRNRequest grnRequest) {

//		grnRequest.setToDate((new DateTime(grnRequest.getToDate())).plusDays(1).toDate());

		List<GRNSearchResponse1> grnSearchListBP = grnDao.searchGRNListSettlement(grnRequest, screenId);

		Iterator<GRNSearchResponse1> itr = grnSearchListBP.iterator();
		while (itr.hasNext()) {
			GRNSearchResponse1 res = itr.next();
			String resStatus = "";
			Double comAmt = 0d;
			Double paidAmt = 0d;
			Double newNetAmount = 0d;
			Double checkNewNetAmount = 0d;
            newNetAmount=grnDao.getNetAmountSumByGrnIdFromGrnDetails(res.getId(),grnRequest.getUnitId(),grnRequest.getOrgId());
            boolean isNanCheck=newNetAmount.isNaN();
			double Output= new BigDecimal(isNanCheck==false ? newNetAmount:0).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
			newNetAmount=Output;
			Double newRoundOffAmount=grnDao.geRoundOffAmountSumByGrnIdFromGrnDetails(res.getId(),grnRequest.getUnitId(),grnRequest.getOrgId());
            Double creditNoteAmount=grnDao.getCreditNoteAmount(res.getId(),grnRequest.getUnitId());
//            if(newNetAmount>0)
//            {
//            	res.setNetAmount((newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0))-(creditNoteAmount!=null ? creditNoteAmount:0.00));
//            }
			if (res.getPaidAmount() != null) {
				paidAmt = res.getPaidAmount();
			}
			if (res.getPaidAmount() == null) {
				res.setPaidAmount(0d);
			}
			if (res.getBalanceAmount() == null) {
				res.setBalanceAmount(0d);
			}

			if (grnRequest.getPaymentStatuses() == 0) {
				if (Double.compare(paidAmt, comAmt) == 0) {
					// status pending id=2
					// goodReceiptNote.setStatus(new Status(2));
					res.setPaymentStatus("Pending");
				}

				else if (Double.compare(paidAmt, Double.valueOf(0.0)) > 0 && paidAmt < res.getNetAmount()) {
					// partially Settled id =19

					res.setPaymentStatus("Partially Settled");
				}

				// else if (paidAmt == res.getNetAmount()) {
				else if (Double.compare(paidAmt, res.getNetAmount()) == 0) {
					// settled id=20
					// res.setPaymentStatus("Settled");
					itr.remove(); // HIS-10415
				}
			} else {
				if (grnRequest.getPaymentStatuses() == 2) {
					if (Double.compare(paidAmt, comAmt) == 0) {
						res.setPaymentStatus("Pending");
					} else {
						itr.remove();
					}
				}

				if (grnRequest.getPaymentStatuses() == 19) {
					// if (paidAmt > 0 && paidAmt < res.getNetAmount()) {

					if (Double.compare(paidAmt, Double.valueOf(0.0)) > 0
							&& Double.compare(paidAmt, res.getNetAmount()) < 0) {
						res.setPaymentStatus("Partially Settled");
					} else {
						itr.remove();
					}
				}

				if (grnRequest.getPaymentStatuses() == 20) {
					// if (paidAmt == res.getNetAmount()) {
					if (Double.compare(paidAmt, res.getNetAmount()) == 0) {
						// res.setPaymentStatus("Settled");
						itr.remove(); // HIS-10415
					} else {
						itr.remove();
					}
				}
			}
			 if(newNetAmount>0)
	            {
					checkNewNetAmount = newNetAmount;
					checkNewNetAmount = (checkNewNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0))-(creditNoteAmount!=null ? creditNoteAmount:0.00);
					int amt = (int) checkNewNetAmount.doubleValue();
					int bal = (int) res.getBalanceAmount().doubleValue();
					if(amt != bal){
						res.setNetAmount(newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0));
						res.setBalanceAmount((newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0))-res.getPaidAmount());
					}
//					res.setNetAmount(newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0));
//					res.setBalanceAmount((newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0))-res.getPaidAmount());
	            }
		}

		return grnSearchListBP;

	}

	@Override
	@Transactional
	public Object getGoodReceiptNoteSettledList(GRNRequest grnRequest) {

		grnRequest.setToDate((new DateTime(grnRequest.getToDate())).plusDays(1).toDate());

		List<GRNSettledSearchResponse> grnSearch = grnDao.getGoodReceiptNoteSettledList(grnRequest);
		Iterator<GRNSettledSearchResponse> itr = grnSearch.iterator();
		while (itr.hasNext()) {
			GRNSettledSearchResponse res = itr.next();
			String resStatus = "";
			Double comAmt = 0d;
			Double paidAmt = 0d;
			Double newNetAmount = 0d;
			newNetAmount=grnDao.getNetAmountSumByGrnIdFromGrnDetails(res.getGrnid(),grnRequest.getUnitId(),grnRequest.getOrganizationId());
			Double newRoundOffAmount=grnDao.geRoundOffAmountSumByGrnIdFromGrnDetails(res.getGrnid(),grnRequest.getUnitId(),grnRequest.getOrganizationId());
            Double creditNoteAmount=grnDao.getCreditNoteAmount(res.getGrnid(),grnRequest.getUnitId());
            double Output= new BigDecimal(newNetAmount).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            newNetAmount=Output;
			res.setNetamount((newNetAmount+(newRoundOffAmount!=null ? newRoundOffAmount :0))-(creditNoteAmount!=null ? creditNoteAmount:0.00));
			if (res.getPaidamount() != null) {
				paidAmt = res.getPaidamount();
			}
			if (res.getPaidamount() == null) {
				res.setPaidamount(0d);
			}

			if (Double.compare(paidAmt, comAmt) == 0) {
				// status pending id=2
				// goodReceiptNote.setStatus(new Status(2));
				res.setPaymentStatus("Pending");
			}

			else if (Double.compare(paidAmt, Double.valueOf(0.0)) > 0 && paidAmt < res.getNetamount()) {
				// partially Settled id =19

				res.setPaymentStatus("Partially Settled");
			}

			// else if (paidAmt == res.getNetAmount()) {
			else if (Double.compare(paidAmt, res.getNetamount()) == 0 || paidAmt>res.getNetamount()) {
				// settled id=20
				res.setPaymentStatus("Settled");
			}

		}
		return grnSearch;

	}

	@Override
	@Transactional
	public Object getGoodReceiptNoteSettledDetails(Integer grnid, Integer unitId) {

		GRNSettledDetailsResponse grnSearchListBP = grnDao.getGoodReceiptNoteSettledDetails(grnid, unitId);
		grnSearchListBP.setSettledDetails(grnDao.settledDetails(grnSearchListBP.getGrnid()));
		/*
		 * for (GRNSettledDetailsResponse res : grnSearchListBP) {
		 * 
		 * 
		 * }
		 */
		return grnSearchListBP;

	}

	@Override
	@Transactional
	public Object getGRNSettlementDetails(Integer id, Integer unitId, Integer orgId) {

		GRNResponse grnResponseBP = grnDao.getGoodReceiptNoteSettledBP(id);
		GRNSettledDetailsResponse grnSearchListBP = grnDao.getGoodReceiptNoteSettledDetails(id, unitId);
		grnResponseBP.setReceiptDate(grnSearchListBP.getReceiptdate());
		grnResponseBP.setReceiptNumber(grnSearchListBP.getReceiptnumber());
		grnResponseBP.setPaidAmt(grnSearchListBP.getPaidamount());
		List<GRNDetailResponse> grnDetails = grnDao.getGoodReceiptNoteDetailsBP(id);

		List<Integer> consumableDetailIds = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId())).map(GRNDetailResponse::getId)
				.collect(Collectors.toList());
		List<Integer> fixedDetaildId = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId())).map(GRNDetailResponse::getId)
				.collect(Collectors.toList());
		List<Integer> consumabelItemIds = grnDetails.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId())).map(GRNDetailResponse::getItemId)
				.collect(Collectors.toList());

		// Required fields for consumable id's
		List<GRNUomMapper> grnUomBP = grnDao.getGRNUomMapperBP(consumableDetailIds);
		List<GRNUomMapper> bonusUomBP = grnDao.getGRNBonusUomMapperBP(consumableDetailIds);
		List<UomResponse> uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds, orgId);

		// Required fields for fixed asset id's
		List<GrnAccessoriesMapper> grnMapperListBP = new ArrayList<GrnAccessoriesMapper>();
		if (fixedDetaildId != null && !fixedDetaildId.isEmpty())
			grnMapperListBP = grnDao.getGrnAccessoryBP(fixedDetaildId);

		// These are the common fields
		List<Integer> allDetailIds = new ArrayList<Integer>();
		allDetailIds.addAll(consumableDetailIds);
		allDetailIds.addAll(fixedDetaildId);
		List<PurchasedOrderResponse> poListBP = grnDao.getPurchaseOrderIdBP(allDetailIds);
		// List<GRNOtherChargesMapper> otherChargesBP =
		// grnDao.getGRNOtherChargesMapperBP(allDetailIds);
		List<GRNStagedDto> grnStagedQunatityBP = grnDao.getGRNStagedQuantityForApprovalBP(allDetailIds);
		List<GRNOtherChargesMapper> otherChargesBP = grnDao.getGRNOtherChargesMapperBP(id);
		// These are the common fields too. With quantities
		consumabelItemIds.addAll(grnDetails.stream().filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(GRNDetailResponse::getItemId).collect(Collectors.toList()));
		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemBP(grnResponseBP.getStoreId(), consumabelItemIds,
				unitId);
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(grnResponseBP.getStoreId(),
				consumabelItemIds, unitId);
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(grnResponseBP.getStoreId(),
				consumabelItemIds, orgId);
		List<Quantities> leaseQuantities = itemsDao.getGrnLeaseQunatityBP(grnResponseBP.getStoreId(),
				consumabelItemIds);
		List<Quantities> approvedPRAndPOQuantities = itemsDao.getApprovedPRandPOQuantityList(grnResponseBP.getStoreId(),
				consumabelItemIds, unitId);
		List<Quantities> approvedPOQuantities = itemsDao
				.getApprovedPurchaseOrderQuantityList(grnResponseBP.getStoreId(), consumabelItemIds, unitId);
		List<CreditNoteAmountList> creditNoteAmountlists = grnDao.getCreditNoteAmt(id);
		grnResponseBP.setCreditNoteAmountList(creditNoteAmountlists);
		grnResponseBP.setGrnOtherChargesMapper(otherChargesBP);
		for (GRNDetailResponse grnDetailResponse : grnDetails) {
			// Get stock quantities populated
			long storeStock = 0;
			long stockInTransit = 0;
			int maxStoreQuantity = 0;
			long leaseQty = 0;
			Long approvedPRAndPOQuantity = 0l;
			Long approvedPOQuantity = 0l;

			List<Quantities> storeStock1 = storeStocks.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> stockInTransit1 = stockInTransits.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> maxStoreQuantity1 = maxStoreQuantitys.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			List<Quantities> leaseQuantity = leaseQuantities.stream()
					.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
			if (storeStock1 != null && !storeStock1.isEmpty())
				storeStock = storeStock1.getFirst().getQuantity();
			if (stockInTransit1 != null && !stockInTransit1.isEmpty())
				stockInTransit = stockInTransit1.getFirst().getQuantity();
			if (maxStoreQuantity1 != null && !maxStoreQuantity1.isEmpty())
				maxStoreQuantity = maxStoreQuantity1.getFirst().getIntQuantity();
			if (leaseQuantity != null && !leaseQuantity.isEmpty())
				leaseQty = leaseQuantity.getFirst().getQuantity();
			// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
			// leaseQty);
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
//					Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit
//							+ itemsDao.getGrnLeaseQunatity(grnDetailResponse.getStoreId(), grnDetailResponse.getItemId()));

			grnDetailResponse.setPoList(poListBP.stream()
					.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.toList()));
			/*
			 * grnDetailResponse.setOtherCharges(otherChargesBP.stream() .filter(x ->
			 * x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.
			 * toList()));
			 */
			grnDetailResponse.setGrnStagedQuantity(grnStagedQunatityBP.stream()
					.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId())).collect(Collectors.toList()));
			grnDetailResponse.setMaxStoreQuantity(maxStoreQuantity);

			if (grnDetailResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
				grnDetailResponse
						.setGrnUom(grnUomBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));
				grnDetailResponse.setBonusUom(
						bonusUomBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));

				List<UomResponse> uomResponse = uomResponses.stream()
						.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList());
				List<PurchaseOrderUomMapper> prUomResponse = new ArrayList<PurchaseOrderUomMapper>();
				for (UomResponse uom : uomResponse) {
					PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new UomType(uom.getUomTypeId()),
							new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
							uom.getStoreUom());
					prUom.setCreateDefaults(orgId != null ? orgId : sessionService.getOrganizationId(),
							unitId != null ? unitId : sessionService.getUnitId(), sessionService.getUserId());

					prUomResponse.add(prUom);
				}
				Long pendingQuantity = (long) Util.GetLeaseQuantityPurchase(maxOrderQuantity.intValue(), prUomResponse,
						new UomType(grnDetailResponse.getGrnUomTypeId()));
				if (approvedPRAndPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPRAndPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).getFirst().getQuantity();
				}
				if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPOQuantity = approvedPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).getFirst().getQuantity();
				}
				maxOrderQuantity = maxStoreQuantity
						- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);
				grnDetailResponse.setMaxOrderQuantity(maxOrderQuantity);

				if (grnDetailResponse.getPoPendingQuantity() != null)
					grnDetailResponse.setPoPendingQuantity(
							grnDetailResponse.getPoPendingQuantity() - grnDetailResponse.getPoGrnQuantity());
				else
					grnDetailResponse.setPoPendingQuantity(0);

				grnDetailResponse.setUom(uomResponse);
				grnResponseBP.addItems(grnDetailResponse);
			} else if (grnDetailResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				grnDetailResponse.setGrnAccessoriesMapper(
						grnMapperListBP.stream().filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getId()))
								.collect(Collectors.toList()));
				if (approvedPRAndPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPRAndPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).getFirst().getQuantity();
				}
				if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
						.collect(Collectors.toList()) != null
						&& !approvedPOQuantities.stream()
								.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
								.collect(Collectors.toList()).isEmpty()) {
					approvedPOQuantity = approvedPOQuantities.stream()
							.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
							.collect(Collectors.toList()).getFirst().getQuantity();
				}
				maxOrderQuantity = maxStoreQuantity
						- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);
				grnDetailResponse.setMaxOrderQuantity(maxOrderQuantity);
				grnResponseBP.addItems(grnDetailResponse);
			}
		}
		grnResponseBP.setOtherChargesList(GlobalDao.getOtherChargesBP(unitId));
		return grnResponseBP;

	}

	@Override
	@Transactional
	public boolean updateBatchIdForBonusGrn(Integer key, Long value) {
		return grnDao.updateBatchIdForBonusGrn(key,value);
	}

	@Override
	@Transactional
	public BigInteger alreadyGoodReceiptNoteExits(List<Integer> grnIds) {
		return grnDao.alreadyGoodReceiptNoteExits(grnIds);
	}
}
