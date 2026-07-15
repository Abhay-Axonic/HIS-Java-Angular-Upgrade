package com.param.linenLaundry.linenReturnAcceptance.service;

import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenReturnAcceptanceService {

	LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId);

	Object getLinenReturnAcceptanceList(LinenRequest request);
	
	Object saveLinenReturnAcceptance(LinenRequest request);
	
	Object getLinenReturnAcceptance(Integer id);
	
}
