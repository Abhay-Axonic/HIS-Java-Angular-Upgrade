package com.param.procurement.consignment.service.implantIndentAcceptance;

import com.param.procurement.consignment.dto.implantIndent.ImplantIndentDropdown;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;

public interface IImplantIndentAcceptanceService {
	
	ImplantIndentDropdown getDropdownList(Integer unitId, Integer orgId);

	Object getImplantIndentAcceptanceList(ImplantIndentRequest request);

	Object getImplantIndentAcceptanceDetails(Integer id, Integer unitId);

	Object updateImplantIndentAcceptance(ImplantIndentRequest request);

}
