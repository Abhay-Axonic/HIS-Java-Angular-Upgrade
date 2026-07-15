package com.param.procurement.purchase.service.itemRequest;

import com.param.procurement.purchase.dto.itemRequest.ItemRequestDropdown;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;

public interface IItemRequestService {

	ItemRequestDropdown getDropdowns();

	public Object getItemRequestList(ItemRequestDto request);

	Object saveItemRequest(ItemRequestDto itemRequestDto);

	Object updateItemRequest(ItemRequestDto itemRequestDto);

	Object getItemRequest(Integer id);

	Object approveItemRequest(ItemRequestDto itemRequestDto);

	Object cancelItemRequest(ItemRequestDto itemRequestDto);

	Object approveItemDetailRequest(ItemRequestDto itemRequestDto);

}
