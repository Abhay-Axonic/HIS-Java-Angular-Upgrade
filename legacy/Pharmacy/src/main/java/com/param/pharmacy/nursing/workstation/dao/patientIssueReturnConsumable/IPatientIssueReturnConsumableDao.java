package com.param.pharmacy.nursing.workstation.dao.patientIssueReturnConsumable;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

public interface IPatientIssueReturnConsumableDao {

	List<PatientIssueReturn> patientIssueReturnSearch(PatientIssueReturnRequest patientIssueReturnRequest, Integer screenId);
	
	List<Issue> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	List<PatientAgainstItemResponse> patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest);

	Integer patientIssueReturnDao(Integer batchMapperId);
	
}