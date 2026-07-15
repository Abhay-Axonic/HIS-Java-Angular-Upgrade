package com.param.inventory.store.dao.MaterialInwardApproval;

import java.util.List;

import com.param.entity.model.inventory.MaterialInward;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardResponse;

public interface IMaterialInwardApprovalDao {

	List<MaterialInwardResponse> searchMaterialInwardApprovalList(MaterialInwardRequest request, Integer screenId);

	MaterialInward getMaterialInwardApproval(Integer id);
	
}
