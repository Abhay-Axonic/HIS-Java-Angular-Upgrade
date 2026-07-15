package com.param.pharmacy.ip.dao.pharmacology;

import java.util.List;

import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.PrescriptionFrequencyDetailResp;
import com.param.pharmacy.nursing.discharge.dto.BatchDetail;

public interface IPharmacologyValidationDao {
	
	List<Prescription> pharmacologyValidationSearch(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);
	
	List<PrescriptionDetail> pharmacologyValidationDetails(Long id);
	
	Prescription pharmacologyValidationSave(Prescription prescription);
	
	Prescription updatePrescription(Prescription prescription);
	
	AssignPicker saveAssignPicker(AssignPicker assignPicker);
	
	Issue saveIssue(Issue issue);
	
	List<PrescriptionFrequencyDetail> getPrescriptionFrequencyDetails(Long id);
	
	Prescription getPrescriptionById(Long id);

	List<PharmacologyValidationSearchResult> pharmacologyValidationSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);

	PharmacologyValidationDetails getPrescriptionByIdBP(Long id, Integer unitId);

	List<PharmacologyItem> pharmacologyValidationDetailsBP(Long id);

	List<PrescriptionFrequencyDetailResp> getPrescriptionFrequencyDetailsBP(List<Long> prescriptionDetailIds);

	List<PharmacologyItem> pharmacologyValidationDetailsBP(Integer id);

	Prescription getPrescriptionByAdmissionId(Integer admissionId);

	List<PrescriptionDetail> pharmacologyValidationDetailsByAdmissionId(Long admissionId);

	BatchDetail getBatchDetail(Long id);

	List<PrescriptionDetail> getPrescriptionDetailsList(Long prescriptionId);

	List<PrescriptionDetail> getPrescriptionFrequencyDetailsForWorklist(Long id);
	
}