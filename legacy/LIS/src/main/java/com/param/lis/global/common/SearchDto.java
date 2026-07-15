package com.param.lis.global.common;

import java.util.List;

public class SearchDto {

	private Integer orgId;
	private Integer orgUnitId;
	private Integer assignUnit;
	private Long fromDate;
	private Long toDate;
	private Integer patientId;
	private Integer priorityId;
	private Integer reportType;
	private Character pending;
	private Character outSource;
	private Character outsourced;
	private Character completed;
	private Integer sampleStatusId;
	private Integer labSampleDtlsId;
	private List<obj> visitTypes;
	private List<obj> specimanTypes;
	private List<obj> testTypes;
	private List<obj> subDepts;
	private Integer deptId;
	private Integer subDeptId;
	private Integer visitTypeId;
	private Integer orderStatusId;
	private Character isPreOrPostBilling;
	private Character isSendToCrApplicable;
	private Integer recordPerPage;
	private Integer offset;
	private String fromDt;
	private String toDt;
	private Integer labSampleId;
	private String outSourceLabName;
	private String uhidNumber;
	private String sampleNo;
	private char isSampleAcceptanceApplicable;
	private char isAcceptanceInDepartment;
	private char isWorklistDepartment;
	private Integer userId;
	private char isOutSource;
	private Integer orderDetailsId;
	private Boolean isCountRequired;
	private List<obj> departmentList;
	private String searchField;
	private Integer labSortingId;
	private Boolean isMailSend;
	private Integer orderId;
	private Integer payeeId;
	private List<obj> ids;
	private Integer paymentEntitlementId;
	private String testDesc;
	private List<Integer> labSampleDtlsIds;
	private Integer microbiologyTemplateId; 
	private Integer referralDocId; 
	private List<SearchDto> orderDetailsMasterDtosList;
	private List<Integer> subDeptIds;
	private String sampleReception;
	private String sampleProcessing;
	private String sampleAnalysisAndDetection;
	private Integer createdBy; 
	private Integer doctorId; 
	private Integer companyId; 
	private Integer insuranceId; 
	private String documentPath;
	private String updatedDate;
	private Integer updatedBy; 
	private Integer serologyTestResultId; 
	private Integer resultEnterBy; 
	private List<SearchDto> serologyResultTestsList;
	private String sampleRejectReason;
	private Integer sampleRejectReasonId; 
	private boolean sampleRecollection;
	private Integer specialityId; 
	private Integer subSpecialityMasterId; 
	private Integer seroDeptId; 
	private String assignedByName;
	private String searchKeyword;
	private Boolean popupIconFlag;

	private String newWhatsAppNo;
	private Boolean sendOnlyWhatsApp;
	private Boolean checkForIsPOC;

	private List<Integer> patientIds;
	private Character isClinicalInterventionRequired;
	private String interventionEndDate;
	private Integer clinicalInterventionProcedureId;
	private Integer interventionCompletedDoctorId;

