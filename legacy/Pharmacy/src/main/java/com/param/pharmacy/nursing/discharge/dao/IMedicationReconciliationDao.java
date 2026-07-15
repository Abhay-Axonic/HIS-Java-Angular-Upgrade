package com.param.pharmacy.nursing.discharge.dao;

import java.util.List;

import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;

public interface IMedicationReconciliationDao {

	List<PrescriptionDetail> medicationReconciliationSearch(MedicationReconciliationSearchRequest request, Integer screenId);

	Prescription medicationReconciliationSave(Prescription prescription);

}
