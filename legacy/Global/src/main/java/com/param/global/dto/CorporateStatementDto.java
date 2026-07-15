package com.param.global.dto;

import java.util.Date;

import javax.persistence.Column;

public class CorporateStatementDto {

	
	private int corporateStatementId;
	
	private int visitTypeId;

	private String invoiceNumber;
	
	private int billId;
	
	private String referenceDate;
	
	private String firstName;
	
	private String fatherName;
	
	private String investigation;
	
	private String doneOnDate;
	
	private String consultant;
	
	private String orgName;

	private String disease;
	
	private String hospitalTreatment;
	
	private String visitfromDate;
	
	private String visittoDate;
	
	private String treatfromDate;
	
	private String treattoDate;
	
	private String consDate;
	
	private String employeeName;
	
	private int relationId;
	
	private int consAmount;
	
	private int labAmount;
	
	private int pharmacyAmount;
	
	private String address1;
	
	private String address2;
	
	private int letterType;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String hospitalName;
	
	private String medicalCardNo;
	private String authorizationLetterNo;
	private String relation;
	
	public int getCorporateStatementId() {
		return corporateStatementId;
	}

	public void setCorporateStatementId(int corporateStatementId) {
		this.corporateStatementId = corporateStatementId;
	}

	public int getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(int visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getInvestigation() {
		return investigation;
	}

	public void setInvestigation(String investigation) {
		this.investigation = investigation;
	}

	public String getConsultant() {
		return consultant;
	}

	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getHospitalTreatment() {
		return hospitalTreatment;
	}

	public void setHospitalTreatment(String hospitalTreatment) {
		this.hospitalTreatment = hospitalTreatment;
	}



	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public int getRelationId() {
		return relationId;
	}

	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}

	public int getConsAmount() {
		return consAmount;
	}

	public void setConsAmount(int consAmount) {
		this.consAmount = consAmount;
	}

	public int getLabAmount() {
		return labAmount;
	}

	public void setLabAmount(int labAmount) {
		this.labAmount = labAmount;
	}

	public int getPharmacyAmount() {
		return pharmacyAmount;
	}

	public void setPharmacyAmount(int pharmacyAmount) {
		this.pharmacyAmount = pharmacyAmount;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public int getLetterType() {
		return letterType;
	}

	public void setLetterType(int letterType) {
		this.letterType = letterType;
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


	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(String referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getDoneOnDate() {
		return doneOnDate;
	}

	public void setDoneOnDate(String doneOnDate) {
		this.doneOnDate = doneOnDate;
	}

	public String getVisitfromDate() {
		return visitfromDate;
	}

	public void setVisitfromDate(String visitfromDate) {
		this.visitfromDate = visitfromDate;
	}

	public String getVisittoDate() {
		return visittoDate;
	}

	public void setVisittoDate(String visittoDate) {
		this.visittoDate = visittoDate;
	}

	public String getTreatfromDate() {
		return treatfromDate;
	}

	public void setTreatfromDate(String treatfromDate) {
		this.treatfromDate = treatfromDate;
	}

	public String getTreattoDate() {
		return treattoDate;
	}

	public void setTreattoDate(String treattoDate) {
		this.treattoDate = treattoDate;
	}

	public String getConsDate() {
		return consDate;
	}

	public void setConsDate(String consDate) {
		this.consDate = consDate;
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

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getMedicalCardNo() {
		return medicalCardNo;
	}

	public void setMedicalCardNo(String medicalCardNo) {
		this.medicalCardNo = medicalCardNo;
	}

	public String getAuthorizationLetterNo() {
		return authorizationLetterNo;
	}

	public void setAuthorizationLetterNo(String authorizationLetterNo) {
		this.authorizationLetterNo = authorizationLetterNo;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}


	
	
	
}
