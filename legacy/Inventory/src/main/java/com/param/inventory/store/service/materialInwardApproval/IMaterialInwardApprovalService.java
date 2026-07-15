package com.param.inventory.store.service.materialInwardApproval;

import com.param.inventory.store.dto.materialInward.MaterialInwardDropdownResponse;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;

public interface IMaterialInwardApprovalService {

	MaterialInwardDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getMaterialInwardApprovalList(MaterialInwardRequest request);

	Object getMaterialInwardApproval(Integer id);

	Object approveMaterialInwardApproval(MaterialInwardRequest request);

}
