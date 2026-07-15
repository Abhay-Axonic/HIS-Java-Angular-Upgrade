package com.param.inventory.stockAdjustments.service.computeStock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.ComputeStock;
import com.param.entity.model.inventory.ComputeStockDetail;
import com.param.entity.model.inventory.ConfirmStock;
import com.param.entity.model.inventory.ConfirmStockDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.UploadStock;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.entity.model.inventory.VarianceApproval;
import com.param.entity.model.inventory.VarianceApprovalDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.inventory.stockAdjustments.dao.computeStock.IComputeStockDao;
import com.param.inventory.stockAdjustments.dto.ComputeStockDropdownResponse;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockItemResponse;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockRequest;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockRequestItem;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockResponse;
import com.param.inventory.stockAdjustments.dto.varianceApproval.BatchResponse;
import com.param.inventory.stockAdjustments.service.uploadStock.IUploadStockService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ComputeStockService")
public class ComputeStockService implements IComputeStockService {

	private static final Logger logger = Logger.getLogger(ComputeStockService.class);

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.ComputeStock.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IComputeStockDao ComputeStockDao;
	
	@Autowired
	IUploadStockService UploadStockService;

	@Override
	@Transactional
	public Object getComputeStockList(ComputeStockRequest request) {

		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ComputeStockDao.searchComputeStockList(request, screenId);
	}

	@Override
	@Transactional
	public Object cancelComputeStock(ComputeStockRequest request) {

		ComputeStock computeStock = ComputeStockDao.getComputeStock(request.getId());

		computeStock.setCancelReason(new CancelReason(request.getCancelReasonId()));
		computeStock.setCancelNote(request.getCancelNote());

		computeStock.setStatus(new Status(request.getStatusId()));

		computeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(computeStock.getUnitId()));
		//computeStock.setUpdatedBy(computeStock.getUser());
		computeStock.setUpdatedByUser(request.getUserId());

		ComputeStockDao.updateComputeStock(computeStock);

