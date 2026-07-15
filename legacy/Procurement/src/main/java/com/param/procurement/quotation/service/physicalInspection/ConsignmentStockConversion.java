package com.param.procurement.quotation.service.physicalInspection;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GrnBonusUomMapper;
import com.param.entity.model.procurement.GrnUomMapper;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PhysicalInspectionDetail;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.AllDataResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.procurement.common.exception.CustomException;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockConversionDao;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockReturnDao;
import com.param.procurement.quotation.dao.physicalInspection.IPhysicalInspectionDao;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversionDetails;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversionItem;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversionItemSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.DetailsConsignmentStockConversionSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.GrnBonusUomMapperResponse;
import com.param.procurement.quotation.dto.physicalInspection.GrnUomMapperResponse;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionResponse;
import com.param.procurement.quotation.dto.physicalInspection.RackResponse;
import com.param.procurement.quotation.dto.physicalInspection.RejectReasonResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ConsignmentStockConversion implements IConsignmentStockConversionService {

	final static Logger logger = Logger.getLogger(ConsignmentStockConversion.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	IItemsDao ItemsDao;

	@Autowired
	IGoodReceiptNoteDao grnDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IPhysicalInspectionDao PhysicalInspectionDao;

	@Autowired
	IConsignmentStockConversionDao consignmentStockConversionDao;
	@Autowired
	IConsignmentStockReturnDao consignmentStockReturnDao;
	private Integer screenId = Screen.ConsignmentConversion.getScreen();

	@Override
	@Transactional
	public List<PhysicalInspectionResponse> getPhysicalnspectionList(
			PhysicalInspectionRequest physicalInspectionRequest) {
		Integer screenId = Screen.PhysicalInspection.getScreen();
		List<PhysicalInspectionResponse> physicalInspectionList = new ArrayList<PhysicalInspectionResponse>();
		physicalInspectionRequest.setToDate((new DateTime(physicalInspectionRequest.getToDate())).plusDays(1).toDate());
		for (PhysicalInspection physicalInspections : consignmentStockConversionDao
				.getPhysicalnspectionList(physicalInspectionRequest, screenId)) {
			PhysicalInspectionResponse physicalInspection = new PhysicalInspectionResponse(physicalInspections.getId(),
					physicalInspections.getAddedDate(), physicalInspections.getGoodReceiptNote().getGrnNumber(),
					physicalInspections.getGoodReceiptNote().getStore().getStoreName(),
					physicalInspections.getGoodReceiptNote().getAssetType().getType(),
					physicalInspections.getGoodReceiptNote().getInvoiceNumber(),
					physicalInspections.getGoodReceiptNote().getInvoiceDate(),
					physicalInspections.getGoodReceiptNote().getDcNumber(),
					physicalInspections.getGoodReceiptNote().getDcDate(),
					physicalInspections.getGoodReceiptNote().getVendor().getName(),
					physicalInspections.getAddedBy().getUserName(),
					physicalInspections.getGoodReceiptNote().getConversionStatus().getStatus(),
					physicalInspections.getGoodReceiptNote().getConversionStatus().getId(),
					//physicalInspections.getGoodReceiptNote().getGoodReceiptNoteDetailsList().get(0).getBatch().getConversionStatus().getStatus(),
					//physicalInspections.getGoodReceiptNote().getGoodReceiptNoteDetailsList().get(0).getBatch().getConversionStatus().getId(),
					physicalInspections.getGoodReceiptNote().getStore().getStoreId(),
					physicalInspections.getGoodReceiptNote().getIsConsignment());
			physicalInspectionList.add(physicalInspection);
		}

		return physicalInspectionList;
	}

	/* added by Minesh */
	@Override
	@Transactional
	public ConsignmentStockConversionDetails getConsignmentStockConversionDetails(
			PhysicalInspectionRequest physicalInspectionRequest) {
		ConsignmentStockConversionDetails consignmentStockConversionDetails = new ConsignmentStockConversionDetails();
		PhysicalInspection physicalInspection = consignmentStockConversionDao
				.getPhysicalnspectionDetailsForSearch(physicalInspectionRequest);

		List<ConsignmentStockConversionItem> items = new ArrayList<ConsignmentStockConversionItem>();

		List<Batch> batchLists = new ArrayList<Batch>();

		ConsignmentStockConversionItem item = new ConsignmentStockConversionItem();
		for (PhysicalInspectionDetail physicalInspectionDetail : consignmentStockConversionDao.getPhysicalInspectionDetailByInspectionId(physicalInspection.getId(), physicalInspectionRequest.getUnitId()!=null ? physicalInspectionRequest.getUnitId():0)) {
			if (physicalInspectionDetail.getPhysicalInspection().getGoodReceiptNote().getAssetType().getId()
					.equals(Constants.getConsumableId())) {
				List<GrnUomMapperResponse> grnUomMapperResponse = new ArrayList<GrnUomMapperResponse>();
				for (GrnUomMapper grnUomMapper : physicalInspectionDetail.getGoodReceiptNoteDetail()
						.getGrnUomMappersList()) {
					GrnUomMapperResponse grnUom = new GrnUomMapperResponse(grnUomMapper.getId(),
							grnUomMapper.getUomType().getId(), grnUomMapper.getUomUnit().getId(),
							grnUomMapper.getConversions(), grnUomMapper.getIpUom(), grnUomMapper.getOpUom(),
							grnUomMapper.getStoreUom());
					grnUomMapperResponse.add(grnUom);
				}
				List<GrnBonusUomMapperResponse> grnBonusUomMapperResponse = new ArrayList<GrnBonusUomMapperResponse>();
				for (GrnBonusUomMapper grnBonusUomMapper : physicalInspectionDetail.getGoodReceiptNoteDetail()
						.getGrnBonusUomMappersList()) {
					GrnBonusUomMapperResponse grnBonusUomMappe = new GrnBonusUomMapperResponse(
							grnBonusUomMapper.getId(), grnBonusUomMapper.getUomType().getId(),
							grnBonusUomMapper.getUomUnit().getId(), grnBonusUomMapper.getConversions(),
							grnBonusUomMapper.getIpUom(), grnBonusUomMapper.getOpUom(),
							grnBonusUomMapper.getStoreUom());
					grnBonusUomMapperResponse.add(grnBonusUomMappe);
				}
				item = new ConsignmentStockConversionItem(physicalInspectionDetail.getId(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getId(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getCode(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getName(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBatchNo(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnApprovedQuantity(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomType().getId(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomType().getType(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomUnit().getId(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomUnit().getUnits(),
						grnUomMapperResponse,
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity():null,
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType().getId() :null,
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType().getType():null,
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit().getId():null,
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit().getUnits():null,
						grnBonusUomMapperResponse, physicalInspectionDetail.getGoodReceiptNoteDetail().getCop(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getMrp(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getSpecification(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getRemark(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnReceivedQuantity(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusReceivedQuantity(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getLotNumber(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getExpiryDate(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getManufacturer().getName());
			} else if (physicalInspectionDetail.getPhysicalInspection().getGoodReceiptNote().getAssetType().getId()
					.equals(Constants.getFixedAssetId())) {
				item = new ConsignmentStockConversionItem(physicalInspectionDetail.getId(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getId(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getCode(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getName(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBatchNo(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnApprovedQuantity(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity():null,
						physicalInspectionDetail.getGoodReceiptNoteDetail().getCop(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getMrp(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getSpecification(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getRemark(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnReceivedQuantity(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusReceivedQuantity(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getLotNumber(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getExpiryDate(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getManufacturer().getName());
			}
			item.setApprovedQuantity(physicalInspectionDetail.getApprovedQuantity());
			item.setRejectQuantity(physicalInspectionDetail.getRejectQuantity());
			item.setStatusId(physicalInspectionDetail.getStatus().getId());
			item.setStatus(physicalInspectionDetail.getStatus().getStatus());

			/* added by Minesh */
			item.setBatchId(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getId());
			List<Batch> batchList = consignmentStockConversionDao.getBatchListFromItem(item.getItemId(), item.getBatchNo(), item.getLotNo() , physicalInspectionRequest.getUnitId()!=null ? physicalInspectionRequest.getUnitId():0);

			int totalConsignmentStock = 0;
			int availableConsignmentStock = 0;
			int returnedQuantity = 0;
			int convertedConsignmentStock = 0;
			int availableConvertedStock = 0;
			Integer currentQty = 0;

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
						totalConsignmentStock += physicalInspectionDetail.getApprovedQuantity() / conversion;

						returnedQuantity += (batch.getReturnedQuantity() != null
								? batch.getReturnedQuantity() / conversion
								: 0);
						if (!batch.getIsConsignment()) {
							convertedConsignmentStock += batch.getQuantity() / conversion;
							availableConvertedStock += batch.getCurrentQuantity() / conversion;
						} else {
							availableConsignmentStock += batch.getCurrentQuantity() / conversion;
						}
					}

				}
				// item.setTotalConsignmentStock(totalConsignmentStock);
				// item.setReturnedQuantity(returnedQuantity);
			}
			/*
			 * if (batchList.size() == 0) {
			 * item.setTotalConsignmentStock(physicalInspectionDetail.getApprovedQuantity())
			 * ;
			 * item.setReturnedQuantity(physicalInspectionDetail.getGoodReceiptNoteDetail().
			 * getBatch().getReturnedQuantity());
			 * 
			 * }
			 */
			Integer sCstock = consignmentStockConversionDao.getTotalConsignmentStockFromBatch(
					physicalInspectionDetail.getGoodReceiptNoteDetail().getBatchNo(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getLotNumber());
			
			//Integer sCstock = consignmentStockConversionDao.getTotalConsignmentStock( physicalInspectionDetail.getGoodReceiptNoteDetail().getId());
			
			/* item.setReturnedQuantity(consignmentStockConversionDao.getConsignmentReturnQty(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatchNo(), physicalInspectionDetail.getGoodReceiptNoteDetail().getLotNumber(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getId())); */
			item.setReturnedQuantity(consignmentStockConversionDao.getTotalConsignmentReturnQtyAgainGRNDetailId(
					physicalInspectionDetail.getGoodReceiptNoteDetail().getId(), physicalInspectionRequest.getUnitId()!=null ? physicalInspectionRequest.getUnitId():0).intValue());
			
			item.setTotalConsignmentStock(sCstock);
			// if
			// (physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getIsConsignment()
			// == true) {
			// item.setTotalConsignmentStock(physicalInspectionDetail.getApprovedQuantity());
			// }
			// Batch
			// bth=consignmentStockConversionDao.getBatch(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getId());
			Integer grnStoreId = physicalInspectionDetail.getGoodReceiptNoteDetail().getGoodReceiptNote().getStore()
					.getStoreId();
			
		
			BigInteger storeStockTransfered=BigInteger.ZERO;
			if(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getBatchNo()!=null)
			{
				 storeStockTransfered = consignmentStockConversionDao.getStoreStockTransfered(
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getId(), grnStoreId);	
			}
			else
			{
				 storeStockTransfered = consignmentStockConversionDao.getStoreStockTransferedByLotNo(item.getLotNo(), grnStoreId);
				
			}
			
			
			//BigInteger consumedQty = consignmentStockConversionDao.getConsumedQty(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getBatchNo());
			
			//start change by minesh
			//BigInteger consumedQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getBatchNo());
			BigInteger consumedQty = consignmentStockConversionDao.getConsumedQtyWithReturnedQty(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getBatchNo(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getLotNo());
			//end changed by minesh
			
			//BigInteger convertedConsignmentStock1 = consignmentStockConversionDao.getconvertedConsignmentStock(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getBatchNo());
			BigInteger convertedConsignmentStock1 = consignmentStockConversionDao.getconvertedConsignmentStock(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getBatchNo(),physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getLotNo());
			item.setConsumedQty(consumedQty);
			item.setConvertedConsignmentStock(convertedConsignmentStock1);
			// if (bth!=null) {
			// item.setConsumedQty(bth.getQuantity()- bth.getCurrentQuantity());
			// item.setConvertedConsignmentStock( bth.getCurrentQuantity());
			// }
			// else
			// {
			// item.setConsumedQty(0);
			// item.setConvertedConsignmentStock(0);
			// }
			item.setStoreStockTransfered(storeStockTransfered);
			item.setCurrentQty(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getCurrentQuantity());
			item.setAvailableConsignmentStock(availableConsignmentStock);

			// item.setConvertedConsignmentStock(convertedConsignmentStock);
			item.setConversionQuantity(null);
			item.setAvailableConvertedStock(availableConvertedStock);
			item.setBatch(batchList);

			// batchLists.addAll(new ArrayList<>(batchList));
			/* end added by Minesh */

			if (physicalInspectionDetail.getGoodReceiptNoteDetail().getPurchaseOrderDetail() != null) {
				item.setReceivedItemSpecification(physicalInspectionDetail.getGoodReceiptNoteDetail()
						.getPurchaseOrderDetail().getSpecification());
			}
			items.add(item);

		}
		List<RackResponse> racks = new ArrayList<RackResponse>();

		List<RejectReasonResponse> rejectReasonList = new ArrayList<RejectReasonResponse>();

		consignmentStockConversionDetails = new ConsignmentStockConversionDetails(physicalInspection.getId(),
				physicalInspection.getGoodReceiptNote().getGrnNumber(), physicalInspection.getAddedDate(),
				physicalInspection.getGoodReceiptNote().getInvoiceNumber(),
				physicalInspection.getGoodReceiptNote().getInvoiceDate(),
				physicalInspection.getGoodReceiptNote().getDcNumber(),
				physicalInspection.getGoodReceiptNote().getDcDate(),
				physicalInspection.getGoodReceiptNote().getRequestType().getId(),
				physicalInspection.getGoodReceiptNote().getRequestType().getType(),
				physicalInspection.getGoodReceiptNote().getStore().getStoreId(),
				physicalInspection.getGoodReceiptNote().getStore().getStoreName(),
				physicalInspection.getGoodReceiptNote().getVendor().getId(),
				physicalInspection.getGoodReceiptNote().getVendor().getName(),
				physicalInspection.getGoodReceiptNote().getAssetType().getId(),
				physicalInspection.getGoodReceiptNote().getAssetType().getType(), items, racks, rejectReasonList,
				physicalInspection.getAddedBy().getUserName(),
				physicalInspection.getGoodReceiptNote().getIsConsignment());
		consignmentStockConversionDetails.setStatusId(physicalInspection.getStatus().getId());
		consignmentStockConversionDetails.setStatus(physicalInspection.getStatus().getStatus());
		// consignmentStockConversionDetails.setBatch(batchLists);
		consignmentStockConversionDetails.setRemark(physicalInspection.getGoodReceiptNote().getConsignmentStockConversionRemark()!=null ? physicalInspection.getGoodReceiptNote().getConsignmentStockConversionRemark() :null);
		return consignmentStockConversionDetails;
	}

	@Override
	@Transactional
	public CommonResponse saveConsignmentStockConversion(
			DetailsConsignmentStockConversionSaveRequest detailsConsignmentStockConversionSaveRequest)
			throws Exception {
		try {
			CommonResponse response = new CommonResponse();
			Long batchId = null;
			Integer UomTypeId = null;
			for (ConsignmentStockConversionItemSaveRequest item : detailsConsignmentStockConversionSaveRequest
					.getItems()) {
				/*
				 * List<Batch> batchList =
				 * consignmentStockConversionDao.getBatchListFromItem(item.getItemId(),
				 * item.getBatchNo(), item.getLotNo()); HashMap<Long, Integer>
				 * batchUomConvertation = new HashMap<Long, Integer>(); List<Long> batchIds =
				 * new ArrayList<>(); batchIds.add(item.getBatchId()); List<UomResponse>
				 * uomListAll = new ArrayList<UomResponse>(); if (!batchIds.isEmpty()) {
				 * uomListAll = GlobalDao.getBatchUomListBP(batchIds); } for (Batch batch :
				 * batchList) { if
				 * (detailsConsignmentStockConversionSaveRequest.getAssetTypeId()
				 * .equals(Constants.getConsumableId())) { List<UomResponse> uomList =
				 * uomListAll.stream() .filter(x ->
				 * x.getBatchId().equals(batch.getId())).collect(Collectors.toList());
				 * 
				 * if (uomList != null && !uomList.isEmpty()) { UomResponse ipUom =
				 * Util.getIpUom(uomList); if (ipUom != null) {
				 * batchUomConvertation.put(batch.getId(), ipUom.getUomTypeId()); } } } } for
				 * (Map.Entry m : batchUomConvertation.entrySet()) { if (m.getKey() ==
				 * item.getBatchId()) //System.out.println(m.getKey() + " " + m.getValue());
				 * UomTypeId = (Integer) m.getValue(); }
				 */

				List<UomResponse> uomList = GlobalDao.getBatchUomList(item.getBatchId());
				if (uomList != null && !uomList.isEmpty()) {
					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						UomTypeId = ipUom.getUomTypeId();
					}
				}
				batchId = saveStoreStockDetails(detailsConsignmentStockConversionSaveRequest.getStoreId(),
						item.getBatchId(), item.getConversionQuantity(), new UomType(UomTypeId),detailsConsignmentStockConversionSaveRequest.getUnitId(),
						detailsConsignmentStockConversionSaveRequest);
				
				//consignmentStockReturnDao.updateStoreStock(item.getBatchNo(), item.getLotNo() , item.getConversionQuantity());
				
				// consignmentStockReturnDao.updateBatchData(item.getBatchId(),
				// item.getConversionQuantity());
			}
			response.setId(batchId);
			if (batchId != null) {
				response.setMessage("Stock Converted Successfully");
			} else {
				response.setMessage("Somthing To Be Wrong!!!");
			}
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}

	private Long saveStoreStockDetails(Integer storeId, Long batchId, Integer conversionQuantity, UomType uomType, Integer unitId, DetailsConsignmentStockConversionSaveRequest request) {
		if (conversionQuantity > 0) {
			try {
				Batch batch = GlobalDao.getBatch(batchId);
				if (batch != null) {

				//	Integer leaseQuantity = Util.GetLeaseQuantity(conversionQuantity, batch.getBatchUomList(), uomType);
					Integer leaseQuantity =conversionQuantity;

					if (batch.getIsConsignment() != null && batch.getIsConsignment()) {

						if (leaseQuantity <= batch.getCurrentQuantity()) {

							Batch normalBatch = getConsignmentConvertedBatch(batchId, batch, leaseQuantity, request);

							// code for we convert/consume quantity in ConsignmentBatch, so set 'quantity'
							// and also change in storeStockList
							DetailsConsignmentStockConversionSaveRequest res = grnDao.getTransactionDetails(batch.getId());
							setLeaseQuantity(batch, leaseQuantity, storeId,unitId, request,true,res.getTransactionDetailsId(),res.getTransactionId());

							// code for we convert/consume quantity in ConvertedConsignmentBatch, so set
							// 'quantity' and also change in storeStockList
							setLeaseQuantity(normalBatch, leaseQuantity, storeId,unitId, request,false,res.getTransactionDetailsId(),res.getTransactionId());

							// code for update conversion Status of grn
							//updateGRNConversionStatus(batchId, storeId);
							updateGRNConversionStatus(batch,unitId , request);
							
							return normalBatch.getId();
						} else {
							throw new CustomException("Conversion Quantity Must Be Less Than Quantity");
						}
					}

				}

			} catch (Exception e) {
				logger.error("saveStoreStockDetails failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
		return batchId;
	}

	private void setLeaseQuantity(Batch batch, Integer leaseQuantity, Integer storeId,Integer unitId, DetailsConsignmentStockConversionSaveRequest request, boolean b, Integer transactionDetailsId, Integer transactionId) {
		Integer tQuantity;
		Integer availableQuantity;

		StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batch.getId(), storeId,unitId);
		StoreStock storeStockDetail = null;
		if (storeStockList == null) {
			StoreStock storeStock = new StoreStock(leaseQuantity, new Store(storeId), new Batch(batch.getId()));
			storeStock.setTotalQty(leaseQuantity);
			storeStock.setIsConsignment(batch.getIsConsignment());
//			sessionService.setEntityDefaults(storeStock);
			storeStock.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
		} else {
			tQuantity = leaseQuantity + storeStockList.gettQty();
			availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
			storeStockList.setAvailableQty(availableQuantity);
			//storeStockList.setTotalQty(availableQuantity);
			storeStockList.setTotalQty(storeStockList.getTotalQty()-leaseQuantity);
			//storeStockList.settQty(tQuantity);
			storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
		}
		if (batch.getIsConsignment()) {
			batch.setQuantity(batch.getQuantity() - leaseQuantity);
			batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);
			GlobalDao.updateBatchLeaseQuantity(batch);
		}
		saveStockTransaction(leaseQuantity, storeStockDetail, request,b , transactionDetailsId, transactionId);
	}

	private Batch getConsignmentConvertedBatch(Long batchId, Batch batch, Integer leaseQuantity, DetailsConsignmentStockConversionSaveRequest request) {
		// Batch newbatchResponse = new Batch();
		Batch newBatch = new Batch();
		newBatch.setOrganizationId(batch.getOrganization().getOrganizationId());
		newBatch.setUnitId(batch.getUnit().getUnitId());
		newBatch.setUomUnit(batch.getUomUnit());
		newBatch.setItem(batch.getItem());
		newBatch.setBatchNo(batch.getBatchNo());
		newBatch.setLotNo(batch.getLotNo());
		newBatch.setQuantity(leaseQuantity);// leaseQuantity
		newBatch.setCurrentQuantity(leaseQuantity);// leaseQuantity
		newBatch.setExpiryDate(batch.getExpiryDate());
		newBatch.setCop(batch.getCop());
		newBatch.setRate(batch.getRate());
		newBatch.setSellingPrice(batch.getSellingPrice());
		newBatch.setMarkup(batch.getMarkup());
		newBatch.setLendingPrice(batch.getLendingPrice());
		newBatch.setActualLendingPrice(batch.getActualLendingPrice());
		newBatch.setTax(batch.getTax());
		newBatch.setTaxPercentage(batch.getTaxPercentage());
		newBatch.setTaxAmount(batch.getTaxAmount());
		newBatch.setBarcode(batch.getBarcode());
		newBatch.setIsOpb(batch.getIsOpb());
		newBatch.setIsActive(batch.getIsActive());
		newBatch.setAddedDate(TimeZoneComponent.getDateByzone(batch.getUnit().getUnitId()));
		newBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(batch.getUnit().getUnitId()));
		newBatch.setAddedByUser(request.getUserId());
		newBatch.setUpdatedByUser(request.getUserId());
		newBatch.setIsDeleted(batch.getIsDeleted());
		newBatch.setBarcodePrintCount(batch.getBarcodePrintCount());
		newBatch.setIsQuarantine(batch.getIsQuarantine());
		newBatch.setVendor(batch.getVendor());
		newBatch.setServiceProvider(batch.getServiceProvider());
		newBatch.setWarrantyFrom(batch.getWarrantyFrom());
		newBatch.setWarrantyTo(batch.getWarrantyTo());
		newBatch.setPmsFrequency(batch.getPmsFrequency());
		newBatch.setYearOfManufacture(batch.getYearOfManufacture());
		newBatch.setWorkingCondition(batch.getWorkingCondition());
		newBatch.setModelNumber(batch.getModelNumber());
		newBatch.setIsPharmacy(batch.getIsPharmacy());
		newBatch.setIsIndent(batch.getIsIndent());
		newBatch.setIsReturn(false);
		newBatch.setRejectedQuantity(0);
		newBatch.setReturnedQuantity(0);
		// newBatch.stop
		newBatch.setIsConsignment(false);

		for (BatchUomMapper batchUomMapperObj : batch.getBatchUomList()) {
			BatchUomMapper batchUomMapper = new BatchUomMapper(batchUomMapperObj.getConversions(),
					batchUomMapperObj.getIpUom(), batchUomMapperObj.getOpUom(), batchUomMapperObj.getStoreUom(),
					new UomType(batchUomMapperObj.getUomType().getId()),
					new UomUnit(batchUomMapperObj.getUomUnit().getId()));
			batchUomMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(), request.getUserId());
			newBatch.addBatchUom(batchUomMapper);
		}

		// newbatchResponse = GlobalDao.saveBatch(newBatch);
		// batch = GlobalDao.getBatch(newbatchResponse.getId());
		newBatch = GlobalDao.saveBatch(newBatch);
		return newBatch;
	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, DetailsConsignmentStockConversionSaveRequest request, boolean b, Integer transactionDetailsId, Integer transactionId) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(167);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(request.getStoreId()!=null ? request.getStoreId() : sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()),0);
		stockTransaction.setTransactionDetialsId(transactionDetailsId);
		stockTransaction.setTransactionId(transactionId);
		//stockTransaction.setItem(new Item(storeStock.getBatch().getItem().getId()));
		stockTransaction.setIsTransit(false);
		if(b == true) {
			stockTransaction.setOutQty(leaseQuantity);
		}else if(b == false){
			stockTransaction.setInQty(leaseQuantity);
		}
		stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		GlobalDao.saveStockTransaction(stockTransaction);
	}
	private void updateGRNConversionStatus(Batch batch, Integer unitId, DetailsConsignmentStockConversionSaveRequest request) {
		GoodReceiptNote goodReceiptNote = batch.getGoodReceiptNoteDetail().get(0).getGoodReceiptNote();
		goodReceiptNote.setConsignmentStockConversionRemark(request.getRemarks()!=null ? request.getRemarks() :null);
		List<Batch> batchList = GlobalDao.getBatchListForConversion(goodReceiptNote.getId(),unitId);
		int totalCurrentQuantity = 0;
		int totalConsignStock=0;
		int convertedConsignStock=0;
		for (Batch batchObj : batchList) {
			totalCurrentQuantity += batchObj.getCurrentQuantity();
		}
		for (ConsignmentStockConversionItemSaveRequest req : request.getItems()) {
			totalConsignStock+=req.getReceivedQuantity();
			convertedConsignStock+=req.getConvertedConsignmentStock()+req.getConversionQuantity();
		};
		if(request.getStatusId()==18)
		{
			goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
		}
		if(request.getStatusId()==4)
		{
			goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		}
//		if(totalConsignStock ==convertedConsignStock) {
//			goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
//		}else {
//			goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
//		}		
			grnDao.updateGoodReceiptNote(goodReceiptNote);
	}
	//not in use
	private void updateGRNConversionStatus(Long batchId, int storId,Integer unitId) {
		Batch batch = GlobalDao.getBatch(storId, batchId,unitId);
		GoodReceiptNote goodReceiptNote = batch.getGoodReceiptNoteDetail().get(0).getGoodReceiptNote();
		if (goodReceiptNote.getIsConsignment()) {
			List<Batch> batchList = GlobalDao.getBatchListForConversion(batch.getLotNo(), storId,unitId);
			int totalConsignmentQuantity = 0;
			int totalConsignmentConvertedQuantity = 0;
			for (Batch batchObj : batchList) {
				totalConsignmentQuantity += batchObj.getQuantity();
				totalConsignmentConvertedQuantity = !batchObj.getIsConsignment()
						? totalConsignmentConvertedQuantity += batchObj.getQuantity()
						: totalConsignmentConvertedQuantity;
			}
			if (totalConsignmentQuantity == totalConsignmentConvertedQuantity) {
				goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));

			} else if (totalConsignmentConvertedQuantity > 0) {
				goodReceiptNote.setConversionStatus(
						new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
			} else {
				goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
			}
			
			grnDao.updateGoodReceiptNote(goodReceiptNote);
		}
	}
	
	// not use currently
	private void setLeaseQuantityForConvertedBatch(Batch batch, Integer leaseQuantity) {
		Integer tQuantity;
		Integer availableQuantity;
		Integer currentQuantity;

		StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batch.getId(), sessionService.getStoreId(),null);
		StoreStock storeStockDetail = null;
		if (storeStockList == null) {
			// change from batchId to batch.getId() in parameter
			StoreStock storeStock = new StoreStock(leaseQuantity, new Store(sessionService.getStoreId()),
					new Batch(batch.getId()));
			storeStock.setTotalQty(leaseQuantity);
//			sessionService.setEntityDefaults(storeStock);
			storeStock.setCreateDefaults(sessionService.getOrganizationId(),sessionService.getUnitId(),
					sessionService.getUserId());
			storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
		} else {
			tQuantity = leaseQuantity + storeStockList.gettQty();
			availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
			storeStockList.setAvailableQty(availableQuantity);
			storeStockList.settQty(tQuantity);
			storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
		}
		if (batch.getIsConsignment()) {
			batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);
			GlobalDao.updateBatchLeaseQuantity(batch);
		}
		saveStockTransaction(leaseQuantity, storeStockDetail, null,true,0,0);
		// Batch batchObject = GlobalDao.getBatch(sessionService.getStoreId(),
		// storeStockDetail.getBatch().getId());
	}

	@Override
	@Transactional
	public List<AllDataResponse> allDataList(Integer storeId, Integer batchId, String batchNo, String lotNo) {

		List<AllDataResponse> list = consignmentStockConversionDao.allDataList(storeId, batchId, batchNo, lotNo);
		return list;

	}

}