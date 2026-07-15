package com.param.procurement.consignment.service.ConsignmentPOCreation;

import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationResponse;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponse;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDropDownDto;

public interface IConsignmentPOCreationService {
	
	 PurchaseOrderDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);
	
	 Object getConsignmentPuchaseOrderList(ConsignmentPOCreationRequest consignmentPOCreationRequest);
	
	 Object getConsignmentgetissueListForSavePO(ConsignmentPOCreationRequest consignmentPOCreationRequest);
	
	 Object saveConsignmentPOCreation(ConsignmentPOCreationRequest consignmentPOCreationRequest);
	
	ConsignmentPOCreationResponse getConsignmentPOCreationDetails(Integer id, Integer unitId);
	
	 Object updateConsignmentPOCreationDetails(ConsignmentPOCreationRequest consignmentPOCreationRequest);
	 
	 Object approveConsignmentPurchaseOrder(ConsignmentPOCreationRequest consignmentPOCreationRequest);
	 
	 Object cancelConsignemntPurchaseOrder(ConsignmentPOCreationRequest consignmentPOCreationRequest);

}
