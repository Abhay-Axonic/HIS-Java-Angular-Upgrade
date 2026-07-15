package com.param.inventory.stockAdjustments.service.varianceApproval;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.VarianceApproval;
import com.param.entity.model.inventory.VarianceApprovalDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.global.dao.global.IGlobalDao;
import com.param.inventory.stockAdjustments.dao.varianceApproval.IVarianceApprovalDao;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponseItem;
import com.param.inventory.stockAdjustments.dto.varianceApproval.BatchResponse;
import com.param.inventory.stockAdjustments.service.uploadStock.IUploadStockService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class VarianceApprovalService implements IVarianceApprovalService {
	

@Autowired
private SessionService sessionService;

	private Integer screenId = Screen.VarianceApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IVarianceApprovalDao VarianceApprovalDao;
	
	@Autowired
	IUploadStockService UploadStockService;

	@Override
	@Transactional
	public StockAdjustmentDropdownResponse getDropdownList(Integer unitId, Integer orgId, Integer userId) {

		StockAdjustmentDropdownResponse response = new StockAdjustmentDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
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
	public Object getVarianceApprovalList(UploadStockRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return VarianceApprovalDao.searchVarianceApprovalList(request, screenId);
	}

	@Override
	@Transactional
	public Object cancelVarianceApproval(UploadStockRequest request) {

		VarianceApproval varianceApproval = VarianceApprovalDao.getVarianceApproval(request.getId(),request.getUnitId()!=null ? request.getUnitId() :0);

		for (VarianceApprovalDetail detail : varianceApproval.getVarianceApprovalDetails()) {

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(varianceApproval.getUnitId()));
		//	detail.setUpdatedBy(varianceApproval.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		varianceApproval.setCancelReason(new CancelReason(request.getCancelReasonId()));
		varianceApproval.setCancelNote(request.getCancelNote());

		varianceApproval.setStatus(new Status(request.getStatusId()));

		//varianceApproval.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		//varianceApproval.setUpdatedBy(varianceApproval.getUser());
		varianceApproval.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(), varianceApproval.getUnitId());
		VarianceApprovalDao.updateVarianceApproval(varianceApproval);

		CommonResponse response = new CommonResponse();
		response.setMessage("Variance Approval cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getVarianceApprovalDetails(Integer id ,  Integer unitId) {

		VarianceApproval varianceApproval = VarianceApprovalDao.getVarianceApproval(id, unitId);
		UploadStockResponse response = new UploadStockResponse(varianceApproval.getId(),
				varianceApproval.getUploadStock().getStopTransaction().getRequestNo(),
				varianceApproval.getUploadStock().getStopTransaction().getAddedDate(),
				varianceApproval.getUploadStock().getStopTransaction().getStartDateTime(),
				varianceApproval.getUploadStock().getStopTransaction().getStopDateTime(),
				varianceApproval.getStore().getStoreName(), varianceApproval.getStore().getStoreId(),
				varianceApproval.getStatus().getStatus(), varianceApproval.getStatus().getId(),
				varianceApproval.getUploadStock().getAddedBy().getUserName());

		List<UploadStockResponseItem> items = new ArrayList<UploadStockResponseItem>();
		for (VarianceApprovalDetail detail : varianceApproval.getVarianceApprovalDetails()) {
		//	if(detail.getIsDeleted() == false){
			Long totalQty = 0L;
			Double totalCop = 0d;
			Double totalMrp = 0d;		  
			Integer storeStockAvailableQuantity = GlobalDao.getStoreStockByBatch(varianceApproval.getStore().getStoreId(),
					detail.getBatch().getId());	
			
//			UploadStockResponseItem itemDetails = new UploadStockResponseItem(detail.getId(),
//					detail.getBatch().getItem().getGeneric().getProductCategory().getId(),
//					detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(),
//					detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
//					detail.getBatch().getItem().getName(),detail.getBatch().getId(),
//					detail.getBatch().getBatchNo(),detail.getBatch().getRate(),detail.getBatch().getCop(),
//					storeStockAvailableQuantity,detail.getUploadStockDetail().getApprovedUploadQuantity());
//			
			UploadStockResponseItem itemDetails = new UploadStockResponseItem(detail.getId(),
					detail.getBatch().getItem().getGeneric().getProductCategory().getId(),
					detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(),
					detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
					detail.getBatch().getItem().getName(),detail.getBatch().getId(),
					detail.getBatch().getBatchNo(),detail.getBatch().getRate(),detail.getBatch().getCop(),
					detail.getUploadStockDetail().getQty(),detail.getUploadStockDetail().getApprovedUploadQuantity());

	
			Long quantity = 0L;
			Integer uomTypeId = 0;
			Batch batch = VarianceApprovalDao.getQuantity(varianceApproval.getStore().getStoreId(),
					detail.getBatch().getId() , unitId);	

			List<BatchResponse> batchList = new ArrayList<BatchResponse>();

			BatchResponse batchResponse = new BatchResponse();
			for (StoreStock storeStock : batch.getStoreStockList()) {

				batchResponse.setBatchId(batch.getId());
				batchResponse.setBatchNo(batch.getBatchNo());

				quantity = quantity + storeStock.getAvailableQty();
			}			
			
			if (detail.getUploadStockDetail() != null) {
				if (detail.getUploadStockDetail().getBatch().getId().equals(batch.getId())) {
					batchResponse.setUomTypeId(detail.getUploadStockDetail().getUomType().getId());
					batchResponse.setUom(detail.getUploadStockDetail().getUomType().getType());
					batchResponse.setUomUnitId(detail.getUploadStockDetail().getUomUnit().getId());
					batchResponse.setUomUnit(detail.getUploadStockDetail().getUomUnit().getUnits());
					batchResponse.setUploadedQuantity(detail.getUploadStockDetail().getApprovedUploadQuantity());
				}
			}	

				totalCop = totalCop + (batch.getCop() != null ? batch.getCop() : 1);
				totalMrp = totalMrp + (batch.getRate() != null ? batch.getRate() : 1);
			
				//long AvialableQuantity  = storeStockAvailableQuantity;
				long AvialableQuantity  = detail.getUploadStockDetail().getQty();
				batchResponse.setCop((batch.getCop() != null ? batch.getCop() : 1) * (AvialableQuantity > 0 ? AvialableQuantity : 0));
				batchResponse.setMrp((batch.getRate() != null ? batch.getRate() : 1) * (AvialableQuantity > 0 ? AvialableQuantity : 0));
				batchResponse.setAvailableQuantity(AvialableQuantity > 0 ? AvialableQuantity : 0);
				batchResponse.setExpiryDate(batch.getExpiryDate());
				batchList.add(batchResponse);

				totalQty = totalQty + AvialableQuantity;


			itemDetails.setBatches(batchList);

			itemDetails.setQty(totalQty > 0 ? totalQty : 0);
			itemDetails.setCop(totalCop);
			itemDetails.setMrp(totalMrp);
			items.add(itemDetails);
		//  }
		}
		
		response.setItems(items);
		return response;
	}

	
//	@Override
//	@Transactional
//	public Object getVarianceApprovalDetails(Integer id) {
//
//		VarianceApproval varianceApproval = VarianceApprovalDao.getVarianceApproval(id);
//		UploadStockResponse response = new UploadStockResponse(varianceApproval.getId(),
//				varianceApproval.getUploadStock().getStopTransaction().getRequestNo(),
//				varianceApproval.getUploadStock().getStopTransaction().getAddedDate(),
//				varianceApproval.getUploadStock().getStopTransaction().getStartDateTime(),
//				varianceApproval.getUploadStock().getStopTransaction().getStopDateTime(),
//				varianceApproval.getStore().getStoreName(), varianceApproval.getStore().getStoreId(),
//				varianceApproval.getStatus().getStatus(), varianceApproval.getStatus().getId(),
//				varianceApproval.getUploadStock().getAddedBy().getUserName());
//
//		List<UploadStockResponseItem> items = new ArrayList<UploadStockResponseItem>();
//		for (VarianceApprovalDetail detail : varianceApproval.getVarianceApprovalDetails()) {
//			if(detail.getIsDeleted() == false){
//			Long totalQty = 0L;
//			Double totalCop = 0d;
//			Double totalMrp = 0d;		  
//			Integer storeStockAvailableQuantity = GlobalDao.getStoreStockByBatch(varianceApproval.getStore().getStoreId(),
//					detail.getBatch().getId());	
//			
//			UploadStockResponseItem itemDetails = new UploadStockResponseItem(detail.getId(),
//					detail.getBatch().getItem().getGeneric().getProductCategory().getId(),
//					detail.getBatch().getItem().getGeneric().getProductCategory().getCategory(),
//					detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
//					detail.getBatch().getItem().getName(),detail.getBatch().getId(),
//					detail.getBatch().getBatchNo(),detail.getBatch().getRate(),detail.getBatch().getCop(),
//					storeStockAvailableQuantity,detail.getUploadStockDetail().getApprovedUploadQuantity());
//
//			// UploadStockResponseItem itemDto = new UploadStockResponseItem();
//			Long quantity = 0L;
//			Integer uomTypeId = 0;
//			Batch batch = VarianceApprovalDao.getQuantity(varianceApproval.getStore().getStoreId(),
//					detail.getBatch().getId());
//			// if (item.getBatchList() != null) {
//
//			List<BatchResponse> batchList = new ArrayList<BatchResponse>();
//
//			// for (Batch batch : item.getBatchList()) {
//
//			BatchResponse batchResponse = new BatchResponse();
//			for (StoreStock storeStock : batch.getStoreStockList()) {
//
//				batchResponse.setBatchId(batch.getId());
//				batchResponse.setBatchNo(batch.getBatchNo());
//
//				quantity = quantity + storeStock.getAvailableQty();
//
//			}
//			for (BatchUomMapper mapper : batch.getBatchUomList()) {
//				if (mapper.getStoreUom().equals(true)) {
//					uomTypeId = mapper.getUomType().getId();
//					break;
//				}
//			}
//			
//			if (detail.getUploadStockDetail() != null) {
//				if (detail.getUploadStockDetail().getBatch().getId().equals(batch.getId())) {
//					batchResponse.setUomTypeId(detail.getUploadStockDetail().getUomType().getId());
//					batchResponse.setUom(detail.getUploadStockDetail().getUomType().getType());
//					batchResponse.setUomUnitId(detail.getUploadStockDetail().getUomUnit().getId());
//					batchResponse.setUomUnit(detail.getUploadStockDetail().getUomUnit().getUnits());
//					batchResponse.setUploadedQuantity(detail.getUploadStockDetail().getApprovedUploadQuantity());
//				}
//			}
//			
////			if (!uomTypeId.equals(0)) {
////				long conversionQuantity = conversionQuantity(quantity, uomTypeId, batch.getBatchUomList());
////				batchResponse.setCop((batch.getCop() != null ? batch.getCop() : 1) * (quantity > 0 ? quantity : 0));
////				batchResponse.setMrp((batch.getRate() != null ? batch.getRate() : 1) * (quantity > 0 ? quantity : 0));
////				batchResponse.setAvailableQuantity(conversionQuantity > 0 ? conversionQuantity : 0);
////				batchResponse.setExpiryDate(batch.getExpiryDate());
////				batchList.add(batchResponse);
////
////				totalQty = totalQty + conversionQuantity;
////				totalCop = totalCop + (batch.getCop() != null ? batch.getCop() : 1) * quantity;
////				totalMrp = totalMrp + (batch.getRate() != null ? batch.getRate() : 1) * quantity;
////			}
//			
//			
////			if (!uomTypeId.equals(0)) {
//
//				totalCop = totalCop + (batch.getCop() != null ? batch.getCop() : 1);
//				totalMrp = totalMrp + (batch.getRate() != null ? batch.getRate() : 1);
//			
//				long AvialableQuantity  = storeStockAvailableQuantity;
//				batchResponse.setCop((batch.getCop() != null ? batch.getCop() : 1) * (quantity > 0 ? quantity : 0));
//				batchResponse.setMrp((batch.getRate() != null ? batch.getRate() : 1) * (quantity > 0 ? quantity : 0));
//				batchResponse.setAvailableQuantity(AvialableQuantity > 0 ? AvialableQuantity : 0);
//				batchResponse.setExpiryDate(batch.getExpiryDate());
//				batchList.add(batchResponse);
//
//				totalQty = totalQty + AvialableQuantity;
////				totalCop = totalCop + (batch.getCop() != null ? batch.getCop() : 1) * quantity;
////				totalMrp = totalMrp + (batch.getRate() != null ? batch.getRate() : 1) * quantity;
//		
////			} 
//
//			itemDetails.setBatches(batchList);
//
//			itemDetails.setQty(totalQty > 0 ? totalQty : 0);
//			itemDetails.setCop(totalQty > 0 ? totalCop : 0);
//			itemDetails.setMrp(totalQty > 0 ? totalMrp : 0);
//			items.add(itemDetails);
//		  }
//		}
//		
//		response.setItems(items);
//		return response;
//	}

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

	@Override
	@Transactional
	public Object approveVarianceApproval(UploadStockRequest request) {

		VarianceApproval varianceApproval = VarianceApprovalDao.getVarianceApproval(request.getId() , request.getUnitId()!=null ? request.getUnitId() :0);		
		
		varianceApproval.setStatus(new Status(request.getStatusId()));
		//varianceApproval.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		//varianceApproval.setUpdatedBy(varianceApproval.getUser());
		varianceApproval.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(),varianceApproval.getUnitId());
	    VarianceApprovalDao.updateVarianceApproval(varianceApproval);		

		CommonResponse response = new CommonResponse();
		response.setId(varianceApproval.getId().longValue());
		response.setMessage("variance Approval Updated successfully");
		return response;
	}
//	@Override
//	@Transactional
//	public Object approveVarianceApproval(UploadStockRequest request) {
//
//		VarianceApproval varianceApproval = VarianceApprovalDao.getVarianceApproval(request.getId());
//		
//		String stockAdjustmentNo = UploadStockService.getPrefixRunningNumber(request.getStoreId(), sessionService.getUnitId(), screenId);	
//		
//		ConfirmStock confirmStock = new ConfirmStock(new ComputeStock(varianceApproval.getComputeStock().getId()),
//				stockAdjustmentNo, new VarianceApproval(varianceApproval.getId()), new Status(com.param.common.constants.Status.Pending.getStatus()),new Store(varianceApproval.getStore().getStoreId()));  
//		sessionService.setEntityDefaults(confirmStock);	
//		for (VarianceApprovalDetail detail : varianceApproval.getVarianceApprovalDetails())
//		{
//			for (UploadStockRequestItem items : request.getItems())
//			{
//				if (items.getId() != null && items.getId().equals(detail.getId()))
//				{
//					ConfirmStockDetail confirmStockDetail=	new ConfirmStockDetail(new ComputeStockDetail(detail.getComputeStockDetail().getId()),
//							new VarianceApprovalDetail(detail.getId()), new Batch(detail.getBatch().getId()));
//					sessionService.setEntityDefaults(confirmStockDetail);
//					confirmStock.addConfirmStockDetail(confirmStockDetail);
//				}
//
//				detail.setIsApproved(items.getIsApproved());
//				detail.setRejectReason(items.getRejectReasonId()!=null ? new RejectReason(items.getRejectReasonId()) : null);
////				 detail.setStatus(new Status(items.getStatusId()));
//				detail.setRejectNote(items.getRejectNote());
//				//detail.setRejectReason(new RejectReason(items.getRejectReasonId())); 
//				/*
//				 * detail.setIsApproved(items.getIsApproved()); detail.setRejectReason(new
//				 * RejectReason(items.getRejectReasonId())); // detail.setStatus(new
//				 * Status(items.getStatusId())); detail.setRejectNote(items.getRejectNote());
//				 */
//			}
//		}
//		varianceApproval.setStatus(new Status(request.getStatusId()));
//		varianceApproval.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//		varianceApproval.setUpdatedBy(varianceApproval.getUser());
//
//	    //VarianceApprovalDao.updateVarianceApproval(varianceApproval);
//		VarianceApprovalDao.saveVarianceApproval(confirmStock);
//
//		CommonResponse response = new CommonResponse();
//		response.setId(varianceApproval.getId().longValue());
//		response.setMessage("variance Approval Updated successfully");
//		return response;
//	}
}
