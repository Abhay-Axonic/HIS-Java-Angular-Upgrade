package com.param.inventory.store.service.rejectedMaterialAcceptance;

import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueRequest;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialsDropdownResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;

public interface IRejectedMaterialAcceptanceService {

	MaterialsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getRejectMaterialAcceptanceList(MaterialPickerRequest request);

	Object getRejectMaterialAcceptanceDetails(Integer id);

	Object saveRejectMaterialAcceptance(MaterialIssueRequest request);

}
