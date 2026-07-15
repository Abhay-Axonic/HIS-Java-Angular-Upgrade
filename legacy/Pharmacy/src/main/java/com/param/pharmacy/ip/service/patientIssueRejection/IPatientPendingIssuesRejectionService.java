package com.param.pharmacy.ip.service.patientIssueRejection;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearch;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearchResult;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;

public interface IPatientPendingIssuesRejectionService {
	
	CommonDropdown getDropdownList(Integer unitId);
	
	PatientIssueRejectionSearchResult patientIssuesRejectionMedicationDetails(Integer id);
	
	Object patientIssuesRejectionMedicationSave(PendingIssuesAcceptanceSave patientIssueRejectionSave);

	List<PatientIssueRejectionSearchResult> patientIssuesRejectionMedicationSearchPT(
			PatientIssueRejectionSearch patientIssueRejectionSearch);
}