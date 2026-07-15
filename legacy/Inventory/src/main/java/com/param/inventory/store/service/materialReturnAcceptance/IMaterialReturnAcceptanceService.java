package com.param.inventory.store.service.materialReturnAcceptance;

import com.param.inventory.store.dto.materialIssueAcceptance.MaterialsDropdownResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;

public interface IMaterialReturnAcceptanceService {

	MaterialsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getMaterialReturnAcceptanceList(MaterialReurnRequest request);

	Object updateMaterialReturnAcceptance(MaterialReurnRequest request);

	Object getMaterialReturnAcceptanceDetails(Integer id, Integer unitId);

}
