package com.param.global.dto;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.param.global.dto.global.VisitDetails;

public class PatientSearchDto {
	private String uhIdNumber;
	
	private String patientName;
	
	private Integer genderId;
	
	private String mobileNo;
	private String mobileNumber;

	private String identificationNo;
	private String identificationNumber;

	private String birthDate;
	private Date birthDateNew;
	
	private String dob;

	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer patientId;
	
	private String genderCode;
	
	private String identificationName;
	
	private String identificationExpiryDate;
	
	private Integer patientType;
	
	private String genderName;
	
	private String age;
	
	private String wardName;
	
	private String bedNumber;
	
	private Integer admissionId;
	
	private Integer transferTypeId;
	
	private Integer visitTypeId;
	
	private Integer prefixId;
	
	private Integer patientCategoryId;
	
	private Integer identificationTypeId;
	
	private String patientProfile;
	
	private String loyaltycardNo;
	
	private Integer patientLoyaltyCardMpprId;
	
	private Integer registrationTypeId;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String email;
	
	private Integer appointmentId;
	
	private Boolean isGlobalSearch;
	
	private String uhidNumber;

	private Integer doctorId;

	private String doctorName;
	
	private Integer encounterId;
	
	private Integer roleId;

	private String fromDate;
	private String documentName;
	private Integer documentTypeId;
	private String createdDate;
	private Integer userId;
	private String documentPath;
	private String doa;
	private String pdd;
	private String wardNo;
	private Integer specialityId;

	private String specialityName;

	private Integer billingBedCategoryId;
	
	private Integer paymentEntitlementId;
	
	private Integer contractId;

	private String toDate;
	
	private String createdTime;
	
	private List<Integer> wardList;
	
	private List<Integer> bedList;

	private Character isAbsconded;
	
	private Character isCyclicAppointment;
	
	private Integer appointmentStatusId;
	
	private Integer admissionUnitId;
	
	private String fileSequenceNumber;
	private String fromUhidNumber;
	private String toUhidNumber;
	 
	private String toPatientName;
	
	private char isDeath;
	
	private String videoCallUrl;
	
	private List<VisitDetails> visitDetails;
	private Integer wardId;
	private String visitType;
	private Integer bedId;
	private String identificationType;
	private String createdDateString;
	private String countryCallingCode;
	private boolean ipRenderedDateChange;
	private String bloodGroup;
    private Integer offset;
	private Integer noOfRecordsPerPage;
	private Integer bucketFlag;
	private Character isFinalBill;
	private String ageStr;

	private Boolean fromMrdScreen;

	public String getToPatientName() {
		return toPatientName;
	}

	public void setToPatientName(String toPatientName) {
		this.toPatientName = toPatientName;
	}
	private Integer countryId;
	private Integer districtId;
	
	private String address;
	private String cityName;
    private Integer slotId;
    private String slotFromTime;
    private String slotToTime;
    private String encounterDate;
    private String appointmentDate;
    private String legacyVisitId;

