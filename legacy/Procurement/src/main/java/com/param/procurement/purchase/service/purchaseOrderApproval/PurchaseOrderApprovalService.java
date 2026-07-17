package com.param.procurement.purchase.service.purchaseOrderApproval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.OtherCharge;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.User;
import com.param.entity.model.procurement.BonusUomMapper;
import com.param.entity.model.procurement.PoAccessoriesMapper;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseOrderOtherChargesMapper;
import com.param.entity.model.procurement.PurchaseOrderStaged;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dao.purchaseOrder.IPurchaseOrderDao;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperRequest;
import com.param.procurement.purchase.dto.purchaseOrder.PoPurchaseRequestArray;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderStagedDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomMapperDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailsRespDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDropDownDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDetailsDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderSearchDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderStatusDto;
import com.param.procurement.purchase.purchaseOrder.dto.QrDateDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
@Transactional
public class PurchaseOrderApprovalService implements IPurchaseOrderApprovalService {

	private Integer screenId = Screen.PurchaseOrderApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IItemsDao itemsDao;

	@Autowired
	private IPurchaseOrderDao purchaseOrderDao;

	@Autowired
	SessionService sessionService;

	@Override
	@Transactional
	public PurchaseOrderDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		PurchaseOrderDropDownDto purchaseOrdDropDownDto = new PurchaseOrderDropDownDto();

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			purchaseOrdDropDownDto.addRequestingStore(
					new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			purchaseOrdDropDownDto
					.addDeliveryStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			purchaseOrdDropDownDto.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		for (AssetType assetType : GlobalDao.getAssetTypeList(unitId, orgId)) {
			purchaseOrdDropDownDto
					.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return purchaseOrdDropDownDto;
	}

	@Override
	@Transactional
	public Object getPuchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto) {
		PurchaseOrdDto.setToDate((new DateTime(PurchaseOrdDto.getToDate())).plusDays(1).toDate());

		List<PurchaseOrderSearchDto> purchaseOrderSearchDtoList = new ArrayList<PurchaseOrderSearchDto>();

		for (PurchaseOrder purchaseOrder : purchaseOrderDao.searchPurchaseOrderList(PurchaseOrdDto, screenId)) {

			PurchaseOrderSearchDto purchaseReqSearchDto = new PurchaseOrderSearchDto(purchaseOrder.getId(),
					purchaseOrder.getAddedDate(), purchaseOrder.getPoNumber(), purchaseOrder.getStore().getStoreName(),
					purchaseOrder.getStore().getStoreId(), purchaseOrder.getAssetType().getType(),
					purchaseOrder.getRequestType().getType(), purchaseOrder.getApprovalStatus().getStatus(),
					purchaseOrder.getStatus().getStatus(), purchaseOrder.getAddedBy().getUserName(),
					purchaseOrder.getRemark(), purchaseOrder.getVendor().getName(), purchaseOrder.getAddedDate(),
					purchaseOrder.getRequestingStore().getStoreId(), purchaseOrder.getRequestingStore().getStoreName(),
					purchaseOrder.getIsConsignment() /* added by minesh for consignment (true/false) */
			);

			purchaseReqSearchDto.setReviewedBy(
					purchaseOrder.getReviewedBy() != null ? purchaseOrder.getReviewedBy().getUserId() : null);
			purchaseOrderSearchDtoList.add(purchaseReqSearchDto);

		}

		return purchaseOrderSearchDtoList;
	}

	@Override
	@Transactional
	public Object getPucrhaseOrder(Integer id, Integer unitId, Integer orgId) {

		PurchaseOrderMasterDetailsDto POMasterDetail = purchaseOrderDao.getPurchaseOrderBP(id);

		List<PurchaseOrderDetailsRespDto> PurchaseOrderDetailsRespDtoConsumables = purchaseOrderDao
				.getPoDetailsConsumablesByPoIdBP(id);

		List<Integer> detailsIds = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
				.map(PurchaseOrderDetailsRespDto::getId).collect(Collectors.toList());

		List<Integer> fixedDetaildId = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(PurchaseOrderDetailsRespDto::getId).collect(Collectors.toList());

		List<PurchaseOrderUomMapperDto> poUomBP = new ArrayList<PurchaseOrderUomMapperDto>();
		List<PurchaseOrderUomMapperDto> bonusUomBP = new ArrayList<PurchaseOrderUomMapperDto>();
		if (detailsIds.size() > 0) {
			poUomBP = purchaseOrderDao.getPurchaseOrderUomMapperBP(detailsIds);
			bonusUomBP = purchaseOrderDao.getBonusUomMapperBP(detailsIds);
		}

		if (detailsIds.size() != 0) {
			poUomBP = purchaseOrderDao.getPurchaseOrderUomMapperBP(detailsIds);
			bonusUomBP = purchaseOrderDao.getBonusUomMapperBP(detailsIds);
		}

		List<Integer> consumabelItemIds = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
				.map(PurchaseOrderDetailsRespDto::getItemId).collect(Collectors.toList());

		detailsIds.addAll(fixedDetaildId);

		List<PoPurchaseRequestArray> purchaseRequestTypeIdBP = purchaseOrderDao.getPurchaseRequestTypeIdBP(detailsIds);

		List<UomResponse> uomResponses = new ArrayList<UomResponse>();
		if (consumabelItemIds.size() > 0) {
			uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds, orgId);
		}

