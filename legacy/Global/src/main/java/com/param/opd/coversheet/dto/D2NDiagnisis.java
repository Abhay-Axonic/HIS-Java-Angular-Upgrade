package com.param.opd.coversheet.dto;

import java.util.List;

public class D2NDiagnisis extends D2NPatientListDto {

	private Integer diagnosisId;
	
	private String diagnosisName;
	
	private List<D2NPatientListDto> patientList;

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public List<D2NPatientListDto> getPatientList() {
		return patientList;
	}

	public void setPatientList(List<D2NPatientListDto> patientList) {
		this.patientList = patientList;
	}
	
}