	public Integer getCountryId() {
		return countryId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getIdentificationExpiryDate() {
		return identificationExpiryDate;
	}

	public void setIdentificationExpiryDate(String identificationExpiryDate) {
		this.identificationExpiryDate = identificationExpiryDate;
	}

	public Integer getAdmissionUnitId() {
		return admissionUnitId;
	}

	public void setAdmissionUnitId(Integer admissionUnitId) {
		this.admissionUnitId = admissionUnitId;
	}

	public List<Integer> getBedList() {
		return bedList;
	}

	public void setBedList(List<Integer> bedList) {
		this.bedList = bedList;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
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

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(Integer transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getUhIdNumber() {
		return uhIdNumber;
	}

	public void setUhIdNumber(String uhIdNumber) {
		this.uhIdNumber = uhIdNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getIdentificationName() {
		return identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public Integer getPatientType() {
		return patientType;
	}

	public void setPatientType(Integer patientType) {
		this.patientType = patientType;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public String getPatientProfile() {
		return patientProfile;
	}

	public void setPatientProfile(String patientProfile) {
		this.patientProfile = patientProfile;
	}

	public Integer getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Integer identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getLoyaltycardNo() {
		return loyaltycardNo;
	}

	public void setLoyaltycardNo(String loyaltycardNo) {
		this.loyaltycardNo = loyaltycardNo;
	}

	public Boolean getIsGlobalSearch() {
		return isGlobalSearch;
	}

	public void setIsGlobalSearch(Boolean isGlobalSearch) {
		this.isGlobalSearch = isGlobalSearch;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
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

	public Integer getPatientLoyaltyCardMpprId() {
		return patientLoyaltyCardMpprId;
	}

	public void setPatientLoyaltyCardMpprId(Integer patientLoyaltyCardMpprId) {
		this.patientLoyaltyCardMpprId = patientLoyaltyCardMpprId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Character getIsAbsconded() {
		return isAbsconded;
	}

	public void setIsAbsconded(Character isAbsconded) {
		this.isAbsconded = isAbsconded;
	}

	public List<Integer> getWardList() {
		return wardList;
	}

	public void setWardList(List<Integer> wardList) {
		this.wardList = wardList;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
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

	public Character getIsCyclicAppointment() {
		return isCyclicAppointment;
	}

	public void setIsCyclicAppointment(Character isCyclicAppointment) {
		this.isCyclicAppointment = isCyclicAppointment;
	}

	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
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

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	
	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getFileSequenceNumber() {
		return fileSequenceNumber;
	}

	public void setFileSequenceNumber(String fileSequenceNumber) {
		this.fileSequenceNumber = fileSequenceNumber;
	}

	public String getFromUhidNumber() {
		return fromUhidNumber;
	}

	public void setFromUhidNumber(String fromUhidNumber) {
		this.fromUhidNumber = fromUhidNumber;
	}

	public String getToUhidNumber() {
		return toUhidNumber;
	}

	public void setToUhidNumber(String toUhidNumber) {
		this.toUhidNumber = toUhidNumber;
	}

	public Date getBirthDateNew() {
		return birthDateNew;
	}

	public void setBirthDateNew(Date birthDateNew) {
		this.birthDateNew = birthDateNew;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public char getIsDeath() {
		return isDeath;
	}

	public void setIsDeath(char isDeath) {
		this.isDeath = isDeath;
	}

	public String getVideoCallUrl() {
		return videoCallUrl;
	}

	public void setVideoCallUrl(String videoCallUrl) {
		this.videoCallUrl = videoCallUrl;
	}

	public List<VisitDetails> getVisitDetails() {
		return visitDetails;
	}

	public void setVisitDetails(List<VisitDetails> visitDetails) {
		this.visitDetails = visitDetails;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getCreatedDateString() {
		return createdDateString;
	}

	public void setCreatedDateString(String createdDateString) {
		this.createdDateString = createdDateString;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public boolean isIpRenderedDateChange() {
		return ipRenderedDateChange;
	}

	public void setIpRenderedDateChange(boolean ipRenderedDateChange) {
		this.ipRenderedDateChange = ipRenderedDateChange;
		
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

	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}
	
	public Character getIsFinalBill() {
		return isFinalBill;
	}

	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}

	public String getAgeStr() {
		return ageStr;
	}

	public void setAgeStr(String ageStr) {
		this.ageStr = ageStr;
	}

	public Boolean getFromMrdScreen() {
		return fromMrdScreen;
	}
	
	public void setFromMrdScreen(Boolean fromMrdScreen) {
		this.fromMrdScreen = fromMrdScreen;
	}

	public String getWardNo() {
		return wardNo;
	}

	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public String getSlotFromTime() {
        return slotFromTime;
    }

    public void setSlotFromTime(String slotFromTime) {
        this.slotFromTime = slotFromTime;
    }

    public String getSlotToTime() {
        return slotToTime;
    }

    public void setSlotToTime(String slotToTime) {
        this.slotToTime = slotToTime;
    }

    public String getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(String encounterDate) {
        this.encounterDate = encounterDate;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

	public String getLegacyVisitId() {
		return legacyVisitId;
	}

	public void setLegacyVisitId(String legacyVisitId) {
		this.legacyVisitId = legacyVisitId;
	}
}

