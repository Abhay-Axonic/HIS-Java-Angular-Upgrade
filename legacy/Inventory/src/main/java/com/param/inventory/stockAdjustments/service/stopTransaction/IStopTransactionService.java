package com.param.inventory.stockAdjustments.service.stopTransaction;

import java.math.BigInteger;
import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionSearchResponse;

public interface IStopTransactionService {

	Object saveAddNewStopTransaction(StopTransactionRequest request);

	Object saveStopTransaction(StopTransactionRequest request);

	Object getStopTransactionList(StopTransactionRequest request);

	List<StopTransactionSearchResponse> getStopTransactionNewList(StopTransactionRequest request);

	Object getProductCategoryList(String name);

	Object cancelStopTransaction(StopTransactionRequest request);

	Object getStopTransaction(Integer id);

	Object approveStopTransaction(StopTransactionRequest request);		
	
	Object ChecStockInTransit(Integer storeId); 
	
	Object CheckStopTransactionDuration(Integer storeId , Integer orgId, Integer  unitId );
	Object CheckStopTransactionDuration(StopTransactionRequest request);//HIS-10092 While selecting start date time and stop date time then wrong validation showing.
	
	Object CheckStopTransactionItemWise(StopTransactionRequest request);
	
	Object CheckStopTransactionBatchWise(StopTransactionRequest request);
	
	Object sendStopTransactionAlert(Integer storeId , Integer unitId);
	
	Object removeItemsByProductId(Integer ProductId,Integer stopTransactionId,StopTransactionRequest request);
	
	Object removeItembyItemId(StopTransactionRequest request);
	
	/* start code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */
	List<BigInteger> CheckStopTransactionDetail(StopTransactionRequest request);
	/* end code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */

	Object getStopTransactionNonZeroNewList(StopTransactionRequest request);
}
	