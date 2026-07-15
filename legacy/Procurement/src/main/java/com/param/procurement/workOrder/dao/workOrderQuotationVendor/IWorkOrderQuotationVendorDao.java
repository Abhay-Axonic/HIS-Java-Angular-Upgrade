package com.param.procurement.workOrder.dao.workOrderQuotationVendor;

import java.util.List;

import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.entity.model.procurement.WorkOrderQuotationRequestDetail;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;

public interface IWorkOrderQuotationVendorDao {

	List<WoQuotationVendor> searchWorkOrderQVList(WorkOrderQVRequest workOrderQVRequest, Integer screenId);

	List<WoQuotationVendor> searchWorkOrderQVListNew(WorkOrderQVRequest workOrderQVRequest, Integer screenId);

	WoQuotationVendor getWoQuotationVendor(Integer id);

	WoQuotationVendor saveWoQuotationVendor(WoQuotationVendor workOrderQv);

	WoQuotationVendor updateWoQuotationVendor(WoQuotationVendor workOrderQv);

	WorkOrderQuotationRequest getWorkOrderQuotationRequest(Integer id);

	List<WorkOrderQuotationRequest> getWorkOrderQuotationRequestSearchList(WorkOrderQR workOrderQuotationReq,
			List<Integer> statusIds);

	List<WorkOrderQuotationRequest> getByVendorAndId(Integer vendorId, Integer id);

}
