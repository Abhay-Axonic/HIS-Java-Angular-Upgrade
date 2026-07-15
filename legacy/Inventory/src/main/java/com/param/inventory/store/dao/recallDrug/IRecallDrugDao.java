package com.param.inventory.store.dao.recallDrug;

import java.util.List;

import com.param.inventory.store.dto.RecallDrug.RecallDrugRequest;
import com.param.inventory.store.dto.RecallDrug.RecallDrugResponse;

public interface IRecallDrugDao {

	List<RecallDrugResponse> searchRecallDrugList(RecallDrugRequest request, Integer screenId);

}
