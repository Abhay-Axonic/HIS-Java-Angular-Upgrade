package com.param.inventory.store.service.materialIssuePending;

import com.param.global.common.Response;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;

public interface IMaterialIssuePendingService {

	StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getMaterialIssuePendingList(MaterialPickerRequest request);

	Object saveMaterialIssue(MaterialPickerRequest request);

	Object getMaterialIssue(Integer id, Integer orgId, Integer unitId);

	Response materialIssueStatus(Integer id);

}
