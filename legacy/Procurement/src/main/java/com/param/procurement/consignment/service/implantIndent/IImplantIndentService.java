package com.param.procurement.consignment.service.implantIndent;

import java.util.List;

import com.param.procurement.consignment.dto.implantIndent.ImplantIndentDropdown;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentResponse;

public interface IImplantIndentService {

	ImplantIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	List<ImplantIndentResponse> searchImplantIndent(ImplantIndentRequest request);

	ImplantIndentResponse implantIndentDetails(Integer id, Integer storeId, Integer unitId);

	Object saveImplantIndent(ImplantIndentRequest request);

	Object updateImplantIndent(ImplantIndentRequest request);

	Object approveImplantIndent(ImplantIndentRequest request);

	Object cancelImplantIndent(ImplantIndentRequest request);

}
