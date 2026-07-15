package com.param.global.dto;

import java.util.Date;
import java.util.List;

import com.param.global.billing.dto.DepositCollectionReqDto;



public class AdmissionNoteDto 
{
	private int admissionNoteId;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer requestBy;
	
	private Integer requestToDoctorId;
	
	private Integer doctorSpecialityId;
	
	private String specialityName;
	
	private Integer reasonId;
	
	private String reasonDesc;
	
	private Integer patientId;
	
	private String UHIDNumber;

	private String dFirstName;
	
	private String dMiddleName;
	
	private String dLastName;
	
	private String pFirstName;
	
	private String pMiddleName;
	
	private String pLastName;
	
	private String dob;
	
	private Integer preVisitId;
	
	private Integer visitTypeId;
	
	private String visitTypeName;
	
	private char isFlexiableDate;
	
	private char admissionStatus;
	
	private char isCancel;

	private int canceledBy;
	
	private String canceledDate;
	
	private Integer admissionId;
	
	private char status;
	
	private int createdBy;
	
	private int updatedBy;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer bedCategoryId;
	
	private Integer oldBedCategoryId;
	
	private Integer paymentEntitlementId;
	
	private Integer patientCategoryId;
	
	private String doa;
	
	private String oldDoa;
	
	private String pdd;
	
	private char activeStatus;
	
	private Integer prefixId;
	
	private String prefixDesc;
	
	private Integer id;
	
	private String code;
	
	private String bedCategoryDesc;
	
	private String patientCategory;
	
	private String paymentEntitlementDesc;
	
	private Integer offset;
	
	private Integer encounterId;
	
	private Integer noOfRecordsPerPage;
	
	private List<AdmissionPatientDocumentsDto> documentArray;
	
	private String note;
	
	private Integer tPatientId;
	
	private Integer bedCategoryWaitingListId;
	
	private Integer waitListNumber;
	
	private double depositAmount;
	
    private double depositPending;
	
	private double depositCollected;
	
	private Integer depositStatus;
	
	private Integer genderId;
	
	private String mobileNumber;
	
	private String email;
	
	private Integer registrationTypeId;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private Integer admissionTypeId;
	
	private String age;

	private String admissionNoteNumber;
	
	private Integer billingBedCategoryId;
	
	private String billingBedDesc;
	
	private DepositCollectionReqDto reqDto;
	
	private String admissionTypeDesc;
	
	private Integer treatingDoctorId;
	
	private Integer treatingDoctorSpecialityId;
	
	private Integer maxWaitingListNo;
	
	private Integer patientid;

	private String oldPdd;
	
	private Character isAdmissionReqFromCoversheet;
	private String visitDate;
	private Date visitDateNew;
	private String doctorName;
	private Integer specialityId;
	private String fromDate;
	private String toDate;

	private Integer sourceVisitTypeId;
	
	private String requestRejectionNote;
	
	private String requestRejectionReason;
	
	private List<KinDetailsDto> kinList;
	
	private String imagePath;
	
	private String userName;
	
	private String bloodGroup;

	private Integer patientWorkbenchId;
	private Integer resultId;
	private Date workbenchAddedDate;
	private String workbenchRemark;

	private List<AdmissionNoteDto> encounterList;
	private List<AdmissionNoteDto> opdAdmissionDetails;

	private String addedDate;
	private List<String> dates;

	public List<AdmissionNoteDto> getOpdAdmissionDetails() {
		return opdAdmissionDetails;
	}

	public void setOpdAdmissionDetails(List<AdmissionNoteDto> opdAdmissionDetails) {
		this.opdAdmissionDetails = opdAdmissionDetails;
	}

	public List<AdmissionNoteDto> getEncounterList() {
		return encounterList;
	}

