package com.param.procurement.loanInOut.service.loanOutReceive;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.LoanOut;
import com.param.entity.model.procurement.LoanOutDetail;
import com.param.entity.model.procurement.LoanOutReceive;
import com.param.entity.model.procurement.LoanOutReceiveDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.common.exception.CustomException;
import com.param.procurement.loanInOut.dao.loanOutReceive.ILoanOutReceiveDao;
import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutRequestItem;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;
import com.param.procurement.loanInOut.dto.LoanInOutResponseItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class LoanOutReceiveService implements ILoanOutReceiveService {

	private Integer screenId = Screen.LoanOutReceive.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Autowired
	ILoanOutReceiveDao LoanOutReceiveDao;

	@Override
	@Transactional
	public LoanInOutDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		LoanInOutDropdown response = new LoanInOutDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(unitId,orgId)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getLoanOutReceiveList(LoanInOutRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return LoanOutReceiveDao.searchLoanOutReceiveList(request, screenId);
	}

	@Override
	@Transactional
	public LoanInOutResponse getLoanOutReceiveDetails(Integer id,Integer unitId) {

		LoanOutReceive loanOutReceive = LoanOutReceiveDao.getLoanOutReceive(id,unitId);

		LoanInOutResponse loanInOutResponse = new LoanInOutResponse(loanOutReceive.getId(),
				loanOutReceive.getLoanoutReceiveNo(), loanOutReceive.getAddedDate(),
				loanOutReceive.getStore().getStoreName(), loanOutReceive.getStore().getStoreId(),
				loanOutReceive.getVendor().getName(), loanOutReceive.getVendor().getId(),
				loanOutReceive.getAssetType().getType(), loanOutReceive.getAssetType().getId(),
				loanOutReceive.getRemark(), loanOutReceive.getTotalAmount(), loanOutReceive.getAddedBy().getUserName(),
				loanOutReceive.getAddedBy().getUserId(), loanOutReceive.getStatus().getStatus(),
				loanOutReceive.getStatus().getId());
		loanInOutResponse.setDcNo(loanOutReceive.getLoanOut().getDcNo());
		loanInOutResponse.setDcDate(loanOutReceive.getLoanOut().getDcDate());
		loanInOutResponse.setLoanOutNo(loanOutReceive.getLoanOut().getLoanOutNo());

		List<LoanInOutResponseItem> items = new ArrayList<LoanInOutResponseItem>();
		for (LoanOutReceiveDetail loanOutReceiveDetail : loanOutReceive.getLoanOutReceiveDetails()) {
			LoanInOutResponseItem item = null;
			if (loanOutReceive.getAssetType().getId().equals(Constants.getConsumableId())) {
			item = new LoanInOutResponseItem(loanOutReceiveDetail.getId(),
					loanOutReceiveDetail.getBatch().getItem().getId(),
					loanOutReceiveDetail.getBatch().getItem().getCode(),
					loanOutReceiveDetail.getBatch().getItem().getName(), loanOutReceiveDetail.getUomType().getId(),
					loanOutReceiveDetail.getUomType().getType(), loanOutReceiveDetail.getUomUnit().getId(),
					loanOutReceiveDetail.getUomUnit().getUnits(), loanOutReceiveDetail.getBatch().getId(),
					loanOutReceiveDetail.getBatch().getBatchNo(), loanOutReceiveDetail.getBatch().getExpiryDate(),
					loanOutReceiveDetail.getLoanOutDetail().getLoanOutQuantity(),
					loanOutReceiveDetail.getReceiveQuantity(), loanOutReceiveDetail.getLoanOutDetail().getCop(),
					loanOutReceiveDetail.getRemark(), loanOutReceiveDetail.getTotalAmount());
			}
			else if(loanOutReceive.getAssetType().getId().equals(Constants.getFixedAssetId())) {
				item = new LoanInOutResponseItem(loanOutReceiveDetail.getId(),
						loanOutReceiveDetail.getBatch().getItem().getId(),
						loanOutReceiveDetail.getBatch().getItem().getCode(),
						loanOutReceiveDetail.getBatch().getItem().getName(),loanOutReceiveDetail.getBatch().getId(),
						loanOutReceiveDetail.getBatch().getBatchNo(), loanOutReceiveDetail.getBatch().getExpiryDate(),
						loanOutReceiveDetail.getLoanOutDetail().getLoanOutQuantity(),
						loanOutReceiveDetail.getReceiveQuantity(), loanOutReceiveDetail.getLoanOutDetail().getCop(),
						loanOutReceiveDetail.getRemark(), loanOutReceiveDetail.getTotalAmount());
			}

			items.add(item);

		}
		loanInOutResponse.setItems(items);
		return loanInOutResponse;
	}

	@Override
	@Transactional
	public Object saveLoanOutReceive(LoanInOutRequest request) throws CustomException {

		/*Integer storeId = request.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String loanOutRecNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);
		
		if(loanOutRecNumber == null)
		{
			CommonResponse response = new CommonResponse();
			response.setId((long) 0);
			response.setMessage("Please configure prefix number ");
			return response;
		}
		
		LoanOutReceive loanOutReceive = new LoanOutReceive(new AssetType(request.getAssetTypeId()), loanOutRecNumber,
				request.getRemark(), new Status(request.getStatusId()), new Store(request.getStoreId()),
				request.getTotalAmount(), new Vendor(request.getVendorId()), new LoanOut(request.getLoanoutId()));
//		sessionService.setEntityDefaults(loanOutReceive);
		loanOutReceive.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for (LoanInOutRequestItem item : request.getItems()) 
		{
			if (request.getAssetTypeId().equals(Constants.getConsumableId()))
			{
				LoanOutReceiveDetail loanOutReceiveDetail=	new LoanOutReceiveDetail(new Batch(item.getBatchId()), item.getRemark(), item.getReceiveQuantity(),
						new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
						new LoanOutDetail(item.getLoanOutDetailId()), item.getTotalAmount());
				loanOutReceiveDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
			loanOutReceive.addLoanOutReceiveDetail(loanOutReceiveDetail);
			}
			else if(request.getAssetTypeId().equals(Constants.getFixedAssetId()))
			{
				LoanOutReceiveDetail loanOutReceiveDetail=	new LoanOutReceiveDetail(new Batch(item.getBatchId()), item.getRemark(), item.getReceiveQuantity(),
						new LoanOutDetail(item.getLoanOutDetailId()), item.getTotalAmount());
				loanOutReceiveDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				loanOutReceive.addLoanOutReceiveDetail(loanOutReceiveDetail);
			}

		}
//		sessionService.setEntityDefaults(loanOutReceive);	
		loanOutReceive.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		LoanOutReceiveDao.saveLoanOutReceive(loanOutReceive);

		CommonResponse response = new CommonResponse();
		response.setId(loanOutReceive.getId().longValue());
		response.setMessage("Loan Out Receive saved successfully");

		return response;
	}

	@Override
	@Transactional
	public List<LoanInOutResponse> getLoanOutReceiveItemList(LoanInOutRequest request) {

		List<LoanInOutResponse> items = new ArrayList<LoanInOutResponse>();
		//request.setToDate((new DateTime(request.getLoanInDate())).plusDays(1).toDate());
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Closed.getStatus());
		

		for (LoanOut loanOut : LoanOutReceiveDao.getLoanOutItems(request, statusIds)) {

			LoanInOutResponse item = new LoanInOutResponse(loanOut.getId(), loanOut.getLoanOutNo(),
					loanOut.getAddedDate(), loanOut.getStore().getStoreId(), loanOut.getStore().getStoreName(),
					loanOut.getAssetType().getId(), loanOut.getAssetType().getType(), loanOut.getVendor().getId(),
					loanOut.getVendor().getName());

			List<LoanInOutResponseItem> details = new ArrayList<LoanInOutResponseItem>();
			for (LoanOutDetail detail : loanOut.getLoanOutDetails()) {
				
				
				List<LoanOutReceive>  loanOutReceive = LoanOutReceiveDao.getLoanOutReceive1(loanOut.getId(),loanOut.getUnitId());
				Integer qty=0;
				Boolean flag=false;
				if(loanOutReceive!=null && !loanOutReceive.isEmpty())
				{
					for(LoanOutReceive rr :loanOutReceive) {
					for(LoanOutReceiveDetail d :rr.getLoanOutReceiveDetails())
					{
						if(d.getLoanOutDetail().getId().equals(detail.getId()))
						{
							qty=qty+d.getReceiveQuantity();
							flag=true;
						}
					}
				}
				}
				
				if(detail.getIsDeleted() == false && detail.getIsActive() == true && qty <detail.getLoanOutQuantity()) {
					LoanInOutResponseItem detailItem = null;					
					if (request.getAssetTypeId().equals(Constants.getConsumableId())) 
					{
						detailItem = new LoanInOutResponseItem(detail.getId(),
							detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
							detail.getBatch().getItem().getName(), detail.getUomType().getId(),
							detail.getUomType().getType(), detail.getUomUnit().getId(), detail.getUomUnit().getUnits(),
							detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(), detail.getLoanOutQuantity(),
							detail.getCop(), detail.getRemark(), detail.getTotalAmount());
					}
					else if(request.getAssetTypeId().equals(Constants.getFixedAssetId())) {
						detailItem = new LoanInOutResponseItem(detail.getId(),
								detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
								detail.getBatch().getItem().getName(),
								detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(), detail.getLoanOutQuantity(),
								detail.getCop(), detail.getRemark(), detail.getTotalAmount());
					}
				details.add(detailItem);
				}
			}
             if(!details.isEmpty())
             {
            		item.setItems(details);
        			items.add(item); 
             }
		

		}
		return items;
	}

	@Override
	@Transactional
	public LoanInOutResponse getLoanOutReceiveAddItemList(LoanInOutRequest request) {

		LoanInOutResponse response = new LoanInOutResponse();
		List<LoanInOutResponseItem> items = new ArrayList<LoanInOutResponseItem>();

		List<LoanOutDetail> details = LoanOutReceiveDao.getLoanOutItemsDetails(request);

		if (!details.isEmpty()) {
			response.setId(details.get(0).getLoanOut().getId());
			response.setDcNo(details.get(0).getLoanOut().getDcNo());
			response.setDcDate(details.get(0).getLoanOut().getDcDate());
			response.setLoanOutNo(details.get(0).getLoanOut().getLoanOutNo());
		}
		for (LoanOutDetail detail : details) {
			LoanInOutResponseItem item = null;
			if (request.getAssetTypeId().equals(Constants.getConsumableId())) {
			item = new LoanInOutResponseItem(detail.getId(), detail.getBatch().getItem().getId(),
					detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
					detail.getUomType().getId(), detail.getUomType().getType(), detail.getUomUnit().getId(),
					detail.getUomUnit().getUnits(), detail.getBatch().getId(), detail.getBatch().getBatchNo(),
					detail.getBatch().getExpiryDate(), detail.getCop(), detail.getLoanOutQuantity());
			item.setRemark(detail.getRemark()!=null ? detail.getRemark() : null);
			}
			else if(request.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				item = new LoanInOutResponseItem(detail.getId(), detail.getBatch().getItem().getId(),
						detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
						detail.getBatch().getId(), detail.getBatch().getBatchNo(),
						detail.getBatch().getExpiryDate(), detail.getCop(), detail.getLoanOutQuantity());
				item.setRemark(detail.getRemark()!=null ? detail.getRemark() : null);
			}

			items.add(item);
		}
		response.setItems(items);
		return response;
	}

	@Override
	@Transactional
	public Object cancelLoanOutReceive(LoanInOutRequest request) {

		LoanOutReceive loanOutReceive = LoanOutReceiveDao.getLoanOutReceive(request.getId(),request.getUnitId());

		for (LoanOutReceiveDetail detail : loanOutReceive.getLoanOutReceiveDetails()) {
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(loanOutReceive.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}
		loanOutReceive.setCancelReason(new CancelReason(request.getCancelReasonId()));
		loanOutReceive.setCancelNote(request.getCancelNote());

		loanOutReceive.setStatus(new Status(request.getStatusId()));
		loanOutReceive.setUpdatedDate(TimeZoneComponent.getDateByzone(loanOutReceive.getUnitId()));
		loanOutReceive.setUpdatedByUser(request.getUserId());

		LoanOutReceiveDao.updateLoanOutReceive(loanOutReceive);

		CommonResponse response = new CommonResponse();
		response.setMessage("Loan Out cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveLoanOutReceive(LoanInOutRequest request) {

		for (Integer loanOutReceiveId : request.getLoanOutReceiveId()) {

			LoanOutReceive loanOutReceive = LoanOutReceiveDao.getLoanOutReceive(loanOutReceiveId,request.getUnitId());

			Integer storeId = loanOutReceive.getStore().getStoreId();

			for (LoanOutReceiveDetail detail : loanOutReceive.getLoanOutReceiveDetails()) {
				if (detail.getIsActive() && !detail.getIsDeleted()) {
					/*if (request.getAssetTypeId().equals(Constants.getConsumableId())) {
						//Long batchId = detail.getBatch().getId();
						//Integer quantity = detail.getReceiveQuantity();
						//UomType uomType = detail.getUomType();
					}*/

					Batch batch = updateBatchStoreTransaction(storeId, detail.getBatch().getId(), 
							detail.getReceiveQuantity(), detail.getUomType()!=null ? detail.getUomType() : null,
							loanOutReceive.getAssetType().getId(),request.getUnitId(), request, detail);
//					sessionService.setEntityUpdateDefaults(batch);	
					batch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
				}
			}

			loanOutReceive.setStatus(new Status(request.getStatusId()));
			loanOutReceive.setApprovalOn(TimeZoneComponent.getDateByzone(loanOutReceive.getUnitId()));
			//loanOutReceive.setApprovedBy(sessionService.getCookieObjectDto().getUserId());
			//com.param.entity.model.master.User u=GlobalDao.getUserById(sessionService.getCookieObjectDto().getUserId());
			//loanOutReceive.setApprovedBy(u);
			
			//loanOutReceive.setApprovedBy(loanOutReceive.getUser());
			
