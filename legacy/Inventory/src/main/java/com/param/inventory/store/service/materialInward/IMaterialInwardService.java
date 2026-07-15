package com.param.inventory.store.service.materialInward;

import java.util.List;

import com.param.inventory.store.dto.MaterialInwardRequestNew;
import com.param.inventory.store.dto.materialInward.MaterialInwardDropdownResponse;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;

public interface IMaterialInwardService {

	MaterialInwardDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getMaterialInwardList(MaterialInwardRequest request);

	Object saveMaterialInward(MaterialInwardRequestNew request);
	Object saveMaterialInwardNew(MaterialInwardRequestNew request);

	Object updateMaterialInward(MaterialInwardRequest request);

	Object getMaterialInward(Integer id, Integer unitId, Integer orgId);

	Object cancelMaterialInward(MaterialInwardRequest request);

	Object getReturnableGatepassList(MaterialInwardRequest request);

	Object getReturnableGatepassSearchList(MaterialInwardRequest request);

	Object getMultipleReturnableGatepassSearch(List<MaterialInwardRequest> request, Integer orgId, Integer unitId);

	Object getReturnableGatepassVendorWiseSearch(Integer vendorid);

}
