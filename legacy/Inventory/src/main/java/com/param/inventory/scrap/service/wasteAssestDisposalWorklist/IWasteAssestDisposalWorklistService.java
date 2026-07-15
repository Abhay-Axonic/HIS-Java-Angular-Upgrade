package com.param.inventory.scrap.service.wasteAssestDisposalWorklist;

import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.AssestDisposalRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;

public interface IWasteAssestDisposalWorklistService {

	ScrapsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWasteAssestDisposalWorklistList(ScrapRequest request);

	Object saveWasteAssestDisposalWorklist(AssestDisposalRequest request);

	Object getWasteAssestDisposalWorklistDetails(Integer id, Integer unitId, Integer orgId);

	Object updateWasteAssestDisposalWorklist(AssestDisposalRequest request);

}
