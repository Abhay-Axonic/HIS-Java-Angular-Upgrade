package com.param.procurement.quotation.service.physicalInspection;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
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
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ConsignmentStockReturn;
import com.param.entity.model.procurement.ConsignmentStockReturnDetail;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.common.ServiceException;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNUomMapper;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockConversionDao;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockReturnDao;
import com.param.procurement.quotation.dao.physicalInspection.IPhysicalInspectionDao;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailHistory;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailResponse;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailSaveRequest;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnItemDto;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnRequest;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnResponse;
import com.param.procurement.quotation.dto.physicalInspection.DetailsConsignmentStockConversionSaveRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ConsignmentStockReturnService implements IConsignmentStockReturnService {

	final static Logger logger = Logger.getLogger(ConsignmentStockReturnService.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	IItemsDao itemsDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGoodReceiptNoteDao grnDao;

	@Autowired
	IPhysicalInspectionDao PhysicalInspectionDao;

	@Autowired
	IConsignmentStockReturnDao consignmentStockReturnDao;

	@Autowired
	IConsignmentStockConversionDao consignmentStockConversionDao;

	private Integer screenId = Screen.ConsignmentReturn.getScreen();

	@Override
	@Transactional
	public List<ConsignmentStockReturnResponse> getConsignmenStocktList(
			ConsignmentStockReturnRequest consignmentStockReturnRequest) {
		List<ConsignmentStockReturnResponse> consignmentStockReturnResponseList = new ArrayList<ConsignmentStockReturnResponse>();
		consignmentStockReturnRequest
				.setToDate((new DateTime(consignmentStockReturnRequest.getToDate())).plusDays(1).toDate());
		try {
			Integer screenId = Screen.GoodReceiptNote.getScreen();
			consignmentStockReturnResponseList = consignmentStockReturnDao
					.getConsignmentStockListResponse(consignmentStockReturnRequest, screenId);
			/*
			 * for(PhysicalInspection physicalInspections :
			 * consignmentStockReturnDao.getConsignmentStockList(
			 * consignmentStockReturnRequest, screenId)) { ConsignmentStockReturnResponse
			 * consignmentStockReturnResponse = new
			 * ConsignmentStockReturnResponse(physicalInspections.getId(),
			 * physicalInspections.getGoodReceiptNote().getGrnNumber(),
			 * physicalInspections.getGoodReceiptNote().getStore().getStoreName(),
			 * physicalInspections.getGoodReceiptNote().getAssetType().getType(),
			 * physicalInspections.getGoodReceiptNote().getVendor().getName(),
			 * physicalInspections.getStatus().getStatus(),
			 * physicalInspections.getStatus().getId(),
			 * physicalInspections.getGoodReceiptNote().getStore().getStoreId(),
			 * physicalInspections.getGoodReceiptNote().getIsConsignment());
			 * consignmentStockReturnResponseList.add(consignmentStockReturnResponse); }
			 */
			return consignmentStockReturnResponseList;
		} catch (Exception e) {
			logger.error("getConsignmenStocktList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public List<ConsignmentStockReturnDetailSaveRequest> getAllCRGrnDetails(
			ConsignmentStockReturnRequest consignmentStockReturnRequest) {

		consignmentStockReturnRequest
				.setToDate((new DateTime(consignmentStockReturnRequest.getToDate())).plusDays(1).toDate());
		List<ConsignmentStockReturnDetailSaveRequest> grnResponseBPS = new ArrayList<ConsignmentStockReturnDetailSaveRequest>();

		for (ConsignmentStockReturnDetailSaveRequest grnResponseBP : consignmentStockReturnDao
				.getAllGoodReceiptNoteCR(consignmentStockReturnRequest)) {
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
				// List<UomResponse> batchUomBP = GlobalDao.getBatchUomListBP(batchIds);

			// These are the common fields
			List<Integer> allDetailIds = new ArrayList<Integer>();
			allDetailIds.addAll(consumableDetailIds);
			allDetailIds.addAll(fixedDetaildId);

			// These are the common fields too. With quantities
			consumabelItemIds
					.addAll(grnDetails.stream().filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
							.map(ConsignmentStockReturnDetailResponse::getItemId).collect(Collectors.toList()));

			for (ConsignmentStockReturnDetailResponse grnDetailResponse : grnDetails) {

				if (grnDetailResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
					grnDetailResponse.setGrnUom(grnUomBP.stream()
							.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getGoodReceiptNoteDetailId()))
							.collect(Collectors.toList()));
					grnDetailResponse.setUom(
							uomResponses.stream().filter(x -> x.getItemId().equals(grnDetailResponse.getItemId()))
									.collect(Collectors.toList()));

					List<Batch> batchListOfSameLot = consignmentStockReturnDao.getBatchListFromItem(
							grnDetailResponse.getItemId(), grnDetailResponse.getBatchNo(),
							grnDetailResponse.getLotNumber(), consignmentStockReturnRequest.getUnitId()!=null? consignmentStockReturnRequest.getUnitId():0);

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

					// set sum of avalibleStok of consignmetBatch and it's convertedBatch for return
					// as per discuss DivyeshSir.
					grnDetailResponse.setAvailableBatchQuantity(totalAvailableBatchQuantity);

					// Integer
					// totalConsignmentStock=consignmentStockConversionDao.getTotalConsignmentStock(grnDetailResponse.getGoodReceiptNoteDetailId());
					Integer totalConsignmentStock = consignmentStockConversionDao.getTotalConsignmentStockFromBatch(
							grnDetailResponse.getBatchNo(), grnDetailResponse.getLotNo());

					grnDetailResponse.setTotalConsignmentStock(totalConsignmentStock);
					// grnDetailResponse.setConsumedQty(grnDetailResponse.getTotalConsignmentStock()
					// - grnDetailResponse.getAvailableBatchQuantity());
					// Batch
					// bth=consignmentStockConversionDao.getBatch(grnDetailResponse.getBatchId());

					//BigInteger conQty = consignmentStockConversionDao.getConsumedQty(grnDetailResponse.getBatchNo());
					//changed by minesh
					//BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(grnDetailResponse.getBatchNo());
					BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(grnDetailResponse.getBatchNo(),grnDetailResponse.getLotNo());
					
					grnDetailResponse.setConsumedQty(conQty);
					//BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(grnDetailResponse.getBatchNo());
					BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(grnDetailResponse.getBatchNo(),grnDetailResponse.getLotNo());
					grnDetailResponse.setConvertedConsignmentStock(convertedConsignmentStock);

					/*
					 * if (bth!=null) { grnDetailResponse.setConsumedQty(bth.getQuantity()-
					 * bth.getCurrentQuantity()); grnDetailResponse.setConvertedConsignmentStock(
					 * bth.getCurrentQuantity()); } else { grnDetailResponse.setConsumedQty(0);
					 * grnDetailResponse.setConvertedConsignmentStock( 0); }
					 */

					BigInteger sumQty = consignmentStockConversionDao.getBatchReturnQty(grnDetailResponse.getBatchId(), consignmentStockReturnRequest.getUnitId()!=null? consignmentStockReturnRequest.getUnitId():0);
					BigInteger storeStockTransfered = consignmentStockConversionDao
							.getStoreStockTransfered(grnDetailResponse.getBatchId(), grnDetailResponse.getStoreId());

					grnDetailResponse.setStoreStockTransfered(storeStockTransfered);
					grnDetailResponse.setReturnedConsignmentStock(sumQty);
					Integer availableConsignmentStock = consignmentStockConversionDao
							.getAvailableConsignmentStock(grnDetailResponse.getBatchId());
					grnDetailResponse.setAvailableConsignmentStock(availableConsignmentStock);

					grnDetailResponse.setAvailableStockforReturn(grnDetailResponse.getAvailableBatchQuantity());
					
					BigInteger totalPOCreatedQuantity = consignmentStockReturnDao.getTotalPOCreatedQuantity(grnDetailResponse.getGoodReceiptNoteDetailId());
					grnDetailResponse.setTotalPOCreatedQuantity(totalPOCreatedQuantity);
					Integer getconsignmentReturnedQuantity=consignmentStockReturnDao.getconsignmentReturnedQuantity(grnDetailResponse);
					grnDetailResponse.setConsignmentReturnedQuantity(getconsignmentReturnedQuantity!=null ? getconsignmentReturnedQuantity : 0);
					if((grnDetailResponse.getAvailableConsignmentStock()-(getconsignmentReturnedQuantity!=null ? getconsignmentReturnedQuantity :0))!=0 && (grnDetailResponse.getAvailableConsignmentStock()-(getconsignmentReturnedQuantity!=null ? getconsignmentReturnedQuantity :0))>0)
					{
						grnResponseBP.addItems(grnDetailResponse);
					}
				} else if (grnDetailResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					
					BigInteger totalPOCreatedQuantity = consignmentStockReturnDao.getTotalPOCreatedQuantity(grnDetailResponse.getGoodReceiptNoteDetailId());
					grnDetailResponse.setTotalPOCreatedQuantity(totalPOCreatedQuantity);
					Integer totalConsignmentStock = consignmentStockConversionDao.getTotalConsignmentStockFromBatch(
							grnDetailResponse.getBatchNo(), grnDetailResponse.getLotNo());

					grnDetailResponse.setTotalConsignmentStock(totalConsignmentStock);
					// grnDetailResponse.setConsumedQty(grnDetailResponse.getTotalConsignmentStock()
					// - grnDetailResponse.getAvailableBatchQuantity());
					// Batch
					// bth=consignmentStockConversionDao.getBatch(grnDetailResponse.getBatchId());

					//BigInteger conQty = consignmentStockConversionDao.getConsumedQty(grnDetailResponse.getBatchNo());
					//changed by minesh
					//BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(grnDetailResponse.getBatchNo());
					BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(grnDetailResponse.getBatchNo(),grnDetailResponse.getLotNo());
					
					grnDetailResponse.setConsumedQty(conQty);
					//BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(grnDetailResponse.getBatchNo());
					BigInteger convertedConsignmentStock = consignmentStockConversionDao.getconvertedConsignmentStock(grnDetailResponse.getBatchNo(),grnDetailResponse.getLotNo());
					grnDetailResponse.setConvertedConsignmentStock(convertedConsignmentStock);
					
					BigInteger sumQty = consignmentStockConversionDao.getBatchReturnQty(grnDetailResponse.getBatchId(), consignmentStockReturnRequest.getUnitId()!=null? consignmentStockReturnRequest.getUnitId():0);
					
					BigInteger storeStockTransfered=BigInteger.ZERO;
					if(grnDetailResponse.getBatchNo()!=null)
					{
						 storeStockTransfered = consignmentStockConversionDao.getStoreStockTransfered(grnDetailResponse.getBatchId(), grnDetailResponse.getStoreId());	
					}
					else
					{
						 storeStockTransfered = consignmentStockConversionDao.getStoreStockTransferedByLotNo(grnDetailResponse.getLotNo(), grnDetailResponse.getStoreId());
						
					}
					
					//BigInteger storeStockTransfered = consignmentStockConversionDao .getStoreStockTransfered(grnDetailResponse.getBatchNo(), grnDetailResponse.getStoreId());

					grnDetailResponse.setStoreStockTransfered(storeStockTransfered);
					grnDetailResponse.setReturnedConsignmentStock(sumQty);
					Integer getconsignmentReturnedQuantity=consignmentStockReturnDao.getconsignmentReturnedQuantity(grnDetailResponse);
					grnDetailResponse.setConsignmentReturnedQuantity(getconsignmentReturnedQuantity!=null ? getconsignmentReturnedQuantity :0);
					if((grnDetailResponse.getAvailableConsignmentStock()-(getconsignmentReturnedQuantity!=null ? getconsignmentReturnedQuantity :0))!=0 && (grnDetailResponse.getAvailableConsignmentStock()-(getconsignmentReturnedQuantity!=null ? getconsignmentReturnedQuantity :0))>0)
					{
						grnResponseBP.addItems(grnDetailResponse);
					}
				}
			}
			grnResponseBP.setOtherChargesList(GlobalDao.getOtherChargesBP(consignmentStockReturnRequest.getUnitId()));
			if(grnResponseBP.getItems()!=null)
			{
				grnResponseBPS.add(grnResponseBP);
			}
		}

		return grnResponseBPS;
	}

	@Override
	@Transactional
	public ConsignmentStockReturnDetailSaveRequest getCRGrnDetails(
			ConsignmentStockReturnRequest consignmentStockReturnRequest) {

		consignmentStockReturnRequest
				.setToDate((new DateTime(consignmentStockReturnRequest.getToDate())).plusDays(1).toDate());
		ConsignmentStockReturnDetailSaveRequest grnResponseBP = consignmentStockReturnDao
				.getGoodReceiptNoteCR(consignmentStockReturnRequest);

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
		List<GRNUomMapper> grnUomBP = grnDao.getGRNUomMapperBP(consumableDetailIds);
		List<UomResponse> uomResponses = GlobalDao.getUnitOfMeasurementListByItemBP(consumabelItemIds,consignmentStockReturnRequest.getOrganizationId());
		// List<UomResponse> batchUomBP = GlobalDao.getBatchUomListBP(batchIds);

		// These are the common fields
		List<Integer> allDetailIds = new ArrayList<Integer>();
		allDetailIds.addAll(consumableDetailIds);
		allDetailIds.addAll(fixedDetaildId);

		// These are the common fields too. With quantities
		consumabelItemIds.addAll(grnDetails.stream().filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(ConsignmentStockReturnDetailResponse::getItemId).collect(Collectors.toList()));

		for (ConsignmentStockReturnDetailResponse grnDetailResponse : grnDetails) {

			if (grnDetailResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
				grnDetailResponse.setGrnUom(grnUomBP.stream()
						.filter(x -> x.getGrnDetailId().equals(grnDetailResponse.getGoodReceiptNoteDetailId()))
						.collect(Collectors.toList()));
				grnDetailResponse.setUom(uomResponses.stream()
						.filter(x -> x.getItemId().equals(grnDetailResponse.getItemId())).collect(Collectors.toList()));

				// grnDetailResponse.setBatchUomBP(batchUomBP.stream().filter(x ->
				// x.getBatchId().equals(grnDetailResponse.getBatchId())).collect(Collectors.toList()));

				List<Batch> batchListOfSameLot = consignmentStockReturnDao.getBatchListFromItem(
						grnDetailResponse.getItemId(), grnDetailResponse.getBatchNo(),
						grnDetailResponse.getLotNumber(), consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId():0);

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

				// set sum of avalibleStok of consignmetBatch and it's convertedBatch for return
				// as per discuss DivyeshSir.
				grnDetailResponse.setAvailableBatchQuantity(totalAvailableBatchQuantity);

				grnResponseBP.addItems(grnDetailResponse);

			} else if (grnDetailResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				grnResponseBP.addItems(grnDetailResponse);
			}
		}
		grnResponseBP.setOtherChargesList(GlobalDao.getOtherChargesBP(consignmentStockReturnRequest.getUnitId()));
		return grnResponseBP;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public CommonResponse
	 * saveConsignmentStockReturnDetails(ConsignmentStockReturnDetailSaveRequest
	 * consignmentStockReturnSaveRequest) {
	 * 
	 * CommonResponse response = new CommonResponse(); try { Integer screenId =
	 * Screen.ConsignmentReturn.getScreen(); Integer storeId =
	 * sessionService.getStoreId(); Integer unitId = sessionService.getUnitId();
	 * 
	 * String consignmentStockReturnNumber =
	 * GlobalDao.getPrefixRunningNumber(unitId, screenId, storeId);
	 * 
	 * ConsignmentStockReturn consignmentStockReturn = new ConsignmentStockReturn(
	 * new Status(consignmentStockReturnSaveRequest.getApprovalStatusId()), new
	 * AssetType(consignmentStockReturnSaveRequest.getAssetTypeId()),
	 * consignmentStockReturnNumber,
	 * consignmentStockReturnSaveRequest.getIsConsignment(), new
	 * Status(consignmentStockReturnSaveRequest.getStatusId()), new
	 * Store(consignmentStockReturnSaveRequest.getStoreId()), new
	 * Vendor(consignmentStockReturnSaveRequest.getVendorId()), new
	 * GoodReceiptNote(consignmentStockReturnSaveRequest.getGrnId()),
	 * consignmentStockReturnSaveRequest.getTotalAmount(),
	 * consignmentStockReturnSaveRequest.getTotalTaxAmount(),
	 * consignmentStockReturnSaveRequest.getTotalNetAmount());
	 * 
	 * for (ConsignmentStockReturnDetailResponse item :
	 * consignmentStockReturnSaveRequest.getItems()) { ConsignmentStockReturnDetail
	 * csrDetail = null; ConsignmentStockReturnDetail consignmentStockReturnDetail =
	 * new ConsignmentStockReturnDetail( new Batch(item.getBatchId()),
	 * item.getBatchNo(), item.getTotalAmount(), item.getReturnBatchQuantity(),
	 * item.getCop(), item.getExpiryDate(), new UomType(item.getGrnUomTypeId()), new
	 * UomUnit(item.getGrnUomUnitId()), new Item(item.getItemId()),
	 * item.getLotNumber(), item.getMrp(), item.getRemark(), new
	 * Status(item.getStatusId()), new Manufacturer(item.getManufacturerId()), new
	 * GoodReceiptNoteDetail(item.getGoodReceiptNoteDetailId()));
	 * 
	 * sessionService.setEntityDefaults(consignmentStockReturnDetail); csrDetail =
	 * consignmentStockReturn.addConsignmentStockReturnDetailList(
	 * consignmentStockReturnDetail);
	 * 
	 * List<GrnUomMapper> grnUomMappersList =
	 * grnDao.getGRNUomMapper(item.getGoodReceiptNoteDetailId());
	 * csrDetail.setReturnQuantity(Util.GetLeaseQuantity(item.getReturnBatchQuantity
	 * (), grnUomMappersList, item.getGrnUomTypeId()));
	 * 
	 * sessionService.setEntityDefaults(csrDetail);
	 * 
	 * Batch batch = GlobalDao.getBatch(item.getBatchId()); if
	 * (csrDetail.getReturnQuantity() > batch.getCurrentQuantity()) { throw new
	 * CustomException("Return Quantity Must Be Less Than Current Quantity"); } }
	 * 
	 * sessionService.setEntityDefaults(consignmentStockReturn);
	 * consignmentStockReturnDao.saveConsignmentStockReturn(consignmentStockReturn);
	 * response.setId(consignmentStockReturn.getId().longValue());
	 * response.setMessage("Consignment Stock Return Detail saved successfully"); }
	 * catch (Exception e) { 
	 * response.setMessage("Error while saving Consignment Stock Return Detail"); }
	 * return response; }
	 */

	@Override
	@Transactional
	public CommonResponse saveConsignmentStockReturnDetails(
			List<ConsignmentStockReturnDetailSaveRequest> consignmentStockReturnSaveRequests) {

		CommonResponse response = new CommonResponse();
		try {
			Integer screenId = Screen.ConsignmentReturn.getScreen();
			// Integer storeId = sessionService.getStoreId();
			Integer unitId = sessionService.getUnitId();

			for (ConsignmentStockReturnDetailSaveRequest consignmentStockReturnSaveRequest : consignmentStockReturnSaveRequests) {

				String consignmentStockReturnNumber = GlobalDao.getPrefixRunningNumber(unitId, screenId,
						consignmentStockReturnSaveRequest.getStoreId());

				ConsignmentStockReturn consignmentStockReturn = new ConsignmentStockReturn(
						new Status(consignmentStockReturnSaveRequest.getApprovalStatusId()),
						new AssetType(consignmentStockReturnSaveRequest.getAssetTypeId()), consignmentStockReturnNumber,
						consignmentStockReturnSaveRequest.getIsConsignment(),
						new Status(consignmentStockReturnSaveRequest.getStatusId()),
						new Store(consignmentStockReturnSaveRequest.getStoreId()),
						new Vendor(consignmentStockReturnSaveRequest.getVendorId()),
						new GoodReceiptNote(consignmentStockReturnSaveRequest.getGrnId()),
						consignmentStockReturnSaveRequest.getTotalAmount(),
						consignmentStockReturnSaveRequest.getTotalTaxAmount(),
						consignmentStockReturnSaveRequest.getTotalNetAmount());
				consignmentStockReturn.setRemark(consignmentStockReturnSaveRequest.getRemark()!=null ? consignmentStockReturnSaveRequest.getRemark() :null);
				for (ConsignmentStockReturnDetailResponse item : consignmentStockReturnSaveRequest.getItems()) {
					ConsignmentStockReturnDetail csrDetail = null;
					ConsignmentStockReturnDetail consignmentStockReturnDetail = new ConsignmentStockReturnDetail(
							new Batch(item.getBatchId()), item.getBatchNo(), item.getTotalAmount(),
							item.getReturnBatchQuantity(), item.getCop(), item.getExpiryDate(),
							item.getGrnUomTypeId()!=null ? new UomType(item.getGrnUomTypeId()) :null , item.getGrnUomUnitId()!=null ? new UomUnit(item.getGrnUomUnitId()) :null,
							new Item(item.getItemId()), item.getLotNumber(), item.getMrp(), item.getRemark(),
							new Status(item.getStatusId()), new Manufacturer(item.getManufacturerId()),
							new GoodReceiptNoteDetail(item.getGoodReceiptNoteDetailId()));

					consignmentStockReturnDetail.setCreateDefaults(consignmentStockReturnSaveRequest.getOrganizationId()!=null ? consignmentStockReturnSaveRequest.getOrganizationId() : sessionService.getOrganizationId(),consignmentStockReturnSaveRequest.getUnitId() !=null ? consignmentStockReturnSaveRequest.getUnitId() :sessionService.getUnitId() ,consignmentStockReturnSaveRequest.getUserId()!=null ? consignmentStockReturnSaveRequest.getUserId() : sessionService.getUserId());
					csrDetail = consignmentStockReturn
							.addConsignmentStockReturnDetailList(consignmentStockReturnDetail);

					//List<GrnUomMapper> grnUomMappersList = grnDao.getGRNUomMapper(item.getGoodReceiptNoteDetailId());
					// csrDetail.setReturnQuantity(Util.GetLeaseQuantity(item.getReturnBatchQuantity(),
					// grnUomMappersList,item.getGrnUomTypeId()));
					csrDetail.setReturnQuantity(item.getReturnBatchQuantity());

					csrDetail.setCreateDefaults(consignmentStockReturnSaveRequest.getOrganizationId()!=null ? consignmentStockReturnSaveRequest.getOrganizationId() : sessionService.getOrganizationId(),consignmentStockReturnSaveRequest.getUnitId() !=null ? consignmentStockReturnSaveRequest.getUnitId() :sessionService.getUnitId() ,consignmentStockReturnSaveRequest.getUserId()!=null ? consignmentStockReturnSaveRequest.getUserId() : sessionService.getUserId());
					

					//Batch batch = GlobalDao.getBatch(item.getBatchId());
					/*
					 * if (csrDetail.getReturnQuantity() > batch.getCurrentQuantity()) { throw new
					 * CustomException("Return Quantity Must Be Less Than Current Quantity"); }
					 */
					/*List<Batch> bList = consignmentStockReturnDao.getBatchList(item.getBatchNo(), item.getLotNumber());
					Integer returnQtyFromUi = item.getReturnQuantity();
					for (Batch b : bList) {
						Integer returnQty = b.getCurrentQuantity();

						// if(b.getCurrentQuantity() >= item.getReturnQuantity())
						if (returnQtyFromUi <= b.getCurrentQuantity()) {
						//if (b.getIsConsignment() == true) {
							
							consignmentStockReturnDao.updateConsignmentTrueBatchData(b.getId(), returnQtyFromUi);
							consignmentStockReturnDao.updateStoreStock(batch.getBatchNo(), batch.getLotNo(),
									returnQtyFromUi);
							 returnQtyFromUi =returnQtyFromUi-b.getCurrentQuantity() ;
							// consignmentStockReturnDao.updateStoreStock();
							break;
						} else if (returnQtyFromUi > b.getCurrentQuantity()) {
						//else if (b.getIsConsignment() == false) {
						
							consignmentStockReturnDao.updateConsignmentFalseBatchData(b.getId(),
									b.getCurrentQuantity());
							// consignmentStockReturnDao.updateStoreStock(consignmentStockReturnDetail.getBatch().getBatchNo(),
							// consignmentStockReturnDetail.getBatch().getLotNo() , returnQtyFromUi);
							consignmentStockReturnDao.updateConsignmentFalseStoreStock(batch.getBatchNo(),
									batch.getLotNo(), returnQtyFromUi);
							returnQtyFromUi = returnQtyFromUi - returnQty;
						}

					}*/

				}

				consignmentStockReturn.setCreateDefaults(consignmentStockReturnSaveRequest.getOrganizationId()!=null ? consignmentStockReturnSaveRequest.getOrganizationId() : sessionService.getOrganizationId(),consignmentStockReturnSaveRequest.getUnitId() !=null ? consignmentStockReturnSaveRequest.getUnitId() :sessionService.getUnitId() ,consignmentStockReturnSaveRequest.getUserId()!=null ? consignmentStockReturnSaveRequest.getUserId() : sessionService.getUserId());
				
				consignmentStockReturnDao.saveConsignmentStockReturn(consignmentStockReturn);

				response.setId(consignmentStockReturn.getId().longValue());
			}

			response.setMessage("Consignment Stock Return Detail saved successfully");
		} catch (Exception e) {
			logger.error("saveConsignmentStockReturnDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	@Override
	@Transactional
	public ConsignmentStockReturnDetailResponse getConsignmentStockreturnDetail(
			ConsignmentStockReturnRequest consignmentStockReturnRequest) {

		ConsignmentStockReturnDetailResponse consignmentStockReturnDetailResponse = new ConsignmentStockReturnDetailResponse();

		ConsignmentStockReturn consignmentStockReturn = consignmentStockReturnDao
				.getConsignmentStockReturn(consignmentStockReturnRequest.getId(), consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId(): 0);

		List<ConsignmentStockReturnItemDto> items = new ArrayList<ConsignmentStockReturnItemDto>();
		ConsignmentStockReturnItemDto item = new ConsignmentStockReturnItemDto();
		for (ConsignmentStockReturnDetail consignmentStockReturnDetail : consignmentStockReturnDao
				.getConsignmentStockReturnDetails(consignmentStockReturn.getId(),consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId(): 0)) {
			item = new ConsignmentStockReturnItemDto(consignmentStockReturnDetail.getId(),
					consignmentStockReturnDetail.getItem().getId(), consignmentStockReturnDetail.getItem().getCode(),
					consignmentStockReturnDetail.getItem().getName(), consignmentStockReturnDetail.getBatch().getId(),
					consignmentStockReturnDetail.getBatchNo(), consignmentStockReturnDetail.getLotNumber(),
					consignmentStockReturnDetail.getCop(),
					consignmentStockReturnDetail.getGoodReceiptNoteDetail().getMrp(),
					consignmentStockReturnDetail.getReturnQuantity(),
					consignmentStockReturnDetail.getGoodReceiptNoteDetail().getRemark(),
					consignmentStockReturnDetail.getExpiryDate(),
					consignmentStockReturnDetail.getManufacturer().getName());

			item.setGrnDetailId(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getId());
			item.setTotalReturnedQuantity(consignmentStockConversionDao.getTotalConsignmentReturnQtyAgainGRNDetailId(
					consignmentStockReturnDetail.getGoodReceiptNoteDetail().getId(),consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId(): 0).intValue());
			item.setStatusId(consignmentStockReturnDetail.getStatus().getId());
			item.setStatus(consignmentStockReturnDetail.getStatus().getStatus());

			List<Batch> batchList = consignmentStockReturnDao.getBatchListFromItem(item.getItemId(), item.getBatchNo(),
					item.getLotNo(), consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId() : 0);
			int totalConsignmentStock = 0;
			int availableConsignmentStock = 0;
			int returnedQuantity = 0;
			int convertedConsignmentStock = 0;
			int returnQuantity = 0;
			int availableConvertedStock = 0;
			Integer totalConsignmentStock1 = 0;

			List<Long> batchIds = batchList.stream().map(x -> x.getId()).collect(Collectors.toList());
			List<UomResponse> uomListAll = new ArrayList<UomResponse>();
			if (!batchIds.isEmpty()) {
				uomListAll = GlobalDao.getBatchUomListBP(batchIds);
			}
			for (Batch batch : batchList) {
				List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getId()))
						.collect(Collectors.toList());
				if (uomList != null && !uomList.isEmpty()) {
					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						Integer conversion = Util.getIpUomConversion(uomList);
						// totalConsignmentStock += batch.getQuantity() / conversion;
						// totalConsignmentStock += batch.getQuantity() / conversion;
						totalConsignmentStock1 = consignmentStockConversionDao.getTotalConsignmentStock(
								consignmentStockReturnDetail.getGoodReceiptNoteDetail().getId());

						totalConsignmentStock += totalConsignmentStock1 / conversion;
						returnedQuantity += (batch.getReturnedQuantity() != null
								? batch.getReturnedQuantity() / conversion
								: 0);
						if (!batch.getIsConsignment()) {
							convertedConsignmentStock += batch.getQuantity() / conversion;
							availableConvertedStock += batch.getCurrentQuantity() / conversion;

							// for returnConsignment consider normal stock as consignment.
							availableConsignmentStock += batch.getCurrentQuantity() / conversion;
						} else {
							availableConsignmentStock += batch.getCurrentQuantity() / conversion;
						}
						returnQuantity = (returnQuantity == 0) ? item.getReturnQuantity() / conversion : returnQuantity;
					}

				}
				// item.setTotalConsignmentStock(totalConsignmentStock);
				// item.setReturnedQuantity(returnQuantity);
			}
			// if (batchList.size() == 0) {
			// item.setTotalConsignmentStock(consignmentStockConversionDao.getTotalConsignmentStock(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getId()));
			// item.setReturnedQuantity(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatch().getReturnedQuantity());
			// }
			Integer sCstock = consignmentStockConversionDao.getTotalConsignmentStockFromBatch(
					consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatchNo(),
					consignmentStockReturnDetail.getGoodReceiptNoteDetail().getLotNumber());
			item.setTotalConsignmentStock(sCstock);
			
			item.setReturnedQuantity(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatch().getReturnedQuantity());
		//	item.setReturnedQuantity(consignmentStockReturnDetail.getConsignmentReturnQty(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatchNo(), consignmentStockReturnDetail.getGoodReceiptNoteDetail().getLotNumber(), consignmentStockReturnDetail.getGoodReceiptNoteDetail().getId()));
			
			/*BigInteger storeStockTransfered = consignmentStockConversionDao.getStoreStockTransfered(
					consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatch().getBatchNo(),
					consignmentStockReturnDetail.getGoodReceiptNoteDetail().getGoodReceiptNote().getStore()
							.getStoreId());*/
			
			
			
			BigInteger storeStockTransfered=BigInteger.ZERO;
			if(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatch().getBatchNo()!=null)
			{
				 storeStockTransfered = consignmentStockConversionDao.getStoreStockTransfered(
							consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatch().getId(),
							consignmentStockReturnDetail.getGoodReceiptNoteDetail().getGoodReceiptNote().getStore()
									.getStoreId());	
			}
			else
			{
				 storeStockTransfered = consignmentStockConversionDao.getStoreStockTransferedByLotNo(item.getLotNo(),
							consignmentStockReturnDetail.getGoodReceiptNoteDetail().getGoodReceiptNote().getStore()
									.getStoreId());
				
			}
			
			
			
			item.setStoreStockTransfered(storeStockTransfered);
			// Integer
			// totalConsignmentStock1=consignmentStockConversionDao.getTotalConsignmentStock(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getId());

			//BigInteger conQty = consignmentStockConversionDao.getConsumedQty(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatchNo());
			//changed by minesh
			//BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatchNo());
			BigInteger conQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatchNo(),consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatch().getLotNo());
			
			item.setConsumedQty(conQty);
			//BigInteger convertedConsignmentStock1 = consignmentStockConversionDao.getconvertedConsignmentStock(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatchNo());
			BigInteger convertedConsignmentStock1 = consignmentStockConversionDao
					.getconvertedConsignmentStock(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatchNo(),
							consignmentStockReturnDetail.getGoodReceiptNoteDetail().getBatch().getLotNo());
			
			// grnDetailResponse.setConvertedConsignmentStock(convertedConsignmentStock);

			// BigInteger
			// sumQty=consignmentStockConversionDao.getBatchReturnQty(grnDetailResponse.getBatchId());
			// BigInteger storeStockTransfered
			// =consignmentStockConversionDao.getStoreStockTransfered(grnDetailResponse.getBatchNo(),
			// grnDetailResponse.getStoreId());

			item.setStoreStockTransfered(storeStockTransfered);

			// item.setTotalConsignmentStock(consignmentStockReturnDetail.get);
			item.setAvailableConsignmentStock(availableConsignmentStock);
			// item.setReturnedQuantity(returnedQuantity);
			item.setReceivedQuantity(consignmentStockReturnDetail.getReturnQuantity());
			item.setConvertedConsignmentStock(convertedConsignmentStock1);
			// item.setReturnQuantity(returnQuantity);

			item.setAvailableConvertedStock(availableConvertedStock);
			item.setBatch(batchList);
			
			BigInteger totalPOCreatedQuantity = consignmentStockReturnDao.getTotalPOCreatedQuantity(consignmentStockReturnDetail.getGoodReceiptNoteDetail().getId());
			item.setTotalPOCreatedQuantity(totalPOCreatedQuantity);

			items.add(item);
		}

		consignmentStockReturnDetailResponse = new ConsignmentStockReturnDetailResponse(consignmentStockReturn.getId(),
				consignmentStockReturn.getCrNumber(),
				consignmentStockReturn.getGoodReceiptNote().getStore().getStoreName(),
				consignmentStockReturn.getGoodReceiptNote().getAssetType().getType(),
				consignmentStockReturn.getVendor().getName(), items,
				consignmentStockReturn.getGoodReceiptNote().getStore().getStoreId(),
				consignmentStockReturn.getStatus().getId(),consignmentStockReturn.getStatus().getStatus());
		consignmentStockReturnDetailResponse.setRemark(consignmentStockReturn.getRemark()!=null ? consignmentStockReturn.getRemark() :null);
		return consignmentStockReturnDetailResponse;
	}

	@Override
	@Transactional
	public CommonResponse updateConsignmentStockReturnDetails(
			List<ConsignmentStockReturnDetailResponse> consignmentStockReturnDetailResponse) throws CustomException {

		CommonResponse response = new CommonResponse();
		try {
			for (ConsignmentStockReturnDetailResponse item : consignmentStockReturnDetailResponse) {

				ConsignmentStockReturnDetail consignmentStockReturnDetail = consignmentStockReturnDao
						.getConsignmentStockReturnDetail(item.getId(), item.getUnitId()!=null ? item.getUnitId() :0);
				/*System.out.println(consignmentStockReturnDetail.getId() + " "
						+ consignmentStockReturnDetail.getReturnQuantity() + " to " + item.getReturnBatchQuantity());*/

				List<UomResponse> uomList = GlobalDao.getBatchUomList(consignmentStockReturnDetail.getBatch().getId());

				Integer UomTypeId = null;
				if (uomList != null && !uomList.isEmpty()) {
					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						UomTypeId = ipUom.getUomTypeId();
					}
				}

				List<BatchUomMapper> batchUomList = uomList.stream()
						.map(x -> new BatchUomMapper(x.getConversion(), x.getIpUom(), x.getOpUom(), x.getStoreUom(),
								new UomType(x.getUomTypeId()), new UomUnit(x.getUomUnitId())))
						.collect(Collectors.toList());

				Integer leaseQuantity = Util.GetLeaseQuantity(item.getReturnBatchQuantity(), batchUomList, UomTypeId);

				List<Batch> batchList = consignmentStockReturnDao.getSameBatchList(
						consignmentStockReturnDetail.getBatch().getItem().getId(),
						consignmentStockReturnDetail.getBatch().getBatchNo(),
						consignmentStockReturnDetail.getBatch().getLotNo() ,item.getUnitId()!=null ? item.getUnitId() :0);
				Integer totalLeaseBatchCurrentQuantity = batchList.stream().mapToInt(x -> x.getCurrentQuantity()).sum();
				consignmentStockReturnDetail.setReturnQuantity(
						consignmentStockReturnDetail.getReturnQuantity() + item.getReturnBatchQuantity());
				// if (leaseQuantity <= totalLeaseBatchCurrentQuantity) {
				// consignmentStockReturnDetail.setReturnQuantity(leaseQuantity);
				consignmentStockReturnDetail.setUpdatedByUser(item.getUserId()!=null ? item.getUserId() :sessionService.getUserId());
				consignmentStockReturnDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentStockReturnDetail.getUnitId()));
				// consignmentStockReturnDao.updateBatchData(consignmentStockReturnDetail.getBatch().getId(),
				// item.getReturnBatchQuantity());

				/*List<Batch> bList = consignmentStockReturnDao.getBatchList(
						consignmentStockReturnDetail.getBatch().getBatchNo(),
						consignmentStockReturnDetail.getBatch().getLotNo());

				Integer returnQtyFromUi = leaseQuantity;
				for (Batch b : bList) {
										 * Integer returnQty = b.getCurrentQuantity();
										 * 
										 * // if(b.getCurrentQuantity() >= item.getReturnQuantity()) if (returnQtyFromUi
										 * <= b.getCurrentQuantity()) { // returnQtyFromUi
										 * =returnQtyFromUi-b.getCurrentQuantity() ;
										 * consignmentStockReturnDao.updateBatchData(b.getId(), returnQtyFromUi);
										 * consignmentStockReturnDao.updateStoreStock(consignmentStockReturnDetail.
										 * getBatch().getBatchNo(), consignmentStockReturnDetail.getBatch().getLotNo() ,
										 * returnQtyFromUi); break; } else if (returnQtyFromUi > b.getCurrentQuantity())
										 * { returnQtyFromUi = returnQtyFromUi - returnQty;
										 * consignmentStockReturnDao.updateBatchData(b.getId(), b.getCurrentQuantity());
										 * consignmentStockReturnDao.updateStoreStock(consignmentStockReturnDetail.
										 * getBatch().getBatchNo(), consignmentStockReturnDetail.getBatch().getLotNo() ,
										 * returnQtyFromUi); }
										 * 
										 

					Integer returnQty = b.getCurrentQuantity();

					// if(b.getCurrentQuantity() >= item.getReturnQuantity())
					if (returnQtyFromUi <= b.getCurrentQuantity()) {
					//if (b.getIsConsignment() == true) {
						
						consignmentStockReturnDao.updateConsignmentTrueBatchData(b.getId(), returnQtyFromUi);
						consignmentStockReturnDao.updateStoreStock(consignmentStockReturnDetail.getBatch().getBatchNo(),
								consignmentStockReturnDetail.getBatch().getLotNo(), returnQtyFromUi);
						// consignmentStockReturnDao.updateStoreStock();
						 returnQtyFromUi =returnQtyFromUi-b.getCurrentQuantity() ;
						break;
					} else if (returnQtyFromUi > b.getCurrentQuantity()) {
					//else if (b.getIsConsignment() == false) {
						
						consignmentStockReturnDao.updateConsignmentFalseBatchData(b.getId(), b.getCurrentQuantity());
						// consignmentStockReturnDao.updateStoreStock(consignmentStockReturnDetail.getBatch().getBatchNo(),
						// consignmentStockReturnDetail.getBatch().getLotNo() , returnQtyFromUi);
						consignmentStockReturnDao.updateConsignmentFalseStoreStock(
								consignmentStockReturnDetail.getBatch().getBatchNo(),
								consignmentStockReturnDetail.getBatch().getLotNo(), returnQtyFromUi);
						returnQtyFromUi = returnQtyFromUi - returnQty;
					}

				}*/

				// } else {
				// throw new CustomException("Return quantity must be less than current
				// quantity");
				// }

			}
			response.setMessage("Consignment Stock Return Detail updated successfully");
		} /*
			 * catch (CustomException e) {  throw new
			 * CustomException(e.getMessage()); }
			 */ catch (Exception e) {
			
			throw new CustomException("Error While Updating Consignment Stock Return Detail");
		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse saveConsignmentStockReturn(List<ConsignmentStockReturnRequest> consignmentStockReturnRequests)
			throws CustomException {

		CommonResponse response = new CommonResponse();

		try {

			Integer UomTypeId = null;
			Long batchId = null;

			for (ConsignmentStockReturnRequest consignmentStockReturnRequest : consignmentStockReturnRequests) {

				ConsignmentStockReturn consignmentStockReturn = consignmentStockReturnDao.getConsignmentStockReturn(consignmentStockReturnRequest.getId(), consignmentStockReturnRequest.getUnitId()!=null ?consignmentStockReturnRequest.getUnitId() :0 );

				for (ConsignmentStockReturnDetail consignmentStockReturnDetail : consignmentStockReturnDao.getConsignmentStockReturnDetails(consignmentStockReturn.getId(),consignmentStockReturnRequest.getUnitId()!=null ?consignmentStockReturnRequest.getUnitId() :0)) {

					List<UomResponse> uomList = GlobalDao.getBatchUomList(consignmentStockReturnDetail.getBatch().getId());

					if (consignmentStockReturn.getAssetType().getId().equals(Constants.getConsumableId())) {
						if (uomList != null && !uomList.isEmpty()) {
							UomResponse ipUom = Util.getIpUom(uomList);
							if (ipUom != null) {
								UomTypeId = ipUom.getUomTypeId();
							}
						}
					}

					batchId = saveStoreStockDetails(consignmentStockReturn.getStore().getStoreId(),
							consignmentStockReturnDetail.getBatch().getId(),
							consignmentStockReturnDetail.getReturnQuantity(), new UomType(UomTypeId),consignmentStockReturnDetail);
					
						consignmentStockReturnDetail.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
						consignmentStockReturnDetail.setApprovedDate(TimeZoneComponent.getDateByzone(consignmentStockReturnDetail.getUnitId()));
						consignmentStockReturnDetail.setApprovedBy(new User(consignmentStockReturnRequest.getUserId()!=null ? consignmentStockReturnRequest.getUserId() : sessionService.getUserId()));
						consignmentStockReturnDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentStockReturnDetail.getUnitId()));
						consignmentStockReturnDetail.setUpdatedByUser(consignmentStockReturnRequest.getUserId()!=null ? consignmentStockReturnRequest.getUserId() : sessionService.getUserId());

						consignmentStockReturnDao.updateconsignmentStockReturnDetail(consignmentStockReturnDetail);					
				}
					consignmentStockReturn.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
					consignmentStockReturn.setApprovedDate(TimeZoneComponent.getDateByzone(consignmentStockReturn.getUnitId()));
					consignmentStockReturn.setApprovedBy(new User(consignmentStockReturnRequest.getUserId()!=null ? consignmentStockReturnRequest.getUserId() : sessionService.getUserId()));
					consignmentStockReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentStockReturn.getUnitId()));
					consignmentStockReturn.setUpdatedByUser(consignmentStockReturnRequest.getUserId()!=null ? consignmentStockReturnRequest.getUserId() : sessionService.getUserId());
					consignmentStockReturnDao.updateconsignmentStockReturn(consignmentStockReturn);
			}
		} catch (CustomException e) {
			
			throw new CustomException(e.getMessage());
		} catch (Exception e) {
			
			throw new CustomException("Error while Consignment Stock Return");
		}

		response.setMessage("Consignment Stock Return successfully");
		return response;
	}

	private Long saveStoreStockDetails(Integer storeId, Long batchId, Integer returnQuantity, UomType uomType, ConsignmentStockReturnDetail consignmentStockReturnDetail)
			throws CustomException {
		if (returnQuantity > 0) {
			try {
				Batch batch = GlobalDao.getBatch(batchId);

				if (batch != null) {
					
					 Integer leaseQuantity = Util.GetLeaseQuantity(returnQuantity,batch.getBatchUomList(), uomType);
					// already return quantity is featch from directly db so it is leaseQuantity.
					//Integer leaseQuantity = returnQuantity;

					//List<Batch> batchList = consignmentStockReturnDao.getSameBatchList(batch.getItem().getId(), batch.getBatchNo(), batch.getLotNo());
					//Integer totalBatchCurrentQuantity = batchList.stream().mapToInt(x -> x.getCurrentQuantity()).sum();

					List<Batch> bList = consignmentStockReturnDao.getBatchList(batch.getBatchNo(), batch.getLotNo());
					//Integer returnQtyFromUi = totalBatchCurrentQuantity;
					Integer returnQtyFromUi = returnQuantity;
					for (Batch b : bList) {
						//Integer returnQty = b.getCurrentQuantity();
						Integer batchCurrentQuantity = b.getCurrentQuantity();

						// if(b.getCurrentQuantity() >= item.getReturnQuantity())
						if (returnQtyFromUi <= b.getCurrentQuantity()) {
							// returnQtyFromUi =returnQtyFromUi-b.getCurrentQuantity() ;
							//consignmentStockReturnDao.updateBatchData(b.getId(), returnQtyFromUi);
							//consignmentStockReturnDao.updateStoreStock(batch.getBatchNo(), batch.getLotNo(), returnQtyFromUi);
							if(b.getIsConsignment()) { //changed by minesh
								consignmentStockReturnDao.updateConsignmentTrueBatchData(b.getId(), returnQtyFromUi);
								//consignmentStockReturnDao.updateStoreStock(batch.getBatchNo(), batch.getLotNo(), returnQtyFromUi);
								consignmentStockReturnDao.updateStoreStock(batch.getBatchNo(), batch.getLotNo(), returnQtyFromUi,b.getId(),storeId);
							}else {
								consignmentStockReturnDao.updateConsignmentFalseBatchData(b.getId(),returnQtyFromUi);
								//consignmentStockReturnDao.updateConsignmentFalseStoreStock(batch.getBatchNo(), batch.getLotNo(), returnQtyFromUi);
								consignmentStockReturnDao.updateConsignmentFalseStoreStock(batch.getBatchNo(), batch.getLotNo(), returnQtyFromUi,b.getId(),storeId);
							}
							break;
						} else if (returnQtyFromUi > b.getCurrentQuantity()) {
							//returnQtyFromUi = returnQtyFromUi - returnQty;
							//consignmentStockReturnDao.updateBatchData(b.getId(), b.getCurrentQuantity());
							//consignmentStockReturnDao.updateStoreStock(batch.getBatchNo(), batch.getLotNo(),returnQtyFromUi);
							//returnQtyFromUi = returnQtyFromUi - returnQty;
							
							//consignmentStockReturnDao.updateBatchData(b.getId(), batchCurrentQuantity);
							//consignmentStockReturnDao.updateStoreStock(batch.getBatchNo(), batch.getLotNo(),batchCurrentQuantity);
							if(b.getIsConsignment()) {//changed by minesh
								consignmentStockReturnDao.updateConsignmentTrueBatchData(b.getId(), batchCurrentQuantity);
								//consignmentStockReturnDao.updateStoreStock(batch.getBatchNo(), batch.getLotNo(), batchCurrentQuantity);
								consignmentStockReturnDao.updateStoreStock(batch.getBatchNo(), batch.getLotNo(), batchCurrentQuantity,b.getId(),storeId);
							}else {
								consignmentStockReturnDao.updateConsignmentFalseBatchData(b.getId(),batchCurrentQuantity);
								//consignmentStockReturnDao.updateConsignmentFalseStoreStock(batch.getBatchNo(), batch.getLotNo(), batchCurrentQuantity);
								consignmentStockReturnDao.updateConsignmentFalseStoreStock(batch.getBatchNo(), batch.getLotNo(), batchCurrentQuantity,b.getId(),storeId);
							}
							returnQtyFromUi = returnQtyFromUi - batchCurrentQuantity;
						}

						//Batch consignmentBatch = consignmentStockReturnDao.getConsignmentBatch(batch.getItem().getId(), batch.getBatchNo(), batch.getLotNo());
						//updateGRNConversionStatus(consignmentBatch);						
					}

					saveStockTransaction(leaseQuantity,batch,returnQuantity,consignmentStockReturnDetail);
					/*
					 * if (totalBatchCurrentQuantity >= leaseQuantity) { for (Batch batchObj :
					 * batchList) { if (leaseQuantity != 0) { if (batchObj.getCurrentQuantity() >=
					 * leaseQuantity) { setLeaseQuantity(batchObj, leaseQuantity); leaseQuantity =
					 * 0; return batchId; } else { int batchCurrentQuantity =
					 * batchObj.getCurrentQuantity(); setLeaseQuantity(batchObj,
					 * batchCurrentQuantity); leaseQuantity = leaseQuantity - batchCurrentQuantity;
					 * } } } } else { throw new
					 * CustomException("Return quantity must be less than current quantity"); }
					 */
					/*
					 * if(batch.getCurrentQuantity() >= leaseQuantity) { setLeaseQuantity(batch,
					 * leaseQuantity); return batch.getId(); }else { throw new
					 * CustomException("return quantity must be less than current quantity"); }
					 */
				}

			} catch (Exception e) {
				
				throw new CustomException(e.getMessage());
			} 
		}
		return batchId;
	}

	private void saveStockTransaction(Integer leaseQuantity, Batch batch, Integer returnQuantity,
			ConsignmentStockReturnDetail consignmentStockReturnDetail) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(129);
		StockTransaction stockTransaction = new StockTransaction(batch.getCurrentQuantity()-returnQuantity,
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				batch.getRate(), new Store(sessionService.getStoreId()),
				batch,0);
		stockTransaction.setTransactionDetialsId(consignmentStockReturnDetail.getId());
		stockTransaction.setTransactionId(consignmentStockReturnDetail.getConsignmentStockReturn().getId());
		//stockTransaction.setItem(new Item(storeStock.getBatch().getItem().getId()));
		stockTransaction.setIsTransit(false);
		stockTransaction.setOutQty(leaseQuantity);
		
		stockTransaction.setCreateDefaults(consignmentStockReturnDetail.getOrganizationId(),consignmentStockReturnDetail.getUnitId(),
				consignmentStockReturnDetail.getAddedByUser());
		GlobalDao.saveStockTransaction(stockTransaction);
		
	}

	/*private void setLeaseQuantity(Batch batchObj, Integer leaseQuantity) {
		Integer tQuantity;
		Integer availableQuantity;
		Integer currentQuantity;
		Integer returnedQuantity;

		Batch batch = new Batch();
		batch = GlobalDao.getBatch(batchObj.getId());

		StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batch.getId(), sessionService.getStoreId(),sessionService.getUnitId());
		StoreStock storeStockDetail = null;
		if (storeStockList == null) {
			StoreStock storeStock = new StoreStock(leaseQuantity, new Store(sessionService.getStoreId()),
					new Batch(batch.getId()));
			sessionService.setEntityDefaults(storeStock);
			storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
		} else {
			tQuantity = leaseQuantity + storeStockList.gettQty();
			availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
			storeStockList.setAvailableQty(availableQuantity);
			storeStockList.settQty(tQuantity);
			storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
		}

		currentQuantity = batch.getCurrentQuantity() - leaseQuantity;
		returnedQuantity = (batch.getReturnedQuantity() != null) ? batch.getReturnedQuantity() + leaseQuantity
				: leaseQuantity;

		batch.setCurrentQuantity(currentQuantity);
		batch.setReturnedQuantity(returnedQuantity);

		GlobalDao.updateBatchLeaseQuantity(batch);

		saveStockTransaction(leaseQuantity, storeStockDetail);
	}*/

	/*private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()));
		sessionService.setEntityDefaults(stockTransaction);
		GlobalDao.saveStockTransaction(stockTransaction);
	}*/
	
	private void updateGRNConversionStatus(Batch batch) {
		GoodReceiptNote goodReceiptNote = batch.getGoodReceiptNoteDetail().get(0).getGoodReceiptNote();
		List<Batch> batchList = GlobalDao.getBatchListForConversion(goodReceiptNote.getId(),null);
		int totalCurrentQuantity = 0;
		for (Batch batchObj : batchList) {
			totalCurrentQuantity += batchObj.getCurrentQuantity();
		}
		if(totalCurrentQuantity <= 0) {
			goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		}else {
			goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
		}		
			grnDao.updateGoodReceiptNote(goodReceiptNote);
	}

	@Override
	@Transactional
	public List<ConsignmentStockReturnDetailHistory> getConsignmentStockreturnDetailHistory(
			ConsignmentStockReturnDetailHistory consignmentStockReturnDetailHistory) {

		List<ConsignmentStockReturnDetailHistory> consignmentStockReturnDetailHistories = consignmentStockReturnDao
				.getConsignmentStockreturnDetailHistory(consignmentStockReturnDetailHistory.getGrnDetailId(),
						consignmentStockReturnDetailHistory.getCrId() , consignmentStockReturnDetailHistory.getUnitId()!=null ? consignmentStockReturnDetailHistory.getUnitId() : 0);
		return consignmentStockReturnDetailHistories;
	}
	

}