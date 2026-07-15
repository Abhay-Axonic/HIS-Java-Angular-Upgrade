package com.param.assetManagement.indent.assetIndentApproval.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.AccessoriesDetails;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequestAccessories;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequestItem;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentResponse;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentResponseItem;
import com.param.assetManagement.indent.assetIndentApproval.dao.IAssetIndentApprovalDao;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.asset.AssetIndent;
import com.param.entity.model.asset.AssetIndentAccessories;
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueDetail;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("AssetIndentApprovalService")
public class AssetIndentApprovalService implements IAssetIndentApprovalService {
	

@Autowired
private SessionService sessionService;

	private Integer screenId = Screen.AssetIssue.getScreen();

	@Autowired
	IAssetIndentApprovalDao assetIndentApprovalDao;

	@Autowired
	IGlobalDao GlobalDao;
	@Autowired
	IGlobalService GlobalService;

	@Override
	@Transactional
	public AssetIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		AssetIndentDropdown response = new AssetIndentDropdown();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId,unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}

		for (Priority priority : GlobalDao.getPriorityList()) {
			response.addPriority(
					new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getAssetIndentApprovalList(AssetIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<AssetIndentResponse> response = new ArrayList<AssetIndentResponse>();
		for (AssetIndent assetIndent : assetIndentApprovalDao.searchAssetIndent(request, screenId)) {

			AssetIndentResponse item = new AssetIndentResponse(assetIndent.getId(), assetIndent.getAddedDate(),
					assetIndent.getIndentNo(), assetIndent.getFromStore().getStoreId(),
					assetIndent.getFromStore().getStoreName(), assetIndent.getToStore().getStoreId(),
					assetIndent.getToStore().getStoreName(), assetIndent.getPriority().getPriorityId(),
					assetIndent.getPriority().getPriorityDesc(), assetIndent.getIndentType().getType(),
					assetIndent.getAddedBy().getUserName(), assetIndent.getRemark(),
					assetIndent.getStatus().getStatus(), assetIndent.getStatus().getId(),
					assetIndent.getApprovalStatus().getStatus(), assetIndent.getApprovalStatus().getId());
			response.add(item);
		}

		return response;
	}

	@Override
	@Transactional
	public Object getAssetIndentApprovalById(Integer id, Integer unitId, Integer orgId) {
		AssetIndent assetIndent = assetIndentApprovalDao.getAssetIndentById(id);

		AssetIndentResponse response = new AssetIndentResponse(assetIndent.getId(), assetIndent.getAddedDate(),
				assetIndent.getIndentNo(), assetIndent.getFromStore().getStoreId(),
				assetIndent.getFromStore().getStoreName(), assetIndent.getToStore().getStoreId(),
				assetIndent.getToStore().getStoreName(), assetIndent.getPriority().getPriorityId(),
				assetIndent.getPriority().getPriorityDesc(), assetIndent.getIndentType().getType(),
				assetIndent.getAddedBy().getUserName(), assetIndent.getRemark(), assetIndent.getStatus().getStatus(),
				assetIndent.getStatus().getId(), assetIndent.getApprovalStatus().getStatus(),
				assetIndent.getApprovalStatus().getId());

		List<AssetIndentResponseItem> items = new ArrayList<AssetIndentResponseItem>();
		for (AssetIndentDetail detail : assetIndentApprovalDao.getAssetIndentDetail(id)) {
			Long stockAvailable = GlobalDao.getStoreStockByItem(assetIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long stockInTransit = GlobalDao.getstockInTransitByItem(assetIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);

			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(assetIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),orgId);
			Long maxOrderQuantity = maxStoreQuantity - (stockAvailable + stockInTransit);

			AssetIndentResponseItem item = new AssetIndentResponseItem(detail.getId(), detail.getItem().getId(),
					detail.getIndentQuantity(), detail.getApprovedIndentQuantity(), detail.getStatus().getId(),
					detail.getStatus().getStatus(), detail.getItem().getCode(), detail.getItem().getName(),
					stockAvailable, stockInTransit, detail.getSpecification(), maxOrderQuantity);
			item.setRejectedQuantity((detail.getRejectIndentQuantity() != null) ? detail.getRejectIndentQuantity() : 0);
			if (detail.getRejectReason() != null) {
				item.setRejectReasonId(detail.getRejectReason().getId());
			}
			item.setRejectNote((detail.getRejectNote() != null) ? detail.getRejectNote() : "");
			List<AccessoriesDetails> accessories = new ArrayList<AccessoriesDetails>();
			for (AssetIndentAccessories accessoryDetail : assetIndentApprovalDao
					.getAssetIndentAccessories(detail.getId())) {
				AccessoriesDetails accessory = new AccessoriesDetails(accessoryDetail.getId(),
						accessoryDetail.getItem().getId(), accessoryDetail.getItem().getCode(),
						accessoryDetail.getItem().getName(), accessoryDetail.getSpecification(),
						accessoryDetail.getIndentQuantity(), accessoryDetail.getApprovedIndentQuantity());
				accessory.setRejectQuantity(
						(accessoryDetail.getRejectIndentQuantity() != null) ? accessoryDetail.getRejectIndentQuantity()
								: 0);
				if (accessoryDetail.getRejectReason() != null) {
					accessory.setRejectReasonId(accessoryDetail.getRejectReason().getId());
				}
				accessory.setRejectNote(
						(accessoryDetail.getRejectNote() != null) ? accessoryDetail.getRejectNote() : "");

				accessories.add(accessory);
			}
			item.setAccessories(accessories);
			items.add(item);
		}

		response.setItems(items);
		return response;
	}

	@Override
	@Transactional
	public Object saveAssetIndentApproval(AssetIndentRequest request) {

		AssetIndent assetIndent = assetIndentApprovalDao.getAssetIndentById(request.getId());
		Integer storeId = assetIndent.getFromStore().getStoreId();
		String issueNumber = GlobalService.getBillNoByGeneratedCode(storeId, request.getUnitId(), screenId);
		AssetIssue assetIssue = new AssetIssue(assetIndent, new Store(assetIndent.getFromStore().getStoreId()),
				new Store(assetIndent.getToStore().getStoreId()), new IndentType(assetIndent.getIndentType().getId()),
				issueNumber!=null ? issueNumber :"001", new Priority(assetIndent.getPriority().getPriorityId()),
				new Status(com.param.common.constants.Status.Pending.getStatus()));
//		sessionService.setEntityDefaults(assetIssue);
		assetIssue.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		if (assetIndent != null) {
			for (AssetIndentRequestItem item : request.getItems()) {

				AssetIndentDetail assetIndentDetail = assetIndentApprovalDao.getAssetIndentDetailById(item.getId());

				if (assetIndentDetail != null) {

					if (item.getApprovedQuantity() != null) {
						assetIndentDetail.setApprovedIndentQuantity(item.getApprovedQuantity());

						assetIndentDetail.setPendingQuantity(item.getApprovedQuantity());
					}
					if (item.getRejectQuantity() != null) {
						assetIndentDetail.setRejectIndentQuantity(item.getRejectQuantity());
						assetIndentDetail.setRejectReason(new RejectReason(item.getRejectReasonId()));
						assetIndentDetail.setRejectNote(item.getRejectNote());
					}
					assetIndentDetail.setStatus(new Status(item.getStatusId()));

					assetIndentDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIssue.getUnitId()));
//					assetIndentDetail.setUpdatedBy(assetIndent.getUser());
					assetIndentDetail.setUpdatedByUser(request.getUserId());
					for (AssetIndentRequestAccessories accessories : item.getAccessories()) {

						AssetIndentAccessories accessory = assetIndentApprovalDao
								.getAssetIndentAccessoryById(accessories.getId());

						if (accessory != null) {

							if (accessories.getApprovedQuantity() != null) {
								accessory.setApprovedIndentQuantity(accessories.getApprovedQuantity());
							}
							if (accessories.getRejectQuantity() != null) {
								accessory.setRejectIndentQuantity(accessories.getRejectQuantity());
								accessory.setRejectReason(new RejectReason(accessories.getRejectReasonId()));
								accessory.setRejectNote(accessories.getRejectNote());
							}

							accessory.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIssue.getUnitId()));
							accessory.setUpdatedByUser(request.getUserId());
						}
					}
					AssetIssueDetail assetIssueDetail=new AssetIssueDetail((new AssetIndentDetail(assetIndentDetail.getId())),
							new Item(assetIndentDetail.getItem().getId()));
//					sessionService.setEntityDefaults(assetIssueDetail);
					assetIssueDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					assetIssue.addAssetIssueDetail(assetIssueDetail);

				}
			}

			assetIndent.setStatus(new Status(request.getStatusId()));
			assetIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));
			assetIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIssue.getUnitId()));
			assetIndent.setUpdatedByUser(request.getUserId());
			assetIndentApprovalDao.updateAssetIndent(assetIndent);
			assetIndentApprovalDao.saveAssetIssue(assetIssue);
		}
		CommonResponse response = new CommonResponse();
		response.setId(assetIndent.getId().longValue());
		response.setMessage("Asset Indent Approval saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelAssetIndentApproval(AssetIndentRequest request) {
		AssetIndent assetIndent = assetIndentApprovalDao.getAssetIndentById(request.getId());

		for (AssetIndentDetail detail : assetIndent.getAssetIndentDetail()) {
			detail.setStatus(new Status(request.getStatusId()));
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIndent.getUnitId()));
//			detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
			for (AssetIndentAccessories accessory : detail.getAssetIndentAccessories()) {
				accessory.setIsActive(false);
				accessory.setIsDeleted(true);
				accessory.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIndent.getUnitId()));
				accessory.setUpdatedByUser(request.getUserId());
			}

		}
		assetIndent.setCancelReason(new CancelReason(request.getCancelReasonId()));
		assetIndent.setCancelNote(request.getCancelNote());

		assetIndent.setStatus(new Status(request.getStatusId()));
		assetIndent.setApprovalStatus(new Status(request.getStatusId()));
		assetIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIndent.getUnitId()));
		assetIndent.setUpdatedByUser(request.getUserId());

		assetIndentApprovalDao.updateAssetIndent(assetIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Asset Indent cancelled successfully");

		return response;
	}

}
