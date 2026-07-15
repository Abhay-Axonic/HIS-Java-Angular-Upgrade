package com.param.opd.coversheet.dto;

import java.math.BigInteger;
import java.util.List;

public class DiagnosisPatientAppointmentDetailsDto {
	private Integer diagnosisId;
	private Integer type;
	private Integer diagnosisType;
	private String remark;
	private String diagnosisName;
	private List<Integer> selectedOrderSet;
	private Character isDeleted;
	private Integer patientDiagnosisId;
	private List<DiagnosisPatientAppointmentDetailsDto> listDiagnosisPatientAppointmentDetailsDto;
	private Character isResolved;
	private String resolvedDate;
	private Integer appointmentId;
	private Integer resolvedDays;
	private String resolvedRemark;
	private String userName;
	private String diagnosisText;
	private Boolean editDiag;

	private Integer patientId;

	private Character isWhileAdmission;

	private Integer unitId;

	private Integer organizationId;

	private char status;

	private Integer created_by;

	private String created_date;

	private String created_date1;

	private Integer updated_by;

	private String updated_date;

	private String created_date_new;

	private Character isNotifiable;

	private Character isComorbidity;

	private String code;
	private String searchKeywordText;

	
	private Integer encounterId;
	
	private Integer admissionId;
	private String createdTime;

	private String justificationCancel;
	
	private Character isDiagnosisCancel;

	private String doctorName;
	private String fromDate;
	private String toDate;
	private Integer templateTransactionId;
	private String updatedTime;
	private Character isDiagnosisName;
	private String diagnosisCode;
	private String diagnosisNameText;
	private Integer createdBy;

	private Integer covTemplateId;

    private BigInteger count;


	private Long diagnosisTempCount;
	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public List<Integer> getSelectedOrderSet() {
		return selectedOrderSet;
	}

	public void setSelectedOrderSet(List<Integer> selectedOrderSet) {
		this.selectedOrderSet = selectedOrderSet;
	}

	public Integer getDiagnosisType() {
		return diagnosisType;
	}

	public void setDiagnosisType(Integer diagnosisType) {
		this.diagnosisType = diagnosisType;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getPatientDiagnosisId() {
		return patientDiagnosisId;
	}

	public void setPatientDiagnosisId(Integer patientDiagnosisId) {
		this.patientDiagnosisId = patientDiagnosisId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getCreated_date1() {
		return created_date1;
	}

	public void setCreated_date1(String created_date1) {
		this.created_date1 = created_date1;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public String getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}

	public Character getIsNotifiable() {
		return isNotifiable;
	}

	public void setIsNotifiable(Character isNotifiable) {
		this.isNotifiable = isNotifiable;
	}

	public Character getIsComorbidity() {
		return isComorbidity;
	}

	public void setIsComorbidity(Character isComorbidity) {
		this.isComorbidity = isComorbidity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getJustificationCancel() {
		return justificationCancel;
	}

	public void setJustificationCancel(String justificationCancel) {
		this.justificationCancel = justificationCancel;
	}

	public Character getIsDiagnosisCancel() {
		return isDiagnosisCancel;
	}

	public void setIsDiagnosisCancel(Character isDiagnosisCancel) {
		this.isDiagnosisCancel = isDiagnosisCancel;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getDiagnosisTempCount() {
		return diagnosisTempCount;
	}

	public void setDiagnosisTempCount(Long diagnosisTempCount) {
		this.diagnosisTempCount = diagnosisTempCount;
	}

	public List<DiagnosisPatientAppointmentDetailsDto> getListDiagnosisPatientAppointmentDetailsDto() {
		return listDiagnosisPatientAppointmentDetailsDto;
	}

	public void setListDiagnosisPatientAppointmentDetailsDto(
			List<DiagnosisPatientAppointmentDetailsDto> listDiagnosisPatientAppointmentDetailsDto) {
		this.listDiagnosisPatientAppointmentDetailsDto = listDiagnosisPatientAppointmentDetailsDto;
	}

	public Character getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(Character isResolved) {
		this.isResolved = isResolved;
	}

	public String getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public Integer getResolvedDays() {
		return resolvedDays;
	}

	public void setResolvedDays(Integer resolvedDays) {
		this.resolvedDays = resolvedDays;
	}

	public String getResolvedRemark() {
		return resolvedRemark;
	}

	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
	}

	public Character getIsDiagnosisName() {
		return isDiagnosisName;
	}

	public void setIsDiagnosisName(Character isDiagnosisName) {
		this.isDiagnosisName = isDiagnosisName;
	}

	public String getDiagnosisCode() {
		return diagnosisCode;
	}

	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}

	public String getDiagnosisNameText() {
		return diagnosisNameText;
	}

	public void setDiagnosisNameText(String diagnosisNameText) {
		this.diagnosisNameText = diagnosisNameText;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Character getIsWhileAdmission() {
		return isWhileAdmission;
	}

	public void setIsWhileAdmission(Character isWhileAdmission) {
		this.isWhileAdmission = isWhileAdmission;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	public Boolean getEditDiag() {
		return editDiag;
	}

	public void setEditDiag(Boolean editDiag) {
		this.editDiag = editDiag;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public String getCreated_date_new() {
		return created_date_new;
	}

	public void setCreated_date_new(String created_date_new) {
		this.created_date_new = created_date_new;
	}

	public String getDiagnosisText() {
		return diagnosisText;
	}

	public void setDiagnosisText(String diagnosisText) {
		this.diagnosisText = diagnosisText;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getSearchKeywordText() {
		return searchKeywordText;
	}

	public void setSearchKeywordText(String searchKeywordText) {
		this.searchKeywordText = searchKeywordText;
	}

	
}
