package com.param.pharmacy.nursing.workstation.service.patientIssueReturnConsumable;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstIssueResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnResponse;

public interface IPatientIssueReturnConsumableService {
	
	CommonDropdown getDropdownList(Integer unitId);
	
	List<PatientIssueReturnResponse> patientIssueReturnSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	List<PatientAgainstIssueResponse> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	Object patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
}