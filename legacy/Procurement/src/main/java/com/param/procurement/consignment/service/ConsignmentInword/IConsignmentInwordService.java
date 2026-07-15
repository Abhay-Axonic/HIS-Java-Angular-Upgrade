package com.param.procurement.consignment.service.ConsignmentInword;

import java.util.List;

import com.param.procurement.consignment.dto.consignment.ConsignmentDropdown;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponse;

public interface IConsignmentInwordService {

	ConsignmentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);

	Object saveConsignmentInward(ConsignmentRequest request);

	List<ConsignmentResponse> getConsignmentInwardList(ConsignmentRequest request);

	ConsignmentResponse getConsignmentInwardDetails(Integer id, Integer unitId);

	Object updateConsignmentInword(ConsignmentRequest request);

	Object approveConsignmentInword(ConsignmentRequest request);

	Object cancelConsignmentInword(ConsignmentRequest request);

}
