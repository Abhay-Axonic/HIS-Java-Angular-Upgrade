package com.param.procurement.workOrder.dao.workOrderQuotationRequest;

import java.util.List;

import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.entity.model.procurement.WorkOrderRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;

public interface IWorkOrderQRDao {

	List<WorkOrderQuotationRequest> searchWorkOrderQRList(WorkOrderQR workOrderQuotationReq, Integer screenId);

	WorkOrderQuotationRequest getWorkOrderQuotationRequest(Integer id);

	WorkOrderQuotationRequest saveWorkOrderQuotationRequest(WorkOrderQuotationRequest workOrderQR);

	WorkOrderQuotationRequest updateWorkOrderQuotationRequest(WorkOrderQuotationRequest workOrderQR);

	List<WorkOrderQuotationRequest> getWorkOrderQuotationRequestSearchList(WorkOrderQR workOrderQuotationReq,
			List<Integer> statusIds);

	List<WorkOrderRequest> getWorkOrderList(WorkOrderQR workOrderQuotationReq, List<Integer> statusIds);

	com.param.entity.model.procurement.WorkOrderRequest getWorkOrder(Integer id, Integer unitId);

	List<Integer> getVenderListOFQuotationReuest(Integer id);

}
