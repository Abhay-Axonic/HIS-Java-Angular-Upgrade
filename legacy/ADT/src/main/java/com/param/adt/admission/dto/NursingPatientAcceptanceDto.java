package com.param.adt.admission.dto;

import java.util.List;

public class NursingPatientAcceptanceDto {

	private Integer admissionId;
	
	private Integer admissionDetailsId;
	
	private Integer patientId;
	
	private String admissionNumber;
	
	private Integer visitTypeId;
	
	private Integer doctorId;
	
	private Integer specialityId;
	
	private String specialityName;
	
	private String uhidNumber;
	
	private String patientName;
	
	private String doctorName;
	
	private Integer genderId;
	
	private String genderCode;
	
	private String dob;
	
	private String doa;
	
	private String pdd;
	
	private Integer paymentEntitlementId;
	
	private Integer billingBedCategoryId;
	
	private Integer bedCategoryId;
	
	private String paymentEntitlementDesc;
	
	private String bedCategoryDesc;
	
	private Integer payeeId;
	
	private Integer contractId;
	
	private Integer floorId;
	
	private String bedNumber;
	
	private Integer bedId;
	
	private Integer wardId;
	
	private String wardName;
	
	private Character isShortLeave;
	
	private Integer patientCategoryId;
	
	private Integer dischargeId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private List<Integer> wardList;
	
	private List<Integer> bedList;
	
	private Character isShowPatientByBed;
	
	private Integer dischargeStatusId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private Character isFromNursingAcceptancePage;

	private Character isVip;
	
	private Integer isCriticalOrder;
	
	private Integer encounterId;
	
	private Integer admissionNoteVisitType;
	private String prefixDesc;
	private String imagePath;
	private String ageStr;
	private String floorName;
	private Integer bucketFlag;
	
	
	
	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}

	public Character getIsVip() {
		return isVip;
	}

	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}

	public Integer getIsCriticalOrder() {
		return isCriticalOrder;
	}

	public void setIsCriticalOrder(Integer isCriticalOrder) {
		this.isCriticalOrder = isCriticalOrder;
	}

	public List<Integer> getWardList() {
		return wardList;
	}

	public void setWardList(List<Integer> wardList) {
		this.wardList = wardList;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getAdmissionDetailsId() {
		return admissionDetailsId;
	}

	public void setAdmissionDetailsId(Integer admissionDetailsId) {
		this.admissionDetailsId = admissionDetailsId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getDoa() {
		return doa;
	}

	public void setDoa(String doa) {
		this.doa = doa;
	}

	public String getPdd() {
		return pdd;
	}

	public void setPdd(String pdd) {
		this.pdd = pdd;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	public String getBedCategoryDesc() {
		return bedCategoryDesc;
	}

	public void setBedCategoryDesc(String bedCategoryDesc) {
		this.bedCategoryDesc = bedCategoryDesc;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Character getIsShortLeave() {
		return isShortLeave;
	}

	public void setIsShortLeave(Character isShortLeave) {
		this.isShortLeave = isShortLeave;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getDischargeId() {
		return dischargeId;
	}

	public void setDischargeId(Integer dischargeId) {
		this.dischargeId = dischargeId;
	}

	public List<Integer> getBedList() {
		return bedList;
	}

	public void setBedList(List<Integer> bedList) {
		this.bedList = bedList;
	}

	public Character getIsShowPatientByBed() {
		return isShowPatientByBed;
	}

	public void setIsShowPatientByBed(Character isShowPatientByBed) {
		this.isShowPatientByBed = isShowPatientByBed;
	}

	public Integer getDischargeStatusId() {
		return dischargeStatusId;
	}

	public void setDischargeStatusId(Integer dischargeStatusId) {
		this.dischargeStatusId = dischargeStatusId;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Character getIsFromNursingAcceptancePage() {
		return isFromNursingAcceptancePage;
	}

	public void setIsFromNursingAcceptancePage(Character isFromNursingAcceptancePage) {
		this.isFromNursingAcceptancePage = isFromNursingAcceptancePage;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionNoteVisitType() {
		return admissionNoteVisitType;
	}

	public void setAdmissionNoteVisitType(Integer admissionNoteVisitType) {
		this.admissionNoteVisitType = admissionNoteVisitType;
	}

	public String getPrefixDesc() {
		return prefixDesc;
	}

	public void setPrefixDesc(String prefixDesc) {
		this.prefixDesc = prefixDesc;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getAgeStr() {
		return ageStr;
	}

	public void setAgeStr(String ageStr) {
		this.ageStr = ageStr;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	
	
	
	
	
}
