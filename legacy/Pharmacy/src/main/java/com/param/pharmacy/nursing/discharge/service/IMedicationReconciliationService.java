package com.param.pharmacy.nursing.discharge.service;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationResponse;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSave;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;

public interface IMedicationReconciliationService {

	MedicationReconciliationDto getDropdownList(Integer unitId);

	List<MedicationReconciliationSearchDto> medicationReconciliationSearch(MedicationReconciliationSearchRequest request);

	CommonResponse medicationReconciliationSave(MedicationReconciliationSave request);

	MedicationReconciliationResponse medicationReconciliationDetails(Long id, Integer unitId);
}
