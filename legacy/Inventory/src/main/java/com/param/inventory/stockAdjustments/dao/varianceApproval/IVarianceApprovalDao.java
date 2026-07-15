package com.param.inventory.stockAdjustments.dao.varianceApproval;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ConfirmStock;
import com.param.entity.model.inventory.VarianceApproval;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;

public interface IVarianceApprovalDao {

	List<UploadStockResponse> searchVarianceApprovalList(UploadStockRequest request, Integer screenId);

	VarianceApproval getVarianceApproval(Integer id , Integer unitId);

	VarianceApproval updateVarianceApproval(VarianceApproval varianceApproval);

	Batch getQuantity(Integer storeId, Long batchId , Integer unitId);

	ConfirmStock saveVarianceApproval(ConfirmStock confirmStock);

}
