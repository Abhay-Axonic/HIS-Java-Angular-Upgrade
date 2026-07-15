package com.param.procurement.purchase.service.itemCreationPending;

import com.param.procurement.purchase.dto.itemRequest.ItemRequestDropdown;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;

public interface IItemCreationPendingService {

	ItemRequestDropdown getDropdowns();

	Object getItemCreationList(ItemRequestDto request);

	Object getItemCreationPending(Integer id);

	Object cancelItemCreationPending(ItemRequestDto itemRequestDto);

}
