package com.param.procurement.workOrder.dao.workOrderRequest;

import java.util.List;

import com.param.entity.model.procurement.ContractType;
import com.param.entity.model.procurement.WorkOrderRequest;

public interface IWorkOrderRequestDao {

	List<WorkOrderRequest> searchWorkOrderList(
			com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequest workOrderRequest, Integer screenId);

	WorkOrderRequest saveWorkOrderRequest(WorkOrderRequest workOrderRequest);

	WorkOrderRequest updateWorkOrderRequest(WorkOrderRequest workOrderRequest);

	WorkOrderRequest getWorkOrderRequest(Integer id);

}
