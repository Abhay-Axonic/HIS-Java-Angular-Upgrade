package com.param.pharmacy.op.service;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyUpdate;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

public interface IPharmacologyValidationServiceOP {

	CommonDropdown getDropdownList();

	List<PharmacologyValidationSearchResult> pharmacologyValidationSearch(PharmacologyValidationSearch pharmacologyValidationSearch);

	Object pharmacologyValidationSave(PharmacologyUpdate pharmacologyUpdate);

	PharmacologyValidationDetails pharmacologyValidationDetails(Long id);
}
