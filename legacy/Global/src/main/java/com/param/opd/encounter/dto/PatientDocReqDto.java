package com.param.opd.encounter.dto;

public class PatientDocReqDto {
	private Integer encounterId;

	private Integer admissionId;

	private Integer patientId;

	private Integer unitId;

	private Integer organisationId;

	private Integer userId;

	private Integer preauthRequestId;

	private Integer visitTypeId;
	
	private Integer expenseId;

	private Integer docTypeId;
	
	private Integer labSampleDtlsId;
	
	private Integer grnId;
	
	private Integer poId;
	
	private Integer incidentReportingMapperId;
	
	private Integer companyReceiptsExpenseId;
	
	private Integer deathPatientId;
	
	private Integer opbId;
	private Integer bucketFlag;
	
	private Integer encounterDocId;
	
	private Integer nabhReportingMapperId;
	
	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}

	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
	}

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPreauthRequestId() {
		return preauthRequestId;
	}

	public void setPreauthRequestId(Integer preauthRequestId) {
		this.preauthRequestId = preauthRequestId;
	}

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getIncidentReportingMapperId() {
		return incidentReportingMapperId;
	}

	public void setIncidentReportingMapperId(Integer incidentReportingMapperId) {
		this.incidentReportingMapperId = incidentReportingMapperId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getDeathPatientId() {
		return deathPatientId;
	}

	public void setDeathPatientId(Integer deathPatientId) {
		this.deathPatientId = deathPatientId;
	}
	
	public Integer getOpbId() {
		return opbId;
	}

	public void setOpbId(Integer opbId) {
		this.opbId = opbId;
	}
/*------------------------------------------------- Vendor id for upload document in vendor master ----------------------------------------------*/	
	private Integer vendorId;
	
	public Integer getVendorId(){
		return vendorId;
	}
	
	public void setVendorId(Integer vendorId){
		this.vendorId = vendorId;
	}

	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}

	public Integer getEncounterDocId() {
		return encounterDocId;
	}

	public void setEncounterDocId(Integer encounterDocId) {
		this.encounterDocId = encounterDocId;
	}

	public Integer getNabhReportingMapperId() {
		return nabhReportingMapperId;
	}

	public void setNabhReportingMapperId(Integer nabhReportingMapperId) {
		this.nabhReportingMapperId = nabhReportingMapperId;
	}
	
	
}
