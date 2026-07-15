package com.param.lis.transaction.dto;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.param.lis.unit.dto.TestMasterDto;

public class LabSampleMasterDto {
	private Integer labSampleId;

	private Integer orgId;

	private Integer orgUnitId;

	private String sampleNo;
	
	private String sampleId;

	private Date sampleGenDatetime;

	private Integer orderId;

	private Integer visitTypeId;

	private Integer admissionId;

	private Integer encounterId;

	private Integer patientId;

	private Integer doctorId;

	private Integer createdBy;

	private Date createdDate;

	private Integer updatedBy;

	private Date updatedDate;

	private Date collectionTime;

	private String uhid;

	private String patientDetails;

	private String doctorDetails;

	private Integer priorityId;

	private String priorityName;

	private String colorCode;

	private Integer isOrderComplete;

	private Integer orderStatusId;

	private Integer genderId;

	private String visitType;
	
	private String imagePath;
	
	private Character isVip;
	
	private String age;
	
	private Integer encounterTypeId;
	
	private Character isCritical;
	
	private List<TestMasterDto> testList;
	
	private String testDesc;
	
	private String patHistory;
	
	private String referralName;
	
	private String mobileNo;
	
	private String address;
	
	private Integer phlebotomistHomeCollectionId;
	private Integer userId;
	private String phlebotomistCollectionDate;
	private String fromTime;
	private String toTime;
	private Integer statusId;
	
	private Integer billStatusId; 
	
	private String entitlementDesc;
	
	private Integer paymentEntitlementId;
	
	private Integer payeeId;
	
	private String companyDesc;
	
	private String orderNo;
	
	private Character isUniqueBarcode;
	
	private String neuronPlusUrl;
	
	private String sampleReception;
	private String sampleProcessing;
	private String sampleAnalysisAndDetection;
	
	private String genderCode;
	private String firstName;
	private String doctorName;
	private String birthDate;
	private String cityName;
	private String email;
	private String orderDateString;
	private String orderDateTime;
	private String encounterNumber;
	private Integer serviceId;
	private Integer prefixId;
	private String testCode;
	private String serviceStandardCode;
    private String createdDateString;
	private boolean sampleRecollection;
	private String billNumber;
	private String assignUnitName;
	private Integer billingMasterId;
	private String specimenType;
	private String lastName;
	private String testUnit;
	
	private String assignedByName;
	private boolean isDummyService;
	
	private BigInteger realServiceCount;

	private String sampleInfoDate;
	private String prefix;
	private String middleName;
	private Integer apiNumber;
	private String interfaceName;
	private String interfaceResult;
	private String bloodGroup;
	private String bedNo;
	private String drName;
	private String department;
	private boolean isHomeCollection;

	private BigInteger priorityCount;

	private Integer interfaceId;

