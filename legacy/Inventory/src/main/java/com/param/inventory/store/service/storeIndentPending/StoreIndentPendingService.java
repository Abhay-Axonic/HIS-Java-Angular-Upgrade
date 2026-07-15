package com.param.inventory.store.service.storeIndentPending;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.MaterialPickerDetail;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.storeIndent.IStoreIndentDao;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentResponse;
import com.param.inventory.store.dto.storeIndentPending.ItemPendingResponse;
import com.param.inventory.store.dto.storeIndentPending.StoreIndentPendingRequest;
import com.param.inventory.store.dto.storeIndentPending.StoreIndentPendingResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("StoreIndentPendingService")
public class StoreIndentPendingService implements IStoreIndentPendingService {

	private Integer screenId = Screen.PendingStoreIndent.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IStoreIndentDao StoreIndentDao;
	
	@Autowired
	IGlobalService GlobalService;
	

@Autowired
private SessionService sessionService;


	@Override
	@Transactional
	public StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		StoreIndentDropdownResponse response = new StoreIndentDropdownResponse();

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
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
	public Object getStoreIndentList(StoreIndentRequest request) {
		request.setFromDate(new Date(1));
		request.setToDate((new DateTime(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId()))).plusDays(1).toDate());

		List<StoreIndentResponse> response = new ArrayList<StoreIndentResponse>();

		for (StoreIndent storeIndent : StoreIndentDao.searchStoreIndentPending(request, screenId)) {
			StoreIndentResponse result = new StoreIndentResponse(storeIndent.getId(), storeIndent.getAddedDate(),
					storeIndent.getIndentNo(),
                    storeIndent.getFromStore() != null ? storeIndent.getFromStore().getStoreId() : null,
                    storeIndent.getFromStore() != null ? storeIndent.getFromStore().getStoreName() : null,
                    storeIndent.getToStore() != null ? storeIndent.getToStore().getStoreId() : null,
                    storeIndent.getToStore() != null ? storeIndent.getToStore().getStoreName() : null,
					storeIndent.getPriority().getPriorityId(),
					storeIndent.getPriority().getPriorityDesc(), storeIndent.getIndentType().getType(),
					storeIndent.getIsConsignment(), storeIndent.getAddedBy().getUserName(), storeIndent.getRemark(),
					storeIndent.getStatus().getStatus(), storeIndent.getStatus().getId(),
					storeIndent.getApprovalStatus().getStatus(), storeIndent.getApprovalStatus().getId());

			response.add(result);
		}

		return response;
	}

	@Override
	@Transactional
	public Object getStoreIndent(Integer id,Integer unitId, Integer orgId) {
		StoreIndent storeIndent = StoreIndentDao.getStoreIndent(id);

		StoreIndentPendingResponse response = new StoreIndentPendingResponse(storeIndent.getId(),
				storeIndent.getAddedDate(), storeIndent.getIndentNo(), storeIndent.getFromStore().getStoreName(),
				storeIndent.getToStore().getStoreName(), storeIndent.getPriority().getPriorityDesc(),
				storeIndent.getIndentType().getType(), storeIndent.getIsConsignment(),
				storeIndent.getAddedBy().getUserName(), storeIndent.getRemark(), storeIndent.getStatus().getStatus(),
				storeIndent.getStatus().getId(), storeIndent.getApprovalStatus().getStatus(),
				storeIndent.getApprovalStatus().getId());

		for (StoreIndentDetail detail : StoreIndentDao.getStoreIndentDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long availableStock = Util.ConvertQuantityByUom(storeStock, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId());
			Long tQuantity = Util.ConvertQuantityByUom(stockInTransit, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			Long lastMonSale = GlobalDao.getLastMonthSale(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long lastMonthSale = Util.ConvertQuantityByUom(lastMonSale, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			Long currentMonSale = GlobalDao.getCurrentMonthSale(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long currentMonthSale = Util.ConvertQuantityByUom(currentMonSale, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),orgId);
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			Long maxStoreQty = Util.ConvertQuantityByUom(maxOrderQuantity, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());

			
			Integer pendingQty =0;
			if(detail.getAuthorizedQuantity()!=null)
			{
				pendingQty=detail.getAuthorizedQuantity();
			}
			ItemPendingResponse details =new ItemPendingResponse(detail.getId(), detail.getItem().getId(),
					detail.getUomType().getId(), detail.getUomUnit().getId(), detail.getIndentQuantity(),
					detail.getStatus().getId(), detail.getItem().getCode(), detail.getItem().getName(),
					detail.getItem().getGeneric().getName(), detail.getItem().getManufacturer().getName(),
					detail.getUomType().getType(), detail.getUomUnit().getUnits(), availableStock, tQuantity,
					lastMonthSale, currentMonthSale, maxStoreQty, detail.getAuthorizedQuantity(),
					detail.getRejectQuantity(), detail.getRejectReason(), detail.getStatus().getStatus(),
					detail.getRejectNote(), pendingQty, detail.getItem().getIsThreeUOMApplicable(), GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(), orgId));
			
			Integer issueQuantity = 0;
			for (MaterialPickerDetail pickerDetail : detail.getMaterialPickerDetails()) {
				
				for (MaterialIssueDetail issueDetail : pickerDetail.getMaterialIssueDetail()) {
					
					issueQuantity = issueQuantity + issueDetail.getIssueQuantity();
				}
			}
			details.setIssueQuantity(issueQuantity);
			response.addItems(details);
		}
		return response;
	}

	@Override
	@Transactional
	public Object closeStoreIndent(StoreIndentPendingRequest request) {
		StoreIndent storeIndent = StoreIndentDao.getStoreIndent(request.getId());

		for (StoreIndentDetail storeIndentDetail : storeIndent.getStoreIndentDetailList()) {
			storeIndentDetail.setStatus(new Status(request.getStatusId()));
			storeIndentDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
			//storeIndentDetail.setUpdatedBy(storeIndent.getUser());
			storeIndentDetail.setUpdatedByUser(request.getUserId());
		}

		storeIndent.setCancelReason(new CancelReason(request.getCloseReasonId()));
		storeIndent.setCancelNote(request.getCloseNote());
		storeIndent.setApprovalStatus(new Status(request.getStatusId()));
		storeIndent.setStatus(new Status(request.getStatusId()));
		storeIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
	//	storeIndent.setUpdatedBy(storeIndent.getUser());
		storeIndent.setUpdatedByUser(request.getUserId());

		StoreIndentDao.updateStoreIndent(storeIndent);
		CommonResponse response = new CommonResponse();
		response.setMessage("Store indent closed successfully");

		return response;
	}

}
