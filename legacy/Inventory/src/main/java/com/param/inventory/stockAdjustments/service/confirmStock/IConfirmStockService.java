package com.param.inventory.stockAdjustments.service.confirmStock;

import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.confirmStock.ConfirmStockRequestDto;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;

public interface IConfirmStockService {

	StockAdjustmentDropdownResponse getDropdownList(Integer unitId, Integer orgId, Integer userId);

	Object getConfirmStockList(UploadStockRequest request);

	Object getConfirmStock(Integer id , Integer unitId);

	Object approveConfirmStock(ConfirmStockRequestDto confirmStockRequest);

	Object cancelConfirmStock(ConfirmStockRequestDto confirmStockRequest);

	Object detailApproveConfirmStock(ConfirmStockRequestDto confirmStockRequest);
	
	Object RejectConfirmStock(Integer confirmStockDetailId , Integer unitId , Integer statusId);

}
