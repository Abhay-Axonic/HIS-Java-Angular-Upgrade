package com.param.inventory.store.service.materialIssueList;

import com.param.entity.model.inventory.MaterialIssue;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueListDropdownResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;

public interface IMaterialIssueListService {

	Object getMaterialIssueList(MaterialPickerRequest request);

	Object saveMaterialIssueList(MaterialPickerRequest request);

	/*Object getMaterialIssueListDetails(Integer id);*/

	Object getMaterialIssueDetails(Integer id, Integer orgId, Integer unitId);

	StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	MaterialIssueListDropdownResponse getDropdownsMaterialIssueList(Integer unitId, Integer orgId, Integer userId);

	MaterialIssue getMaterialIssueDetailsById(MaterialPickerRequest res,Long id);

}
