package com.param.linenLaundry.linenReturn.service;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.linenAndLaundry.LinenReturn;
import com.param.entity.model.linenAndLaundry.LinenReturnDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ReturnReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.BatchProperty;
import com.param.global.utility.Util;
import com.param.linenLaundry.linenIndent.dto.ItemRequest;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;
import com.param.linenLaundry.linenReturn.dao.ILinenReturnDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("LinenReturnService")
@Transactional
public class LinenReturnService implements ILinenReturnService {

	private Integer screenId = Screen.LinenReturn.getScreen();
	
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	ILinenReturnDao linenReturnDao;

	@Autowired
	private SessionService sessionService;


	@Override
	@Transactional
	public LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId) {
		LinenLaundryDropdown response = new LinenLaundryDropdown();

		for (Store store : GlobalDao.getLinenUserStoreList(unitId,userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (Store store : GlobalDao.getLinenStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getLinenReturnList(LinenRequest request) {

		return linenReturnDao.searchLinenReturn(request, screenId);
	}

	@Override
	@Transactional
	public Object saveLinenReturn(LinenRequest request) {
	
		LinenReturn linenReturn = new LinenReturn(new Status(request.getApprovalStatusId()), new User(request.getCarrierId()),
				new Store(request.getFromStoreId()), "RET-001", request.getRemark(), new Status(request.getStatusId()),
				new Store(request.getToStoreId()));
	//	sessionService.setEntityDefaults(linenReturn);	
		linenReturn.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for (ItemRequest items : request.getItems())
		{
			LinenReturnDetail linenReturnDetail=new LinenReturnDetail(new Batch(items.getBatchId()), items.getReturnQuantity(),
					new ReturnReason(items.getReturnReasonId()), new Status(items.getStatusId()));
			//sessionService.setEntityDefaults(linenReturnDetail);	
			linenReturnDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			linenReturn.addLinenReturnDetail(linenReturnDetail);
		}
		linenReturnDao.saveLinenReturn(linenReturn);

		CommonResponse response = new CommonResponse();
		response.setId(linenReturn.getId().longValue());
		response.setMessage("Linen Return saved successfully");
		
		return response;
		
	}

	@Override
	@Transactional
	public Object getLinenReturn(Integer id) {
		
		LinenResponse linenResponse = linenReturnDao.getLinenReturn(id);
		
		for(LinenReturnDetail detail : linenReturnDao.getLinenReturnDetail(linenResponse.getId()))
		{
			Long storeStock = GlobalDao.getStoreStockByItem(linenResponse.getFromStoreId(),
					detail.getBatch().getItem().getId(),0);
			
			BatchProperty batchProperty = new BatchProperty();
            List<Batch> batchList = GlobalDao.getBatchDetails(detail.getBatch().getItem().getId(), 0,linenResponse.getFromStoreId(),
					batchProperty);
				
				ItemResponse item = new ItemResponse(detail.getId(), detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(), 
					detail.getBatch().getItem().getName(), storeStock, detail.getStatus().getId(), detail.getStatus().getStatus(),
					detail.getBatch().getId(), detail.getBatch().getBatchNo(), detail.getReturnQuantity(), (detail.getReturnReason() != null) ? detail.getReturnReason().getId() : null,
					(detail.getReturnReason() != null) ? detail.getReturnReason().getReason() : null);

				for (Batch batch : batchList) {
					item.addBatches(Util.PopulateBatchWithStock(batch));
				}
				
				linenResponse.addItems(item);
		}
		
		return linenResponse;
	}
	
	@Override
	@Transactional
	public Object approveLinenReturn(LinenRequest request)
	{
		Timestamp now = new Timestamp(System.currentTimeMillis());
		for (Integer id : request.getReturnId()) {

			LinenReturn linenReturn = linenReturnDao.getLinenReturnId(id);

			linenReturn.setStatus(new Status(request.getStatusId()));
			linenReturn.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (LinenReturnDetail detail : linenReturn.getLinenReturnDetails()) {
				detail.setStatus(new Status(request.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
				//detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			}

			linenReturn.setApprovedOn(now);
			linenReturn.setApprovedBy(new User(sessionService.getUserId()));

			linenReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
			//linenReturn.setUpdatedBy(linenReturn.getUser());
			linenReturn.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			linenReturnDao.updateLinenReturn(linenReturn);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Linen Return approved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object cancelLinenReturn(LinenRequest request)
	{
		LinenReturn linenReturn = linenReturnDao.getLinenReturnId(request.getId());

		for (LinenReturnDetail detail : linenReturn.getLinenReturnDetails()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
			//detail.setUpdatedBy(linenReturn.getUser());
			detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		}

		linenReturn.setCancelReason(new CancelReason(request.getCancelReasonId()));
		linenReturn.setCancelNote(request.getCancelNote());

		linenReturn.setStatus(new Status(request.getStatusId()));

		linenReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
	//	linenReturn.setUpdatedBy(linenReturn.getUser());
		linenReturn.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		linenReturnDao.updateLinenReturn(linenReturn);

		CommonResponse response = new CommonResponse();
		response.setMessage("Linen Return cancelled successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object updateLinenReturn(LinenRequest request)
	{
		LinenReturn linenReturn = linenReturnDao.getLinenReturnId(request.getId());

		linenReturn.updateLinenReturn(new User(request.getCarrierId()), request.getRemark());

		for (LinenReturnDetail detail : linenReturn.getLinenReturnDetails()) {
			Integer id = null;

			for (ItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateLinenReturnDetail(
							item.getReturnQuantity(),
							new com.param.entity.model.master.ReturnReason(item.getReturnReasonId()));
					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
			//detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId() );
		}

		for (ItemRequest items : request.getItems()) {
			
			if (items.getId() == null) {
				LinenReturnDetail linenReturnDetail	=new LinenReturnDetail(new Batch(items.getBatchId()), items.getReturnQuantity(),
						new ReturnReason(items.getReturnReasonId()), new Status(items.getStatusId()));
			//	sessionService.setEntityDefaults(linenReturnDetail);	
				linenReturnDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				linenReturn.addLinenReturnDetail(linenReturnDetail);
			}
		}

		linenReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
		//linenReturn.setUpdatedBy(linenReturn.getUser());
		linenReturn.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId() );
		
		linenReturnDao.updateLinenReturn(linenReturn);

		CommonResponse response = new CommonResponse();
		response.setMessage("Linen Return updated successfully");

		return response;
	}

}