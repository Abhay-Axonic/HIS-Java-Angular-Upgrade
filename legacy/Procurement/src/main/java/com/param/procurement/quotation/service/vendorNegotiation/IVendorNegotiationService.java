package com.param.procurement.quotation.service.vendorNegotiation;

import com.param.entity.model.procurement.VendorSelection;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationDropDown;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationRequest;

public interface IVendorNegotiationService {

	VendorNegotiationDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getVendorNegotiationList(VendorNegotiationRequest vendorNegotiationRequest);

	Object updateVendorNegotiation(VendorNegotiationRequest vendorNegotiationRequest);

	Object getVendorNegotiation(Integer id);

	Object cancelVendorNegotiation(VendorNegotiationRequest vendorNegotiationRequest);

	Object saveVendorSelection(VendorNegotiationRequest vendorNegotiationRequest);
	
	Object saveVendorSelectionForNewVendor(VendorNegotiationRequest vendorNegotiationRequest,VendorSelection vendorSelection);


}
