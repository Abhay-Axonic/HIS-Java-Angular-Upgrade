package com.param.procurement.purchase.service.localPurchaseRequest;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dao.localPurchaseRequest.ILocalPurchaseRequestDao;
import com.param.procurement.purchase.dto.localPurchaseRequest.ItemRequest;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseReqDetailsResponseDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestMasterResponseDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestSearchDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseReqStatusDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseRequestDropDownResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
@Transactional
public class LocalPurchaseRequestService implements ILocalPurchaseRequestService {

	private Integer screenId = Screen.LocalPurchaseRequest.getScreen();

	private Integer localPurchaseReqId = 5;

	@Autowired
	private SessionService sessionService;
	@Autowired
	IItemsDao ItemsDao;
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IItemsDao itemsDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	private ILocalPurchaseRequestDao locPurchaseReqDao;

	@Override
	@Transactional
	public LocalPurchaseRequestDropDownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {

		LocalPurchaseRequestDropDownResponse purchaseReqDropDown = new LocalPurchaseRequestDropDownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			purchaseReqDropDown.addRequestingStore(
					new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			purchaseReqDropDown
					.addDeliveryStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
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

		for (PurchaseRequest purchaseRequest : locPurchaseReqDao.searchLocalPuchaseRequest(locPurchaseReqDto, screenId,
				localPurchaseReqId)) {
			LocPurchaseRequestSearchDto purchaseReqSearchDto = new LocPurchaseRequestSearchDto(purchaseRequest.getId(),
					purchaseRequest.getAddedDate(), purchaseRequest.getPurchaseNo(),
					purchaseRequest.getStore().getStoreName(), purchaseRequest.getAssetType().getType(),
					purchaseRequest.getRequestType().getType(), purchaseRequest.getPriority().getPriorityDesc(),
					purchaseRequest.getApprovalStatus().getStatus(), purchaseRequest.getStatus().getStatus(),
					purchaseRequest.getAddedBy().getUserName(), purchaseRequest.getRemark(),
					purchaseRequest.getRequestingStore().getStoreName());

			response.add(purchaseReqSearchDto);
		}
		return response;
	}

	@Override
	@Transactional
	public Object saveLocalPurchaseRequest(LocPurchaseRequestDto locPurchaseReqDto) {

		CommonResponse response = new CommonResponse();
		try {
			Integer id = 0;

			/*
			 * Random rand = new Random(); int num = rand.nextInt(100); String purchaseNo =
			 * "LPR-" + num;
			 */
			/*
			 * Integer storeId = locPurchaseReqDto.getStoreId();
			 * 
			 * Store storeNew = GlobalDao.getStore(storeId);
			 * 
			 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
			 */

			String loaclPurNumber = GlobalService.getBillNoByGeneratedCode(locPurchaseReqDto.getStoreId(),
					locPurchaseReqDto.getUnitId(), screenId);

			PurchaseRequest purchaseRequest = new PurchaseRequest(new Status(locPurchaseReqDto.getApprovalStatusId()),
					new AssetType(locPurchaseReqDto.getAssetTypeId()), new Priority(locPurchaseReqDto.getPriorityId()),
					loaclPurNumber, locPurchaseReqDto.getRemark(), new RequestType(locPurchaseReqDto.getPrTypeId()),
					new com.param.entity.model.master.Screen(screenId), new Status(locPurchaseReqDto.getStatusId()),
					new Store(locPurchaseReqDto.getStoreId()), new Store(locPurchaseReqDto.getRequestingStoreId()));
//		sessionService.setEntityDefaults(purchaseRequest);
			purchaseRequest.setCreateDefaults(locPurchaseReqDto.getOrganizationId(), locPurchaseReqDto.getUnitId(),
					locPurchaseReqDto.getUserId());
			for (ItemRequest item : locPurchaseReqDto.getItems()) {
				if (isQuantityValidated(locPurchaseReqDto.getAssetTypeId(), locPurchaseReqDto.getStoreId(),
						item.getItemId(), item.getQuantity(), item.getUomTypeId(),
						locPurchaseReqDto.getOrganisationId(), locPurchaseReqDto.getUnitId())) {
					PurchaseRequestDetail purchaseRequestDetail = null;
					if (locPurchaseReqDto.getAssetTypeId().equals(Constants.getConsumableId())) {
						Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(),
								getUom(item.getItemId(), locPurchaseReqDto.getOrganisationId()),
								new UomType(item.getUomTypeId()));

						purchaseRequestDetail = new PurchaseRequestDetail(item.getQuantity(),
								new Item(item.getItemId()), item.getIndentQuantity(), new Status(item.getStatusId()),
								item.getCop(), item.getAmountCop(), new UomType(item.getUomTypeId()),
								new UomUnit(item.getUomUnitId()), item.getSpecification(), leaseQuantity);
						purchaseRequestDetail
								.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity() : 0);
					} else if (locPurchaseReqDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
						purchaseRequestDetail = new PurchaseRequestDetail(new Item(item.getItemId()),
								item.getQuantity(), item.getSpecification(), new Status(item.getStatusId()),
								item.getCop(), item.getAmountCop(), item.getQuantity());
						purchaseRequestDetail
								.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity() : 0);
					}

					/**
					 * If request type id is against indent (2) & indent list is not null, then
					 * proceed to add Store indent details list
					 */
					if (locPurchaseReqDto.getPrTypeId()
							.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType())
							&& item.getIndentId() != null
							&& locPurchaseReqDto.getAssetTypeId().equals(Constants.getConsumableId())) {
						purchaseRequestDetail.setIndentQuantity(item.getIndentQuantity());
						List<StoreIndentDetail> storeIndentDetails = GlobalDao
								.getStoreIndentDetails(item.getIndentId());
						for (StoreIndentDetail storeIndentDetail : storeIndentDetails) {
							purchaseRequestDetail.addStoreIndentDetailList(storeIndentDetail);
						}
					}

					else if (locPurchaseReqDto.getPrTypeId()
							.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType())
							&& item.getIndentId() != null
							&& locPurchaseReqDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
						purchaseRequestDetail.setIndentQuantity(item.getIndentQuantity());
						List<AssetIndentDetail> storeIndentDetails = new ArrayList<>();

						storeIndentDetails.add(GlobalDao.getAssetIndentDetail(item.getIndentId().getFirst()));

						for (AssetIndentDetail storeIndentDetail : storeIndentDetails) {
							purchaseRequestDetail.addassetIndentDetailList(storeIndentDetail);
						}
					}
//		sessionService.setEntityDefaults(purchaseRequestDetail);
					purchaseRequestDetail.setCreateDefaults(locPurchaseReqDto.getOrganizationId(),
							locPurchaseReqDto.getUnitId(), locPurchaseReqDto.getUserId());
					purchaseRequest.addPurchaseRequestDetailList(purchaseRequestDetail);
				} else {
					throw new DataIntegrityViolationException("Purchase request Quantity is Invalid");

				}

			}
			locPurchaseReqDao.saveLocalPurchaseRequest(purchaseRequest);
			response.setId(purchaseRequest.getId().longValue());
			response.setMessage("Local Purchase Request saved successfully");
		} catch (DataIntegrityViolationException div) {
			throw div;
		} catch (Exception e) {
			throw e;
		}
		return response;

	}

	@Override
	@Transactional
	public Object updateLocalPurchaseReq(LocPurchaseRequestDto locPurchaseReqDto) {
		PurchaseRequest purchaseRequest = locPurchaseReqDao.getPurchaseRequest(locPurchaseReqDto.getId());
		try {
			purchaseRequest.updatePurchaseRequest(new Priority(locPurchaseReqDto.getPriorityId()),
					locPurchaseReqDto.getRemark());

			for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList()) {
				Integer id = null;

				for (ItemRequest item : locPurchaseReqDto.getItems()) {
					if (item.getId() != null && item.getId().equals(detail.getId())) {
						id = item.getId();

//					if(isQuantityValidated( purchaseRequest.getAssetType().getId(),purchaseRequest.getStore().getStoreId(), item.getItemId(), item.getQuantity(),item.getUomTypeId())) {

						if (purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId())) {
							List<UomResponse> uomResponse = GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(),
									locPurchaseReqDto.getOrganisationId());
							List<PurchaseOrderUomMapper> prUomResponse = new ArrayList<PurchaseOrderUomMapper>();
							for (UomResponse uom : uomResponse) {
								PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(
										new UomType(uom.getUomTypeId()), new UomUnit(uom.getUomUnitId()),
										uom.getConversion(), uom.getIpUom(), uom.getOpUom(), uom.getStoreUom());
								prUomResponse.add(prUom);

							}

							detail.updatePurchaseRequestDetail(
									new Item(item.getItemId() != null ? item.getItemId() : detail.getItem().getId()),
									item.getQuantity() != null ? item.getQuantity() : detail.getQuantity(),
									item.getCop() != null ? item.getCop() : detail.getCop(),
									item.getAmountCop() != null ? item.getAmountCop() : detail.getAmountCop(),
									new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
									item.getSpecification() != null ? item.getSpecification()
											: detail.getSpecification(),
									item.getQuantity() != null
											? Util.ConvertQuantityByUomForLocalPurReq(item.getQuantity(),
													detail.getItem().getUnitOfMeasurementList(), detail.getUomType())
											: detail.getLeaseQuantity());
							detail.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity()
									: detail.getPendingQuantity());
							break;
						} else if (purchaseRequest.getAssetType().getId().equals(Constants.getFixedAssetId())) {
							detail.updateFixedPurchaseRequestDetail(
									new Item(item.getItemId() != null ? item.getItemId() : detail.getItem().getId()),
									item.getQuantity() != null ? item.getQuantity() : detail.getQuantity(),
									item.getCop() != null ? item.getCop() : detail.getCop(),
									item.getAmountCop() != null ? item.getAmountCop() : detail.getAmountCop(),
									item.getSpecification() != null ? item.getSpecification()
											: detail.getSpecification());
							detail.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity()
									: detail.getPendingQuantity());
						}