		CommonResponse response = new CommonResponse();
		response.setMessage(" Compute Stock cancelled successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object approveComputeStock(ComputeStockRequest request) {
		
		CommonResponse response = new CommonResponse();
		
		for (Integer id : request.getComputeStockId()) {
			
			Boolean isDataSave = ComputeStockDao.CheckDataForConfirmStock(id);
			
			if(isDataSave){
				
			ComputeStock computeStock = ComputeStockDao.getComputeStock(id);
			computeStock.setStatus(new Status(request.getStatusId()));			
		
				Integer count = 0;
				Integer detailCount = computeStock.getComputeStockDetail().size();
				UploadStock uploadStock = computeStock.getUploadStock();
				
				VarianceApproval varianceApproval = new VarianceApproval(
						new Status(com.param.common.constants.Status.Pending.getStatus()),
						new Store(computeStock.getStore().getStoreId()), new UploadStock(computeStock.getUploadStock().getId()),
						new ComputeStock(computeStock.getId()));
				//sessionService.setEntityDefaults(varianceApproval);	
				/*varianceApproval.setAddedBy(new User(request.getUserId()));
				varianceApproval.setUpdatedBy(new User(request.getUserId()));*/
				varianceApproval.setAddedByUser(request.getUserId());
				varianceApproval.setUpdatedByUser(request.getUserId());
				varianceApproval.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				varianceApproval.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				varianceApproval.setOrganizationId(request.getOrganizationId());
				varianceApproval.setUnitId(request.getUnitId());

				String stockAdjustmentNo = UploadStockService.getPrefixRunningNumber(computeStock.getStore().getStoreId(), request.getUnitId() !=null ? request.getUnitId() : sessionService.getUnitId(), screenId);
				
				ConfirmStock confirmStock = new ConfirmStock(computeStock,
						stockAdjustmentNo, new Status(com.param.common.constants.Status.Pending.getStatus()),new Store(computeStock.getStore().getStoreId()));  
				//sessionService.setEntityDefaults(confirmStock);	
				/*confirmStock.setAddedBy(new User(request.getUserId()));
				confirmStock.setUpdatedBy(new User(request.getUserId()));*/
				confirmStock.setAddedByUser(request.getUserId());
				confirmStock.setUpdatedByUser(request.getUserId());
				confirmStock.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				confirmStock.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				confirmStock.setOrganizationId(request.getOrganizationId());
				confirmStock.setUnitId(request.getUnitId());
				for (ComputeStockDetail detail : computeStock.getComputeStockDetail()) {
					UploadStockDetail uploadStockDetail = detail.getUploadStockDetail();
					Integer storeStockAvailableQuantity = GlobalDao
							.getStoreStockByBatch(computeStock.getStore().getStoreId(), detail.getBatch().getId(),0);			
				if (storeStockAvailableQuantity.equals(uploadStockDetail.getApprovedUploadQuantity())) {
						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
						/*detail.setUpdatedBy(detail.getUser());*/
						detail.setUpdatedByUser(request.getUserId());
						uploadStockDetail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));					
						count++;
					}
				VarianceApprovalDetail vad=new VarianceApprovalDetail(
						new Batch(detail.getBatch().getId()), new ComputeStockDetail(detail.getId()),
						new UploadStockDetail(detail.getUploadStockDetail().getId()));
				vad.setAddedByUser(request.getUserId());
				vad.setUpdatedByUser(request.getUserId());
				vad.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				vad.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				vad.setOrganizationId(request.getOrganizationId());
				vad.setUnitId(request.getUnitId());
					varianceApproval.addVarianceApprovalDetail(vad);								
					try {
					  if(detail.getStatus().getId() == 8){
						 ConfirmStockDetail confirmStockDetail = new ConfirmStockDetail(
								new ComputeStockDetail(detail.getId()),
								new VarianceApprovalDetail(detail.getId()), new Batch(detail.getBatch().getId()));
						//sessionService.setEntityDefaults(confirmStockDetail);
						   /* confirmStockDetail.setAddedBy(new User(request.getUserId()));
						    confirmStockDetail.setUpdatedBy(new User(request.getUserId()));*/
						 confirmStockDetail.setAddedByUser(request.getUserId());
						 confirmStockDetail.setUpdatedByUser(request.getUserId());
							confirmStockDetail.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							confirmStockDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							confirmStockDetail.setOrganizationId(request.getOrganizationId());
							confirmStockDetail.setUnitId(request.getUnitId());
						confirmStock.addConfirmStockDetail(confirmStockDetail);
						confirmStockDetail.setStatus(
								new Status(com.param.common.constants.Status.Pending.getStatus()));
						// confirmStockDetail.setApprovalQuantity(detail.getApprovedQuantity());
						confirmStockDetail.setApprovalQuantity(detail.getApprovedQuantity());		
					}

					} catch (Exception e) {
						logger.error("Error while confirming stock detail during compute stock approval", e);
						throw new ServiceException(e.getMessage());
					}
				}

				if (count == detailCount) {
					uploadStock.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
				}

				computeStock.setApprovedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				computeStock.setApprovedBy(new User(request.getUserId() !=null ? request.getUserId() : sessionService.getUserId()));

				computeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				/*computeStock.setUpdatedBy(computeStock.getUser());	*/
				computeStock.setUpdatedByUser(request.getUserId());
									
				ComputeStockDao.updateComputeStock(computeStock);
				ComputeStockDao.saveConfirmStock(confirmStock);
				ComputeStockDao.saveVarianceApproval(varianceApproval);
				
				response.setMessage("Compute Stock approved successfully");
				response.setCode("true");
			}
			else{			
				response.setMessage("Save compute Stock details before Approved.");
				response.setCode("false");
			}
		}	
	
		return response;
	}


