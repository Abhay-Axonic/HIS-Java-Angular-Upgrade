package com.param.inventory.store.service.returnableGatepassApproval;

import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassDropdownResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassRequest;

public interface IReturnableGatepassApprovalService {

	ReturnableGatepassDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getReturnableGatepassList(ReturnableGatepassRequest request);

	Object getReturnableGatepass(Integer id, Integer orgId, Integer unitId);

	Object cancelReturnableGatepass(ReturnableGatepassRequest request);

	Object approveReturnableGatepass(ReturnableGatepassRequest request);

	Object approveReturnableGatepassNew(ReturnableGatepassRequest request);

}
