package com.param.linenLaundry.linenIssueAcceptance.service;

import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIssueAcceptanceService {

	LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId);

	Object getLinenIssueAcceptance(LinenRequest request);
	
	Object saveLinenIssueAcceptance(LinenRequest request);
	
	Object getLinenIssueAcceptance(Integer id);
	
}
