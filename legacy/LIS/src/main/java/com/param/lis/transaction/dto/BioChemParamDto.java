package com.param.lis.transaction.dto;

public class BioChemParamDto {
	private Integer labSampleDtlsId;

	private Integer orgId;

	private Integer orgUnitId;

	private Integer offset;

	private Integer deptId;

	private Integer recordPerPage;

	private Integer testId;

	private Integer testType;

	private Integer patientAgeDays;

	private Integer panelId;

	private Integer genderId;

	private Integer labResultId;

	private Integer headerId;

	private Integer subDeptId;

	private String labSampleNo;

	private Integer patientId;

	private Integer resultLevel;

	private Integer assingnedFrom;
	
	private String fromDate;
	
	private String toDate;
	
	private Long fromDt;
	
	private Long toDt;
	
	private char isSampleAcceptanceApplicable;
	
	private char isAcceptanceInDepartment;
	
	private char isWorklistDepartment;
	
	private Character isSendToCrApplicable;
	
	private Integer assignUnit;
	
	private Boolean isMailSend;
	
	private Integer labSortingId;
	private Integer labTestResId;

	private Integer machineId;
    private boolean isOnHoldParameter;

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

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

	public Integer getPatientAgeDays() {
		return patientAgeDays;
	}

	public void setPatientAgeDays(Integer patientAgeDays) {
		this.patientAgeDays = patientAgeDays;
	}

	public Integer getLabResultId() {
		return labResultId;
	}

	public void setLabResultId(Integer labResultId) {
		this.labResultId = labResultId;
	}

	public Integer getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public String getLabSampleNo() {
		return labSampleNo;
	}

	public void setLabSampleNo(String labSampleNo) {
		this.labSampleNo = labSampleNo;
	}

	public Integer getPanelId() {
		return panelId;
	}

	public void setPanelId(Integer panelId) {
		this.panelId = panelId;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getResultLevel() {
		return resultLevel;
	}

	public void setResultLevel(Integer resultLevel) {
		this.resultLevel = resultLevel;
	}

	public Integer getAssingnedFrom() {
		return assingnedFrom;
	}

	public void setAssingnedFrom(Integer assingnedFrom) {
		this.assingnedFrom = assingnedFrom;
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

	public Long getFromDt() {
		return fromDt;
	}

	public void setFromDt(Long fromDt) {
		this.fromDt = fromDt;
	}

	public Long getToDt() {
		return toDt;
	}

	public void setToDt(Long toDt) {
		this.toDt = toDt;
	}


	public char getIsSampleAcceptanceApplicable() {
		return isSampleAcceptanceApplicable;
	}

	public void setIsSampleAcceptanceApplicable(char isSampleAcceptanceApplicable) {
		this.isSampleAcceptanceApplicable = (isSampleAcceptanceApplicable == '\u0000') ? 'N' : isSampleAcceptanceApplicable;
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

	public Character getIsSendToCrApplicable() {
		return isSendToCrApplicable;
	}

	public void setIsSendToCrApplicable(Character isSendToCrApplicable) {
		this.isSendToCrApplicable = isSendToCrApplicable;
	}

	public Integer getAssignUnit() {
		return assignUnit;
	}

	public void setAssignUnit(Integer assignUnit) {
		this.assignUnit = assignUnit;
	}

	public Boolean getIsMailSend() {
		return isMailSend;
	}

	public void setIsMailSend(Boolean isMailSend) {
		this.isMailSend = isMailSend;
	}

	public Integer getLabSortingId() {
		return labSortingId;
	}

	public void setLabSortingId(Integer labSortingId) {
		this.labSortingId = labSortingId;
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

    public boolean getIsOnHoldParameter() {
        return isOnHoldParameter;
    }

    public void setIsOnHoldParameter(boolean isOnHoldParameter) {
        this.isOnHoldParameter = isOnHoldParameter;
    }

	public Integer getLabTestResId() {
		return labTestResId;
	}

	public void setLabTestResId(Integer labTestResId) {
		this.labTestResId = labTestResId;
	}

}
