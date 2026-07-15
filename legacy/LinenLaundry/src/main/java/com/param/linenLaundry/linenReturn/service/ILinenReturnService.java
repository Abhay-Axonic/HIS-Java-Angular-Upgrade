package com.param.linenLaundry.linenReturn.service;

import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenReturnService {

	LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId);

	Object getLinenReturnList(LinenRequest request);
	
	Object saveLinenReturn(LinenRequest request);
	
	Object getLinenReturn(Integer id);
	
	Object approveLinenReturn(LinenRequest request);
	
	Object cancelLinenReturn(LinenRequest request);
	
	Object updateLinenReturn(LinenRequest request);
	
}
