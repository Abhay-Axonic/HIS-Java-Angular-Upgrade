package com.param.procurement.consignment.service.consignment;

import java.util.List;

import com.param.procurement.consignment.dto.consignment.ConsignmentDropdown;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponse;
public interface IConsignmentOutwordService
{
	ConsignmentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	Object saveConsignmentOutward(ConsignmentRequest request);
	
	List<ConsignmentResponse> getConsignmentOutwardList(ConsignmentRequest request);
		
	ConsignmentResponse getConsignmentOutwardDetails(Integer id, Integer unitId);

	Object updateConsignmentOutword(ConsignmentRequest request);
	
	Object approveConsignmentOutword(ConsignmentRequest request);
	
	Object cancelConsignmentOutword(ConsignmentRequest request);

}
