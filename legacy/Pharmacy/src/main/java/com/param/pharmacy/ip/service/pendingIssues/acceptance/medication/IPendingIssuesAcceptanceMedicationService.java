package com.param.pharmacy.ip.service.pendingIssues.acceptance.medication;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.IssueAcceptanceMadicationDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.MedicationHandoverSaveRequest;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceDetails;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.nursing.discharge.dto.MedicationHandoverResponse;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationResponse;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;

public interface IPendingIssuesAcceptanceMedicationService {
	
	IssueAcceptanceMadicationDropdown getDropdownList(Integer unitId);
	
	List<PendingIssuesAcceptanceSearchResult> pendingIssuesAcceptanceMedicationSearch(PendingIssuesAcceptanceSearch pendingIssuesAcceptanceSearch);

	PendingIssuesAcceptanceDetails pendingIssuesAcceptanceMedicationDetails(Integer id);
	
	Object pendingIssuesAcceptanceMedicationSave(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave);

	List<MedicationReconciliationSearchDto> medicationHandoverSearch(MedicationReconciliationSearchRequest medicationReconciliationSearchRequest, Integer admissionId);

	MedicationHandoverResponse medicationHandoverDetail(Integer admissionId);

	Object medicationHandoverDetail(MedicationHandoverSaveRequest medicationHandoverSaveRequest);
}