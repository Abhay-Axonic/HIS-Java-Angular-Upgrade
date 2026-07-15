package com.param.inventory.stockAdjustments.service.uploadStock;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.ServiceMasterDto;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;

import in.param.exception.ApplicationException;

public interface IUploadStockService {

	StockAdjustmentDropdownResponse getDropdownList(Integer unitId, Integer orgId, Integer userId);

	Object getUploadStockList(UploadStockRequest request);

	Object saveUploadStock(UploadStockRequest request);

	List<UploadStockResponse> getUploadStockItemList(UploadStockRequest request);

	UploadStockResponse getuploadStockAddItemList(UploadStockRequest request);

	UploadStockResponse getUploadStockDetails(Integer id ,Integer unitId);

	Object cancelUploadStock(UploadStockRequest request);

	Object approveUploadStock(UploadStockRequest request);

	Object updateUploadStock(UploadStockRequest request);
	
	String getPrefixRunningNumber(Integer storeId , int unitId, Integer screenId);
	
	UploadStockResponse getuploadStockAddItemListOldBatch(UploadStockRequest request);

	@SuppressWarnings("rawtypes")
	Response readCSVFile(UploadStockRequest dto)throws ApplicationException; 
}
