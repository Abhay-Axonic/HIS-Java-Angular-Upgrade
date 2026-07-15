package com.param.procurement.consignment.service.implantReturnAcceptance;

import java.util.List;

import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceDropdown;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceRequest;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceResponse;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceSave;

public interface IImplantReturnAcceptanceService
{
	
	ImplantReturnAcceptanceDropdown getDropdownList(Integer unitId);
	
	List<ImplantReturnAcceptanceResponse> consignmentReturnAcceptanceSearch(ImplantReturnAcceptanceRequest implantReturnAcceptanceRequest);
	
	List<ImplantReturnAcceptanceResponse> implantReturnAcceptanceSearch(ImplantReturnAcceptanceRequest implantReturnAcceptanceRequest);

	Object implantReturnAcceptanceConsignmentSave(ImplantReturnAcceptanceSave implantReturnAcceptanceSave);

	ImplantReturnAcceptanceResponse implantReturnAcceptanceDetails(Integer id, Integer unitId);

	
	
}
