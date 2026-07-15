package com.param.inventory.stockAdjustments.service.computeStock;

import com.param.inventory.stockAdjustments.dto.ComputeStockDropdownResponse;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockRequest;

public interface IComputeStockService {

	Object getComputeStockList(ComputeStockRequest request);

	Object cancelComputeStock(ComputeStockRequest request);

	Object approveComputeStock(ComputeStockRequest request);

	ComputeStockDropdownResponse  getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getComputeStock(Integer id);
	
	Object saveComputeStock(ComputeStockRequest request);

}
