package com.param.inventory.store.service.storeIndentApproval;

import org.springframework.util.MultiValueMap;

import com.param.global.common.Response;
import com.param.global.service.items.CustomException;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndentApproval.StoreIndentApprovalRequest;

public interface IStoreIndentApprovalService {

	StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getStoreIndentList(StoreIndentRequest request);

	Object getStoreIndent(Integer id, Integer unitId, Integer orgId);

	Object approveStoreIndent(StoreIndentApprovalRequest request) throws CustomException;

	Object cancelStoreIndent(StoreIndentApprovalRequest request);

	Response StoreIndentStatus(Integer id);

	Response updateStoreIndentForForceClose(StoreIndentApprovalRequest request);

}
