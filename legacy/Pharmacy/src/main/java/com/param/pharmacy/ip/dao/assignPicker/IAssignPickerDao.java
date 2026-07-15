package com.param.pharmacy.ip.dao.assignPicker;

import java.util.List;

import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.AssignPickerDetail;
import com.param.entity.model.pharmacy.Issue;
import com.param.pharmacy.ip.dto.assignPicker.AssignPickerDetailItem;
import com.param.pharmacy.ip.dto.assignPicker.PrescriptionDetailsDto;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

import in.param.exception.ApplicationException;

public interface IAssignPickerDao {

	AssignPicker updateAssignPicker(AssignPicker assignPicker);
	
	Issue saveIssue(Issue issue);
	
	AssignPicker getAssignPicker(Integer id);
	
	List<AssignPicker> assignPickerSearch(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);
	
	List<AssignPickerDetail> assignPickerDetails(Integer id);

	List<PharmacologyValidationSearchResult> assignPickerSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId, Integer unitId);

	PharmacologyValidationDetails getAssignPickerBP(Integer id, Integer unitId);

	List<PharmacologyItem> assignPickerDetailsBP(Integer id);

	List<PharmacologyItem> getIssuedQuantity(List<Long> prescriptionDetailId);

	List<PharmacologyItem> getPendingIssueQuantity(List<Long> prescriptionDetailId);

	List<PharmacologyItem> assignPickerAddedItemsDetailsBP(Integer id);

	List<Issue> getIssuesOfPicker(Integer id);

	List<PharmacologyItem> getIssuedQuantity(List<Long> collect, List<Integer> list);

	PrescriptionDetailsDto getPrescriptionDtlsByPrescriptionId(Long id, Integer itemId);

	AssignPickerDetailItem getAssignPickerDetailItemByPrescriptionId(Integer id,
			Integer prescriptionDetailsid);

	AssignPicker updateassignPicker(AssignPicker assignPicker)throws ApplicationException;

	AssignPicker getAssignPickerList(Integer id)throws ApplicationException;

	List<PharmacologyItem> getIssuedQty(Long long1)throws ApplicationException;
}