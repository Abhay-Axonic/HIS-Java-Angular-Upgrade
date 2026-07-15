package com.param.procurement.purchase.service.itemRequestApproval;

import com.param.procurement.purchase.dto.itemRequest.ItemRequestDropdown;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;

public interface IItemRequestApprovalService {
	
	ItemRequestDropdown getDropdowns();

	public Object getItemRequestList(ItemRequestDto request);
	
	Object getItemRequest(Integer id);
	
	Object approveItemRequest(ItemRequestDto itemRequestDto);
	
	Object cancelItemRequest(ItemRequestDto itemRequestDto);

	Object approveItemDetailRequest(ItemRequestDto itemRequestDto);

}
