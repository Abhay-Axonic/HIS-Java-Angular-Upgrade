package com.param.data.dto;

import java.util.List;

public class QuickSummeryDto {

	private List<CommonVitalsDto> triage;
	private List<CommonHistoryDto> history;
	private List<CommonPhysicalExaminationDto> physicalExamination;
	private List<CommonPrescriptionsDto> prescriptions;
	private List<CommonLaboratoryDto> loboratory;
	private List<CommonDiagnosisDto> diagnosis;
	private List<CommonProcedureDto> procedure;
	private List<CommonReviewNotesDto> reviewNotes;
	private List<CommonRadiologyDto> radiologyorders;
	private List<CommonProvisionDiagonosisDto> provisionalDiagnosys;

	public List<CommonVitalsDto> getTriage() {
		return triage;
	}

	public void setTriage(List<CommonVitalsDto> triage) {
		this.triage = triage;
	}

	public List<CommonHistoryDto> getHistory() {
		return history;
	}

	public void setHistory(List<CommonHistoryDto> history) {
		this.history = history;
	}

	public List<CommonPhysicalExaminationDto> getPhysicalExamination() {
		return physicalExamination;
	}

	public void setPhysicalExamination(List<CommonPhysicalExaminationDto> physicalExamination) {
		this.physicalExamination = physicalExamination;
	}

	public List<CommonPrescriptionsDto> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(List<CommonPrescriptionsDto> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public List<CommonLaboratoryDto> getLoboratory() {
		return loboratory;
	}

	public void setLoboratory(List<CommonLaboratoryDto> loboratory) {
		this.loboratory = loboratory;
	}

	public List<CommonDiagnosisDto> getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(List<CommonDiagnosisDto> diagnosis) {
		this.diagnosis = diagnosis;
	}

	public List<CommonProcedureDto> getProcedure() {
		return procedure;
	}

	public void setProcedure(List<CommonProcedureDto> procedure) {
		this.procedure = procedure;
	}

	public List<CommonReviewNotesDto> getReviewNotes() {
		return reviewNotes;
	}

	public void setReviewNotes(List<CommonReviewNotesDto> reviewNotes) {
		this.reviewNotes = reviewNotes;
	}


	public List<CommonProvisionDiagonosisDto> getProvisionalDiagnosys() {
		return provisionalDiagnosys;
	}

	public void setProvisionalDiagnosys(List<CommonProvisionDiagonosisDto> provisionalDiagnosys) {
		this.provisionalDiagnosys = provisionalDiagnosys;
	}

	public List<CommonRadiologyDto> getRadiologyorders() {
		return radiologyorders;
	}

	public void setRadiologyorders(List<CommonRadiologyDto> radiologyorders) {
		this.radiologyorders = radiologyorders;
	}

}
