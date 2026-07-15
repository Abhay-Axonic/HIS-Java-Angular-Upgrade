package com.param.procurement.quotation.service.vendorSelection;

import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionDropdown;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionRequest;

public interface IVendorSelectionService {

	VendorSelectionDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getVendorSelectionList(VendorSelectionRequest vendorSelectionRequest);

	Object updateVendorSelection(VendorSelectionRequest vendorSelectionRequest);

	Object getVendorSelection(Integer id);

	Object cancelVendorSelection(VendorSelectionRequest vendorSelectionRequest);

}
