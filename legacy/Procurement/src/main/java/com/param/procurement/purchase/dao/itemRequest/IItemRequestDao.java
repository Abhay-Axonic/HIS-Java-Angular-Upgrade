package com.param.procurement.purchase.dao.itemRequest;

import java.util.List;

import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ItemRequest;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;

public interface IItemRequestDao {

	List<ItemRequest> searchItemRequest(ItemRequestDto request, Integer screenId);

	ItemRequest getItemRequest(Integer id);

	List<ItemRequestDetail> getItemRequestDetail(Integer itemRequestId);

	ItemRequest saveItemRequest(ItemRequest itemRequest);

	ItemRequest updateItemRequest(ItemRequest itemRequest);

	List<ItemRequest> searchItemCreationPending(ItemRequestDto request, Integer screenId);

}
