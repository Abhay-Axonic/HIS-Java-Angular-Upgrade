package com.param.opd.coversheet.dto;


import java.util.List;


public class DiagnosisMapperDto {
	private Integer diagnosisMapperId;

	private Integer diagnosisId;
	
	private Character isNotifiable;
	
	private Character isCommunicable;
	
	private Character isNonCommunicable;
	
	private Character isMedicoLegal;

	private Integer genderId;

	private Integer unitId;

	private Integer organizationId;

	private Character status;

	private Integer createdBy;
	private Integer oldDiagnosisId;

	private String createdDate;
	private Character isDeleted;
	private Integer updatedBy;
	private String code;
	private Integer id;
	private String genderDesc;
	private Integer offset;
	
	private Integer noOfRecordsPerPage;

	private String updatedDate;
	private String diagnosisName;
	private String diagnosisCode;
	private List<DiagnosisMapperDto> listOfGendersDto;
	private List<DiagnosisMapperDto> listOfDtos;

	public Integer getDiagnosisMapperId() {
		return diagnosisMapperId;
	}

	public void setDiagnosisMapperId(Integer diagnosisMapperId) {
		this.diagnosisMapperId = diagnosisMapperId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Character getIsNotifiable() {
		return isNotifiable;
	}

	public void setIsNotifiable(Character isNotifiable) {
		this.isNotifiable = isNotifiable;
	}

	public Character getIsCommunicable() {
		return isCommunicable;
	}

	public void setIsCommunicable(Character isCommunicable) {
		this.isCommunicable = isCommunicable;
	}

	public Character getIsNonCommunicable() {
		return isNonCommunicable;
	}

	public void setIsNonCommunicable(Character isNonCommunicable) {
		this.isNonCommunicable = isNonCommunicable;
	}

	public Character getIsMedicoLegal() {
		return isMedicoLegal;
	}

	public void setIsMedicoLegal(Character isMedicoLegal) {
		this.isMedicoLegal = isMedicoLegal;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
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

	public String getGenderDesc() {
		return genderDesc;
	}

	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getOldDiagnosisId() {
		return oldDiagnosisId;
	}

	public void setOldDiagnosisId(Integer oldDiagnosisId) {
		this.oldDiagnosisId = oldDiagnosisId;
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
	
	
}
