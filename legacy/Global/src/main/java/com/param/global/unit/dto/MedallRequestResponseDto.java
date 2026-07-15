package com.param.global.unit.dto;

import java.util.List;

public class MedallRequestResponseDto {
	private Integer  medallRequestId;
	
	private Integer medallBillId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer patientId;
	
	private String requestedPayload;
	
	private String returnedResponse;
	
	private Character status;
	
	private Integer organizationId;
	
	private Integer unitId;

	private String addedDate;
	
	private Integer addedBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	private Integer orderId;
	private String sampleNo;
	private String parameterName;
	private String parameterUnit;
	private String parameterValue;
	private Double minValue;
	private Double maxValue;
	private Double highValue;
	private Double lowValue;
	private String paramResult;
	private String note;
	private String machineName;
	private String resultDateTime;
	private Integer orderDetailsId;
	private Integer serviceId;
	private Integer corpbillingid;
	private Integer medallServiceId;
	private String serviceStandardName;
	private String objectString;
	private List<MedallRequestResponseDto> testDetailsList;
	private List<MedallRequestResponseDto> orderDetailsListById;

	public Integer getMedallRequestId() {
		return medallRequestId;
	}

	public void setMedallRequestId(Integer medallRequestId) {
		this.medallRequestId = medallRequestId;
	}

	public Integer getMedallBillId() {
		return medallBillId;
	}

	public void setMedallBillId(Integer medallBillId) {
		this.medallBillId = medallBillId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
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

	public String getRequestedPayload() {
		return requestedPayload;
	}

	public void setRequestedPayload(String requestedPayload) {
		this.requestedPayload = requestedPayload;
	}

	public String getReturnedResponse() {
		return returnedResponse;
	}

	public void setReturnedResponse(String returnedResponse) {
		this.returnedResponse = returnedResponse;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterUnit() {
		return parameterUnit;
	}

	public void setParameterUnit(String parameterUnit) {
		this.parameterUnit = parameterUnit;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getHighValue() {
		return highValue;
	}

	public void setHighValue(Double highValue) {
		this.highValue = highValue;
	}

	public Double getLowValue() {
		return lowValue;
	}

	public void setLowValue(Double lowValue) {
		this.lowValue = lowValue;
	}

	public String getParamResult() {
		return paramResult;
	}

	public void setParamResult(String paramResult) {
		this.paramResult = paramResult;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getResultDateTime() {
		return resultDateTime;
	}

	public void setResultDateTime(String resultDateTime) {
		this.resultDateTime = resultDateTime;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public List<MedallRequestResponseDto> getTestDetailsList() {
		return testDetailsList;
	}

	public void setTestDetailsList(List<MedallRequestResponseDto> testDetailsList) {
		this.testDetailsList = testDetailsList;
	}

	public String getObjectString() {
		return objectString;
	}

	public void setObjectString(String objectString) {
		this.objectString = objectString;
	}

	public Integer getCorpbillingid() {
		return corpbillingid;
	}

	public void setCorpbillingid(Integer corpbillingid) {
		this.corpbillingid = corpbillingid;
	}

	public Integer getMedallServiceId() {
		return medallServiceId;
	}

	public void setMedallServiceId(Integer medallServiceId) {
		this.medallServiceId = medallServiceId;
	}

	public List<MedallRequestResponseDto> getOrderDetailsListById() {
		return orderDetailsListById;
	}

	public void setOrderDetailsListById(List<MedallRequestResponseDto> orderDetailsListById) {
		this.orderDetailsListById = orderDetailsListById;
	}
	
}