//	@Override
//	@Transactional
//	public Object approveComputeStock(ComputeStockRequest request) {
//
//		for (Integer id : request.getComputeStockId()) {
//			ComputeStock computeStock = ComputeStockDao.getComputeStock(id);
//			computeStock.setStatus(new Status(request.getStatusId()));
//
//			Integer count = 0;
//			Integer detailCount = computeStock.getComputeStockDetail().size();
//			UploadStock uploadStock = computeStock.getUploadStock();
//
//			for (ComputeStockDetail detail : computeStock.getComputeStockDetail()) {
//				UploadStockDetail uploadStockDetail = detail.getUploadStockDetail();
//				Integer storeStockAvailableQuantity = GlobalDao
//						.getStoreStockByBatch(computeStock.getStore().getStoreId(), detail.getBatch().getId());
//				if (storeStockAvailableQuantity.equals(uploadStockDetail.getApprovedUploadQuantity())) {
//					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//					detail.setUpdatedBy(detail.getUser());
//					uploadStockDetail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));					
//					count++;
//				}
//			}
//
//			if (count == detailCount) {
//				uploadStock.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
//			}
//
//			computeStock.setApprovedDate(TimeZoneComponent.getDateByzone(unitId));
//			computeStock.setApprovedBy(new User(sessionService.getUserId()));
//
//			computeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//			computeStock.setUpdatedBy(computeStock.getUser());	
//								
//			ComputeStockDao.updateComputeStock(computeStock);				
//		}
//
//		CommonResponse response = new CommonResponse();
//		response.setMessage(" Compute Stock approved successfully");
//
//		return response;
//	}



	@Override
	@Transactional
	public ComputeStockDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		ComputeStockDropdownResponse response = new ComputeStockDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		return response;
	}
	
	@Override
	@Transactional
	public Object getComputeStock(Integer id) {		
		
		ComputeStock computeStock = ComputeStockDao.getComputeStock(id);	
		ComputeStockResponse response = new ComputeStockResponse(computeStock.getId(),
		computeStock.getStore().getStoreId(), computeStock.getStore().getStoreName(),
		computeStock.getUploadStock().getStopTransaction().getStartDateTime(),
		computeStock.getUploadStock().getStopTransaction().getStopDateTime(),
		computeStock.getUploadStock().getUpdatedBy().getUserId(),
		computeStock.getUploadStock().getUpdatedBy().getUserName(),
		computeStock.getStatus().getStatus(),
		computeStock.getStatus().getId());
		
		ComputeStockItemResponse computeStockItemResponseDetail  =new  ComputeStockItemResponse ();
		List previosItemIds = new ArrayList<>();
		List<BatchResponse> batchList = null;				
		
		Long quantity = 0L;
		List<ComputeStockDetail> computeDetails = computeStock.getComputeStockDetail();
		Comparator<ComputeStockDetail> compareById = (ComputeStockDetail o1, ComputeStockDetail o2) -> o1.getItemId().compareTo( o2.getItemId());
		Collections.sort(computeDetails, compareById.reversed());
		 BatchResponse  batchResponse = null;
		
		for (ComputeStockDetail detail : computeDetails) {	
		   if(previosItemIds == null || !previosItemIds.contains(detail.getBatch().getItem().getId()) ){			
				 batchList =new ArrayList<BatchResponse>();
				 batchResponse = new BatchResponse();
				 Long TotalstoreStock = ComputeStockDao.getStoreStockByItemForComputeStock(computeStock.getStore().getStoreId(), detail.getBatch().getItem().getId());
				 Integer TotalUploadedQty = ComputeStockDao.getUploadStockByItemForComputeStock(detail.getBatch().getItem().getId(),id);
//				 Double cop = (detail.getBatch().getCop() != null ? detail.getBatch().getCop() : 1) * TotalstoreStock;
//				 Double mrp = (detail.getBatch().getRate() != null ? detail.getBatch().getRate() : 1) * TotalstoreStock;
				 
				 Double cop = (detail.getBatch().getCop() != null ? detail.getBatch().getCop() : 1);
				 Double mrp = (detail.getBatch().getRate() != null ? detail.getBatch().getRate() : 1); 
		
					previosItemIds.add(detail.getBatch().getItem().getId());
					  computeStockItemResponseDetail =new ComputeStockItemResponse(detail.getId(),
								detail.getUploadStockDetail().getBatch().getItem() != null
										? detail.getUploadStockDetail().getBatch().getItem().getId()
										: null,
								detail.getUploadStockDetail().getBatch().getItem() != null
										? detail.getUploadStockDetail().getBatch().getItem().getName()
										: null,
								detail.getUploadStockDetail().getBatch().getItem() != null
										? detail.getUploadStockDetail().getBatch().getItem().getCode()
										: null,
								detail.getUploadStockDetail().getBatch().getItem().getGeneric().getProductCategory() != null
										? detail.getUploadStockDetail().getBatch().getItem().getGeneric().getProductCategory()
												.getId()
										: null,
								detail.getUploadStockDetail().getBatch().getItem().getGeneric().getProductCategory() != null
										? detail.getUploadStockDetail().getBatch().getItem().getGeneric().getProductCategory()
												.getCategory()
										: null,
								detail.getUploadStockDetail().getUomType() != null
										? detail.getUploadStockDetail().getUomType().getId()
										: null,
								detail.getUploadStockDetail().getUomType() != null
										? detail.getUploadStockDetail().getUomType().getType()
										: null,
								detail.getUploadStockDetail().getUomUnit() != null
										? detail.getUploadStockDetail().getUomUnit().getId()
										: null,
								detail.getUploadStockDetail().getUomUnit() != null
										? detail.getUploadStockDetail().getUomUnit().getUnits()
										: null,
								detail.getUploadStockDetail().getBatch() != null ? detail.getUploadStockDetail().getBatch().getId()
										: null,
								detail.getUploadStockDetail().getBatch() != null
										? detail.getUploadStockDetail().getBatch().getBatchNo()
										: null,
								detail.getUploadStockDetail().getBatch().getExpiryDate(), 
								detail.getUploadStockDetail().getApprovedUploadQuantity() != null
										? TotalUploadedQty 
										: null,
								detail.getStatus() != null ? detail.getStatus().getId() : null, 
								detail.getStatus() != null ? detail.getStatus().getStatus() : null,							
												
								detail.getNewExpiryDate() != null ? detail.getNewExpiryDate() : null,
								detail.getNewBatchNo() != null ? detail.getNewBatchNo() : null,
								detail.getNewMrp() != null ? detail.getNewMrp() : null,
								detail.getRemark() != null ? detail.getRemark() : null,
							    detail.getRejectQuantity() != null ? detail.getRejectQuantity() : 0
								);	
					  response.addItems(computeStockItemResponseDetail);
					  computeStockItemResponseDetail.setAvailableQuantity(TotalstoreStock);
					  computeStockItemResponseDetail.setCop(cop);
					  computeStockItemResponseDetail.setMrp(mrp);
					  
					  long AvialableQuantity = 0L;
					  List<Batch> getNewBatchList = ComputeStockDao.getNewBatchList(detail.getItemId(), computeStock.getStore().getStoreId(),id, computeStock.getUnit().getUnitId());
					  for(Batch newBatch : getNewBatchList){
						  batchResponse = new BatchResponse();
							for (StoreStock storeStock : newBatch.getStoreStockList()) {
								batchResponse.setBatchId(newBatch.getId());
								batchResponse.setBatchNo(newBatch.getBatchNo());
								AvialableQuantity  =  storeStock.getAvailableQty();
							}	
							batchResponse.setUomTypeId(detail.getUploadStockDetail().getUomType().getId());
							batchResponse.setUom(detail.getUploadStockDetail().getUomType().getType());
							batchResponse.setUomUnitId(detail.getUploadStockDetail().getUomUnit().getId());
							batchResponse.setUomUnit(detail.getUploadStockDetail().getUomUnit().getUnits());				
//							batchResponse.setCop((newBatch.getCop() != null ? newBatch.getCop() : 1) * (AvialableQuantity));
//							batchResponse.setMrp((newBatch.getRate() != null ? newBatch.getRate() : 1) * (AvialableQuantity));
							batchResponse.setCop((newBatch.getCop() != null ? newBatch.getCop() : 1));
							batchResponse.setMrp((newBatch.getRate() != null ? newBatch.getRate() : 1));
							batchResponse.setAvailableQuantity(AvialableQuantity);
							batchResponse.setUploadedQuantity(0);
							batchResponse.setExpiryDate(newBatch.getExpiryDate());
							
							batchResponse.setIsnew(true);
							batchResponse.setNotAnOption(false);
							
							batchList.add(batchResponse);
					  }						 				 				  			  
			}	 			

			  batchResponse = new BatchResponse();	  
			  Batch batch = ComputeStockDao.getQuantity(computeStock.getStore().getStoreId(),
						detail.getBatch().getId());				  			  		  				  
				for (StoreStock storeStock : batch.getStoreStockList()) {
					batchResponse.setBatchId(batch.getId());
					batchResponse.setBatchNo(batch.getBatchNo());
					quantity = quantity + storeStock.getAvailableQty();
				}				
				if (detail.getUploadStockDetail() != null) {
					if (detail.getBatch().getId().equals(batch.getId())) {
						batchResponse.setId(detail.getId());
						batchResponse.setUomTypeId(detail.getUploadStockDetail().getUomType().getId());
						batchResponse.setUom(detail.getUploadStockDetail().getUomType().getType());
						batchResponse.setUomUnitId(detail.getUploadStockDetail().getUomUnit().getId());
						batchResponse.setUomUnit(detail.getUploadStockDetail().getUomUnit().getUnits());
						batchResponse.setUploadedQuantity(detail.getApprovedQuantity()); // Approved Quantity from Compute Details
											
						Integer storeStockAvailableQuantity = GlobalDao.getStoreStockByBatch(computeStock.getStore().getStoreId(),
							detail.getBatch().getId(),0);	
						long AvialableQuantity  = storeStockAvailableQuantity;
//						batchResponse.setCop((batch.getCop() != null ? batch.getCop() : 1) * (AvialableQuantity > 0 ? AvialableQuantity : 0));
//						batchResponse.setMrp((batch.getRate() != null ? batch.getRate() : 1) * (AvialableQuantity > 0 ? AvialableQuantity : 0));
						batchResponse.setCop((batch.getCop() != null ? batch.getCop() : 1)) ;
						batchResponse.setMrp((batch.getRate() != null ? batch.getRate() : 1)); 
						batchResponse.setAvailableQuantity(AvialableQuantity > 0 ? AvialableQuantity : 0);
						batchResponse.setExpiryDate(batch.getExpiryDate());
						
						batchResponse.setNotAnOption(true);
						batchResponse.setIsnew(false);
					}
			   }				
				
				batchList.add(batchResponse);					
				computeStockItemResponseDetail.setBatch(batchList);			
			} 			
						
		return response;
	}


