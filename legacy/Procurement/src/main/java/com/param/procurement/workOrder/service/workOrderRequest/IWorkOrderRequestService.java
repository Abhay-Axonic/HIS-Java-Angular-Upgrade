package com.param.procurement.workOrder.service.workOrderRequest;

import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequest;

public interface IWorkOrderRequestService {

	Object approveWorkOrderRequest(WorkOrderRequest workOrderRequest);

	WorkOrderDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWorkOrderList(WorkOrderRequest workOrderRequest);
	
	Object saveWorkOrderRequest(WorkOrderRequest workOrderRequest);

	Object updateWorkOrderRequest(WorkOrderRequest workOrderRequest);

	Object getWorkOrderRequest(Integer id, Integer unitId);

	Object cancelworkOrderRequest(WorkOrderRequest workOrderRequest);

}
