package com.param.inventory.stockAdjustments.service.confirmStock;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ConfirmStock;
import com.param.entity.model.inventory.ConfirmStockDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StopTransaction;
import com.param.entity.model.inventory.StopTransactionItemDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.entity.model.inventory.VarianceApprovalDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.inventory.stockAdjustments.dao.confirmStock.IConfirmStockDao;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.confirmStock.ConfirmStockDetailDto;
import com.param.inventory.stockAdjustments.dto.confirmStock.ConfirmStockDetailRequest;
import com.param.inventory.stockAdjustments.dto.confirmStock.ConfirmStockDto;
import com.param.inventory.stockAdjustments.dto.confirmStock.ConfirmStockRequestDto;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ConfirmStockService implements IConfirmStockService {

	private static final Logger logger = Logger.getLogger(ConfirmStockService.class);

	private Integer screenId = Screen.ConfirmStock.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IConfirmStockDao ConfirmStockDao;

	@Autowired
	SessionService sessionService;

	@Override
	@Transactional
	public StockAdjustmentDropdownResponse getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		StockAdjustmentDropdownResponse response = new StockAdjustmentDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getConfirmStockList(UploadStockRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ConfirmStockDao.searchConfirmStockList(request, screenId);
	}

	@Override
	@Transactional
	public Object getConfirmStock(Integer id, Integer unitId) {

		ConfirmStock confirmStock = ConfirmStockDao.getConfirmStock(id, unitId);

//		ConfirmStockDto response = new ConfirmStockDto(confirmStock.getId(), confirmStock.getVarianceApproval().getStore().getStoreId(), 
//				confirmStock.getVarianceApproval().getStore().getStoreName(), confirmStock.getVarianceApproval().getUploadStock().getStopTransaction().getStartDateTime(), 
//				confirmStock.getVarianceApproval().getUploadStock().getStopTransaction().getStopDateTime(), confirmStock.getComputeStock().getAddedBy().getUserName());

		ConfirmStockDto response = new ConfirmStockDto(confirmStock.getId(), confirmStock.getStore().getStoreId(),
				confirmStock.getStore().getStoreName(),
				confirmStock.getComputeStock().getUploadStock().getStopTransaction().getStartDateTime(),
				confirmStock.getComputeStock().getUploadStock().getStopTransaction().getStopDateTime(),
				confirmStock.getComputeStock().getAddedBy().getUserName());

		for (ConfirmStockDetail detail : confirmStock.getConfirmStockDetails()) {
			Integer storeStockAvailableQuantity = GlobalDao.getStoreStockByBatch(confirmStock.getStore().getStoreId(),
					detail.getBatch().getId(), 0);
			long AvailableQty = storeStockAvailableQuantity;
			// Double cop = detail.getBatch().getCop() * storeStockAvailableQuantity;
			// Double mrp = detail.getBatch().getRate() * storeStockAvailableQuantity;

			Double cop = detail.getBatch().getCop();
			Double mrp = detail.getBatch().getRate();

			response.addItems(new ConfirmStockDetailDto(detail.getId(), detail.getBatch().getItem().getId(),
					detail.getBatch().getItem().getName(), detail.getBatch().getItem().getCode(),
					detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(),
					detail.getComputeStockDetail().getUploadStockDetail().getUomType().getType(),
					detail.getComputeStockDetail().getUploadStockDetail().getUomUnit().getUnits(),
					detail.getBatch().getId(), detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(), cop,
					mrp, AvailableQty, detail.getComputeStockDetail().getUploadStockDetail().getQty(),
					detail.getComputeStockDetail().getNewExpiryDate(), detail.getComputeStockDetail().getNewMrp(),
					detail.getApprovalQuantity() != null ? detail.getApprovalQuantity() : 0,
					detail.getRejectQuantity() != null ? detail.getRejectQuantity() : 0,
					detail.getRejectNote() != null ? detail.getRejectNote() : null, detail.getStatus().getStatus()));

		}

//		for(ConfirmStockDetail detail : confirmStock.getConfirmStockDetails()) {
//			response.addItems(new ConfirmStockDetailDto(detail.getId(), detail.getBatch().getItem().getId(), detail.getBatch().getItem().getName(), detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(), 
//					detail.getVarianceApprovalDetail().getUploadStockDetail().getUomType().getType(), detail.getVarianceApprovalDetail().getUploadStockDetail().getUomUnit().getUnits(), 
//					detail.getBatch().getId(), detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(), detail.getBatch().getCop(), detail.getBatch().getRate(), getStoreStockQunatity(detail.getBatch().getStoreStockList()), 
//					detail.getVarianceApprovalDetail().getUploadStockDetail().getQty(), detail.getComputeStockDetail().getNewExpiryDate(), detail.getComputeStockDetail().getNewMrp(), 
//					detail.getApprovalQuantity() != null ? detail.getApprovalQuantity() : 0, detail.getRejectQuantity() !=null ? detail.getRejectQuantity() : 0, detail.getRejectReason().getId()!=null ? detail.getRejectReason().getId() : 0,
//				    detail.getRejectNote() != null ? detail.getRejectNote() : null));

//			for(ConfirmStockDetail detail : confirmStock.getConfirmStockDetails()) {
//				Integer storeStockAvailableQuantity = GlobalDao.getStoreStockByBatch(confirmStock.getVarianceApproval().getStore().getStoreId(), 
//						detail.getBatch().getId());	
//				long AvailableQty = storeStockAvailableQuantity;
//				
//				response.addItems(new ConfirmStockDetailDto(detail.getId(), detail.getBatch().getItem().getId(), detail.getBatch().getItem().getName(), detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(), 
//						detail.getVarianceApprovalDetail().getUploadStockDetail().getUomType().getType(), detail.getVarianceApprovalDetail().getUploadStockDetail().getUomUnit().getUnits(), 
//						detail.getBatch().getId(), detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(), detail.getBatch().getCop(), detail.getBatch().getRate(), AvailableQty, 
//						detail.getVarianceApprovalDetail().getUploadStockDetail().getQty(), detail.getComputeStockDetail().getNewExpiryDate(), detail.getComputeStockDetail().getNewMrp(), 
//						detail.getApprovalQuantity() != null ? detail.getApprovalQuantity() : 0, detail.getRejectQuantity() !=null ? detail.getRejectQuantity() : 0, 
//					    detail.getRejectNote() != null ? detail.getRejectNote() : null)); 
//			
//		}

		return response;
	}

	private Long getStoreStockQunatity(List<StoreStock> storeckList) {
		Long quantity = 0L;
		for (StoreStock storeStock : storeckList) {
			quantity = quantity + storeStock.getAvailableQty();
		}
		return quantity;
	}

	@Override
	@Transactional
	public Object approveConfirmStock(ConfirmStockRequestDto confirmStockRequest) {

		for (Integer id : confirmStockRequest.getConfirmStockIds()) {
			ConfirmStock confirmStock = ConfirmStockDao.getConfirmStock(id,
					confirmStockRequest.getUnitId() != null ? confirmStockRequest.getUnitId() : 0);

			confirmStock.setStatus(new Status(confirmStockRequest.getStatusId()));

			for (ConfirmStockDetail detail : confirmStock.getConfirmStockDetails()) {
				if (detail.getStatus().getId().equals(com.param.common.constants.Status.Rejected.getStatus())) {

				} else {
					detail.setStatus(new Status(confirmStockRequest.getStatusId()));

					// detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
					// detail.setUpdatedBy(detail.getUser());
					detail.setUpdateDefaults(confirmStockRequest.getUserId()!=null ? confirmStockRequest.getUserId() : sessionService.getUserId(), confirmStock.getUnitId());
				}
			}


			confirmStock.setApprovedDate(TimeZoneComponent.getDateByzone(confirmStock.getUnitId()));
			confirmStock.setApprovedBy(new User(confirmStockRequest.getUserId()!=null ? confirmStockRequest.getUserId() : sessionService.getUserId()));

			confirmStock.setUpdatedDate(TimeZoneComponent.getDateByzone(confirmStock.getUnitId()));
			confirmStock.setUpdatedByUser(confirmStockRequest.getUserId());

			ConfirmStockDao.updateConfirmStock(confirmStock);
			StopTransaction stopTransaction = confirmStock.getComputeStock().getUploadStock().getStopTransaction();
			stopTransaction.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
			ConfirmStockDao.updateStopTransaction(stopTransaction);

		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Confirm Stock approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelConfirmStock(ConfirmStockRequestDto confirmStockRequest) {

		ConfirmStock confirmStock = ConfirmStockDao.getConfirmStock(confirmStockRequest.getId(),
				confirmStockRequest.getUnitId() != null ? confirmStockRequest.getUnitId() : 0);

		for (ConfirmStockDetail detail : confirmStock.getConfirmStockDetails()) {
			detail.setStatus(new Status(confirmStockRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(confirmStock.getUnitId()));
			detail.setUpdatedByUser(confirmStockRequest.getUserId());
		}

		confirmStock.setCancelReason(new CancelReason(confirmStockRequest.getCancelReasonId()));
		confirmStock.setCancelNote(confirmStockRequest.getCancelNote());

		confirmStock.setStatus(new Status(confirmStockRequest.getStatusId()));

		confirmStock.setUpdatedDate(TimeZoneComponent.getDateByzone(confirmStock.getUnitId()));
		//confirmStock.setUpdatedBy(confirmStock.getUser());
		confirmStock.setUpdatedByUser(confirmStockRequest.getUserId());
		ConfirmStockDao.updateConfirmStock(confirmStock);

		CommonResponse response = new CommonResponse();
		response.setMessage("Confirm Stock cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object detailApproveConfirmStock(ConfirmStockRequestDto confirmStockRequest) {

		ConfirmStock confirmStock = ConfirmStockDao.getConfirmStock(confirmStockRequest.getId(),
				confirmStockRequest.getUnitId() != null && confirmStockRequest.getUnitId() > 0
						? confirmStockRequest.getUnitId()
						: 0);

		for (ConfirmStockDetail detail : confirmStock.getConfirmStockDetails()) {

			for (ConfirmStockDetailRequest item : confirmStockRequest.getItems()) {
				if (item.getId().equals(detail.getId())) {
					detail.setStatus(new Status(confirmStockRequest.getStatusId()));
					detail.setApprovalQuantity(item.getApprovalQunatity());
					detail.setRejectQuantity(item.getRejectQuantity());
					detail.setRejectReason(new RejectReason(item.getRejectReasonId()));
					detail.setRejectNote(item.getRejectNote());
                    detail.setUpdatedDate(TimeZoneComponent.getDateByzone(confirmStock.getUnitId()));
					// detail.setUpdatedBy(confirmStock.getUser());
					detail.setUpdatedByUser(confirmStockRequest.getUserId() != null ? confirmStockRequest.getUserId()
							: sessionService.getUserId());

					/*
					 * updateStoreStockQuantity(confirmStock.getStore().getStoreId(),
					 * detail.getBatch().getId(),
					 * item.getApprovalQunatity(),item.getRejectQuantity(),confirmStockRequest.
					 * getStoreId(),confirmStockRequest.getUnitId()!=null ?
					 * confirmStockRequest.getUnitId() : sessionService.getUnitId() ,
					 * confirmStockRequest.getOrganizationId()!=null ?
					 * confirmStockRequest.getOrganizationId() :sessionService.getOrganizationId() ,
					 * confirmStockRequest.getUserId()!=null ? confirmStockRequest.getUserId() :
					 * sessionService.getUserId());
					 */ updateStoreStockQuantity1(confirmStock.getStore().getStoreId(), detail.getBatch().getId(),
							item.getApprovalQunatity(), item.getRejectQuantity(), confirmStockRequest.getStoreId(),
							confirmStockRequest.getUnitId() != null ? confirmStockRequest.getUnitId()
									: sessionService.getUnitId(),
							confirmStockRequest.getOrganizationId() != null ? confirmStockRequest.getOrganizationId()
									: sessionService.getOrganizationId(),
							confirmStockRequest.getUserId() != null ? confirmStockRequest.getUserId()
									: sessionService.getUserId(),
							detail.getComputeStockDetail().getItemId(), detail);

					openItemForTransaction(item, detail);
				}
			}
		}

		confirmStock.setStatus(new Status(confirmStockRequest.getStatusId()));

		//confirmStock.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		//confirmStock.setUpdatedBy(confirmStock.getUser());
		confirmStock.setUpdateDefaults(confirmStockRequest.getUserId()!=null ? confirmStockRequest.getUserId() : sessionService.getUserId(),confirmStock.getUnitId());
		ConfirmStockDao.updateConfirmStock(confirmStock);
		StopTransaction stopTransaction = confirmStock.getComputeStock().getUploadStock().getStopTransaction();
		stopTransaction.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
		ConfirmStockDao.updateStopTransaction(stopTransaction);
		CommonResponse response = new CommonResponse();
		response.setMessage("Confirm Stock Approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object RejectConfirmStock(Integer confirmStockDetailId, Integer unitId, Integer statusId) {
		int result = 0;
		try {
			ConfirmStockDetail confirmStockDetail = ConfirmStockDao.getConfirmDetailStock(confirmStockDetailId, unitId);
			confirmStockDetail.setStatus(new Status(com.param.common.constants.Status.Rejected.getStatus()));
			confirmStockDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			confirmStockDetail.getConfirmStock().setStatus(new Status(statusId));
			confirmStockDetail = ConfirmStockDao.updateConfirStockDetail(confirmStockDetail);

			VarianceApprovalDetail varianceApprovalDetail = ConfirmStockDao
					.getvarianceApprovalDetail(confirmStockDetail.getComputeStockDetail().getId(), unitId);
			varianceApprovalDetail.setIsDeleted(true);
			varianceApprovalDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			ConfirmStockDao.updateVarianceApprovalDetail(varianceApprovalDetail);

			UploadStockDetail uploadStockDetail = ConfirmStockDao.getUploadDetailStock(
					confirmStockDetail.getComputeStockDetail().getUploadStockDetail().getId(), unitId);
			uploadStockDetail.setStatus(new Status(com.param.common.constants.Status.Rejected.getStatus()));
			uploadStockDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			ConfirmStockDao.updateUploadStockDetail(uploadStockDetail);

		} catch (Exception e) {
			logger.error("RejectConfirmStock failed", e);
			throw new ServiceException(e.getMessage());
		}
		return result;
	}

	@Transactional
	private void updateStoreStockQuantity(Integer storeId, Long batchId, Integer uploadedQuantity,
			Integer rejectedQuantity, Integer cookieStorId, Integer unitId, Integer orgId, Integer userId) {
		try {

			if (uploadedQuantity != null) {
				StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batchId, storeId, unitId);
				StoreStock storeStockDetail = null;
				if (storeStockList == null) {
					StoreStock storeStock = new StoreStock(uploadedQuantity, new Store(storeId), new Batch(batchId));
					storeStock.setCreateDefaults(orgId, unitId, userId);
					storeStock.setTotalQty(uploadedQuantity);
					storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
				} else {
					storeStockList.setAvailableQty(uploadedQuantity);
					storeStockList.setTotalQty(uploadedQuantity);
					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
				}

				// updateBatchQuantity(storeId,batchId,storeStockDetail,uploadedQuantity,rejectedQuantity,
				// orgId, unitId, userId,0) ;
			}
		} catch (Exception e) {
			logger.error("updateStoreStockQuantity failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Transactional
	private void updateStoreStockQuantity1(Integer storeId, Long batchId, Integer uploadedQuantity,
			Integer rejectedQuantity, Integer cookieStorId, Integer unitId, Integer orgId, Integer userId,
			Integer itemId, ConfirmStockDetail d) {
		try {

			if (uploadedQuantity != null) {
				StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStoreItemId(batchId, storeId, unitId,
						itemId);
				StoreStock storeStockDetail = null;
				if (storeStockList == null) {
					StoreStock storeStock = new StoreStock(uploadedQuantity, new Store(storeId), new Batch(batchId));
					storeStock.setTotalQty(uploadedQuantity);
					storeStock.setCreateDefaults(orgId, unitId, userId);
					// storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
				} else {
					storeStockList.setAvailableQty(uploadedQuantity);
					storeStockList.setTotalQty(uploadedQuantity);
					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
					updateBatchQuantity(storeId, batchId, storeStockDetail, uploadedQuantity, rejectedQuantity, orgId,
							unitId, userId, d);
				}

			}
		} catch (Exception e) {
			logger.error("updateStoreStockQuantity1 failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Transactional
	private void updateBatchQuantity(Integer storeId, Long batchId, StoreStock storeStockDetail,
			Integer uploadedQuantity, Integer rejectedQuantity, Integer orgId, Integer unitId, Integer userid,
			ConfirmStockDetail d) {
		try {
			Integer currentQuantity;
			Batch batch = ConfirmStockDao.getBatch(batchId);

//			batch.setQuantity(batch.getQuantity() + uploadedQuantity);
//			batch.setCurrentQuantity(batch.getCurrentQuantity() + uploadedQuantity);

//			Integer OtherStoreQty = ConfirmStockDao.getStoreStockForByBatchForOtherStore(storeId,batchId);
//			
//			if(OtherStoreQty > 0){
//				Integer batchCurrentQuantity =  OtherStoreQty + uploadedQuantity;
//			    Integer BatchQuanity = batch.getQuantity()!= null ? batch.getQuantity() : 0;
//			    
//				if(batchCurrentQuantity > BatchQuanity){
//					batch.setCurrentQuantity(batchCurrentQuantity);
//					Integer rejectedQty = batchCurrentQuantity - BatchQuanity;
//					batch.setRejectedQuantity(rejectedQty);
//				}else if(batchCurrentQuantity < BatchQuanity) {
//					batch.setCurrentQuantity(batchCurrentQuantity);									
//				}
//			}else{			
//				batch.setCurrentQuantity(uploadedQuantity);
//				if(rejectedQuantity > 0){					
//					Integer currentRejectedQty = batch.getRejectedQuantity() != null ? batch.getRejectedQuantity() : 0;					
//					batch.setRejectedQuantity(rejectedQuantity +  currentRejectedQty);
//				}
//			}	

			batch.setCurrentQuantity(uploadedQuantity);
			batch.setQuantity(uploadedQuantity);

			if (rejectedQuantity > 0) {
				Integer currentRejectedQty = batch.getRejectedQuantity() != null ? batch.getRejectedQuantity() : 0;
				batch.setRejectedQuantity(rejectedQuantity + currentRejectedQty);
			}
			currentQuantity = Math.abs(batch.getCurrentQuantity() - uploadedQuantity);

			GlobalDao.updateBatchLeaseQuantity(batch);
			saveStockTransaction(currentQuantity, storeStockDetail, orgId, unitId, userid, d);

		} catch (Exception ex) {
			logger.error("updateBatchQuantity failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@Transactional
	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, Integer orgId, Integer unitid,
			Integer userId, ConfirmStockDetail d) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()), d.getId());
		stockTransaction.setCreateDefaults(orgId, unitid, userId);
		GlobalDao.saveStockTransaction(stockTransaction);
	}

	@Transactional
	private void openItemForTransaction(ConfirmStockDetailRequest item, ConfirmStockDetail detail) {
		try {
			StopTransactionItemDetail stopTransactionItemDetail = ConfirmStockDao.getStopTransactionItemDetail(
					detail.getComputeStockDetail().getUploadStockDetail().getUploadStock().getStopTransaction().getId(),
					detail.getBatch().getId());
			stopTransactionItemDetail.setIsDeleted(true);
			stopTransactionItemDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(stopTransactionItemDetail.getUnitId()));
			ConfirmStockDao.updateStopTransactionItemDetail(stopTransactionItemDetail);
		}catch(Exception ex){
			logger.error("openItemForTransaction failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
