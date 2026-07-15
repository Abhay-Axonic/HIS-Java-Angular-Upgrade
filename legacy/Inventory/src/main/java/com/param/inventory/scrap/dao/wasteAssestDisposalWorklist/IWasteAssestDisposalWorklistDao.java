package com.param.inventory.scrap.dao.wasteAssestDisposalWorklist;

import java.util.List;

import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

public interface IWasteAssestDisposalWorklistDao {

	List<ScrapResponse> getWasteAssestDisposalWorklistList(ScrapRequest request, Integer screenId);

	WasteAssetDisposalWorklist saveWasteAssestDisposalWorklist(WasteAssetDisposalWorklist wasteAssetDisposalWorklist);

	ScrapResponse getWasteAssestDisposalWorklist(Integer id);

	List<ItemResponse> getWasteAssestDisposalWorklistDetail(Integer id);
	
	WasteAssetDisposalWorklist getWasteAssestsDisposalWorklist(Integer id);

	WasteAssetDisposalWorklist updateWasteAssestDisposalWorklist(WasteAssetDisposalWorklist wasteAssetDisposalWorklist);

}