		if (consumabelItemIds.size() != 0) {
			uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds, orgId);
		}

		List<PoOtherChargesMapperDto> otherChargesBP = purchaseOrderDao.getPoOtherChargesMapperBPPoDetails(id);
		POMasterDetail.setOtherCharges(otherChargesBP);
		List<PurchaseOrderStagedDto> purchaseOrderStagedDtoBP = purchaseOrderDao.getPoStagedBP(detailsIds);
		List<PurchaseOrderStagedDto> initialPurchaseOrderStagedDtoBP = purchaseOrderDao.getInitialPurchaseOrderStagedDto(detailsIds);

		List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> accessoriesmapperBP = new ArrayList<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper>();
		if (fixedDetaildId != null && !fixedDetaildId.isEmpty()) {
			accessoriesmapperBP = purchaseOrderDao.getPoAccessoriesMapperBP(fixedDetaildId);
		}

		consumabelItemIds.addAll(PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(PurchaseOrderDetailsRespDto::getItemId).collect(Collectors.toList()));
		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemBP(POMasterDetail.getStoreId(), consumabelItemIds,
				unitId);
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(POMasterDetail.getStoreId(),
				consumabelItemIds, unitId);
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(POMasterDetail.getStoreId(),
				consumabelItemIds, orgId);
		List<Quantities> leaseQuantities = itemsDao.getLeaseQunatityBP(POMasterDetail.getStoreId(), consumabelItemIds);
		List<Quantities> approvedPRAndPOQuantities = itemsDao
				.getApprovedPRandPOQuantityList(POMasterDetail.getStoreId(), consumabelItemIds, unitId);
		List<Quantities> approvedPOQuantities = itemsDao
				.getApprovedPurchaseOrderQuantityList(POMasterDetail.getStoreId(), consumabelItemIds, unitId);

		for (PurchaseOrderDetailsRespDto purchaseOrderDetailsRespDto : PurchaseOrderDetailsRespDtoConsumables) {

			POMasterDetail.setTotalNetAmount(purchaseOrderDetailsRespDto.getTotalNetAmount()!=null ? purchaseOrderDetailsRespDto.getTotalNetAmount() : 0.00);
			List<PoPurchaseRequestArray> purchaseRequestTypeId = purchaseRequestTypeIdBP.stream()
					.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
					.collect(Collectors.toList());

			Integer prQunatity = null;
			if (purchaseRequestTypeId != null) {
				for (PoPurchaseRequestArray pr : purchaseRequestTypeId) {
					if (pr.getApprovedQuantity() != null) {
						prQunatity = pr.getApprovedQuantity();
					}
				}
			}

			List<PoOtherChargesMapperDto> otherCharges = otherChargesBP.stream()
					.filter(x -> x.getPoId().equals(purchaseOrderDetailsRespDto.getId())).collect(Collectors.toList());
			List<PurchaseOrderStagedDto> purchaseOrderStagedDto = purchaseOrderStagedDtoBP.stream()
					.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
					.collect(Collectors.toList());
			List<PurchaseOrderStagedDto> initialPurchaseOrderStagedDto = initialPurchaseOrderStagedDtoBP.stream()
					.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
					.collect(Collectors.toList());

			long storeStock = 0;
			long stockInTransit = 0;
			int maxStoreQuantity = 0;
			long leaseQty = 0;
			Long approvedPRAndPOQuantity = 0l;
			Long approvedPOQuantity = 0l;

			List<Quantities> storeStock1 = storeStocks.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList());
			List<Quantities> stockInTransit1 = stockInTransits.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList());
			List<Quantities> maxStoreQuantity1 = maxStoreQuantitys.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList());
			List<Quantities> leaseQuantity = leaseQuantities.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList());

			if (storeStock1 != null && !storeStock1.isEmpty()) {
				storeStock = storeStock1.getFirst().getQuantity();
				purchaseOrderDetailsRespDto.setStockAvailable(storeStock);
			}
			if (stockInTransit1 != null && !stockInTransit1.isEmpty())
				stockInTransit = stockInTransit1.getFirst().getQuantity();
			if (maxStoreQuantity1 != null && !maxStoreQuantity1.isEmpty())
				maxStoreQuantity = maxStoreQuantity1.getFirst().getIntQuantity();
			if (leaseQuantity != null && !leaseQuantity.isEmpty())
				leaseQty = leaseQuantity.getFirst().getQuantity();
			if (approvedPRAndPOQuantities.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
						.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
						.collect(Collectors.toList()).getFirst().getQuantity();
			}
			if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPOQuantities.stream()
							.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPOQuantity = approvedPOQuantities.stream()
						.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
						.collect(Collectors.toList()).getFirst().getQuantity();
			}
			// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
			// approvedPRAndPOQuantity + approvedPOQuantity);
			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
					approvedPRAndPOQuantity, approvedPOQuantity);

			// Long storeStock =
			// GlobalDao.getStoreStockByItem(purchaseOrderDetailsRespDto.getStoreId(),
			// purchaseOrderDetailsRespDto.getItemId());
			// Long stockInTransit =
			// GlobalDao.getStoreStockInTransitByItem(purchaseOrderDetailsRespDto.getStoreId(),
			// purchaseOrderDetailsRespDto.getItemId());
			// Integer maxStoreQuantity =
			// GlobalDao.getStoreMaxQuantityByItem(purchaseOrderDetailsRespDto.getStoreId(),
			// purchaseOrderDetailsRespDto.getItemId());
			// Long maxOrderQuantity = maxStoreQuantity
			// - (storeStock + stockInTransit +
			// itemsDao.getLeaseQunatity(purchaseOrderDetailsRespDto.getStoreId(),
			// purchaseOrderDetailsRespDto.getItemId()));
			if (purchaseOrderDetailsRespDto.getQrdid() != null) {
				List<QrDateDto> qrDateDto = purchaseOrderDao.getqrDetails(purchaseOrderDetailsRespDto.getQrdid());
				purchaseOrderDetailsRespDto.setQrDateDto(qrDateDto);
			}
			if (purchaseOrderDetailsRespDto.getAssetTypeId().equals(Constants.getConsumableId())) {
				purchaseOrderDetailsRespDto.setMaxOrderQuantity(maxOrderQuantity);
				purchaseOrderDetailsRespDto.setMaxStoreQuantity(maxStoreQuantity);
				purchaseOrderDetailsRespDto.setPrId(purchaseRequestTypeId);
				// purchaseOrderDetailsRespDto.setOtherCharges(otherCharges);
				purchaseOrderDetailsRespDto.setPurchaseOrderStagedDto(purchaseOrderStagedDto);
				purchaseOrderDetailsRespDto.setInitialPurchaseOrderStagedDto(initialPurchaseOrderStagedDto);
				purchaseOrderDetailsRespDto.setPrQuantity(prQunatity);
				if (poUomBP != null) {
					purchaseOrderDetailsRespDto.setPoUom(
							poUomBP.stream().filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
									.collect(Collectors.toList()));
				}
				List<PurchaseOrderUomMapperDto> bonusUomBP1 = null;
				if (bonusUomBP != null) {
					bonusUomBP1 = bonusUomBP.stream()
							.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
							.collect(Collectors.toList());
					purchaseOrderDetailsRespDto.setBonusUom(bonusUomBP1);
				}
				if (uomResponses != null)
					purchaseOrderDetailsRespDto.setUom(uomResponses.stream()
							.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
							.collect(Collectors.toList()));

				POMasterDetail.addItems(purchaseOrderDetailsRespDto);
			} else if (purchaseOrderDetailsRespDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				purchaseOrderDetailsRespDto.setMaxOrderQuantity(maxOrderQuantity);
				purchaseOrderDetailsRespDto.setMaxStoreQuantity(maxStoreQuantity);
				purchaseOrderDetailsRespDto.setPrId(purchaseRequestTypeId);
				// purchaseOrderDetailsRespDto.setOtherCharges(otherCharges);
				purchaseOrderDetailsRespDto.setPrQuantity(prQunatity);
				purchaseOrderDetailsRespDto.setPurchaseOrderStagedDto(purchaseOrderStagedDto);
				purchaseOrderDetailsRespDto.setPoAccessoriesMapper(accessoriesmapperBP.stream()
						.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
						.collect(Collectors.toList()));

				POMasterDetail.addItems(purchaseOrderDetailsRespDto);
			}

		}

		POMasterDetail.setOtherChargesList(GlobalDao.getOtherChargesBP(unitId));
		// POMasterDetail.setTaxList(GlobalDao.getTaxListBP());
		return POMasterDetail;

		/*
		 * List<PurchaseOrderDetailsRespDto> items = null;
		 * 
		 * PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(id);
		 * 
		 * PurchaseOrderMasterDetailsDto purchaseOrderMasterDetail = new
		 * PurchaseOrderMasterDetailsDto(purchaseOrder.getId(),
		 * purchaseOrder.getPoNumber(), purchaseOrder.getAssetType().getId(),
		 * purchaseOrder.getAssetType().getType(),
		 * purchaseOrder.getRequestType().getId(),
		 * purchaseOrder.getRequestType().getType(),
		 * purchaseOrder.getDiscountType()!=null ?
		 * purchaseOrder.getDiscountType().getId() : null,
		 * purchaseOrder.getDiscountType()!=null ?
		 * purchaseOrder.getDiscountType().getType() : null,
		 * purchaseOrder.getDiscount(), purchaseOrder.getIsConsignment(),
		 * purchaseOrder.getNetAmount(), purchaseOrder.getRoundOffAmount(),
		 * purchaseOrder.getTotalAmount(), purchaseOrder.getTotalDiscount(),
		 * purchaseOrder.getTotalOtherAmount(), purchaseOrder.getTotalTaxAmount(),
		 * purchaseOrder.getStatus().getId(), purchaseOrder.getStatus().getStatus(),
		 * purchaseOrder.getStatus().getId(), purchaseOrder.getStatus().getStatus(),
		 * purchaseOrder.getStore().getStoreId(),
		 * purchaseOrder.getStore().getStoreName(), purchaseOrder.getVendor().getId(),
		 * purchaseOrder.getVendor().getName(),purchaseOrder.getPaymentTerm(),
		 * purchaseOrder.getRemark(), purchaseOrder.getAddedBy().getUserName(),
		 * purchaseOrder.getAddedDate(), items);
		 * 
		 * for(PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList())
		 * {
		 * 
		 * if(detail.getIsActive()!=false && detail.getIsDeleted()!=true) {
		 * 
		 * List<PoPurchaseRequestArray> purchaseRequestTypeId =
		 * purchaseOrderDao.getPurchaseRequestTypeId(detail.getId()); Integer prQunatity
		 * = null; if (purchaseRequestTypeId != null) { for (PoPurchaseRequestArray pr :
		 * purchaseRequestTypeId) { if (pr.getApprovedQuantity() != null) { prQunatity =
		 * pr.getApprovedQuantity(); } } } Long storeStock =
		 * GlobalDao.getStoreStockByItem(detail.getPurchaseOrder().getStore().getStoreId
		 * (), detail.getItem().getId()); Long stockInTransit =
		 * GlobalDao.getStoreStockInTransitByItem(detail.getPurchaseOrder().getStore().
		 * getStoreId(), detail.getItem().getId()); Long lastMonthSale =
		 * GlobalDao.getLastMonthSale(detail.getPurchaseOrder().getStore().getStoreId(),
		 * detail.getItem().getId()); Long currentMonthSale =
		 * GlobalDao.getCurrentMonthSale(detail.getPurchaseOrder().getStore().getStoreId
		 * (), detail.getItem().getId()); Integer maxStoreQuantity =
		 * GlobalDao.getStoreMaxQuantityByItem(detail.getPurchaseOrder().getStore().
		 * getStoreId(), detail.getItem().getId()); // Long maxOrderQuantity =
		 * maxStoreQuantity - (storeStock + stockInTransit +
		 * (detail.getPoApprovedQuantity()!=null ? detail.getPoApprovedQuantity() :
		 * detail.getPoQuantity()) + // (detail.getBonusApprovedQuantity()!=null ?
		 * detail.getBonusApprovedQuantity() : detail.getBonusQuantity()!=null
		 * ?detail.getBonusQuantity() : 0));
		 * 
		 * Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
		 * itemsDao.getLeaseQunatity(detail.getPurchaseOrder().getStore().getStoreId(),
		 * detail.getItem().getId()));
		 * 
		 * List<PoOtherChargesMapperDto> otherCharges =
		 * getPoOtherChargesList(detail.getId()); List<PurchaseOrderStagedDto>
		 * purchaseOrderStagedDto = getPurchaseOrderStagedList(detail.getId());
		 * 
		 * if(purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId()))
		 * {
		 * 
		 * List<PurchaseOrderUomMapperDto> poUom =
		 * getPurchaseOrderUomList(detail.getId()); List<PurchaseOrderUomMapperDto>
		 * bonusUom = getBonusUomList(detail.getId()); Integer leaseQuantity = null;
		 * 
		 * purchaseOrderMasterDetail.addItems(new
		 * PurchaseOrderDetailsRespDto(detail.getId(),detail.getItem().getId(),detail.
		 * getItem().getCode(),detail.getItem().getName(),
		 * detail.getPoQuantity(),prQunatity,leaseQuantity,maxStoreQuantity,
		 * maxOrderQuantity,detail.getPoUomType().getId(),detail.getPoUomType().getType(
		 * ),detail.getPoUomUnit().getId(),detail.getPoUomUnit().getUnits(),
		 * detail.getBonusQuantity(), detail.getBonusUomType() != null ?
		 * detail.getBonusUomType().getId() : null, detail.getBonusUomType() != null ?
		 * detail.getBonusUomType().getType() : null, detail.getBonusUomType() != null ?
		 * detail.getBonusUomUnit().getId() : null, detail.getBonusUomUnit() != null ?
		 * detail.getBonusUomUnit().getUnits() : null,
		 * detail.getCop(),detail.getMrp(),detail.getDiscount(),detail.getTax().getId(),
		 * detail.getTax().getName(),detail.getTaxPercentage(),
		 * detail.getTotalAmount(),detail.getTaxAmount(),detail.getNetAmount(),detail.
		 * getTotalDiscount(), detail.getTotalOtherAmount(),
		 * detail.getStatus().getId(),detail.getStatus().getStatus(),
		 * detail.getManufacturer().getId(),detail.getManufacturer().getName(),
		 * detail.getSpecification(),detail.getRemark(),detail.getPoApprovedQuantity(),
		 * detail.getBonusApprovedQuantity(),detail.getRejectedQuantity(),
		 * GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId()),poUom,
		 * bonusUom,purchaseRequestTypeId,otherCharges,purchaseOrderStagedDto)); } else
		 * if(purchaseOrder.getAssetType().getId().equals(Constants.getFixedAssetId())){
		 * List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper>
		 * accessoriesmapper = getpoAccessoriesMapper(detail.getId());
		 * 
		 * purchaseOrderMasterDetail.addItems(new
		 * PurchaseOrderDetailsRespDto(detail.getId(),detail.getItem().getId(),detail.
		 * getItem().getCode(),detail.getItem().getName(),
		 * detail.getPoQuantity(),prQunatity,
		 * maxStoreQuantity,maxOrderQuantity,detail.getBonusQuantity(),detail.getCop(),
		 * detail.getMrp(),detail.getDiscount(),detail.getTax().getId(),detail.getTax().
		 * getCode(),detail.getTaxPercentage(),
		 * detail.getTotalAmount(),detail.getTaxAmount(),detail.getNetAmount(),detail.
		 * getTotalDiscount(), detail.getTotalOtherAmount(),
		 * detail.getStatus().getId(),detail.getStatus().getStatus(),
		 * detail.getManufacturer().getId(),detail.getManufacturer().getName(),
		 * detail.getSpecification(),detail.getRemark(),detail.getPoApprovedQuantity(),
		 * detail.getBonusApprovedQuantity(),detail.getRejectedQuantity(),
		 * detail.getwarrantyPeriod(), detail.getSupportTerms(),
		 * detail.getTechnicalSpecification(),
		 * detail.getExpectedPayment(),detail.getServiceProvider().getId(),detail.
		 * getServiceProvider().getName(),detail.getwarrantyDate(),
		 * accessoriesmapper,purchaseRequestTypeId,otherCharges,purchaseOrderStagedDto))
		 * ; }
		 * 
		 * 
		 * } } purchaseOrderMasterDetail.setOtherChargesList(getOtherCharges()); return
		 * purchaseOrderMasterDetail;
		 */
	}

	@Transactional
	private List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> getpoAccessoriesMapper(
			Integer purchaseOrderDetailId) {

		List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> response = new ArrayList<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper>();

		for (PoAccessoriesMapper mapper : purchaseOrderDao.getPoAccessoriesMapper(purchaseOrderDetailId)) {
			if (mapper.getIsActive() != false && mapper.getIsDeleted() != true) {
				com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper mapperdto = new com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper(
						mapper.getId(), mapper.getItem().getId(), mapper.getItem().getName(),
						mapper.getItem().getCode());
				response.add(mapperdto);
			}
		}
		return response;
	}

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
	private List<PurchaseOrderStagedDto> getPurchaseOrderStagedList(Integer purchaseOrderDetailId) {
		List<PurchaseOrderStagedDto> response = new ArrayList<PurchaseOrderStagedDto>();
		for (PurchaseOrderStaged poStaged : purchaseOrderDao.getPoStaged(purchaseOrderDetailId)) {
			if (poStaged.getIsActive() != false && poStaged.getIsDeleted() != true) {
				PurchaseOrderStagedDto purchaseOrderStagedDto = new PurchaseOrderStagedDto();
				purchaseOrderStagedDto.setQuantity(poStaged.getQuantity());
				purchaseOrderStagedDto.setId(poStaged.getId());
				purchaseOrderStagedDto.setStagedDate(poStaged.getStagedDate());
				purchaseOrderStagedDto.setApprovedQuantity(poStaged.getApprovedQunatity());
				response.add(purchaseOrderStagedDto);
			}
		}
		return response;
	}

	@Transactional
	private List<PurchaseOrderUomMapperDto> getPurchaseOrderUomList(Integer purchaseOrderDetailId) {
		List<PurchaseOrderUomMapperDto> response = new ArrayList<PurchaseOrderUomMapperDto>();
		for (PurchaseOrderUomMapper poUomMapper : purchaseOrderDao.getPurchaseOrderUomMapper(purchaseOrderDetailId)) {
			if (poUomMapper.getIsDeleted() == false && poUomMapper.getIsActive() == true) {
				response.add(new PurchaseOrderUomMapperDto(poUomMapper.getId(), poUomMapper.getUomType().getId(),
						poUomMapper.getUomType().getType(), poUomMapper.getUomUnit().getId(),
						poUomMapper.getUomUnit().getUnits(), poUomMapper.getConversions(), poUomMapper.getIpUom(),
						poUomMapper.getOpUom(), poUomMapper.getStoreUom()));
			}
		}
		return response;
	}

	@Transactional
	private List<PurchaseOrderUomMapperDto> getBonusUomList(Integer purchaseOrderDetailId) {
		List<PurchaseOrderUomMapperDto> response = new ArrayList<PurchaseOrderUomMapperDto>();
		for (BonusUomMapper bonusUomMapper : purchaseOrderDao.getBonusUomMapper(purchaseOrderDetailId)) {
			if (bonusUomMapper.getIsDeleted() == false && bonusUomMapper.getIsActive() == true) {
				response.add(new PurchaseOrderUomMapperDto(bonusUomMapper.getId(), bonusUomMapper.getUomType().getId(),
						bonusUomMapper.getUomType().getType(), bonusUomMapper.getUomUnit().getId(),
						bonusUomMapper.getUomUnit().getUnits(), bonusUomMapper.getConversions(),
						bonusUomMapper.getIpUom(), bonusUomMapper.getOpUom(), bonusUomMapper.getStoreUom()));

			}
		}
		return response;
	}

	@Transactional
	private List<PoOtherChargesMapperDto> getPoOtherChargesList(Integer purchaseOrderDetailId) {
		List<PoOtherChargesMapperDto> poOtherChargesList = new ArrayList<PoOtherChargesMapperDto>();
		for (PurchaseOrderOtherChargesMapper poOtherChargesMapper : purchaseOrderDao
				.getPoOtherChargesMapper(purchaseOrderDetailId)) {
			if (poOtherChargesMapper.getIsActive() != false && poOtherChargesMapper.getIsDeleted() != true) {
				poOtherChargesList.add(new PoOtherChargesMapperDto(poOtherChargesMapper.getId(),
						poOtherChargesMapper.getOtherCharge().getId(),
						poOtherChargesMapper.getOtherCharge().getDescription(), poOtherChargesMapper.getAmount()));
			}
		}
		return poOtherChargesList;
	}

	@Override
	@Transactional
	public Object approvePurchaseOrder(List<PurchaseOrderStatusDto> purchaseOrderStatusDtoList) {

		List<Integer> poIds = purchaseOrderStatusDtoList.stream().map(PurchaseOrderStatusDto::getPoId)
				.collect(Collectors.toList());

		List<PurchaseOrder> purchaseOrders = purchaseOrderDao.getPurchaseOrders(poIds);
		for (PurchaseOrderStatusDto PurchaseOrderStatusDto : purchaseOrderStatusDtoList) {
			List<PurchaseOrder> purchaseOrders1 = purchaseOrders.stream()
					.filter(x -> x.getId().equals(PurchaseOrderStatusDto.getPoId())).collect(Collectors.toList());

			if (purchaseOrders1 != null && !purchaseOrders1.isEmpty()) {
				PurchaseOrder purchaseOrder = purchaseOrders1.getFirst();
				boolean finalPOApprovalFlag=purchaseOrderDao.getFinalPOApprovalFlag(purchaseOrder.getUnitId());
				if(finalPOApprovalFlag==false)
				{
					purchaseOrder.setPoFinalApprovalRequired(true);
				}
				else
				{
					purchaseOrder.setPoFinalApprovalRequired(false);
				}
				if (PurchaseOrderStatusDto.getStatusId().equals(com.param.common.constants.Status.Pending.getStatus())
						|| PurchaseOrderStatusDto.getStatusId().equals(22)
						|| PurchaseOrderStatusDto.getStatusId().equals(5)) // 2 statusId for Approved
				{
					purchaseOrder.setStatus(new Status(PurchaseOrderStatusDto.getStatusId()));
					purchaseOrder.setApprovalStatus(new Status(PurchaseOrderStatusDto.getApprovalStatusId()));

					for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
						detail.setStatus(new Status(PurchaseOrderStatusDto.getApprovalStatusId()));
						detail.setPoApprovedQuantity(detail.getPoQuantity());
						detail.setBonusApprovedQuantity(detail.getBonusQuantity());
						if (purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId())) {
							detail.setPoLeaseQuantity(Util.GetLeaseQuantity(detail.getPoApprovedQuantity(),
									detail.getPurchaseOrderUomMapperList(),
									new UomType(detail.getPoUomType().getId())));
							if (detail.getPendingQuantity() != null) {
								String type="Against Purchase Request";
								if(PurchaseOrderStatusDto.getPurchaseType().equals(type))
								{
									detail.setPendingQuantity(detail.getPendingQuantity());
								}
								else
								{
									detail.setPendingQuantity(detail.getPendingQuantity() - Util.GetLeaseQuantity(
											detail.getPoApprovedQuantity(), detail.getPurchaseOrderUomMapperList(),
											new UomType(detail.getPoUomType().getId())));
								}
							}

							if (detail.getBonusApprovedQuantity() != null && detail.getBonusUomType() != null) {
								detail.setBonusLeaseQuantity(Util.GetLeaseQuantity(detail.getBonusApprovedQuantity(),
										detail.getBonusUomMapperList(), new UomType(detail.getBonusUomType().getId())));
							} else {
								detail.setBonusLeaseQuantity(0);
							}
						} else {
							detail.setPoLeaseQuantity(detail.getPoQuantity());
							detail.setBonusLeaseQuantity(detail.getBonusQuantity());
						}
						detail.setRejectedQuantity(0);
						detail.setRejectReason(new RejectReason(PurchaseOrderStatusDto.getRejectReasonId()));
						detail.setRejectNote(PurchaseOrderStatusDto.getRejectNote());

						for (PurchaseOrderStaged staged : detail.getPurchaseOrderStagedList()) {
							staged.approvePurchaseOrderStaged(staged.getQuantity());
						}
					}

					purchaseOrder.setApprovedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
					// purchaseOrder.setUpdatedByUser(PurchaseOrderStatusDto.getUserId()!=null ?
					// PurchaseOrderStatusDto.getUserId(): sessionService.getUserId());
				} else if (PurchaseOrderStatusDto.getStatusId()
						.equals(com.param.common.constants.Status.Closed.getStatus())) {
					purchaseOrder.setStatus(new Status(PurchaseOrderStatusDto.getStatusId()));
					purchaseOrder.setApprovalStatus(new Status(PurchaseOrderStatusDto.getApprovalStatusId()));
					for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
						detail.setStatus(new Status(PurchaseOrderStatusDto.getApprovalStatusId()));
						detail.setRejectedQuantity(detail.getPoQuantity());
						detail.setRejectReason(new RejectReason(PurchaseOrderStatusDto.getRejectReasonId()));
						detail.setRejectNote(PurchaseOrderStatusDto.getRejectNote());

					}
				}

				// start code by minesh for consignment PO status should be closed.
				if (purchaseOrder.getIsConsignment()) {
					purchaseOrder.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
				}
				// end code by minesh for consignment PO status should be closed.

				// purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
				purchaseOrder.setApprovedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
				purchaseOrder.setApprovedBy(
						new User(PurchaseOrderStatusDto.getUserId() != null ? PurchaseOrderStatusDto.getUserId()
								: sessionService.getUserId()));
				// purchaseOrder.setUpdatedByUser( PurchaseOrderStatusDto.getUserId()!=null ?
				// PurchaseOrderStatusDto.getUserId(): sessionService.getUserId());
				purchaseOrderDao.updatePurchaseOrder(purchaseOrder);

			}
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order save successfully");

		return response;

		/*
		 * for (PurchaseOrderStatusDto PurchaseOrderStatusDto :
		 * purchaseOrderStatusDtoList) { PurchaseOrder purchaseOrder =
		 * purchaseOrderDao.getPurchaseOrder(PurchaseOrderStatusDto.getPoId());
		 * 
		 * if(PurchaseOrderStatusDto.getStatusId().equals(com.param.common.constants.
		 * Status.Pending.getStatus())) // 2 statusId for Approved {
		 * purchaseOrder.setStatus(new Status(PurchaseOrderStatusDto.getStatusId()));
		 * purchaseOrder.setApprovalStatus(new
		 * Status(PurchaseOrderStatusDto.getApprovalStatusId()));
		 * 
		 * 
		 * for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList())
		 * { detail.setStatus(new Status(PurchaseOrderStatusDto.getApprovalStatusId()));
		 * detail.setPoApprovedQuantity(detail.getPoQuantity());
		 * detail.setBonusApprovedQuantity(detail.getBonusQuantity());
		 * if(purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId()))
		 * {
		 * detail.setPoLeaseQuantity(Util.GetLeaseQuantity(detail.getPoApprovedQuantity(
		 * ), detail.getPurchaseOrderUomMapperList(), new
		 * UomType(detail.getPoUomType().getId())));
		 * if(detail.getBonusApprovedQuantity() != null && detail.getBonusUomType() !=
		 * null){ detail.setBonusLeaseQuantity(Util.GetLeaseQuantity(detail.
		 * getBonusApprovedQuantity(), detail.getBonusUomMapperList(), new
		 * UomType(detail.getBonusUomType().getId()))); } else{
		 * detail.setBonusLeaseQuantity(0); } } else {
		 * detail.setPoLeaseQuantity(detail.getPoQuantity());
		 * detail.setBonusLeaseQuantity(detail.getBonusQuantity()); }
		 * detail.setRejectedQuantity(0); detail.setRejectReason(new
		 * RejectReason(PurchaseOrderStatusDto.getRejectReasonId()));
		 * detail.setRejectNote(PurchaseOrderStatusDto.getRejectNote());
		 * 
		 * for(PurchaseOrderStaged staged : detail.getPurchaseOrderStagedList()) {
		 * staged.approvePurchaseOrderStaged(staged.getQuantity()); } }
		 * 
		 * purchaseOrder.setApprovedDate(TimeZoneComponent.getDateByzone(unitId)); purchaseOrder.setApprovedBy(new
		 * User(sessionService.getUserId()));
		 * 
		 * 
		 * } //8 status Id for Reject create a Enum for Status else
		 * if(PurchaseOrderStatusDto.getStatusId().equals(com.param.common.constants.
		 * Status.Closed.getStatus())) { purchaseOrder.setStatus(new
		 * Status(PurchaseOrderStatusDto.getStatusId()));
		 * purchaseOrder.setApprovalStatus(new
		 * Status(PurchaseOrderStatusDto.getApprovalStatusId())); for
		 * (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
		 * detail.setStatus(new Status(PurchaseOrderStatusDto.getApprovalStatusId()));
		 * detail.setRejectedQuantity(detail.getPoQuantity());
		 * detail.setRejectReason(new
		 * RejectReason(PurchaseOrderStatusDto.getRejectReasonId()));
		 * detail.setRejectNote(PurchaseOrderStatusDto.getRejectNote());
		 * 
		 * } }
		 * 
		 * 
		 * purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		 * purchaseOrder.setUpdatedBy(purchaseOrder.getUser());
		 * purchaseOrderDao.updatePurchaseOrder(purchaseOrder); }
		 * 
		 * CommonResponse response = new CommonResponse();
		 * response.setMessage("Purchase Order approved successfully");
		 * 
		 * return response;
		 */
	}

	@Override
	@Transactional
	public Object approvePurchaseOrderDetail(PurchaseOrderMasterDto purchaseOrdDto) {

		PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(purchaseOrdDto.getId());
		purchaseOrder
				.setTotalOtherAmount(purchaseOrdDto.getTotalOtherAmount() != null ? purchaseOrdDto.getTotalOtherAmount()
						 : (purchaseOrder.getTotalOtherAmount()!=null ? purchaseOrder.getTotalOtherAmount() :0));
		purchaseOrder.setNetAmount(
				purchaseOrdDto.getNetAmount() != null ? purchaseOrdDto.getNetAmount() : purchaseOrder.getNetAmount());
		purchaseOrder.setTotalAmount(purchaseOrdDto.getTotalAmount() != null ? purchaseOrdDto.getTotalAmount()
				: purchaseOrder.getTotalAmount());
		purchaseOrder.setRoundOffAmount(purchaseOrdDto.getRoundOffAmount() != null ? purchaseOrdDto.getRoundOffAmount()
				: purchaseOrder.getRoundOffAmount());
		purchaseOrder.setTotalDiscount(purchaseOrdDto.getTotalDiscount() != null ? purchaseOrdDto.getTotalDiscount()
				: purchaseOrder.getTotalDiscount());
		purchaseOrder.setTotalTaxAmount(purchaseOrdDto.getTotalTaxAmount() != null ? purchaseOrdDto.getTotalTaxAmount()
				: purchaseOrder.getTotalTaxAmount());
		// purchaseOrder.setBalanceAmount(purchaseOrdDto.getNetAmount()!=null
		// ?purchaseOrdDto.getNetAmount() : purchaseOrder.getNetAmount());

		purchaseOrder.approvePurchaseOrder(
				purchaseOrdDto.getRemark() != null ? purchaseOrdDto.getRemark() : purchaseOrder.getRemark(),
				purchaseOrdDto.getPaymentTerm() != null ? purchaseOrdDto.getPaymentTerm()
						: purchaseOrder.getPaymentTerm());

		for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {

			for (PurchaseOrderDetailDto item : purchaseOrdDto.getItems()) {
				Integer rejectedQty = 0;

				if (item.getRejectedQuantity() != null) {
					rejectedQty = item.getRejectedQuantity();
				}

				if (item.getId() != null && item.getId().equals(detail.getId())) {
					// add if-else condition for resolve HIS-9502 Unable to save PO for asset on the
					// PO approval details screen
					if (purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId())) {
						// origin code (no changes regarding HIS-9502)
						detail.approvePurchaseOrderDetail(item.getPoApprovedQuantity(), item.getBonusApprovedQuantity(),
								item.getRejectedQuantity(), new RejectReason(item.getRejectReasonId()),
								item.getRejectNote(), new Status(item.getStatusId()),
								(new User(purchaseOrdDto.getUserId())), TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()),
								item.getRemark() != null ? item.getRemark() : detail.getRemark(),
								item.getPoApprovedQuantity() != null
										? Util.GetLeaseQuantity(item.getPoApprovedQuantity(),
												detail.getPurchaseOrderUomMapperList(),
												new UomType(item.getPoUomTypeId()))
										: detail.getPoLeaseQuantity(),
								item.getBonusApprovedQuantity() != null
										? Util.GetLeaseQuantity(item.getBonusApprovedQuantity(),
												detail.getBonusUomMapperList(),
												new UomType(detail.getBonusUomType()!=null ? detail.getBonusUomType().getId():null))
										: detail.getBonusLeaseQuantity() != null ? detail.getBonusLeaseQuantity() : 0);
						detail.setPoUomType(new UomType(item.getPoUomTypeId()!=null ? item.getPoUomTypeId():null));
						detail.setRoundOffAmount(purchaseOrdDto.getRoundOffAmount());
						detail.setTotalNetAmount(purchaseOrdDto.getTotalNetAmount());
						detail.setNetAmount(item.getNetAmount());
						detail.setTotalOtherAmount(item.getTotalOtherAmount());
						detail.setTotalAmount(item.getTotalAmount());
						detail.setTotalDiscount(item.getTotalDiscount());
						detail.setTaxAmount(item.getTaxAmount());
						if (detail.getPendingQuantity() != null) {
							String type="Against Purchase Request";
							if(purchaseOrdDto.getPurchaseType().equals(type))
							{
								detail.setPendingQuantity(detail.getPendingQuantity());
							}
							else
							{
								detail.setPendingQuantity(detail.getPendingQuantity() - item.getPoApprovedQuantity());
							}
						}

					} else {
						// add else condition for resolve HIS-9502 Unable to save PO for asset on the PO
						// approval details screen
						detail.approvePurchaseOrderDetail(item.getPoApprovedQuantity(), item.getBonusApprovedQuantity(),
								item.getRejectedQuantity(), new RejectReason(item.getRejectReasonId()),
								item.getRejectNote(), new Status(item.getStatusId()),
								(new User(purchaseOrdDto.getUserId())), TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()),
								item.getRemark() != null ? item.getRemark() : detail.getRemark(),
								item.getPoApprovedQuantity() != null ? item.getPoApprovedQuantity()
										: detail.getPoLeaseQuantity(),
								item.getBonusApprovedQuantity() != null ? item.getBonusApprovedQuantity()
										: detail.getBonusLeaseQuantity());
						detail.setRoundOffAmount(purchaseOrdDto.getRoundOffAmount());
						detail.setTotalNetAmount(purchaseOrdDto.getTotalNetAmount());
						detail.setNetAmount(item.getNetAmount());
						detail.setTotalOtherAmount(item.getTotalOtherAmount());
						detail.setTotalAmount(item.getTotalAmount());
						detail.setTotalDiscount(item.getTotalDiscount());
						detail.setTaxAmount(item.getTaxAmount());
					} // end else condition for resolve HIS-9502 Unable to save PO for asset on the PO
						// approval details screen

					for (PurchaseOrderStaged staged : detail.getPurchaseOrderStagedList()) {

						for (PurchaseOrderStagedDto stagedDto : item.getPurchaseOrderStaged()) {
							if (stagedDto.getId() != null && stagedDto.getId().equals(staged.getId())) {
								staged.approvePurchaseOrderStaged(stagedDto.getApprovedQuantity());
							}
						}
					}
				}
			}
		}
		purchaseOrderDao.updateOtherCharges(purchaseOrdDto.getId());
		if (purchaseOrdDto.getOtherCharges() != null) {
			for (PoOtherChargesMapperRequest poOtherCharges : purchaseOrdDto.getOtherCharges()) {
				if (poOtherCharges.getId() == null) {
					PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper = new PurchaseOrderOtherChargesMapper(
							new OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
					// sessionService.setEntityUpdateDefaults(purchaseOrderOtherChargesMapper);
					purchaseOrderOtherChargesMapper.setAddedByUser(purchaseOrdDto.getUserId());
					purchaseOrderOtherChargesMapper.setUpdatedByUser(purchaseOrdDto.getUserId());
					purchaseOrderOtherChargesMapper.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
					purchaseOrderOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
					purchaseOrderOtherChargesMapper.setIsFromPo(poOtherCharges.getIsFromPo());
//					purchaseOrderOtherChargesMapper.setOrganization(new Organization(purchaseOrdDto.getOrganizationId()));
//					purchaseOrderOtherChargesMapper.setUnit(new Unit(purchaseOrdDto.getUnitId()));
					purchaseOrderOtherChargesMapper.setOrganizationId(purchaseOrdDto.getOrganizationId());
					purchaseOrderOtherChargesMapper.setUnitId(purchaseOrdDto.getUnitId());
					purchaseOrder.addPoOtherChargesMapperList(purchaseOrderOtherChargesMapper);
				} else {
					if(poOtherCharges.getOtherChargesId()!=null)
					{
					PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper = new PurchaseOrderOtherChargesMapper(
							new OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
					// sessionService.setEntityUpdateDefaults(purchaseOrderOtherChargesMapper);
					purchaseOrderOtherChargesMapper.setAddedByUser(purchaseOrdDto.getUserId());
					purchaseOrderOtherChargesMapper.setUpdatedByUser(purchaseOrdDto.getUserId());
					purchaseOrderOtherChargesMapper.setAddedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
					purchaseOrderOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
					purchaseOrderOtherChargesMapper.setIsFromPo(poOtherCharges.getIsFromPo());
//					purchaseOrderOtherChargesMapper.setOrganization(new Organization(purchaseOrdDto.getOrganizationId()));
//					purchaseOrderOtherChargesMapper.setUnit(new Unit(purchaseOrdDto.getUnitId()));
					purchaseOrderOtherChargesMapper.setOrganizationId(purchaseOrdDto.getOrganizationId());
					purchaseOrderOtherChargesMapper.setUnitId(purchaseOrdDto.getUnitId());
					purchaseOrder.addPoOtherChargesMapperList(purchaseOrderOtherChargesMapper);
					}

				}
			}
		}

		purchaseOrder.setStatus(new Status(purchaseOrdDto.getStatusId()));
		purchaseOrder.setApprovalStatus(new Status(purchaseOrdDto.getApprovalStatusId()));

		// start code by minesh for consignment PO status should be closed.
		if (purchaseOrder.getIsConsignment()) {
			purchaseOrder.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
		}
		// end code by minesh for consignment PO status should be closed.

		purchaseOrder.setApprovedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
		purchaseOrder.setApprovedBy(new User(purchaseOrdDto.getUserId()));

		// purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		// purchaseOrder.setUpdatedByUser(purchaseOrdDto.getUserId());
		boolean finalPOApprovalFlag=purchaseOrderDao.getFinalPOApprovalFlag(purchaseOrder.getUnitId());
		if(finalPOApprovalFlag==false)
		{
			purchaseOrder.setPoFinalApprovalRequired(true);
		}
		else
		{
			purchaseOrder.setPoFinalApprovalRequired(false);
		}
		purchaseOrderDao.updatePurchaseOrder(purchaseOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order Quantity approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object secondLevelApprovePurchaseOrderDetail(PurchaseOrderMasterDto purchaseOrdDto) {

		PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(purchaseOrdDto.getId());
		purchaseOrder.setStatus(new Status(com.param.common.constants.Status.Approved.getStatus()));
		purchaseOrder.setReviewedBy(new User(purchaseOrdDto.getUserId()));
		purchaseOrder.setReviewedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
		purchaseOrder.setPoFinalApprovalRequired(true);
		purchaseOrderDao.updatePurchaseOrder(purchaseOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order approved successfully");

		return response;

	}

	@Override
	@Transactional
	public Object getPurchaseOrderFinalList(PurchaseOrderMasterDto PurchaseOrdDto) {

		PurchaseOrdDto.setToDate((new DateTime(PurchaseOrdDto.getToDate())).plusDays(1).toDate());

		List<PurchaseOrderSearchDto> purchaseOrderSearchDtoList = new ArrayList<PurchaseOrderSearchDto>();

		for (PurchaseOrder purchaseOrder : purchaseOrderDao.searchPurchaseOrderList(PurchaseOrdDto, screenId)) {

			if (purchaseOrder.getApprovalStatus().getId().equals(5)) {
				PurchaseOrderSearchDto purchaseReqSearchDto = new PurchaseOrderSearchDto(purchaseOrder.getId(),
						purchaseOrder.getAddedDate(), purchaseOrder.getPoNumber(),
						purchaseOrder.getStore().getStoreName(), purchaseOrder.getStore().getStoreId(),
						purchaseOrder.getAssetType().getType(), purchaseOrder.getRequestType().getType(),
						purchaseOrder.getApprovalStatus().getStatus(), purchaseOrder.getStatus().getStatus(),
						purchaseOrder.getAddedBy().getUserName(), purchaseOrder.getRemark(),
						purchaseOrder.getVendor().getName(), purchaseOrder.getAddedDate(),
						purchaseOrder.getRequestingStore().getStoreId(),
						purchaseOrder.getRequestingStore().getStoreName(),
						purchaseOrder.getIsConsignment() /* added by minesh for consignment (true/false) */
				);

				purchaseReqSearchDto.setReviewedBy(
						purchaseOrder.getReviewedBy() != null ? purchaseOrder.getReviewedBy().getUserId() : null);
				purchaseOrderSearchDtoList.add(purchaseReqSearchDto);
			}

		}

		return purchaseOrderSearchDtoList;

	}

}
