package com.param.opd.coversheet.dto;

import java.util.List;

public class D2NPatientListDto extends D2NMedicationDto{

	private Integer patientId;
	
	private String patientName;
	
	private Integer admissionId;
	
	private List<D2NMedicationDto> medicationDetails;

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public List<D2NMedicationDto> getMedicationDetails() {
		return medicationDetails;
	}

	public void setMedicationDetails(List<D2NMedicationDto> medicationDetails) {
		this.medicationDetails = medicationDetails;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	
	
}
