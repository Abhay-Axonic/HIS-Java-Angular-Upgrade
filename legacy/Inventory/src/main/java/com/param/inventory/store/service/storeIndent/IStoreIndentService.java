package com.param.inventory.store.service.storeIndent;

import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;

public interface IStoreIndentService {

	StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getStoreIndentList(StoreIndentRequest request);
	
	Object saveStoreIndent(StoreIndentRequest request);
	
	Object updateStoreIndent(StoreIndentRequest request);
	
	Object getStoreIndent(Integer id, Integer orgId, Integer unitId);
	
	Object approveStoreIndent(StoreIndentRequest request);
	
	Object cancelStoreIndent(StoreIndentRequest request);
}
