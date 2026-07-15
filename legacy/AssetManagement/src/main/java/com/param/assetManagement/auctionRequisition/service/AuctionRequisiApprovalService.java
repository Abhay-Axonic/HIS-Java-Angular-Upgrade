package com.param.assetManagement.auctionRequisition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.auctionRequisition.dao.IAuctionRequisiApprovalDao;
import com.param.assetManagement.auctionRequisition.dao.IAuctionRequisitionDao;
import com.param.assetManagement.auctionRequisition.dto.AssetDetailResponse;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionApprovalRequest;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionDropdown;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionListResponse;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;
import com.param.assetManagement.auctionRequisition.dto.ItemAuctionRequisitionAccessoriesRequest;
import com.param.assetManagement.auctionRequisition.dto.ItemAuctionRequisitionApproveRequest;
import com.param.assetManagement.auctionRequisition.dto.ItemAuctionRequisitionResponse;
import com.param.assetManagement.auctionRequisition.dto.RejectReasonResponse;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.AccessoriesDetails;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.asset.AuctionAccessoriesMapper;
import com.param.entity.model.asset.AuctionRequisition;
import com.param.entity.model.asset.AuctionRequisitionDetail;
import com.param.entity.model.asset.ItemAuction;
import com.param.entity.model.asset.ItemAuctionDetail;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("AuctionRequisiApprovalService")
public class AuctionRequisiApprovalService implements IAuctionRequisiApprovalService {

	private Integer screenId = Screen.AuctionRequisitionApproval.getScreen();



@Autowired
private SessionService sessionService;

	@Autowired
	IAuctionRequisiApprovalDao auctionRequisiApprovalDao;

	@Autowired
	IAuctionRequisitionDao auctionRequisitionDao;

	@Autowired
	IGlobalDao globalDao;

	@Override
	@Transactional
	public AuctionRequisitionDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		AuctionRequisitionDropdown response = new AuctionRequisitionDropdown();

