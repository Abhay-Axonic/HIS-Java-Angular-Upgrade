package com.param.inventory.store.service.materialReturnAcceptance;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.RejectDetail;
import com.param.entity.model.inventory.Return;
import com.param.entity.model.inventory.ReturnDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.materialReturn.IMaterialReturnDao;
import com.param.inventory.store.dao.materialReturnAcceptance.IMaterialReturnAcceptanceDao;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialsDropdownResponse;
import com.param.inventory.store.dto.materialReturn.ItemRespone;
import com.param.inventory.store.dto.materialReturn.MaterialResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReturnIssue;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class MaterialReturnAcceptanceService implements IMaterialReturnAcceptanceService {

	private Integer screenId = Screen.MaterialReturnAcceptance.getScreen();
	
	private Integer rejectScreenId = Screen.MaterialRejectAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IMaterialReturnAcceptanceDao MaterialReturnAcceptanceDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IMaterialReturnDao MaterialReturnDao;
	@Override
	@Transactional
	public MaterialsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		MaterialsDropdownResponse response = new MaterialsDropdownResponse();

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
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
	public Object getMaterialReturnAcceptanceList(MaterialReurnRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return MaterialReturnAcceptanceDao.searchMaterialReturnAcceptanceList(request, screenId);
	}

	@Override
	@Transactional
	public Object updateMaterialReturnAcceptance(MaterialReurnRequest request) {

		screen = GlobalDao.getScreen(screenId);

		Return returnData = MaterialReturnAcceptanceDao.getReturn(request.getId());

		/*Store storeNew = GlobalDao.getStore(sessionService.getStoreId());

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		
		String rejectNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId() != null ? request.getStoreId() :sessionService.getStoreId(), request.getUnitId()!=null ? request.getUnitId() :sessionService.getUnitId(), rejectScreenId);

		Reject reject = new Reject(new Store(returnData.getFromStore().getStoreId()),
				new Store(returnData.getToStore().getStoreId()), new User(request.getUserId()!=null?request.getUserId() : sessionService.getUserId()), TimeZoneComponent.getDateByzone(request.getUnitId()), rejectNumber,
				new Status(2), returnData);
	//	sessionService.setEntityDefaults(reject);	
		reject.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
				request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId(),
				request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
		for (com.param.inventory.store.dto.storeIndent.ItemRequest item : request.getItems())
		{
			for (ReturnDetail detail : returnData.getReturnDetail()) 
			{
				if (item.getId().equals(detail.getId()) && detail.getIsActive() && !detail.getIsDeleted())
				{
					updateStoreStock(returnData, detail.getBatch(),
							item.getAcceptedQuantity(), detail.getUomType(),request,detail,item.getRejectedQty());
					
					detail.updateReturnDetail(item.getAcceptedQuantity(),
							item.getRejectedQty() != null ? item.getRejectedQty() : 0,
							new RejectReason(item.getRejectReasonId()), new Status(item.getStatusId()), TimeZoneComponent.getDateByzone(request.getUnitId()),
							new User(request.getUserId()!=null ? request.getUserId(): sessionService.getUserId()),0);

					if (item.getRejectedQty() != null && item.getRejectedQty() > 0) {
						
						RejectDetail rejectDetail=new RejectDetail(new RejectReason(item.getRejectReasonId()),
								item.getRejectNote(), item.getRejectedQty(), detail);
				//		sessionService.setEntityDefaults(rejectDetail);	
						rejectDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
								request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId(),
								request.getUserId()!=null ?request.getUserId() : sessionService.getUserId() );
						reject.addRejectDetail(rejectDetail);
					}
				}
			}
		}
		returnData.setStatus(new Status(request.getStatusId()));
		returnData.setApprovalStatus(new Status(request.getApprovalStatusId()));
		returnData.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		//returnData.setUpdatedBy(returnData.getUser());
		returnData.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId() );
		returnData.setRemark(request.getRemark());
		if (reject.getRejectDetails() != null) {
			MaterialReturnAcceptanceDao.save(reject);
		} else {
			MaterialReturnAcceptanceDao.updateReturn(returnData);
		}

		CommonResponse response = new CommonResponse();
		response.setId(returnData.getId().longValue());
		response.setMessage("Material Return Acceptance Updated successfully");
		return response;
	}

	//this is for return acceptance
/*	private Batch updateStoreStock(Integer storeId, Long batchId, Integer quantity, UomType uomType) {
		{
			// get batch needs to be issued
			Batch batch = GlobalDao.getBatch(storeId, batchId);

			// Get existing batch store stock
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

			// Get lease quantity
			Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

			Integer balanceQuantity = avaialableStoreStock + leaseQuantity;

			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);

			// Add stock transaction
			batch.addStockTransaction(new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
					batch.getStoreStockList().get(0).getStore()));

			return batch;
		}
	}
*/

	private void updateStoreStock(Return returnData, Batch batch, Integer quantity, UomType uomType , MaterialReurnRequest request, ReturnDetail d, Integer rejectQty) {
		
        Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(returnData.getToStore().getStoreId(), batch.getId());
        //add by minesh
        Integer avaialableStoreStockOfFromStore = GlobalDao.getStoreStockByBatch(returnData.getFromStore().getStoreId(), batch.getId());
		
		Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
		
		Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(returnData.getFromStore().getStoreId(), batch.getId());
		
		Integer balanceQuantity = avaialableStoreStock + leaseQuantity;
				
		Integer tQty = inTransitQuantity - leaseQuantity;
		Integer rejectQty1=Util.GetLeaseQuantity(rejectQty, batch.getBatchUomList(), uomType);
		
		List<StoreStock> fromStoreStock = batch.getStoreStockList().stream().filter(i -> i.getStore().getStoreId() == returnData.getFromStore().getStoreId()).collect(Collectors.toList());
		if(fromStoreStock!=null &&  fromStoreStock.size()>0) {
			fromStoreStock.getFirst().settQty(tQty);
			if(rejectQty1 != null && rejectQty1>0) {
				//saveStockTransaction(true,rejectQty1, fromStoreStock.get(0),request,d.getId(), d.getMreturn().getReturnNo(),returnData);
			}

		}
		
		List<StoreStock> toStoreStock = batch.getStoreStockList().stream().filter(i -> i.getStore().getStoreId() == returnData.getToStore().getStoreId()).collect(Collectors.toList());
		if(toStoreStock!=null &&  toStoreStock.size()>0) {
			toStoreStock.getFirst().setAvailableQty(balanceQuantity);
	    	
			StockTransaction stockTransaction=	new StockTransaction(balanceQuantity, screen, screen.getCode(),
					leaseQuantity, new Store(request.getStoreId()),d.getId());
			stockTransaction.setTransitNo(returnData.getReturnNo());
			
			if(rejectQty1 != null && rejectQty1 >0) {
				stockTransaction.setIsTransit(true);
				stockTransaction.setOutQty(rejectQty1);
			}else {
				stockTransaction.setIsTransit(false);
			}
			
			stockTransaction.setTransactionId(returnData.getId());
			stockTransaction.setInQty(leaseQuantity);
			MaterialReturnAcceptanceDao.updateIntransitQty(d.getId(),41);
		//	sessionService.setEntityDefaults(stockTransaction);	
			stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
					request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
	    	batch.addStockTransaction(stockTransaction);
		}
		else {
			StoreStock storeStockNew = new StoreStock(leaseQuantity, new Store(returnData.getToStore().getStoreId()), new Batch(batch.getId()));
		//	sessionService.setEntityDefaults(storeStockNew);
			storeStockNew.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
					request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
			//storeStockNew.setIsConsignment(isConsignment);
			storeStockNew.setTotalQty(leaseQuantity);			
			MaterialReturnAcceptanceDao.saveStoreStock(storeStockNew);
			
			StockTransaction stockTransaction=new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					leaseQuantity,new Store(request.getStoreId()), d.getId());
			stockTransaction.setTransitNo(returnData.getReturnNo());
			if(rejectQty1 != null && rejectQty1 >0) {
				stockTransaction.setIsTransit(true);
				stockTransaction.setOutQty(rejectQty1);
			}else {
				stockTransaction.setIsTransit(false);
			}
			stockTransaction.setTransactionId(returnData.getId());
			stockTransaction.setInQty(leaseQuantity);
			MaterialReturnAcceptanceDao.updateIntransitQty(d.getId(),41);
		//	sessionService.setEntityDefaults(stockTransaction);
			stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
					request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
			batch.addStockTransaction(stockTransaction);
		}
	}
	
/*
 	private void saveReject(Return returnData, MaterialReurnRequest request) {
		Boolean flag = false;
		Reject reject = new Reject(new Store(returnData.getFromStore().getStoreId()),
				new Store(returnData.getToStore().getStoreId()), new User(sessionService.getUserId()), TimeZoneComponent.getDateByzone(unitId), "RET-101",
				new Status(2), new Return(request.getmReturnId()));

		for (com.param.inventory.store.dto.storeIndent.ItemRequest item : request.getItems()) {
			if (item.getRejectedQty() != null && item.getRejectedQty() > 0) {
				flag = true;

				for (ReturnDetail detail : returnData.getReturnDetail()) {
					if (item.getId().equals(detail.getId())) {
						reject.addRejectDetail(new RejectDetail(new RejectReason(item.getRejectReasonId()),
								item.getRejectNote(), item.getRejectedQty(), new ReturnDetail(detail.getId())));
					}
				}
			}
		}
		if (reject.getRejectDetails() != null) {
			MaterialReturnAcceptanceDao.save(reject);
		}
	}
*/
	private void saveStockTransaction(Boolean flag,Integer leaseQuantity, StoreStock storeStock, MaterialReurnRequest request, Integer integer, String returnBillNumber, Return returnData) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), storeStock.getStore(),
				new Batch(storeStock.getBatch().getId()),integer);
		stockTransaction.setTransitNo(returnBillNumber);
		stockTransaction.setItem(storeStock.getBatch().getItem());
		stockTransaction.setIsTransit(flag);
		stockTransaction.setTransactionId(returnData.getId());
		stockTransaction.setOutQty(leaseQuantity);
		//sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
	}
	@Override
	@Transactional
	public Object getMaterialReturnAcceptanceDetails(Integer id,Integer unitId) {

		Return returnData = MaterialReturnAcceptanceDao.getReturn(id);

		MaterialResponse response = new MaterialResponse(returnData.getId(), returnData.getAddedDate(),
				returnData.getReturnNo(), returnData.getFromStore().getStoreId(),
				returnData.getFromStore().getStoreName(), returnData.getToStore().getStoreId(),
				returnData.getToStore().getStoreName(), returnData.getAddedBy().getUserName(),
				returnData.getIsConsignment(), returnData.getRemark(), returnData.getStatus().getId(),
				returnData.getStatus().getStatus(), returnData.getNearByExpiry(),
				returnData.getApprovalStatus().getId(), returnData.getApprovalStatus().getStatus());

		for (ReturnDetail detail : MaterialReturnAcceptanceDao.getReturnDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(returnData.getFromStore().getStoreId(),
					detail.getBatch().getItem().getId(),unitId);
			Long availableStock = Util.ConvertQuantity(storeStock, detail.getBatch().getBatchUomList(), detail.getUomType());

			
			List<MaterialReturnIssue> materialReturnIssue = MaterialReturnDao.getMaterialReturnIssueData(detail.getId());
			String issueNo="";
			Date issueDate=null;
			if(materialReturnIssue.size() > 0)
			{
				issueNo=materialReturnIssue.getFirst().getIssueno();
				issueDate=materialReturnIssue.getFirst().getIsuedate();
			}
			
			response.addItems(new ItemRespone(detail.getBatch().getItem().getId(),
					detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
					detail.getBatch().getId(), detail.getUomType().getId(), detail.getUomType().getType(),
					detail.getUomUnit().getId(), detail.getUomUnit().getUnits(), detail.getBatch().getExpiryDate(),
					availableStock, detail.getId(), detail.getReturnReason().getReasonMasterId(),
					detail.getReturnQty() != null ? detail.getReturnQty() : 0, 
					detail.getReturnReason().getReasonDesc(),
					detail.getBatch().getBatchNo(), 
					detail.getBatch().getItem().getGeneric().getName(),
					detail.getAcceptedQty() != null ? detail.getAcceptedQty() : 0,
					detail.getRejectedQty() != null ? detail.getRejectedQty() : 0,
					detail.getStatus() != null ? detail.getStatus().getId() : 0,
					detail.getStatus() != null ? detail.getStatus().getStatus() : null, issueNo, issueDate, detail.getBatch().getItem().getIsThreeUOMApplicable(),
					GlobalDao.getUnitOfMeasurementListByItem(detail.getBatch().getItem().getId(), returnData.getOrganizationId()),detail.getAcceptedQty()));
		}
		return response;
	}

}