//	@Override
//	@Transactional
//	public Object getComputeStock(Integer id) {
//
//		ComputeStock computeStock = ComputeStockDao.getComputeStock(id);
//
//		ComputeStockResponse response = new ComputeStockResponse(computeStock.getId(),
//				computeStock.getStore().getStoreId(), computeStock.getStore().getStoreName(),
//				computeStock.getUploadStock().getStopTransaction().getStartDateTime(),
//				computeStock.getUploadStock().getStopTransaction().getStopDateTime(),
//				computeStock.getUploadStock().getUpdatedBy().getUserId(),
//				computeStock.getUploadStock().getUpdatedBy().getUserName(), computeStock.getStatus().getStatus(),
//				computeStock.getStatus().getId());
//
//		for (ComputeStockDetail detail : computeStock.getComputeStockDetail()) {
//
//			Batch batch = detail.getUploadStockDetail().getBatch();
//			
//			Integer storeStockAvailableQuantity = GlobalDao.getStoreStockByBatch(computeStock.getStore().getStoreId(),
//					detail.getUploadStockDetail().getBatch().getId());
//			
//			long availableQty = (long) storeStockAvailableQuantity;
//			
////			long conversionQuantity = conversionQuantity((long) storeStockAvailableQuantity,
////					detail.getUploadStockDetail().getUomType().getId(), batch.getBatchUomList()); 
//			
//			Double cop = (batch.getCop() != null ? batch.getCop() : 1) * storeStockAvailableQuantity;
//			
//			Double mrp = (batch.getRate() != null ? batch.getRate() : 1) * storeStockAvailableQuantity;
//
//			response.addItems(new ComputeStockItemResponse(detail.getId(),
//					detail.getUploadStockDetail().getBatch().getItem() != null
//							? detail.getUploadStockDetail().getBatch().getItem().getId()
//							: null,
//					detail.getUploadStockDetail().getBatch().getItem() != null
//							? detail.getUploadStockDetail().getBatch().getItem().getName()
//							: null,
//					detail.getUploadStockDetail().getBatch().getItem() != null
//							? detail.getUploadStockDetail().getBatch().getItem().getCode()
//							: null,
//					detail.getUploadStockDetail().getBatch().getItem().getGeneric().getProductCategory() != null
//							? detail.getUploadStockDetail().getBatch().getItem().getGeneric().getProductCategory()
//									.getId()
//							: null,
//					detail.getUploadStockDetail().getBatch().getItem().getGeneric().getProductCategory() != null
//							? detail.getUploadStockDetail().getBatch().getItem().getGeneric().getProductCategory()
//									.getCategory()
//							: null,
//					detail.getUploadStockDetail().getUomType() != null
//							? detail.getUploadStockDetail().getUomType().getId()
//							: null,
//					detail.getUploadStockDetail().getUomType() != null
//							? detail.getUploadStockDetail().getUomType().getType()
//							: null,
//					detail.getUploadStockDetail().getUomUnit() != null
//							? detail.getUploadStockDetail().getUomUnit().getId()
//							: null,
//					detail.getUploadStockDetail().getUomUnit() != null
//							? detail.getUploadStockDetail().getUomUnit().getUnits()
//							: null,
//					detail.getUploadStockDetail().getBatch() != null ? detail.getUploadStockDetail().getBatch().getId()
//							: null,
//					detail.getUploadStockDetail().getBatch() != null
//							? detail.getUploadStockDetail().getBatch().getBatchNo()
//							: null,
//					detail.getUploadStockDetail().getBatch().getExpiryDate(), cop, mrp, availableQty, 
//					detail.getUploadStockDetail().getApprovedUploadQuantity() != null
//							? detail.getUploadStockDetail().getApprovedUploadQuantity()
//							: null,
//					detail.getStatus() != null ? detail.getStatus().getId() : null, 
//					detail.getStatus() != null ? detail.getStatus().getStatus() : null,
//					
//									
//					detail.getNewExpiryDate() != null ? detail.getNewExpiryDate() : null,
//					detail.getNewBatchNo() != null ? detail.getNewBatchNo() : null,
//					detail.getNewMrp() != null ? detail.getNewMrp() : null,
//					detail.getRemark() != null ? detail.getRemark() : null,
//				    detail.getRejectQuantity() != null ? detail.getRejectQuantity() : 0
//					));
//		}

	//	return response;
	//}

	
	@Override
	@Transactional
	public Object saveComputeStock(ComputeStockRequest request) {
		ComputeStock computeStock = ComputeStockDao.getComputeStock(request.getId());				
		List<Long> batchIds = ComputeStockDao.getBatchIds(request.getId());
		List previosItemIds = new ArrayList<>();	
		List<BatchResponse> batchList = null;
		
		for (ComputeStockDetail detail : computeStock.getComputeStockDetail())
		{
		     for(ComputeStockRequestItem items : request.getItems()){
		    	 
		    	 if (detail.getItemId().equals(items.getItemId()) || detail.getItemId() == items.getItemId() )  {
		    	 
			   	
			   		batchList = items.getBatches();	
				
//					if (items.getRejectQuantity() != null && items.getRejectQuantity() != 0) {
//						detail.setRejectQuantity(items.getRejectQuantity());
//						detail.setRejectReason(new RejectReason(items.getRejectReasonId()));
//						detail.setRejectNote(items.getRejectNote());
//						detail.setRemark(items.getRemark());
//						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//						detail.setUpdatedBy(detail.getUser());
//						detail.setvarianceAtCOP(items.getVarianceAtCOP());
//						detail.setvarianceAtMrp(items.getVarianceAtMrp());
//						detail.setOldMrp(items.getMrp());					
//						detail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
//					}
					for (BatchResponse batch : batchList) {
						if (batch.getId() != null && batch.getId().equals(detail.getId())) { //compute_stock_detail_id(detail)
							if (batch.getUploadedQuantity() != null) {
								detail.setApprovedQuantity(batch.getUploadedQuantity());
								detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							//	detail.setUpdatedBy(detail.getUser());	
								detail.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());	
								detail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));	
								
								if(batch.getRejectedQuantity() != null  && batch.getRejectedQuantity() > 0){
									detail.setRejectQuantity(batch.getRejectedQuantity());														
								}
							}
						}else if(batch.getId() == null && !batchIds.contains(batch.getBatchId())){
							UploadStockDetail uploadStockDetailOld = detail.getUploadStockDetail();					
							UploadStockDetail	uploadStockDetail = new UploadStockDetail();			
							/*uploadStockDetail.setAddedBy(new User(request.getUserId()));
							uploadStockDetail.setUpdatedBy(new User(request.getUserId()));*/
							uploadStockDetail.setAddedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
							uploadStockDetail.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());	
							uploadStockDetail.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							uploadStockDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							uploadStockDetail.setOrganizationId(request.getOrganizationId());
							uploadStockDetail.setUnitId(request.getUnitId());
						
						  //  sessionService.setEntityDefaults(uploadStockDetail);					
							uploadStockDetail.setBatch(new Batch(batch.getBatchId()));
							uploadStockDetail.setUomType(new UomType(batch.getUomTypeId()));
							uploadStockDetail.setUomUnit(new UomUnit(batch.getUomUnitId()));
						    uploadStockDetail.setUploadStock(new UploadStock(uploadStockDetailOld.getUploadStock().getId()));
						    uploadStockDetail.setQty(batch.getQuantity());
						    uploadStockDetail.setApprovedUploadQuantity(batch.getUploadedQuantity());
						    uploadStockDetail.setStatus(new Status(com.param.common.constants.Status.Approved.getStatus()));			   
					        uploadStockDetail = ComputeStockDao.saveUploadStockDetail(uploadStockDetail);
					        
					        ComputeStockDetail	computeStockDetailNew = new ComputeStockDetail();	
					      //  sessionService.setEntityDefaults(computeStockDetailNew);
					       /* computeStockDetailNew.setAddedBy(new User(request.getUserId()));
					        computeStockDetailNew.setUpdatedBy(new User(request.getUserId()));*/
					        computeStockDetailNew.setAddedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
					        computeStockDetailNew.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());	
					        computeStockDetailNew.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					        computeStockDetailNew.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					        computeStockDetailNew.setOrganizationId(request.getOrganizationId());
					        computeStockDetailNew.setUnitId(request.getUnitId());
						
					        computeStockDetailNew.setBatch(new Batch(batch.getBatchId()));
					        computeStockDetailNew.setItemId(items.getItemId());
					        computeStockDetailNew.setComputeStock(new ComputeStock(detail.getComputeStock().getId()));
					        computeStockDetailNew.setUploadStockDetail(uploadStockDetail);
					        computeStockDetailNew.setApprovedQuantity(batch.getUploadedQuantity());			
					        computeStockDetailNew.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));						
					        computeStockDetailNew.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					       // computeStockDetailNew.setAddedBy(computeStock.getUser());			
					        computeStockDetailNew.setAddedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
							ComputeStockDao.saveComputeStockDetail(computeStockDetailNew);
							
							batchIds.add(batch.getBatchId());
						}	
						
					} 		
					
				 }	
			  
			}
		
		 	
		} 

	 //	computeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
	//	computeStock.setUpdatedBy(computeStock.getUser());
		computeStock.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(), computeStock.getUnitId());
		ComputeStockDao.updateComputeStock(computeStock);
		
		CommonResponse response = new CommonResponse();
		response.setId(computeStock.getId().longValue());
		response.setMessage("Compute Stock saved successfully");

		return response;
	}
	
