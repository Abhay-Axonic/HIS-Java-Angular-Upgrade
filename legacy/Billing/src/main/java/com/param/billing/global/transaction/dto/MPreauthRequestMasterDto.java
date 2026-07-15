package com.param.billing.global.transaction.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

public class MPreauthRequestMasterDto {
	private Integer preauthRequestId;
	private Integer patientId;
	private Integer visitType;
	private Integer visitAdmId;
	private Integer payeeId;
	private String requestNo;
	private Integer requestTypeId;
	private Long requesDate;
	private String requesDateString;
	private Integer requestSendBy;
	private Integer requestSendThrough;
	private Double requestAmount;
	private Double authorizationAmount;
	private String authorizationNo;
	private Long authorizationDate;
	private Integer receivedThrough;
	private String requestRemark;
	private String authRemark;
	private Integer receivedBy;
	private Integer patientSponsorId;
	private Integer unitId;
	private Integer organisationId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Long updatedDate;
	private Integer updatedBy;
	private String requestDate;
	private String authDate;
	private String authDateString;
	private String requestDateString;
	private Date requestDateNew;
	private Date authDateNew;
	private String recivedThroughDesc;
	private String requestTypeDesc;
	private String sendThroughDesc;
	private Integer receivedby;
	private String receivedThroughId;
	private String patientName;
	private String uhidNumber;
	private String genderCode;
	private String genderDec;
	private Integer authStatusId;
	private Character isAuthDeleted;
	private Integer authorizationStatus;
	private String  authRequestSendThroughId;
	private Character isFinalSubmit;
	private String receivedByName;
	private Character isSplitStatus;
	private Integer contractId;
	private String  authorizationStatusName;
	private String  authRequestSendThroughName;
	private String  authorizationReceivedBy; //login user details means authorizationReceivedBy
	private String  requestSendThroughName;
	private String  requestSendByName;
	private String  requestTypeName;
	private List<Integer> preAuthReqIds;
    private Integer admissionId;
	private Integer encounterId;
	private String companyDesc;
	private Integer docTypeId;
	private String documentName;
	
	
	
	
	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPreauthRequestId() {
		return preauthRequestId;
	}

	public void setPreauthRequestId(Integer preauthRequestId) {
		this.preauthRequestId = preauthRequestId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}

	public Integer getVisitAdmId() {
		return visitAdmId;
	}

