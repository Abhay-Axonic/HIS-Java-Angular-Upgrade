package com.param.inventory.scrap.service.requestAssestDisposalWorklist;

import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.AssestDisposalRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;

public interface IRequestAssestDisposalWorklistService {

	ScrapsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getRequestAssestDisposalWorklistList(ScrapRequest request);

	Object saveRequestAssestDisposalWorklist(AssestDisposalRequest request);

	Object getRequestAssestDisposalWorklistDetails(Integer id, Integer orgId, Integer unitId);

	Object updateRequestAssestDisposalWorklist(AssestDisposalRequest request);

}
