package com.param.linenLaundry.linenIndent.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.linenAndLaundry.LinenIndent;
import com.param.entity.model.linenAndLaundry.LinenIndentDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.linenLaundry.linenIndent.dao.ILinenIndentDao;
import com.param.linenLaundry.linenIndent.dto.ItemRequest;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("LinenIndentService")
@Transactional
public class LinenIndentService implements ILinenIndentService {

	private Integer screenId = Screen.LinenIndent.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	ILinenIndentDao linenIndentDao;

	@Autowired
	SessionService sessionService;

	@Override
	@Transactional
	public LinenLaundryDropdown getDropdowns(Integer unitId, Integer orgId,Integer userId) {
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

		for (Priority priority : GlobalDao.getPriorityList()) {
			response.addPriority(
					new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getLinenIndentList(LinenRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<LinenResponse> response = new ArrayList<LinenResponse>();

		for (LinenIndent linenIndent : linenIndentDao.searchLinenIndent(request, screenId)) {
			LinenResponse result = new LinenResponse(linenIndent.getId(), linenIndent.getAddedDate(), linenIndent.getIndentNo(),
					linenIndent.getFromStore().getStoreId(), linenIndent.getFromStore().getStoreName(), linenIndent.getToStore().getStoreId(),
					linenIndent.getPriority().getPriorityId(), linenIndent.getPriority().getPriorityDesc(), linenIndent.getToStore().getStoreName(),
					linenIndent.getAddedBy().getUserName(), linenIndent.getAddedBy().getUserId(), linenIndent.getRemark(), linenIndent.getStatus().getId(),
					linenIndent.getStatus().getStatus());
			result.setApprovalStatusId(linenIndent.getApprovalStatus().getId());
			result.setApprovalStatus(linenIndent.getApprovalStatus().getStatus());
			response.add(result);
		}

		return response;
	}

	@Override
	@Transactional
	public Object saveLinenIndent(LinenRequest request) {
		
		LinenIndent linenIndent = new LinenIndent("IND-001", new Store(request.getFromStoreId()), new Store(request.getToStoreId()),
				new Priority(request.getPriorityId()), new Status(request.getStatusId()), new Status(request.getApprovalStatusId()), request.getRemark());
		for (ItemRequest item : request.getItems()) {
			LinenIndentDetail linenIndentDetail=new LinenIndentDetail(new Item(item.getItemId()), item.getQuantity(), new Status(item.getStatusId()),
					item.getSpecification(), item.getQuantity());
			//sessionService.setEntityDefaults(linenIndentDetail);
			linenIndentDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			
			linenIndent.addLinenIndentDetail(linenIndentDetail);
		}
	//	sessionService.setEntityDefaults(linenIndent);
		linenIndent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		linenIndentDao.saveLinenIndent(linenIndent);

		CommonResponse response = new CommonResponse();
		response.setId(linenIndent.getId().longValue());
		response.setMessage("Linen indent saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getLinenIndent(Integer id) {
		
		LinenIndent linenIndent = linenIndentDao.getLinenIndent(id);

		LinenResponse response = new LinenResponse(linenIndent.getId(), linenIndent.getAddedDate(), linenIndent.getIndentNo(),
				linenIndent.getFromStore().getStoreId(), linenIndent.getFromStore().getStoreName(), linenIndent.getToStore().getStoreId(),
				linenIndent.getPriority().getPriorityId(), linenIndent.getPriority().getPriorityDesc(), linenIndent.getToStore().getStoreName(),
				linenIndent.getAddedBy().getUserName(), linenIndent.getAddedBy().getUserId(), linenIndent.getRemark(), linenIndent.getStatus().getId(),
				linenIndent.getStatus().getStatus());
		response.setApprovalStatusId(linenIndent.getApprovalStatus().getId());
		response.setApprovalStatus(linenIndent.getApprovalStatus().getStatus());

		for (LinenIndentDetail detail : linenIndentDao.getLinenIndentDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(linenIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),0);
			Long stockInTransit = GlobalDao.getstockInTransitByItem(linenIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),null);

			response.addItems(new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(), 
					detail.getItem().getName(), detail.getIndentQuantity(), storeStock, stockInTransit,
					 detail.getStatus().getId(), detail.getStatus().getStatus()));
		}

		return response;
	}
	
	@Override
	@Transactional
	public Object approveLinenIndent(LinenRequest request)
	{
		for (Integer id : request.getIndentId()) {
			LinenIndent linenIndent = linenIndentDao.getLinenIndent(id);

			linenIndent.setStatus(new Status(request.getStatusId()));
			linenIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (LinenIndentDetail detail : linenIndent.getLinenIndentDetail()) {
				detail.setStatus(new Status(request.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
				//detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId());
				
			}

			linenIndent.setApprovedOn(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
			linenIndent.setApprovedBy(new User(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId()));

			linenIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
			//linenIndent.setUpdatedBy(linenIndent.getUser());
			linenIndent.setUpdatedByUser(request.getUserId());

			linenIndentDao.updateLinenIndent(linenIndent);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Linen indent approved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object cancelLinenIndent(LinenRequest request)
	{
		LinenIndent linenIndent = linenIndentDao.getLinenIndent(request.getId());

		for (LinenIndentDetail detail : linenIndent.getLinenIndentDetail()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
			//detail.setUpdatedBy(linenIndent.getUser());
			detail.setUpdatedByUser(request.getUserId());
			
		}

		linenIndent.setCancelReason(new CancelReason(request.getCancelReasonId()));
		linenIndent.setCancelNote(request.getCancelNote());

		linenIndent.setStatus(new Status(request.getStatusId()));

		linenIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
		//linenIndent.setUpdatedBy(linenIndent.getUser());
		linenIndent.setUpdatedByUser(request.getUserId());

		linenIndentDao.updateLinenIndent(linenIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Linen indent cancelled successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object updateLinenIndent(LinenRequest request) {

		LinenIndent linenIndent = linenIndentDao.getLinenIndent(request.getId());

		linenIndent.updateLinenIndent(new Priority(request.getPriorityId()), request.getRemark());

		for (LinenIndentDetail detail : linenIndent.getLinenIndentDetail()) {
			Integer id = null;

			for (ItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateLinenIndentDetail(new Item(item.getItemId()), item.getQuantity(), item.getSpecification());
					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
			//detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		for (ItemRequest item : request.getItems()) {
			if (item.getId() == null) {
				LinenIndentDetail linenIndentDetail=new LinenIndentDetail(new Item(item.getItemId()), item.getQuantity(), new Status(item.getStatusId()),
						item.getSpecification(), item.getQuantity());
				//sessionService.setEntityDefaults(linenIndentDetail);
				linenIndentDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				linenIndent.addLinenIndentDetail(linenIndentDetail);
			}
		}

		linenIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
		//linenIndent.setUpdatedBy(linenIndent.getUser());
		linenIndent.setUpdatedByUser(request.getUserId());
		
		linenIndentDao.updateLinenIndent(linenIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Linen indent updated successfully");

		return response;
	}

}