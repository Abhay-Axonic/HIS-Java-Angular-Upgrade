package com.param.inventory.store.service.storeStock;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.inventory.store.dto.storeStock.ExpiredStoreStockRequest;
import com.param.inventory.store.dto.storeStock.ExpiredStoreStockResponse;
import com.param.inventory.store.dto.storeStock.StoreStockRequest;
import com.param.inventory.store.dto.storeStock.StoreStockRequestDate;
import com.param.inventory.store.dto.storeStock.SubstituteStoreStockDashboardDto;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;

public interface IStoreStockService {

	Object getStoreStock(StoreStockRequest request);

	List<com.param.global.dto.items.BatchResponse1>  getBatchDetails(int storeId, int itemId,boolean showDetails, Integer unitId);


	Object getStoreIndentDateWiseList(StoreStockRequestDate request);

	Object getItemWiseBatchDetails(int storeId, int itemId);

	Object getItemWiseBatchDetailsWithUnit(int itemId,Integer unitId);

	Object getBatchWiseBatchDetails(int batchId, Integer unitId);

	List<ExpiredStoreStockResponse> expiredStoreStockDashboard(ExpiredStoreStockRequest request);

	List<com.param.global.dto.items.BatchResponse1>  getExpiredBatchDetails(int storeId, int itemId, boolean showDetails, Integer unitId);

	Object  getStoreIndentDateWiseZeroStockList(StoreStockRequestDate request);

	List<com.param.global.dto.items.BatchResponse1> getZeroStockBatchDetails(int storeId, int itemId, boolean showDetails, Integer unitId);

	Object getStoreIndentDateWiseZeroWikthStockList(StoreStockRequestDate request);

	List<ExpiredStoreStockResponse> nextExpiredStoreStockDashboard(ExpiredStoreStockRequest request);

	List<com.param.global.dto.items.BatchResponse1> getNextExpiredBatchDetails(int storeId, int itemId, Integer month, Integer unitId);

	Object stockInTransit(ExpiredStoreStockRequest request);

	List<SubstituteStoreStockDashboardDto> getSubstituteDetailsForStoreStockDashboard(Integer genericId, Integer itemId,
			Boolean isNarcotic, Integer storeId, Integer unitId, String dateString) throws Exception;

}
