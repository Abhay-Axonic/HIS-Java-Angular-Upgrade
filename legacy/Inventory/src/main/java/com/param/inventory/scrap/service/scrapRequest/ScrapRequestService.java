package com.param.inventory.scrap.service.scrapRequest;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ScrapRequestDetail;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.dao.IGetStoreListUsingsessionDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.scrap.dao.scrapRequest.IScrapRequestDao;
import com.param.inventory.scrap.dto.scrapRequest.ScrapDetailRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ScrapRequestService implements IScrapRequestService {

	private Integer screenId = Screen.ScrapRequest.getScreen();

	@Autowired
	IScrapRequestDao ScrapRequestDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	IGetStoreListUsingsessionDao storeListUsingsessionDao;

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
	public Object getScrapRequestList(ScrapRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ScrapRequestDao.scrapRequestList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveScrapRequest(ScrapRequest request) {

		/*Integer storeId = request.getStoreId();
		Store storeNew = GlobalDao.getStore(storeId);
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/
		String grnNo = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);

		com.param.entity.model.inventory.ScrapRequest scrapRequest = new com.param.entity.model.inventory.ScrapRequest(
				new Status(request.getApprovalStatusId()), new AssetType(request.getAssetTypeId()),
				request.getNearByExpiry(), request.getRemark(), grnNo, new Status(request.getStatusId()),
				new Store(request.getStoreId()), request.getScrapType());
		//sessionService.setEntityDefaults(scrapRequest);	
		//scrapRequest.setAddedBy(new User(request.getUserId()));
		//scrapRequest.setUpdatedBy(new User(request.getUserId()));
		scrapRequest.setAddedByUser(request.getUserId());
		scrapRequest.setUpdatedByUser(request.getUserId());
		scrapRequest.setAddedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
		scrapRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
		scrapRequest.setOrganizationId(request.getOrganizationId());
		scrapRequest.setUnitId(request.getUnitId());
		for (ScrapDetailRequest item : request.getItems())
		{
			ScrapRequestDetail scrapRequestDetail=	new ScrapRequestDetail(item.getScrapQuantity(), new ReasonMaster(item.getScrapReasonId()),
					new Batch(item.getBatchId()), new Status(item.getStatusId()),
					(item.getUomTypeId()!=null) ? new UomType(item.getUomTypeId()) : null,
					(item.getUomUnitId()!=null) ? new UomUnit(item.getUomUnitId()) : null, item.getAmountCop());
			//sessionService.setEntityDefaults(scrapRequestDetail);	
			//scrapRequestDetail.setAddedBy(new User(request.getUserId()));
			//scrapRequestDetail.setUpdatedBy(new User(request.getUserId()));
			scrapRequestDetail.setAddedByUser(request.getUserId());
			scrapRequestDetail.setUpdatedByUser(request.getUserId());
			scrapRequestDetail.setAddedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
			scrapRequestDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
			scrapRequestDetail.setOrganizationId(request.getOrganizationId());
			scrapRequestDetail.setUnitId(request.getUnitId());
			scrapRequest.addScrapRequestDetail(scrapRequestDetail);
		}
		ScrapRequestDao.saveScrapRequest(scrapRequest);

		CommonResponse response = new CommonResponse();
		response.setId(scrapRequest.getId().longValue());
		response.setMessage("Scrap Request saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getScrapRequestDetails(Integer id) {

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
					detail.getBatch().getItem().getId(),detail.getBatch().getId(),scrapRequest.getUnit().getUnitId());
			List<UomResponse> uomList = GlobalDao.getBatchUomList(detail.getBatch().getId());
			if (uomList != null && !uomList.isEmpty()) {
				UomResponse sUom = Util.getStoreUom(uomList);
				UomResponse dUom = Util.getOpDispenseUom(uomList);

				Integer conversion = sUom.getConversion() * dUom.getConversion();
				//availableQuantity = availableQuantity / conversion;
				response.addItems(new com.param.inventory.scrap.dto.scrapRequest.ItemResponse(detail.getId(),
						detail.getBatch().getItem().getId(), detail.getUomType().getId(), detail.getUomUnit().getId(),
						detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
						detail.getBatch().getItem().getGeneric().getName(), detail.getUomType().getType(),
						detail.getUomUnit().getUnits(), detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(),
						detail.getScrapQty(), detail.getScrapReason().getReasonMasterId(), detail.getScrapReason().getReasonDesc(),
						detail.getBatch().getLotNo(), detail.getBatch().getCop(), availableQuantity,
						detail.getAmountCop(),uomList, detail.getBatch().getItem().getIsThreeUOMApplicable()));
			}
			else
			{
				response.addItems(new com.param.inventory.scrap.dto.scrapRequest.ItemResponse(detail.getId(),
						detail.getBatch().getItem().getId(), null,null,
						detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
						null, null,null, detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(),
						detail.getScrapQty(), detail.getScrapReason().getReasonMasterId(), detail.getScrapReason().getReasonDesc(),
						detail.getBatch().getLotNo(), detail.getBatch().getCop(), availableQuantity,
						detail.getAmountCop(), detail.getBatch().getItem().getIsThreeUOMApplicable()));	
			}

			/*response.addItems(new com.param.inventory.scrap.dto.scrapRequest.ItemResponse(detail.getId(),
					detail.getBatch().getItem().getId(), detail.getUomType().getId(), detail.getUomUnit().getId(),
					detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
					detail.getBatch().getItem().getGeneric().getName(), detail.getUomType().getType(),
					detail.getUomUnit().getUnits(), detail.getBatch().getBatchNo(), detail.getBatch().getExpiryDate(),
					detail.getScrapQty(), detail.getScrapReason().getId(), detail.getScrapReason().getReason(),
					detail.getBatch().getLotNo(), detail.getBatch().getCop(), availableQuantity,
					detail.getAmountCop()));*/
		}
		return response;
	}

	@Override
	@Transactional
	public Object updateScrapRequest(ScrapRequest request) {

		com.param.entity.model.inventory.ScrapRequest scrapRequest = ScrapRequestDao.getScrapRequest(request.getId());

		scrapRequest.updateScrapRequest(request.getRemark());

		for (ScrapRequestDetail detail : scrapRequest.getScrapRequestDetails()) {
			Integer id = null;

			for (com.param.inventory.scrap.dto.scrapRequestApproval.ItemRequest item : request.getItem()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId(); // added for resolved HIS-10476 Scrap Request : Removed Item Getting Display in Scrap Request Details
					if (item.getAmountCop() != null) {
						//id = item.getId(); // comment for resolved HIS-10476 Scrap Request : Removed Item Getting Display in Scrap Request Details
						detail.updateScrapRequestDetail(item.getScrapQuantity(),
								new ReasonMaster(item.getScrapReasonId()), item.getAmountCop());
						break;
					}
					if (item.getAmountCop() == null) {
						detail.updateScrapRequestDetail(item.getScrapQuantity(),
								new ReasonMaster(item.getScrapReasonId()));
						break;
					}
				}
			}
			if (id == null) { // uncomment for resolved HIS-10476 Scrap Request : Removed Item Getting Display in Scrap Request Details
				detail.setIsDeleted(true);
			}
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
			//detail.setUpdatedByUser(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());

		}
		for (com.param.inventory.scrap.dto.scrapRequestApproval.ItemRequest item : request.getItem()) {
			if (item.getId() == null) {
				ScrapRequestDetail scrapRequestDetail=	new ScrapRequestDetail(item.getScrapQuantity(), new ReasonMaster(item.getScrapReasonId()),
						new Status(item.getStatusId()), new UomType(item.getUomTypeId()),
						new UomUnit(item.getUomUnitId()), new Batch(item.getBatchId()), item.getAmountCop());
				//sessionService.setEntityDefaults(scrapRequestDetail);	
				//scrapRequestDetail.setAddedBy(new User(request.getUserId()));
				//scrapRequestDetail.setUpdatedBy(new User(request.getUserId()));
				scrapRequestDetail.setAddedByUser(request.getUserId());
				scrapRequestDetail.setUpdatedByUser(request.getUserId());
				scrapRequestDetail.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				scrapRequestDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				scrapRequestDetail.setOrganizationId(request.getOrganizationId());
				scrapRequestDetail.setUnitId(request.getUnitId());
				scrapRequest.addScrapRequestDetail(scrapRequestDetail);
			}
		}

		scrapRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		//scrapRequest.setUpdatedBy(scrapRequest.getUser());
		scrapRequest.setUpdatedByUser(request.getUserId());

		ScrapRequestDao.updateScrapRequest(scrapRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Scrap Request updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveScrapRequest(ScrapRequest request) {

		for (Integer id : request.getScrapId()) {
			com.param.entity.model.inventory.ScrapRequest scrapRequest = ScrapRequestDao.getScrapRequest(id);

			scrapRequest.setStatus(new Status(request.getStatusId()));
			scrapRequest.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (ScrapRequestDetail detail : scrapRequest.getScrapRequestDetails()) {
				detail.setStatus(new Status(request.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
				//detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId());
			}

			scrapRequest.setApprovedOn(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
			scrapRequest.setApprovedBy(new User(request.getUserId()));

			scrapRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
			//scrapRequest.setUpdatedBy(scrapRequest.getUser());
			scrapRequest.setUpdatedByUser(request.getUserId());
			
			ScrapRequestDao.updateScrapRequest(scrapRequest);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Scrap Request approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelScrapRequest(ScrapRequest request) {

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

		scrapRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(scrapRequest.getUnitId()));
	//	scrapRequest.setUpdatedBy(scrapRequest.getUser());
		scrapRequest.setUpdatedByUser(request.getUserId());

		ScrapRequestDao.updateScrapRequest(scrapRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Scrap Request cancelled successfully");

		return response;
	}

}
