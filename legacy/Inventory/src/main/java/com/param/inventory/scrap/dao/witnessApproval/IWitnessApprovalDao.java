package com.param.inventory.scrap.dao.witnessApproval;

import java.util.List;

import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.entity.model.inventory.WasteAssetDisposalWorklistDetail;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

public interface IWitnessApprovalDao {

	List<ScrapResponse> getWitnessApprovalList(ScrapRequest request, Integer screenId);

	WasteAssetDisposalWorklist getWasteAssestDisposalWorklist(Integer id);

	List<WasteAssetDisposalWorklistDetail> getWasteAssestDisposalWorklistDetail(Integer wasteAssetDisposalWorklistId);

	WasteAssetDisposalWorklist saveWitnessApproval(WasteAssetDisposalWorklist wasteAssetDisposalWorklist);

	ScrapResponse getWitnessApproval(Integer id);

	List<ItemResponse> getWitnessApprovalDetail(Integer id);

}
