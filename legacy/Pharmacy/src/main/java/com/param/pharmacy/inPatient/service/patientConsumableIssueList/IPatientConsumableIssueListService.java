package com.param.pharmacy.inPatient.service.patientConsumableIssueList;

import java.util.List;

import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueListResponse;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientConsumableIssueListDetails;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientConsumableIssueListRequest;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsRequestForDrugRecall;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsResponseForDrugRecall;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;

public interface IPatientConsumableIssueListService {
	
	PatientIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	List<PatientConsumableIssueListResponse> patientConsumableIssueSearch(PatientConsumableIssueListRequest request);

	List<PatientDetailsResponseForDrugRecall> patientConsumableIssueSearchForDrugRecall(PatientDetailsRequestForDrugRecall request);
	
	PatientConsumableIssueListDetails patientConsumableIssueDetails(Integer id, Integer unitId) throws Exception;
	
}