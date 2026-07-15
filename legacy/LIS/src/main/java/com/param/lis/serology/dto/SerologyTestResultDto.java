package com.param.lis.serology.dto;

import java.util.List;

import com.param.lis.unit.dto.HelpValueMasterDto;

public class SerologyTestResultDto {

	private int serologyTestResultId;

	private int labSampleDtlsId;

	private Integer resultEnterBy;
	
	private String resultEnterDate;

	private Integer resultValidatedBy;
	
	private String resultValidatedDate;
	
	private Integer reportReleaseBy;
	
	private String reportReleaseDate;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private Integer orgId;

	private Integer orgUnitId;
	
	private String techniqueName;
	
	private String resultEntryUser;
	
	private String resultValidatedUser;
	
	private String firstFinding;

	private String firstFindingDesc;
	
	private String parameterName;
	
	private int serologyResultTestMpprId;
	
	private String secondFinding;

	private String secondFindingDesc;
	
	private Character proRelease; 
	
	private Integer proReleaseFlag;
	
	private Integer orderDetailsId;
	
	private String findingResult;

	private String findingResultDesc;
	
	private String testDesc;
	private String desc;
	private Integer techniqueId;
	private String internalNotes;
	
	private Boolean triggerReleaseMail;
	
	private Integer companyId;
	
	private Integer insuranceId;
	
	private Integer parameterId;
	private Integer deptId;
	private Integer testId;

	 private List<HelpValueMasterDto> listHelpValueMaster;
	 
	private Boolean sendMailOnOrderCompletion;
	 private boolean isOnHoldParameter;
	private String orderNo;

	List<SerologyResultTestMapperDto> serologyResultTestMapperDtoList;
	
	private boolean isReportRecall;

	private Character isAbnormal;
	
	private Boolean provisional;
	
	private Boolean amend;
	
	private Integer sampleStatusId;

	private Integer assignUnitId;
	
	public int getSerologyTestResultId() {
		return serologyTestResultId;
	}

	public void setSerologyTestResultId(int serologyTestResultId) {
		this.serologyTestResultId = serologyTestResultId;
	}

	public int getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(int labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getResultEnterBy() {
		return resultEnterBy;
	}

	public void setResultEnterBy(Integer resultEnterBy) {
		this.resultEnterBy = resultEnterBy;
	}

	public String getResultEnterDate() {
		return resultEnterDate;
	}

	public void setResultEnterDate(String resultEnterDate) {
		this.resultEnterDate = resultEnterDate;
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

	public List<SerologyResultTestMapperDto> getSerologyResultTestMapperDtoList() {
		return serologyResultTestMapperDtoList;
	}

	public void setSerologyResultTestMapperDtoList(List<SerologyResultTestMapperDto> serologyResultTestMapperDtoList) {
		this.serologyResultTestMapperDtoList = serologyResultTestMapperDtoList;
	}

	public String getTechniqueName() {
		return techniqueName;
	}

	public void setTechniqueName(String techniqueName) {
		this.techniqueName = techniqueName;
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

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public int getSerologyResultTestMpprId() {
		return serologyResultTestMpprId;
	}

	public void setSerologyResultTestMpprId(int serologyResultTestMpprId) {
		this.serologyResultTestMpprId = serologyResultTestMpprId;
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

	public Character getProRelease() {
		return proRelease;
	}

	public void setProRelease(Character proRelease) {
		this.proRelease = proRelease;
	}

	public Integer getProReleaseFlag() {
		return proReleaseFlag;
	}

	public void setProReleaseFlag(Integer proReleaseFlag) {
		this.proReleaseFlag = proReleaseFlag;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
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

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public Boolean getTriggerReleaseMail() {
		return triggerReleaseMail;
	}

	public void setTriggerReleaseMail(Boolean triggerReleaseMail) {
		this.triggerReleaseMail = triggerReleaseMail;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public List<HelpValueMasterDto> getListHelpValueMaster() {
		return listHelpValueMaster;
	}

	public void setListHelpValueMaster(List<HelpValueMasterDto> listHelpValueMaster) {
		this.listHelpValueMaster = listHelpValueMaster;
	}

	public Boolean getSendMailOnOrderCompletion() {
		return sendMailOnOrderCompletion;
	}

	public void setSendMailOnOrderCompletion(Boolean sendMailOnOrderCompletion) {
		this.sendMailOnOrderCompletion = sendMailOnOrderCompletion;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}

	public boolean getIsReportRecall() {
		return isReportRecall;
	}

	public void setIsReportRecall(boolean isReportRecall) {
		this.isReportRecall = isReportRecall;
	}

	public Character getIsAbnormal() {
		return isAbnormal;
	}

	public void setIsAbnormal(Character isAbnormal) {
		this.isAbnormal = isAbnormal;
	}
	
	 public boolean getIsOnHoldParameter() {
	        return isOnHoldParameter;
	    }

	    public void setIsOnHoldParameter(boolean isOnHoldParameter) {
	        this.isOnHoldParameter = isOnHoldParameter;
	    }

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public Integer getTechniqueId() {
			return techniqueId;
		}

		public void setTechniqueId(Integer techniqueId) {
			this.techniqueId = techniqueId;
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

		public Boolean getProvisional() {
			return provisional;
		}

		public void setProvisional(Boolean provisional) {
			this.provisional = provisional;
		}

		public Boolean getAmend() {
			return amend;
		}

		public void setAmend(Boolean amend) {
			this.amend = amend;
		}

		public Integer getSampleStatusId() {
			return sampleStatusId;
		}

		public void setSampleStatusId(Integer sampleStatusId) {
			this.sampleStatusId = sampleStatusId;
		}

	public Integer getAssignUnitId() {
		return assignUnitId;
	}

	public void setAssignUnitId(Integer assignUnitId) {
		this.assignUnitId = assignUnitId;
	}
}
