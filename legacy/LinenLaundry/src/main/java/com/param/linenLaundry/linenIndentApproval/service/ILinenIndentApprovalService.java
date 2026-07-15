package com.param.linenLaundry.linenIndentApproval.service;

import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIndentApprovalService {

	LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId);

	Object getLinenIndentList(LinenRequest request);
	
	Object saveLinenIndent(LinenRequest request);
	
	Object getLinenIndent(Integer id, Integer unitId, Integer orgId);
	
	Object cancelLinenIndent(LinenRequest request);
	
}
