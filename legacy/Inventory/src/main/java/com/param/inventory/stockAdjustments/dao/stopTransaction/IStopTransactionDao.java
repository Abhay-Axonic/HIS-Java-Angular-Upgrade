package com.param.inventory.stockAdjustments.dao.stopTransaction;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.inventory.StopTransaction;
import com.param.entity.model.inventory.StopTransactionItemDetail;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Store;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionProcessResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionSearchResponse;

public interface IStopTransactionDao {

	StopTransaction saveAddNewStopTransaction(StopTransaction stopTransaction);

	StopTransaction getStopTransaction(Integer id);

	StopTransaction updateStopTransaction(StopTransaction stopTransaction);

	List<StopTransactionResponse> searchStopTransactionList(StopTransactionRequest request, Integer screenId);

	List<StopTransactionSearchResponse> searchStopTransactionNewList(StopTransactionRequest request, Integer screenId);

	List<ProductCategory> getProductCategoryList(String name);

	List<StopTransactionSearchResponse> searchAllStopTransactionNewList(StopTransactionRequest request, Integer screenId);	
	
	Boolean isProductExclusionExist(Integer stopTransactionid,Integer prodId); 
		
	Boolean isItemWiseExclutionExist(Integer stopTransactionid,Integer itemId); 
	
	Boolean isStopTransactionItemDetail(Integer stopTransactionid, Long batchId);
	
	Long ChecStockInTransit(Integer storeId); 
	
	Object CheckStopTransactionDuration(Integer storeId ,Integer orgId , Integer unitId);
	Object CheckStopTransactionDuration(StopTransactionRequest request);//HIS-10092 While selecting start date time and stop date time then wrong validation showing.
	
	List<StopTransactionProcessResponse> CheckStopTransactionItemWise(Integer storeId,List<Integer> itemIds, StopTransactionRequest request);
	
	List<StopTransactionProcessResponse> CheckStopTransactionBatchWise (Integer storeId,List<Long> batchIds, StopTransactionRequest request);
	
	Store getStoreForAlert(Integer storeId , Integer unitId);
	
	Store updateStoreforAlert(Store store);
	//List<StopTransactionProcessResponse> CheckStopTransactionBatchWise(StopTransactionRequest request);
	
	List<StopTransactionItemDetail> getStopTransactionItemDetail(Integer ProductId,Integer stopTransactionId);
	
	StopTransactionItemDetail updateStopTransactionItemDetail(StopTransactionItemDetail stopTransactionItemDetail);
	
	List<StopTransactionItemDetail> getStopTransactionItemDetailByItemId(List<Integer> itemIds,Integer stopTransactionId);
	
	/* start code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */
    List<BigInteger> CheckStopTransactionDetail(StopTransactionRequest request);
    /* end code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */

	List<StopTransactionSearchResponse> searchAllStopTransactionNonZeroNewList(StopTransactionRequest request,
			Integer screenId);

	List<StopTransactionSearchResponse> searchStopTransactionNonZeroNewList(StopTransactionRequest request,
			Integer screenId);
}
