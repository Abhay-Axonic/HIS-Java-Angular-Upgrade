package com.param.procurement.workOrder.service.workOrderQuotationRequest;

import java.util.List;

import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;

public interface IWorkOrderQRService {

	WorkOrderDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWorkOrderQuotationRequestList(WorkOrderQR workOrderQuotationReq);

	Object saveWorkOrderQuotationRequest(WorkOrderQR workOrderQuotationReq);

	Object updateWorkOrderQuotationRequest(WorkOrderQR workOrderQuotationReq);

	Object getWorkOrderQuotationRequest(Integer id, Integer unitId);

	Object approveWOQuotationRequest(WorkOrderQR workOrderQuotationReq);
	
	Object approvalWOQuotationRequest(WorkOrderQR workOrderQuotationReq);

	Object cancelWOQuotationRequest(WorkOrderQR workOrderQuotationReq);

	Object getWorkOrderPopSearchList(WorkOrderQR workOrderQuotationReq);

	Object getWorkOrderDetails(List<WorkOrderQR> workOrderQuotationReqList, Integer unitId);

	

	

}
