package com.param.pharmacy.nursing.pharmacy.service.billableConsumption;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.pharmacy.dto.BillableConsumptionSave;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionDetails;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionResponse;
@SuppressWarnings("rawtypes")
public interface IBillableConsumptionService {
	
	CommonDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	List<BillableConsumptionResponse> billableConsumptionSearch(BillableConsumptionRequest request);
	
	BillableConsumptionDetails billableConsumptionDetails(Integer id, Integer unitId);
	
	CommonResponse billableConsumptionSave(BillableConsumptionSave request) throws Exception;

	List<BillableConsumptionSave> getBillableConsumptionDetails(Long id)throws Exception;

	
}