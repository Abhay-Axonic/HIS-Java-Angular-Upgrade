package com.param.inventory.scrap.service.scrapRequestApproval;

import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;

public interface IScrapRequestApprovalService {

	ScrapsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	ScrapsDropdownResponse getDropownsByUserId(Integer id);

	Object getScrapRequestApprovalList(ScrapRequest request);

	Object getScrapRequestApprovalDetails(Integer id, Integer unitId);

	Object approveScrapRequestApproval(ScrapRequest request);

    Object cancelScrapRequestApproval(ScrapRequest request);
}
