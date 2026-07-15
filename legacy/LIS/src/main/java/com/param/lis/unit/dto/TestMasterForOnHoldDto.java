package com.param.lis.unit.dto;

import java.util.List;

import com.param.global.dto.CommonDto;

public class TestMasterForOnHoldDto {
	private Integer deptId;
	private Character isCentrifugationReq;
	private String testAlies;
	private String testCode;
	private String testDesc;
	private Integer testType;
	private String techniqueName;
	private Integer serologyResultTestMpprId;
	private String parameterName;
	private Integer sampleId;
	private Integer techniqueId;
	private Integer testId;
	private Character isHistoCyto;
	private Integer specimanId;
	private String specimanName;
	private Character isGynac;
	private Character isFnac;
	private List<ParameterMasterDto> listParameterMasterDto;
	private List<TestMasterForOnHoldDto> listTestParamMppr;
	private List<TestMasterForOnHoldDto> listTestParamMpprList;
	private List<CommonDto> equipmentList;
	private Character status;
	private String sampleDesc;
	private Character isUniqueBarcode;
	private String finalResult;
	private Integer serologyTestResultId;
	private Integer labSampleDtlsId;
	private String firstFinding;
	private String firstFindingDesc;
	private String secondFinding;
	private String secondFindingDesc;
	private String internalNotes;
	private boolean isOnHoldParameter;
	private String resultEntryUser;
	private String resultValidatedUser;
	private Integer orgId;
	private Integer orgUnitId;
	private Integer resultValidatedBy;
	private String resultValidatedDate;
	private Integer reportReleaseBy;
	private String reportReleaseDate;
	private String findingResult;
	private String findingResultDesc;
	private Integer parameterId;
	private String desc;
	private String parameterDesc;

	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public Character getIsCentrifugationReq() {
		return isCentrifugationReq;
	}
	public void setIsCentrifugationReq(Character isCentrifugationReq) {
		this.isCentrifugationReq = isCentrifugationReq;
	}
	public String getTestAlies() {
		return testAlies;
	}
	public void setTestAlies(String testAlies) {
		this.testAlies = testAlies;
	}
	public String getTestCode() {
		return testCode;
	}
	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}
	public String getTestDesc() {
		return testDesc;
	}
	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}
	public Integer getTestType() {
		return testType;
	}
	public void setTestType(Integer testType) {
		this.testType = testType;
	}
	public String getTechniqueName() {
		return techniqueName;
	}
	public void setTechniqueName(String techniqueName) {
		this.techniqueName = techniqueName;
	}
	public Integer getSerologyResultTestMpprId() {
		return serologyResultTestMpprId;
	}
	public void setSerologyResultTestMpprId(Integer serologyResultTestMpprId) {
		this.serologyResultTestMpprId = serologyResultTestMpprId;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public Integer getSampleId() {
		return sampleId;
	}
	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}
	public Integer getTechniqueId() {
		return techniqueId;
	}
	public void setTechniqueId(Integer techniqueId) {
		this.techniqueId = techniqueId;
	}
	public Integer getTestId() {
		return testId;
	}
	public void setTestId(Integer testId) {
		this.testId = testId;
	}
	public Character getIsHistoCyto() {
		return isHistoCyto;
	}
	public void setIsHistoCyto(Character isHistoCyto) {
		this.isHistoCyto = isHistoCyto;
	}
	public Integer getSpecimanId() {
		return specimanId;
	}
	public void setSpecimanId(Integer specimanId) {
		this.specimanId = specimanId;
	}
	public String getSpecimanName() {
		return specimanName;
	}
	public void setSpecimanName(String specimanName) {
		this.specimanName = specimanName;
	}
	public Character getIsGynac() {
		return isGynac;
	}
	public void setIsGynac(Character isGynac) {
		this.isGynac = isGynac;
	}
	public Character getIsFnac() {
		return isFnac;
	}
	public void setIsFnac(Character isFnac) {
		this.isFnac = isFnac;
	}
	public List<ParameterMasterDto> getListParameterMasterDto() {
		return listParameterMasterDto;
	}
	public void setListParameterMasterDto(List<ParameterMasterDto> listParameterMasterDto) {
		this.listParameterMasterDto = listParameterMasterDto;
	}
	public List<CommonDto> getEquipmentList() {
		return equipmentList;
	}
	public void setEquipmentList(List<CommonDto> equipmentList) {
		this.equipmentList = equipmentList;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public String getSampleDesc() {
		return sampleDesc;
	}
	public void setSampleDesc(String sampleDesc) {
		this.sampleDesc = sampleDesc;
	}
	public Character getIsUniqueBarcode() {
		return isUniqueBarcode;
	}
	public void setIsUniqueBarcode(Character isUniqueBarcode) {
		this.isUniqueBarcode = isUniqueBarcode;
	}
	public String getFinalResult() {
		return finalResult;
	}
	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}
	public Integer getSerologyTestResultId() {
		return serologyTestResultId;
	}
	public void setSerologyTestResultId(Integer serologyTestResultId) {
		this.serologyTestResultId = serologyTestResultId;
	}
	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}
	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}
	public String getFirstFinding() {
		return firstFinding;
	}
	public void setFirstFinding(String firstFinding) {
		this.firstFinding = firstFinding;
	}
	public String getFirstFindingDesc() {
		return firstFindingDesc;
	}
	public void setFirstFindingDesc(String firstFindingDesc) {
		this.firstFindingDesc = firstFindingDesc;
	}
	public String getSecondFinding() {
		return secondFinding;
	}
	public void setSecondFinding(String secondFinding) {
		this.secondFinding = secondFinding;
	}
	public String getSecondFindingDesc() {
		return secondFindingDesc;
	}
	public void setSecondFindingDesc(String secondFindingDesc) {
		this.secondFindingDesc = secondFindingDesc;
	}
	public String getInternalNotes() {
		return internalNotes;
	}
	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}
	public boolean getIsOnHoldParameter() {
		return isOnHoldParameter;
	}
	public void setIsOnHoldParameter(boolean isOnHoldParameter) {
		this.isOnHoldParameter = isOnHoldParameter;
	}
	public String getResultEntryUser() {
		return resultEntryUser;
	}
	public void setResultEntryUser(String resultEntryUser) {
		this.resultEntryUser = resultEntryUser;
	}
	public String getResultValidatedUser() {
		return resultValidatedUser;
	}
	public void setResultValidatedUser(String resultValidatedUser) {
		this.resultValidatedUser = resultValidatedUser;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getOrgUnitId() {
		return orgUnitId;
	}
	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}
	public Integer getResultValidatedBy() {
		return resultValidatedBy;
	}
	public void setResultValidatedBy(Integer resultValidatedBy) {
		this.resultValidatedBy = resultValidatedBy;
	}
	public String getResultValidatedDate() {
		return resultValidatedDate;
	}
	public void setResultValidatedDate(String resultValidatedDate) {
		this.resultValidatedDate = resultValidatedDate;
	}
	public Integer getReportReleaseBy() {
		return reportReleaseBy;
	}
	public void setReportReleaseBy(Integer reportReleaseBy) {
		this.reportReleaseBy = reportReleaseBy;
	}
	public String getReportReleaseDate() {
		return reportReleaseDate;
	}
	public void setReportReleaseDate(String reportReleaseDate) {
		this.reportReleaseDate = reportReleaseDate;
	}
	public List<TestMasterForOnHoldDto> getListTestParamMppr() {
		return listTestParamMppr;
	}
	public void setListTestParamMppr(List<TestMasterForOnHoldDto> listTestParamMppr) {
		this.listTestParamMppr = listTestParamMppr;
	}
	public String getFindingResult() {
		return findingResult;
	}
	public void setFindingResult(String findingResult) {
		this.findingResult = findingResult;
	}
	public String getFindingResultDesc() {
		return findingResultDesc;
	}
	public void setFindingResultDesc(String findingResultDesc) {
		this.findingResultDesc = findingResultDesc;
	}
	public Integer getParameterId() {
		return parameterId;
	}
	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<TestMasterForOnHoldDto> getListTestParamMpprList() {
		return listTestParamMpprList;
	}
	public void setListTestParamMpprList(List<TestMasterForOnHoldDto> listTestParamMpprList) {
		this.listTestParamMpprList = listTestParamMpprList;
	}
	public String getParameterDesc() {
		return parameterDesc;
	}
	public void setParameterDesc(String parameterDesc) {
		this.parameterDesc = parameterDesc;
	}
	
	
}
