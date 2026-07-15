package com.param.procurement.workOrder.service.workOrderSelection;

import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionRequest;

public interface IWOSelectionService {

	WorkOrderDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getWoQuotationSelectionList(WOSelectionRequest woSelectionRequest);

	//Object saveWoQuotationSelection(WOSelectionRequest woSelectionRequest);

	Object getWoQuotationSelection(Integer id);

	Object updateWoQuotationSelection(WOSelectionRequest woSelectionRequest);

}
