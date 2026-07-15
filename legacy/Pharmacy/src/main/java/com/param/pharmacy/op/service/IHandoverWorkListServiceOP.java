package com.param.pharmacy.op.service;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.UpdateReceiver;

public interface IHandoverWorkListServiceOP {
	
	CommonDropdown getDropdownList();
	
	List<PharmacologyValidationSearchResult> handoverWorkListSearch(PharmacologyValidationSearch pharmacologyValidationSearch);

	HandoverDetails handoverWorkListDetails(Long id);
	
	Object handoverWorkListSave(UpdateReceiver updateReceiver);

	/*List<PharmacologyValidationSearchResult> handoverWorkListSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch);
	
	HandoverDetails handoverWorkListDetailsBP(Long id);*/
}