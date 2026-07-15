package com.param.procurement.workOrder.dao.workOrderCreation;

import java.util.List;

import com.param.entity.model.procurement.WorkOrderCreation;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.entity.model.procurement.WorkOrderRequest;
import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationRequest;

public interface IWOCreationDao {

	List<WorkOrderCreation> searchWorkOrderCreationList(WOCreationRequest woCreationRequest, Integer screenId);
	
	List<WorkOrderQuotationRequest> searchWorkOrderCreationListByQR(WOCreationRequest woCreationRequest, Integer screenId,Integer woAgainstQR);
	
	List<WorkOrderRequest> searchWorkOrderCreationListByWR(WOCreationRequest woCreationRequest, Integer screenId,Integer woAgainstQR);
	
	WorkOrderCreation saveWorkOrderCreation(WorkOrderCreation woCreation);
	
	WorkOrderCreation getWorkOrderCreation(Integer id);
	
	WorkOrderCreation updateWorkOrderCreation(WorkOrderCreation woCreation);

	WorkOrderQuotationRequest searchWorkOrderCreationQRById(WOCreationRequest woCreationRequest);

	WorkOrderRequest searchWorkOrderCreationWRId(WOCreationRequest woCreationRequest);

	

}
