package com.param.opd.coversheet.dto;

import java.util.List;

public class EmergencyDiagnosisMasterDto {
	private Integer emergencyDiagnosisMapperId;

	private Integer diagnosisId;
	
	private Integer unitId;

	private Integer organizationId;

	private Character status;

	private Integer createdBy;

	private String createdDate;
	private Integer updatedBy;
	private String code;
	private Integer id;
	private Integer offset;
	private Integer admissionId;
	
	private Integer noOfRecordsPerPage;

	private String updatedDate;
	private String diagnosisName;
	private String diagnosisCode;
	private List<DiagnosisMapperDto> listOfGendersDto;
	private List<DiagnosisMapperDto> listOfDtos;

	private Integer patientId;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer diagnisisId;
	
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Integer getDiagnisisId() {
		return diagnisisId;
	}

	public void setDiagnisisId(Integer diagnisisId) {
		this.diagnisisId = diagnisisId;
	}

	public Integer getEmergencyDiagnosisMapperId() {
		return emergencyDiagnosisMapperId;
	}

	public void setEmergencyDiagnosisMapperId(Integer emergencyDiagnosisMapperId) {
		this.emergencyDiagnosisMapperId = emergencyDiagnosisMapperId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public String getDiagnosisCode() {
		return diagnosisCode;
	}

	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}

	public List<DiagnosisMapperDto> getListOfGendersDto() {
		return listOfGendersDto;
	}

	public void setListOfGendersDto(List<DiagnosisMapperDto> listOfGendersDto) {
		this.listOfGendersDto = listOfGendersDto;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<DiagnosisMapperDto> getListOfDtos() {
		return listOfDtos;
	}

	public void setListOfDtos(List<DiagnosisMapperDto> listOfDtos) {
		this.listOfDtos = listOfDtos;
	}
	
	
	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	
	
}
