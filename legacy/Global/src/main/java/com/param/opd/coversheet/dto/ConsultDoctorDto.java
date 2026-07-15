package com.param.opd.coversheet.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

public class ConsultDoctorDto {

	private Integer consultDoctorId;

	private Integer organizationId;

	private Integer unitId;
	
	private Integer organizationIdp;

	private Integer unitIdp;
	
	private Integer defaultSelfTariffId;

	private Integer refByDoctorId;
	
	private Integer refBySpecialityId;

	private String refByDoctor;

	private Integer refToDoctorId;

	private String refToDoctor;

	private Integer refToSpecialityId;

	private String specialityName;

	private Integer patientId;

	private String patientName;

	private Integer encounterId;

	private String notes;

	private Character status;

	private String createdDate;
	
	private Date createdDateNew;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private String uhidNumber;
	
	private String priority;
	
	private String referredStatus;

	private Integer admissionId;
	
	private Integer visitTypeId;

	private String visitTypeCode;
	
	private Integer bedId;

	private String bedNumber;

	private Integer wardId;
	
	private String createdTime;
	
	private String wardName;
	
	private String unitName;
	
	private Integer doctorId;
	
	private Integer referredUnitId;

	private String mpshahPatientId;// Ganesh
	
	private Character isRMO;
	
	private Character isVip;
	
	private Integer isCriticalOrder;
	
//	private Integer bedCategoryId;
	
	private String bedCategoryDecs;
	
	private Integer billingBedCategoryid;
	
	private Integer paymentEntitlementId;
	
	private String dob;
	
	private Integer genderId;
	
	private Integer bedCategoryId;
	
	private Integer patientCategoryId;
	
	private Integer prefixId;
	
	private String genderCode;
	private String doa;
	private String pdd;
	private String age;
	private String fromDate;
	private String toDate;
	private String userName;
	private Integer outsideHospitalId;
	private String outsideHospitalDesc;
	private Integer outsideHosDocId;
	private String outsideHospitalDoctorName;
	private Integer covTemplateId;
	private String imagePath;

	
   private Integer noOfRecordsPerPage;
	
	private Integer  offset;
	private Character isFinalBill;

	private Integer patientWorkbenchId;
	private Integer resultId;
	private Date workbenchAddedDate;
	private String workbenchRemark;
	
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

	private List<Integer> doctorList;

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getRefByDoctor() {
		return refByDoctor;
	}

	public void setRefByDoctor(String refByDoctor) {
		this.refByDoctor = refByDoctor;
	}

	public String getRefToDoctor() {
		return refToDoctor;
	}

	public void setRefToDoctor(String refToDoctor) {
		this.refToDoctor = refToDoctor;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getConsultDoctorId() {
		return consultDoctorId;
	}

	public void setConsultDoctorId(Integer consultDoctorId) {
		this.consultDoctorId = consultDoctorId;
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

	public Integer getRefByDoctorId() {
		return refByDoctorId;
	}

	public void setRefByDoctorId(Integer refByDoctorId) {
		this.refByDoctorId = refByDoctorId;
	}

	public Integer getRefToDoctorId() {
		return refToDoctorId;
	}

	public void setRefToDoctorId(Integer refToDoctorId) {
		this.refToDoctorId = refToDoctorId;
	}

	public Integer getRefToSpecialityId() {
		return refToSpecialityId;
	}

	public void setRefToSpecialityId(Integer refToSpecialityId) {
		this.refToSpecialityId = refToSpecialityId;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getMpshahPatientId() {
		return mpshahPatientId;
	}

	public void setMpshahPatientId(String mpshahPatientId) {
		this.mpshahPatientId = mpshahPatientId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public Integer getRefBySpecialityId() {
		return refBySpecialityId;
	}

	public void setRefBySpecialityId(Integer refBySpecialityId) {
		this.refBySpecialityId = refBySpecialityId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getVisitTypeCode() {
		return visitTypeCode;
	}

	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getReferredStatus() {
		return referredStatus;
	}

	public void setReferredStatus(String referredStatus) {
		this.referredStatus = referredStatus;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getReferredUnitId() {
		return referredUnitId;
	}

	public void setReferredUnitId(Integer referredUnitId) {
		this.referredUnitId = referredUnitId;
	}
	public Character getIsRMO() {
		return isRMO;
	}

	public void setIsRMO(Character isRMO) {
		this.isRMO = isRMO;
	}

	public List<Integer> getDoctorList() {
		return doctorList;
	}

	public void setDoctorList(List<Integer> doctorList) {
		this.doctorList = doctorList;
	}

/*	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}
*/
	public String getBedCategoryDecs() {
		return bedCategoryDecs;
	}

	public void setBedCategoryDecs(String bedCategoryDecs) {
		this.bedCategoryDecs = bedCategoryDecs;
	}

	public Integer getBillingBedCategoryid() {
		return billingBedCategoryid;
	}

	public void setBillingBedCategoryid(Integer billingBedCategoryid) {
		this.billingBedCategoryid = billingBedCategoryid;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getOrganizationIdp() {
		return organizationIdp;
	}

	public void setOrganizationIdp(Integer organizationIdp) {
		this.organizationIdp = organizationIdp;
	}

	public Integer getUnitIdp() {
		return unitIdp;
	}

	public void setUnitIdp(Integer unitIdp) {
		this.unitIdp = unitIdp;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getOutsideHospitalId() {
		return outsideHospitalId;
	}

	public void setOutsideHospitalId(Integer outsideHospitalId) {
		this.outsideHospitalId = outsideHospitalId;
	}

	public String getOutsideHospitalDesc() {
		return outsideHospitalDesc;
	}

	public void setOutsideHospitalDesc(String outsideHospitalDesc) {
		this.outsideHospitalDesc = outsideHospitalDesc;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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

	public Integer getOutsideHosDocId() {
		return outsideHosDocId;
	}

	public void setOutsideHosDocId(Integer outsideHosDocId) {
		this.outsideHosDocId = outsideHosDocId;
	}

	public String getOutsideHospitalDoctorName() {
		return outsideHospitalDoctorName;
	}

	public void setOutsideHospitalDoctorName(String outsideHospitalDoctorName) {
		this.outsideHospitalDoctorName = outsideHospitalDoctorName;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public Date getCreatedDateNew() {
		return createdDateNew;
	}

	public void setCreatedDateNew(Date createdDateNew) {
		this.createdDateNew = createdDateNew;
	}

	public Character getIsFinalBill() {
		return isFinalBill;
	}

	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}

	public Integer getPatientWorkbenchId() {
		return patientWorkbenchId;
	}

	public void setPatientWorkbenchId(Integer patientWorkbenchId) {
		this.patientWorkbenchId = patientWorkbenchId;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public Date getWorkbenchAddedDate() {
		return workbenchAddedDate;
	}

	public void setWorkbenchAddedDate(Date workbenchAddedDate) {
		this.workbenchAddedDate = workbenchAddedDate;
	}

	public String getWorkbenchRemark() {
		return workbenchRemark;
	}

	public void setWorkbenchRemark(String workbenchRemark) {
		this.workbenchRemark = workbenchRemark;
	}
}
