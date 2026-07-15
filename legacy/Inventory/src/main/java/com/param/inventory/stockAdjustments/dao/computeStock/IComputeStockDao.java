package com.param.inventory.stockAdjustments.dao.computeStock;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ComputeStock;
import com.param.entity.model.inventory.ComputeStockDetail;
import com.param.entity.model.inventory.ConfirmStock;
import com.param.entity.model.inventory.UploadStock;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.entity.model.inventory.VarianceApproval;
import com.param.global.dto.items.BatchResponse;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockRequest;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockResponse;

public interface IComputeStockDao {

	List<ComputeStockResponse> searchComputeStockList(ComputeStockRequest request, Integer screenId);

	ComputeStock getComputeStock(Integer id);

	ComputeStock updateComputeStock(ComputeStock computeStock);

	VarianceApproval saveVarianceApproval(VarianceApproval varianceApproval);
	
	ConfirmStock saveConfirmStock(ConfirmStock confirmStock);
	
	List<ComputeStockDetail> getComputeStockDetail(Integer id); 
	
	Long getStoreStockByItemForComputeStock(Integer storeId, Integer itemId);
	
	List<Batch> getBatchList(Integer itemId, Long batchId, Integer storeId);

	Integer getUploadStockByItemForComputeStock(Integer itemId, Integer computeId);
	
	Batch getQuantity(Integer storeId, Long batchId);	
	
	List<Batch> getNewBatchList(Integer itemId, Integer storeId, Integer computeStockId, Integer unitId);
	
	ComputeStockDetail saveComputeStockDetail(ComputeStockDetail computeStockDetail);
	
	UploadStockDetail saveUploadStockDetail(UploadStockDetail uploadStockDetail);
	
	List<Long> getBatchIds(Integer computeStockId);
	
	Boolean CheckDataForConfirmStock(Integer id);
}
