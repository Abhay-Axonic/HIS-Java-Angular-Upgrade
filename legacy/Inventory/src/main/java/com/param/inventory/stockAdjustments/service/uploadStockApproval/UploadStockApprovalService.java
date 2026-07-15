package com.param.inventory.stockAdjustments.service.uploadStockApproval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ComputeStock;
import com.param.entity.model.inventory.ComputeStockDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.UploadStock;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.User;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.utility.Util;
import com.param.inventory.stockAdjustments.dao.confirmStock.IConfirmStockDao;
import com.param.inventory.stockAdjustments.dao.uploadStock.IUploadStockDao;
import com.param.inventory.stockAdjustments.dao.uploadStockApproval.IUploadStockApprovalDao;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequestItem;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponseItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class UploadStockApprovalService implements IUploadStockApprovalService {

	private static final Logger logger = Logger.getLogger(UploadStockApprovalService.class);

	private Integer screenId = Screen.UploadStockApproval.getScreen();
	

@Autowired
private SessionService sessionService;


	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IUploadStockApprovalDao UploadStockApprovalDao;

	@Autowired
	IUploadStockDao UploadStockDao;
	
	@Autowired
	IConfirmStockDao ConfirmStockDao;

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
	public Object getUploadStockApprovalList(UploadStockRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return UploadStockApprovalDao.searchUploadStockApprovalList(request, screenId);
	}

	@Override
	@Transactional
	public Object approveUploadStockApproval(UploadStockRequest request) {

		UploadStock uploadStock = UploadStockDao.getUploadStock(request.getId());

		ComputeStock computeStock = new ComputeStock(uploadStock,
				new Status(com.param.common.constants.Status.Pending.getStatus()), new Store(uploadStock.getStore().getStoreId()));
		//sessionService.setEntityDefaults(computeStock);	
		/*computeStock.setAddedBy(new User(request.getUserId()));
		computeStock.setUpdatedBy(new User(request.getUserId()));*/
		computeStock.setAddedByUser(request.getUserId());
		computeStock.setUpdatedByUser(request.getUserId());
		computeStock.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		computeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		computeStock.setOrganizationId(request.getOrganizationId());
		computeStock.setUnitId(request.getUnitId());

		for (UploadStockDetail detail : UploadStockDao.getUploadStockDetail(request.getId())) {

			for (UploadStockRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) 
				{
					ComputeStockDetail computeStockDetail=new ComputeStockDetail(detail, new Batch(detail.getBatch().getId()),detail.getBatch().getItem().getId());					 
					//sessionService.setEntityDefaults(computeStockDetail);	
					/*computeStockDetail.setAddedBy(new User(request.getUserId()));
					computeStockDetail.setUpdatedBy(new User(request.getUserId()));*/
					computeStockDetail.setAddedByUser(request.getUserId());
					computeStockDetail.setUpdatedByUser(request.getUserId());
					computeStockDetail.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					computeStockDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					computeStockDetail.setOrganizationId(request.getOrganizationId());
					computeStockDetail.setUnitId(request.getUnitId());
					//computeStockDetail.setItemId(detail.getBatch().getItem().getId());
					computeStock.addComputeStockDetail(computeStockDetail);
					computeStockDetail.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
					//computeStockDetail.setApprovedQuantity(detail.getApprovedUploadQuantity());
					computeStockDetail.setApprovedQuantity(item.getUploadedQuantity());
					detail.approveUploadStockDetail(item.getUploadedQuantity(), new Status(item.getStatusId()),
							TimeZoneComponent.getDateByzone(request.getUnitId()), new User(request.getUserId()),  item.getVarianceQty(), item.getVarianceCop() ,item.getVarianceMrp());
				}
			}
		}
		uploadStock.setStatus(new Status(request.getStatusId()));
		uploadStock.setApprovalStatus(new Status(request.getApprovalStatusId()));

		uploadStock.setApprovedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
	//	uploadStock.setApprovedBy(uploadStock.getUser());

		uploadStock.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
	//	uploadStock.setUpdatedBy(uploadStock.getUser());

		uploadStock.setAddedByUser(request.getUserId());
		uploadStock.setUpdatedByUser(request.getUserId());
		
		UploadStockDao.updateUploadStock(uploadStock); 
		UploadStockDao.saveComputeStock(computeStock);

		CommonResponse response = new CommonResponse();
		response.setMessage("Upload stock quantity approved successfully");

		return response;
	}

	
	
	
	@Override
	@Transactional
	public Object approveUploadStockNewApproval(UploadStockRequest request) {

		UploadStock uploadStock = UploadStockDao.getUploadStock(request.getId());


		for (UploadStockDetail detail : UploadStockDao.getUploadStockDetail(request.getId())) {

			for (UploadStockRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) 
				{
					Integer leaseQuantity = 0;
					if(detail.getUomType() == null) {
						 leaseQuantity = item.getUploadedQuantity();
					}else {
						 leaseQuantity = Util.GetLeaseQuantity(item.getUploadedQuantity(), detail.getBatch().getBatchUomList(),
								new UomType(detail.getUomType().getId()));
					}
					
				
					//computeStockDetail.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
					//computeStockDetail.setApprovedQuantity(detail.getApprovedUploadQuantity());
				//	computeStockDetail.setApprovedQuantity(item.getUploadedQuantity());
					detail.approveUploadStockDetail(item.getUploadedQuantity(), new Status(item.getStatusId()),
							TimeZoneComponent.getDateByzone(uploadStock.getUnitId()), new User(request.getUserId()),  item.getVarianceQty(), item.getVarianceCop() ,item.getVarianceMrp());
					
					updateStoreStockQuantity1(detail,uploadStock.getStore().getStoreId(), detail.getBatch().getId(), leaseQuantity,0,uploadStock.getStore().getStoreId(),request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId() , 
							request.getOrganizationId()!=null ? request.getOrganizationId() :sessionService.getOrganizationId() , request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(),  detail.getBatch().getItem().getId());
				 
				}
			}
		}
		uploadStock.setStatus(new Status(request.getStatusId()));
		uploadStock.setApprovalStatus(new Status(request.getApprovalStatusId()));

		uploadStock.setApprovedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
	//	uploadStock.setApprovedBy(uploadStock.getUser());

		uploadStock.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
	//	uploadStock.setUpdatedBy(uploadStock.getUser());

		uploadStock.setAddedByUser(request.getUserId());
		uploadStock.setUpdatedByUser(request.getUserId());
		
		UploadStockDao.updateUploadStock(uploadStock); 
		//UploadStockDao.saveComputeStock(computeStock);

		CommonResponse response = new CommonResponse();
		response.setMessage("Upload stock quantity approved successfully");

		return response;
	}
	
	
	
	@Transactional
	private void updateStoreStockQuantity1(UploadStockDetail detail, Integer storeId, Long batchId, Integer uploadedQuantity,Integer rejectedQuantity, Integer cookieStorId, Integer unitId , Integer orgId, Integer userId , Integer itemId){
		try{
						
			if(uploadedQuantity != null){		
				StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStoreItemId(batchId, storeId,unitId , itemId);				
				StoreStock storeStockDetail = null;
				if (storeStockList == null) {
					StoreStock storeStock = new StoreStock(uploadedQuantity, new Store(storeId),
							new Batch(batchId));
					storeStock.setTotalQty(uploadedQuantity);
					storeStock.setCreateDefaults(orgId,unitId,userId);
					//storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
				} else {	
					boolean inOrNot = false;
					Integer qty = 0;
					if(storeStockList.getAvailableQty() > uploadedQuantity) {
						inOrNot = false;
						qty = storeStockList.getAvailableQty() - uploadedQuantity;
					}else {
						inOrNot = true;
						qty = uploadedQuantity - storeStockList.getAvailableQty();
					}
					
					storeStockList.setAvailableQty(uploadedQuantity);	
					storeStockList.setTotalQty(uploadedQuantity);
					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
					updateBatchQuantity(detail,storeId,batchId,storeStockDetail,uploadedQuantity,rejectedQuantity, orgId,  unitId,  userId,itemId,qty,inOrNot);		
				}	
						
									
		 }
		}catch(Exception e){
			logger.error("Error while updating store stock quantity", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Transactional
	private void updateBatchQuantity(UploadStockDetail detail, Integer storeId,Long batchId,StoreStock storeStockDetail,Integer uploadedQuantity,Integer rejectedQuantity , Integer orgId, Integer unitId, Integer userid, Integer itemId, Integer qty, boolean inOrNot) {
		try{
			Integer currentQuantity;			
			Batch batch = ConfirmStockDao.getBatch(batchId);
			
			batch.setCurrentQuantity(uploadedQuantity);
			batch.setQuantity(uploadedQuantity);	
			
			
			if(rejectedQuantity > 0){					
				Integer currentRejectedQty = batch.getRejectedQuantity() != null ? batch.getRejectedQuantity() : 0;					
				batch.setRejectedQuantity(rejectedQuantity +  currentRejectedQty);
			}
			currentQuantity =  Math.abs(batch.getCurrentQuantity() - uploadedQuantity);
			 
			GlobalDao.updateBatchLeaseQuantity(batch);	
			
			saveStockTransaction(detail,currentQuantity, storeStockDetail , orgId, unitId, userid,uploadedQuantity,batch,itemId,storeId,qty,inOrNot);
			
		}catch(Exception ex){
			logger.error("Error while updating batch quantity", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	
	private void saveStockTransaction(UploadStockDetail detail, Integer currentQuantity, StoreStock storeStockDetail, Integer orgId,
			Integer unitId, Integer userid, Integer uploadedQuantity, Batch batch, Integer itemId, Integer storeId, Integer qty, boolean inOrNot) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), qty,
				batch.getRate(), new Store(storeId),
				new Batch(batch.getId()), 0);
		stockTransaction.setIsTransit(false);
		stockTransaction.setItem(batch.getItem());
		stockTransaction.setTransactionId(detail.getUploadStock().getId());
		stockTransaction.setTransactionDetialsId(detail.getId());
		
		if(inOrNot == true) {
			stockTransaction.setInQty(qty);
		}else {
			stockTransaction.setOutQty(qty);
		}
		stockTransaction.setItem(new Item(itemId));
		stockTransaction.setCreateDefaults(
				orgId != null ? orgId : sessionService.getOrganizationId(),
						unitId!= null ?unitId : sessionService.getUnitId(),
								userid != null ? userid : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
		
	}

	@Override
	@Transactional
	public Object getUploadStockApprovalDetails(Integer id) {

		UploadStock uploadStock = UploadStockDao.getUploadStock(id);
		UploadStockResponse response = new UploadStockResponse(uploadStock.getId(), uploadStock.getUploadNo(),
				uploadStock.getAddedDate(), uploadStock.getStopTransaction() !=null?uploadStock.getStopTransaction().getRequestNo():null,
						uploadStock.getStopTransaction() !=null ?uploadStock.getStopTransaction().getStartDateTime():null, uploadStock.getStopTransaction()!=null ? 
								uploadStock.getStopTransaction().getStopDateTime() :null,
				uploadStock.getAddedBy().getUserName(), uploadStock.getStore().getStoreName(),
				uploadStock.getStore().getStoreId(), uploadStock.getStatus().getStatus(),
				uploadStock.getStatus().getId(), uploadStock.getApprovalStatus().getStatus(),
				uploadStock.getApprovalStatus().getId());
		response.setRemark(uploadStock.getRemark());
		List<UploadStockResponseItem> items = new ArrayList<UploadStockResponseItem>();
		for (UploadStockDetail uploadStockDetail : uploadStock.getUploadStockDetails()) {

			UploadStockResponseItem item = new UploadStockResponseItem(uploadStockDetail.getId(),
					uploadStockDetail.getBatch().getItem().getGeneric()!=null ?
							uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getId() :uploadStockDetail.getBatch().getItem().getProductCategory().getAssetCategory().getId(),
							uploadStockDetail.getBatch().getItem().getGeneric()!=null ?
							uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getCategory() : uploadStockDetail.getBatch().getItem().getProductCategory().getAssetCategory().getCategory(),
							uploadStockDetail.getBatch().getItem().getGeneric()!=null ?
							uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getId() :uploadStockDetail.getBatch().getItem().getProductCategory().getId(),
							uploadStockDetail.getBatch().getItem().getGeneric()!=null ?
							uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getCategory() : uploadStockDetail.getBatch().getItem().getProductCategory().getCategory(),
					uploadStockDetail.getBatch().getItem().getId(), uploadStockDetail.getBatch().getItem().getCode(),
					uploadStockDetail.getBatch().getItem().getName(),  uploadStockDetail.getUomType()!=null ? uploadStockDetail.getUomType().getId():null,
					uploadStockDetail.getUomType()!=null ? uploadStockDetail.getUomType().getType():null,
					uploadStockDetail.getUomUnit()!=null ? uploadStockDetail.getUomUnit().getId():null,
					uploadStockDetail.getUomUnit()!=null ? uploadStockDetail.getUomUnit().getUnits():null, 
					uploadStockDetail.getBatch().getId(),
					uploadStockDetail.getBatch().getBatchNo(), uploadStockDetail.getBatch().getExpiryDate()!=null ? uploadStockDetail.getBatch().getExpiryDate():null,
					uploadStockDetail.getBatch().getRate(), uploadStockDetail.getBatch().getCop(),
					uploadStockDetail.getQty(), uploadStockDetail.getApprovedUploadQuantity(),
					uploadStockDetail.getStatus().getId(), uploadStockDetail.getStatus().getStatus(), uploadStockDetail.getBatch().getItem().getIsThreeUOMApplicable(), 
					GlobalDao.getUnitOfMeasurementListByItem(uploadStockDetail.getBatch().getItem().getId(), uploadStock.getOrganizationId()));
			item.setVarianceQty(uploadStockDetail.getVarianceQty()!=null ? uploadStockDetail.getVarianceQty() :null);
			item.setVarianceCop(uploadStockDetail.getVarianceCop()!=null ? uploadStockDetail.getVarianceCop() :null);
			item.setVarianceMrp(uploadStockDetail.getVarianceMrp()!=null ? uploadStockDetail.getVarianceMrp() :null);
			item.setAssetTypeId(uploadStockDetail.getBatch().getItem().getGeneric() != null ?
					uploadStockDetail.getBatch().getItem().getGeneric().getProductCategory().getAssetCategory().getAssetType().getId():
						uploadStockDetail.getBatch().getItem().getProductCategory().getAssetCategory().getAssetType().getId());
			if(uploadStockDetail.getIsActive())
			{
				items.add(item);
			}

		}
		Collections.sort(items, (s1, s2) ->
	    Integer.compare(s2.getId(), s1.getId()));
		Collections.reverse(items);
		response.setItems(items);
		return response;
	}

	@Override
	@Transactional
	public Object cancelUploadStockApproval(UploadStockRequest request) {

		UploadStock uploadStock = UploadStockDao.getUploadStock(request.getId());

		for (UploadStockDetail detail : uploadStock.getUploadStockDetails()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
		//	detail.setUpdatedBy(uploadStock.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		uploadStock.setCancelReason(new CancelReason(request.getCancelReasonId()));
		uploadStock.setCancelNote(request.getCancelNote());

		uploadStock.setStatus(new Status(request.getStatusId()));

		uploadStock.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
	//	uploadStock.setUpdatedBy(uploadStock.getUser());
		uploadStock.setUpdatedByUser(request.getUserId());

		UploadStockDao.updateUploadStock(uploadStock);

		CommonResponse response = new CommonResponse();
		response.setMessage("Upload stock Approval cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object uploadStockApproval(UploadStockRequest request) {
		
		for (Integer id : request.getUploadStockId()) {
			
			UploadStock uploadStock = UploadStockDao.getUploadStock(id);

			uploadStock.setStatus(new Status(request.getStatusId()));
			uploadStock.setApprovalStatus(new Status(request.getApprovalStatusId()));
			ComputeStock computeStock = new ComputeStock(uploadStock,
					new Status(com.param.common.constants.Status.Pending.getStatus()), new Store(uploadStock.getStore().getStoreId()));
			//sessionService.setEntityDefaults(computeStock);	
			/*computeStock.setAddedBy(new User(request.getUserId()));
			computeStock.setUpdatedBy(new User(request.getUserId()));*/
			computeStock.setAddedByUser(request.getUserId());
			computeStock.setUpdatedByUser(request.getUserId());
			computeStock.setAddedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
			computeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
			computeStock.setOrganizationId(request.getOrganizationId());
			computeStock.setUnitId(request.getUnitId());
			for (UploadStockDetail detail : uploadStock.getUploadStockDetails()) {
				
				
				ComputeStockDetail computeStockDetail=new ComputeStockDetail(detail, new Batch(detail.getBatch().getId()),detail.getBatch().getItem().getId());					 
				//sessionService.setEntityDefaults(computeStockDetail);	
				/*computeStockDetail.setAddedBy(new User(request.getUserId()));
				computeStockDetail.setUpdatedBy(new User(request.getUserId()));*/
				computeStockDetail.setAddedByUser(request.getUserId());
				computeStockDetail.setUpdatedByUser(request.getUserId());
				computeStockDetail.setAddedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
				computeStockDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
				computeStockDetail.setOrganizationId(request.getOrganizationId());
				computeStockDetail.setUnitId(request.getUnitId());
				//computeStockDetail.setItemId(detail.getBatch().getItem().getId());
				computeStock.addComputeStockDetail(computeStockDetail);
				computeStockDetail.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
				//computeStockDetail.setApprovedQuantity(detail.getApprovedUploadQuantity());
				computeStockDetail.setApprovedQuantity(detail.getQty());
				detail.approveUploadStockDetail(detail.getQty(), new Status(5),
						TimeZoneComponent.getDateByzone(uploadStock.getUnitId()), new User(request.getUserId()),  detail.getVarianceQty(), detail.getVarianceCop() ,detail.getVarianceMrp());
				
				
				detail.setStatus(new Status(request.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
			//	detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId());
			}

			uploadStock.setApprovedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
			uploadStock.setApprovedBy(new User(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId()));
			uploadStock.setStatus(new Status(request.getApprovalStatusId()));
			uploadStock.setUpdatedDate(TimeZoneComponent.getDateByzone(uploadStock.getUnitId()));
		//	uploadStock.setUpdatedBy(uploadStock.getUser());
			uploadStock.setUpdatedByUser(request.getUserId());

			UploadStockDao.updateUploadStock(uploadStock);
			UploadStockDao.saveComputeStock(computeStock);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Upload Stock approved successfully");

		return response;
	}

}
