package com.param.assetManagement.nonReturnableGatepass.service;

import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssRequest;
import com.param.global.dto.global.CommonDropdown;

public interface INonReturnableGatepssService {

	CommonDropdown getDropdownList();
	
	Object getNonReturnableGatepssList(NonReturnableGatepssRequest request);
	
	Object getNonReturnableGatepssById(Integer id);
	
	Object saveNonReturnableGatepass(NonReturnableGatepssRequest request);
	
	Object getRequestNonReturnableGatepss(NonReturnableGatepssRequest request, Integer unitId, Integer orgId);

	Object cancelNonReturnableGatepass(NonReturnableGatepssRequest request);
	
	Object approveNonReturnableGatepass(NonReturnableGatepssRequest request);
	
	Object updateNonReturnableGatepass(NonReturnableGatepssRequest request);
}
