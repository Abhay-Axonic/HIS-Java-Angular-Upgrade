package com.param.inventory.stockAdjustments.dao.confirmStock;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ConfirmStock;
import com.param.entity.model.inventory.ConfirmStockDetail;
import com.param.entity.model.inventory.StopTransaction;
import com.param.entity.model.inventory.StopTransactionItemDetail;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.entity.model.inventory.VarianceApprovalDetail;
import com.param.inventory.stockAdjustments.dto.confirmStock.ConfirmStockResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;

public interface IConfirmStockDao {

	List<ConfirmStockResponse> searchConfirmStockList(UploadStockRequest request, Integer screenId);

	ConfirmStock getConfirmStock(Integer id, Integer unitId);

	ConfirmStock updateConfirmStock(ConfirmStock confirmStock);	
	
	ConfirmStockDetail getConfirmDetailStock(Integer id ,Integer unitId);
	
	UploadStockDetail getUploadDetailStock(Integer id ,Integer unitId);
	
	ConfirmStockDetail updateConfirStockDetail(ConfirmStockDetail confirmStockDetail);
	
	UploadStockDetail updateUploadStockDetail(UploadStockDetail uploadStockDetail);	
	
	Integer getStoreStockForByBatchForOtherStore(Integer storeId, Long batchId);
	
	VarianceApprovalDetail updateVarianceApprovalDetail(VarianceApprovalDetail varianceApprovalDetail);
	
	VarianceApprovalDetail getvarianceApprovalDetail(Integer id , Integer unitId);
	
	StopTransactionItemDetail getStopTransactionItemDetail(Integer stopTransactionId,Long batchId);	
	
	StopTransactionItemDetail updateStopTransactionItemDetail(StopTransactionItemDetail stopTransactionItemDetail);
	
	Batch getBatch(Long id);

	void updateStopTransaction(StopTransaction stopTransaction);
}
