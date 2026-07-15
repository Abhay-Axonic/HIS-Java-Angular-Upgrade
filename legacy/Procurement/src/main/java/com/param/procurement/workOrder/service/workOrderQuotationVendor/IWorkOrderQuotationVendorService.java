package com.param.procurement.workOrder.service.workOrderQuotationVendor;

import java.util.List;

import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;

public interface IWorkOrderQuotationVendorService {

	WorkOrderDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWorkOrderQuotationVendorList(WorkOrderQVRequest workOrderQVRequest);

	Object getWorkOrderQuotationVendorListNew(WorkOrderQVRequest workOrderQVRequest);

	Object saveWoQuotationVendor(WorkOrderQVRequest workOrderQVRequest);

	Object saveWoQuotationVendorNew(WorkOrderQVRequest workOrderQVRequest);

	Object updateWoQuotationVendor(WorkOrderQVRequest workOrderQVRequest);

	Object getWoQuotationVendor(Integer id);

	Object approveWoQuotationVendor(WorkOrderQVRequest workOrderQVRequest);

	Object cancelWoQuotationVendor(WorkOrderQVRequest workOrderQVRequest);

	Object getWorkOrderQRPopSearchList(WorkOrderQR workOrderQuotationReq);

	Object getWoQuotationRequestDetails(List<WorkOrderQR> workOrderQuotationReqList);

}
