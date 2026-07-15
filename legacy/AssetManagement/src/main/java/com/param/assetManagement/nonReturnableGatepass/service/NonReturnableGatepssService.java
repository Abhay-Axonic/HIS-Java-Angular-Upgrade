package com.param.assetManagement.nonReturnableGatepass.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.auctionVendorApproval.dao.IAuctionVendorApprovalDao;
import com.param.assetManagement.nonReturnableGatepass.dao.INonReturnableGatepssDao;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssRequest;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssRequestAccessories;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssRequestItem;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssResponse;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssResponseItem;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.asset.AuctionRequisition;
import com.param.entity.model.asset.AuctionRequisitionDetail;
import com.param.entity.model.asset.NonReturnableGatepass;
import com.param.entity.model.asset.NonReturnableGatepassAccessoriesMapper;
import com.param.entity.model.asset.NonReturnableGatepassDetail;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("NonReturnableGatepssService")
public class NonReturnableGatepssService implements INonReturnableGatepssService {

	private Integer screenId = Screen.AssetNonReturnable.getScreen();

	@Autowired
	INonReturnableGatepssDao nonReturnableGatepssDao;

	@Autowired
	IAuctionVendorApprovalDao auctionVendorApprovalDao;

	@Autowired
	IItemsDao itemsDao;

	@Autowired
	IGlobalDao GlobalDao;
	

@Autowired
private SessionService sessionService;

