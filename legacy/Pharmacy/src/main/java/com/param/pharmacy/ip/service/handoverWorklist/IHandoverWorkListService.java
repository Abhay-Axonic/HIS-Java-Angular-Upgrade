package com.param.pharmacy.ip.service.handoverWorklist;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.UpdateReceiver;

public interface IHandoverWorkListService {
	
	CommonDropdown getDropdownList();
	
	Object handoverWorkListSave(UpdateReceiver updateReceiver);

	List<PharmacologyValidationSearchResult> handoverWorkListSearchBP(PharmacologyValidationSearch pharmacologyValidationSearch);

	HandoverDetails handoverWorkListDetailsBP(Long id, Integer unitId);
}