package com.param.pharmacy.ip.service.assignPicker;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.global.service.items.CustomException;
import com.param.pharmacy.ip.dto.assignPicker.AssignPickerSave;
import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

public interface IAssignPickerService {
	
	CommonDropdown getDropdownList();
	
	List<PharmacologyValidationSearchResult> assignPickerSearch(PharmacologyValidationSearch pharmacologyValidationSearch);

	//PharmacologyValidationDetails assignPickerDetails(Integer id) throws Exception;
	
	List<PharmacologyItem> getSubstitute(Integer id, Integer itemId, Boolean isNarcotic, Integer storeId, Integer unitId, Integer formulationTypeId) throws Exception;
	
	Object assignPickerSave(AssignPickerSave assignPickerSave, boolean b) throws CustomException;
	
	List<PharmacologyValidationSearchResult> assignPickerSearchBP(PharmacologyValidationSearch pharmacologyValidationSearch);

	PharmacologyValidationDetails assignPickerDetailsBP(Integer id, Integer unitId, Integer orgId, Integer storeId, Integer userId) throws Exception;

	List<PharmacologyItem> getSubstituteBP(Integer id, Integer itemId) throws Exception;

	void cancelAssignPicker(CancelRequest cancelRequest) throws CustomException;
}