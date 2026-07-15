package com.param.lis.microbiology.transaction.dto;

import java.util.List;

public class SensitivityTestResultMasterDto {

	private Integer sensitivityResultId;
	private Integer orgId;
	private Integer orgUnitId;
	private Character checkTest;
	private Integer organismId;
	private Integer createdBy;
	private Long createdDate;
	private Integer updatedBy;
	private Long updatedDate;
	private Character isDeleted;
	private Integer labSampleDtlsId;
	private Integer reportReleaseBy;
	private Integer orderDetailsId;
	private List<SensitivityTestResultDetailsMasterDto> listSensitivityTestResultDetailsMaster;
	
	private Integer doctorId;
	private Integer patientId;
	private String  testDesc;
	
	 private Boolean triggerReleaseMail;
	 
	 private Boolean amend;

	public Integer getSensitivityResultId() {
		return sensitivityResultId;
	}

	public void setSensitivityResultId(Integer sensitivityResultId) {
		this.sensitivityResultId = sensitivityResultId;
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

	public Character getCheckTest() {
		return checkTest;
	}

	public void setCheckTest(Character checkTest) {
		this.checkTest = checkTest;
	}

	public Integer getOrganismId() {
		return organismId;
	}

	public void setOrganismId(Integer organismId) {
		this.organismId = organismId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public List<SensitivityTestResultDetailsMasterDto> getListSensitivityTestResultDetailsMaster() {
		return listSensitivityTestResultDetailsMaster;
	}

	public void setListSensitivityTestResultDetailsMaster(
			List<SensitivityTestResultDetailsMasterDto> listSensitivityTestResultDetailsMaster) {
		this.listSensitivityTestResultDetailsMaster = listSensitivityTestResultDetailsMaster;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getReportReleaseBy() {
		return reportReleaseBy;
	}

	public void setReportReleaseBy(Integer reportReleaseBy) {
		this.reportReleaseBy = reportReleaseBy;
	}

	public Boolean getTriggerReleaseMail() {
		return triggerReleaseMail;
	}

	public void setTriggerReleaseMail(Boolean triggerReleaseMail) {
		this.triggerReleaseMail = triggerReleaseMail;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Boolean getAmend() {
		return amend;
	}

	public void setAmend(Boolean amend) {
		this.amend = amend;
	}

}
