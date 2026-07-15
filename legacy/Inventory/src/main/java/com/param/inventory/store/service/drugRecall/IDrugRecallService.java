package com.param.inventory.store.service.drugRecall;

import com.param.inventory.store.dto.drugRecall.DrugRecallDropdownResponse;
import com.param.inventory.store.dto.drugRecall.DrugRecallRequest;

public interface IDrugRecallService {

	Object getDrugRecallList(DrugRecallRequest request);

	Object saveDrugRecall(DrugRecallRequest request);

	DrugRecallDropdownResponse getDropdowns();

	Object getDrugRecall(Integer id);

	Object cancelDrugRecall(DrugRecallRequest request);

	Object updateDrugRecall(DrugRecallRequest request);

	Object approveDrugRecall(DrugRecallRequest request);

}
