package com.param.pharmacy.nursing.pharmacy.dao.patientIndent;

import java.util.List;

import com.param.entity.model.pharmacy.PatientIndent;
import com.param.entity.model.pharmacy.PatientIndentDetail;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentRequest;

public interface IPatientIndentDao {

	List<PatientIndent> patientIndentSearch(PatientIndentRequest patientIndentRequest, Integer screenId);
	
	PatientIndent patientIndentById(Integer id, Integer unitId);
	
	List<PatientIndentDetail> patientIndentDetailById(Integer id, Integer unitId);
	
	PatientIndent savePatientIndent(PatientIndent patientIndent);
	
	PatientIndent updatePatientIndent(PatientIndent patientIndent);

	List<PatientIndentDetail> patientPreviousIndentDetails(PatientIndentRequest patientIndentRequest);

	List<PatientIndent> patientPreviousIndent(PatientIndentRequest patientIndentRequest);
}