		for (ScreenStatus status : globalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (Store store : globalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getAuctionRequisitionApprovalList(AuctionRequisitionRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return auctionRequisiApprovalDao.searchAuctionRequisitionApproval(request, screenId);
	}

	@Override
	@Transactional
	public Object getAuctionRequisitionApproval(Integer id) {
		AuctionRequisition auctionRequisition = auctionRequisitionDao.getAuctionRequisition(id);

		List<RejectReasonResponse> rejectReasons = getRejectReasons();
		AuctionRequisitionListResponse response = new AuctionRequisitionListResponse(auctionRequisition.getId(),
				auctionRequisition.getAuctionRequisitionNo(), auctionRequisition.getAddedDate(),
				auctionRequisition.getAddedBy().getUserId(), auctionRequisition.getAddedBy().getUserName(),
				auctionRequisition.getStore().getStoreName(), auctionRequisition.getStore().getStoreId(),
				auctionRequisition.getStatus().getId(), auctionRequisition.getStatus().getStatus(), rejectReasons);

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
					detail.getBatch().getGoodReceiptNoteDetail() != null
							? detail.getBatch().getGoodReceiptNoteDetail().getFirst().getGoodReceiptNote().getAddedDate()
							: null,
					detail.getBatch() != null ? detail.getBatch().getLotNo() : null,
					detail.getBatch() != null ? detail.getBatch().getCop() : null,
					detail.getBatch() != null ? detail.getBatch().getBatchNo() : null,
					detail.getBatch().getVendor() != null ? detail.getBatch().getVendor().getId() : null,
					detail.getBatch().getVendor() != null ? detail.getBatch().getVendor().getName() : null,
					detail.getBatch() != null ? detail.getBatch().getYearOfManufacture() : null,
					detail.getAuctionQuantity(), assetDetails, detail.getStatus().getId());

			List<AccessoriesDetails> accessories = new ArrayList<AccessoriesDetails>();
			for (AuctionAccessoriesMapper accessoryDetail : detail.getAuctionAccessoriesMapper()) {
				AccessoriesDetails accessory = new AccessoriesDetails(accessoryDetail.getId(),
						accessoryDetail.getBatch().getItem().getId(), accessoryDetail.getBatch().getItem().getCode(),
						accessoryDetail.getBatch().getItem().getName(), accessoryDetail.getSpecification(),
						accessoryDetail.getAuctionQuantity(), accessoryDetail.getApprovedAuctionQuantity(),
						accessoryDetail.getBatch().getId(), accessoryDetail.getBatch().getBatchNo());

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

	private List<RejectReasonResponse> getRejectReasons() {

		List<RejectReasonResponse> list = new ArrayList<RejectReasonResponse>();
		for (RejectReason rejectReason : auctionRequisitionDao.getRejectReasons()) {
			RejectReasonResponse response = new RejectReasonResponse(rejectReason.getId(), rejectReason.getReason());
			list.add(response);
		}
		return list;
	}

	@Override
	@Transactional
	public Object saveAuctionRequisitionApproval(AuctionRequisitionApprovalRequest request) {

		AuctionRequisition auctionRequisition = auctionRequisitionDao.getAuctionRequisition(request.getId());
		auctionRequisition.approveAuctionRequisition(new Status(request.getStatusId()),
				new Status(request.getApprovalStatusId()));

		Random rand = new Random();
		int num = rand.nextInt(1000);
		String itemReqNo = "item-Req-" + num;

		ItemAuction itemAuction = new ItemAuction(itemReqNo,
				new Status(com.param.common.constants.Status.Pending.getStatus()),
				new Store(auctionRequisition.getStore().getStoreId()),
				new AuctionRequisition(auctionRequisition.getId()));
//		sessionService.setEntityDefaults(itemAuction);	
		itemAuction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for (AuctionRequisitionDetail detail : auctionRequisition.getAuctionRequisitionDetails())
		{
			ItemAuctionDetail auctionDetail = null;
			for (ItemAuctionRequisitionApproveRequest item : request.getItems())
			{
				if (item.getId() != null && item.getId().equals(detail.getId()))
				{
					detail.approveAuctionRequisition(
							item.getAcceptedAuctionQuantiity() != null ? item.getAcceptedAuctionQuantiity() : null,
							item.getAuctionPrice() != null ? item.getAuctionPrice() : null,
							item.getTotalAuctionPrice() != null ? item.getTotalAuctionPrice() : null,
							item.getRejectAuctionQuantity() != null ? item.getRejectAuctionQuantity() : null,
							item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
							item.getStatusId() != null ? new Status(item.getStatusId()) : null,
							item.getRejectNote() != null ? item.getRejectNote() : null);

					ItemAuctionDetail itemAuctionDetail=new ItemAuctionDetail(new AuctionRequisitionDetail(detail.getId()),
							new Status(com.param.common.constants.Status.Pending.getStatus()));
//					sessionService.setEntityDefaults(itemAuctionDetail);	
					itemAuctionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					auctionDetail = itemAuction.addItemAuctionRequisiDetail(itemAuctionDetail);
				}

				for (AuctionAccessoriesMapper accessoriesMapper : detail.getAuctionAccessoriesMapper()) {
					for (ItemAuctionRequisitionAccessoriesRequest accessories : item.getAccessories()) {
						if (accessories.getId() != null && accessories.getId().equals(accessoriesMapper.getId())) {
							accessoriesMapper.approveAuctionAccessoriesMapper(accessories.getApprovedQuantity(),
									accessories.getRejectQuantity(), new RejectReason(accessories.getRejectReasonId()),
									accessories.getRejectNote());
						}
					}
				}
			}
//			sessionService.setEntityDefaults(auctionDetail);
			auctionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}
		auctionRequisition.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		auctionRequisition.setUpdatedByUser(request.getUserId());
		auctionRequisitionDao.updateAuctionRequisition(auctionRequisition);

		auctionRequisitionDao.saveItemAuction(itemAuction);

		CommonResponse response = new CommonResponse();
		response.setId(auctionRequisition.getId().longValue());
		response.setMessage("Auction Requisition Approval saved successfully");
		return response;
	}

	@Override
	@Transactional
	public Object approveAuctionRequisitionApproval(AuctionRequisitionApprovalRequest request) {
		for (Integer id : request.getAuctionRequisitionId()) {
			AuctionRequisition auctionRequisition = auctionRequisitionDao.getAuctionRequisition(id);

			Random rand = new Random();
			int num = rand.nextInt(1000);
			String itemReqNo = "item-Req-" + num;

			ItemAuction itemAuction = new ItemAuction(itemReqNo,
					new Status(com.param.common.constants.Status.Pending.getStatus()),
					new Store(auctionRequisition.getStore().getStoreId()),
					new AuctionRequisition(auctionRequisition.getId()));
//			sessionService.setEntityDefaults(itemAuction);	
			itemAuction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			for (AuctionRequisitionDetail auctionRequisitionDetail : auctionRequisition
					.getAuctionRequisitionDetails())
			{
				auctionRequisitionDetail.setStatus(new Status(request.getStatusId()));
				auctionRequisitionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
//				auctionRequisitionDetail.setUpdatedBy(itemAuction.getUser());
				auctionRequisitionDetail.setUpdatedByUser(request.getUserId());

				ItemAuctionDetail itemAuctionDetail1=new ItemAuctionDetail(new ItemAuction(itemAuction.getId()),
						new AuctionRequisitionDetail(auctionRequisitionDetail.getId()));
//				sessionService.setEntityDefaults(itemAuctionDetail1);	
				itemAuctionDetail1.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				itemAuctionDetail1.setStatus(new Status(request.getStatusId()));
				ItemAuctionDetail itemAuctionDetail = itemAuction.addItemAuctionRequisiDetail(itemAuctionDetail1);
//				sessionService.setEntityDefaults(itemAuctionDetail);
				itemAuctionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				itemAuctionDetail.setStatus(new Status(request.getStatusId()));
			}
			auctionRequisition.setStatus(new Status(request.getStatusId()));
			auctionRequisition.setApprovalStatus(new Status(request.getApprovalStatusId()));
			auctionRequisition.setApprovedOn(TimeZoneComponent.getDateByzone(request.getUnitId()));
			auctionRequisition.setApprovedBy(new User(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId()));
			auctionRequisition.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
//			auctionRequisition.setUpdatedBy(auctionRequisition.getUser());
			auctionRequisition.setUpdatedByUser(request.getUserId());
			auctionRequisitionDao.updateAuctionRequisition(auctionRequisition);

			auctionRequisitionDao.saveItemAuction(itemAuction);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Auction requisition approval approved successfully");

		return response;
	}

}