//					}	
//					else {
//						//CustomException custom = new CustomException("Purchase request Quantity is Invalid");
//						throw new DataIntegrityViolationException("Purchase request Quantity is Invalid");
//						}

					}
				}
				if (id == null) {
					detail.setIsDeleted(true);
				}

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
				detail.setUpdatedByUser(locPurchaseReqDto.getUserId());
			}

			for (ItemRequest item : locPurchaseReqDto.getItems()) {
				if (item.getId() == null) {
					PurchaseRequestDetail purchaseRequestDetail = null;
					if (purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId())) {
						Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(),
								getUom(item.getItemId(), locPurchaseReqDto.getOrganisationId()),
								new UomType(item.getUomTypeId()));
						PurchaseRequestDetail prd = new PurchaseRequestDetail(item.getQuantity(),
								new Item(item.getItemId()), item.getIndentQuantity(), new Status(item.getStatusId()),
								item.getCop(), item.getAmountCop(), new UomType(item.getUomTypeId()),
								new UomUnit(item.getUomUnitId()), item.getSpecification(), leaseQuantity);
						prd.setCreateDefaults(purchaseRequest.getOrganizationId(), purchaseRequest.getUnitId(),
								purchaseRequest.getAddedByUser());
						purchaseRequestDetail = purchaseRequest.addPurchaseRequestDetailList(prd);
						purchaseRequestDetail
								.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity() : 0);
					} else if (purchaseRequest.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						PurchaseRequestDetail prd = new PurchaseRequestDetail(new Item(item.getItemId()),
								item.getQuantity(), item.getSpecification(), new Status(item.getStatusId()),
								item.getCop(), item.getAmountCop(), item.getQuantity());
						prd.setCreateDefaults(purchaseRequest.getOrganizationId(), purchaseRequest.getUnitId(),
								purchaseRequest.getAddedByUser());
						purchaseRequestDetail = purchaseRequest.addPurchaseRequestDetailList(prd);
						purchaseRequestDetail
								.setPendingQuantity(item.getPendingQuantity() != null ? item.getPendingQuantity() : 0);
					}

					if (purchaseRequest.getRequestType().getId() == 2 && item.getIndentId() != null) {
						List<StoreIndentDetail> storeIndentDetails = GlobalDao
								.getStoreIndentDetails(item.getIndentId());
						for (StoreIndentDetail storeIndentDetail : storeIndentDetails) {
							purchaseRequestDetail.addStoreIndentDetailList(storeIndentDetail);
						}
					}

					else if (purchaseRequest.getRequestType().getId() == 1) {
						// CustomException custom = new CustomException("Purchase request Quantity is
						// Invalid");
//					throw new DataIntegrityViolationException("Purchase request Quantity is Invalid");
						locPurchaseReqDao.saveLocalPurchaseRequestDetail(purchaseRequestDetail);
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
		purchaseRequest.setUpdatedByUser(locPurchaseReqDto.getUserId());

		locPurchaseReqDao.updateLocalPurchaseRequest(purchaseRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Local Purchase Request updated successfully");

		return response;
	}

	private Boolean isQuantityValidated(Integer assetTypeId, Integer storeId, Integer itemId, Integer quantity,
			Integer uomTypeId, Integer orgId, Integer unitId) {

		Long storeStock = GlobalDao.getStoreStockByItem(storeId, itemId, unitId);
		Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeId, itemId);
		Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeId, itemId, orgId);
		// Long prLeaseQunatity = locPurchaseReqDao.getLeaseQunatity(storeId, itemId);

		Long prLeaseQunatity = ItemsDao.getApprovedPRandPOQuantity(storeId, itemId,unitId);
		Integer leaseQuantity = quantity;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			leaseQuantity = Util.GetLeaseQuantity(quantity, getUom(itemId, orgId), new UomType(uomTypeId));
		}
		if (maxStoreQuantity < (prLeaseQunatity + leaseQuantity + stockInTransit + storeStock)) {
			return false;
		}

		return true;
	}

	private List<PurchaseOrderUomMapper> getUom(Integer itemId, Integer orgId) {

		List<UomResponse> uomResponse = GlobalDao.getUnitOfMeasurementListByItem(itemId, orgId);
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
	public Object getLocalPurchaseRequest(Long id, Integer unitId, Integer orgId) {

		PurchaseRequest purchaseRequest = locPurchaseReqDao.getPurchaseRequest(id);

		LocPurchaseRequestMasterResponseDto purchaseReqDetailsDto = new LocPurchaseRequestMasterResponseDto(
				purchaseRequest.getId(), purchaseRequest.getAddedDate(), purchaseRequest.getPurchaseNo(),
				purchaseRequest.getStore().getStoreId(), purchaseRequest.getStore().getStoreName(),
				purchaseRequest.getAssetType().getId(), purchaseRequest.getAssetType().getType(),
				purchaseRequest.getRequestType().getId(), purchaseRequest.getRequestType().getType(),
				purchaseRequest.getPriority().getPriorityId(), purchaseRequest.getPriority().getPriorityDesc(),
				purchaseRequest.getApprovalStatus().getId(), purchaseRequest.getApprovalStatus().getStatus(),
				purchaseRequest.getStatus().getId(), purchaseRequest.getStatus().getStatus(),
				purchaseRequest.getRemark(), purchaseRequest.getRequestingStore().getStoreId(),
				purchaseRequest.getRequestingStore().getStoreName());

		for (PurchaseRequestDetail detail : locPurchaseReqDao.getPurchaseRequestDetail(id)) {
			if (detail.getIsActive() != false && detail.getIsDeleted() != true) {
				Long storeStock = itemsDao.getStockAvailable(detail.getPurchaseRequest().getStore().getStoreId(),
						detail.getItem().getId(), unitId);
				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(
						detail.getPurchaseRequest().getStore().getStoreId(), detail.getItem().getId());
				Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(
						detail.getPurchaseRequest().getStore().getStoreId(), detail.getItem().getId(), orgId);
				Long approvedPRAndPOQuantity = itemsDao.getApprovedPRandPOQuantity(
						detail.getPurchaseRequest().getStore().getStoreId(), detail.getItem().getId(), unitId);
				Long approvedPOQuantity = itemsDao.getApprovedPurchaseOrderQuantity(
						detail.getPurchaseRequest().getStore().getStoreId(), detail.getItem().getId(), unitId);

				// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
				// approvedPRAndPOQuantity + approvedPOQuantity);
				Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
						approvedPRAndPOQuantity, approvedPOQuantity);

				List<Integer> indentId = new ArrayList<>();
				Integer indentQuantity = 0;

				if (detail.getPurchaseRequest().getRequestType().getId()
						.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType())
						&& purchaseReqDetailsDto.getAssetTypeId().equals(Constants.getConsumableId())) {

					/*
					 * for (StoreIndentDetail storeIndentDetail : detail.getStoreIndentDetailList())
					 * { indentId.add(storeIndentDetail.getId()); indentQuantity = indentQuantity +
					 * storeIndentDetail.getPendingQuantity(); }
					 */
					indentQuantity = detail.getIndentQuantity();
				}
				if (detail.getPurchaseRequest().getRequestType().getId()
						.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType())
						&& purchaseReqDetailsDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					indentQuantity = detail.getIndentQuantity();
					/*
					 * for (AssetIndentDetail storeIndentDetail : detail.getAssetIndentDetailList())
					 * { indentId.add(storeIndentDetail.getId()); indentQuantity = indentQuantity +
					 * storeIndentDetail.getPendingQuantity(); }
					 */
				}
				if (purchaseReqDetailsDto.getAssetTypeId().equals(Constants.getConsumableId())) {
					LocPurchaseReqDetailsResponseDto data = new LocPurchaseReqDetailsResponseDto(detail.getId(),
							detail.getItem().getId(), detail.getItem().getCode(), detail.getItem().getName(),
							detail.getUomType().getId().shortValue(), detail.getUomType().getType(),
							detail.getUomUnit().getId().shortValue(), detail.getUomUnit().getUnits(),
							GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(), orgId),
							detail.getQuantity(), detail.getCop(),
							GlobalDao.getStoreStockByItem(purchaseRequest.getStore().getStoreId(),
									detail.getItem().getId(), unitId),
							detail.getRejectQty() != null ? detail.getRejectQty() : 0, detail.getAmountCop(),
							detail.getSpecification(), indentQuantity, detail.getApprovedQuantity(), maxStoreQuantity,
							maxOrderQuantity, detail.getStatus().getId(), detail.getStatus().getStatus(), indentId, detail.getItem().getIsThreeUOMApplicable());
					data.setPendingQuantity(detail.getPendingQuantity());
					purchaseReqDetailsDto.addItems(data);
				} else if (purchaseReqDetailsDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					LocPurchaseReqDetailsResponseDto d = new LocPurchaseReqDetailsResponseDto(detail.getId(),
							detail.getItem().getId(), detail.getItem().getCode(), detail.getItem().getName(),
							GlobalDao.getStoreStockByItem(purchaseRequest.getStore().getStoreId(),
									detail.getItem().getId(), unitId),
							detail.getApprovedQuantity(), maxStoreQuantity, maxOrderQuantity,
							detail.getRejectQty() != null ? detail.getRejectQty() : 0, detail.getAmountCop(),
							detail.getCop(), detail.getQuantity(), indentQuantity, indentId, detail.getSpecification(),
							detail.getStatus().getId(), detail.getStatus().getStatus(), detail.getItem().getIsThreeUOMApplicable());
					d.setPendingQuantity(detail.getPendingQuantity());
					purchaseReqDetailsDto.addItems(d);
				}

			}

		}
		return purchaseReqDetailsDto;
	}

	@Transactional
	private List<Integer> getIndentIdList(Integer purchaseReqDetailsId) {
		List<Integer> indentIdList = new ArrayList<Integer>();

		for (StoreIndentDetail storeIndent : locPurchaseReqDao.getIndentIdList(purchaseReqDetailsId)) {
			indentIdList.add(storeIndent.getId());

		}

		return indentIdList;
	}

	@Override
	@Transactional
	public Object approvePurchaseRequest(LocalPurchaseReqStatusDto locPurchaseReqStatusDto) {

		for (Long id : locPurchaseReqStatusDto.getPrId()) {
			PurchaseRequest purchaseRequest = locPurchaseReqDao.getPurchaseRequest(id);

			purchaseRequest.setStatus(new Status(locPurchaseReqStatusDto.getStatusId()));
			purchaseRequest.setApprovalStatus(new Status(locPurchaseReqStatusDto.getApprovalStatusId()));

			for (PurchaseRequestDetail detail : purchaseRequest.getPurchaseRequestDetailList()) {
				detail.setStatus(new Status(locPurchaseReqStatusDto.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
				detail.setUpdatedByUser(locPurchaseReqStatusDto.getUserId());
			}

			purchaseRequest.setApprovedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
			purchaseRequest.setApprovedBy(new User(locPurchaseReqStatusDto.getUserId()));

			purchaseRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseRequest.getUnitId()));
			purchaseRequest.setUpdatedByUser(locPurchaseReqStatusDto.getUserId());

			locPurchaseReqDao.updateLocalPurchaseRequest(purchaseRequest);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Local Purchase Request approved successfully");

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

}