	public void setEncounterList(List<AdmissionNoteDto> encounterList) {
		this.encounterList = encounterList;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public String getRequestRejectionNote() {
		return requestRejectionNote;
	}

	public void setRequestRejectionNote(String requestRejectionNote) {
		this.requestRejectionNote = requestRejectionNote;
	}

	public String getRequestRejectionReason() {
		return requestRejectionReason;
	}

	public void setRequestRejectionReason(String requestRejectionReason) {
		this.requestRejectionReason = requestRejectionReason;
	}

	public String getOldPdd() {
		return oldPdd;
	}

	public void setOldPdd(String oldPdd) {
		this.oldPdd = oldPdd;
	}

	public Integer getMaxWaitingListNo() {
		return maxWaitingListNo;
	}

	public void setMaxWaitingListNo(Integer maxWaitingListNo) {
		this.maxWaitingListNo = maxWaitingListNo;
	}

	public Integer getTreatingDoctorId() {
		return treatingDoctorId;
	}

	public void setTreatingDoctorId(Integer treatingDoctorId) {
		this.treatingDoctorId = treatingDoctorId;
	}

	

	public Integer getTreatingDoctorSpecialityId() {
		return treatingDoctorSpecialityId;
	}

	public void setTreatingDoctorSpecialityId(Integer treatingDoctorSpecialityId) {
		this.treatingDoctorSpecialityId = treatingDoctorSpecialityId;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAdmissionNoteNumber() {
		return admissionNoteNumber;
	}

	public void setAdmissionNoteNumber(String admissionNoteNumber) {
		this.admissionNoteNumber = admissionNoteNumber;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public String getBillingBedDesc() {
		return billingBedDesc;
	}

	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}

	public Integer getAdmissionTypeId() {
		return admissionTypeId;
	}

	public void setAdmissionTypeId(Integer admissionTypeId) {
		this.admissionTypeId = admissionTypeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}

	public Integer getOldBedCategoryId() {
		return oldBedCategoryId;
	}

	public void setOldBedCategoryId(Integer oldBedCategoryId) {
		this.oldBedCategoryId = oldBedCategoryId;
	}

	public String getOldDoa() {
		return oldDoa;
	}

	public void setOldDoa(String oldDoa) {
		this.oldDoa = oldDoa;
	}

	public Integer getBedCategoryWaitingListId() {
		return bedCategoryWaitingListId;
	}

	public void setBedCategoryWaitingListId(Integer bedCategoryWaitingListId) {
		this.bedCategoryWaitingListId = bedCategoryWaitingListId;
	}

	public Integer getWaitListNumber() {
		return waitListNumber;
	}

	public void setWaitListNumber(Integer waitListNumber) {
		this.waitListNumber = waitListNumber;
	}

	public Integer gettPatientId() {
		return tPatientId;
	}

	public void settPatientId(Integer tPatientId) {
		this.tPatientId = tPatientId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public List<AdmissionPatientDocumentsDto> getDocumentArray() {
		return documentArray;
	}

	public void setDocumentArray(List<AdmissionPatientDocumentsDto> documentArray) {
		this.documentArray = documentArray;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getBedCategoryDesc() {
		return bedCategoryDesc;
	}

	public void setBedCategoryDesc(String bedCategoryDesc) {
		this.bedCategoryDesc = bedCategoryDesc;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	public String getPrefixDesc() {
		return prefixDesc;
	}

	public void setPrefixDesc(String prefixDesc) {
		this.prefixDesc = prefixDesc;
	}

	public String getGenderCode() {
		return code;
	}

	public void setGenderCode(String code) {
		this.code = code;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getUHIDNumber() {
		return UHIDNumber;
	}

	public void setUHIDNumber(String uHIDNumber) {
		UHIDNumber = uHIDNumber;
	}

	

	public String getdFirstName() {
		return dFirstName;
	}

	public void setdFirstName(String dFirstName) {
		this.dFirstName = dFirstName;
	}

	public String getdMiddleName() {
		return dMiddleName;
	}

	public void setdMiddleName(String dMiddleName) {
		this.dMiddleName = dMiddleName;
	}

	public String getdLastName() {
		return dLastName;
	}

	public void setdLastName(String dLastName) {
		this.dLastName = dLastName;
	}

	public String getpFirstName() {
		return pFirstName;
	}

	public void setpFirstName(String pFirstName) {
		this.pFirstName = pFirstName;
	}

	public String getpMiddleName() {
		return pMiddleName;
	}

	public void setpMiddleName(String pMiddleName) {
		this.pMiddleName = pMiddleName;
	}

	public String getpLastName() {
		return pLastName;
	}

	public void setpLastName(String pLastName) {
		this.pLastName = pLastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
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

	public char getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(char activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getAdmissionNoteId() {
		return admissionNoteId;
	}

	public void setAdmissionNoteId(int admissionNoteId) {
		this.admissionNoteId = admissionNoteId;
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

	public Integer getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(Integer requestBy) {
		this.requestBy = requestBy;
	}

	public Integer getRequestToDoctorId() {
		return requestToDoctorId;
	}

	public void setRequestToDoctorId(Integer requestToDoctorId) {
		this.requestToDoctorId = requestToDoctorId;
	}

	public Integer getDoctorSpecialtyId() {
		return doctorSpecialityId;
	}

	public void setDoctorSpecialtyId(Integer doctorSpecialtyId) {
		this.doctorSpecialityId = doctorSpecialtyId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPreVisitId() {
		return preVisitId;
	}

	public void setPreVisitId(Integer preVisitId) {
		this.preVisitId = preVisitId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public char getIsFlexiableDate() {
		return isFlexiableDate;
	}

	public void setIsFlexiableDate(char isFlexiableDate) {
		this.isFlexiableDate = isFlexiableDate;
	}

	public char getAdmissionStatus() {
		return admissionStatus;
	}

	public void setAdmissionStatus(char admissionStatus) {
		this.admissionStatus = admissionStatus;
	}

	public char getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(char isCancel) {
		this.isCancel = isCancel;
	}

	
	

	public Integer getDoctorSpecialityId() {
		return doctorSpecialityId;
	}

	public void setDoctorSpecialityId(Integer doctorSpecialityId) {
		this.doctorSpecialityId = doctorSpecialityId;
	}

	public int getCanceledBy() {
		return canceledBy;
	}

	public void setCanceledBy(int canceledBy) {
		this.canceledBy = canceledBy;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(String canceledDate) {
		this.canceledDate = canceledDate;
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

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getDepositPending() {
		return depositPending;
	}

	public void setDepositPending(double depositPending) {
		this.depositPending = depositPending;
	}

	public double getDepositCollected() {
		return depositCollected;
	}

	public void setDepositCollected(double depositCollected) {
		this.depositCollected = depositCollected;
	}

	public Integer getDepositStatus() {
		return depositStatus;
	}

	public void setDepositStatus(Integer depositStatus) {
		this.depositStatus = depositStatus;
	}

	public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public DepositCollectionReqDto getReqDto() {
		return reqDto;
	}

	public void setReqDto(DepositCollectionReqDto reqDto) {
		this.reqDto = reqDto;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getAdmissionTypeDesc() {
		return admissionTypeDesc;
	}

	public void setAdmissionTypeDesc(String admissionTypeDesc) {
		this.admissionTypeDesc = admissionTypeDesc;
	}

	public Integer getPatientid() {
		return patientid;
	}

	public void setPatientid(Integer patientid) {
		this.patientid = patientid;
	}

	public Character getIsAdmissionReqFromCoversheet() {
		return isAdmissionReqFromCoversheet;
	}

	public void setIsAdmissionReqFromCoversheet(Character isAdmissionReqFromCoversheet) {
		this.isAdmissionReqFromCoversheet = isAdmissionReqFromCoversheet;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSourceVisitTypeId() {
		return sourceVisitTypeId;
	}

	public void setSourceVisitTypeId(Integer sourceVisitTypeId) {
		this.sourceVisitTypeId = sourceVisitTypeId;
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

	public List<KinDetailsDto> getKinList() {
		return kinList;
	}

	public void setKinList(List<KinDetailsDto> kinList) {
		this.kinList = kinList;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getVisitDateNew() {
		return visitDateNew;
	}

	public void setVisitDateNew(Date visitDateNew) {
		this.visitDateNew = visitDateNew;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
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