//			sessionService.setEntityUpdateDefaults(loanOutReceive);
			loanOutReceive.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			//loanOutReceive.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			//Integer userId = sessionService.getCookieObjectDto().getUserId();
			//com.param.entity.model.master.User u =GlobalDao.getUserById(userId);
			//u.setUpdatedBy(userId);
			//loanOutReceive.setUpdatedBy(u);
			//loanOutReceive.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			//loanOutReceive.setUpdatedBy(loanOutReceive.getUser());
			//sessionService.setEntityDefaults(loanOutReceive);
			LoanOutReceiveDao.updateLoanOutReceive(loanOutReceive);
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Loan Out Receive approved successfully");

		return response;
	}

	private Batch updateBatchStoreTransaction(Integer storeId, Long batchId, Integer quantity, UomType uomType,Integer assetTypeId,Integer unitId, LoanInOutRequest request, LoanOutReceiveDetail d) {

		screen = GlobalDao.getScreen(screenId);

		Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId,0);//0 itemId

		Integer leaseQuantity = quantity;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
		}

		Integer balanceQuantity = avaialableStoreStock + leaseQuantity;

		if(balanceQuantity < 0)
		{
			balanceQuantity=0;
		}
		Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity;

		if(currentQuantity < 0)
		{
			currentQuantity=0;
		}
		batch.setCurrentQuantity(currentQuantity);

		batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);

		// Add stock transaction
		StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
				batch.getStoreStockList().get(0).getStore(), d.getId());
