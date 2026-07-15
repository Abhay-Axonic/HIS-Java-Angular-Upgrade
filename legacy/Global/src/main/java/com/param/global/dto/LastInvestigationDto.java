package com.param.global.dto;

import java.util.List;

public class LastInvestigationDto {
	
	private Integer parameterId;
	
	private String  paramName;
	
	private String paramUnit;
	
	private String releaseDate;
	
	private String finalResult;
	
	private Integer patientId;
	
	private List<LastInvestigationDto> releaseDateList;
	
	private List<LastInvestigationDto> parameterList;
	
	private List<LastInvestigationDto> lastInvestigationList;
	
	private Integer organizationId;
	
	private Integer unitId;

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamUnit() {
		return paramUnit;
	}

	public void setParamUnit(String paramUnit) {
		this.paramUnit = paramUnit;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	
	public List<LastInvestigationDto> getLastInvestigationList() {
		return lastInvestigationList;
	}

	public void setLastInvestigationList(List<LastInvestigationDto> lastInvestigationList) {
		this.lastInvestigationList = lastInvestigationList;
	}

	public List<LastInvestigationDto> getReleaseDateList() {
		return releaseDateList;
	}

	public void setReleaseDateList(List<LastInvestigationDto> releaseDateList) {
		this.releaseDateList = releaseDateList;
	}

	public List<LastInvestigationDto> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<LastInvestigationDto> parameterList) {
		this.parameterList = parameterList;
	}
}
