package com.param.inventory.scrap.service.requestAssestDisposalWorklist;

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
import com.param.entity.model.inventory.ScrapRequestDetail;
import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.entity.model.inventory.WasteAssetDisposalWorklistDetail;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.inventory.scrap.dao.requestAssestDisposalWorklist.IRequestAssestDisposalWorklistDao;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.AssestDisposalRequest;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.WitnessResponse;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class RequestAssestDisposalWorklistService implements IRequestAssestDisposalWorklistService {

	private Integer screenId = Screen.RequestAssetDisposalWorklist.getScreen();

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
	public Object getRequestAssestDisposalWorklistList(ScrapRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return RequestAssestDisposalWorklistDao.getRequestAssestDisposalWorklistList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveRequestAssestDisposalWorklist(AssestDisposalRequest request) {

		AssetDisposalWorklistRequest assetDisposalWorklistRequest = new AssetDisposalWorklistRequest(
				new com.param.entity.model.inventory.ScrapRequest(request.getScrapRequestId()),
				new Status(request.getStatusId()), new User(request.getWitnessId()));

		com.param.entity.model.inventory.ScrapRequest scrapRequest = RequestAssestDisposalWorklistDao
				.getScrapRequestById(request.getScrapRequestId());

		for (ScrapRequestDetail detail : scrapRequest.getScrapRequestDetails()) {
			AssetDisposalWorklistRequestDetail assetDisposalWorklistRequestDetail=new AssetDisposalWorklistRequestDetail(new ScrapRequestDetail(detail.getId()));
			assetDisposalWorklistRequestDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			assetDisposalWorklistRequest.addAssetDisposalWorklistRequestDetail(assetDisposalWorklistRequestDetail);

		}
		assetDisposalWorklistRequest.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		RequestAssestDisposalWorklistDao.saveRequestAssestDisposalWorklist(assetDisposalWorklistRequest);

		CommonResponse response = new CommonResponse();
		response.setId(assetDisposalWorklistRequest.getId().longValue());
		response.setMessage("Request Asset Disposal Worklist saved successfully");

		return response;
	}

	/*@Override
	@Transactional
	public Object getRequestAssestDisposalWorklistDetails(Integer id) {

		ScrapResponse response = RequestAssestDisposalWorklistDao.getScrapRequest(id);
		for (ItemResponse details : RequestAssestDisposalWorklistDao.getScrapRequestDetail(id)) {
			response.addItems(details);
		}
		response.setWitnessDetails(PopulateWitness());
		return response;
	}*/
	
	@Override
	@Transactional
	public Object getRequestAssestDisposalWorklistDetails(Integer id,Integer orgId, Integer unitId) {

		ScrapResponse response = RequestAssestDisposalWorklistDao.getRequestAssestDisposalWorklist(id);
		for (ItemResponse details : RequestAssestDisposalWorklistDao.getRequestAssestDisposalWorklistDetails(id)) {
			response.addItems(details);
		}
		response.setWitnessDetails(PopulateWitness(orgId,unitId));
		if(response.getWitnessId()!=null)
		{
			for(WitnessResponse w : response.getWitnessDetails())
			{
				if(response.getWitnessId().equals(w.getId()))
				{
					response.setWitnessName(w.getName());
				}
			}	
		}
		
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

	@Override
	@Transactional
	public Object updateRequestAssestDisposalWorklist(AssestDisposalRequest request) {

		AssetDisposalWorklistRequest assetDisposalWorklistRequest = RequestAssestDisposalWorklistDao
				.getRequestAssetDisposalWorklist(request.getId());

		WasteAssetDisposalWorklist wasteAssetDisposalWorklist = new WasteAssetDisposalWorklist(
				new Status(com.param.common.constants.Status.Pending.getStatus()), assetDisposalWorklistRequest);
		wasteAssetDisposalWorklist.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());

		for (AssetDisposalWorklistRequestDetail detail : RequestAssestDisposalWorklistDao
				.getRequestAssetDisposalWorklistDetail(request.getId()))
		{
			WasteAssetDisposalWorklistDetail wasteAssetDisposalWorklistDetail=new WasteAssetDisposalWorklistDetail(new Status(com.param.common.constants.Status.Pending.getStatus()),null,
					request.getApprovedDisposedQty(),
					new AssetDisposalWorklistRequestDetail(detail.getId()));
		//	sessionService.setEntityDefaults(wasteAssetDisposalWorklistDetail);	
			wasteAssetDisposalWorklistDetail.setAddedByUser(request.getUserId());
			wasteAssetDisposalWorklistDetail.setUpdatedByUser(request.getUserId());
			//wasteAssetDisposalWorklistDetail.setUpdatedBy(new User(request.getUserId()));
			wasteAssetDisposalWorklistDetail.setAddedDate(TimeZoneComponent.getDateByzone(assetDisposalWorklistRequest.getUnitId()));
			wasteAssetDisposalWorklistDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetDisposalWorklistRequest.getUnitId()));
			wasteAssetDisposalWorklistDetail.setOrganizationId(request.getOrganizationId());
			wasteAssetDisposalWorklistDetail.setUnitId(request.getUnitId());
			wasteAssetDisposalWorklist.addWasteAssetDisposalWorklistDetail(wasteAssetDisposalWorklistDetail);
		}

		assetDisposalWorklistRequest.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
		assetDisposalWorklistRequest.setWitnessApprovalStatus(new Status(com.param.common.constants.Status.PendingApproval.getStatus()));
		assetDisposalWorklistRequest.setWitness(new User(request.getWitnessId()));
		assetDisposalWorklistRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(assetDisposalWorklistRequest.getUnitId()));
		//assetDisposalWorklistRequest.setUpdatedBy(assetDisposalWorklistRequest.getUser());
		assetDisposalWorklistRequest.setUpdatedByUser(request.getUserId());
		RequestAssestDisposalWorklistDao.saveWasteAssestDisposalWorklist(wasteAssetDisposalWorklist);

		CommonResponse response = new CommonResponse();
		response.setId(assetDisposalWorklistRequest.getId().longValue());
		response.setMessage("Request Asset Disposal Worklist updated successfully");

		return response;

	}

}
