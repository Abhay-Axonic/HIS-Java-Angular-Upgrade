package com.param.inventory.nursing.dao.nonBillableConsumption;

import java.util.List;

import com.param.entity.model.inventory.NonBillableConsumption;
import com.param.global.common.Response;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.BatchResponse;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionResponse;

import in.param.exception.ApplicationException;

public interface IConsumptionDao {

	NonBillableConsumption saveConsumption(NonBillableConsumption consumption);

	List<ConsumptionResponse> getConsumptionList(ConsumptionRequest request, Integer screenId);

	ConsumptionResponse getConsumption(Integer id);

	List<BatchResponse> getConsumptionDetail(Integer id);

	NonBillableConsumption getNonBillableConsumption(Integer id);

	NonBillableConsumption updateConsumption(NonBillableConsumption consumption);
	
	/* start code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */
	 // reference from batchListBillableConsumptionNew(ItemsServiceImpl.java L : 1605) -> 
	 // -> List<BatchListResponse> batchListStoreWise(ItemsRequest itemRequest) (ItemsDaoImpl.java L:923);
	List<BatchListResponse> batchListStoreWiseForNonBillableConsumption(ItemsRequest itemsRequest);	
	/* end code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */

	Response getConsumptionDetailsByTemplateTransactionId(ConsumptionRequest request)throws ApplicationException;

}
