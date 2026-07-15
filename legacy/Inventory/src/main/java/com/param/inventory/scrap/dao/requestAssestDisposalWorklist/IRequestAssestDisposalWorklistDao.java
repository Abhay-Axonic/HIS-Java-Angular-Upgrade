package com.param.inventory.scrap.dao.requestAssestDisposalWorklist;

import java.util.List;

import com.param.entity.model.inventory.AssetDisposalWorklistRequest;
import com.param.entity.model.inventory.AssetDisposalWorklistRequestDetail;
import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

public interface IRequestAssestDisposalWorklistDao {

	List<ScrapResponse> getRequestAssestDisposalWorklistList(ScrapRequest request, Integer screenId);

	AssetDisposalWorklistRequest saveRequestAssestDisposalWorklist(
			AssetDisposalWorklistRequest assetDisposalWorklistRequest);

	ScrapResponse getScrapRequest(Integer id);

	ScrapResponse getRequestAssestDisposalWorklist(Integer id);

	List<ItemResponse> getScrapRequestDetail(Integer id);
	
	List<ItemResponse> getRequestAssestDisposalWorklistDetails(Integer id);

	com.param.entity.model.inventory.ScrapRequest getScrapRequestById(Integer scrapRequestId);

	AssetDisposalWorklistRequest getRequestAssetDisposalWorklist(Integer id);

	List<AssetDisposalWorklistRequestDetail> getRequestAssetDisposalWorklistDetail(Integer id);

	WasteAssetDisposalWorklist saveWasteAssestDisposalWorklist(WasteAssetDisposalWorklist wasteAssetDisposalWorklist);

	AssetDisposalWorklistRequest updateRequestAssestDisposalWorklist(AssetDisposalWorklistRequest assetDisposalWorklistRequest);
}
