package com.param.procurement.workOrder.dao.workOrderCreation;

import java.util.List;

import com.param.entity.model.procurement.WorkOrderCreation;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationRequest;

public interface IWorkOrderCreationDao {

	List<WorkOrderCreation> searchWorkOrderCreationList(WorkOrderCreationRequest woCreationRequest, Integer screenId);

	WorkOrderCreation getWorkOrderCreation(Integer id);

	WorkOrderCreation saveWorkOrderCreation(WorkOrderCreation woCreation);

	WorkOrderCreation updateWorkOrderCreation(WorkOrderCreation woCreation);

}
