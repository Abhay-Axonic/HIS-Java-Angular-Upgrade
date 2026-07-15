package com.param.inventory.scrap.service.witnessApproval;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.AssetDisposalWorklistRequest;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ScrapRequestDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.entity.model.inventory.WasteAssetDisposalWorklistDetail;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.Organization;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Unit;
import com.param.entity.model.master.User;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.inventory.scrap.dao.scrapRequest.IScrapRequestDao;
import com.param.inventory.scrap.dao.wasteAssestDisposalWorklist.IWasteAssestDisposalWorklistDao;
import com.param.inventory.scrap.dao.witnessApproval.IWitnessApprovalDao;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.AssestDisposalRequest;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.ItemRequest;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
@Service
public class WitnessApprovalService implements IWitnessApprovalService {

	private static final Logger logger = Logger.getLogger(WitnessApprovalService.class);

	private Integer screenId = Screen.WitnessApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IWitnessApprovalDao WitnessApprovalDao;

	@Autowired
	IWalkInSalesDao WalkInSalesDao;

	@Autowired
	IWasteAssestDisposalWorklistDao WasteAssestDisposalWorklistDao;
	
	@Autowired
	IScrapRequestDao ScrapRequestDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public ScrapsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {

		ScrapsDropdownResponse response = new ScrapsDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStores(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(0,0)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getWitnessApprovalList(ScrapRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return WitnessApprovalDao.getWitnessApprovalList(request, screenId);
	}

	@Override
	@Transactional
	public Object updateWitnessApproval(AssestDisposalRequest request) {

		WasteAssetDisposalWorklist wasteAssetDisposalWorklist = WitnessApprovalDao
				.getWasteAssestDisposalWorklist(request.getId());

		com.param.entity.model.inventory.ScrapRequest scrapRequest = ScrapRequestDao.getScrapRequest(wasteAssetDisposalWorklist.getAssetDisposalWorklistRequest().getScrapRequest().getId());

		for (WasteAssetDisposalWorklistDetail detail : wasteAssetDisposalWorklist.getWasteAssetDisposalWorklistDetails()) {
			for (ItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
				//	detail.getAssetDisposalWorklistRequestDetail().getScrapRequestDetail().getUomType(
					
				//	Integer leaseQuantity = Util.GetLeaseQuantity(acceptedQuantity, batch.getBatchUomList(), uomType);
					detail.updateWasteAssetDisposalWorklistDetail(item.getApprovedDisposedQuantity(),
							item.getRejectNote(), item.getRejectQuantity(),
							item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
							new Status(item.getStatusId()), TimeZoneComponent.getDateByzone(wasteAssetDisposalWorklist.getUnitId()), new User(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId()) );

					try {
						//allocateItemQuantity(item.getBatchId(),item.getApprovedDisposedQuantity(), screenId);
						// start code for HIS-8553 Scrap witness approval does not reduce the quantity on store stock dashboard
						allocateItemQuantity(item.getBatchId(),item.getApprovedDisposedQuantity(), screenId, request,detail.getAssetDisposalWorklistRequestDetail().getScrapRequestDetail(),detail);
						// end code for HIS-8553 Scrap witness approval does not reduce the quantity on store stock dashboard
						
					} catch (Exception e) {
						throw new ApplicationException(e.getMessage(), e);
				}
				}
			}
		} 
		scrapRequest.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
		scrapRequest.setApprovalStatus(new Status((com.param.common.constants.Status.Approved.getStatus())));
		scrapRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(wasteAssetDisposalWorklist.getUnitId()));
	//	scrapRequest.setUpdatedBy(scrapRequest.getUser());
		scrapRequest.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId() );
		scrapRequest.setUpdateDefaults(request.getUserId(), scrapRequest.getUnitId());
		AssetDisposalWorklistRequest assetDisposalWorklistRequest = wasteAssetDisposalWorklist.getAssetDisposalWorklistRequest();
		if (assetDisposalWorklistRequest !=null){
			assetDisposalWorklistRequest.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
			assetDisposalWorklistRequest.setWitnessApprovalStatus(new Status(com.param.common.constants.Status.Approved.getStatus()));
		}

		wasteAssetDisposalWorklist.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		wasteAssetDisposalWorklist.setUpdatedDate(TimeZoneComponent.getDateByzone(wasteAssetDisposalWorklist.getUnitId()));
		//wasteAssetDisposalWorklist.setUpdatedBy(wasteAssetDisposalWorklist.getUser());
		wasteAssetDisposalWorklist.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
		
		WitnessApprovalDao.saveWitnessApproval(wasteAssetDisposalWorklist);
		
		CommonResponse response = new CommonResponse();
		response.setMessage("Witness Approval updated successfully");
		
