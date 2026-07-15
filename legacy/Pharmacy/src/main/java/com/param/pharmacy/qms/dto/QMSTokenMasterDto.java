package com.param.pharmacy.qms.dto;


public class QMSTokenMasterDto {
	
	private Integer qmsTokenMasterId ;
	
	private Integer previousQMSTokenMasterId ;
	
	private Integer qmsKioskMasterId;
	
	private Integer qmsCounterMasterId;
	
	private Integer qmsDeptMasterId;
	
	private Integer tokenNumber;
	
	private Integer dummyTokenNumber ;
	
	private Integer callCount ;
	
	private String startLetter;
	
	private String startNumber;
	
	private String tokenNumberWithStartLetter;
	
	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Integer organisationId;
	
	private Integer unitId;
	
	private String callTime;
	
	private String servedTime;
	
	private Character isCancel;
	
	private Character isCall;//C=call,recall,nextCall, S=served ,D=cancel
	
	private Character isTransfer;
	
	private Integer callLogStatus;
	
	private Integer qmsTokenCallLogId;
	
	private Integer assignedDeptId;
	
	private Integer srNo;
	
	
	private String qmsCallMassage;
	
	private Integer broadcastQmsMsgId;
	
	private String qmsCounterDesc;
	
	public Integer getQmsTokenMasterId() {
		return qmsTokenMasterId;
	}

	public void setQmsTokenMasterId(Integer qmsTokenMasterId) {
		this.qmsTokenMasterId = qmsTokenMasterId;
	}

	public Integer getQmsKioskMasterId() {
		return qmsKioskMasterId;
	}

	public void setQmsKioskMasterId(Integer qmsKioskMasterId) {
		this.qmsKioskMasterId = qmsKioskMasterId;
	}

	public Integer getQmsCounterMasterId() {
		return qmsCounterMasterId;
	}

	public void setQmsCounterMasterId(Integer qmsCounterMasterId) {
		this.qmsCounterMasterId = qmsCounterMasterId;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
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

	public String getStartLetter() {
		return startLetter;
	}

	public void setStartLetter(String startLetter) {
		this.startLetter = startLetter;
	}

	public Integer getQmsDeptMasterId() {
		return qmsDeptMasterId;
	}

	public void setQmsDeptMasterId(Integer qmsDeptMasterId) {
		this.qmsDeptMasterId = qmsDeptMasterId;
	}

	public String getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(String startNumber) {
		this.startNumber = startNumber;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getServedTime() {
		return servedTime;
	}

	public void setServedTime(String servedTime) {
		this.servedTime = servedTime;
	}

	public Character getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Character isCancel) {
		this.isCancel = isCancel;
	}

	public Character getIsCall() {
		return isCall;
	}

	public void setIsCall(Character isCall) {
		this.isCall = isCall;
	}

	public String getTokenNumberWithStartLetter() {
		return tokenNumberWithStartLetter;
	}

	public void setTokenNumberWithStartLetter(String tokenNumberWithStartLetter) {
		this.tokenNumberWithStartLetter = tokenNumberWithStartLetter;
	}

	public Character getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(Character isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Integer getCallLogStatus() {
		return callLogStatus;
	}

	public void setCallLogStatus(Integer callLogStatus) {
		this.callLogStatus = callLogStatus;
	}

	public Integer getQmsTokenCallLogId() {
		return qmsTokenCallLogId;
	}

	public void setQmsTokenCallLogId(Integer qmsTokenCallLogId) {
		this.qmsTokenCallLogId = qmsTokenCallLogId;
	}

	public Integer getDummyTokenNumber() {
		return dummyTokenNumber;
	}

	public void setDummyTokenNumber(Integer dummyTokenNumber) {
		this.dummyTokenNumber = dummyTokenNumber;
	}

	public Integer getCallCount() {
		return callCount;
	}

	public void setCallCount(Integer callCount) {
		this.callCount = callCount;
	}

	public Integer getAssignedDeptId() {
		return assignedDeptId;
	}

	public void setAssignedDeptId(Integer assignedDeptId) {
		this.assignedDeptId = assignedDeptId;
	}

	public String getQmsCallMassage() {
		return qmsCallMassage;
	}

	public void setQmsCallMassage(String qmsCallMassage) {
		this.qmsCallMassage = qmsCallMassage;
	}

	public Integer getBroadcastQmsMsgId() {
		return broadcastQmsMsgId;
	}

	public void setBroadcastQmsMsgId(Integer broadcastQmsMsgId) {
		this.broadcastQmsMsgId = broadcastQmsMsgId;
	}

	public Integer getPreviousQMSTokenMasterId() {
		return previousQMSTokenMasterId;
	}

	public void setPreviousQMSTokenMasterId(Integer previousQMSTokenMasterId) {
		this.previousQMSTokenMasterId = previousQMSTokenMasterId;
	}

	public Integer getSrNo() {
		return srNo;
	}

	public void setSrNo(Integer srNo) {
		this.srNo = srNo;
	}

	public String getQmsCounterDesc() {
		return qmsCounterDesc;
	}

	public void setQmsCounterDesc(String qmsCounterDesc) {
		this.qmsCounterDesc = qmsCounterDesc;
	}	
	
}
