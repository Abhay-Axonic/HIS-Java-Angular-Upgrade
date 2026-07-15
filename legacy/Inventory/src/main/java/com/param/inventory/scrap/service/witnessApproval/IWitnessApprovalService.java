package com.param.inventory.scrap.service.witnessApproval;

import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.AssestDisposalRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;

public interface IWitnessApprovalService {

	ScrapsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWitnessApprovalList(ScrapRequest request);

	Object updateWitnessApproval(AssestDisposalRequest request);

	Object getWitnessApprovalDetails(Integer id);

}
