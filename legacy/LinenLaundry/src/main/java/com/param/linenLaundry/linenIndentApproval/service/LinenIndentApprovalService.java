package com.param.linenLaundry.linenIndentApproval.service;

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
import com.param.entity.model.linenAndLaundry.LinenIssue;
import com.param.entity.model.linenAndLaundry.LinenIssueDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.linenLaundry.linenIndent.dto.ItemRequest;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;
import com.param.linenLaundry.linenIndentApproval.dao.ILinenIndentApprovalDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("LinenIndentApprovalService")
@Transactional
public class LinenIndentApprovalService implements ILinenIndentApprovalService {
	
	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.LinenIndentApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	ILinenIndentApprovalDao linenIndentApprovalDao;

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

		for (LinenIndent linenIndent : linenIndentApprovalDao.searchLinenIndent(request, screenId)) {
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

		LinenIndent linenIndent = linenIndentApprovalDao.getLinenIndent(request.getId());

		LinenIssue linenIssue = new LinenIssue("ISS-001", new Store(linenIndent.getToStore().getStoreId()),
				new Store(linenIndent.getFromStore().getStoreId()), new LinenIndent(linenIndent.getId()),
				new Priority(linenIndent.getPriority().getPriorityId()), new Status(linenIndent.getStatus().getId()));
//		sessionService.setEntityDefaults(linenIssue);	
		linenIssue.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for (LinenIndentDetail detail : linenIndentApprovalDao.getLinenIndentDetail(request.getId())) {

			for (ItemRequest item : request.getItems()) {
				
				if (item.getId() != null && item.getId().equals(detail.getId())) {					
				
					detail.updateLinenIndentDetail((item.getApprovedQuantity() != null) ? item.getApprovedQuantity() : null,
							(item.getRejectQuantity() != null) ? item.getRejectQuantity() : null, new Status(item.getStatusId()),
							(item.getRejectReasonId() != null) ? new RejectReason(item.getRejectReasonId()) : null,
							(item.getSpecification() != null) ? item.getSpecification() : null,
							(item.getRejectNote() != null) ? item.getRejectNote() : null);

					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					detail.setUpdatedByUser(request.getUserId());
					LinenIssueDetail linenIssueDetail=new LinenIssueDetail(new LinenIndentDetail(detail.getId()), new Item(detail.getItem().getId()));
//					sessionService.setEntityDefaults(linenIssueDetail);
					linenIssueDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					linenIssue.addLinenIssueDetail(linenIssueDetail);
				}
			}
		}

		linenIndent.setStatus(new Status(request.getStatusId()));
		linenIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));

		linenIndent.setApprovedOn(TimeZoneComponent.getDateByzone(request.getUnitId()));
        //linenIndent.setApprovedBy(linenIndent.getUser());
		linenIndent.setApprovedBy(new User(request.getUserId()));

		linenIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		linenIndent.setUpdatedByUser(request.getUserId());

		linenIndentApprovalDao.updateLinenIndent(linenIndent);
		linenIndentApprovalDao.saveLinenIssue(linenIssue);
		
		CommonResponse response = new CommonResponse();
		response.setMessage("Store indent quantity approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getLinenIndent(Integer id,Integer unitId, Integer orgId) {
		
		LinenIndent linenIndent = linenIndentApprovalDao.getLinenIndent(id);

		LinenResponse response = new LinenResponse(linenIndent.getId(), linenIndent.getAddedDate(), linenIndent.getIndentNo(),
				linenIndent.getFromStore().getStoreId(), linenIndent.getFromStore().getStoreName(), linenIndent.getToStore().getStoreId(),
				linenIndent.getPriority().getPriorityId(), linenIndent.getPriority().getPriorityDesc(), linenIndent.getToStore().getStoreName(),
				linenIndent.getAddedBy().getUserName(), linenIndent.getAddedBy().getUserId(), linenIndent.getRemark(), linenIndent.getStatus().getId(),
				linenIndent.getStatus().getStatus());
		response.setApprovalStatusId(linenIndent.getApprovalStatus().getId());
		response.setApprovalStatus(linenIndent.getApprovalStatus().getStatus());
		
		for (LinenIndentDetail detail : linenIndentApprovalDao.getLinenIndentDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(linenIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long stockInTransit = GlobalDao.getstockInTransitByItem(linenIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);

			response.addItems(new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(), 
					detail.getItem().getName(), detail.getIndentQuantity(), storeStock, stockInTransit,
					 detail.getStatus().getId(), detail.getStatus().getStatus()));
		}

		return response;
	}
	
	@Override
	@Transactional
	public Object cancelLinenIndent(LinenRequest request)
	{
		LinenIndent linenIndent = linenIndentApprovalDao.getLinenIndent(request.getId());

		for (LinenIndentDetail detail : linenIndent.getLinenIndentDetail()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
			//detail.setUpdatedBy(linenIndent.getUser());
			detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		}

		linenIndent.setCancelReason(new CancelReason(request.getCancelReasonId()));
		linenIndent.setCancelNote(request.getCancelNote());

		linenIndent.setStatus(new Status(request.getStatusId()));

		linenIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() :0));
	//	linenIndent.setUpdatedBy(linenIndent.getUser());
		linenIndent.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		linenIndentApprovalDao.updateLinenIndent(linenIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Linen indent approval cancelled successfully");

		return response;
	}
	
}