package com.param.inventory.store.service.batchChange;

import com.param.inventory.store.dto.batchChange.BatchChangeRequest;

public interface IBatchChangeService {

	Object saveBatchChange(BatchChangeRequest request);

	Object getBatchChangeList(BatchChangeRequest request);

	Object getBatchChangeDetail(Integer id);

	Object updateBatchChange(BatchChangeRequest request);

	Object cancelBatchChange(BatchChangeRequest request);

	Object submitBatchChange(BatchChangeRequest request);

}
