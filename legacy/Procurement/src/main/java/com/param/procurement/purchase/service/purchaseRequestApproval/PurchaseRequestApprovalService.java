package com.param.procurement.purchase.service.purchaseRequestApproval;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
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
import com.param.global.service.items.IItemsService;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dao.purchaseRequest.IPurchaseRequestDao;
import com.param.procurement.purchase.dao.purchaseRequestApproval.IPurchaseRequestApprovalDao;
import com.param.procurement.purchase.dto.purchaseRequestApproval.ItemRequest;
import com.param.procurement.purchase.dto.purchaseRequestApproval.ItemResponse;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApproval;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalDetail;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalDropDown;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class PurchaseRequestApprovalService implements IPurchaseRequestApprovalService{

    @Autowired
    IPurchaseRequestApprovalDao PurchaseRequestApprovalDao;

    @Autowired
    IPurchaseRequestDao PurchaseRequestDao;

    @Autowired
    IGlobalDao GlobalDao;

    @Autowired
    IItemsService ItemsService;
    
    @Autowired
	private SessionService sessionService;
    
    @Autowired
	IItemsDao itemsDao;


    private Integer purchaseRequestApprovalScreenId = Screen.PurchaseRequestApproval.getScreen();
    private Integer purchaseRequestScreenId = Screen.PurchaseRequest.getScreen();

    @Override
    @Transactional
    public PurchaseRequestApprovalDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
        PurchaseRequestApprovalDropDown response = new PurchaseRequestApprovalDropDown();
        for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
            response.addRequestingStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
        }

        for (Store store : GlobalDao.getStoreList(unitId)) {
            response.addDeliveryStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
        }

        for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(purchaseRequestScreenId)) {
            response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
                    screenStatus.getStatus().getStatus()));
        }

        for (Priority priority : GlobalDao.getPriorityList()) {
            response.addPriority(
                    new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
        }

        return response;
    }

    @Override
    @Transactional
    public List<PurchaseRequestApprovalResponse> purchaseRequestApprovalSearch(PurchaseRequestApproval search) {
        List<PurchaseRequestApprovalResponse> purchaseRequestSearchResponseList = new ArrayList<>();

        List<com.param.entity.model.procurement.PurchaseRequest> purchaseRequests = PurchaseRequestApprovalDao.purchaseRequestApprovalSearch(search, purchaseRequestApprovalScreenId, purchaseRequestScreenId);

        for (com.param.entity.model.procurement.PurchaseRequest purchaseRequest : purchaseRequests) {
            PurchaseRequestApprovalResponse purchaseRequestSearchResponse = new PurchaseRequestApprovalResponse(purchaseRequest.getId(),
                    purchaseRequest.getAddedDate(), purchaseRequest.getPurchaseNo(), purchaseRequest.getStore().getStoreName(), purchaseRequest.getAssetType().getType(),
                    purchaseRequest.getRequestType().getType(), purchaseRequest.getPriority().getPriorityDesc(), purchaseRequest.getApprovalStatus().getStatus(),
                    purchaseRequest.getStatus().getStatus(), purchaseRequest.getAddedBy().getUserName(), purchaseRequest.getRemark() , purchaseRequest.getRequestingStore().getStoreName());

            purchaseRequestSearchResponseList.add(purchaseRequestSearchResponse);
        }

        return purchaseRequestSearchResponseList;
    }

    @Override
    @Transactional
    public PurchaseRequestApprovalDetail purchaseRequestDetails(Long purchaseRequestId,Integer unitId, Integer orgId) {

         	com.param.entity.model.procurement.PurchaseRequest purchaseRequest = PurchaseRequestDao.getPurchaseRequest((int)(long)purchaseRequestId);
            
            PurchaseRequestApprovalDetail response = new PurchaseRequestApprovalDetail((int)(long)purchaseRequest.getId(), purchaseRequest.getAddedDate(), 
            		purchaseRequest.getPurchaseNo(), 
            		purchaseRequest.getStore().getStoreId(), purchaseRequest.getStore().getStoreName(), 
            		purchaseRequest.getPriority().getPriorityId(), purchaseRequest.getPriority().getPriorityDesc(), 
            		purchaseRequest.getAssetType().getId(), purchaseRequest.getAssetType().getType(), 
            		purchaseRequest.getRequestType().getId(), purchaseRequest.getRequestType().getType(),
            		purchaseRequest.getRemark(), purchaseRequest.getStatus().getStatus(), purchaseRequest.getApprovalStatus().getStatus() ,  purchaseRequest.getRequestingStore().getStoreId(), purchaseRequest.getRequestingStore().getStoreName());
            
			for (PurchaseRequestDetail purchaseRequestDetail : PurchaseRequestDao.purchaseRequestDetails(purchaseRequestId)) {
			if(purchaseRequestDetail.getIsActive()!=false && purchaseRequestDetail.getIsDeleted()!=true) {
				
				Long storeStock = itemsDao.getStockAvailable(purchaseRequest.getStore().getStoreId(),purchaseRequestDetail.getItem().getId(),unitId);
				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(purchaseRequestDetail.getPurchaseRequest().getStore().getStoreId(),purchaseRequestDetail.getItem().getId());
				Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(purchaseRequestDetail.getPurchaseRequest().getStore().getStoreId(),purchaseRequestDetail.getItem().getId(),orgId);
			
				Long approvedPRAndPOQuantity = itemsDao.getApprovedPRandPOQuantity(purchaseRequestDetail.getPurchaseRequest().getStore().getStoreId(), purchaseRequestDetail.getItem().getId(),unitId);
				Long approvedPOQuantity = itemsDao.getApprovedPurchaseOrderQuantity(purchaseRequestDetail.getPurchaseRequest().getStore().getStoreId(), purchaseRequestDetail.getItem().getId(),unitId);

				//Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);
                Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit , approvedPRAndPOQuantity , approvedPOQuantity);
				if(response.getAssetTypeId().equals(Constants.getConsumableId()))
			{
					ItemResponse data =		new ItemResponse(purchaseRequestDetail.getId(),purchaseRequestDetail.getItem().getId(),
                            purchaseRequestDetail.getItem().getCode(), purchaseRequestDetail.getItem().getName(), purchaseRequestDetail.getUomType().getType(),
                            purchaseRequestDetail.getUomUnit().getUnits(), storeStock, purchaseRequestDetail.getQuantity(),
                            purchaseRequestDetail.getApprovedQuantity(),maxStoreQuantity,maxOrderQuantity,
                            purchaseRequestDetail.getRejectQty(),
                            purchaseRequestDetail.getRejectReason() != null ? purchaseRequestDetail.getRejectReason().getId() : null,
                            purchaseRequestDetail.getRejectReason() != null ? purchaseRequestDetail.getRejectReason().getReason() : null,
                            purchaseRequestDetail.getSpecification(),
                            purchaseRequestDetail.getStatus() != null ? purchaseRequestDetail.getStatus().getStatus() : null,
                            purchaseRequestDetail.getStatus() != null ? purchaseRequestDetail.getStatus().getId() : null,
            		GlobalDao.getUnitOfMeasurementListByItem(purchaseRequestDetail.getItem().getId(),orgId), purchaseRequestDetail.getPendingQuantity(),
            		purchaseRequestDetail.getItem().getIsThreeUOMApplicable());
					data.setIndentQuantity( purchaseRequestDetail.getQuantity() != null ?  purchaseRequestDetail.getQuantity() : null);
					
            response.addItems(data);
			}
            else if (response.getAssetTypeId().equals(Constants.getFixedAssetId()))
			{
            	ItemResponse data1=	new ItemResponse(purchaseRequestDetail.getId(),purchaseRequestDetail.getItem().getId(),
                        purchaseRequestDetail.getItem().getCode(), purchaseRequestDetail.getItem().getName(),storeStock,purchaseRequestDetail.getQuantity(),
                        purchaseRequestDetail.getApprovedQuantity(),maxStoreQuantity,maxOrderQuantity,
                        purchaseRequestDetail.getRejectQty(),
                        purchaseRequestDetail.getSpecification(),
                        purchaseRequestDetail.getStatus() != null ? purchaseRequestDetail.getStatus().getStatus() : null,
                        purchaseRequestDetail.getStatus() != null ? purchaseRequestDetail.getStatus().getId() : null,purchaseRequestDetail.getPendingQuantity(),
                		purchaseRequestDetail.getItem().getIsThreeUOMApplicable());
            	data1.setIndentQuantity(purchaseRequestDetail.getQuantity() != null ? purchaseRequestDetail.getQuantity(): null);
            	response.addItems(data1);
			}
			
			}
        }

        return response;
    }

    @Override
    @Transactional
    public Object updatePurchaseRequestApproval(PurchaseRequestApproval request) {
        PurchaseRequest purchaseRequest = PurchaseRequestDao.getPurchaseRequest(request.getId());
        CommonResponse response = new CommonResponse();
        try {
        for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList()) {
            for (ItemRequest item : request.getItems()) {
                if (item.getId() != null && item.getId().equals(detail.getId())) {
                
                /*	if(isQuantityValidated(purchaseRequest.getAssetType().getId(),purchaseRequest.getStore().getStoreId(),detail.getItem().getId(),item.getApprovedQuantity(),
                			purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId()) ? detail.getUomType().getId() : null )) {*/
                		Integer leaseQuantity = item.getApprovedQuantity();
                	if(purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId())) {
                		leaseQuantity = Util.GetLeaseQuantity(item.getApprovedQuantity(), getUom(detail.getItem().getId(),request.getOrganizationId()),new UomType(detail.getUomType().getId()));
                	}
                    detail.updatePurchaseRequestDetail(item.getApprovedQuantity(),
                            item.getRejectQuantity() != null ? item.getRejectQuantity() : null, 
                            item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
                            new Status(item.getStatusId()),
                            leaseQuantity);
//                	}
                /*	else {
    					throw new DataIntegrityViolationException("Purchase request Approved Quantity is Invalid");
    					}*/
                }
            }

        }

        purchaseRequest.setStatus(new Status(request.getStatusId()));
        purchaseRequest.setApprovalStatus(new Status(request.getApprovalStatusId()));

        purchaseRequest.setApprovedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
        purchaseRequest.setApprovedBy(new User(request.getUserId()));

        purchaseRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
        purchaseRequest.setUpdatedByUser(request.getUserId());

        PurchaseRequestDao.updatePurchaseRequest(purchaseRequest);

        
        response.setMessage("Purchase request approval status updated successfully");
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
    public Object approvePurchaseRequestApproval(PurchaseRequestApproval request) {
        for (Integer id : request.getPrId()) {
            com.param.entity.model.procurement.PurchaseRequest purchaseRequest = PurchaseRequestDao.getPurchaseRequest(id);

            purchaseRequest.setStatus(new Status(request.getStatusId()));
            purchaseRequest.setApprovalStatus(new Status(request.getApprovalStatusId()));

            for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList()) {
            	 for (ItemRequest item : request.getItems()) {
                     if (item.getId() != null && item.getId().equals(detail.getId())) {
			                detail.setStatus(new Status(request.getApprovalStatusId()));
			                detail.setApprovedQuantity(item.getApprovedQuantity());
			                detail.setPendingQuantity(detail.getPendingQuantity()-item.getApprovedQuantity());
			                detail.setRejectQty(item.getRejectQuantity());
			                detail.setRejectNote(item.getRejectNote());
			                detail.setRejectReason(new RejectReason(item.getRejectReasonId()));
			                
                     }
            	 }
            	 detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
	             detail.setUpdatedByUser(request.getUserId());
	             detail.setApprovedBy(new User(request.getUserId()));
	             detail.setApprovedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
            }

            purchaseRequest.setApprovedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
            purchaseRequest.setApprovedBy(new User(request.getUserId()));

            purchaseRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
            purchaseRequest.setUpdatedByUser(request.getUserId());

            PurchaseRequestDao.updatePurchaseRequest(purchaseRequest);
        }

        CommonResponse response = new CommonResponse();
        response.setMessage("Purchase request approved successfully");

        return response;
    }

    @Override
    @Transactional
    public Object cancelPurchaseRequestApproval(PurchaseRequestApproval request) {

        com.param.entity.model.procurement.PurchaseRequest storeIndent = PurchaseRequestDao.getPurchaseRequest(request.getId());

        for (PurchaseRequestDetail detail : storeIndent.getPurchaseRequestDetailList()) {
            detail.setStatus(new Status(request.getStatusId()));

            detail.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
            detail.setUpdatedByUser(request.getUserId());
        }

        storeIndent.setCancelReason(new CancelReason(request.getReasonId()));
        storeIndent.setCancelNote(request.getNote());

        storeIndent.setStatus(new Status(request.getStatusId()));
        storeIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));
        storeIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
        storeIndent.setUpdatedByUser(request.getUserId());

        PurchaseRequestDao.updatePurchaseRequest(storeIndent);

        CommonResponse response = new CommonResponse();
        response.setMessage("Purchase request approval cancelled successfully");

        return response;
    }
    
    
	/*private Boolean isQuantityValidated(Integer assetTypeId,Integer storeId, Integer itemId, Integer quantity,Integer uomTypeId) {
				
				Long storeStock = GlobalDao.getStoreStockByItem(storeId, itemId);
				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeId, itemId);
				Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeId, itemId);
				Long prLeaseQunatity = PurchaseRequestDao.getLeaseQunatity(storeId, itemId);
				Integer leaseQuantity = quantity;
				if(assetTypeId.equals(Constants.getConsumableId())) {
					leaseQuantity = Util.GetLeaseQuantity(quantity, getUom(itemId),new UomType(uomTypeId));
				}
				if(maxStoreQuantity < (prLeaseQunatity + leaseQuantity + stockInTransit + storeStock)) {
					return false;
				}			
		
		return true;
		}	
*/

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
