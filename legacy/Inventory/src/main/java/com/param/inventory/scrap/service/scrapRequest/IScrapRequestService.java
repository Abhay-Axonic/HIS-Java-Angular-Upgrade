package com.param.inventory.scrap.service.scrapRequest;

import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;

public interface IScrapRequestService {

	ScrapsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	ScrapsDropdownResponse getDropownsByUserId(Integer id);

	Object saveScrapRequest(ScrapRequest request);

	Object getScrapRequestList(ScrapRequest request);

	Object getScrapRequestDetails(Integer id);

	Object approveScrapRequest(ScrapRequest request);

	Object cancelScrapRequest(ScrapRequest request);

	Object updateScrapRequest(ScrapRequest request);

}
