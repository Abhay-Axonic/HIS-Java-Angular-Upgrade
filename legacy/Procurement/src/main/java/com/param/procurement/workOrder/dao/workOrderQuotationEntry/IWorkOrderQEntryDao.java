package com.param.procurement.workOrder.dao.workOrderQuotationEntry;

import java.util.List;

import com.param.entity.model.procurement.WoQuotationEntry;
import com.param.entity.model.procurement.WoQuotationEntryDetail;
import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WoQuotationVendorDetail;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuoatationEntryRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuotationEntryDetailResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;

public interface IWorkOrderQEntryDao {

	List<WoQuotationEntry> searchWorkOrderQEntryList(WoQuoatationEntryRequest woQEntryRequest, Integer screenId);

	WoQuotationEntry getWoQuotationEntry(Integer id);

	WoQuotationEntry saveWoQuotationEntry(WoQuotationEntry woQuotationEntry);

	WoQuotationEntry updateWoQuotationEntry(WoQuotationEntry woQuotationEntry);

	List<WoQuotationVendor> getWoQuotationVendorSearchList(WorkOrderQVRequest workOrderQuotationVendor,
			List<Integer> statusIds);

	WoQuotationVendor getWoQuotationVendor(Integer id);

	List<WoQuotationVendorDetail> getWoQuotationVendorDetails(List<Integer> ids);

	WorkOrderQuotationRequest updateWoQuotationRequest(WorkOrderQuotationRequest WorkOrderQR);

	WoQuotationEntryDetailResponse getWouotationNegoAmount(Integer negoId, Integer venId, Integer detailId);

}
