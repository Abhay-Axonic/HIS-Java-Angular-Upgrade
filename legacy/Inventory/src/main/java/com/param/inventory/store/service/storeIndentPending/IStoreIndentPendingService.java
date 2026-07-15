package com.param.inventory.store.service.storeIndentPending;

import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndentPending.StoreIndentPendingRequest;

public interface IStoreIndentPendingService {

	StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getStoreIndent(Integer id, Integer unitId, Integer orgId);

	Object getStoreIndentList(StoreIndentRequest request);

	Object closeStoreIndent(StoreIndentPendingRequest request);

}
