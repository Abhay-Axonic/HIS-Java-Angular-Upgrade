package com.param.assetManagement.auctionRequisition.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.auctionRequisition.dao.IAuctionRequisitionDao;
import com.param.assetManagement.auctionRequisition.dto.AssetDetailResponse;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionDropdown;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionListResponse;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;
import com.param.assetManagement.auctionRequisition.dto.ItemAuctionRequisitionResponse;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.AccessoriesDetails;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.asset.AuctionAccessoriesMapper;
import com.param.entity.model.asset.AuctionRequisition;
import com.param.entity.model.asset.AuctionRequisitionDetail;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("AuctionRequisitionService")
public class AuctionRequisitionService implements IAuctionRequisitionService {

	private Integer screenId = Screen.AuctionRequisition.getScreen();

	@Autowired
	IAuctionRequisitionDao auctionRequisitionDao;
	

@Autowired
private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Override
	@Transactional
	public AuctionRequisitionDropdown getDropdownList(Integer unitId, Integer userId) {
		AuctionRequisitionDropdown response = new AuctionRequisitionDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		/*for (Store store : GlobalDao.getUserStoreListBP()) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}*/
		for (com.param.global.dto.global.Store store : GlobalDao.getUserStoreListBP(userId,unitId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getId(), store.getStore()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getAuctionRequisitionList(AuctionRequisitionRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return auctionRequisitionDao.searchAuctionRequisition(request, screenId);
	}

	@Override
	@Transactional
	public Object getAuctionRequisition(Integer id,Integer unitId, Integer orgId, Integer userId) {

		AuctionRequisition auctionRequisition = auctionRequisitionDao.getAuctionRequisition(id);

		AuctionRequisitionListResponse response = new AuctionRequisitionListResponse(auctionRequisition.getId(),
				auctionRequisition.getAuctionRequisitionNo(), auctionRequisition.getAddedDate(),
				auctionRequisition.getAddedBy().getUserId(), auctionRequisition.getAddedBy().getUserName(),
				auctionRequisition.getStore().getStoreName(), auctionRequisition.getStore().getStoreId(),
				auctionRequisition.getStatus().getId(), auctionRequisition.getStatus().getStatus());

		List<ItemAuctionRequisitionResponse> items = new ArrayList<ItemAuctionRequisitionResponse>();
		for (AuctionRequisitionDetail detail : auctionRequisition.getAuctionRequisitionDetails()) {

			List<AssetDetailResponse> assetDetails = getAssetDetails(detail);

			ItemAuctionRequisitionResponse requisitionResponse = new ItemAuctionRequisitionResponse(detail.getId(),
				detail.getBatch() != null ? detail.getBatch().getItem().getId() : null,
				detail.getBatch() != null ? detail.getBatch().getItem().getCode() : null,
				detail.getBatch() != null ? detail.getBatch().getItem().getName() : null,
				detail.getBatch() != null ? detail.getBatch().getModelNumber() : null,
				detail.getBatch().getGoodReceiptNoteDetail() != null
						? detail.getBatch().getGoodReceiptNoteDetail().getFirst().getGoodReceiptNote().getGrnNumber()
						: null,
				detail.getBatch().getGoodReceiptNoteDetail() != null
						? detail.getBatch().getGoodReceiptNoteDetail().getFirst().getGoodReceiptNote().getId()
						: null,
				detail.getBatch() != null ? detail.getBatch().getBatchNo() : null,
				detail.getBatch().getVendor() != null ? detail.getBatch().getVendor().getId() : null,
				detail.getBatch().getVendor() != null ? detail.getBatch().getVendor().getName() : null,
				detail.getBatch() != null ? detail.getBatch().getYearOfManufacture() : null,
				detail.getAuctionQuantity(),
				GlobalDao.getStoreStockByItem(detail.getAuctionRequisition().getStore().getStoreId(),detail.getBatch().getItem().getId(),unitId),
					assetDetails, detail.getRemark(), detail.getBatch() != null ? detail.getBatch().getId() : null);

			List<AccessoriesDetails> accessories = new ArrayList<AccessoriesDetails>();
			for (AuctionAccessoriesMapper accessoryDetail : detail.getAuctionAccessoriesMapper()) {
				AccessoriesDetails accessory = new AccessoriesDetails(accessoryDetail.getId(),
						accessoryDetail.getBatch().getItem().getId(), accessoryDetail.getBatch().getItem().getCode(),
						accessoryDetail.getBatch().getItem().getName(), accessoryDetail.getBatch().getId(),
						accessoryDetail.getBatch().getBatchNo(), accessoryDetail.getSpecification(),
						accessoryDetail.getAuctionQuantity(), accessoryDetail.getApprovedAuctionQuantity());

				accessories.add(accessory);
			}
			requisitionResponse.setAccessoriesDetails(accessories);
			items.add(requisitionResponse);
		}
		response.setItems(items);
		return response;
	}

	private List<AssetDetailResponse> getAssetDetails(AuctionRequisitionDetail detail) {

		List<AssetDetailResponse> list = new ArrayList<AssetDetailResponse>();
		for (AuctionAccessoriesMapper mapper : detail.getAuctionAccessoriesMapper()) {
			AssetDetailResponse response = new AssetDetailResponse(mapper.getId(),
					mapper.getBatch() != null
							? mapper.getBatch().getItem().getProductCategory().getAssetCategory().getAssetType().getId()
							: null,
					mapper.getBatch() != null
							? mapper.getBatch().getItem().getProductCategory().getAssetCategory().getAssetType()
									.getCode()
							: null,
					mapper.getBatch() != null
							? mapper.getBatch().getItem().getProductCategory().getAssetCategory().getAssetType()
									.getType()
							: null,
					mapper.getBatch() != null ? mapper.getBatch().getBatchNo() : null,
					mapper.getBatch() != null ? mapper.getBatch().getYearOfManufacture() : null,
					mapper.getBatch() != null ? mapper.getBatch().getId() : null);
			list.add(response);
		}
		return list;
	}

	@Override
	@Transactional
	public Object saveAuctionRequisition(AuctionRequisitionListResponse request) {

		AuctionRequisition auctionRequisition = new AuctionRequisition(new Store(request.getStoreId()),
				request.getRemark(), new Status(request.getStatusId()), "auc_reqno",
				new Status(request.getApprovalStatusId()));

		for (ItemAuctionRequisitionResponse item : request.getItems()) {
			AuctionRequisitionDetail quctionRequisitionDetail1=new AuctionRequisitionDetail(new Batch(item.getBatchId()),
					item.getRemark(), item.getAuctionQty(), new Status(item.getStatusId()));
//			sessionService.setEntityDefaults(quctionRequisitionDetail1);
			quctionRequisitionDetail1.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			AuctionRequisitionDetail auctionRequisitionDetail = auctionRequisition.addAuctionRequisitionDetailList(quctionRequisitionDetail1);
//			sessionService.setEntityDefaults(auctionRequisitionDetail);	
			quctionRequisitionDetail1.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			if(item.getAccessories()!=null && item.getAccessories().size()>0) {
			for (AssetDetailResponse acceessories : item.getAccessories())
			{
				AuctionAccessoriesMapper auctionAccessoriesMapper=new AuctionAccessoriesMapper(new Batch(acceessories.getBatchId()),
						acceessories.getAuctionQuantity(), acceessories.getSpecification());
//				sessionService.setEntityDefaults(auctionAccessoriesMapper);	
				auctionAccessoriesMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				auctionRequisitionDetail.addAuctionAccessoriesMapper(auctionAccessoriesMapper);

			}
			}
		}
//		sessionService.setEntityDefaults(auctionRequisition);
		auctionRequisition.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		auctionRequisitionDao.saveAuctionRequisition(auctionRequisition);
		CommonResponse response = new CommonResponse();
		response.setId(auctionRequisition.getId().longValue());
		response.setMessage("Auction Requisition saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveAuctionRequisition(AuctionRequisitionListResponse request) {
		for (Integer id : request.getAuctionRequisitionId()) {
			AuctionRequisition auctionRequisition = auctionRequisitionDao.getAuctionRequisition(id);
			auctionRequisition.setStatus(new Status(request.getStatusId()));
			auctionRequisition.setApprovalStatus(new Status(request.getApprovalStatusId()));
			auctionRequisition.setApprovedOn(TimeZoneComponent.getDateByzone(auctionRequisition.getUnitId()));
			auctionRequisition.setApprovedBy(new User(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId()));
			auctionRequisition.setUpdatedDate(TimeZoneComponent.getDateByzone(auctionRequisition.getUnitId()));
//			auctionRequisition.setUpdatedBy(auctionRequisition.getUser());
			auctionRequisition.setUpdatedByUser(request.getUserId());
			auctionRequisitionDao.updateAuctionRequisition(auctionRequisition);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Auction requisition approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelAuctionRequisition(AuctionRequisitionListResponse request) {
		AuctionRequisition auctionRequisition = auctionRequisitionDao.getAuctionRequisition(request.getId());

		auctionRequisition.setCancelReason(new CancelReason(request.getCancelReasonId()));
		auctionRequisition.setCancelNote(request.getCancelNote());
		auctionRequisition.setStatus(new Status(request.getStatusId()));
		auctionRequisition.setUpdatedDate(TimeZoneComponent.getDateByzone(auctionRequisition.getUnitId()));
//		auctionRequisition.setUpdatedBy(auctionRequisition.getUser());
		auctionRequisition.setUpdatedByUser(request.getUserId());
		auctionRequisitionDao.updateAuctionRequisition(auctionRequisition);

		CommonResponse response = new CommonResponse();
		response.setMessage("Auction requisition cancelled successfully");
		return response;
	}

	@Override
	@Transactional
	public Object updateAuctionRequisition(AuctionRequisitionListResponse request) {

		AuctionRequisition auctionRequisition = auctionRequisitionDao.getAuctionRequisition(request.getId());
		auctionRequisition.updateAuctionRequisition(new Store(request.getStoreId()), request.getRemark());

		for (AuctionRequisitionDetail detail : auctionRequisition.getAuctionRequisitionDetails()) {
			Integer id = null;
			for (ItemAuctionRequisitionResponse item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateAuctionRequisitionDetail(new Batch(item.getBatchId()), item.getAuctionQty(),
							item.getRemark());

					for (AuctionAccessoriesMapper auctionAccessoriesMapper : detail.getAuctionAccessoriesMapper()) {
						Integer mapperId = null;
						for (AssetDetailResponse assetDetailResponse : item.getAccessories()) {
							if (assetDetailResponse.getId() != null
									&& assetDetailResponse.getId().equals(auctionAccessoriesMapper.getId())) {
								mapperId = assetDetailResponse.getId();
								auctionAccessoriesMapper.updateAuctionAccessoriesMapper(
										new Batch(assetDetailResponse.getBatchId()),
										assetDetailResponse.getAuctionQuantity(),
										assetDetailResponse.getSpecification());
								break;
							}
						}
						if (mapperId == null) {
							auctionAccessoriesMapper.setIsDeleted(true);
						}
					}
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(auctionRequisition.getUnitId()));
//			detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}
		auctionRequisition.setUpdatedDate(TimeZoneComponent.getDateByzone(auctionRequisition.getUnitId()));
		auctionRequisition.setUpdatedByUser(request.getUserId());
		auctionRequisitionDao.updateAuctionRequisition(auctionRequisition);

		CommonResponse response = new CommonResponse();
		response.setMessage("Auction Requisition updated successfully");
		return response;
	}
}
