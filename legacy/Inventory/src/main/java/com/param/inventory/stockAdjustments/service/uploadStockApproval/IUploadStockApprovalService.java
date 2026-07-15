package com.param.inventory.stockAdjustments.service.uploadStockApproval;

import org.springframework.util.MultiValueMap;

import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;

public interface IUploadStockApprovalService {

	StockAdjustmentDropdownResponse getDropdownList(Integer unitId, Integer orgId, Integer userId);

	Object getUploadStockApprovalList(UploadStockRequest request);

	Object approveUploadStockApproval(UploadStockRequest request);

	Object getUploadStockApprovalDetails(Integer id);

	Object cancelUploadStockApproval(UploadStockRequest request);

	Object uploadStockApproval(UploadStockRequest request);

	Object approveUploadStockNewApproval(UploadStockRequest request);

}
