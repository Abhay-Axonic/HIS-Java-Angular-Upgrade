package com.param.opd.coversheet.dto;

import java.util.List;

public class MedicalHistoryDto {

	private List<DiagnosisPatientAppointmentMapperDto> resolveDiagnosisList;
	
	private List<PatientMedicalHistoryDto> medicalHistoryList;

	public List<DiagnosisPatientAppointmentMapperDto> getResolveDiagnosisList() {
		return resolveDiagnosisList;
	}

	public void setResolveDiagnosisList(List<DiagnosisPatientAppointmentMapperDto> resolveDiagnosisList) {
		this.resolveDiagnosisList = resolveDiagnosisList;
	}

	public List<PatientMedicalHistoryDto> getMedicalHistoryList() {
		return medicalHistoryList;
	}

	public void setMedicalHistoryList(List<PatientMedicalHistoryDto> medicalHistoryList) {
		this.medicalHistoryList = medicalHistoryList;
	}
}
