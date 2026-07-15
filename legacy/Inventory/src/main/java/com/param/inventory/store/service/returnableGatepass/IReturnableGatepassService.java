package com.param.inventory.store.service.returnableGatepass;

import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassDropdownResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassRequest;
import java.util.List;


public interface IReturnableGatepassService {

	ReturnableGatepassDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getReturnableGatepassList(ReturnableGatepassRequest request);

	Object saveReturnableGatepass(ReturnableGatepassRequest request);

	Object getReturnableGatepass(Integer id, Integer orgId, Integer unitId);

	Object updateReturnableGatepass(ReturnableGatepassRequest request);

	Object submitReturnableGatepass(ReturnableGatepassRequest request);

	Object cancelReturnableGatepass(ReturnableGatepassRequest request);

    Object checkReturnableGatePass(List<ReturnableGatepassRequest> request);
}
