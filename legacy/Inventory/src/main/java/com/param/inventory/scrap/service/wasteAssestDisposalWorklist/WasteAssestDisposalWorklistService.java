package com.param.inventory.scrap.service.wasteAssestDisposalWorklist;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.AssetDisposalWorklistRequest;
import com.param.entity.model.inventory.AssetDisposalWorklistRequestDetail;
import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.entity.model.inventory.WasteAssetDisposalWorklistDetail;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.inventory.scrap.dao.requestAssestDisposalWorklist.IRequestAssestDisposalWorklistDao;
import com.param.inventory.scrap.dao.wasteAssestDisposalWorklist.IWasteAssestDisposalWorklistDao;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.AssestDisposalRequest;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.ItemRequest;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.WitnessResponse;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WasteAssestDisposalWorklistService implements IWasteAssestDisposalWorklistService {

	private Integer screenId = Screen.WasteAssetDisposalWorklist.getScreen();

	@Autowired
	IWasteAssestDisposalWorklistDao WasteAssestDisposalWorklistDao;

	@Autowired
	IRequestAssestDisposalWorklistDao RequestAssestDisposalWorklistDao;

	@Autowired
	IGlobalDao GlobalDao;

@Autowired
private SessionService sessionService;

	@Override
	@Transactional
	public ScrapsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {

		ScrapsDropdownResponse response = new ScrapsDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStores(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(0,0)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getWasteAssestDisposalWorklistList(ScrapRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return WasteAssestDisposalWorklistDao.getWasteAssestDisposalWorklistList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveWasteAssestDisposalWorklist(AssestDisposalRequest request) {

		WasteAssetDisposalWorklist wasteAssetDisposalWorklist = new WasteAssetDisposalWorklist(
				new Status(request.getStatusId()),
				(new AssetDisposalWorklistRequest(request.getAssetDisposalWorklistRequestId())));

		for (ItemRequest item : request.getItems()) {

			wasteAssetDisposalWorklist.addWasteAssetDisposalWorklistDetail(
					new WasteAssetDisposalWorklistDetail(item.getDisposeQuantity(), new Status(item.getStatusId()),
							new AssetDisposalWorklistRequestDetail(item.getAssetDisposalWorklistRequestDetailId())));

		}
		wasteAssetDisposalWorklist.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		WasteAssestDisposalWorklistDao.saveWasteAssestDisposalWorklist(wasteAssetDisposalWorklist);

		CommonResponse response = new CommonResponse();
		response.setId(wasteAssetDisposalWorklist.getId().longValue());
		response.setMessage("Waste Assest Disposal Worklist saved successfully");

		return response;
	}

	/* Changing post to patch */
	@Override
	@Transactional
	public Object updateWasteAssestDisposalWorklist(AssestDisposalRequest request) {

		AssetDisposalWorklistRequest assetDisposalWorklistRequest = RequestAssestDisposalWorklistDao
				.getRequestAssetDisposalWorklist(request.getId());

		WasteAssetDisposalWorklist wasteAssetDisposalWorklist = new WasteAssetDisposalWorklist(
				new Status(com.param.common.constants.Status.Pending.getStatus()), assetDisposalWorklistRequest);
		//sessionService.setEntityDefaults(wasteAssetDisposalWorklist);
	//	wasteAssetDisposalWorklist.setAddedBy(new User(request.getUserId()));
	//	wasteAssetDisposalWorklist.setUpdatedBy(new User(request.getUserId()));
		wasteAssetDisposalWorklist.setAddedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
		wasteAssetDisposalWorklist.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
		wasteAssetDisposalWorklist.setAddedDate(TimeZoneComponent.getDateByzone(assetDisposalWorklistRequest.getUnitId()));
		wasteAssetDisposalWorklist.setUpdatedDate(TimeZoneComponent.getDateByzone(assetDisposalWorklistRequest.getUnitId()));
		wasteAssetDisposalWorklist.setOrganizationId(request.getOrganizationId());
		wasteAssetDisposalWorklist.setUnitId(request.getUnitId());

		List<WasteAssetDisposalWorklist> wasteAssetDisposalWorklists = assetDisposalWorklistRequest.getWasteAssetDisposalWorklists();
		if(wasteAssetDisposalWorklists.isEmpty())
			wasteAssetDisposalWorklists = new ArrayList<>();


		for (AssetDisposalWorklistRequestDetail detail : RequestAssestDisposalWorklistDao
				.getRequestAssetDisposalWorklistDetail(request.getId()))
		{
			for (ItemRequest item : request.getItems()) {

				if (item.getId() != null && item.getId().equals(detail.getId())) {

					WasteAssetDisposalWorklistDetail wasteAssetDisposalWorklistDetail = new WasteAssetDisposalWorklistDetail(new Status(com.param.common.constants.Status.Pending.getStatus()),null,
							request.getApprovedDisposedQty(),
							new AssetDisposalWorklistRequestDetail(detail.getId()));
					if(item.getDisposeQuantity() != null)
					{
						wasteAssetDisposalWorklistDetail.setDisposedQty(item.getDisposeQuantity());
					}
					if(item.getApprovedDisposedQuantity() != null)
					{
						wasteAssetDisposalWorklistDetail.setApprovedDisposedQty(item.getApprovedDisposedQuantity());
					}
					/*if(item.getStatusId() != null)
					{
						detail.setStatus(new Status(item.getStatusId()));
					}*/
					//sessionService.setEntityDefaults(wasteAssetDisposalWorklistDetail);
				//	wasteAssetDisposalWorklistDetail.setAddedBy(new User(request.getUserId()));
				//	wasteAssetDisposalWorklistDetail.setUpdatedBy(new User(request.getUserId()));
					wasteAssetDisposalWorklistDetail.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
					wasteAssetDisposalWorklistDetail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
					wasteAssetDisposalWorklistDetail.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					wasteAssetDisposalWorklistDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					wasteAssetDisposalWorklistDetail.setOrganizationId(request.getOrganizationId());
					wasteAssetDisposalWorklistDetail.setUnitId(request.getUnitId());
					wasteAssetDisposalWorklist.addWasteAssetDisposalWorklistDetail(wasteAssetDisposalWorklistDetail);
				}
			}
		}
		wasteAssetDisposalWorklists.add(wasteAssetDisposalWorklist);
		assetDisposalWorklistRequest.setWasteAssetDisposalWorklists(wasteAssetDisposalWorklists);
		assetDisposalWorklistRequest.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
		assetDisposalWorklistRequest.setWitnessApprovalStatus(new Status(com.param.common.constants.Status.PendingApproval.getStatus()));
		assetDisposalWorklistRequest.setWitness(new User(request.getWitnessId()));
		assetDisposalWorklistRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
	//	assetDisposalWorklistRequest.setUpdatedBy(new User(request.getUserId()));
		assetDisposalWorklistRequest.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());

		RequestAssestDisposalWorklistDao.updateRequestAssestDisposalWorklist(assetDisposalWorklistRequest);

		//RequestAssestDisposalWorklistDao.saveWasteAssestDisposalWorklist(wasteAssetDisposalWorklist);
//-------------------------------------------------------------

		/*for (WasteAssetDisposalWorklistDetail detail : wasteAssetDisposalWorklist
				.getWasteAssetDisposalWorklistDetails()) {

			for (ItemRequest item : request.getItems()) {

				if (item.getId() != null && item.getId().equals(detail.getId())) {
					if(item.getDisposeQuantity() != null)
					{
						detail.setDisposedQty(item.getDisposeQuantity());	
					}
					if(item.getApprovedDisposedQuantity() != null)
					{
						detail.setApprovedDisposedQty(item.getApprovedDisposedQuantity());	
					}
					if(item.getStatusId() != null)
					{
						detail.setStatus(new Status(item.getStatusId()));
					}
					

				}
			}
		}
		wasteAssetDisposalWorklist.setStatus(new Status(com.param.common.constants.Status.PendingApproval.getStatus()));
		wasteAssetDisposalWorklist.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		wasteAssetDisposalWorklist.setUpdatedBy(wasteAssetDisposalWorklist.getUser());
		WasteAssestDisposalWorklistDao.updateWasteAssestDisposalWorklist(wasteAssetDisposalWorklist);*/

		CommonResponse response = new CommonResponse();
		response.setId(assetDisposalWorklistRequest.getId().longValue());
		//response.setId(wasteAssetDisposalWorklist.getId().longValue());
		response.setMessage("Waste Assest Disposal Worklist updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getWasteAssestDisposalWorklistDetails(Integer id,Integer unitId, Integer orgId) {

		ScrapResponse response = WasteAssestDisposalWorklistDao.getWasteAssestDisposalWorklist(id);
		for (ItemResponse details : WasteAssestDisposalWorklistDao.getWasteAssestDisposalWorklistDetail(id)) {
			if(details!=null) {
				response.addItems(details);
			}
		}
		response.setWitnessDetails(PopulateWitness(orgId,unitId));

		return response;
	}

	private List<WitnessResponse> PopulateWitness(Integer orgId,Integer unitId) {

		List<WitnessResponse> witnessResponseList = new ArrayList<WitnessResponse>();

		for (User user : GlobalDao.getUser(orgId,unitId)) {
			WitnessResponse witnessResponse = new WitnessResponse(user.getUserId(), user.getUserName());
			witnessResponseList.add(witnessResponse);
		}
		return witnessResponseList;
	}

}
