package com.param.procurement.purchase.service.localPurchaseRequestApproval;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Constants;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dao.localPurchaseRequest.ILocalPurchaseRequestDao;
import com.param.procurement.purchase.dto.localPurchaseRequest.ItemRequest;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseReqDetailsResponseDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestMasterResponseDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestSearchDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseRequestDropDownResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;


@Service
public class LocalPurchaseRequestApprovalService implements ILocalPurchaseRequestApprovalService{
	
	private Integer screenId = 7;
	
	private Integer localPurchaseReqId = 5;
	
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IItemsDao itemsDao;

	@Autowired
	private ILocalPurchaseRequestDao locPurchaseReqDao;

	@Autowired
	SessionService sessionService;

	@Override
	@Transactional
	public LocalPurchaseRequestDropDownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {

		LocalPurchaseRequestDropDownResponse purchaseReqDropDown = new LocalPurchaseRequestDropDownResponse();
		
		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			purchaseReqDropDown.addRequestingStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			purchaseReqDropDown.addDeliveryStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		
		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			purchaseReqDropDown.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		
		for (Priority priority : GlobalDao.getPriorityList()) {
			purchaseReqDropDown.addPriority(
					new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
		}		
				
		return purchaseReqDropDown;
	}
	
	
	@Override
	@Transactional
	public Object getLocalPuchaseRequestList(LocPurchaseRequestDto locPurchaseReqDto) {
		
		locPurchaseReqDto.setToDate((new DateTime(locPurchaseReqDto.getToDate())).plusDays(1).toDate());
		
		List<LocPurchaseRequestSearchDto> response = new ArrayList<LocPurchaseRequestSearchDto>();
		
		List<PurchaseRequest> purchaseRequestList = locPurchaseReqDao.searchLocalPuchaseRequest(locPurchaseReqDto, screenId , localPurchaseReqId);
			for (PurchaseRequest purchaseRequest : purchaseRequestList) {
				LocPurchaseRequestSearchDto purchaseReqSearchDto = new LocPurchaseRequestSearchDto(purchaseRequest.getId(),purchaseRequest.getAddedDate(),
						purchaseRequest.getPurchaseNo(),purchaseRequest.getStore().getStoreName(),purchaseRequest.getAssetType().getType(),
						purchaseRequest.getRequestType().getType(),purchaseRequest.getPriority().getPriorityDesc(),purchaseRequest.getApprovalStatus().getStatus(),
						purchaseRequest.getStatus().getStatus(),purchaseRequest.getAddedBy().getUserName(),purchaseRequest.getRemark() , purchaseRequest.getRequestingStore().getStoreName());

				response.add(purchaseReqSearchDto);
			}
		return response;
	}
	
	@Override
	@Transactional
	public Object getLocalPurchaseRequest(Long id,Integer unitId, Integer orgId) {
		
		PurchaseRequest purchaseRequest = locPurchaseReqDao.getPurchaseRequest(id);
		
		LocPurchaseRequestMasterResponseDto purchaseReqDetailsDto = new LocPurchaseRequestMasterResponseDto(purchaseRequest.getId(),purchaseRequest.getAddedDate(),
				purchaseRequest.getPurchaseNo(),purchaseRequest.getStore().getStoreId(),purchaseRequest.getStore().getStoreName(),purchaseRequest.getAssetType().getId(),
				purchaseRequest.getAssetType().getType(),purchaseRequest.getRequestType().getId(),purchaseRequest.getRequestType().getType(),purchaseRequest.getPriority().getPriorityId(),purchaseRequest.getPriority().getPriorityDesc(),
				purchaseRequest.getApprovalStatus().getId(),purchaseRequest.getApprovalStatus().getStatus(),purchaseRequest.getStatus().getId(),purchaseRequest.getStatus().getStatus(),purchaseRequest.getRemark(), purchaseRequest.getRequestingStore().getStoreId() , purchaseRequest.getRequestingStore().getStoreName());
		
		
		for (PurchaseRequestDetail detail : locPurchaseReqDao.getPurchaseRequestDetail(id)) {
			if(detail.getIsActive()!=false && detail.getIsDeleted()!=true) {
				List<Integer> indentId = getIndentIdList(detail.getId());				
				
				Long storeStock = itemsDao.getStockAvailable(purchaseRequest.getStore().getStoreId(),detail.getItem().getId(),unitId);
				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(purchaseRequest.getStore().getStoreId(),detail.getItem().getId());
				Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(purchaseRequest.getStore().getStoreId(),detail.getItem().getId(),orgId);
				Long approvedPRAndPOQuantity = itemsDao.getApprovedPRandPOQuantity(detail.getPurchaseRequest().getStore().getStoreId(),detail.getItem().getId(),unitId);
				Long approvedPOQuantity = itemsDao.getApprovedPurchaseOrderQuantity(detail.getPurchaseRequest().getStore().getStoreId(),detail.getItem().getId(),unitId);

				//Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);
				Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit , approvedPRAndPOQuantity , approvedPOQuantity);
				
			if(purchaseReqDetailsDto.getAssetTypeId().equals(Constants.getConsumableId()))
			{
		       purchaseReqDetailsDto.addItems(new LocPurchaseReqDetailsResponseDto(detail.getId(),detail.getItem().getId(),detail.getItem().getCode(),detail.getItem().getName(),
					detail.getUomType().getId().shortValue(),detail.getUomType().getType(),detail.getUomUnit().getId().shortValue(),detail.getUomUnit().getUnits(),
					GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(),orgId),detail.getQuantity(),detail.getCop(),
					storeStock,detail.getRejectQty()!=null ? detail.getRejectQty() : 0,detail.getAmountCop(),detail.getSpecification(),
					detail.getIndentQuantity(),detail.getApprovedQuantity(),maxStoreQuantity,maxOrderQuantity,detail.getStatus().getId(),detail.getStatus().getStatus(),indentId, detail.getItem().getIsThreeUOMApplicable()));
			}
			else if (purchaseReqDetailsDto.getAssetTypeId().equals(Constants.getFixedAssetId()))
			{
				purchaseReqDetailsDto.addItems(new LocPurchaseReqDetailsResponseDto(detail.getId(), detail.getItem().getId(),
						detail.getItem().getCode(), detail.getItem().getName(), 
						storeStock,detail.getApprovedQuantity(),maxStoreQuantity,maxOrderQuantity,
						detail.getRejectQty()!=null ? detail.getRejectQty() : 0, detail.getAmountCop(),detail.getCop(), detail.getQuantity(), detail.getIndentQuantity(), 
						indentId, detail.getSpecification(),detail.getStatus().getId(),detail.getStatus().getStatus(), detail.getItem().getIsThreeUOMApplicable()));
			}
			}
		}
		
