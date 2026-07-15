package com.param.procurement.purchase.service.itemRequestApproval;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.purchase.dao.itemRequest.IItemRequestDao;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDetailDto;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDetailResponse;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDropdown;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestResponse;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestSearchDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
@Transactional
public class ItemRequestApprovalService implements IItemRequestApprovalService{
	
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	private IItemRequestDao itemRequestDao;

	@Autowired
	SessionService sessionService;
	
	private Integer screenId = Screen.ItemRequestApproval.getScreen();

	
	@Override
	@Transactional
	public ItemRequestDropdown getDropdowns() {

		ItemRequestDropdown purchaseReqDropDown = new ItemRequestDropdown();

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			purchaseReqDropDown.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		return purchaseReqDropDown;
	}

	@Override
	@Transactional
	public Object getItemRequestList(ItemRequestDto request) {

		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<ItemRequestSearchDto> response = new ArrayList<ItemRequestSearchDto>();

		for (com.param.entity.model.procurement.ItemRequest itemRequest : itemRequestDao.searchItemRequest(request, screenId)) {
			ItemRequestSearchDto purchaseReqSearchDto = new ItemRequestSearchDto(itemRequest.getId(),itemRequest.getRequestNumber(),itemRequest.getAddedDate(),
					itemRequest.getAssetType().getType(),itemRequest.getAddedBy().getUserName(),itemRequest.getStatus().getStatus(),itemRequest.getApprovalStatus().getStatus());
					
			response.add(purchaseReqSearchDto);
		}
		return response;
	}

	@Override
	@Transactional
	public Object getItemRequest(Integer id) {

		com.param.entity.model.procurement.ItemRequest itemRequest = itemRequestDao.getItemRequest(id);

		ItemRequestResponse itemRequestResponse = new ItemRequestResponse(itemRequest.getId(),itemRequest.getApprovalStatus().getId(),
				itemRequest.getApprovalStatus().getStatus(),itemRequest.getAssetType().getId(),itemRequest.getAssetType().getType(),
				itemRequest.getRequestNumber(),itemRequest.getAddedDate(), itemRequest.getStatus().getId(),itemRequest.getStatus().getStatus());

		for (ItemRequestDetail detail : itemRequestDao.getItemRequestDetail(id)) {
			
		  if(detail.getVendor()!=null && detail.getManufacturer()!=null) {
				itemRequestResponse.addItems(new ItemRequestDetailResponse(detail.getId(), detail.getItemName(), detail.getManufacturer().getId(),detail.getManufacturer().getName(),detail.getVendor().getId(),
								detail.getVendor().getName(),detail.getExpectedPrice(),detail.getItemCreationReason(),
								detail.getManufactureTemp(), detail.getVendorTemp(), detail.getStatus().getId(),detail.getStatus().getStatus()));
				}
				else {
					itemRequestResponse.addItems(new ItemRequestDetailResponse(detail.getId(), detail.getItemName(),detail.getExpectedPrice(),detail.getItemCreationReason(),
							detail.getManufactureTemp(), detail.getVendorTemp(), detail.getStatus().getId(),detail.getStatus().getStatus()));
				}
				
			}
		return itemRequestResponse;
	}
	
	
	@Override
	@Transactional
	public Object approveItemDetailRequest(ItemRequestDto itemRequestDto) {

		com.param.entity.model.procurement.ItemRequest itemRequest = itemRequestDao.getItemRequest(itemRequestDto.getId());
		
		for (ItemRequestDetail detail : itemRequest.getItemRequestDetailList()) {
			
			for(ItemRequestDetailDto item :itemRequestDto.getItems()) {
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
					detail.approveItemRequestDetail(item.getRejectNote(), new RejectReason(item.getRejectReasonId()), new Status(item.getStatusId()),
							TimeZoneComponent.getDateByzone(itemRequest.getUnitId()), (new User(itemRequestDto.getUserId())));
				}
			}
		}
		
		itemRequest.setStatus(new Status(itemRequestDto.getStatusId()));
		itemRequest.setApprovalStatus(new Status(itemRequestDto.getApprovalStatusId()));
		
		itemRequest.setApprovedDate(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
		itemRequest.setApprovedBy(new User(itemRequestDto.getUserId()));

		itemRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
		itemRequest.setUpdatedByUser(itemRequestDto.getUserId());

		itemRequestDao.updateItemRequest(itemRequest);
	
		CommonResponse response = new CommonResponse();
		response.setMessage("Item Request Details approved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object approveItemRequest(ItemRequestDto itemRequestDto) {

		for (Integer id : itemRequestDto.getItemRequestId()) {
			com.param.entity.model.procurement.ItemRequest itemRequest = itemRequestDao.getItemRequest(id);

			itemRequest.setStatus(new Status(itemRequestDto.getStatusId()));
			itemRequest.setApprovalStatus(new Status(itemRequestDto.getApprovalStatusId()));
			
			for (ItemRequestDetail detail : itemRequest.getItemRequestDetailList()) {
				detail.setStatus(new Status(itemRequestDto.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
				detail.setUpdatedByUser(itemRequestDto.getUserId());
			}

			itemRequest.setApprovedDate(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
			itemRequest.setApprovedBy(new User(itemRequestDto.getUserId()));

			itemRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
			itemRequest.setUpdatedByUser(itemRequestDto.getUserId());

			itemRequestDao.updateItemRequest(itemRequest);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Item Request approved successfully");

		return response;
	}
	
	
	@Override
	@Transactional
	public Object cancelItemRequest(ItemRequestDto itemRequestDto) {

		com.param.entity.model.procurement.ItemRequest itemRequest = itemRequestDao.getItemRequest(itemRequestDto.getId());

		for (ItemRequestDetail detail : itemRequest.getItemRequestDetailList()) {
			detail.setStatus(new Status(itemRequestDto.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
			detail.setUpdatedByUser(itemRequestDto.getUserId());
		}

		itemRequest.setCancelReason(new CancelReason(itemRequestDto.getCancelReasonId()));
		itemRequest.setCancelNote(itemRequestDto.getCancelNote());

		itemRequest.setStatus(new Status(itemRequestDto.getStatusId()));
		itemRequest.setApprovalStatus(new Status(itemRequestDto.getApprovalStatusId()));

		itemRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
		itemRequest.setUpdatedByUser(itemRequestDto.getUserId());

		itemRequestDao.updateItemRequest(itemRequest);

		CommonResponse response = new CommonResponse();
		response.setMessage("Item Request cancelled successfully");

		return response;
	}

	
	
}
