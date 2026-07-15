package com.param.inventory.scrap.service.scrapRequestApproval;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.AssetDisposalWorklistRequest;
import com.param.entity.model.inventory.AssetDisposalWorklistRequestDetail;
import com.param.entity.model.inventory.ScrapRequestDetail;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.inventory.scrap.dao.scrapRequest.IScrapRequestDao;
import com.param.inventory.scrap.dao.scrapRequestApproval.IScrapRequestApprovalDao;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;
import com.param.inventory.scrap.dto.scrapRequestApproval.ItemRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ScrapRequestApprovalService implements IScrapRequestApprovalService {

	private Integer screenId = Screen.ScrapRequestApproval.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IScrapRequestApprovalDao ScrapRequestApprovalDao;

	@Autowired
	IScrapRequestDao ScrapRequestDao;
	
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
	public ScrapsDropdownResponse getDropownsByUserId(Integer id) {
		ScrapsDropdownResponse response = new ScrapsDropdownResponse();

		/*for (Store store : GlobalDao.getUserStoreList(id)) {
			response.addStores(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList()) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}*/
		return response;

	}

	@Override
	@Transactional
	public Object getScrapRequestApprovalList(ScrapRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ScrapRequestApprovalDao.scrapRequestApprovalList(request, screenId);
	}

	@Override
	@Transactional
	public Object getScrapRequestApprovalDetails(Integer id,Integer unitId) {

		com.param.entity.model.inventory.ScrapRequest scrapRequest = ScrapRequestDao.getScrapRequest(id);

		ScrapResponse response = new ScrapResponse(scrapRequest.getId(), scrapRequest.getRequestNo(),
				scrapRequest.getAddedDate(), scrapRequest.getStore().getStoreId(),
				scrapRequest.getStore().getStoreName(), scrapRequest.getAssetType().getId(),
				scrapRequest.getAssetType().getType(), scrapRequest.getAddedBy().getUserName(),
				scrapRequest.getRemark(), scrapRequest.getStatus().getStatus(), scrapRequest.getStatus().getId(),
				scrapRequest.getApprovalStatus().getStatus(), scrapRequest.getApprovalStatus().getId(),
				scrapRequest.getNearByExpiry());

		for (ScrapRequestDetail detail : ScrapRequestDao.getScrapRequestdetail(id)) {

			Long availableQuantity = GlobalDao.getStoreStockByItem(scrapRequest.getStore().getStoreId(),
					detail.getBatch().getItem().getId(),unitId);

			response.addItems(new com.param.inventory.scrap.dto.scrapRequest.ItemResponse(detail.getId(),
					detail.getBatch().getItem().getId(), detail.getUomType()!=null ? detail.getUomType().getId():null,  detail.getUomUnit()!=null ?detail.getUomUnit().getId():null,
					detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
					detail.getBatch().getItem().getGeneric()!=null ?detail.getBatch().getItem().getGeneric().getName():null , detail.getUomType() !=null ? detail.getUomType().getType():null,
							detail.getUomUnit()!=null ? detail.getUomUnit().getUnits():null, detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate()!=null ? detail.getBatch().getExpiryDate():null,
					detail.getScrapQty(), detail.getScrapReason().getReasonMasterId(), detail.getScrapReason().getReasonDesc(),
					detail.getBatch().getLotNo(), detail.getBatch().getCop(), availableQuantity, detail.getAmountCop(),
					detail.getAcceptedQty(), detail.getRejectedQty(),
					detail.getRejectReason() != null ? detail.getRejectReason().getId() : 0,
					detail.getRejectReason() != null ? detail.getRejectReason().getReason() : null,
					detail.getRejectedNote() != null ? detail.getRejectedNote() : null, detail.getBatch().getItem().getIsThreeUOMApplicable(), 
					GlobalDao.getUnitOfMeasurementListByItem(detail.getBatch().getItem().getId(), scrapRequest.getOrganizationId())));
		}
		return response;
	}

	@Override
	@Transactional
	public Object approveScrapRequestApproval(ScrapRequest request) {

		screen = GlobalDao.getScreen(screenId);

		com.param.entity.model.inventory.ScrapRequest scrapRequest = ScrapRequestDao.getScrapRequest(request.getId());

		AssetDisposalWorklistRequest assetDisposalWorklistRequest = new AssetDisposalWorklistRequest(scrapRequest,
				new Status(com.param.common.constants.Status.Pending.getStatus()),new Status(com.param.common.constants.Status.Pending.getStatus()));

		Integer storeId = scrapRequest.getStore().getStoreId();

		for (ScrapRequestDetail detail : ScrapRequestDao.getScrapRequestdetail(request.getId()))
		{

			for (ItemRequest item : request.getItem()) 
			{

				if (item.getId() != null && item.getId().equals(detail.getId()))
				{
					AssetDisposalWorklistRequestDetail ad=new AssetDisposalWorklistRequestDetail(detail);
					ad.setAddedByUser(request.getUserId());
					ad.setUpdatedByUser(request.getUserId());
					ad.setAddedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
					ad.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
					ad.setOrganizationId(request.getOrganizationId());
					ad.setUnitId(request.getUnitId());
					assetDisposalWorklistRequest.addAssetDisposalWorklistRequestDetail(ad);

					detail.approveScrapRequestDetail(item.getAcceptedQuantity(), item.getRejectNote(),
							item.getRejectedQty(), new RejectReason(item.getRejectReasonId()),
							new Status(item.getStatusId()), TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()), new User(request.getUserId()));

				}
			}
		}
		scrapRequest.setApprovalStatus(new Status(request.getApprovalStatusId()));
		scrapRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
		//scrapRequest.setUpdatedBy(scrapRequest.getUser());
		scrapRequest.setUpdatedByUser(request.getUserId());
		//sessionService.setEntityDefaults(assetDisposalWorklistRequest);
		//assetDisposalWorklistRequest.setAddedBy(new User(request.getUserId()));
		//assetDisposalWorklistRequest.setUpdatedBy(new User(request.getUserId()));
		assetDisposalWorklistRequest.setAddedByUser(request.getUserId());
		assetDisposalWorklistRequest.setUpdatedByUser(request.getUserId());
		assetDisposalWorklistRequest.setAddedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
		assetDisposalWorklistRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
		assetDisposalWorklistRequest.setOrganizationId(request.getOrganizationId());
		assetDisposalWorklistRequest.setUnitId(request.getUnitId());

		
		ScrapRequestDao.saveScrapRequest(assetDisposalWorklistRequest);

		CommonResponse response = new CommonResponse();
		response.setId(scrapRequest.getId().longValue());
		response.setMessage("Scrap Request Approval Updated successfully");
		return response;
	}

	@Override
	@Transactional
	public Object cancelScrapRequestApproval(ScrapRequest request) {

		com.param.entity.model.inventory.ScrapRequest scrapRequest = ScrapRequestDao.getScrapRequest(request.getId());

		for (ScrapRequestDetail detail : scrapRequest.getScrapRequestDetails()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
			//detail.setUpdatedBy(scrapRequest.getUser());
			detail.setUpdatedByUser(request.getUserId());

		}

		scrapRequest.setCancelReason(new CancelReason(request.getCancelReasonId()));
		scrapRequest.setCancelNote(request.getCancelNote());

		scrapRequest.setStatus(new Status(request.getStatusId()));
		scrapRequest.setApprovalStatus(new Status(com.param.common.constants.Status.Rejected.getStatus()));

		//scrapRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		//scrapRequest.setUpdatedBy(scrapRequest.getUser());
		scrapRequest.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(), scrapRequest.getUnitId());
		ScrapRequestDao.updateScrapRequest(scrapRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Scrap Request Approval cancelled successfully");

		return response;
	}

}