	public void setVisitAdmId(Integer visitAdmId) {
		this.visitAdmId = visitAdmId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Long getRequesDate() {
		return requesDate;
	}

	public void setRequesDate(Long requesDate) {
		this.requesDate = requesDate;
	}

	public Integer getRequestSendBy() {
		return requestSendBy;
	}

	public void setRequestSendBy(Integer requestSendBy) {
		this.requestSendBy = requestSendBy;
	}

	public Integer getRequestSendThrough() {
		return requestSendThrough;
	}

	public void setRequestSendThrough(Integer requestSendThrough) {
		this.requestSendThrough = requestSendThrough;
	}

	public Double getRequestAmount() {
		return requestAmount;
	}

	public void setRequestAmount(Double requestAmount) {
		this.requestAmount = requestAmount;
	}

	public Double getAuthorizationAmount() {
		return authorizationAmount;
	}

	public void setAuthorizationAmount(Double authorizationAmount) {
		this.authorizationAmount = authorizationAmount;
	}

	public String getAuthorizationNo() {
		return authorizationNo;
	}

	public void setAuthorizationNo(String authorizationNo) {
		this.authorizationNo = authorizationNo;
	}

	public Long getAuthorizationDate() {
		return authorizationDate;
	}

	public void setAuthorizationDate(Long authorizationDate) {
		this.authorizationDate = authorizationDate;
	}

	public Integer getReceivedThrough() {
		return receivedThrough;
	}

	public void setReceivedThrough(Integer receivedThrough) {
		this.receivedThrough = receivedThrough;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}

	public String getAuthRemark() {
		return authRemark;
	}

	public void setAuthRemark(String authRemark) {
		this.authRemark = authRemark;
	}

	public Integer getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(Integer receivedBy) {
		this.receivedBy = receivedBy;
	}

	public Integer getPatientSponsorId() {
		return patientSponsorId;
	}

	public void setPatientSponsorId(Integer patientSponsorId) {
		this.patientSponsorId = patientSponsorId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getAuthDate() {
		return authDate;
	}

	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}

	public String getRecivedThroughDesc() {
		return recivedThroughDesc;
	}

	public void setRecivedThroughDesc(String recivedThroughDesc) {
		this.recivedThroughDesc = recivedThroughDesc;
	}

	public String getRequestTypeDesc() {
		return requestTypeDesc;
	}

	
	public Integer getReceivedby() {
		return receivedby;
	}

	public void setReceivedby(Integer receivedby) {
		this.receivedby = receivedby;
	}

	public void setRequestTypeDesc(String requestTypeDesc) {
		this.requestTypeDesc = requestTypeDesc;
	}

	public String getSendThroughDesc() {
		return sendThroughDesc;
	}

	public void setSendThroughDesc(String sendThroughDesc) {
		this.sendThroughDesc = sendThroughDesc;
	}

	public String getReceivedThroughId() {
		return receivedThroughId;
	}

	public void setReceivedThroughId(String receivedThroughId) {
		this.receivedThroughId = receivedThroughId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getGenderDec() {
		return genderDec;
	}

	public void setGenderDec(String genderDec) {
		this.genderDec = genderDec;
	}

	public Integer getAuthStatusId() {
		return authStatusId;
	}

	public void setAuthStatusId(Integer authStatusId) {
		this.authStatusId = authStatusId;
	}

	public Character getIsAuthDeleted() {
		return isAuthDeleted;
	}

	public void setIsAuthDeleted(Character isAuthDeleted) {
		this.isAuthDeleted = isAuthDeleted;
	}

	public String getAuthorizationStatusName() {
		return authorizationStatusName;
	}

	public void setAuthorizationStatusName(String authorizationStatusName) {
		this.authorizationStatusName = authorizationStatusName;
	}

	public String getAuthRequestSendThroughName() {
		return authRequestSendThroughName;
	}

	public void setAuthRequestSendThroughName(String authRequestSendThroughName) {
		this.authRequestSendThroughName = authRequestSendThroughName;
	}

	public String getAuthorizationReceivedBy() {
		return authorizationReceivedBy;
	}

	public void setAuthorizationReceivedBy(String authorizationReceivedBy) {
		this.authorizationReceivedBy = authorizationReceivedBy;
	}

	public String getRequestSendThroughName() {
		return requestSendThroughName;
	}

	public void setRequestSendThroughName(String requestSendThroughName) {
		this.requestSendThroughName = requestSendThroughName;
	}

	public String getRequestSendByName() {
		return requestSendByName;
	}

	public void setRequestSendByName(String requestSendByName) {
		this.requestSendByName = requestSendByName;
	}

	public String getRequestTypeName() {
		return requestTypeName;
	}

	public void setRequestTypeName(String requestTypeName) {
		this.requestTypeName = requestTypeName;
	}

	public Integer getAuthorizationStatus() {
		return authorizationStatus;
	}

	public void setAuthorizationStatus(Integer authorizationStatus) {
		this.authorizationStatus = authorizationStatus;
	}

	public String getAuthRequestSendThroughId() {
		return authRequestSendThroughId;
	}

	public void setAuthRequestSendThroughId(String authRequestSendThroughId) {
		this.authRequestSendThroughId = authRequestSendThroughId;
	}

	public Character getIsFinalSubmit() {
		return isFinalSubmit;
	}

	public void setIsFinalSubmit(Character isFinalSubmit) {
		this.isFinalSubmit = isFinalSubmit;
	}

	public String getReceivedByName() {
		return receivedByName;
	}

	public void setReceivedByName(String receivedByName) {
		this.receivedByName = receivedByName;
	}

	public Character getIsSplitStatus() {
		return isSplitStatus;
	}

	public void setIsSplitStatus(Character isSplitStatus) {
		this.isSplitStatus = isSplitStatus;
	}

	public List<Integer> getPreAuthReqIds() {
		return preAuthReqIds;
	}

	public void setPreAuthReqIds(List<Integer> preAuthReqIds) {
		this.preAuthReqIds = preAuthReqIds;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Date getRequestDateNew() {
		return requestDateNew;
	}

	public void setRequestDateNew(Date requestDateNew) {
		this.requestDateNew = requestDateNew;
	}

	public Date getAuthDateNew() {
		return authDateNew;
	}

	public void setAuthDateNew(Date authDateNew) {
		this.authDateNew = authDateNew;
	}

	public String getRequesDateString() {
		return requesDateString;
	}

	public void setRequesDateString(String requesDateString) {
		this.requesDateString = requesDateString;
	}

	public String getAuthDateString() {
		return authDateString;
	}

	public void setAuthDateString(String authDateString) {
		this.authDateString = authDateString;
	}

	public String getRequestDateString() {
		return requestDateString;
	}

	public void setRequestDateString(String requestDateString) {
		this.requestDateString = requestDateString;
	}

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}



}
