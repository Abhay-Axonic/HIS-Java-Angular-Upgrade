package com.param.pharmacy.narcotics.deliveryWorklist.service;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsDetails;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsResponse;
import com.param.pharmacy.narcotics.issueWorklist.dto.UpdateCarrier;

public interface INarcoticsDeliveryWorklistService {
	
	CommonDropdown getDropdownList(Integer unitId);
	
	List<NarcoticsResponse> narcoticsDeliveryWorklistSearch(NarcoticsRequest narcoticsRequest);

	NarcoticsDetails narcoticsDeliveryWorklistDetails(Long id, Integer unitId, Integer storeId, Integer orgId) throws Exception;
	
	CommonResponse narcoticsDeliveryWorklistSave(UpdateCarrier updateCarrier);
	
}