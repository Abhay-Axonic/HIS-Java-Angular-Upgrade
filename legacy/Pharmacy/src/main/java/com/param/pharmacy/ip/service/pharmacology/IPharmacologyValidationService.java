package com.param.pharmacy.ip.service.pharmacology;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyUpdate;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

public interface IPharmacologyValidationService {
	
	CommonDropdown getDropdownList();
	
	Object pharmacologyValidationSave(PharmacologyUpdate pharmacologyUpdate);

	List<PharmacologyValidationSearchResult> pharmacologyValidationSearchBP(PharmacologyValidationSearch pharmacologyValidationSearch);

	PharmacologyValidationDetails pharmacologyValidationDetailsBP(Long id, Integer unitId);
}