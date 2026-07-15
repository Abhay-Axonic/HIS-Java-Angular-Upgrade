package com.param.global.dto;

import java.util.List;

public class PatientVisitsDto {

	private Integer patientId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String fromDate;

	private String toDate;

	private Integer encounterId;
	
	private String encounterDate;
	
	private String encounterNumber;
	
	private String specialityName;
	
	private Integer specialityId;
	
	private Integer doctorId;
	
	private String doctorName;
	
	private Integer admissionId;
	
	private String admissionDate;
	private String dischargeDate;

	private Integer dischargeSummaryId;
	
	private Integer visitTypeId;
	private Integer proReqId;

	private Integer templateFormatId;
	private Integer templateId;
	private String title;
	private Integer subSpecialityId;
	private String templateObject;
	private String template;

	private Integer templateTypeId;
	private String visitTypeName;
	private String visitDate;
	private String encounterDateString;
	private String encounterDateString2;
	private Integer patientClassId;
	private String createdDate;
	private Integer deptId;
	private Integer userDefinedVisitTypeId;
	private Integer templateTransactionId;
	private String preferedDateTime;
	private String serviceStandardName;
	private Character isHtml;
	public String getPreferedDateTime() {
		return preferedDateTime;
	}

	public void setPreferedDateTime(String preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	private List<PatientVisitsDto> admissionList;
	
	private List<PatientVisitsDto> encounterList;
	
	private List<PatientVisitsDto> listForAllPastvisits;
	private List<PatientVisitsDto> listForDailyProgress;
	private List<PatientVisitsDto> listForProcedureNote;
	private List<PatientVisitsDto> listForDischargeSummary;
	private List<PatientVisitsDto> listForCertificates;
	
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}


	public List<PatientVisitsDto> getAdmissionList() {
		return admissionList;
	}

	public void setAdmissionList(List<PatientVisitsDto> admissionList) {
		this.admissionList = admissionList;
	}

	public List<PatientVisitsDto> getEncounterList() {
		return encounterList;
	}

	public void setEncounterList(List<PatientVisitsDto> encounterList) {
		this.encounterList = encounterList;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getDischargeSummaryId() {
		return dischargeSummaryId;
	}

	public void setDischargeSummaryId(Integer dischargeSummaryId) {
		this.dischargeSummaryId = dischargeSummaryId;
	}
	
	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(String dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public List<PatientVisitsDto> getListForDailyProgress() {
		return listForDailyProgress;
	}

	public void setListForDailyProgress(List<PatientVisitsDto> listForDailyProgress) {
		this.listForDailyProgress = listForDailyProgress;
	}

	public List<PatientVisitsDto> getListForProcedureNote() {
		return listForProcedureNote;
	}

	public void setListForProcedureNote(List<PatientVisitsDto> listForProcedureNote) {
		this.listForProcedureNote = listForProcedureNote;
	}

	public List<PatientVisitsDto> getListForDischargeSummary() {
		return listForDischargeSummary;
	}

	public void setListForDischargeSummary(List<PatientVisitsDto> listForDischargeSummary) {
		this.listForDischargeSummary = listForDischargeSummary;
	}

	public List<PatientVisitsDto> getListForCertificates() {
		return listForCertificates;
	}

	public void setListForCertificates(List<PatientVisitsDto> listForCertificates) {
		this.listForCertificates = listForCertificates;
	}

	public List<PatientVisitsDto> getListForAllPastvisits() {
		return listForAllPastvisits;
	}

	public void setListForAllPastvisits(List<PatientVisitsDto> listForAllPastvisits) {
		this.listForAllPastvisits = listForAllPastvisits;
	}

	public Integer getTemplateFormatId() {
		return templateFormatId;
	}

	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getEncounterDateString() {
		return encounterDateString;
	}

	public void setEncounterDateString(String encounterDateString) {
		this.encounterDateString = encounterDateString;
	}

	public String getEncounterDateString2() {
		return encounterDateString2;
	}

	public void setEncounterDateString2(String encounterDateString2) {
		this.encounterDateString2 = encounterDateString2;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getUserDefinedVisitTypeId() {
		return userDefinedVisitTypeId;
	}

	public void setUserDefinedVisitTypeId(Integer userDefinedVisitTypeId) {
		this.userDefinedVisitTypeId = userDefinedVisitTypeId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Character getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(Character isHtml) {
		this.isHtml = isHtml;
	}

}
