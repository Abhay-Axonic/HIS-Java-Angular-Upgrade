package com.param.opd.coversheet.dto;


import java.math.BigInteger;
import java.util.List;

public class CoversheetInstructionDto {

	private Integer instructionId;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer instructionDetailsId;
	
	private String instructionDesc;
	
	private String notes;
	
	private Integer patientId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Character status;
	
	private String advicedInstruction;
	
	private String doctorName;
	
	private String fromDate;

	private String toDate;

	private Boolean isOtims;
	
	private Integer activityOrIntrcs;
	private Integer visitTypeId;
	private Integer covTemplateId;
	private List<CoversheetInstructionDto> coversheetInstructionDtoslist; 
	private BigInteger adviceCount;

	private String userName;

	private Boolean editAdvice;

	private String createdDate1;

	public String getCreatedDate1() {
		return createdDate1;
	}

	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}

	public Boolean getEditAdvice() {
		return editAdvice;
	}

	public void setEditAdvice(Boolean editAdvice) {
		this.editAdvice = editAdvice;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public List<CoversheetInstructionDto> getCoversheetInstructionDtoslist() {
		return coversheetInstructionDtoslist;
	}

	public void setCoversheetInstructionDtoslist(List<CoversheetInstructionDto> coversheetInstructionDtoslist) {
		this.coversheetInstructionDtoslist = coversheetInstructionDtoslist;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getInstructionDetailsId() {
		return instructionDetailsId;
	}

	public void setInstructionDetailsId(Integer instructionDetailsId) {
		this.instructionDetailsId = instructionDetailsId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getAdvicedInstruction() {
		return advicedInstruction;
	}

	public void setAdvicedInstruction(String advicedInstruction) {
		this.advicedInstruction = advicedInstruction;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public Boolean getIsOtims() {
		return isOtims;
	}

	public void setIsOtims(Boolean isOtims) {
		this.isOtims = isOtims;
	}

	public Integer getActivityOrIntrcs() {
		return activityOrIntrcs;
	}

	public void setActivityOrIntrcs(Integer activityOrIntrcs) {
		this.activityOrIntrcs = activityOrIntrcs;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigInteger getAdviceCount() {
		return adviceCount;
	}

	public void setAdviceCount(BigInteger adviceCount) {
		this.adviceCount = adviceCount;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	
}