		return response;
	}

	@Override
	@Transactional
	public Object getWitnessApprovalDetails(Integer id) {
		
		ScrapResponse response = WitnessApprovalDao.getWitnessApproval(id);
		for (ItemResponse details : WitnessApprovalDao.getWitnessApprovalDetail(id)) {
			response.addItems(details);
		}
		
		return response;
	}

	/* Private Methods */
	/*private CommonResponse allocateItemQuantity(Long batchId, Integer approvedDisposedQuantity, Integer screenId,Integer unitId,Integer storeId) throws CustomException {

		try {
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId,0);//0 itemId

			if (avaialableStoreStock >= approvedDisposedQuantity) {
				Batch batch = GlobalDao.getBatch(sessionService.getStoreId(), batchId,unitId);

				Integer balanceQuantity = batch.getCurrentQuantity() - approvedDisposedQuantity;

				batch.setCurrentQuantity(balanceQuantity);
				batch.getStoreStockList().get(0)
						.setAvailableQty(avaialableStoreStock - approvedDisposedQuantity);

				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

				StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
						approvedDisposedQuantity, batch.getRate(), batch.getStoreStockList().get(0).getStore(),
						batch);
				sessionService.setEntityDefaults(stockTransaction);

				WalkInSalesDao.saveStockTransaction(stockTransaction);

				CommonResponse response = new CommonResponse();
				//response.setId(WalkInSalesDao.allocateQuantity(batchAllocation).longValue());
				response.setMessage("Item quantity allocated successfully");

				return response;
			} else {
				throw new CustomException("Quantity entered is exceeding max quantity");
			}
		} catch (CustomException e) {
			throw new CustomException("Allocation of item quantity failed");
		} catch (Exception e) {
			
		}
		return null;
	}*/
	//Duplication of above Method
	// start code for HIS-8553 Scrap witness approval does not reduce the quantity on store stock dashboard
	private CommonResponse allocateItemQuantity(Long batchId, Integer approvedDisposedQuantity, Integer screenId, AssestDisposalRequest request, ScrapRequestDetail scrapRequestDetail, WasteAssetDisposalWorklistDetail d) throws CustomException {
		try {
			
		
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch( request.getStoreId(), batchId,0);//0 itemId

			if (avaialableStoreStock >= approvedDisposedQuantity) 
			{
				Batch batch = GlobalDao.getBatch(request.getStoreId()!=null ?  request.getStoreId() : sessionService.getStoreId(), batchId,request.getUnitId());
				Integer balanceQuantity = batch.getCurrentQuantity() - approvedDisposedQuantity;
				batch.setCurrentQuantity(balanceQuantity);
				Integer leaseQuantity=null;
				if(scrapRequestDetail.getUomType()!=null)
				{
					 leaseQuantity = Util.GetLeaseQuantity(approvedDisposedQuantity, batch.getBatchUomList(), scrapRequestDetail.getUomType());	
				}
			//	batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock - approvedDisposedQuantity);
				
				for(StoreStock s : batch.getStoreStockList())
				{
					if(s.getStore().getStoreId().equals( request.getStoreId()))
					{
						s.setAvailableQty(avaialableStoreStock - (leaseQuantity!=null ? leaseQuantity :approvedDisposedQuantity));
					}
				}
				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
				
				
				StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
						approvedDisposedQuantity, batch.getRate(), batch.getStoreStockList().get(0).getStore(),
						batch,d.getId());
				//sessionService.setEntityDefaults(stockTransaction);
				
			//	stockTransaction.setAddedBy(new User(request.getUserId()));
			//	stockTransaction.setUpdatedBy(new User(request.getUserId()));
				stockTransaction.setItem(batch.getItem());
				stockTransaction.setIsTransit(false);
				stockTransaction.setTransactionId(d.getWasteAssetDisposalWorklist().getId());
				stockTransaction.setOutQty(approvedDisposedQuantity);
				stockTransaction.setAddedByUser(request.getUserId());
				stockTransaction.setUpdatedByUser(request.getUserId());
				stockTransaction.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				stockTransaction.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				stockTransaction.setOrganizationId(request.getOrganizationId());
				stockTransaction.setUnitId(request.getUnitId());
				WalkInSalesDao.saveStockTransaction(stockTransaction);

				CommonResponse response = new CommonResponse();
				//response.setId(WalkInSalesDao.allocateQuantity(batchAllocation).longValue());
				response.setMessage("Item quantity allocated successfully");

				return response;
			} else {
				throw new CustomException("Quantity entered is exceeding max quantity");
			}
		} catch (CustomException e) {
			throw new CustomException("Allocation of item quantity failed");
		} catch (Exception e) {
			logger.error("Error while allocating item quantity", e);
			throw new ServiceException(e.getMessage());
		}
	}
	// end code for HIS-8553 Scrap witness approval does not reduce the quantity on store stock dashboard
}
