package com.param.ipd.dto;

public class ComplaintAdmissionDetailsDto {

	private Integer diagnosisId;
	private String complaintsDateTime;
	private String remark;
	private char isReviewedFlag;
	private Integer complaintAdmissionId;
	private Integer since;
	private String duration;
	private String diagnosisName;
	private Integer icpcSystemId;
	private String icpcSystemDesc;

	private String systemName;
	private String icpcCodeText;
	private String resolvedRemark;
	
	private Character isResolved;
	
	private String resolvedDate;
	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getComplaintsDateTime() {
		return complaintsDateTime;
	}

	public void setComplaintsDateTime(String complaintsDateTime) {
		this.complaintsDateTime = complaintsDateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}

	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}

	public Integer getComplaintAdmissionId() {
		return complaintAdmissionId;
	}

	public void setComplaintAdmissionId(Integer complaintAdmissionId) {
		this.complaintAdmissionId = complaintAdmissionId;
	}

	public Integer getSince() {
		return since;
	}

	public void setSince(Integer since) {
		this.since = since;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}

	public String getIcpcSystemDesc() {
		return icpcSystemDesc;
	}

	public void setIcpcSystemDesc(String icpcSystemDesc) {
		this.icpcSystemDesc = icpcSystemDesc;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getIcpcCodeText() {
		return icpcCodeText;
	}

	public void setIcpcCodeText(String icpcCodeText) {
		this.icpcCodeText = icpcCodeText;
	}

	public String getResolvedRemark() {
		return resolvedRemark;
	}

	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
	}

	public Character getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(Character isResolved) {
		this.isResolved = isResolved;
	}

	public String getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}
	

}
