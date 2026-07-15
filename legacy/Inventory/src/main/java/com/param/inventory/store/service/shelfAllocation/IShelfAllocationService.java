package com.param.inventory.store.service.shelfAllocation;

import com.param.inventory.store.dto.shelfAllocation.ShelfAllocationRequest;

public interface IShelfAllocationService {

	Object getItemList(ShelfAllocationRequest request);

	Object allocateShelf(ShelfAllocationRequest request);

	Object getAllocatedItems(Integer storeId, Integer rackId, Integer shelfId, Integer unitId);

	Object updateStatus(ShelfAllocationRequest request);

	Object updateAllocatedItem(ShelfAllocationRequest request);

}
