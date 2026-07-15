package com.param.global.unit.dto;

public class LabConfigurationDto {

	private Integer tLabConfigId;

	private Integer labSpecialityId;

	private Integer biochemistryId;

	private Integer hematologyId;

	private Integer microbiologyId;

	private Integer histopathologyId;

	private Integer cystopathologyId;

	private Integer serologyId;

	private Character isPrebarcodeGenerantion;

	private Character status;
	
	private Character isSendToCrApplicable;

	private Integer organisationId;

	private Integer unitId;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private String biochemistryDept;
	
	private String heamotologyDept;
	
	private String microbioDept;
	
	private String histopathDept;
	
	private String cytopathDept;
	
	private String seologyDept;
	
	private String specialityName;
	
	private Boolean isPinApplicable;
	
	private Integer clinicalPathologyId;
	
	private String clinicalPathologyDept;
	
	private Character isSampleAcceptanceApplicable;
	
	private Character isAcceptanceInDepartment;
	
	private Character isWorklistDepartment;
	
	private Character isReportRecall;
	
	private Character isValidationAndRelease;
	
	private Character reportReleasePin;
	
	private Character photoRequired; 
	
	private Character consolidatedResultEntry; 
	
	private Character groupRelease; 
	
	private Character triggerReleaseMail;
	
	private Character isPRNPrint;
	
	private String prnFormat;
	
	private String prnUrl;
	
	private Character collectSampleOnOPBilling;
	
	private Integer labSortingId;
	
	private Character skipValidation; 
	
	private Character bySampleType; 
	
	private Character byDepartment; 
	
	private Character byContainer; 
	
	private Character sendMailOnOrderCompletion;
	
	private Character manualSampleCollectionTime;
	
	private Character incubationMasterApplicable;
	
	private Character macroscopicExaminationApplicable;
	
	private Character biochemicalTestingApplicable;
	
	private Character microscopicExaminationApplicable;
	
	private Character qrCodeRequired;
	
	private Character printLabReportBeforRelease;
	private Integer outsourceLabId;
	public Integer gettLabConfigId() {
		return tLabConfigId;
	}

	public void settLabConfigId(Integer tLabConfigId) {
		this.tLabConfigId = tLabConfigId;
	}

	public Integer getLabSpecialityId() {
		return labSpecialityId;
	}

	public void setLabSpecialityId(Integer labSpecialityId) {
		this.labSpecialityId = labSpecialityId;
	}

	public Integer getBiochemistryId() {
		return biochemistryId;
	}

	public void setBiochemistryId(Integer biochemistryId) {
		this.biochemistryId = biochemistryId;
	}

	public Integer getHematologyId() {
		return hematologyId;
	}

	public void setHematologyId(Integer hematologyId) {
		this.hematologyId = hematologyId;
	}

	public Integer getMicrobiologyId() {
		return microbiologyId;
	}

	public void setMicrobiologyId(Integer microbiologyId) {
		this.microbiologyId = microbiologyId;
	}

	public Integer getHistopathologyId() {
		return histopathologyId;
	}

	public void setHistopathologyId(Integer histopathologyId) {
		this.histopathologyId = histopathologyId;
	}

	public Integer getCystopathologyId() {
		return cystopathologyId;
	}

	public void setCystopathologyId(Integer cystopathologyId) {
		this.cystopathologyId = cystopathologyId;
	}

	public Integer getSerologyId() {
		return serologyId;
	}

	public void setSerologyId(Integer serologyId) {
		this.serologyId = serologyId;
	}

	public Character getIsPrebarcodeGenerantion() {
		return isPrebarcodeGenerantion;
	}

