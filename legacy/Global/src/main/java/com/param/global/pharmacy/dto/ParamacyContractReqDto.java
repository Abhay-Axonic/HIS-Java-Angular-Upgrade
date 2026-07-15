package com.param.global.pharmacy.dto;

import java.util.List;

import com.param.global.dto.PatientSponsorDetailsDto;

public class ParamacyContractReqDto {
	
	Integer selfPayeeId;
	Integer contractId;
	Integer patientId;
	Integer encounterId;
	Integer admissionId;
	Integer orgId;
	Integer unitId;
	WalkInSalesRequest request;
	boolean taxInclusive;
	List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList;
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public WalkInSalesRequest getRequest() {
		return request;
	}
	public void setRequest(WalkInSalesRequest request) {
		this.request = request;
	}
	public List<PatientSponsorDetailsDto> getPatientSponsorDetailsDtosList() {
		return patientSponsorDetailsDtosList;
	}
	public void setPatientSponsorDetailsDtosList(List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList) {
		this.patientSponsorDetailsDtosList = patientSponsorDetailsDtosList;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public boolean isTaxInclusive() {
		return taxInclusive;
	}
	public void setTaxInclusive(boolean taxInclusive) {
		this.taxInclusive = taxInclusive;
	}
	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}
	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}


}
