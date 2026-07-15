package com.param.linenLaundry.linenIndent.service;

import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIndentService {

	LinenLaundryDropdown getDropdowns(Integer unitId, Integer orgId,Integer userId);

	Object getLinenIndentList(LinenRequest request);
	
	Object saveLinenIndent(LinenRequest request);
	
	Object getLinenIndent(Integer id);
	
	Object approveLinenIndent(LinenRequest request);
	
	Object cancelLinenIndent(LinenRequest request);
	
	Object updateLinenIndent(LinenRequest request);

}
