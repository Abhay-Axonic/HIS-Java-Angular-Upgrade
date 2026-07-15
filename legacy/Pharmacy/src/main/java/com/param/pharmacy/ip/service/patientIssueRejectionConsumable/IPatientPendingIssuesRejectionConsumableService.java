package com.param.pharmacy.ip.service.patientIssueRejectionConsumable;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearchResult;
import com.param.pharmacy.ip.dto.patientIssueRejectionConsumable.PatientIssueRejectionConsumableSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;

public interface IPatientPendingIssuesRejectionConsumableService {
	
	CommonDropdown getDropdownList(Integer unitId);
	
	List<PatientIssueRejectionSearchResult> patientIssuesRejectionConsumableSearch(PatientIssueRejectionConsumableSearch patientIssueRejectionSearch);

	PatientIssueRejectionSearchResult patientIssuesRejectionConsumableDetails(Integer id);
	
	Object patientIssuesRejectionMedicationSave(PendingIssuesAcceptanceSave patientIssueRejectionSave);
}