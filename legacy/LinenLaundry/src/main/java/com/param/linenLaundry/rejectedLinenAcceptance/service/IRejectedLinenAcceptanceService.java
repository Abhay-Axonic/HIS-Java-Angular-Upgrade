package com.param.linenLaundry.rejectedLinenAcceptance.service;

import com.param.linenLaundry.rejectedLinenAcceptance.dto.LinensDropdownResponse;
import com.param.linenLaundry.rejectedLinenAcceptance.dto.RejectedLinenRequest;

public interface IRejectedLinenAcceptanceService {

	LinensDropdownResponse getDropdowns();
    
	Object getRejectLinenAcceptanceList(RejectedLinenRequest request);

//	Object getRejectLinenAcceptanceDetails(Integer id);

//	Object saveRejectMaterialAcceptance(LinenIssueRequest request);



}

