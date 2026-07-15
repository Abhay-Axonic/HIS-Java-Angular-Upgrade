package com.param.inventory.store.dao.drugRecall;

import java.util.List;

import com.param.entity.model.inventory.DrugRecall;
import com.param.entity.model.inventory.DrugRecallDetail;
import com.param.inventory.store.dto.drugRecall.DrugRecallRequest;
import com.param.inventory.store.dto.drugRecall.DrugRecallResponse;

public interface IDrugRecallDao {

	List<DrugRecallResponse> searchDrugRecallList(DrugRecallRequest request, Integer screenId);

	DrugRecall saveDrugRecall(DrugRecall drugRecall);

	DrugRecall getDrugRecall(Integer id);

	DrugRecall updateDrugRecall(DrugRecall drugRecall);

	DrugRecallDetail saveDrugRecallDetail(DrugRecallDetail recall);

}
