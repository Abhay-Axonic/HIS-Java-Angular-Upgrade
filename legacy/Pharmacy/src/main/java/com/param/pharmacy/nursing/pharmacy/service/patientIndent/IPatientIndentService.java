package com.param.pharmacy.nursing.pharmacy.service.patientIndent;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDetails;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentList;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentRequest;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentSave;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

public interface IPatientIndentService {

	PatientIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);

	List<PatientIndentList> patientIndentSearch(PatientIndentRequest patientIndentRequest);

	PatientIndentDetails patientIndentDetails(Integer id, Integer unitId);

	Object patientIndentSave(PatientIndentSave patientIndentSave);

	Object updatePatientIndent(PatientIndentRequest patientIndentRequest);

	Object approvePatientIndent(PatientIndentRequest patientIndentRequest);

	Object cancelPatientIndent(PatientIndentRequest patientIndentRequest);

	List<PatientIndentDetails>  patientPreviousIndentDetails(PatientIndentRequest patientIndentRequest);
}