		return purchaseReqDetailsDto;
	}
	
	@Transactional
	private List<Integer> getIndentIdList(Integer purchaseReqDetailsId) {
		List<Integer> indentIdList = new ArrayList<Integer>();
					
			for (StoreIndentDetail storeIndent :  locPurchaseReqDao.getIndentIdList(purchaseReqDetailsId)) {
				indentIdList.add(storeIndent.getId());

			}
		return indentIdList;
	}
	
	@Override
	@Transactional
	public Object approvePurchaseRequest(LocPurchaseRequestDto locPurchaseReqDto) {
		
		PurchaseRequest purchaseRequest = locPurchaseReqDao.getPurchaseRequest(locPurchaseReqDto.getId());
		CommonResponse response = new CommonResponse();
        try {
		for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList()) {			
               for(ItemRequest item : locPurchaseReqDto.getItems()) {
            	   if(item.getId() !=null && item.getId().equals(detail.getId())) {
//            		 if(isQuantityValidated(purchaseRequest.getAssetType().getId(),purchaseRequest.getStore().getStoreId(),detail.getItem().getId(),item.getApprovedQuantity(),
//            				 purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId()) ? detail.getUomType().getId() : null )) {
                   	Integer leaseQuantity = item.getApprovedQuantity();
                   	if(purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId())) {
                   				leaseQuantity = Util.GetLeaseQuantity(item.getApprovedQuantity(), getUom(detail.getItem().getId(),locPurchaseReqDto.getOrganisationId()),new UomType(detail.getUomType().getId()));
                   	}
                    	
            		   detail.approveLocalPurchaseRequestDetail(item.getApprovedQuantity(), (new User(locPurchaseReqDto.getUserId())), TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()),item.getRejectNote(),
            				   item.getRejectQuantity(),new RejectReason(item.getRejectReasonId()), new Status(item.getStatusId()), TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()), (new User(locPurchaseReqDto.getUserId())),
            				   leaseQuantity);
            		   			break;
            		   
                   	/*	}
                    	else {
        					throw new DataIntegrityViolationException("Purchase request Approved Quantity is Invalid");
        					}*/
            	   }
             }
		}
		 purchaseRequest.setStatus(new Status(locPurchaseReqDto.getStatusId()));
         purchaseRequest.setApprovalStatus(new Status(locPurchaseReqDto.getApprovalStatusId()));

         purchaseRequest.setApprovedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
         purchaseRequest.setApprovedBy(new User(locPurchaseReqDto.getUserId()));

         purchaseRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
         purchaseRequest.setUpdatedByUser(locPurchaseReqDto.getUserId());

		locPurchaseReqDao.updateLocalPurchaseRequest(purchaseRequest);
		response.setMessage("Local Purchase Request approved successfully");
		
         }
               catch (DataIntegrityViolationException div) {
       			throw div;
       		}
       		catch (Exception e) {
       			throw e;
       		}

		return response;
	}
	
	@Override
	@Transactional
	public Object cancelLocalPurchaseRequest(LocPurchaseRequestDto locPurchaseReqDto) {

		PurchaseRequest purchaseRequest = locPurchaseReqDao.getPurchaseRequest(locPurchaseReqDto.getId());

		for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList()) {
			detail.setStatus(new Status(locPurchaseReqDto.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
			detail.setUpdatedByUser(locPurchaseReqDto.getUserId());
		}

		purchaseRequest.setCancelReason(new CancelReason(locPurchaseReqDto.getCancelReasonId()));
		purchaseRequest.setCancelNote(locPurchaseReqDto.getCancelNote());

		purchaseRequest.setStatus(new Status(locPurchaseReqDto.getStatusId()));
		purchaseRequest.setApprovalStatus(new Status(locPurchaseReqDto.getApprovalStatusId()));

		purchaseRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
		purchaseRequest.setUpdatedByUser(locPurchaseReqDto.getUserId());

		locPurchaseReqDao.updateLocalPurchaseRequest(purchaseRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Local Purchase Request cancelled successfully");

		return response;
	}
	
	
	/* private Boolean isQuantityValidated(Integer assetTypeId,Integer storeId, Integer itemId, Integer quantity,Integer uomTypeId,Integer orgId,Integer unitId) {
			
			Long storeStock = GlobalDao.getStoreStockByItem(storeId, itemId);
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeId, itemId);
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeId, itemId);
			Long prLeaseQunatity = locPurchaseReqDao.getLeaseQunatity(storeId, itemId);
			Integer leaseQuantity = quantity;
			if(assetTypeId.equals(Constants.getConsumableId())) {
				leaseQuantity = Util.GetLeaseQuantity(quantity, getUom(itemId),new UomType(uomTypeId));
			}
			if(maxStoreQuantity < (prLeaseQunatity + leaseQuantity + stockInTransit + storeStock)) {
				return false;
			}				
	
	return true;
	}*/	


	private List<PurchaseOrderUomMapper> getUom(Integer itemId,Integer orgId){
	
	List<UomResponse> uomResponse = GlobalDao.getUnitOfMeasurementListByItem(itemId,orgId);
	List<PurchaseOrderUomMapper> prUomResponse = new ArrayList<PurchaseOrderUomMapper>();
	for (UomResponse uom : uomResponse) {
		PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new UomType(uom.getUomTypeId()),
				new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
				uom.getStoreUom());
		prUomResponse.add(prUom);
	}
	return prUomResponse;
}
}
