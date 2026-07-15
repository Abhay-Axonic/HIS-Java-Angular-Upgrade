package com.param.inventory.stockAdjustments.dao.uploadStock;

import java.util.List;

import com.param.entity.model.inventory.ComputeStock;
import com.param.entity.model.inventory.StopTransaction;
import com.param.entity.model.inventory.StopTransactionItemDetail;
import com.param.entity.model.inventory.UploadStock;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;

public interface IUploadStockDao {

	UploadStock saveUploadStock(UploadStock uploadStock);

	List<StopTransaction> getStopTransactionItems(UploadStockRequest request);

	List<StopTransactionItemDetail> getStopTransactionItemsDetails(UploadStockRequest request);

	List<UploadStockResponse> searchUploadStockList(UploadStockRequest request, Integer screenId);

	UploadStock getUploadStock(Integer id );

	UploadStock updateUploadStock(UploadStock uploadStock);

	List<UploadStockDetail> getUploadStockDetail(Integer id);

	ComputeStock saveComputeStock(ComputeStock computeStock);

	StopTransaction getStopTransaction(UploadStockRequest request);
	
	String getPrefixRunningNumber(Integer storeId , int unitId, Integer screenId);
	
	List<StopTransactionItemDetail> getStopTransactionItemsDetailsOldBatch(UploadStockRequest request); 
	
	Integer getStoreStockByBatch(Integer storeId, Long batchId);

	UploadStock getUploadStockWithUnitId(Integer id, Integer unitId);

	void inactiveOldItems(Integer id);
}
