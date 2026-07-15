package com.param.inventory.scrap.dao.scrapRequest;

import java.util.List;

import com.param.entity.model.inventory.AssetDisposalWorklistRequest;
import com.param.entity.model.inventory.ScrapRequest;
import com.param.entity.model.inventory.ScrapRequestDetail;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

public interface IScrapRequestDao {

	ScrapRequest saveScrapRequest(ScrapRequest scrapRequest);

	List<ScrapResponse> scrapRequestList(com.param.inventory.scrap.dto.scrapRequest.ScrapRequest request, Integer screenId);

	ScrapRequest getScrapRequest(Integer id);

	List<ScrapRequestDetail> getScrapRequestdetail(Integer id);

	ScrapRequest updateScrapRequest(ScrapRequest scrapRequest);

	AssetDisposalWorklistRequest saveScrapRequest(AssetDisposalWorklistRequest assetDisposalWorklistRequest);

}
