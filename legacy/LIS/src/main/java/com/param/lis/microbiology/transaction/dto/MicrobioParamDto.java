package com.param.lis.microbiology.transaction.dto;

import javax.persistence.Column;

public class MicrobioParamDto {
	
private Integer labSampleDtlsId;
	
	private Integer orgId;
	
	private Integer orgUnitId;
	
	private Integer offset;
	
	private Integer deptId;
	
	private Integer subDeptId;
	
	private Integer recordPerPage;
	
	private Integer microbioResultEntryId;
	
	private Integer sampleStatusId;
	
	private Integer sampleId;
	
	private String fromDate;
	
	private String toDate;
	
	private char isAcceptanceInDepartment;
	
	private char isWorklistDepartment;
	
	private Integer assignUnit;

    private boolean isOnHoldParameter;
    
	private Character isSendToCrApplicable;
	
	private char isSampleAcceptanceApplicable;
    
	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
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

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public Integer getMicrobioResultEntryId() {
		return microbioResultEntryId;
	}

	public void setMicrobioResultEntryId(Integer microbioResultEntryId) {
		this.microbioResultEntryId = microbioResultEntryId;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}

	public Integer getSampleId() {
		return sampleId;
	}

	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
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
	

	public char getIsAcceptanceInDepartment() {
		return isAcceptanceInDepartment;
	}

	public void setIsAcceptanceInDepartment(char isAcceptanceInDepartment) {
		this.isAcceptanceInDepartment = (isAcceptanceInDepartment == '\u0000') ? 'N' : isAcceptanceInDepartment;
	}

	public char getIsWorklistDepartment() {
		return isWorklistDepartment;
	}

	public void setIsWorklistDepartment(char isWorklistDepartment) {
		this.isWorklistDepartment = (isWorklistDepartment == '\u0000') ? 'N' : isWorklistDepartment;
	}

	public Integer getAssignUnit() {
		return assignUnit;
	}

	public void setAssignUnit(Integer assignUnit) {
		this.assignUnit = assignUnit;
	}
	
	public boolean getIsOnHoldParameter() {
        return isOnHoldParameter;
    }

    public void setIsOnHoldParameter(boolean isOnHoldParameter) {
        this.isOnHoldParameter = isOnHoldParameter;
    }

	public Character getIsSendToCrApplicable() {
		return isSendToCrApplicable;
	}

	public void setIsSendToCrApplicable(Character isSendToCrApplicable) {
		this.isSendToCrApplicable = isSendToCrApplicable;
	}

	public char getIsSampleAcceptanceApplicable() {
		return isSampleAcceptanceApplicable;
	}

	public void setIsSampleAcceptanceApplicable(char isSampleAcceptanceApplicable) {
		this.isSampleAcceptanceApplicable = isSampleAcceptanceApplicable;
	}

}
