package com.param.inventory.stockAdjustments.dao.uploadStockApproval;

import java.util.List;

import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;

public interface IUploadStockApprovalDao {

	List<UploadStockResponse> searchUploadStockApprovalList(UploadStockRequest request, Integer screenId);

}
