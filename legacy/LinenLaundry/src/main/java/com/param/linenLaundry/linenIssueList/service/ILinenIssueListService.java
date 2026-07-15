package com.param.linenLaundry.linenIssueList.service;

import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIssueListService {

	LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId);

	Object getLinenIssueList(LinenRequest request);
	
	Object saveLinenIssueList(LinenRequest request);
	
	Object getLinenIssueList(Integer id);
	
}
