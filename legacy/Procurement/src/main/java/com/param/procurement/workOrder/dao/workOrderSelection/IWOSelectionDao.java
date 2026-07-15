package com.param.procurement.workOrder.dao.workOrderSelection;

import java.util.List;

import com.param.entity.model.procurement.WoQuotationSelection;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionRequest;

public interface IWOSelectionDao {

	List<WoQuotationSelection> searchWoQuotationSelectionList(WOSelectionRequest woSelection, Integer screenId);

	WoQuotationSelection getWoQuotationSelection(Integer id);

	WoQuotationSelection saveWoQuotationSelection(WoQuotationSelection woQuotationSelection);

	WoQuotationSelection updateWoQuotationSelection(WoQuotationSelection woQuotationSelection);

}
