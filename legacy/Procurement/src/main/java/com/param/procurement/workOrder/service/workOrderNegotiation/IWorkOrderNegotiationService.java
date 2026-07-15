package com.param.procurement.workOrder.service.workOrderNegotiation;

import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationRequest;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;

public interface IWorkOrderNegotiationService {

	WorkOrderDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWoQuotationNegotiationList(WONegotiationRequest woNegotiationRequest);

	Object saveWoQuotationNegotiation(WONegotiationRequest woNegotiationRequest);

	//Object updateWoQuotationNegotiation(WONegotiationRequest woNegotiationRequest);

	Object getWoQuotationNegotiation(Integer id);

	Object updateWoQuotationNegotiation(WONegotiationRequest woNegotiationRequest);

	Object saveVendorSelection(WONegotiationRequest woNegotiationRequest);

}
