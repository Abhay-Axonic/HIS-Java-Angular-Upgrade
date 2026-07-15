package com.param.pharmacy.op.dao;

import java.util.List;

import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;

public interface IPharmacologyValidationOPDao {

	List<Prescription> pharmacologyValidationSearchOP(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);

	Prescription getPrescriptionById(Long id);

	List<PrescriptionDetail> pharmacologyValidationDetails(Long id);

	List<PrescriptionFrequencyDetail> getPrescriptionFrequencyDetails(Long id);

	Prescription updatePrescription(Prescription prescription);

	AssignPicker saveAssignPicker(AssignPicker assignPickerObject);

	Issue saveIssue(Issue issue);

	List<Prescription> pharmacologyValidationSearchOP(PharmacologyValidationSearch pharmacologyValidationSearch,
			Integer screenId, List<Integer> deptIds);

}
