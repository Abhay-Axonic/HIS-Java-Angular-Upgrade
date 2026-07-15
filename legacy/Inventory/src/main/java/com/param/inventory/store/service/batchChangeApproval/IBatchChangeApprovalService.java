package com.param.inventory.store.service.batchChangeApproval;

import com.param.inventory.store.dto.batchChange.BatchChangeRequest;

public interface IBatchChangeApprovalService {

	Object getBatchChangeList(BatchChangeRequest request);

	Object getBatchChangeDetails(Integer id);

	Object approveBatchChange(BatchChangeRequest request);

	Object approveBatchChangeDetails(BatchChangeRequest request);

	Object cancelBatchChange(BatchChangeRequest request);
	
}