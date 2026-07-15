package com.param.inventory.stockAdjustments.service.varianceApproval;

import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;

public interface IVarianceApprovalService {

	StockAdjustmentDropdownResponse getDropdownList(Integer unitId, Integer orgId, Integer userId);

	Object getVarianceApprovalList(UploadStockRequest request);

	Object getVarianceApprovalDetails(Integer id , Integer unitId);

	Object cancelVarianceApproval(UploadStockRequest request);

	Object approveVarianceApproval(UploadStockRequest request);

}
