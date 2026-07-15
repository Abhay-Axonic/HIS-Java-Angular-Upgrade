package com.param.procurement.purchase.service.itemCreationPending;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.purchase.dao.itemRequest.IItemRequestDao;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDetailResponse;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDropdown;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestResponse;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestSearchDto;
import com.param.utility.TimeZoneComponent;

@Service
@Transactional
public class ItemCreationPendingService implements IItemCreationPendingService{	
	
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	private IItemRequestDao itemRequestDao;
	
	private Integer screenId = Screen.ItemCreationPending.getScreen();
	
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
	public Object getItemCreationList(ItemRequestDto request) {

		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<ItemRequestSearchDto> response = new ArrayList<ItemRequestSearchDto>();

		for (com.param.entity.model.procurement.ItemRequest itemRequest : itemRequestDao.searchItemCreationPending(request, screenId)) {
			ItemRequestSearchDto purchaseReqSearchDto = new ItemRequestSearchDto(itemRequest.getId(),itemRequest.getRequestNumber(),itemRequest.getAddedDate(),
					itemRequest.getAssetType().getType(),itemRequest.getAddedBy().getUserName(),itemRequest.getStatus().getStatus(),itemRequest.getApprovalStatus().getStatus());
					
			response.add(purchaseReqSearchDto);
		}
		return response;
	}
	
	
	@Override
	@Transactional
	public Object getItemCreationPending(Integer id) {

		com.param.entity.model.procurement.ItemRequest itemRequest = itemRequestDao.getItemRequest(id);

		ItemRequestResponse itemRequestResponse = new ItemRequestResponse(itemRequest.getId(),itemRequest.getApprovalStatus().getId(),
				itemRequest.getApprovalStatus().getStatus(),itemRequest.getAssetType().getId(),itemRequest.getAssetType().getType(),
				itemRequest.getRequestNumber(),itemRequest.getAddedDate(),itemRequest.getStatus().getId(),itemRequest.getStatus().getStatus());

		for (ItemRequestDetail detail : itemRequestDao.getItemRequestDetail(id)) {
			//addded if condition for resolved HIS-9372 Incorrect filter for the item creation pending list
			
			if (detail.getStatus().getId().equals(5) || detail.getStatus().getId().equals(8)) { // HIS-11007

				if (detail.getVendor() != null && detail.getManufacturer() != null) {
					itemRequestResponse.addItems(new ItemRequestDetailResponse(detail.getId(), detail.getItemName(),
							detail.getManufacturer().getId(), detail.getManufacturer().getName(),
							detail.getVendor().getId(), detail.getVendor().getName(), detail.getExpectedPrice(),
							detail.getItemCreationReason(), detail.getManufactureTemp(), detail.getVendorTemp(),
							detail.getStatus().getId(), detail.getStatus().getStatus()));
				} else {
					itemRequestResponse.addItems(new ItemRequestDetailResponse(detail.getId(), detail.getItemName(),
							detail.getExpectedPrice(), detail.getItemCreationReason(), detail.getManufactureTemp(),
							detail.getVendorTemp(), detail.getStatus().getId(), detail.getStatus().getStatus()));
				}

			}//end if condition for resolved HIS-9372 Incorrect filter for the item creation pending list
			
		}

		return itemRequestResponse;
	}
	
	@Override
	@Transactional
	public Object cancelItemCreationPending(ItemRequestDto itemRequestDto) {

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
