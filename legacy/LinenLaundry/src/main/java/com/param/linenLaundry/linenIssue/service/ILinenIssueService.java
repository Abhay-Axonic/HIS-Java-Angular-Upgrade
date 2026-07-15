package com.param.linenLaundry.linenIssue.service;

import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIssueService {

	LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId);

	Object getLinenIssueList(LinenRequest request);
	
	Object saveLinenIssue(LinenRequest request);
	
	Object getLinenIssue(Integer id, Integer unitId, Integer orgId);
	
}
