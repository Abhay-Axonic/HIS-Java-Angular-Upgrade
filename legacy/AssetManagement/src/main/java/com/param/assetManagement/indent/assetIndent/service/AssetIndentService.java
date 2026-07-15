package com.param.assetManagement.indent.assetIndent.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.AccessoriesDetails;
import com.param.assetManagement.indent.assetIndent.dao.IAssetIndentDao;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequestAccessories;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequestItem;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentResponse;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentResponseItem;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.asset.AssetIndent;
import com.param.entity.model.asset.AssetIndentAccessories;
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("AssetIndentService")
public class AssetIndentService implements IAssetIndentService {

	private Integer screenId = Screen.AssetIndent.getScreen();

	@Autowired
	IAssetIndentDao assetIndentDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;
	

@Autowired
private SessionService sessionService;


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
	public Object getAssetIndentList(AssetIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<AssetIndentResponse> response = new ArrayList<AssetIndentResponse>();
		for (AssetIndent assetIndent : assetIndentDao.searchAssetIndent(request, screenId)) {

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
	public Object getAssetIndentById(Integer id,Integer unitId, Integer orgId) {
		AssetIndent assetIndent = assetIndentDao.getAssetIndentById(id);

		AssetIndentResponse response = new AssetIndentResponse(assetIndent.getId(), assetIndent.getAddedDate(),
				assetIndent.getIndentNo(), assetIndent.getFromStore().getStoreId(),
				assetIndent.getFromStore().getStoreName(), assetIndent.getToStore().getStoreId(),
				assetIndent.getToStore().getStoreName(), assetIndent.getPriority().getPriorityId(),
				assetIndent.getPriority().getPriorityDesc(), assetIndent.getIndentType().getType(),
				assetIndent.getAddedBy().getUserName(), assetIndent.getRemark(), assetIndent.getStatus().getStatus(),
				assetIndent.getStatus().getId(), assetIndent.getApprovalStatus().getStatus(),
				assetIndent.getApprovalStatus().getId());

		List<AssetIndentResponseItem> items = new ArrayList<AssetIndentResponseItem>();
		for (AssetIndentDetail detail : assetIndentDao.getAssetIndentDetail(id)) {
			Long stockAvailable = GlobalDao.getStoreStockByItem(assetIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long stockInTransit = GlobalDao.getstockInTransitByItem(assetIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(assetIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),orgId);
			Long maxOrderQuantity = maxStoreQuantity - (stockAvailable + stockInTransit);
			if(maxOrderQuantity < 0){ maxOrderQuantity = 0L; }

			AssetIndentResponseItem item = new AssetIndentResponseItem(detail.getId(), detail.getItem().getId(),
					detail.getIndentQuantity(), detail.getApprovedIndentQuantity(), detail.getStatus().getId(),
					detail.getStatus().getStatus(), detail.getItem().getCode(), detail.getItem().getName(),
					stockAvailable, stockInTransit, detail.getSpecification(), maxOrderQuantity);
			
			List<AccessoriesDetails> accessories = new ArrayList<AccessoriesDetails>();
			for (AssetIndentAccessories accessoryDetail : assetIndentDao.getAssetIndentAccessories(detail.getId())) {
				AccessoriesDetails accessory = new AccessoriesDetails(accessoryDetail.getId(),
						accessoryDetail.getItem().getId(), accessoryDetail.getItem().getCode(),
						accessoryDetail.getItem().getName(), accessoryDetail.getSpecification(),
						accessoryDetail.getIndentQuantity(), accessoryDetail.getApprovedIndentQuantity());

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
	public Object saveAssetIndent(AssetIndentRequest request) {

		/*Integer storeId = request.getFromStoreId();
		Store storeNew = GlobalDao.getStore(storeId);
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/
		String grnNo = GlobalService.getBillNoByGeneratedCode(request.getFromStoreId(), request.getUnitId(), screenId);
		AssetIndent assetIndent = new AssetIndent(grnNo, new Store(request.getFromStoreId()),
				new Store(request.getToStoreId()), new IndentType(request.getIndentTypeId()),
				new Priority(request.getPriorityId()), new Status(request.getStatusId()),
				new Status(request.getApprovalStatusId()), request.getRemark());
//		sessionService.setEntityDefaults(assetIndent);	
		assetIndent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for (AssetIndentRequestItem item : request.getItems()) {

			AssetIndentDetail assetIndentDetail = assetIndent
					.addAssetIndentDetail(new AssetIndentDetail(new Item(item.getItemId()), item.getQuantity(),
							new Status(item.getStatusId()), item.getSpecification()));
//			sessionService.setEntityDefaults(assetIndentDetail);	
			assetIndentDetail.setPendingQuantity(item.getQuantity());
			assetIndentDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			for (AssetIndentRequestAccessories accessories : item.getAccessories())
			{
				AssetIndentAccessories assetIndentAccessories=new AssetIndentAccessories(
						new Item(accessories.getItemId()), accessories.getQuantity(), accessories
						.getSpecification());
//				sessionService.setEntityDefaults(assetIndentAccessories);	
				assetIndentAccessories.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				assetIndentDetail.addAssetIndentAccessories(assetIndentAccessories);
			}
		}

		assetIndentDao.saveAssetIndent(assetIndent);

		CommonResponse response = new CommonResponse();
		response.setId(assetIndent.getId().longValue());
		response.setMessage("Asset Indent saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updateAssetIndent(AssetIndentRequest request) {
		AssetIndent assetIndent = assetIndentDao.getAssetIndentById(request.getId());

		assetIndent.setPriority(new Priority(request.getPriorityId()));
		assetIndent.setRemark(request.getRemark());

		for (AssetIndentDetail assetIndentDetails : assetIndent.getAssetIndentDetail()) {
			Integer id = null;
			for (AssetIndentRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(assetIndentDetails.getId())) {
					id = item.getId();

					assetIndentDetails.updateAssetIndentDetail(new Item(item.getItemId()), item.getQuantity(),
							new Status(item.getStatusId()), item.getSpecification());

					for (AssetIndentAccessories detailAccessories : assetIndentDetails.getAssetIndentAccessories()) {
						Integer accessoryId = null;
						for (AssetIndentRequestAccessories accessory : item.getAccessories()) {
							if (accessory.getId() != null && accessory.getId().equals(detailAccessories.getId())) {
								accessoryId = accessory.getId();
								detailAccessories.updateAssetIndentAccessories(new Item(accessory.getItemId()),
										accessory.getQuantity(), accessory.getSpecification());
							}
						}

						if (accessoryId == null) {
							detailAccessories.setIsDeleted(true);
						}

						detailAccessories.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
//						detailAccessories.setUpdatedBy(assetIndentDetails.getUser());
						detailAccessories.setUpdatedByUser(request.getUserId());
					}

					break;
				}
			}

			if (id == null) {
				assetIndentDetails.setIsDeleted(true);
			}

			assetIndentDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
//			assetIndentDetails.setUpdatedBy(assetIndentDetails.getUser());
			assetIndentDetails.setUpdatedByUser(request.getUserId());
		}

		for (AssetIndentRequestItem item : request.getItems()) {

			if (item.getId() == null) {
				AssetIndentDetail assetIndentDetail = new AssetIndentDetail(new Item(item.getItemId()),
						item.getQuantity(), new Status(item.getStatusId()), item.getSpecification());
//				sessionService.setEntityDefaults(assetIndentDetail);	
				assetIndentDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				for (AssetIndentRequestAccessories accessories : item.getAccessories()) 
				{
					AssetIndentAccessories assetIndentAccessories=new AssetIndentAccessories(new Item(accessories.getItemId()),
							accessories.getQuantity(), accessories.getSpecification());
//					sessionService.setEntityDefaults(assetIndentAccessories);	
					assetIndentAccessories.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					assetIndentDetail.addAssetIndentAccessories(assetIndentAccessories);
				}

				assetIndent.addAssetIndentDetail(assetIndentDetail);
			}
		}

		assetIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
//		assetIndent.setUpdatedBy(assetIndent.getUser());
		assetIndent.setUpdatedByUser(request.getUserId());
		assetIndentDao.updateAssetIndent(assetIndent);

		CommonResponse response = new CommonResponse();
		response.setId(assetIndent.getId().longValue());
		response.setMessage("Asset Indent updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelAssetIndent(AssetIndentRequest request) {
		AssetIndent assetIndent = assetIndentDao.getAssetIndentById(request.getId());

		for (AssetIndentDetail detail : assetIndent.getAssetIndentDetail()) {
			detail.setStatus(new Status(request.getStatusId()));
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIndent.getUnitId()));
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
		assetIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIndent.getUnitId()));
//		assetIndent.setUpdatedBy(assetIndent.getUser());
		assetIndent.setUpdatedByUser(request.getUserId());
		assetIndentDao.updateAssetIndent(assetIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Asset Indent cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveAssetIndent(AssetIndentRequest request) {
		for (Integer id : request.getIndentId()) {
			AssetIndent assetIndent = assetIndentDao.getAssetIndentById(id);

			for (AssetIndentDetail detail : assetIndent.getAssetIndentDetail()) {
				detail.setStatus(new Status(request.getStatusId()));
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIndent.getUnitId()));
//				detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId());
			}
			assetIndent.setStatus(new Status(request.getStatusId()));
			assetIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));
			assetIndent.setApprovedBy(new User(request.getUserId()));
			assetIndent.setAppprovedOn(TimeZoneComponent.getDateByzone(assetIndent.getUnitId()));
			assetIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIndent.getUnitId()));
//			assetIndent.setUpdatedBy(assetIndent.getUser());
			assetIndent.setUpdatedByUser(request.getUserId());
			assetIndentDao.updateAssetIndent(assetIndent);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Asset Indent Approved successfully");

		return response;
	}

}
