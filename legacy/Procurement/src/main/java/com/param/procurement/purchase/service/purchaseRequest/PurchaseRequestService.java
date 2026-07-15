package com.param.procurement.purchase.service.purchaseRequest;

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
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.IItemsService;
import com.param.global.utility.Util;
import com.param.procurement.common.exception.CustomException;
import com.param.procurement.purchase.dao.purchaseRequest.IPurchaseRequestDao;
import com.param.procurement.purchase.dto.purchaseRequest.ItemRequest;
import com.param.procurement.purchase.dto.purchaseRequest.ItemResponse;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequest;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestDetailResponse;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestDropdownResponse;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestSearchResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class PurchaseRequestService implements IPurchaseRequestService {

	@Autowired
	IItemsDao ItemsDao;

	@Autowired
	IPurchaseRequestDao PurchaseRequestDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IItemsService ItemsService;

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.PurchaseRequest.getScreen();
	
	@Autowired
	IItemsDao itemsDao;

	@Override
	@Transactional
	public PurchaseRequestDropdownResponse getDropdowns(Integer unitId, Integer userId) {
		PurchaseRequestDropdownResponse response = new PurchaseRequestDropdownResponse();


		for (com.param.global.dto.global.Store store : GlobalDao.getUserStoreListBP(userId,unitId)) {
			response.addDeliveryStore(new com.param.global.dto.global.Store(store.getId(), store.getStore()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addRequestingStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
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
	public List<PurchaseRequestSearchResponse> purchaseRequestSearch(PurchaseRequest search) {
		List<PurchaseRequestSearchResponse> purchaseRequestSearchResponseList = new ArrayList<PurchaseRequestSearchResponse>();

		List<com.param.entity.model.procurement.PurchaseRequest> purchaseRequests = PurchaseRequestDao
				.purchaseRequestSearch(search, screenId);

		for (com.param.entity.model.procurement.PurchaseRequest purchaseRequest : purchaseRequests) {
			PurchaseRequestSearchResponse purchaseRequestSearchResponse = new PurchaseRequestSearchResponse(
					purchaseRequest.getId(), purchaseRequest.getAddedDate(), purchaseRequest.getPurchaseNo(),
					purchaseRequest.getStore().getStoreName(), purchaseRequest.getAssetType().getType(),
					purchaseRequest.getRequestType().getType(), purchaseRequest.getPriority().getPriorityCode(),
					purchaseRequest.getApprovalStatus().getStatus(), purchaseRequest.getStatus().getStatus(),
					purchaseRequest.getAddedBy().getUserName(), purchaseRequest.getRemark(), purchaseRequest.getRequestingStore().getStoreName());

			purchaseRequestSearchResponseList.add(purchaseRequestSearchResponse);
		}

		return purchaseRequestSearchResponseList;
	}

	@Override
	@Transactional
	public PurchaseRequestDetailResponse purchaseRequestDetails(Long purchaseRequestId,Integer unitId, Integer orgId) {

		com.param.entity.model.procurement.PurchaseRequest purchaseRequest = PurchaseRequestDao
				.getPurchaseRequest((int) (long) purchaseRequestId);

		PurchaseRequestDetailResponse response = new PurchaseRequestDetailResponse(purchaseRequest.getId(),
				purchaseRequest.getAddedDate(), purchaseRequest.getPurchaseNo(),
				purchaseRequest.getStore().getStoreId(), purchaseRequest.getStore().getStoreName(),
				purchaseRequest.getPriority().getPriorityId(), purchaseRequest.getPriority().getPriorityDesc(),
				purchaseRequest.getAssetType().getId(), purchaseRequest.getAssetType().getType(),
				purchaseRequest.getRequestType().getId(), purchaseRequest.getRequestType().getType(),
				purchaseRequest.getRemark(), purchaseRequest.getStatus().getStatus(),
				purchaseRequest.getApprovalStatus().getStatus(), purchaseRequest.getRequestingStore().getStoreId(), purchaseRequest.getRequestingStore().getStoreName());

		List<ItemResponse> itemsList = new ArrayList<ItemResponse>();
		for (PurchaseRequestDetail purchaseRequestDetail : PurchaseRequestDao
				.purchaseRequestDetails(purchaseRequestId)) {

			if (purchaseRequestDetail.getIsActive() != false && purchaseRequestDetail.getIsDeleted() != true) {
				Long storeStock = ItemsDao.getStockAvailable(purchaseRequest.getStore().getStoreId(),purchaseRequestDetail.getItem().getId(),unitId);
				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(
						purchaseRequestDetail.getPurchaseRequest().getStore().getStoreId(),
						purchaseRequestDetail.getItem().getId());
				Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(
						purchaseRequestDetail.getPurchaseRequest().getStore().getStoreId(),
						purchaseRequestDetail.getItem().getId(),orgId);
				Long approvedPRAndPOQuantity = itemsDao.getApprovedPRandPOQuantity(purchaseRequestDetail.getPurchaseRequest().getStore().getStoreId(), purchaseRequestDetail.getItem().getId(),unitId);
				Long approvedPOQuantity = itemsDao.getApprovedPurchaseOrderQuantity(purchaseRequestDetail.getPurchaseRequest().getStore().getStoreId(), purchaseRequestDetail.getItem().getId(),unitId);

				Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit , approvedPRAndPOQuantity , approvedPOQuantity);
						
				ItemResponse itemResponse = new ItemResponse();
				
				// consumable items
				if (response.getAssetTypeId().equals(Constants.getConsumableId())) {
					itemResponse = new ItemResponse(purchaseRequestDetail.getId(),
							purchaseRequestDetail.getItem().getId(), purchaseRequestDetail.getItem().getCode(),
							purchaseRequestDetail.getItem().getName(), purchaseRequestDetail.getUomType().getId(),
							purchaseRequestDetail.getUomType().getType(), purchaseRequestDetail.getUomUnit().getId(),
							purchaseRequestDetail.getUomUnit().getUnits(), storeStock,
							purchaseRequestDetail.getQuantity(), maxStoreQuantity, maxOrderQuantity,
							purchaseRequestDetail.getSpecification(),purchaseRequestDetail.getItem().getIsThreeUOMApplicable(), 
							GlobalDao.getUnitOfMeasurementListByItem(purchaseRequestDetail.getItem().getId(),orgId));
					itemResponse.setPendingQuantity(purchaseRequestDetail.getPendingQuantity());
					Item i = GlobalDao.getItemById(purchaseRequestDetail.getItem().getId());
					itemResponse.setIsThreeUOMApplicable(i.getIsThreeUOMApplicable());
					
				}
				// Fixed Items
				else if (response.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					itemResponse = new ItemResponse(purchaseRequestDetail.getId(),
							purchaseRequestDetail.getItem().getId(), purchaseRequestDetail.getItem().getCode(),
							purchaseRequestDetail.getItem().getName(), storeStock, purchaseRequestDetail.getQuantity(),
							maxStoreQuantity, maxOrderQuantity, purchaseRequestDetail.getSpecification());
					itemResponse.setPendingQuantity(purchaseRequestDetail.getPendingQuantity());
					Item i = GlobalDao.getItemById(purchaseRequestDetail.getItem().getId());
					itemResponse.setIsThreeUOMApplicable(i.getIsThreeUOMApplicable());
				}

				if (purchaseRequestDetail.getPurchaseRequest().getRequestType().getId()
						.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType())) {
					List<Integer> indentIds = new ArrayList<>();
					Integer indentQuantity = 0;
					for (StoreIndentDetail storeIndentDetail : purchaseRequestDetail.getStoreIndentDetailList()) {
						indentIds.add(storeIndentDetail.getId());
						indentQuantity = indentQuantity + storeIndentDetail.getPendingQuantity();
					}
					if(indentQuantity.equals(0))
					{
						for (AssetIndentDetail storeIndentDetail : purchaseRequestDetail.getAssetIndentDetailList()) {
							indentIds.add(storeIndentDetail.getId());
							indentQuantity = indentQuantity + storeIndentDetail.getPendingQuantity();
						}	
					}
					itemResponse.setIndentQuantity(indentQuantity);
					itemResponse.setIndentId(indentIds);
					itemResponse.setPendingQuantity(purchaseRequestDetail.getPendingQuantity());
					Item i = GlobalDao.getItemById(purchaseRequestDetail.getItem().getId());
					itemResponse.setIsThreeUOMApplicable(i.getIsThreeUOMApplicable());
				}
				itemsList.add(itemResponse);
			}
			response.setItems(itemsList);
		}
		return response;
	}

	@Override
	@Transactional
	public Object savePurchaseRequest(PurchaseRequest request) throws Exception {
		CommonResponse response = new CommonResponse();
		
		 /* Integer storeId = request.getStoreId();
		  
		  Store storeNew = GlobalDao.getStore(storeId);
		 

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String purReqNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);
		if(purReqNumber== null)
		{
			throw new CustomException("Please set prefix number");
		}
		try {
//			if (request.ValidateSave(GlobalDao)) {

				com.param.entity.model.procurement.PurchaseRequest purchaseRequest = new com.param.entity.model.procurement.PurchaseRequest(
						new Status(request.getApprovalStatusId()), new AssetType(request.getAssetTypeId()),
						new Priority(request.getPriorityId()), purReqNumber, request.getRemark(),
						new RequestType(request.getRequestTypeId()), new com.param.entity.model.master.Screen(screenId),
						new Status(request.getStatusId()), new Store(request.getStoreId()), new Store(request.getRequestingStoreId()));
				purchaseRequest.setPurchaseNo(purReqNumber);
//				sessionService.setEntityDefaults(purchaseRequest);
				purchaseRequest.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			//	purchaseRequest.setUpdatedByUser(request.getUserId());
				for (ItemRequest item : request.getItems()) {
//					if (isQuantityValidated(request.getAssetTypeId(), request.getStoreId(), item.getItemId(),
//							item.getQuantity(), item.getUomTypeId(), 0)) {
						PurchaseRequestDetail purchaseRequestDetail = null;
						
						// consumable
						if (request.getAssetTypeId().equals(Constants.getConsumableId())) 
						{
							Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(), getUom(item.getItemId(),request.getOrganizationId()),
									new UomType(item.getUomTypeId()));
							purchaseRequestDetail = new PurchaseRequestDetail(new Item(item.getItemId()),
									item.getQuantity(), item.getSpecification(), new Status(item.getStatusId()),
									new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()), leaseQuantity);
							purchaseRequestDetail.setPendingQuantity(item.getPendingQuantity());
							
						}
						// Fixed
						else if (request.getAssetTypeId().equals(Constants.getFixedAssetId())) {
							purchaseRequestDetail = new PurchaseRequestDetail(new Item(item.getItemId()),
									item.getQuantity(), item.getSpecification(), new Status(item.getStatusId()),
									item.getQuantity());
							purchaseRequestDetail.setPendingQuantity(item.getPendingQuantity());

						}
						/**
						 * If request type id is against indent (2) & indent list is not null, then
						 * proceed to add Store indent details list
						 */
						if (request.getRequestTypeId()
								.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType()) && request.getAssetTypeId().equals(Constants.getConsumableId())
								&& item.getIndentId() != null) {
							purchaseRequestDetail.setIndentQuantity(item.getIndentQuantity());
							purchaseRequestDetail.setPendingQuantity(item.getPendingQuantity());
							List<StoreIndentDetail> storeIndentDetails = GlobalDao
									.getStoreIndentDetails(item.getIndentId());
							for (StoreIndentDetail storeIndentDetail : storeIndentDetails) {
								purchaseRequestDetail.addStoreIndentDetailList(storeIndentDetail);
							}
						}
						else if(request.getRequestTypeId()
								.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType()) && request.getAssetTypeId().equals(Constants.getFixedAssetId())
								&& item.getIndentId() != null)
						{

							purchaseRequestDetail.setIndentQuantity(item.getIndentQuantity());
							purchaseRequestDetail.setPendingQuantity(item.getPendingQuantity());
							List<AssetIndentDetail> storeIndentDetails =new ArrayList<>();
							for(Integer i :item.getIndentId() )
							{
							storeIndentDetails.add(GlobalDao.getAssetIndentDetail(i));
							}
							 for (AssetIndentDetail storeIndentDetail : storeIndentDetails) {
								purchaseRequestDetail.addassetIndentDetailList(storeIndentDetail);
							}
						
						}
//						sessionService.setEntityDefaults(purchaseRequestDetail);
						purchaseRequestDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						purchaseRequest.addPurchaseRequestDetailList(purchaseRequestDetail);
				/*	} else {
						// CustomException custom = new CustomException("Purchase request Quantity is
						// Invalid");
						throw new DataIntegrityViolationException("Please enter valid PR quantity");

					}*/
						
						
						
				}
				PurchaseRequestDao.savePurchaseRequest(purchaseRequest);
				response.setId(purchaseRequest.getId());
				response.setMessage("Purchase request saved successfully");
//			} 
				/*else {
				throw new CustomException("Please enter valid PR quantity");
				return new ResponseEntity<Object>(new ErrorResponse("HIS-1044", request.getvalidationMessage(),
						new CustomException(request.getvalidationMessage())), HttpStatus.BAD_REQUEST);

			}*/
		}
		/*catch(CustomException cutom) {
			throw cutom;
		}*/
		catch (DataIntegrityViolationException div) {
			throw div;
		} catch (Exception e) {
			
			throw e;
		}

		return response;
	}

	@Override
	@Transactional
	public Object updatePurchaseRequest(PurchaseRequest request) {

	com.param.entity.model.procurement.PurchaseRequest purchaseRequest = PurchaseRequestDao.getPurchaseRequest(request.getId());
	try {
		purchaseRequest.updatePurchaseRequest(new Priority(request.getPriorityId()), request.getRemark());
		for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList())
		{
			Integer id = null;
			for (ItemRequest item : request.getItems())
			{
				if (item.getId() != null && item.getId().equals(detail.getId()))
				{
					id = item.getId();
					if (isQuantityValidated(purchaseRequest.getAssetType().getId(),	purchaseRequest.getStore().getStoreId(),
								detail.getItem().getId(), item.getQuantity(),item.getUomTypeId(), detail.getLeaseQuantity(),request.getOrganizationId(),request.getUnitId())) 
					{

					if (purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId())) 
					{
						Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(),getUom(item.getItemId(),request.getOrganizationId()), new UomType(item.getUomTypeId()));
						detail.updatePurchaseRequestDetail(item.getQuantity() != null ? item.getQuantity() : detail.getQuantity(),
										item.getSpecification() != null ? item.getSpecification(): detail.getSpecification(),
										item.getStatusId() != null ? new Status(item.getStatusId())	: detail.getStatus(),
										item.getUomTypeId() != null ? new UomType(item.getUomTypeId()): detail.getUomType(),
										item.getUomUnitId() != null ? new UomUnit(item.getUomUnitId()): detail.getUomUnit(),
										item.getQuantity() != null ? leaseQuantity : detail.getLeaseQuantity());
						detail.setPendingQuantity(item.getPendingQuantity()!= null ? item.getPendingQuantity() :detail.getPendingQuantity() );
							break;
					} 
					else if (purchaseRequest.getAssetType().getId().equals(Constants.getFixedAssetId()))
					{
								detail.updateFixedPurchaseRequestDetail(
										item.getQuantity() != null ? item.getQuantity() : detail.getQuantity(),
										item.getSpecification() != null ? item.getSpecification()
												: detail.getSpecification(),
										item.getQuantity() != null ? item.getQuantity() : detail.getLeaseQuantity());
								detail.setPendingQuantity(item.getPendingQuantity()!= null ? item.getPendingQuantity() :detail.getPendingQuantity() );
								break;
							}
						} else {
							// CustomException custom = new CustomException("Purchase request Quantity is
							// Invalid;
							throw new DataIntegrityViolationException("Please enter valid PR quantity");
						}

					}
				}

				if (id == null) {
					detail.setIsDeleted(true);
				}

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
				detail.setUpdatedByUser(request.getUserId());
			}

			for (ItemRequest item : request.getItems()) {

				if (item.getId() == null) {
					PurchaseRequestDetail purchaseRequestDetail = null;
					if (isQuantityValidated(purchaseRequest.getAssetType().getId(),
							purchaseRequest.getStore().getStoreId(), item.getItemId(), item.getQuantity(),
							item.getUomTypeId(), 0,request.getOrganizationId(),request.getUnitId())) {
						if (purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId())) {
							Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(), getUom(item.getItemId(),request.getOrganizationId()),
									new UomType(item.getUomTypeId()));
							
							PurchaseRequestDetail prd=new PurchaseRequestDetail(new Item(item.getItemId()),
									item.getQuantity(), item.getSpecification(), new Status(item.getStatusId()),
									new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
									leaseQuantity);
							prd.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							purchaseRequestDetail = purchaseRequest
									.addPurchaseRequestDetailList(prd);
							purchaseRequestDetail.setPendingQuantity(item.getPendingQuantity());
						} else if (purchaseRequest.getAssetType().getId().equals(Constants.getFixedAssetId())) {
							PurchaseRequestDetail prd=new PurchaseRequestDetail(new Item(item.getItemId()),
									item.getQuantity(), item.getSpecification(), new Status(item.getStatusId()),
									item.getQuantity());
							prd.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							purchaseRequestDetail = purchaseRequest
									.addPurchaseRequestDetailList(prd);
							purchaseRequestDetail.setPendingQuantity(item.getPendingQuantity());
						}

						if (purchaseRequest.getRequestType().getId()
								.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType())
								&& item.getIndentId() != null) {
							List<StoreIndentDetail> storeIndentDetails = GlobalDao
									.getStoreIndentDetails(item.getIndentId());
							for (StoreIndentDetail storeIndentDetail : storeIndentDetails) {
								purchaseRequestDetail.addStoreIndentDetailList(storeIndentDetail);
								purchaseRequestDetail.setPendingQuantity(item.getPendingQuantity());
							}
						}
					} else {
						// CustomException custom = new CustomException("Purchase request Quantity is
						// Invalid");
						throw new DataIntegrityViolationException("Please enter valid PR quantity");
					}
				}
			}

		}

		catch (DataIntegrityViolationException div) {
			throw div;
		} catch (Exception e) {
			throw e;
		}

		purchaseRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
		purchaseRequest.setUpdatedByUser(request.getUserId());

		PurchaseRequestDao.updatePurchaseRequest(purchaseRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase request updated successfully");

		return response;
	}

	public Boolean isQuantityValidated(Integer assetTypeId, Integer storeId, Integer itemId, Integer quantity,
			Integer uomTypeId, Integer currentLeaseQunatity,Integer orgId, Integer unitId) {

		Long storeStock = GlobalDao.getStoreStockByItem(storeId, itemId,unitId);
		Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeId, itemId);
		Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeId, itemId,orgId);
		Long prLeaseQunatity = PurchaseRequestDao.getLeaseQunatity(storeId, itemId);
		if (prLeaseQunatity < currentLeaseQunatity) {
			prLeaseQunatity = currentLeaseQunatity - prLeaseQunatity;
		} else if (prLeaseQunatity > currentLeaseQunatity) {
			prLeaseQunatity = prLeaseQunatity - currentLeaseQunatity;
		}
		// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
		// prLeaseQunatity);
		Integer leaseQuantity = quantity;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			leaseQuantity = Util.GetLeaseQuantity(quantity, getUom(itemId,orgId), new UomType(uomTypeId));
		}
		if (maxStoreQuantity < (prLeaseQunatity + leaseQuantity + stockInTransit + storeStock)) {
			return false;
		}

		return true;
	}

	public List<PurchaseOrderUomMapper> getUom(Integer itemId,Integer orgId) {

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

	@Override
	@Transactional
	public Object approvePurchaseRequest(PurchaseRequest request) {

		for (Integer id : request.getPrId()) {
			com.param.entity.model.procurement.PurchaseRequest purchaseRequest = PurchaseRequestDao
					.getPurchaseRequest(id);

			purchaseRequest.setStatus(new Status(request.getStatusId()));
			purchaseRequest.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList()) {
				detail.setStatus(new Status(request.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
				detail.setUpdatedByUser(request.getUserId());
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
	public Object cancelPurchaseRequest(PurchaseRequest request) {

		com.param.entity.model.procurement.PurchaseRequest purchaseRequest = PurchaseRequestDao
				.getPurchaseRequest(request.getId());

		for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		purchaseRequest.setCancelReason(new CancelReason(request.getReasonId()));
		purchaseRequest.setCancelNote(request.getNote());

		purchaseRequest.setStatus(new Status(request.getStatusId()));
		purchaseRequest.setApprovalStatus(new Status(request.getApprovalStatusId()));

		purchaseRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
		purchaseRequest.setUpdatedByUser(request.getUserId());

		PurchaseRequestDao.updatePurchaseRequest(purchaseRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase request cancelled successfully");

		return response;
	}
}