	public String getAssignedByName() {
		return assignedByName;
	}
	public void setAssignedByName(String assignedByName) {
		this.assignedByName = assignedByName;
	}
	public List<SearchDto> getOrderDetailsMasterDtosList() {
		return orderDetailsMasterDtosList;
	}
	public void setOrderDetailsMasterDtosList(List<SearchDto> orderDetailsMasterDtosList) {
		this.orderDetailsMasterDtosList = orderDetailsMasterDtosList;
	}
	private String reportReleaseDate;
    private boolean isOnHoldParameter;
    
	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public Long getFromDate() {
		return fromDate;
	}

	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}

	public Long getToDate() {
		return toDate;
	}

	public void setToDate(Long toDate) {
		this.toDate = toDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public List<obj> getVisitTypes() {
		return visitTypes;
	}

	public void setVisitTypes(List<obj> visitTypes) {
		this.visitTypes = visitTypes;
	}

	public List<obj> getSpecimanTypes() {
		return specimanTypes;
	}

	public void setSpecimanTypes(List<obj> specimanTypes) {
		this.specimanTypes = specimanTypes;
	}

	public List<obj> getTestTypes() {
		return testTypes;
	}

	public void setTestTypes(List<obj> testTypes) {
		this.testTypes = testTypes;
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

	public List<obj> getSubDepts() {
		return subDepts;
	}

	public void setSubDepts(List<obj> subDepts) {
		this.subDepts = subDepts;
	}

	public Character getPending() {
		return pending;
	}

	public void setPending(Character pending) {
		this.pending = pending;
	}

	public Character getOutSource() {
		return outSource;
	}

	public void setOutSource(Character outSource) {
		this.outSource = outSource;
	}

	public Character getCompleted() {
		return completed;
	}

	public void setCompleted(Character completed) {
		this.completed = completed;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public Character getOutsourced() {
		return outsourced;
	}

	public void setOutsourced(Character outsourced) {
		this.outsourced = outsourced;
	}

	public Integer getAssignUnit() {
		return assignUnit;
	}

	public void setAssignUnit(Integer assignUnit) {
		this.assignUnit = assignUnit;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Character getIsSendToCrApplicable() {
		return isSendToCrApplicable;
	}

	public void setIsSendToCrApplicable(Character isSendToCrApplicable) {
		this.isSendToCrApplicable = isSendToCrApplicable;
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

	public String getFromDt() {
		return fromDt;
	}

	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}

	public String getToDt() {
		return toDt;
	}

	public void setToDt(String toDt) {
		this.toDt = toDt;
	}

	public Integer getLabSampleId() {
		return labSampleId;
	}

	public void setLabSampleId(Integer labSampleId) {
		this.labSampleId = labSampleId;
	}

	public String getOutSourceLabName() {
		return outSourceLabName;
	}

	public void setOutSourceLabName(String outSourceLabName) {
		this.outSourceLabName = outSourceLabName;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public char getIsOutSource() {
		return isOutSource;
	}

	public void setIsOutSource(char isOutSource) {
		this.isOutSource = isOutSource;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Boolean getIsCountRequired() {
		return isCountRequired;
	}

	public void setIsCountRequired(Boolean isCountRequired) {
		this.isCountRequired = isCountRequired;
	}

	public List<obj> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<obj> departmentList) {
		this.departmentList = departmentList;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public List<obj> getIds() {
		return ids;
	}

	public void setIds(List<obj> ids) {
		this.ids = ids;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public List<Integer> getLabSampleDtlsIds() {
		return labSampleDtlsIds;
	}

	public void setLabSampleDtlsIds(List<Integer> labSampleDtlsIds) {
		this.labSampleDtlsIds = labSampleDtlsIds;
	}

	public Integer getMicrobiologyTemplateId() {
		return microbiologyTemplateId;
	}

	public void setMicrobiologyTemplateId(Integer microbiologyTemplateId) {
		this.microbiologyTemplateId = microbiologyTemplateId;
	}

	public Integer getReferralDocId() {
		return referralDocId;
	}

	public void setReferralDocId(Integer referralDocId) {
		this.referralDocId = referralDocId;
	}


	public String getReportReleaseDate() {
		return reportReleaseDate;
	}

	public void setReportReleaseDate(String reportReleaseDate) {
		this.reportReleaseDate = reportReleaseDate;
	}

    public boolean getIsOnHoldParameter() {
        return isOnHoldParameter;
    }

    public void setIsOnHoldParameter(boolean isOnHoldParameter) {
        this.isOnHoldParameter = isOnHoldParameter;
    }
	public String getSampleReception() {
		return sampleReception;
	}
	public void setSampleReception(String sampleReception) {
		this.sampleReception = sampleReception;
	}
	public String getSampleProcessing() {
		return sampleProcessing;
	}
	public void setSampleProcessing(String sampleProcessing) {
		this.sampleProcessing = sampleProcessing;
	}
	public String getSampleAnalysisAndDetection() {
		return sampleAnalysisAndDetection;
	}
	public void setSampleAnalysisAndDetection(String sampleAnalysisAndDetection) {
		this.sampleAnalysisAndDetection = sampleAnalysisAndDetection;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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
	public String getDocumentPath() {
		return documentPath;
	}
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
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
	public Integer getSerologyTestResultId() {
		return serologyTestResultId;
	}
	public void setSerologyTestResultId(Integer serologyTestResultId) {
		this.serologyTestResultId = serologyTestResultId;
	}
	public Integer getResultEnterBy() {
		return resultEnterBy;
	}
	public void setResultEnterBy(Integer resultEnterBy) {
		this.resultEnterBy = resultEnterBy;
	}
	public List<SearchDto> getSerologyResultTestsList() {
		return serologyResultTestsList;
	}
	public void setSerologyResultTestsList(List<SearchDto> serologyResultTestsList) {
		this.serologyResultTestsList = serologyResultTestsList;
	}
	public String getSampleRejectReason() {
		return sampleRejectReason;
	}
	public void setSampleRejectReason(String sampleRejectReason) {
		this.sampleRejectReason = sampleRejectReason;
	}
	public Integer getSampleRejectReasonId() {
		return sampleRejectReasonId;
	}
	public void setSampleRejectReasonId(Integer sampleRejectReasonId) {
		this.sampleRejectReasonId = sampleRejectReasonId;
	}
	public boolean getSampleRecollection() {
		return sampleRecollection;
	}
	public void setSampleRecollection(boolean sampleRecollection) {
		this.sampleRecollection = sampleRecollection;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getSubSpecialityMasterId() {
		return subSpecialityMasterId;
	}
	public void setSubSpecialityMasterId(Integer subSpecialityMasterId) {
		this.subSpecialityMasterId = subSpecialityMasterId;
	}
	public List<Integer> getSubDeptIds() {
		return subDeptIds;
	}
	public void setSubDeptIds(List<Integer> subDeptIds) {
		this.subDeptIds = subDeptIds;
	}
	public Integer getSeroDeptId() {
		return seroDeptId;
	}
	public void setSeroDeptId(Integer seroDeptId) {
		this.seroDeptId = seroDeptId;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public Boolean getPopupIconFlag() {
		return popupIconFlag;
	}
	public void setPopupIconFlag(Boolean popupIconFlag) {
		this.popupIconFlag = popupIconFlag;
	}

	public String getNewWhatsAppNo() {
		return newWhatsAppNo;
	}

	public void setNewWhatsAppNo(String newWhatsAppNo) {
		this.newWhatsAppNo = newWhatsAppNo;
	}

	public Boolean getSendOnlyWhatsApp() {
		return sendOnlyWhatsApp;
	}

	public void setSendOnlyWhatsApp(Boolean sendOnlyWhatsApp) {
		this.sendOnlyWhatsApp = sendOnlyWhatsApp;
	}

	public Boolean getCheckForIsPOC() {
		return checkForIsPOC;
	}

	public void setCheckForIsPOC(Boolean checkForIsPOC) {
		this.checkForIsPOC = checkForIsPOC;
	}

	public List<Integer> getPatientIds() {
		return patientIds;
	}

	public void setPatientIds(List<Integer> patientIds) {
		this.patientIds = patientIds;
	}

	public Character getIsClinicalInterventionRequired() {
		return isClinicalInterventionRequired;
	}

	public void setIsClinicalInterventionRequired(Character isClinicalInterventionRequired) {
		this.isClinicalInterventionRequired = isClinicalInterventionRequired;
	}

	public String getInterventionEndDate() {
		return interventionEndDate;
	}

	public void setInterventionEndDate(String interventionEndDate) {
		this.interventionEndDate = interventionEndDate;
	}

	public Integer getClinicalInterventionProcedureId() {
		return clinicalInterventionProcedureId;
	}

	public void setClinicalInterventionProcedureId(Integer clinicalInterventionProcedureId) {
		this.clinicalInterventionProcedureId = clinicalInterventionProcedureId;
	}

	public Integer getInterventionCompletedDoctorId() {
		return interventionCompletedDoctorId;
	}

	public void setInterventionCompletedDoctorId(Integer interventionCompletedDoctorId) {
		this.interventionCompletedDoctorId = interventionCompletedDoctorId;
	}
}
