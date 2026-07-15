package com.param.procurement.workOrder.dao.workOrderNegotiation;

import java.util.List;

import com.param.entity.model.procurement.WoQuotationNegotiation;
import com.param.entity.model.procurement.WoQuotationSelection;
import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuoatationEntryRequest;

public interface IWorkOrderNegotiationDao {

	WoQuotationNegotiation getWoQuotationNegotiation(Integer id);

	WoQuotationNegotiation saveWoQuotationNegotiation(WoQuotationNegotiation woQuotationNegotiation);

	WoQuotationNegotiation updateWoQuotationNegotiation(WoQuotationNegotiation woQuotationNegotiation);

	List<WoQuotationNegotiation> searchWorkOrderNegotiationList(WONegotiationRequest woQNego, Integer screenId);

	List<WoQuotationVendor> getWoQuotationVendor(Integer id);

	WorkOrderQuotationRequest getWoWorkOrderQuotationRequest(Integer id);

	WoQuotationSelection saveWoQuotationSelection(WoQuotationSelection woQuotationSelection);

}
