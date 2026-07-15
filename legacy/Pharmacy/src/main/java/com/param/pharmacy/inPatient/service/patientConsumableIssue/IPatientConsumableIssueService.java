package com.param.pharmacy.inPatient.service.patientConsumableIssue;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.global.pharmacy.dto.PatientConsumableIssueSave;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueDetails;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueRequest;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueResponse;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;

public interface IPatientConsumableIssueService {
	
	PatientIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	List<PatientConsumableIssueResponse> patientConsumableIssueSearch(PatientConsumableIssueRequest request);

	List<PatientConsumableIssueResponse> patientConsumableIssueSearchForPatientIssueAcceptance(PatientConsumableIssueRequest request);
	
	PatientConsumableIssueDetails patientConsumableIssueDetails(Integer id, Integer screenFlag, Integer storeId, Integer unitId) throws Exception;
	
	Object patientConsumableIssueSave(PatientConsumableIssueSave patientConsumableIssueSave) throws Exception;

	PatientConsumableIssueDetails patientConsumableIssueAcceptanceDetails(Integer id, Integer screenFlag, Integer storeId, Integer unitId);

	//Object savePatientIssueAcceptance(Issue issue, PatientConsumableIssueSave patientConsumableIssueSave);
}