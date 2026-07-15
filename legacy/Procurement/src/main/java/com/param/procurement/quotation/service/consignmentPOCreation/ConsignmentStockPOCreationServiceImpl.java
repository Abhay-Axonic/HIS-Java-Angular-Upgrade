package com.param.procurement.quotation.service.consignmentPOCreation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.OtherCharge;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.BonusUomMapper;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.PoAccessoriesMapper;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseOrderOtherChargesMapper;
import com.param.entity.model.procurement.PurchaseOrderStaged;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.common.exception.CustomException;
import com.param.procurement.purchase.consignmentstockpo.dto.ConsignmentStockPODetailDto;
import com.param.procurement.purchase.consignmentstockpo.dto.ConsignmentStockPOHistoryDto;
import com.param.procurement.purchase.consignmentstockpo.dto.ConsignmentStockPOMasterDto;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.procurement.purchase.dao.purchaseOrder.IPurchaseOrderDao;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNUomMapper;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperRequest;
import com.param.procurement.purchase.dto.purchaseOrder.PoPurchaseRequestArray;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderStagedDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomMapperDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomRequest;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailsRespDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDetailsDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderSearchDto;
import com.param.procurement.purchase.purchaseOrder.dto.QrDateDto;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockConversionDao;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockReturnDao;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailResponse;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ConsignmentStockPOCreationServiceImpl implements IConsignmentStockPOCreationService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IGoodReceiptNoteDao grnDao;
	
	@Autowired
	IItemsDao itemsDao;

	@Autowired
	IConsignmentStockReturnDao consignmentStockReturnDao;

	@Autowired
	IConsignmentStockConversionDao consignmentStockConversionDao;
	
	@Autowired
	IPurchaseOrderDao purchaseOrderDao;

	private Integer screenId = Screen.ConsignmentPurchaseOrder.getScreen();
	
	@Override
	@Transactional
	public Object getConsignmentStockPuchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto) {
		PurchaseOrdDto.setToDate((new DateTime(PurchaseOrdDto.getToDate())).plusDays(1).toDate());

		List<PurchaseOrderSearchDto> purchaseOrderSearchDtoList = new ArrayList<PurchaseOrderSearchDto>();

		for (PurchaseOrder purchaseOrder : purchaseOrderDao.searchConsignmentStockPurchaseOrderList(PurchaseOrdDto, screenId)) {

			PurchaseOrderSearchDto purchaseReqSearchDto = new PurchaseOrderSearchDto(purchaseOrder.getId(),
					purchaseOrder.getAddedDate(), purchaseOrder.getPoNumber(), purchaseOrder.getStore().getStoreName(),
					purchaseOrder.getStore().getStoreId(), purchaseOrder.getAssetType().getType(),
					purchaseOrder.getRequestType().getType(), purchaseOrder.getApprovalStatus().getStatus(),
					purchaseOrder.getStatus().getStatus(), purchaseOrder.getAddedBy().getUserName(),
					purchaseOrder.getRemark(), purchaseOrder.getVendor().getName(), purchaseOrder.getAddedDate(),
					purchaseOrder.getRequestingStore().getStoreId(), purchaseOrder.getRequestingStore().getStoreName(),
					purchaseOrder.getIsConsignment() /* added by minesh for consignment (true/false) */
			);

			purchaseOrderSearchDtoList.add(purchaseReqSearchDto);

		}
		return purchaseOrderSearchDtoList;
	}

	
	
	@Override
	@Transactional
	public List<ConsignmentStockReturnDetailSaveRequest> getAllCRGrnDetails(ConsignmentStockReturnRequest consignmentStockReturnRequest) {
		consignmentStockReturnRequest.setToDate((new DateTime(consignmentStockReturnRequest.getToDate())).plusDays(1).toDate());
		List<ConsignmentStockReturnDetailSaveRequest> grnResponseBPS = new ArrayList<ConsignmentStockReturnDetailSaveRequest>();		
		boolean otherChargeAndTaxList= true;

		for (ConsignmentStockReturnDetailSaveRequest grnResponseBP : consignmentStockReturnDao.getAllGoodReceiptNoteCR(consignmentStockReturnRequest)) {
			consignmentStockReturnRequest.setGrnId(grnResponseBP.getId());
			List<ConsignmentStockReturnDetailResponse> grnDetails = consignmentStockReturnDao
					.getGoodReceiptNoteDetailsCR(consignmentStockReturnRequest);

			List<Integer> consumableDetailIds = grnDetails.stream()
					.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
					.map(ConsignmentStockReturnDetailResponse::getGoodReceiptNoteDetailId).collect(Collectors.toList());
			List<Integer> fixedDetaildId = grnDetails.stream()
					.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
					.map(ConsignmentStockReturnDetailResponse::getGoodReceiptNoteDetailId).collect(Collectors.toList());
			List<Integer> consumabelItemIds = grnDetails.stream()
					.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
					.map(ConsignmentStockReturnDetailResponse::getItemId).collect(Collectors.toList());

			List<Long> batchIds = grnDetails.stream().map(ConsignmentStockReturnDetailResponse::getBatchId)
					.collect(Collectors.toList());

			// Required fields for consumable id's
			List<GRNUomMapper> grnUomBP =new ArrayList<>();
			List<UomResponse> uomResponses =new ArrayList<>();
			if(!consumableDetailIds.isEmpty()) {
			     grnUomBP = grnDao.getGRNUomMapperBP(consumableDetailIds);
				uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds,consignmentStockReturnRequest.getOrganizationId());
			
			}
						// These are the common fields
			List<Integer> allDetailIds = new ArrayList<Integer>();
			allDetailIds.addAll(consumableDetailIds);
			allDetailIds.addAll(fixedDetaildId);

			// These are the common fields too. With quantities
			consumabelItemIds
					.addAll(grnDetails.stream().filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
							.map(ConsignmentStockReturnDetailResponse::getItemId).collect(Collectors.toList()));

			for (ConsignmentStockReturnDetailResponse grnDetailResponse : grnDetails) {
				List<ConsignmentStockPOHistoryDto> consignmentStockPOHistoryDto = purchaseOrderDao.getConsignmentStockPOHistory(grnDetailResponse.getGoodReceiptNoteDetailId() , consignmentStockReturnRequest.getUnitId());
				
				Integer poQty=0;
				for (ConsignmentStockPOHistoryDto consignmentStockPOHistoryDto1 : consignmentStockPOHistoryDto) {
					poQty=poQty+consignmentStockPOHistoryDto1.getPoApprovedQuantity();
				}
				grnDetailResponse.setPoApprovedQuantity(poQty);
				if (grnDetailResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
					grnDetailResponse.setGrnUom(grnUomBP.stream()
							.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getGoodReceiptNoteDetailId()))
							.collect(Collectors.toList()));
					grnDetailResponse.setUom(
							uomResponses.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
									.collect(Collectors.toList()));

					List<Batch> batchListOfSameLot = consignmentStockReturnDao.getBatchListFromItem(
							grnDetailResponse.getItemId(), grnDetailResponse.getBatchNo(),
							grnDetailResponse.getLotNumber() , consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId():0);

					List<PurchaseOrderUomMapper> prUomResponseForGrn = grnDetailResponse.getGrnUom().stream()
							.map(x -> new PurchaseOrderUomMapper(new UomType(x.getUomTypeId()),
									new UomUnit(x.getUomUnitId()), x.getConversion(), x.getIpUom(), x.getOpUom(),
									x.getStoreUom()))
							.collect(Collectors.toList());

					int totalAvailableBatchQuantity = 0;
					for (Batch batch : batchListOfSameLot) {
						totalAvailableBatchQuantity += Util.GetLeaseQuantityPurchase(batch.getCurrentQuantity(),
								prUomResponseForGrn, new UomType(grnDetailResponse.getGrnUomTypeId()));
					}

					List<UomResponse> uomListAll = new ArrayList<UomResponse>();
					if (!batchIds.isEmpty()) {
						uomListAll = GlobalDao.getBatchUomListBP(batchIds);
					}
					grnDetailResponse.setAvailableBatchQuantity(totalAvailableBatchQuantity);

					Integer totalConsignmentStock = consignmentStockConversionDao.getTotalConsignmentStockFromBatch(
							grnDetailResponse.getBatchNo(), grnDetailResponse.getLotNo());

				
					
					
					
					grnDetailResponse.setTotalConsignmentStock(totalConsignmentStock);

					//BigInteger conQty = consignmentStockConversionDao.getConsumedQty(grnDetailResponse.getBatchNo());
					//changed by minesh
					//BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(grnDetailResponse.getBatchNo());
					BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(grnDetailResponse.getBatchNo(),grnDetailResponse.getLotNo());
					
					grnDetailResponse.setConsumedQty(conQty);
					//BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(grnDetailResponse.getBatchNo());
					BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(grnDetailResponse.getBatchNo(),grnDetailResponse.getLotNo());
					grnDetailResponse.setConvertedConsignmentStock(convertedConsignmentStock);

					BigInteger sumQty = consignmentStockConversionDao.getBatchReturnQty(grnDetailResponse.getBatchId(),  consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId():0);
					BigInteger storeStockTransfered = consignmentStockConversionDao
							.getStoreStockTransfered(grnDetailResponse.getBatchId(), grnDetailResponse.getStoreId());

					grnDetailResponse.setStoreStockTransfered(storeStockTransfered);
					grnDetailResponse.setReturnedConsignmentStock(sumQty);
					Integer availableConsignmentStock = consignmentStockConversionDao
							.getAvailableConsignmentStock(grnDetailResponse.getBatchId());
					grnDetailResponse.setAvailableConsignmentStock(availableConsignmentStock);

					grnDetailResponse.setAvailableStockforReturn(grnDetailResponse.getAvailableBatchQuantity());
					grnResponseBP.addItems(grnDetailResponse);

				} else if (grnDetailResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					Integer totalConsignmentStock = consignmentStockConversionDao.getTotalConsignmentStockFromBatch(
							grnDetailResponse.getBatchNo(), grnDetailResponse.getLotNo());

				
					
					
					
					grnDetailResponse.setTotalConsignmentStock(totalConsignmentStock);

					//BigInteger conQty = consignmentStockConversionDao.getConsumedQty(grnDetailResponse.getBatchNo());
					//changed by minesh
					//BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(grnDetailResponse.getBatchNo());
					BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(grnDetailResponse.getBatchNo(),grnDetailResponse.getLotNo());
					
					grnDetailResponse.setConsumedQty(conQty);
					//BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(grnDetailResponse.getBatchNo());
					BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(grnDetailResponse.getBatchNo(),grnDetailResponse.getLotNo());
					grnDetailResponse.setConvertedConsignmentStock(convertedConsignmentStock);

					BigInteger sumQty = consignmentStockConversionDao.getBatchReturnQty(grnDetailResponse.getBatchId(),  consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId():0);
					BigInteger storeStockTransfered = consignmentStockConversionDao
							.getStoreStockTransfered(grnDetailResponse.getBatchId(), grnDetailResponse.getStoreId());

					grnDetailResponse.setStoreStockTransfered(storeStockTransfered);
					grnDetailResponse.setReturnedConsignmentStock(sumQty);
					Integer availableConsignmentStock = consignmentStockConversionDao
							.getAvailableConsignmentStock(grnDetailResponse.getBatchId());
					grnDetailResponse.setAvailableConsignmentStock(availableConsignmentStock);

					grnDetailResponse.setAvailableStockforReturn(grnDetailResponse.getAvailableBatchQuantity());
				
					grnResponseBP.addItems(grnDetailResponse);
				}
				
				BigInteger totalPOApproveQuantity = purchaseOrderDao.getTotalPOApprovedQuantityAgainGRNDetail(grnDetailResponse.getGoodReceiptNoteDetailId() ,  consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId():0);
				grnDetailResponse.setApprovedQuantity(totalPOApproveQuantity.intValue());
			}
			
			if(otherChargeAndTaxList) {
			grnResponseBP.setOtherChargesList(GlobalDao.getOtherChargesBP(consignmentStockReturnRequest.getUnitId()));
			grnResponseBP.setTaxList(GlobalDao.getTaxListBP(consignmentStockReturnRequest.getOrganizationId()));
			otherChargeAndTaxList = false;
			}

			grnResponseBPS.add(grnResponseBP);
		}
		return grnResponseBPS;
	}
	
	
	@Override
	@Transactional
	public Object savePurchaseOrder(ConsignmentStockPOMasterDto purchaseOrderDto) throws CustomException {

		/*Integer storeId = purchaseOrderDto.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/
		
		//Integer screenId = Screen.PurchaseOrderCreation.getScreen();
		//String purchaseOrderNo = GlobalService.getBillNoByGeneratedCode(storeNew, unit, screenId);
		String consignmentPONumber = GlobalService.getBillNoByGeneratedCode(purchaseOrderDto.getStoreId(), purchaseOrderDto.getUnitId()!=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId(), screenId);
		
		HashMap<Integer, PurchaseRequestDetail> hm = new HashMap();

		PurchaseOrder purchaseOrder = new PurchaseOrder(new Status(purchaseOrderDto.getApprovalStatusId()),
				new AssetType(purchaseOrderDto.getAssetTypeId()), purchaseOrderDto.getIsConsignment(),
				purchaseOrderDto.getNetAmount(), consignmentPONumber,
				purchaseOrderDto.getRemark() != null ? purchaseOrderDto.getRemark() : null,
				purchaseOrderDto.getRoundOffAmount(), new Status(purchaseOrderDto.getStatusId()),
				new Store(purchaseOrderDto.getStoreId()), new RequestType(purchaseOrderDto.getPoTypeId()),
				purchaseOrderDto.getTotalAmount(), purchaseOrderDto.getTotalDiscount(),
				purchaseOrderDto.getTotalOtherAmount(), purchaseOrderDto.getTotalTaxAmount(),
				new Vendor(purchaseOrderDto.getVendorId()), purchaseOrderDto.getPaymentTerm(),
				new Store(purchaseOrderDto.getRequestingStoreId()));
		

		purchaseOrder.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
		/*if (purchaseOrderDto.getOtherCharges() != null) {
			for (PoOtherChargesMapperRequest poOtherCharges : purchaseOrderDto.getOtherCharges()) {
				if (poOtherCharges.getOtherChargesId() != null) {
					PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper = new PurchaseOrderOtherChargesMapper(
							new OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
					sessionService.setEntityDefaults(purchaseOrderOtherChargesMapper);
					purchaseOrder.addPoOtherChargesMapperList(purchaseOrderOtherChargesMapper);
				}
			}
		}*/
		
		// need to modify for request level
		if (purchaseOrderDto.getOtherCharges() != null) {
			for (PoOtherChargesMapperRequest poOtherCharges : purchaseOrderDto.getOtherCharges()) {
				if (poOtherCharges.getOtherChargesId() != null) {
					PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper = new PurchaseOrderOtherChargesMapper(
							new OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
					purchaseOrderOtherChargesMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
					
					purchaseOrder.addPoOtherChargesMapperList(purchaseOrderOtherChargesMapper);
				}
			}
		}
		if (purchaseOrderDto.getDiscount() != null) {
			purchaseOrder.setDiscount(purchaseOrderDto.getDiscount());
			purchaseOrder.setDiscountType(new DiscountType(purchaseOrderDto.getDiscountTypeId()));
		}
		PurchaseOrderDetail purchaseOrderDetail = null;

		for (ConsignmentStockPODetailDto item : purchaseOrderDto.getItems()) {

			if (purchaseOrderDto.getAssetTypeId().equals(Constants.getConsumableId())) {
				PurchaseOrderDetail purchaseOrderDetail2 = new PurchaseOrderDetail(new Item(item.getItemId()),
						item.getPoQuantity(), item.getBonusQuantity() != null ? item.getBonusQuantity() : null,
						item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : null,
						new Tax(item.getTaxId()), item.getTaxPercentage() != null ? item.getTaxPercentage() : null,
						item.getTotalAmount(), item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
						item.getTaxAmount() != null ? item.getTaxAmount() : null, item.getTotalOtherAmount(),
						item.getNetAmount(), new Manufacturer(item.getManufactureId()),
						item.getSpecification() != null ? item.getSpecification() : null,
						item.getRemark() != null ? item.getRemark() : null, new UomType(item.getPoUomTypeId()!= null ? item.getPoUomTypeId() : 3),
						new UomUnit(item.getPoUomUnitId()!= null ? item.getPoUomUnitId() : 3),
						item.getBonusUomTypeId() != null ? new UomType(item.getBonusUomTypeId()) : null,
						item.getBonusUomUnitId() != null ? new UomUnit(item.getBonusUomUnitId()) : null,
								new Status(item.getStatusId()));
				purchaseOrderDetail2.setIsFromConsignmentPoCreation(item.getIsFromConsignmentPoCreation());
				purchaseOrderDetail2.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity() :0);
				purchaseOrderDetail2.setGrnDetail(new GoodReceiptNoteDetail(item.getGrnDetailId()));
				
				purchaseOrderDetail2.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
				
				purchaseOrderDetail = purchaseOrder.addPurchaseOrderDetailList(purchaseOrderDetail2);

				if (item.getPoUom() != null) {
					for (PurchaseOrderUomRequest poUomMapperDto : item.getPoUom()) {
						PurchaseOrderUomMapper purchaseOrderUomMapper = new PurchaseOrderUomMapper(
								new UomType(poUomMapperDto.getUomTypeId()), new UomUnit(poUomMapperDto.getUomUnitId()),
								poUomMapperDto.getConversion(), poUomMapperDto.getIpUom(), poUomMapperDto.getOpUom(),
								poUomMapperDto.getStoreUom());
						purchaseOrderUomMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
						
						purchaseOrderDetail.addPurchaseOrderUomMapperList(purchaseOrderUomMapper);

					}

				}
				if (item.getBonusUom() != null) {
					for (PurchaseOrderUomRequest bonusUomMapperDto : item.getBonusUom()) {
						BonusUomMapper bonusUomMapper = new BonusUomMapper(
								new UomType(bonusUomMapperDto.getUomTypeId()),
								new UomUnit(bonusUomMapperDto.getUomUnitId()), bonusUomMapperDto.getConversion(),
								bonusUomMapperDto.getIpUom(), bonusUomMapperDto.getOpUom(),
								bonusUomMapperDto.getStoreUom());
						bonusUomMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
						
						purchaseOrderDetail.addBonusUomMapperList(bonusUomMapper);
					}
				}

				/*purchaseOrderDetail.setPoLeaseQuantity(Util.GetLeaseQuantity(item.getPoQuantity(),
						purchaseOrderDetail.getPurchaseOrderUomMapperList(), new UomType(item.getPoUomTypeId())));*/
				purchaseOrderDetail.setPoLeaseQuantity(item.getPoQuantity());
				
				if (item.getBonusQuantity() != null) {
					/*purchaseOrderDetail.setBonusLeaseQuantity(Util.GetLeaseQuantity(item.getBonusQuantity(),
							purchaseOrderDetail.getBonusUomMapperList(), new UomType(item.getBonusUomTypeId())));*/
				}

			} else if (purchaseOrderDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				purchaseOrderDetail = purchaseOrder.addPurchaseOrderDetailList(new PurchaseOrderDetail(
						new Item(item.getItemId()), item.getPoQuantity(), item.getBonusQuantity(), item.getCop(),
						item.getMrp(), item.getDiscount() != null ? item.getDiscount() : null, new Tax(item.getTaxId()),
						item.getTaxPercentage() != null ? item.getTaxPercentage() : null, item.getTotalAmount(),
						item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
						item.getTaxAmount() != null ? item.getTaxAmount() : null, item.getTotalOtherAmount(),
						item.getNetAmount(), new Manufacturer(item.getManufactureId()),
						item.getSpecification() != null ? item.getSpecification() : null,
						item.getRemark() != null ? item.getRemark() : null, new Status(item.getStatusId()),
						item.getWarrentyPeriod(), item.getSupportTerms(), item.getTechnicalSpecification(),
						item.getExpectedPayment(), item.getWarrantyDate(), item.getServiceProviderId()!=null ?  new Vendor(item.getServiceProviderId() ): null));
				purchaseOrderDetail.setIsFromConsignmentPoCreation(item.getIsFromConsignmentPoCreation());
				purchaseOrderDetail.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity() :0);
				purchaseOrderDetail.setGrnDetail(new GoodReceiptNoteDetail(item.getGrnDetailId()));

				if (item.getPoAccessoriesMapper() != null) {
					com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper mapper = item
							.getPoAccessoriesMapper();
					for (Integer itemId : mapper.getItemIds()) {
						PoAccessoriesMapper poAccessoriesMapper = new PoAccessoriesMapper(new Item(itemId));
						poAccessoriesMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
						
						purchaseOrderDetail.addPoAccessoriesMapper(poAccessoriesMapper);
					}
				}
			}

			if (item.getPurchaseOrderStaged() != null) {
				for (PurchaseOrderStagedDto pucrhaseOrderStaged : item.getPurchaseOrderStaged()) {
					com.param.entity.model.procurement.PurchaseOrderStaged purchaseOrderStaged = new com.param.entity.model.procurement.PurchaseOrderStaged(
							pucrhaseOrderStaged.getQuantity(), pucrhaseOrderStaged.getStagedDate());
					purchaseOrderStaged.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
					
					purchaseOrderDetail.addPurchaseOrderStaged(purchaseOrderStaged);
				}
			}
		}
		purchaseOrderDetail.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
		
		purchaseOrderDao.savePurchaseOrder(purchaseOrder);
		CommonResponse response = new CommonResponse();
		response.setId(purchaseOrder.getId().longValue());
		response.setMessage("Purchase Order saved successfully");
		return response;

	}
	
	@Override
	@Transactional
	public Object updatePurchaseOrder(ConsignmentStockPOMasterDto purchaseOrderDto) {

		PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(purchaseOrderDto.getId());
		HashMap<Integer, PurchaseRequestDetail> hm = new HashMap();

		purchaseOrder.updatePurchaseOrder(
				purchaseOrderDto.getDiscountTypeId() != null ? new DiscountType(purchaseOrderDto.getDiscountTypeId())
						: (purchaseOrder.getDiscountType() != null
								? new DiscountType(purchaseOrder.getDiscountType().getId())
								: null),
				purchaseOrderDto.getDiscount() != null ? purchaseOrderDto.getDiscount() : purchaseOrder.getDiscount(),
				purchaseOrderDto.getNetAmount() != null ? purchaseOrderDto.getNetAmount()
						: purchaseOrder.getNetAmount(),
				purchaseOrderDto.getRemark() != null ? purchaseOrderDto.getRemark() : purchaseOrder.getRemark(),
				purchaseOrderDto.getRoundOffAmount(), purchaseOrderDto.getTotalAmount(),
				purchaseOrderDto.getTotalDiscount(), purchaseOrderDto.getTotalOtherAmount(),
				purchaseOrderDto.getTotalTaxAmount(),
				purchaseOrderDto != null ? purchaseOrderDto.getPaymentTerm() : purchaseOrder.getPaymentTerm());

		for (PurchaseOrderOtherChargesMapper poOtherChargesMapper : purchaseOrder.getPoOtherChargesList()) {
			Integer otherChargesId = null;
			for (PoOtherChargesMapperRequest poOtherCharges : purchaseOrderDto.getOtherCharges()) {
				if (poOtherCharges.getId() != null && poOtherCharges.getId() != 0
						&& poOtherCharges.getId().equals(poOtherChargesMapper.getId())) {
					otherChargesId = poOtherChargesMapper.getId();
					poOtherChargesMapper.updatePoOtherChargesMapper(new OtherCharge(poOtherCharges.getOtherChargesId()),
							poOtherCharges.getAmount());
				}
			}
			if (otherChargesId == null) {
				poOtherChargesMapper.setIsDeleted(true);
			}
			poOtherChargesMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
			poOtherChargesMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
			poOtherChargesMapper.setUpdatedByUser(purchaseOrderDto.getUserId());
		}

		for (PoOtherChargesMapperRequest poOtherCharges : purchaseOrderDto.getOtherCharges()) {
			if (poOtherCharges.getId() == null && poOtherCharges.getAmount() != 0) {
				PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper = new PurchaseOrderOtherChargesMapper(
						new OtherCharge(poOtherCharges.getOtherChargesId()), poOtherCharges.getAmount());
				purchaseOrderOtherChargesMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
				
				purchaseOrder.addPoOtherChargesMapperList(purchaseOrderOtherChargesMapper);
			}
		}

		for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
			Integer id = null;

			for (ConsignmentStockPODetailDto item : purchaseOrderDto.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					if (purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId())) {
						detail = updateConsumableTypePODetail(item, detail);
					} else if (purchaseOrder.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						detail = updateFixedTypePODetail(item, detail);
					}

					for (PurchaseOrderStaged purchaseOrderStaged : detail.getPurchaseOrderStagedList()) {
						Integer poStagedId = null;
						for (PurchaseOrderStagedDto purchaseOrderstagedDto : item.getPurchaseOrderStaged()) {
							if (purchaseOrderstagedDto.getId() != null
									&& purchaseOrderstagedDto.getId().equals(purchaseOrderStaged.getId())) {
								poStagedId = purchaseOrderStaged.getId();
								purchaseOrderStaged.updatePurchaseOrderStaged(purchaseOrderstagedDto.getQuantity(),
										purchaseOrderstagedDto.getStagedDate());
							}
						}
						if (poStagedId == null) {
							purchaseOrderStaged.setIsDeleted(true);
						}
						purchaseOrderStaged.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
						purchaseOrderStaged.setUpdatedByUser(purchaseOrderDto.getUserId());
					}

					for (PurchaseOrderStagedDto purchaseOrderstagedDto : item.getPurchaseOrderStaged()) {
						if (purchaseOrderstagedDto.getId() == null) {
							com.param.entity.model.procurement.PurchaseOrderStaged purchaseOrderStaged = new com.param.entity.model.procurement.PurchaseOrderStaged(
									purchaseOrderstagedDto.getQuantity(), purchaseOrderstagedDto.getStagedDate());
							purchaseOrderStaged.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
							
							detail.addPurchaseOrderStaged(purchaseOrderStaged);
						}
					}
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
			detail.setUpdatedByUser(purchaseOrderDto.getUserId());
		}

		for (ConsignmentStockPODetailDto item : purchaseOrderDto.getItems()) {
			if (item.getId() == null) {
				PurchaseOrderDetail purchaseOrderDetail = null;
				if (purchaseOrder.getAssetType().getId().equals(Constants.getConsumableId())) {
					PurchaseOrderDetail purchaseOrderDetail2 = new PurchaseOrderDetail(new Item(item.getItemId()),
							item.getPoQuantity(), item.getBonusQuantity() != null ? item.getBonusQuantity() : null,
							item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : null,
							new Tax(item.getTaxId()), item.getTaxPercentage() != null ? item.getTaxPercentage() : null,
							item.getTotalAmount(), item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
							item.getTaxAmount() != null ? item.getTaxAmount() : null, item.getTotalOtherAmount(),
							item.getNetAmount(), new Manufacturer(item.getManufactureId()),
							item.getSpecification() != null ? item.getSpecification() : null,
							item.getRemark() != null ? item.getRemark() : null, new UomType(item.getPoUomTypeId()),
							new UomUnit(item.getPoUomUnitId()),
							item.getBonusUomTypeId() != null ? new UomType(item.getBonusUomTypeId()) : null,
							item.getBonusUomUnitId() != null ? new UomUnit(item.getBonusUomUnitId()) : null,
							new Status(item.getStatusId()));
					purchaseOrderDetail2.setIsFromConsignmentPoCreation(item.getIsFromConsignmentPoCreation());
					purchaseOrderDetail2.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity() :0);
					purchaseOrderDetail2.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
					
					
					purchaseOrderDetail2.setGrnDetail(new GoodReceiptNoteDetail(item.getGrnDetailId()));
					
					purchaseOrderDetail = purchaseOrder.addPurchaseOrderDetailList(purchaseOrderDetail2);
					if (item.getPoUom() != null) {
						for (PurchaseOrderUomRequest poUomMapperDto : item.getPoUom()) {
							PurchaseOrderUomMapper purchaseOrderUomMapper = new PurchaseOrderUomMapper(
									new UomType(poUomMapperDto.getUomTypeId()),
									new UomUnit(poUomMapperDto.getUomUnitId()), poUomMapperDto.getConversion(),
									poUomMapperDto.getIpUom(), poUomMapperDto.getOpUom(), poUomMapperDto.getStoreUom());
							purchaseOrderUomMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
							
							purchaseOrderDetail.addPurchaseOrderUomMapperList(purchaseOrderUomMapper);
						}
					}
					if (item.getBonusUom() != null) {
						for (PurchaseOrderUomRequest bonusUomMapperDto : item.getBonusUom()) {
							BonusUomMapper bonusUomMapper = new BonusUomMapper(
									new UomType(bonusUomMapperDto.getUomTypeId()),
									new UomUnit(bonusUomMapperDto.getUomUnitId()), bonusUomMapperDto.getConversion(),
									bonusUomMapperDto.getIpUom(), bonusUomMapperDto.getOpUom(),
									bonusUomMapperDto.getStoreUom());
							bonusUomMapper.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
							
							purchaseOrderDetail.addBonusUomMapperList(bonusUomMapper);
						}
					}

					/*purchaseOrderDetail.setPoLeaseQuantity(Util.GetLeaseQuantity(item.getPoQuantity(),
							purchaseOrderDetail.getPurchaseOrderUomMapperList(), new UomType(item.getPoUomTypeId())));
					purchaseOrderDetail.setBonusLeaseQuantity(Util.GetLeaseQuantity(item.getPoQuantity(),
							purchaseOrderDetail.getBonusUomMapperList(), new UomType(item.getBonusUomTypeId())));*/
					purchaseOrderDetail.setPoLeaseQuantity(item.getPoQuantity());
					purchaseOrderDetail.setBonusLeaseQuantity(item.getBonusQuantity());

				} else if (purchaseOrder.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					PurchaseOrderDetail purchaseOrderDetail3 = new PurchaseOrderDetail(new Item(item.getItemId()),
							item.getPoQuantity(), item.getBonusQuantity() != null ? item.getBonusQuantity() : null,
							item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : null,
							new Tax(item.getTaxId()), item.getTaxPercentage() != null ? item.getTaxPercentage() : null,
							item.getTotalAmount(), item.getTotalDiscount() != null ? item.getTotalDiscount() : null,
							item.getTaxAmount() != null ? item.getTaxAmount() : null, item.getTotalOtherAmount(),
							item.getNetAmount(), new Manufacturer(item.getManufactureId()),
							item.getSpecification() != null ? item.getSpecification() : null,
							item.getRemark() != null ? item.getRemark() : null, new Status(item.getStatusId()),
							item.getWarrentyPeriod(), item.getSupportTerms(), item.getTechnicalSpecification(),
							item.getExpectedPayment(), item.getWarrantyDate(), new Vendor(item.getServiceProviderId()));
					purchaseOrderDetail3.setIsFromConsignmentPoCreation(item.getIsFromConsignmentPoCreation());
					purchaseOrderDetail3.setPendingQuantity(item.getPendingQuantity()!=null ? item.getPendingQuantity() :0);
					purchaseOrderDetail3.setGrnDetail(new GoodReceiptNoteDetail(item.getGrnDetailId()));
					
					purchaseOrderDetail3.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
					
					purchaseOrderDetail = purchaseOrder.addPurchaseOrderDetailList(purchaseOrderDetail3);

					if (item.getPoAccessoriesMapper() != null) {
						com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper mapper = item
								.getPoAccessoriesMapper();
						for (Integer itemId : mapper.getItemIds()) {
							purchaseOrderDetail.addPoAccessoriesMapper(new PoAccessoriesMapper(new Item(itemId)));
						}
					}
				}
				
				if (item.getPurchaseOrderStaged() != null) {
					for (PurchaseOrderStagedDto pucrhaseOrderStaged : item.getPurchaseOrderStaged()) {
						com.param.entity.model.procurement.PurchaseOrderStaged purchaseOrderStaged = new com.param.entity.model.procurement.PurchaseOrderStaged(
								pucrhaseOrderStaged.getQuantity(), pucrhaseOrderStaged.getStagedDate());
						purchaseOrderStaged.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
						
						purchaseOrderDetail.addPurchaseOrderStaged(purchaseOrderStaged);
					}
				}
				purchaseOrderDetail.setCreateDefaults(purchaseOrderDto.getOrganizationId()!=null ? purchaseOrderDto.getOrganizationId() : sessionService.getOrganizationId(),purchaseOrderDto.getUnitId() !=null ? purchaseOrderDto.getUnitId() :sessionService.getUnitId() ,purchaseOrderDto.getUserId()!=null ? purchaseOrderDto.getUserId() : sessionService.getUserId());
				
			}
		}
		purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
		purchaseOrder.setUpdatedByUser(purchaseOrderDto.getUserId());
		purchaseOrderDao.updatePurchaseOrder(purchaseOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order updated successfully");
		return response;
	}
	
	@Override
	@Transactional
	public Object getPucrhaseOrder(Integer id,Integer unitId, Integer orgId) {
		PurchaseOrderMasterDetailsDto POMasterDetail = purchaseOrderDao.getPurchaseOrderBP(id);

		//List<PurchaseOrderDetailsRespDto> PurchaseOrderDetailsRespDtoConsumables = purchaseOrderDao.getPoDetailsConsumablesByPoIdBP(id);
		List<PurchaseOrderDetailsRespDto> PurchaseOrderDetailsRespDtoConsumables = purchaseOrderDao.getConsignmentStockPoDetailsConsumablesByPoIdBP(id);

		List<Integer> detailsIds = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
				.map(PurchaseOrderDetailsRespDto::getId).collect(Collectors.toList());

		List<Integer> fixedDetaildId = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(PurchaseOrderDetailsRespDto::getId).collect(Collectors.toList());

		List<PurchaseOrderUomMapperDto> poUomBP = null;
		List<PurchaseOrderUomMapperDto> bonusUomBP = null;
		if (detailsIds.size() > 0) {
			poUomBP = purchaseOrderDao.getPurchaseOrderUomMapperBP(detailsIds);
			bonusUomBP = purchaseOrderDao.getBonusUomMapperBP(detailsIds);
		}
		List<PoOtherChargesMapperDto> otherChargesBP = null;
		otherChargesBP = purchaseOrderDao.getPoOtherChargesMapperBP(id);
		POMasterDetail.setOtherCharges(otherChargesBP);
		List<Integer> consumabelItemIds = PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
				.map(PurchaseOrderDetailsRespDto::getItemId).collect(Collectors.toList());

		detailsIds.addAll(fixedDetaildId);
		List<PoPurchaseRequestArray> purchaseRequestTypeIdBP = null;

		List<PurchaseOrderStagedDto> purchaseOrderStagedDtoBP = null;
		if (detailsIds.size() > 0) {
			purchaseRequestTypeIdBP = purchaseOrderDao.getPurchaseRequestTypeIdBP(detailsIds);

			purchaseOrderStagedDtoBP = purchaseOrderDao.getPoStagedBP(detailsIds);
		}
		List<UomResponse> uomResponses = null;
		if (consumabelItemIds.size() > 0) {
			uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds,orgId);
		}

		List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> accessoriesmapperBP = new ArrayList<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper>();
		if (fixedDetaildId != null && !fixedDetaildId.isEmpty()) {
			accessoriesmapperBP = purchaseOrderDao.getPoAccessoriesMapperBP(fixedDetaildId);
		}

		consumabelItemIds.addAll(PurchaseOrderDetailsRespDtoConsumables.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(PurchaseOrderDetailsRespDto::getItemId).collect(Collectors.toList()));
		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemBP(POMasterDetail.getStoreId(), consumabelItemIds,unitId);
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(POMasterDetail.getStoreId(),
				consumabelItemIds,unitId);
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(POMasterDetail.getStoreId(),
				consumabelItemIds,orgId);
		List<Quantities> leaseQuantities = itemsDao.getLeaseQunatityBP(POMasterDetail.getStoreId(), consumabelItemIds);

		List<Quantities> approvedPRAndPOQuantities = itemsDao
				.getApprovedPRandPOQuantityList(POMasterDetail.getStoreId(), consumabelItemIds,unitId);
		List<Quantities> approvedPOQuantities = itemsDao
				.getApprovedPurchaseOrderQuantityList(POMasterDetail.getStoreId(), consumabelItemIds,unitId);

		for (PurchaseOrderDetailsRespDto purchaseOrderDetailsRespDto : PurchaseOrderDetailsRespDtoConsumables) {
			// purchaseOrderDetailsRespDto.get
			List<ConsignmentStockPOHistoryDto> consignmentStockPOHistoryDto = purchaseOrderDao.getConsignmentStockPOHistory(purchaseOrderDetailsRespDto.getGrnDetailId() ,unitId);
			
			Integer poQty=0;
			for (ConsignmentStockPOHistoryDto consignmentStockPOHistoryDto1 : consignmentStockPOHistoryDto) {
				poQty=poQty+consignmentStockPOHistoryDto1.getPoApprovedQuantity();
			}
			purchaseOrderDetailsRespDto.setPoApprovedQuantity(poQty);
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
				storeStock = storeStock1.get(0).getQuantity();
				purchaseOrderDetailsRespDto.setStockAvailable(storeStock);
			}
			if (stockInTransit1 != null && !stockInTransit1.isEmpty())
				stockInTransit = stockInTransit1.get(0).getQuantity();
			if (maxStoreQuantity1 != null && !maxStoreQuantity1.isEmpty())
				maxStoreQuantity = maxStoreQuantity1.get(0).getIntQuantity();
			if (leaseQuantity != null && !leaseQuantity.isEmpty())
				leaseQty = leaseQuantity.get(0).getQuantity();
			if (approvedPRAndPOQuantities.stream()
					.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPRAndPOQuantities.stream()
							.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
						.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPOQuantities.stream()
							.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPOQuantity = approvedPOQuantities.stream()
						.filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit , approvedPRAndPOQuantity , approvedPOQuantity);
			

			if (purchaseOrderDetailsRespDto.getQrdid() != null) {
				List<QrDateDto> qrDateDto = purchaseOrderDao.getqrDetails(purchaseOrderDetailsRespDto.getQrdid());
				purchaseOrderDetailsRespDto.setQrDateDto(qrDateDto);
			}

			Integer sCstock = consignmentStockConversionDao.getTotalConsignmentStockFromBatch(purchaseOrderDetailsRespDto.getBatchNo(),purchaseOrderDetailsRespDto.getLotNo());
			//BigInteger consumedQty = consignmentStockConversionDao.getConsumedQty(purchaseOrderDetailsRespDto.getBatchNo());
			//changed by minesh
			//BigInteger consumedQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(purchaseOrderDetailsRespDto.getBatchNo());
			BigInteger consumedQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(purchaseOrderDetailsRespDto.getBatchNo(),purchaseOrderDetailsRespDto.getLotNo());
			//BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(purchaseOrderDetailsRespDto.getBatchNo());
			BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(purchaseOrderDetailsRespDto.getBatchNo(),purchaseOrderDetailsRespDto.getLotNo());
			BigInteger storeStockTransfered = consignmentStockConversionDao.getStoreStockTransfered(purchaseOrderDetailsRespDto.getBatchId(), purchaseOrderDetailsRespDto.getStoreId());
			Integer availableConsignmentStock = consignmentStockConversionDao.getAvailableConsignmentStock(purchaseOrderDetailsRespDto.getBatchId());
			Integer returnedQuantity = consignmentStockConversionDao.getBatchReturnQty(purchaseOrderDetailsRespDto.getBatchId(),unitId).intValue();
			purchaseOrderDetailsRespDto.setTotalConsignmentStock(sCstock);
			purchaseOrderDetailsRespDto.setConsumedQty(consumedQty.intValue());
			purchaseOrderDetailsRespDto.setConvertedConsignmentStock(convertedConsignmentStock.intValue());
			purchaseOrderDetailsRespDto.setConvertedConsignmentStock(convertedConsignmentStock.intValue());
			purchaseOrderDetailsRespDto.setAvailableConsignmentStock(availableConsignmentStock.intValue());
			purchaseOrderDetailsRespDto.setReturnedConsignmentStock(returnedQuantity.intValue());
			purchaseOrderDetailsRespDto.setStoreStockTransfered(storeStockTransfered.intValue());
			
			BigInteger totalPOApproveQuantity = purchaseOrderDao.getTotalPOApprovedQuantityAgainGRNDetail(purchaseOrderDetailsRespDto.getGrnDetailId(),unitId);
			purchaseOrderDetailsRespDto.setApprovedQuantity(totalPOApproveQuantity.intValue());
			//purchaseOrderDetailsRespDto.setGoodReceiptNoteDetailId(purchaseOrderDetailsRespDto.getGrnDetailId());
			
			
			if (purchaseOrderDetailsRespDto.getAssetTypeId().equals(Constants.getConsumableId())) {
				purchaseOrderDetailsRespDto.setMaxOrderQuantity(maxOrderQuantity);
				purchaseOrderDetailsRespDto.setMaxStoreQuantity(maxStoreQuantity);
				purchaseOrderDetailsRespDto.setPrId(purchaseRequestTypeId);
				purchaseOrderDetailsRespDto.setPurchaseOrderStagedDto(purchaseOrderStagedDto);
				purchaseOrderDetailsRespDto.setPrQuantity(prQunatity);
				purchaseOrderDetailsRespDto.setPoUom(
						poUomBP.stream().filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
								.collect(Collectors.toList()));

				List<PurchaseOrderUomMapperDto> bonusUomBP1 = bonusUomBP.stream()
						.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
						.collect(Collectors.toList());

				purchaseOrderDetailsRespDto.setBonusUom(bonusUomBP1);
				purchaseOrderDetailsRespDto.setUom(
						uomResponses.stream().filter(x -> x.getItemId().equals(purchaseOrderDetailsRespDto.getItemId()))
								.collect(Collectors.toList()));

				POMasterDetail.addItems(purchaseOrderDetailsRespDto);
			} else if (purchaseOrderDetailsRespDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				purchaseOrderDetailsRespDto.setMaxOrderQuantity(maxOrderQuantity);
				purchaseOrderDetailsRespDto.setMaxStoreQuantity(maxStoreQuantity);
				purchaseOrderDetailsRespDto.setPrId(purchaseRequestTypeId);
				purchaseOrderDetailsRespDto.setPrQuantity(prQunatity);
				purchaseOrderDetailsRespDto.setPurchaseOrderStagedDto(purchaseOrderStagedDto);
				purchaseOrderDetailsRespDto.setPoAccessoriesMapper(accessoriesmapperBP.stream()
						.filter(x -> x.getPoDetailId().equals(purchaseOrderDetailsRespDto.getId()))
						.collect(Collectors.toList()));

				POMasterDetail.addItems(purchaseOrderDetailsRespDto);
			}

		}

		POMasterDetail.setOtherChargesList(GlobalDao.getOtherChargesBP(unitId));
		POMasterDetail.setTaxList(GlobalDao.getTaxListBP(orgId));
		return POMasterDetail;
	}

	
	private PurchaseOrderDetail updateConsumableTypePODetail(ConsignmentStockPODetailDto item, PurchaseOrderDetail detail) {
		
		detail.updatePurchaseOrderDetail(new Item(item.getItemId()), item.getPoQuantity(), item.getBonusQuantity(),
				item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : detail.getDiscount(),
				item.getTaxPercentage(), item.getTotalAmount(), item.getTotalDiscount(),
				item.getTaxAmount() != null ? item.getTaxAmount() : detail.getTaxAmount(), item.getTotalOtherAmount(),
				item.getNetAmount(),
				new Manufacturer(
						item.getManufactureId() != null ? item.getManufactureId() : detail.getManufacturer().getId()),
				item.getSpecification() != null ? item.getSpecification() : detail.getSpecification(),
				item.getRemark() != null ? item.getRemark() : detail.getRemark(), new UomType(item.getPoUomTypeId()),
				new UomUnit(item.getPoUomUnitId()),
				item.getBonusUomTypeId() != null ? new UomType(item.getBonusUomTypeId()) : null,
				item.getBonusUomUnitId() != null ? new UomUnit(item.getBonusUomUnitId()) : null,
				item.getPoQuantity() != null 
				? item.getPoQuantity() /*Util.GetLeaseQuantity(item.getPoQuantity(), detail.getPurchaseOrderUomMapperList(), new UomType(detail.getPoUomType().getId()))*/
				: detail.getPoLeaseQuantity(),
				item.getBonusQuantity() != null 
				? item.getBonusQuantity() /*Util.GetLeaseQuantity(item.getBonusQuantity(), detail.getBonusUomMapperList(), new UomType(item.getBonusUomTypeId()))*/
				: detail.getBonusLeaseQuantity(), item.getPoApprovedQuantity() != null ? item.getPoApprovedQuantity() : 0);
		
		detail.setGrnDetail(new GoodReceiptNoteDetail(item.getGrnDetailId()));

		for (PurchaseOrderUomMapper poUomMapper : detail.getPurchaseOrderUomMapperList()) {
			for (PurchaseOrderUomRequest poUomMapperDto : item.getPoUom()) {
				if (poUomMapperDto.getId() != null && poUomMapperDto.getId().equals(poUomMapper.getId())) {
					poUomMapper.updatePurchaseOrderUomMapper(new UomType(poUomMapperDto.getUomTypeId()),
							new UomUnit(poUomMapperDto.getUomUnitId()), poUomMapperDto.getConversion(),
							poUomMapperDto.getIpUom(), poUomMapperDto.getOpUom(), poUomMapperDto.getStoreUom());
				}
			}
		}
		for (BonusUomMapper bonusMapper : detail.getBonusUomMapperList()) {
			for (PurchaseOrderUomRequest bonusMapperDto : item.getBonusUom()) {
				if (bonusMapperDto.getId() != null && bonusMapperDto.getId().equals(bonusMapper.getId())) {
					bonusMapper.updateBonusUomMapper(
							bonusMapperDto.getUomTypeId() != null ? new UomType(bonusMapperDto.getUomTypeId()) : null,
							bonusMapperDto.getUomUnitId() != null ? new UomUnit(bonusMapperDto.getUomUnitId()) : null,
							bonusMapperDto.getConversion() != null ? bonusMapperDto.getConversion() : null,
							bonusMapperDto.getIpUom() != null ? bonusMapperDto.getIpUom() : null,
							bonusMapperDto.getOpUom() != null ? bonusMapperDto.getOpUom() : null,
							bonusMapperDto.getStoreUom() != null ? bonusMapperDto.getStoreUom() : null);
				}
			}
		}

		return detail;
	}

	private PurchaseOrderDetail updateFixedTypePODetail(ConsignmentStockPODetailDto item, PurchaseOrderDetail detail) {
		detail.updatePurchaseOrderDetail(new Item(item.getItemId()), item.getPoQuantity(), item.getBonusQuantity(),
				item.getCop(), item.getMrp(), item.getDiscount() != null ? item.getDiscount() : detail.getDiscount(),
				item.getTaxPercentage(), item.getTotalAmount(), item.getTotalDiscount(),
				item.getTaxAmount() != null ? item.getTaxAmount() : detail.getTaxAmount(), item.getTotalOtherAmount(),
				item.getNetAmount(),
				new Manufacturer( item.getManufactureId() != null ? item.getManufactureId() : detail.getManufacturer().getId()),
				item.getSpecification() != null ? item.getSpecification() : detail.getSpecification(),
				item.getRemark() != null ? item.getRemark() : detail.getRemark(),
				item.getWarrentyPeriod() != null ? item.getWarrentyPeriod() : detail.getwarrantyPeriod(),
				item.getSupportTerms() != null ? item.getSupportTerms() : detail.getSupportTerms(),
				item.getTechnicalSpecification() != null ? item.getTechnicalSpecification()
						: detail.getTechnicalSpecification(),
				item.getExpectedPayment() != null ? item.getExpectedPayment() : detail.getExpectedPayment(),
				item.getWarrantyDate() != null ? item.getWarrantyDate() : detail.getwarrantyDate(),
						item.getServiceProviderId() != null ? new Vendor(item.getServiceProviderId()):detail.getServiceProvider(), 
								item.getPoApprovedQuantity() != null ? item.getPoApprovedQuantity() : 0);
		
		detail.setGrnDetail(new GoodReceiptNoteDetail(item.getGrnDetailId()));

		return detail;
	}


	@Override
	@Transactional
	public List<ConsignmentStockPOHistoryDto> getConsignmentPOHistoryByGRNDetailId(Integer grnDetailId , Integer unitId) {
		
		/*List<ConsignmentStockPOHistoryDto> consignmentStockPOHistoryDto = new ArrayList<ConsignmentStockPOHistoryDto>();
		for (PurchaseOrderDetail poDetail : purchaseOrderDao.getConsignmentStockPOHistory(grnDetailId)) {
			ConsignmentStockPOHistoryDto poHistory = new ConsignmentStockPOHistoryDto(
					poDetail.getPurchaseOrder().getPoNumber(),
					poDetail.getPurchaseOrder().getAddedDate(),
					poDetail.getPoApprovedQuantity(),
					poDetail.getCop(),
					poDetail.getMrp()  );
			consignmentStockPOHistoryDto.add(poHistory);
		}*/
		List<ConsignmentStockPOHistoryDto> consignmentStockPOHistoryDto = purchaseOrderDao.getConsignmentStockPOHistory(grnDetailId , unitId);
		return consignmentStockPOHistoryDto;
	}

}
