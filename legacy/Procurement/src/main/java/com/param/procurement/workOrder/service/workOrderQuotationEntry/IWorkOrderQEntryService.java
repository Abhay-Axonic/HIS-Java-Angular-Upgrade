package com.param.procurement.workOrder.service.workOrderQuotationEntry;

import java.util.List;

import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuoatationEntryRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;

public interface IWorkOrderQEntryService {

	WorkOrderDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWoQuotationEntryList(WoQuoatationEntryRequest woQEntryRequest);

	Object saveWoQuotationEntry(WoQuoatationEntryRequest woQEntryRequest);

	Object updateWoQuotationEntry(WoQuoatationEntryRequest woQEntryRequest);

	Object getWoQuotationEntry(Integer id);

	Object approveWoQuotationEntry(WoQuoatationEntryRequest woQEntryRequest);

	Object cancelWoQuotationEntry(WoQuoatationEntryRequest woQEntryRequest);

	Object getWorkOrderQVendorPopSearchList(WorkOrderQVRequest workOrderQuotationVendor);

	Object getWoQuotationRequestDetails(List<WorkOrderQVRequest> woQuotationVendorList);

}