//		sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		
//		sessionService.setEntityUpdateDefaults(batch);
		//stockTransaction.setUpdateDefaults(request.getUserId());
		batch.addStockTransaction(stockTransaction);
		//sessionService.setEntityDefaults(batch);	
		return batch;
	}

	@Override
	@Transactional
	public Object updateLoanOutReceive(LoanInOutRequest request) {

		LoanOutReceive loanOutReceive = LoanOutReceiveDao.getLoanOutReceive(request.getId(),request.getUnitId());

		for (LoanOutReceiveDetail detail : loanOutReceive.getLoanOutReceiveDetails()) {
			Integer id = null;

			for (LoanInOutRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();

					if (loanOutReceive.getAssetType().getId().equals(Constants.getConsumableId())) {
						detail.updateLoanOutReceiveDetail(new Batch(item.getBatchId()), item.getReceiveQuantity(),
								item.getRemark(), item.getTotalAmount(), new UomType(item.getUomTypeId()),
								new UomUnit(item.getUomUnitId()));
					}
					else if(loanOutReceive.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						detail.updateLoanOutReceiveDetail(new Batch(item.getBatchId()), item.getReceiveQuantity(),
								item.getRemark(), item.getTotalAmount());
					}

				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(loanOutReceive.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		for (LoanInOutRequestItem item : request.getItems()) {

			if (item.getId() == null)
			{
				LoanOutReceiveDetail detail = null;
				if (loanOutReceive.getAssetType().getId().equals(Constants.getConsumableId())) 
				{
				detail = new LoanOutReceiveDetail(new Batch(item.getBatchId()), item.getRemark(),
						item.getReceiveQuantity(), new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
						new LoanOutDetail(item.getLoanOutDetailId()), item.getTotalAmount());
				}
				else if(loanOutReceive.getAssetType().getId().equals(Constants.getFixedAssetId())) {
				detail = new LoanOutReceiveDetail(new Batch(item.getBatchId()), item.getRemark(),
							item.getReceiveQuantity(),
							new LoanOutDetail(item.getLoanOutDetailId()), item.getTotalAmount());
				}
//				sessionService.setEntityDefaults(detail);	
				detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				loanOutReceive.addLoanOutReceiveDetail(detail);
			}
		}
		loanOutReceive.setTotalAmount(request.getTotalAmount());
		loanOutReceive.setRemark(request.getRemark());
		LoanOutReceiveDao.updateLoanOutReceive(loanOutReceive);

		CommonResponse response = new CommonResponse();
		response.setId(loanOutReceive.getId().longValue());
		response.setMessage("Loan Out Receive updated successfully");

		return response;
	}
}