	public void setIsPrebarcodeGenerantion(Character isPrebarcodeGenerantion) {
		this.isPrebarcodeGenerantion = isPrebarcodeGenerantion;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Character getIsSendToCrApplicable() {
		return isSendToCrApplicable;
	}

	public void setIsSendToCrApplicable(Character isSendToCrApplicable) {
		this.isSendToCrApplicable = isSendToCrApplicable;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public String getBiochemistryDept() {
		return biochemistryDept;
	}

	public void setBiochemistryDept(String biochemistryDept) {
		this.biochemistryDept = biochemistryDept;
	}

	public String getHeamotologyDept() {
		return heamotologyDept;
	}

	public void setHeamotologyDept(String heamotologyDept) {
		this.heamotologyDept = heamotologyDept;
	}

	public String getMicrobioDept() {
		return microbioDept;
	}

	public void setMicrobioDept(String microbioDept) {
		this.microbioDept = microbioDept;
	}

	public String getHistopathDept() {
		return histopathDept;
	}

	public void setHistopathDept(String histopathDept) {
		this.histopathDept = histopathDept;
	}

	public String getCytopathDept() {
		return cytopathDept;
	}

	public void setCytopathDept(String cytopathDept) {
		this.cytopathDept = cytopathDept;
	}

	public String getSeologyDept() {
		return seologyDept;
	}

	public void setSeologyDept(String seologyDept) {
		this.seologyDept = seologyDept;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Boolean getIsPinApplicable() {
		return isPinApplicable;
	}

	public void setIsPinApplicable(Boolean isPinApplicable) {
		this.isPinApplicable = isPinApplicable;
	}

	public Integer getClinicalPathologyId() {
		return clinicalPathologyId;
	}

	public void setClinicalPathologyId(Integer clinicalPathologyId) {
		this.clinicalPathologyId = clinicalPathologyId;
	}

	public String getClinicalPathologyDept() {
		return clinicalPathologyDept;
	}

	public void setClinicalPathologyDept(String clinicalPathologyDept) {
		this.clinicalPathologyDept = clinicalPathologyDept;
	}

	public Character getIsSampleAcceptanceApplicable() {
		return isSampleAcceptanceApplicable;
	}

	public void setIsSampleAcceptanceApplicable(Character isSampleAcceptanceApplicable) {
		this.isSampleAcceptanceApplicable = isSampleAcceptanceApplicable;
	}

	public Character getIsAcceptanceInDepartment() {
		return isAcceptanceInDepartment;
	}

	public void setIsAcceptanceInDepartment(Character isAcceptanceInDepartment) {
		this.isAcceptanceInDepartment = isAcceptanceInDepartment;
	}

	public Character getIsWorklistDepartment() {
		return isWorklistDepartment;
	}

	public void setIsWorklistDepartment(Character isWorklistDepartment) {
		this.isWorklistDepartment = isWorklistDepartment;
	}

	public Character getIsReportRecall() {
		return isReportRecall;
	}

	public void setIsReportRecall(Character isReportRecall) {
		this.isReportRecall = isReportRecall;
	}

	public Character getIsValidationAndRelease() {
		return isValidationAndRelease;
	}

	public void setIsValidationAndRelease(Character isValidationAndRelease) {
		this.isValidationAndRelease = isValidationAndRelease;
	}

	public Character getReportReleasePin() {
		return reportReleasePin;
	}

	public void setReportReleasePin(Character reportReleasePin) {
		this.reportReleasePin = reportReleasePin;
	}

	public Character getPhotoRequired() {
		return photoRequired;
	}

	public void setPhotoRequired(Character photoRequired) {
		this.photoRequired = photoRequired;
	}

	public Character getConsolidatedResultEntry() {
		return consolidatedResultEntry;
	}

	public void setConsolidatedResultEntry(Character consolidatedResultEntry) {
		this.consolidatedResultEntry = consolidatedResultEntry;
	}

	public Character getGroupRelease() {
		return groupRelease;
	}

	public void setGroupRelease(Character groupRelease) {
		this.groupRelease = groupRelease;
	}

	public Character getTriggerReleaseMail() {
		return triggerReleaseMail;
	}

	public void setTriggerReleaseMail(Character triggerReleaseMail) {
		this.triggerReleaseMail = triggerReleaseMail;
	}

	public Character getIsPRNPrint() {
		return isPRNPrint;
	}

	public void setIsPRNPrint(Character isPRNPrint) {
		this.isPRNPrint = isPRNPrint;
	}

	public String getPrnFormat() {
		return prnFormat;
	}

	public void setPrnFormat(String prnFormat) {
		this.prnFormat = prnFormat;
	}

	public String getPrnUrl() {
		return prnUrl;
	}

	public void setPrnUrl(String prnUrl) {
		this.prnUrl = prnUrl;
	}

	public Character getCollectSampleOnOPBilling() {
		return collectSampleOnOPBilling;
	}

	public void setCollectSampleOnOPBilling(Character collectSampleOnOPBilling) {
		this.collectSampleOnOPBilling = collectSampleOnOPBilling;
	}

	public Integer getLabSortingId() {
		return labSortingId;
	}

	public void setLabSortingId(Integer labSortingId) {
		this.labSortingId = labSortingId;
	}

	public Character getSkipValidation() {
		return skipValidation;
	}

	public void setSkipValidation(Character skipValidation) {
		this.skipValidation = skipValidation;
	}

	public Character getBySampleType() {
		return bySampleType;
	}

	public void setBySampleType(Character bySampleType) {
		this.bySampleType = bySampleType;
	}

	public Character getByDepartment() {
		return byDepartment;
	}

	public void setByDepartment(Character byDepartment) {
		this.byDepartment = byDepartment;
	}

	public Character getByContainer() {
		return byContainer;
	}

	public void setByContainer(Character byContainer) {
		this.byContainer = byContainer;
	}

	public Character getSendMailOnOrderCompletion() {
		return sendMailOnOrderCompletion;
	}

	public void setSendMailOnOrderCompletion(Character sendMailOnOrderCompletion) {
		this.sendMailOnOrderCompletion = sendMailOnOrderCompletion;
	}

	public Character getManualSampleCollectionTime() {
		return manualSampleCollectionTime;
	}

	public void setManualSampleCollectionTime(Character manualSampleCollectionTime) {
		this.manualSampleCollectionTime = manualSampleCollectionTime;
	}

	public Character getIncubationMasterApplicable() {
		return incubationMasterApplicable;
	}

	public void setIncubationMasterApplicable(Character incubationMasterApplicable) {
		this.incubationMasterApplicable = incubationMasterApplicable;
	}

	public Character getMacroscopicExaminationApplicable() {
		return macroscopicExaminationApplicable;
	}

	public void setMacroscopicExaminationApplicable(Character macroscopicExaminationApplicable) {
		this.macroscopicExaminationApplicable = macroscopicExaminationApplicable;
	}

	public Character getBiochemicalTestingApplicable() {
		return biochemicalTestingApplicable;
	}

	public void setBiochemicalTestingApplicable(Character biochemicalTestingApplicable) {
		this.biochemicalTestingApplicable = biochemicalTestingApplicable;
	}

	public Character getMicroscopicExaminationApplicable() {
		return microscopicExaminationApplicable;
	}

	public void setMicroscopicExaminationApplicable(Character microscopicExaminationApplicable) {
		this.microscopicExaminationApplicable = microscopicExaminationApplicable;
	}

	public Character getQrCodeRequired() {
		return qrCodeRequired;
	}

	public void setQrCodeRequired(Character qrCodeRequired) {
		this.qrCodeRequired = qrCodeRequired;
	}

	public Character getPrintLabReportBeforRelease() {
		return printLabReportBeforRelease;
	}

	public void setPrintLabReportBeforRelease(Character printLabReportBeforRelease) {
		this.printLabReportBeforRelease = printLabReportBeforRelease;
	}

	public Integer getOutsourceLabId() {
		return outsourceLabId;
	}

	public void setOutsourceLabId(Integer outsourceLabId) {
		this.outsourceLabId = outsourceLabId;
	}
	
}
