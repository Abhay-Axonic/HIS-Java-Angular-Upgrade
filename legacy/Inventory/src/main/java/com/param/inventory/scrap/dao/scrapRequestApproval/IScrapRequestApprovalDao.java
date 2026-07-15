package com.param.inventory.scrap.dao.scrapRequestApproval;

import java.util.List;

import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

public interface IScrapRequestApprovalDao {

	List<ScrapResponse> scrapRequestApprovalList(ScrapRequest request, Integer screenId);

}
