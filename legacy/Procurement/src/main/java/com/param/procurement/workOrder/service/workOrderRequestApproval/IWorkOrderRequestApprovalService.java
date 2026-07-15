package com.param.procurement.workOrder.service.workOrderRequestApproval;

import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequest;

public interface IWorkOrderRequestApprovalService {

	WorkOrderDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWorkOrderList(WorkOrderRequest workOrderRequest);

	Object getWorkOrderRequest(Integer id, Integer unitId);

	Object approveWorkOrderRequest(WorkOrderRequest workOrderRequest);

	Object cancelworkOrderRequest(WorkOrderRequest workOrderRequest);

}
