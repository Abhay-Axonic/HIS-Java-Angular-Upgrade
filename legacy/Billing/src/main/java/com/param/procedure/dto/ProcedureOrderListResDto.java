package com.param.procedure.dto;

public class ProcedureOrderListResDto {
	
	private Integer patientId;
	private Integer admissionId;
	private Integer procedureOrderId;
	private String uhidNumber;
	private String patientName;
	private String preferedDateTime;
	private Integer procedureTypeId;
	private String procedureTypeName;
	private String serviceName;
	private String procedureStatus;
	private Integer proReqId;
	public Integer getProReqId() {
		return proReqId;
	}
	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}
	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPreferedDateTime() {
		return preferedDateTime;
	}
	public void setPreferedDateTime(String preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}
	public String getProcedureTypeName() {
		return procedureTypeName;
	}
	public void setProcedureTypeName(String procedureTypeName) {
		this.procedureTypeName = procedureTypeName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getProcedureStatus() {
		return procedureStatus;
	}
	public void setProcedureStatus(String procedureStatus) {
		this.procedureStatus = procedureStatus;
	}
	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}
	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}
	

}