	@Override
	@Transactional
	public CommonDropdown getDropdownList() {
		CommonDropdown response = new CommonDropdown();

		for (Store store : GlobalDao.getStoreList(null)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getNonReturnableGatepssList(NonReturnableGatepssRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<NonReturnableGatepssResponse> response = new ArrayList<NonReturnableGatepssResponse>();
		for (NonReturnableGatepass nonReturnableGatepass : nonReturnableGatepssDao.searchNonReturnableGatepass(request,
				screenId)) {
			NonReturnableGatepssResponse nonReturnableGatepssResponse = new NonReturnableGatepssResponse(
					nonReturnableGatepass.getId(), nonReturnableGatepass.getAddedDate(),
					nonReturnableGatepass.getNonRetGpNo(), nonReturnableGatepass.getStore().getStoreName(),
					nonReturnableGatepass.getStore().getStoreId(), nonReturnableGatepass.getVendor().getName(),
					nonReturnableGatepass.getVendor().getId(), nonReturnableGatepass.getAddedBy().getUserName(),
					nonReturnableGatepass.getStatus().getStatus(), nonReturnableGatepass.getStatus().getId(),
					nonReturnableGatepass.getRequestType().getId(),
					(nonReturnableGatepass.getApprovalStatus() != null)
							? nonReturnableGatepass.getApprovalStatus().getStatus()
							: null,
					(nonReturnableGatepass.getApprovalStatus() != null)
							? nonReturnableGatepass.getApprovalStatus().getId()
							: null);

			response.add(nonReturnableGatepssResponse);
		}
		return response;
	}

	@Override
	@Transactional
	public Object getNonReturnableGatepssById(Integer id) {
		NonReturnableGatepass nonReturnableGatepass = nonReturnableGatepssDao.getNonReturnableGatepassById(id);

		NonReturnableGatepssResponse response = new NonReturnableGatepssResponse(nonReturnableGatepass.getId(),
				nonReturnableGatepass.getAddedDate(), nonReturnableGatepass.getNonRetGpNo(),
				nonReturnableGatepass.getStore().getStoreName(), nonReturnableGatepass.getStore().getStoreId(),
				nonReturnableGatepass.getVendor().getName(), nonReturnableGatepass.getVendor().getId(),
				nonReturnableGatepass.getAddedBy().getUserName(), nonReturnableGatepass.getStatus().getStatus(),
				nonReturnableGatepass.getStatus().getId(), nonReturnableGatepass.getRequestType().getId(),
				(nonReturnableGatepass.getApprovalStatus() != null)
						? nonReturnableGatepass.getApprovalStatus().getStatus()
						: null,
				(nonReturnableGatepass.getApprovalStatus() != null) ? nonReturnableGatepass.getApprovalStatus().getId()
						: null);

		List<NonReturnableGatepssResponseItem> items = new ArrayList<NonReturnableGatepssResponseItem>();
		for (NonReturnableGatepassDetail details : nonReturnableGatepass.getNonReturnableGatepassDetail()) {
			NonReturnableGatepssResponseItem item = new NonReturnableGatepssResponseItem(details.getId(),
					details.getBatch().getItem().getId(), details.getBatch().getItem().getName(),
					details.getBatch().getItem().getCode(), details.getBatch().getBatchNo(), details.getQuantity());
			item.setStatus(details.getStatus().getStatus());
			item.setStatusId(details.getStatus().getId());
			item.setAmount(details.getAmount());
			item.setRemark(details.getRemark());

			List<NonReturnableGatepssResponseItem> itemAccessories = new ArrayList<NonReturnableGatepssResponseItem>();
			for (NonReturnableGatepassAccessoriesMapper accessories : details
					.getNonReturnableGatepassAccessoriesMapper()) {
				NonReturnableGatepssResponseItem accessory = new NonReturnableGatepssResponseItem(accessories.getId(),
						accessories.getBatch().getItem().getId(), accessories.getBatch().getItem().getName(),
						accessories.getBatch().getItem().getCode(), accessories.getBatch().getBatchNo(),
						accessories.getQuantity(), accessories.getBatch().getId());

				itemAccessories.add(accessory);
			}
			item.setAccessorriesList(itemAccessories);
			items.add(item);
		}

		response.setItems(items);
		return response;
	}

	@Override
	@Transactional
	public Object saveNonReturnableGatepass(NonReturnableGatepssRequest request) 
	{
		NonReturnableGatepass nonReturnableGatepss = new NonReturnableGatepass("NRGNO-001",
				new Store(request.getStoreId()), new Vendor(request.getVendorId()), new Status(request.getStatusId()),
				new RequestType(request.getRequestTypeId()));
		if (request.getAuctionRequisitionId() != null) {
			nonReturnableGatepss.setAuctionRequisition(new AuctionRequisition(request.getAuctionRequisitionId()));
		}

		for (NonReturnableGatepssRequestItem items : request.getItems())
		{
			NonReturnableGatepassDetail nonReturnableGatepassDetail1=new NonReturnableGatepassDetail(new Batch(items.getBatchId()), items.getQuantity(),
					items.getAmount(), items.getRemark(), new Status(items.getStatusId()));
//			sessionService.setEntityDefaults(nonReturnableGatepassDetail1);	
			nonReturnableGatepassDetail1.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			NonReturnableGatepassDetail nonReturnableGatepassDetail = nonReturnableGatepss.addNonReturnableGatepassDetail(nonReturnableGatepassDetail1);
			
			if (items.getAuctionRequisitionDetailId() != null) 
			{
				nonReturnableGatepassDetail.setAuctionRequisitionDetail(new AuctionRequisitionDetail(items.getAuctionRequisitionDetailId()));
			}

			for (NonReturnableGatepssRequestAccessories accessories : items.getAccessories()) {
				NonReturnableGatepassAccessoriesMapper nonReturnableGatepassAccessoriesMapper=	new NonReturnableGatepassAccessoriesMapper(new Batch(accessories.getBatchId()),
						accessories.getQuantity());
//				sessionService.setEntityDefaults(nonReturnableGatepassAccessoriesMapper);
				nonReturnableGatepassAccessoriesMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				nonReturnableGatepassDetail.addNonReturnableGatepassAccessoriesMapper(nonReturnableGatepassAccessoriesMapper);
			}
			
		}
//		sessionService.setEntityDefaults(nonReturnableGatepss);	
		nonReturnableGatepss.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		nonReturnableGatepssDao.saveNonReturnableGatepass(nonReturnableGatepss);
		CommonResponse response = new CommonResponse();
		response.setId(nonReturnableGatepss.getId().longValue());
		response.setMessage("Non Returnable Gatepss saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getRequestNonReturnableGatepss(NonReturnableGatepssRequest request,Integer unitId, Integer orgId) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<NonReturnableGatepssResponse> response = new ArrayList<NonReturnableGatepssResponse>();
		for (NonReturnableGatepssResponse auctionVendorApprovalResponse : nonReturnableGatepssDao
				.getAuctionVendorApprovalList(request)) {
			for (NonReturnableGatepssResponse vendorDetails : nonReturnableGatepssDao
					.getAuctionVendorApprovalVendorList(request, auctionVendorApprovalResponse.getId())) {
				NonReturnableGatepssResponse item = new NonReturnableGatepssResponse(
						auctionVendorApprovalResponse.getId(), vendorDetails.getVendorId(), vendorDetails.getVendor(),
						auctionVendorApprovalResponse.getRequestNo(), auctionVendorApprovalResponse.getRequestDate(),
						auctionVendorApprovalResponse.getAgainstRequisition(),
						auctionVendorApprovalResponse.getApproveById(), auctionVendorApprovalResponse.getApproveBy(),
						auctionVendorApprovalResponse.getAuctionRequisitionId());
				List<NonReturnableGatepssResponseItem> itemList = new ArrayList<NonReturnableGatepssResponseItem>();
				for (NonReturnableGatepssResponseItem items : nonReturnableGatepssDao
						.getAuctionVendorApprovalItemList(auctionVendorApprovalResponse.getId())) {
					items.setAccessories(itemsDao.batchAccessoriesList(items.getBatchId().intValue(),unitId));
					itemList.add(items);
				}
				item.setItems(itemList);
				response.add(item);
			}
		}

		return response;
	}

	@Override
	@Transactional
	public Object cancelNonReturnableGatepass(NonReturnableGatepssRequest request) {
		NonReturnableGatepass nonReturnableGatepass = nonReturnableGatepssDao
				.getNonReturnableGatepassById(request.getId());

		for (NonReturnableGatepassDetail detail : nonReturnableGatepass.getNonReturnableGatepassDetail()) {
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(nonReturnableGatepass.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
			detail.setStatus(new Status(request.getStatusId()));
		}

		nonReturnableGatepass.setCancelReason(new CancelReason(request.getCancelReasonId()));
		nonReturnableGatepass.setCancelNote(request.getCancelNote());
		nonReturnableGatepass.setStatus(new Status(request.getStatusId()));
		nonReturnableGatepass.setUpdatedDate(TimeZoneComponent.getDateByzone(nonReturnableGatepass.getUnitId()));
		nonReturnableGatepass.setUpdatedByUser(request.getUserId());

		nonReturnableGatepssDao.updateNonReturnableGatepass(nonReturnableGatepass);

		CommonResponse response = new CommonResponse();
		response.setMessage("Non Returnable Gatepass cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveNonReturnableGatepass(NonReturnableGatepssRequest request) {

		for (Integer id : request.getIds()) {
			NonReturnableGatepass nonReturnableGatepass = nonReturnableGatepssDao.getNonReturnableGatepassById(id);

			for (NonReturnableGatepassDetail detail : nonReturnableGatepass.getNonReturnableGatepassDetail()) {
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(nonReturnableGatepass.getUnitId()));
				detail.setUpdatedByUser(request.getUserId());
				detail.setStatus(new Status(request.getStatusId()));
			}
			nonReturnableGatepass.setStatus(new Status(request.getStatusId()));
			nonReturnableGatepass.setApprovalStatus(new Status(request.getApprovalStatusId()));
			nonReturnableGatepass.setApprovedBy(new User(request.getUserId()));
			nonReturnableGatepass.setApprovedOn(TimeZoneComponent.getDateByzone(nonReturnableGatepass.getUnitId()));
			nonReturnableGatepass.setUpdatedDate(TimeZoneComponent.getDateByzone(nonReturnableGatepass.getUnitId()));
			nonReturnableGatepass.setUpdatedByUser(request.getUserId());

			nonReturnableGatepssDao.updateNonReturnableGatepass(nonReturnableGatepass);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Non Returnable Gatepass Approved successfully");

		return response;

	}

	@Override
	@Transactional
	public Object updateNonReturnableGatepass(NonReturnableGatepssRequest request) {
		NonReturnableGatepass nonReturnableGatepass = nonReturnableGatepssDao
				.getNonReturnableGatepassById(request.getId());

		for (NonReturnableGatepassDetail detail : nonReturnableGatepass.getNonReturnableGatepassDetail()) {
			Integer id = null;
			for (NonReturnableGatepssRequestItem items : request.getItems()) {
				if (items.getId() != null && items.getId().equals(detail.getId())) {
					id = items.getId();

					detail.updateNonReturnableGatepassDetail(items.getQuantity(), items.getAmount(), items.getRemark());
					for (NonReturnableGatepassAccessoriesMapper accessoryMapper : detail
							.getNonReturnableGatepassAccessoriesMapper()) {
						Integer accessoryId = null;
						for (NonReturnableGatepssRequestAccessories accessories : items.getAccessories()) {
							if (accessories.getId() != null && accessories.getId().equals(accessoryMapper.getId())) {
								accessoryId = accessories.getId();

								accessoryMapper.updateNonReturnableGatepassAccessoriesMapper(
										new Batch(accessories.getBatchId()), accessories.getQuantity());
							}
						}
						if (accessoryId == null) {
							accessoryMapper.setIsDeleted(true);
						}

						accessoryMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
						accessoryMapper.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
					}
					break;
				}

			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());

		}
		for (NonReturnableGatepssRequestItem items : request.getItems())
		{
			if (items.getId() == null) {
				NonReturnableGatepassDetail nonReturnableGatepassDetail = new NonReturnableGatepassDetail(
						new Batch(items.getBatchId()), items.getQuantity(), items.getAmount(), items.getRemark(),
						new Status(items.getStatusId()));
				
				if (items.getAuctionRequisitionDetailId() != null)
				{
					nonReturnableGatepassDetail.setAuctionRequisitionDetail(new AuctionRequisitionDetail(items.getAuctionRequisitionDetailId()));
				}

				for (NonReturnableGatepssRequestAccessories accessories : items.getAccessories()) 
				{
					NonReturnableGatepassAccessoriesMapper nonReturnableGatepassAccessoriesMapper=new NonReturnableGatepassAccessoriesMapper(new Batch(accessories.getBatchId()),
							accessories.getQuantity());
//					sessionService.setEntityDefaults(nonReturnableGatepassAccessoriesMapper);	
					nonReturnableGatepassAccessoriesMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					nonReturnableGatepassDetail.addNonReturnableGatepassAccessoriesMapper(nonReturnableGatepassAccessoriesMapper);
				}
//				sessionService.setEntityDefaults(nonReturnableGatepassDetail);	
				nonReturnableGatepassDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				nonReturnableGatepass.addNonReturnableGatepassDetail(nonReturnableGatepassDetail);
			}
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Non Returnable Gatepass Updated successfully");

		return response;
	}
}
