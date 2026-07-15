package com.param.inventory.store.service.materialPicker;

import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;

public interface IMaterialPickerService {

	StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getMaterialPickerList(MaterialPickerRequest request);

	Object getMaterialPicker(Integer id, Integer unitId, Integer orgId);

	Object saveMaterialPicker(MaterialPickerRequest request);

	//Object cancelMaterialPicker(MaterialPickerRequest request);

}
