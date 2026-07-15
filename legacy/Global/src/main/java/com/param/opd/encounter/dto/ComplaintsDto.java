package com.param.opd.encounter.dto;

public class ComplaintsDto {
	private Integer encounterId;

	private Integer patientId;
	
	private Integer complaintAppoId;
	
	private String diagnosisName;
	
	private String icpcCode;
	
	private String icpcCodeText;
	
	private String icpcSystemDesc;
	
	private String remark;
	
	private Integer diagnosisId;
	
	private String createdDate;
	
	private String since;
	
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getComplaintAppoId() {
		return complaintAppoId;
	}
	public void setComplaintAppoId(Integer complaintAppoId) {
		this.complaintAppoId = complaintAppoId;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public String getIcpcCode() {
		return icpcCode;
	}
	public void setIcpcCode(String icpcCode) {
		this.icpcCode = icpcCode;
	}
	public String getIcpcCodeText() {
		return icpcCodeText;
	}
	public void setIcpcCodeText(String icpcCodeText) {
		this.icpcCodeText = icpcCodeText;
	}
	public String getIcpcSystemDesc() {
		return icpcSystemDesc;
	}
	public void setIcpcSystemDesc(String icpcSystemDesc) {
		this.icpcSystemDesc = icpcSystemDesc;
	}
	public Integer getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getSince() {
		return since;
	}
	public void setSince(String since) {
		this.since = since;
	}
	
	

}