//	@Override
//	@Transactional
//	public Object saveComputeStock(ComputeStockRequest request) {
//		ComputeStock computeStock = ComputeStockDao.getComputeStock(request.getId());
//		//computeStock.setStatus(new Status(request.getStatusId()));
//					
//		List previosItemIds = new ArrayList<>();		
//		List<Integer> batchIds = ComputeStockDao.getBatchIds(request.getId());
//		
//		List<BatchResponse> batchList = null;
//		for (ComputeStockDetail detail : computeStock.getComputeStockDetail())
//		{			
//			for (ComputeStockRequestItem items : request.getItems()) {							
//			//	if (items.getId() != null && items.getId().equals(detail.getId())) {		
//				if(previosItemIds == null || !previosItemIds.contains(items.getItemId())){					
//					batchList = items.getBatches();	
//					
//					previosItemIds.add(items.getItemId());
//					
//					if (items.getRejectQuantity() != null && items.getRejectQuantity() != 0) {
//						detail.setRejectQuantity(items.getRejectQuantity());
//						detail.setRejectReason(new RejectReason(items.getRejectReasonId()));
//						detail.setRejectNote(items.getRejectNote());
//						detail.setRemark(items.getRemark());
//						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//						detail.setUpdatedBy(detail.getUser());
//						detail.setvarianceAtCOP(items.getVarianceAtCOP());
//						detail.setvarianceAtMrp(items.getVarianceAtMrp());
//						detail.setOldMrp(items.getMrp());
//						// detail.setApprovedQuantity(items.getApprovedQuantity());
//						detail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
//					}				
//					for (BatchResponse batch : items.getBatches()) {
//				      if(batchList.size() > 1 && batch.getId() == null && !batchIds.contains(batch.getBatchId())) {							   
//							UploadStockDetail uploadStockDetailOld = detail.getUploadStockDetail();					
//							UploadStockDetail	uploadStockDetail = new UploadStockDetail();			
//														
//						    sessionService.setEntityDefaults(uploadStockDetail);					
//							uploadStockDetail.setBatch(new Batch(batch.getBatchId()));
//							uploadStockDetail.setUomType(new UomType(batch.getUomTypeId()));
//							uploadStockDetail.setUomUnit(new UomUnit(batch.getUomUnitId()));
//						    uploadStockDetail.setUploadStock(new UploadStock(uploadStockDetailOld.getUploadStock().getId()));
//						    uploadStockDetail.setQty(batch.getQuantity());
//						    uploadStockDetail.setApprovedUploadQuantity(batch.getUploadedQuantity());
//						    uploadStockDetail.setStatus(new Status(com.param.common.constants.Status.Approved.getStatus()));			   
//					        uploadStockDetail = ComputeStockDao.saveUploadStockDetail(uploadStockDetail);
//					        
//					        ComputeStockDetail	computeStockDetailNew = new ComputeStockDetail();	
//					        sessionService.setEntityDefaults(computeStockDetailNew);
//					        computeStockDetailNew.setBatch(new Batch(batch.getBatchId()));
//					        computeStockDetailNew.setItemId(items.getItemId());
//					        computeStockDetailNew.setComputeStock(new ComputeStock(detail.getComputeStock().getId()));
//					        computeStockDetailNew.setUploadStockDetail(uploadStockDetail);
//					        computeStockDetailNew.setApprovedQuantity(batch.getUploadedQuantity());			
//					        computeStockDetailNew.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));						
//					        computeStockDetailNew.setAddedDate(TimeZoneComponent.getDateByzone(unitId));
//					        computeStockDetailNew.setAddedBy(computeStock.getUser());						
//							ComputeStockDao.saveComputeStockDetail(computeStockDetailNew);				
//
//						}	
//					}
//					for (BatchResponse batch : batchList) {
//						if (batch.getId() != null) {
//							if (batch.getUploadedQuantity() != null && batch.getUploadedQuantity() > 0) {
//								detail.setApprovedQuantity(batch.getUploadedQuantity());
//								detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//								detail.setUpdatedBy(detail.getUser());	
//								detail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
//							}
//						}					
//				   }									
//					
//				}				
//				
//			}	     
//			
//		}
//		computeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//		computeStock.setUpdatedBy(computeStock.getUser());
//		ComputeStockDao.updateComputeStock(computeStock);
//		
//		CommonResponse response = new CommonResponse();
//		response.setId(computeStock.getId().longValue());
//		response.setMessage("Compute Stock saved successfully");
//
//		return response;
//	}
//	
//	@Override
//	@Transactional
//	public Object saveComputeStock(ComputeStockRequest request) {
//		ComputeStock computeStock = ComputeStockDao.getComputeStock(request.getId());
//		//computeStock.setStatus(new Status(request.getStatusId())); //diilp
//
//		VarianceApproval varianceApproval = new VarianceApproval(
//				new Status(com.param.common.constants.Status.Pending.getStatus()),
//				new Store(computeStock.getStore().getStoreId()), new UploadStock(computeStock.getUploadStock().getId()),
//				new ComputeStock(computeStock.getId()));
//		sessionService.setEntityDefaults(varianceApproval);	
//
//		for (ComputeStockDetail detail : computeStock.getComputeStockDetail())
//		{
//			for (ComputeStockRequestItem items : request.getItems())
//			{
//				if (items.getId() != null && items.getId().equals(detail.getId())) {
//					if (items.getRejectQuantity() != null && items.getRejectQuantity() != 0) {
//						detail.setRejectQuantity(items.getRejectQuantity());
//						detail.setRejectReason(new RejectReason(items.getRejectReasonId()));
//						detail.setRejectNote(items.getRejectNote());
//						detail.setRemark(items.getRemark());
//						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//						detail.setUpdatedBy(detail.getUser());
//						detail.setvarianceAtCOP(items.getVarianceAtCOP()); 
//						detail.setvarianceAtMrp(items.getVarianceAtMrp()); 
//						detail.setNewBatchNo(items.getNewBatchNo()); 
//						detail.setNewExpiryDate(items.getNewExpiryDate()); 				
//						detail.setNewMrp(items.getNewMrp()); 
//						detail.setOldExpiryDate(items.getExpiryDate()); 
//						detail.setOldBatchNo(items.getBatchNo()); 
//						detail.setOldMrp(items.getMrp()); 
//						detail.setApprovedQuantity(items.getApprovedQuantity());
//				
//						varianceApproval.addVarianceApprovalDetail(
//								new VarianceApprovalDetail(new Batch(detail.getUploadStockDetail().getBatch().getId()),
//										new ComputeStockDetail(detail.getId()), new UploadStockDetail(detail.getUploadStockDetail().getId()) ));
//
//						ComputeStockDao.saveVarianceApproval(varianceApproval);
//
//					} else if (items.getApprovedQuantity() != null && items.getApprovedQuantity() > 0) {
//
//						detail.setApprovedQuantity(items.getApprovedQuantity());
//						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//						detail.setUpdatedBy(detail.getUser());
//						Batch batch = detail.getBatch();
//						if (items.getNewBatchNo() != null) {
//							detail.setNewBatchNo(items.getNewBatchNo());
//							batch.setBatchNo(items.getNewBatchNo());
//						}
//						if (items.getNewExpiryDate() != null) {
//							detail.setNewExpiryDate(items.getNewExpiryDate());
//							batch.setExpiryDate(items.getNewExpiryDate());
//						}
//						if (items.getNewMrp() != null) {
//							detail.setNewMrp(items.getNewMrp());
//							batch.setRate(items.getNewMrp());
//						}
//					}
//				}
//			}
//		}
//		computeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//		computeStock.setUpdatedBy(computeStock.getUser());
//
//		ComputeStockDao.updateComputeStock(computeStock);
//
//		CommonResponse response = new CommonResponse();
//		response.setId(computeStock.getId().longValue());
//		response.setMessage("Compute Stock saved successfully");
//
//		return response;
//	}
//	
	private Long conversionQuantity(Long qty, Integer uomTypeId, List<BatchUomMapper> uomList) {
		Long quantity = 0L;
		Double conversion = 1d;
		if (uomTypeId.equals(1)) {
			for (BatchUomMapper batchUomMapper : uomList) {
				conversion = conversion * batchUomMapper.getConversions();
			}
			quantity = (long) (qty / conversion);
		} else if (uomTypeId.equals(2)) {
			for (BatchUomMapper batchUomMapper : uomList) {
				if (batchUomMapper.getUomType().getId().equals(2) || batchUomMapper.getUomType().getId().equals(3)) {
					conversion = conversion * batchUomMapper.getConversions();
				}
			}
			quantity = (long) (qty / conversion);
		}

		else if (uomTypeId.equals(3)) {
			quantity = (long) (qty);
		}

		return quantity;

	}

}
