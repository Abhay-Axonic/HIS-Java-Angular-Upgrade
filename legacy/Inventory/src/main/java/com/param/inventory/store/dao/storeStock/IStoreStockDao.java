package com.param.inventory.store.dao.storeStock;

import java.math.BigInteger;
import java.util.List;

import com.param.global.dto.items.BatchResponse1;
import com.param.global.dto.items.ItemWiseBatchResponse;
import com.param.inventory.store.dto.storeStock.ExpiredStoreStockRequest;
import com.param.inventory.store.dto.storeStock.ExpiredStoreStockResponse;
import com.param.inventory.store.dto.storeStock.StockInTransitResponse;
import com.param.inventory.store.dto.storeStock.StoreStockRequest;
import com.param.inventory.store.dto.storeStock.StoreStockRequestDate;
import com.param.inventory.store.dto.storeStock.StoreStockResponse;
import com.param.inventory.store.dto.storeStock.SubstituteStoreStockDashboardDto;

public interface IStoreStockDao {

	List<StoreStockResponse> getStoreStock(StoreStockRequest request);

	List<com.param.global.dto.items.BatchResponse1> getBatchDetails(int storeId, int itemId,boolean showDetails, Integer unitId);
	
	List<com.param.global.dto.items.BatchResponse1> getBatchNotApplicableDetails(int storeId, int itemId, Integer unitId);

	List<StoreStockResponse> getStoreIndentDateWiseList(StoreStockRequestDate request);
	
	List<ItemWiseBatchResponse> getItemWiseBatchDetails(int storeId, int itemId);

	List<ItemWiseBatchResponse> getItemWiseBatchDetailsWithUnit(int itemId,Integer unitId);

	List<ItemWiseBatchResponse> getBatchWiseBatchDetails(int batchId, Integer unitId);

	List<ExpiredStoreStockResponse> expiredStoreStockDashboard(ExpiredStoreStockRequest request);

	List<BatchResponse1> getItemBatchDetails(Integer storeId, Integer id, Long id2, Integer uomTypeId);

	List<BatchResponse1> getExpiredBatchDetails(int storeId, int itemId, boolean showDetails, Integer unitId);

	BatchResponse1 getUpdatedBatchDetails(int storeId, BigInteger bigInteger, Integer unitId);

	List<StoreStockResponse> getStoreIndentDateWiseZeroStockList(StoreStockRequestDate request);

	List<BatchResponse1> getZeroStockBatchDetails(int storeId, int itemId, boolean showDetails);

	List<StoreStockResponse> getStoreIndentDateWiseZeroWikthStockList(StoreStockRequestDate request);

	List<ExpiredStoreStockResponse> nextExpiredStoreStockDashboard(ExpiredStoreStockRequest request);

	List<BatchResponse1> getNextExpiredBatchDetails(int storeId, int itemId, Integer month, Integer unitId);

	List<StockInTransitResponse> stockInTransit(ExpiredStoreStockRequest request);
	
	List<SubstituteStoreStockDashboardDto> getSubstituteByIdForStoreStockDB(Integer genericId, Integer itemId, Boolean isNarcotic, Integer storeId, Integer unitId, String dateString);

	List<BatchResponse1> getUOMUnitType(int storeId, int itemId, Integer unitId, String batchno);
}
