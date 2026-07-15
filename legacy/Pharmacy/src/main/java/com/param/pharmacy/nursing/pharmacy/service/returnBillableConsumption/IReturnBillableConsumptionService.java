package com.param.pharmacy.nursing.pharmacy.service.returnBillableConsumption;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.global.billing.controller.BillingException;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.pharmacy.dto.BillableConsumptionSave;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionDetails;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionResponse;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.ConsumptionIds;

public interface IReturnBillableConsumptionService {
	
	CommonDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	List<BillableConsumptionResponse> returnBillableConsumptionSearch(BillableConsumptionRequest request);
	
	BillableConsumptionDetails returnBillableConsumptionDetails(Integer id, Integer unitId);
	
	CommonResponse returnBillableConsumptionSave(BillableConsumptionSave request) throws BillingException, Exception;
	
	List<BillableConsumptionDetails> returnBillableConsumptionSearchIssue(BillableConsumptionRequest request);
	
	List<BillableConsumptionDetails> returnBillableConsumptionAdd(ConsumptionIds ids);
	
}