	public boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}


	public String getAssignedByName() {
		return assignedByName;
	}

	public void setAssignedByName(String assignedByName) {
		this.assignedByName = assignedByName;
	}

	public String getPatHistory() {
		return patHistory;
	}

	public void setPatHistory(String patHistory) {
		this.patHistory = patHistory;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public Character getIsVip() {
		return isVip;
	}

	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Integer getEncounterTypeId() {
		return encounterTypeId;
	}

	public void setEncounterTypeId(Integer encounterTypeId) {
		this.encounterTypeId = encounterTypeId;
	}

	public Character getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Character isCritical) {
		this.isCritical = isCritical;
	}

	private List<LabSampleDetailsMasterDto> listLabSampleDetailsMaster;

	public Integer getLabSampleId() {
		return labSampleId;
	}

	public void setLabSampleId(Integer labSampleId) {
		this.labSampleId = labSampleId;
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

	public Date getSampleGenDatetime() {
		return sampleGenDatetime;
	}

	public void setSampleGenDatetime(Date sampleGenDatetime) {
		this.sampleGenDatetime = sampleGenDatetime;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public String getDoctorDetails() {
		return doctorDetails;
	}

	public void setDoctorDetails(String doctorDetails) {
		this.doctorDetails = doctorDetails;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public Integer getIsOrderComplete() {
		return isOrderComplete;
	}

	public void setIsOrderComplete(Integer isOrderComplete) {
		this.isOrderComplete = isOrderComplete;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public List<LabSampleDetailsMasterDto> getListLabSampleDetailsMaster() {
		return listLabSampleDetailsMaster;
	}

	public void setListLabSampleDetailsMaster(List<LabSampleDetailsMasterDto> listLabSampleDetailsMaster) {
		this.listLabSampleDetailsMaster = listLabSampleDetailsMaster;
	}

	public Instant getCollectionTime() {
		// return collectionTime;
		Instant collectionLocalTime = collectionTime != null
				? Instant.now() : null;
		return collectionLocalTime;
	}

	public void setCollectionTime(Date collectionTime) {
		this.collectionTime = collectionTime;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public List<TestMasterDto> getTestList() {
		return testList;
	}

	public void setTestList(List<TestMasterDto> testList) {
		this.testList = testList;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPhlebotomistHomeCollectionId() {
		return phlebotomistHomeCollectionId;
	}

	public void setPhlebotomistHomeCollectionId(Integer phlebotomistHomeCollectionId) {
		this.phlebotomistHomeCollectionId = phlebotomistHomeCollectionId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPhlebotomistCollectionDate() {
		return phlebotomistCollectionDate;
	}

	public void setPhlebotomistCollectionDate(String phlebotomistCollectionDate) {
		this.phlebotomistCollectionDate = phlebotomistCollectionDate;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public String getEntitlementDesc() {
		return entitlementDesc;
	}

	public void setEntitlementDesc(String entitlementDesc) {
		this.entitlementDesc = entitlementDesc;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Character getIsUniqueBarcode() {
		return isUniqueBarcode;
	}

	public void setIsUniqueBarcode(Character isUniqueBarcode) {
		this.isUniqueBarcode = isUniqueBarcode;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getNeuronPlusUrl() {
		return neuronPlusUrl;
	}

	public void setNeuronPlusUrl(String neuronPlusUrl) {
		this.neuronPlusUrl = neuronPlusUrl;
	}

	public String getSampleReception() {
		return sampleReception;
	}

	public void setSampleReception(String sampleReception) {
		this.sampleReception = sampleReception;
	}

	public String getSampleProcessing() {
		return sampleProcessing;
	}

	public void setSampleProcessing(String sampleProcessing) {
		this.sampleProcessing = sampleProcessing;
	}

	public String getSampleAnalysisAndDetection() {
		return sampleAnalysisAndDetection;
	}

	public void setSampleAnalysisAndDetection(String sampleAnalysisAndDetection) {
		this.sampleAnalysisAndDetection = sampleAnalysisAndDetection;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrderDateString() {
		return orderDateString;
	}

	public void setOrderDateString(String orderDateString) {
		this.orderDateString = orderDateString;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public String getCreatedDateString() {
		return createdDateString;
	}

	public void setCreatedDateString(String createdDateString) {
		this.createdDateString = createdDateString;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public boolean getSampleRecollection() {
		return sampleRecollection;
	}

	public void setSampleRecollection(boolean sampleRecollection) {
		this.sampleRecollection = sampleRecollection;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getAssignUnitName() {
		return assignUnitName;
	}

	public void setAssignUnitName(String assignUnitName) {
		this.assignUnitName = assignUnitName;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public BigInteger getRealServiceCount() {
		return realServiceCount;
	}

	public void setRealServiceCount(BigInteger realServiceCount) {
		this.realServiceCount = realServiceCount;
	}
	
	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTestUnit() {
		return testUnit;
	}

	public void setTestUnit(String testUnit) {
		this.testUnit = testUnit;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Integer getApiNumber() {
		return apiNumber;
	}

	public void setApiNumber(Integer apiNumber) {
		this.apiNumber = apiNumber;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getSampleInfoDate() {
		return sampleInfoDate;
	}

	public void setSampleInfoDate(String sampleInfoDate) {
		this.sampleInfoDate = sampleInfoDate;
	}

	public String getInterfaceResult() {
		return interfaceResult;
	}

	public void setInterfaceResult(String interfaceResult) {
		this.interfaceResult = interfaceResult;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getDrName() {
		return drName;
	}

	public void setDrName(String drName) {
		this.drName = drName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public boolean isHomeCollection() {
		return isHomeCollection;
	}

	public void setHomeCollection(boolean isHomeCollection) {
		this.isHomeCollection = isHomeCollection;
	}

	public BigInteger getPriorityCount() {
		return priorityCount;
	}

	public void setPriorityCount(BigInteger priorityCount) {
		this.priorityCount = priorityCount;
	}

	public Integer getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}
}
