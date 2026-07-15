package com.param.inventory.store.service.materialIssueAcceptance;

import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;

public interface IMaterialIssueAcceptanceService {

	Object getMaterialIssueAcceptanceList(MaterialPickerRequest request);

	Object getMaterialIssueAcceptanceDetails(Integer id, Integer orgId, Integer unitId);

	Object saveMaterialIssueAcceptance(MaterialIssueRequest request);

	StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

}
