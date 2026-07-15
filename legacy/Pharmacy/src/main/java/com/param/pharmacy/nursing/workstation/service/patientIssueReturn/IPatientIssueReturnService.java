package com.param.pharmacy.nursing.workstation.service.patientIssueReturn;

import java.util.List;

import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstIssueResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnAddItem;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnAddItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnDetails;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnDropdown;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnSave;

public interface IPatientIssueReturnService {
	
	PatientIssueReturnDropdown getDropdownList(Integer unitId);
	
	List<PatientIssueReturnResponse> patientIssueReturnSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	PatientIssueReturnDetails patientIssueReturnById(Integer id, Integer unitId);
	
	List<PatientAgainstIssueResponse> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	Object patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	PatientIssueReturnAddItemResponse patientIssueReturnAddNew(PatientIssueReturnAddItem patientIssueReturnAddItem);
	
	Object patientIssueReturnSave(PatientIssueReturnSave patientIssueReturnSave);
	
}