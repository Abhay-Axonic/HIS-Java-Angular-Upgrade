package com.param.inventory.store.dao.batchChange;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchExp;
import com.param.entity.model.inventory.BatchExpDetail;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.inventory.store.dto.batchChange.BatchChangeResponse;
import com.param.inventory.store.dto.batchChange.BatchChangeRequest;
import com.param.inventory.store.dto.batchChange.BatchResponse;

public interface IBatchChangeDao {

	BatchExp saveBatchChange(BatchExp batchExp);

	List<BatchChangeResponse> getBatchChangeList(BatchChangeRequest request, Integer screenId);

	BatchChangeResponse getBatchChange(Integer id);

	List<BatchResponse> getBatchChangeDetails(Integer id);

	BatchExp getBathExp(Integer id);

	BatchExp updateBatchChange(BatchExp batchExp);

	void updateBatch(Batch b);

	Batch getbatch(Integer id, BatchExpDetail detail, Double mrpLeaseQuantity, Double lcopLeaseQuantity , Double copLeaseQuantity);

	List<BatchExpDetail>  getBatchChangeDetailsById(Integer id);

	Integer getBatchId(Integer id);

	List<BatchUomMapper> getBatchUomMapper(Integer batchId);

	Batch getbatchWithOutLcop(Integer batchId, BatchExpDetail detail, Double mrpLeaseQuantity, Double copLeaseQuantity);

}
