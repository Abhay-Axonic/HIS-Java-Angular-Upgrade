package com.param.procurement.workOrder.service.workOrderCreation;



import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationRequest;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationDropDown;

public interface IWOCreationService {

	WorkOrderCreationDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWorkOrderCreationList(WOCreationRequest woCreationRequest);
	
	Object getWorkOrderCreationListByQR(WOCreationRequest woCreationRequest);
	
	Object getWorkOrderCreationListByWR(WOCreationRequest woCreationRequest);
	
	Object saveWorkOrderByQuotationRequest(WOCreationRequest woCreationRequest);

	Object updateWorkOrderCreation(WOCreationRequest woCreationRequest);
	
	Object getworkOrderCreation(Integer id, Integer unitId, Integer orgId);
	
	Object approveWorkOrderCreation(WOCreationRequest woCreationRequest);
	
	Object cancelWorkOrderCreation(WOCreationRequest woCreationRequest);

	Object getWorkOrderCreationQRById(WOCreationRequest woCreationRequest);

	Object getWorkOrderCreationWRById(WOCreationRequest woCreationRequest);

	
}
