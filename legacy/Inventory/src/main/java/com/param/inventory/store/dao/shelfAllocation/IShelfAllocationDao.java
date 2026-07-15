package com.param.inventory.store.dao.shelfAllocation;

import java.util.List;

import com.param.entity.model.procurement.ShelfItem;
import com.param.inventory.store.dto.shelfAllocation.ShelfAllocationRequest;
import com.param.inventory.store.dto.shelfAllocation.ShelfAllocationResponse;

public interface IShelfAllocationDao {

	List<ShelfAllocationResponse> getAllocatedItemList(ShelfAllocationRequest request);

	List<ShelfAllocationResponse> getUnAllocatedItemList(ShelfAllocationRequest request);

	List<ShelfAllocationResponse> getAllocatedItems(Integer storeId, Integer rackId, Integer shelfId, Integer unitId);

	ShelfItem getShelfItem(Integer id, Integer unitId);

	ShelfItem updateShelfItem(ShelfItem shelfItem);

}
