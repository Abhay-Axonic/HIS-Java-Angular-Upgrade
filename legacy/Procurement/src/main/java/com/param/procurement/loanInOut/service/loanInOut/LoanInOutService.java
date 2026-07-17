package com.param.procurement.loanInOut.service.loanInOut;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.procurement.LoanIn;
import com.param.entity.model.procurement.LoanInDetail;
import com.param.entity.model.procurement.LoanInOut;
import com.param.entity.model.procurement.LoanInOutDetail;
import com.param.entity.model.procurement.LoanOutReceive;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.loanInOut.dao.loanInOut.ILoanInOutDao;
import com.param.procurement.loanInOut.dao.loanOutReceive.ILoanOutReceiveDao;
import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutRequestItem;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;
import com.param.procurement.loanInOut.dto.LoanInOutResponseItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class LoanInOutService implements ILoanInOutService {

	final static Logger logger = Logger.getLogger(LoanInOutService.class);

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	ILoanInOutDao loanInOutDao;

	@Autowired
	private SessionService sessionService;
	@Autowired
	ILoanOutReceiveDao LoanOutReceiveDao;
	private Integer screenId = Screen.LoanInOut.getScreen();
	
	@Override
	@Transactional
	public List<LoanInOutResponse> getLoanInOutList(LoanInOutRequest request) {
		List<LoanInOutResponse> loanInList = new ArrayList<LoanInOutResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		for(LoanInOut loanInOut : loanInOutDao.getloanInOutList(request, screenId))
		{
			LoanInOutResponse loanInOutObj = new LoanInOutResponse( loanInOut.getId(),loanInOut.getLoanInOutNo(), loanInOut.getAddedDate(),
					loanInOut.getLoanIn().getStore().getStoreName(), loanInOut.getLoanIn().getStore().getStoreId(), loanInOut.getLoanIn().getVendor().getName(),
					loanInOut.getLoanIn().getVendor().getId(), loanInOut.getLoanIn().getAssetType().getType(), loanInOut.getLoanIn().getAssetType().getId(),
					loanInOut.getRemark(), loanInOut.getStatus().getStatus(), loanInOut.getStatus().getId());
			loanInOutObj.setOutwardById(loanInOut.getAddedBy().getUserId());
			loanInOutObj.setOutwardBy(loanInOut.getAddedBy().getUserName());
			loanInList.add(loanInOutObj);
		}
		return loanInList;
	}
	
	@Override
	@Transactional
	public LoanInOutResponse getLoanInOutDetails(Integer id,Integer unitId)
	{
		LoanInOut loanInOut = loanInOutDao.getloanInOutDetails(id,unitId);
		LoanInOutResponse loanInOutResponse = new LoanInOutResponse( loanInOut.getId(), loanInOut.getLoanInOutNo(), loanInOut.getAddedDate(),
				loanInOut.getLoanIn().getStore().getStoreName(), loanInOut.getLoanIn().getStore().getStoreId(), loanInOut.getLoanIn().getVendor().getName(), loanInOut.getLoanIn().getVendor().getId(),
				loanInOut.getLoanIn().getAssetType().getType(), loanInOut.getLoanIn().getAssetType().getId(), loanInOut.getRemark(), loanInOut.getStatus().getStatus(), loanInOut.getStatus().getId());
		loanInOutResponse.setLoanInNo(loanInOut.getLoanIn().getInwardNo());
		loanInOutResponse.setOutwardById(loanInOut.getAddedBy().getUserId());
		loanInOutResponse.setOutwardBy(loanInOut.getAddedBy().getUserName());
		loanInOutResponse.setDcNo(loanInOut.getLoanIn().getDcNo());
		loanInOutResponse.setDcDate(loanInOut.getLoanIn().getDcDate());
		List<LoanInOutResponseItem> items = new ArrayList<LoanInOutResponseItem>();
		for(LoanInOutDetail loanInOutDetail : loanInOut.getLoanInOutDetails())
		{
			if(loanInOutDetail.getIsActive() == true && loanInOutDetail.getIsDeleted() == false)
			{
				LoanInOutResponseItem item = null;
				if (loanInOutResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
					item = new LoanInOutResponseItem(loanInOutDetail.getId(), loanInOutDetail.getLoanInDetail().getItem().getId(),
						loanInOutDetail.getLoanInDetail().getItem().getCode(), loanInOutDetail.getLoanInDetail().getItem().getName(), loanInOutDetail.getLoanInDetail().getUomType().getId(),
						loanInOutDetail.getLoanInDetail().getUomType().getType(), loanInOutDetail.getLoanInDetail().getUomUnit().getId(), loanInOutDetail.getLoanInDetail().getUomUnit().getUnits(),
						loanInOutDetail.getLoanInDetail().getBatchNo(), loanInOutDetail.getLoanInDetail().getExpiryDate(), loanInOutDetail.getLoanInDetail().getCop(),
						loanInOutDetail.getTotalAmount(), loanInOutDetail.getRemark());
				item.setLoanInDetailId(loanInOutDetail.getLoanInDetail().getId());
				item.setReturnQuantity(loanInOutDetail.getReturnQuantity());
				item.setLoanInQuantity(loanInOutDetail.getLoanInDetail().getApprovedQuantity());
			}
				else if(loanInOutResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					item = new LoanInOutResponseItem(loanInOutDetail.getId(), loanInOutDetail.getLoanInDetail().getItem().getId(),
							loanInOutDetail.getLoanInDetail().getItem().getCode(), loanInOutDetail.getLoanInDetail().getItem().getName(),
							loanInOutDetail.getLoanInDetail().getBatchNo(), loanInOutDetail.getLoanInDetail().getExpiryDate(), loanInOutDetail.getLoanInDetail().getCop(),
							loanInOutDetail.getTotalAmount(), loanInOutDetail.getRemark());
					item.setLoanInDetailId(loanInOutDetail.getLoanInDetail().getId());
					item.setReturnQuantity(loanInOutDetail.getReturnQuantity());
					item.setLoanInQuantity(loanInOutDetail.getLoanInDetail().getApprovedQuantity());
				}
				items.add(item);
			}
		}
		loanInOutResponse.setItems(items);
		return loanInOutResponse;
	}
	
	@Override
	@Transactional
	public Object saveLoanInOut(LoanInOutRequest request)
	{/*
		 * Integer storeId = request.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 */

		/*Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String loanInOutNumber = GlobalService.getBillNoByGeneratedCode(0, request.getUnitId(), screenId);
		
		LoanInOut loanInOut = new LoanInOut(loanInOutNumber, new LoanIn(request.getId()), request.getRemark(), 
				new Status(request.getStatusId()), request.getTotalAmount());
//		sessionService.setEntityDefaults(loanInOut);
		loanInOut.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for (LoanInOutRequestItem item : request.getItems()) 
		{
			LoanInOutDetail loanInOutDetail=new LoanInOutDetail(new LoanInDetail(item.getLoanInDetailId()), item.getReturnQuantity(),
					item.getTotalAmount(), item.getRemark());
//			sessionService.setEntityDefaults(loanInOutDetail);	
			loanInOutDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			loanInOut.addLoanInOutDetail(loanInOutDetail);
		}
		loanInOutDao.saveloanInOut(loanInOut);
	
		CommonResponse response = new CommonResponse();
		response.setId(loanInOut.getId().longValue());
		response.setMessage("Loan In Out saved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public LoanInOutDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId)
	{
		LoanInOutDropdown response = new LoanInOutDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}
		
		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId))
		{
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		
		for (AssetType assetType : GlobalDao.getAssetTypeList(unitId,orgId)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(),
					assetType.getType()));
		}
		return response;
	}
	
	@Override
	@Transactional
	public Object cancelLoanInOut(LoanInOutRequest request)
	{
		LoanInOut loanInOut = loanInOutDao.getloanInOutDetails(request.getId(),request.getUnitId());

		for (LoanInOutDetail detail : loanInOut.getLoanInOutDetails()) {
			detail.setStatus(new Status(request.getStatusId()));
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(loanInOut.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}
		loanInOut.setCancelReason(new CancelReason(request.getCancelReasonId()));
		loanInOut.setCancelNote(request.getCancelNote());

		loanInOut.setStatus(new Status(request.getStatusId()));
		loanInOut.setUpdatedDate(TimeZoneComponent.getDateByzone(loanInOut.getUnitId()));
		loanInOut.setUpdatedByUser(request.getUserId());

		loanInOutDao.updateloanInOut(loanInOut);

		CommonResponse response = new CommonResponse();
		response.setMessage("Loan In Out cancelled successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object approveLoanInOut(LoanInOutRequest request)
	{

		for (Integer loanInOutId : request.getLoanInOutId()) {

			LoanInOut loanInOut = loanInOutDao.getloanInOutDetails(loanInOutId,request.getUnitId());

			Integer storeId = loanInOut.getLoanIn().getStore().getStoreId();
			
			Integer vendorId = loanInOut.getLoanIn().getVendor().getId();

			for (LoanInOutDetail details : loanInOut.getLoanInOutDetails()) {
				if (details.getIsActive() && !details.getIsDeleted()) {
					try
					{
						Batch batch = InsertApprovedItems(details, storeId, vendorId,loanInOut.getLoanIn().getAssetType().getId(),request.getUnitId(),request);
						if(batch != null)
						{
							loanInOutDao.saveBatch(batch);
						}
					}
					catch(Exception e)
					{
						throw new ApplicationException(e.getMessage(), e);
					}

					details.setStatus(new Status(request.getStatusId()));
					details.setUpdatedDate(TimeZoneComponent.getDateByzone(loanInOut.getUnitId()));
					details.setUpdatedByUser(request.getUserId());
					
				}
			}

			loanInOut.setStatus(new Status(request.getStatusId()));
			loanInOut.setApprovalOn(TimeZoneComponent.getDateByzone(loanInOut.getUnitId()));
			loanInOut.setApprovedBy(new User(request.getUserId()));

			loanInOut.setUpdatedDate(TimeZoneComponent.getDateByzone(loanInOut.getUnitId()));
			loanInOut.setUpdatedByUser(request.getUserId());

			loanInOutDao.updateloanInOut(loanInOut);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Loan In Out approved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object updateLoanInOut(LoanInOutRequest request)
	{
		LoanInOut loanInOut = loanInOutDao.getloanInOutDetails(request.getId(),request.getUnitId());

		for (LoanInOutDetail detail : loanInOut.getLoanInOutDetails()) {
			Integer id = null;

			for (LoanInOutRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					
					detail.updateLoanInOutDetail(new LoanInDetail(item.getLoanInDetailId()), item.getReturnQuantity(),
							item.getTotalAmount(), item.getRemark());
					break;
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(loanInOut.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		for (LoanInOutRequestItem item : request.getItems()) {

			if (item.getId() == null) {
				LoanInOutDetail detail = new LoanInOutDetail(new LoanInDetail(item.getLoanInDetailId()), item.getReturnQuantity(),
						item.getTotalAmount(), item.getRemark());
				detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				loanInOut.addLoanInOutDetail(detail);
			}
		}

		loanInOut.setRemark(request.getRemark());
		loanInOutDao.updateloanInOut(loanInOut);

		CommonResponse response = new CommonResponse();
		response.setId(loanInOut.getId().longValue());
		response.setMessage("Loan In Out updated successfully");

		return response;
	}
	
	private Batch InsertApprovedItems(LoanInOutDetail detail, Integer storeId, Integer vendorId, Integer assetTypeId,Integer unitId, LoanInOutRequest request) {

		Batch batch = detail.getLoanInDetail().getBatch();
		if(batch != null)
		{
			Integer leaseQuantity = detail.getReturnQuantity();
			if(assetTypeId.equals(Constants.getConsumableId())) {
				leaseQuantity = Util.GetLeaseQuantity(detail.getReturnQuantity(), detail.getLoanInDetail().getBatch().getBatchUomList(), detail.getLoanInDetail().getUomType());
			}
			StoreStock storeStock = GlobalDao.getStoreStockByBatchAndStore(batch.getId(), storeId,unitId);
			if(storeStock != null)
			{
				Integer stockAvailableQuantity = storeStock.getAvailableQty();
				Integer availableQuantity = stockAvailableQuantity - leaseQuantity;
				if(availableQuantity <0)
				{
					availableQuantity=0;
				}
				storeStock.setAvailableQty(availableQuantity);
			}
			
			com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
			StockTransaction stockTransaction=new StockTransaction(leaseQuantity, new com.param.entity.model.master.Screen(screen.getId()), 
					screen.getCode(), leaseQuantity, new Store(storeId), new Batch(batch.getId()), detail.getId());
//			sessionService.setEntityDefaults(stockTransaction);
			stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			batch.addStockTransaction(stockTransaction);
			
			Integer batchCurrentQuantity = batch.getCurrentQuantity();
			Integer currentQuantity = batchCurrentQuantity - leaseQuantity;
			batch.setCurrentQuantity(currentQuantity);
	
			return batch;
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<LoanInOutResponse> getLoanInOutItemList(LoanInOutRequest request)
	{
		List<LoanInOutResponse> items = new ArrayList<LoanInOutResponse>();
	//	request.setToDate((new DateTime(request.getLoanInDate())).plusDays(1).toDate());
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		for(LoanIn loanIn : loanInOutDao.getLoanInItems(request))
		{
			LoanInOutResponse item = new LoanInOutResponse(loanIn.getId(), loanIn.getInwardNo(), loanIn.getAddedDate(),
					loanIn.getStore().getStoreName(), loanIn.getStore().getStoreId(), loanIn.getVendor().getName(),
					loanIn.getVendor().getId(), loanIn.getAssetType().getType(), loanIn.getAssetType().getId());
			
			List<LoanInOutResponseItem> details = new ArrayList<LoanInOutResponseItem>();
			LoanInOutResponseItem detailItem = null;
			for(LoanInDetail detail : loanInOutDao.getLoanInItemDetails(loanIn.getId(),request.getUnitId()))
			{ 
				if(loanIn.getAssetType().getId().equals(Constants.getConsumableId())) {
				detailItem = new LoanInOutResponseItem(detail.getId(), detail.getItem().getId(),
						detail.getItem().getCode(), detail.getItem().getName(), detail.getUomType().getId(), detail.getUomType().getType(),
						detail.getUomUnit().getId(), detail.getUomUnit().getUnits(), detail.getBatchNo(), detail.getExpiryDate(),
						detail.getApprovedQuantity(), detail.getCop(),  detail.getRemark(), detail.getTotalAmount());
				detailItem.setQuantity(detail.getApprovedQuantity());
				}
				else if(loanIn.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					detailItem = new LoanInOutResponseItem(detail.getId(), detail.getItem().getId(),
							detail.getItem().getCode(), detail.getItem().getName(), detail.getBatchNo(), detail.getExpiryDate(),
							detail.getApprovedQuantity(), detail.getCop(),  detail.getRemark(), detail.getTotalAmount());
					detailItem.setQuantity(detail.getApprovedQuantity());
				}
				details.add(detailItem);
			}
			
			item.setItems(details);
			items.add(item);
		}
		return items;
	}
	
	@Override
	@Transactional
	public LoanInOutResponse getLoanInOutAddItemList(LoanInOutRequest request)
	{
		LoanInOutResponse response = new LoanInOutResponse();
		List<LoanInOutResponseItem> items = new ArrayList<LoanInOutResponseItem>();
		List<LoanInDetail> detailList = loanInOutDao.getLoanInItemDetails(request);
		if(!detailList.isEmpty())
		{
			 response.setId(detailList.getFirst().getLoanIn().getId());
			 response.setDcNo(detailList.getFirst().getLoanIn().getDcNo());
			 response.setDcDate(detailList.getFirst().getLoanIn().getDcDate());
			 response.setLoanInNo(detailList.getFirst().getLoanIn().getInwardNo());
		}
		for(LoanInDetail detail : detailList)
		{
			
			List<LoanInOutDetail> loanInOutDetails = loanInOutDao.getLoanInOutDtls(detail.getId(), screenId,request.getUnitId());
			int returnQty = 0;
			for(LoanInOutDetail dtl : loanInOutDetails) {
				returnQty = returnQty + dtl.getReturnQuantity();
			}
			
			LoanInOutResponseItem item = null;
			if(detailList.getFirst().getLoanIn().getAssetType().getId().equals(Constants.getConsumableId())) {
				item = new LoanInOutResponseItem(detail.getId(), detail.getItem().getId(),
					detail.getItem().getCode(), detail.getItem().getName(), detail.getUomType().getId(), detail.getUomType().getType(),
					detail.getUomUnit().getId(), detail.getUomUnit().getUnits(), detail.getBatchNo(), detail.getExpiryDate(),
					detail.getApprovedQuantity(), detail.getCop());
				item.setReturnQty(returnQty);
				item.setReturnedQuantity(returnQty);
				item.setAvailableForReturn(detail.getApprovedQuantity() - returnQty);
				item.setRemark(detail.getRemark()!=null ? detail.getRemark() :null);
			}
			else if(detailList.getFirst().getLoanIn().getAssetType().getId().equals(Constants.getFixedAssetId())) {
				item = new LoanInOutResponseItem(detail.getId(), detail.getItem().getId(),
						detail.getItem().getCode(), detail.getItem().getName(), detail.getBatchNo(), detail.getExpiryDate(),
						detail.getApprovedQuantity(), detail.getCop());
				item.setReturnQty(returnQty);
				item.setReturnedQuantity(returnQty);
				item.setRemark(detail.getRemark()!=null ? detail.getRemark() :null);
				//item.setAvailableForReturn(detail.getApprovedQuantity() - returnQty);
				
				/* start code for resolved HIS-10527 Unable to add fixed type item in loan in out */
				item.setAvailableForReturn( (detail.getApprovedQuantity()!=null ? detail.getApprovedQuantity() : 0) - returnQty);
				/* end code for resolved HIS-10527 Unable to add fixed type item in loan in out */
			}
			items.add(item);
		}
		response.setItems(items);
		return response;
	}
}