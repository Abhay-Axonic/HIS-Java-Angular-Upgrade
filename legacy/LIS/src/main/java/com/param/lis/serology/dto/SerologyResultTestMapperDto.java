package com.param.lis.serology.dto;

public class SerologyResultTestMapperDto {

	private int serologyResultTestMpprId;

	private int serologyTestResultId;
	
	private int techniqueId;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private Integer orgId;

	private Integer orgUnitId;
	
	private String findingResult;

	private String findingResultDesc;
	
	private String firstFinding;

	private String firstFindingDesc;

	private String secondFinding;

	private String secondFindingDesc;
	
	private String thiredFinding;

	private String thirdFindingDesc;

	private Integer parameterId;

	private Character isAbnormal;

	public int getSerologyResultTestMpprId() {
		return serologyResultTestMpprId;
	}

	public void setSerologyResultTestMpprId(int serologyResultTestMpprId) {
		this.serologyResultTestMpprId = serologyResultTestMpprId;
	}

	public int getSerologyTestResultId() {
		return serologyTestResultId;
	}

	public void setSerologyTestResultId(int serologyTestResultId) {
		this.serologyTestResultId = serologyTestResultId;
	}


	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public int getTechniqueId() {
		return techniqueId;
	}

	public void setTechniqueId(int techniqueId) {
		this.techniqueId = techniqueId;
	}

	public String getFindingResult() {
		return findingResult;
	}

	public void setFindingResult(String findingResult) {
		 this.findingResult =  (findingResult == null) ? "N" : findingResult;
	}

	public String getFindingResultDesc() {
		return findingResultDesc;
	}

	public void setFindingResultDesc(String findingResultDesc) {
		this.findingResultDesc = findingResultDesc;
	}

	public String getFirstFinding() {
		return firstFinding;
	}

	public void setFirstFinding(String firstFinding) {
		this.firstFinding = (firstFinding == null) ? "N" : firstFinding;
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
		this.secondFinding = (secondFinding == null) ? "N" : secondFinding;
	}

	public String getSecondFindingDesc() {
		return secondFindingDesc;
	}

	public void setSecondFindingDesc(String secondFindingDesc) {
		this.secondFindingDesc = secondFindingDesc;
	}

	public String getThiredFinding() {
		return thiredFinding;
	}

	public void setThiredFinding(String thiredFinding) {
		this.thiredFinding = (thiredFinding == null) ? "N" : thiredFinding;
	}

	public String getThirdFindingDesc() {
		return thirdFindingDesc;
	}

	public void setThirdFindingDesc(String thirdFindingDesc) {
		this.thirdFindingDesc = thirdFindingDesc;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public Character getIsAbnormal() {
		return isAbnormal;
	}

	public void setIsAbnormal(Character isAbnormal) {
		this.isAbnormal = isAbnormal;
	}


	
	
	
}
