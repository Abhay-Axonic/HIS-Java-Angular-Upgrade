package com.param.procurement.consignment.service.ConsignmentPOApproval;

import com.param.procurement.consignment.dto.ConsignmentPOApproval.ConsignmentPOApprovalDropdown;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationResponse;

public interface IConsignmentPOApprovalService {
	
	public ConsignmentPOApprovalDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);
	
	 Object getConsignmentPuchaseOrderList(ConsignmentPOCreationRequest consignmentPOCreationRequest);
	 
	 ConsignmentPOCreationResponse getConsignmentPOCreationDetails(Integer id, Integer unitId);
	 
	 Object approveConsignmentPurchaseOrder(ConsignmentPOCreationRequest consignmentPOCreationRequest);

}
