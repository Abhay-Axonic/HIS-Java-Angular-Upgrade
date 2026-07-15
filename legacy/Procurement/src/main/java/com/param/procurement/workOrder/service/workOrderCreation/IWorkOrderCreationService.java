package com.param.procurement.workOrder.service.workOrderCreation;

import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationDropDown;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationRequest;

public interface IWorkOrderCreationService {

	WorkOrderCreationDropDown getDropdowns();

	Object getWorkOrderCreationList(WorkOrderCreationRequest woCreationRequest);

	Object saveWorkOrderCreation(WorkOrderCreationRequest woCreationRequest);

	Object updateWorkOrderCreation(WorkOrderCreationRequest woCreationRequest);

	Object getworkOrderCreation(Integer id);

	Object approveWorkOrderCreation(WorkOrderCreationRequest woCreationRequest);

	Object cancelWorkOrderCreation(WorkOrderCreationRequest woCreationRequest);

}
