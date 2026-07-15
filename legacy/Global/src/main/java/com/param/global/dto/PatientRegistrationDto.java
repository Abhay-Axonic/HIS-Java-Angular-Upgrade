package com.param.global.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class PatientRegistrationDto {
	private Integer patientId;

	private Integer organizationId;

	private Integer unitId;

	private String uhidNumber;

	private Integer prefixId;

	private String firstName;

	private String middleName;

	private String lastName;

	private Integer genderId;

	private String dob;

	private String birthDate;
	
	private Date birthDateNew;

	private String mobileNumber;

	private String countryCallingCode;

	private String barCode;

	private char isVip;

	private String vipRemark;

	private char isBlacklist;

	private Integer identificationTypeId;

	private String identificationNumber;

	private String email;

	private Integer patientCategoryId;
	
	private Integer appointmentTypeId;

	private char isUnknownReg;

	private char status;

	private Integer createdBy;

	private Integer updatedBy;

	private String createdDate;

	private String updatedDate;

	private Integer patientDetailsId;

	private Integer nationalityId;

	private Integer raceId;

	private Integer maritalStatusId;

	private String address;

	private Integer countryId;

	private Integer stateId;

	private Integer districtId;

	private Integer cityId;

	private Integer areaId;

	private String zipCode;

	private String phoneCode;

	private String phoneNumber;

	private String companyName;

	private String companyAddress;

	private Integer companyCountryId;

	private Integer companyStateId;

	private Integer companyDistrictId;

	private Integer companyCityId;

	private Integer companyAreaId;

	private String companyZipCode;

	private String companyMobileNumber;

	private String patientName;

	private String genderCode;

	private String permanentAddress;

	private Integer permanentCountryId;

	private Integer permanentStateId;

	private Integer permanentDistrictId;

	private Integer permanentCityId;

	private Integer permanentAreaId;

	private String permanentZipCode;

	private String permanentPhoneCode;

	private String permanentPhoneNumber;

	private String aliseName;

	private String identificationExpiryDate;

	private Integer occupationId;

	private Integer registrationTypeId;

	private Integer previousId;

	private String prefixCode;

	private String identificationName;

	private char isOtcReg;

	private char isPreReg;

	private Character isExpiryRequired;

	private String ageString;

	private Integer empDocId;

	private Integer empDepTypeId;

	private Integer empDocDepId;

	private Integer dependentDetailsId;

	private Integer typeId;

	private Integer relationId;

	private String mobileNo;

	private String dobKin;

	private char isNok;

	private String employeeName;

	private String empCode;

	private String dependentName;

	private String relationName;

	private String specialityName;

	private Integer reasonId;

	private Integer reasonDesc;

	private String remark;

	private String blockedRemark;

	private Character isConversion;

	private String prevUhidNumber;

	private String thumbImage;

    private String imagePath;
    
    private Character isPrimary;
    
    private Character isFromSelf;
    
    private KinDetailsDto kinDetailsDto;
    
    private List<Integer> registrationTypeIdList;
    
    private List<String> allergies;
    
    private Integer appointmentId;
    
    private String appointmentDate;
    
    private String appointmentTime;
    
    private String appointmentTime2;
    
    private String sender;
    
    private String currentDate;
    
    private Integer appointmentStatusId;
    
    private Integer specialityId;
    
    private Integer doctorId;
    
    private String doctorName;
    
    private String age;
    
    private Character isAbsconded;
    
    private Character isFromER;
    
    private Character deptWiseAppointment;
    
    private List<KinDetailsDto> kinList;
    
    private String smsTemplateMessage;
    
	private String mpshahPatientId;
	
	private Integer tempPatientId;
    
	private Integer admissionId;
	
	private Integer visitTypeId;
	
	private Character isPaidAppointment;
	
	private Integer encounterId;
	
	private Integer patientid;

	private String firstname;
	private String uhidnumber;
	private String lastname;
	private String gender;

	private byte[] image;
	
	private BigInteger encounterCount;
	
	private Character isFromPatientList;
	
	private String whatsappNo;

    private String whatsappCallingCode;
    
    private String skypeId;
    
    private String area;
    
    private String permanentArea;
    
    
    private Integer kinDetailsId;
    
    private String admissionNumber;
    
	private Integer offset;

	private Integer noOfRecordsPerPage;
    
	private String fileSequenceNumber;
	
	private String customDataPost;

	private String customDataDisplay;
	private Integer customDetailsId;
	private String countryName;
	private String stateName;
	private String cityName;
	private String areaName;
	private CustomDataPostDto customDataPostDto;
	private String registrationNumber;
	private String dateTime;
	private String videoCallUrl;
	private String patientRemark;
	private boolean isDataAvailable;
	private String followUpDate;
	
	private String bloodGroup;
	
	 private String allergyTypeDesc;
	 
	 private String emergencyNumber;
	 
     private Integer id;
     private String name;
 	private Integer sourceId;
     
     private Integer bucketFlag;
     private String mysqlOnp;
     private Character isAlreadyExist;
     private String familyId;
     private String healthId;
     
     private String healthNumber;
     
     private String aadharNumber;
     private String searchKeyword;
     private String unitName;
     private String unitEmailId;
     private String unitContact;
     private String districtName;
     private String districtCode;
     private String stateCode;
     private String legacyRegistrationId;

	 private Boolean initialAssesmentStarted;

	 private String assignmentStartTime;

	 private Integer assignmentStartBy;

	 private Integer orderDetailsId;

    public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Integer getKinDetailsId() {
		return kinDetailsId;
	}

	public void setKinDetailsId(Integer kinDetailsId) {
		this.kinDetailsId = kinDetailsId;
	}

	public String getPermanentArea() {
		return permanentArea;
	}

	public void setPermanentArea(String permanentArea) {
		this.permanentArea = permanentArea;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getWhatsappNo() {
        return whatsappNo;
    }

    public void setWhatsappNo(String whatsappNo) {
        this.whatsappNo = whatsappNo;
    }

    public String getWhatsappCallingCode() {
        return whatsappCallingCode;
    }

    public void setWhatsappCallingCode(String whatsappCallingCode) {
        this.whatsappCallingCode = whatsappCallingCode;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }
	
    public Character getIsFromPatientList() {
		return isFromPatientList;
	}

	public void setIsFromPatientList(Character isFromPatientList) {
		this.isFromPatientList = isFromPatientList;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Character getIsPaidAppointment() {
		return isPaidAppointment;
	}

	public void setIsPaidAppointment(Character isPaidAppointment) {
		this.isPaidAppointment = isPaidAppointment;
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

	public Character getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(Character deptWiseAppointment) {
		this.deptWiseAppointment = deptWiseAppointment;
	}

	public Integer getAppointmentTypeId() {
		return appointmentTypeId;
	}

	public void setAppointmentTypeId(Integer appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}

	public String getAppointmentTime2() {
		return appointmentTime2;
	}

	public void setAppointmentTime2(String appointmentTime2) {
		this.appointmentTime2 = appointmentTime2;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Character getIsFromSelf() {
		return isFromSelf;
	}

	public void setIsFromSelf(Character isFromSelf) {
		this.isFromSelf = isFromSelf;
	}

	public List<String> getAllergies() {
		return allergies;
	}

	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}

	private String uuid;

	public KinDetailsDto getKinDetailsDto() {
		return kinDetailsDto;
	}

	public void setKinDetailsDto(KinDetailsDto kinDetailsDto) {
		this.kinDetailsDto = kinDetailsDto;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Character getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Character isPrimary) {
		this.isPrimary = isPrimary;
	}

	public List<Integer> getRegistrationTypeIdList() {
		return registrationTypeIdList;
	}

	public void setRegistrationTypeIdList(List<Integer> registrationTypeIdList) {
		this.registrationTypeIdList = registrationTypeIdList;
	}

	public Character getIsConversion() {
		return isConversion;
	}

	public void setIsConversion(Character isConversion) {
		this.isConversion = isConversion;
	}

	public String getPrevUhidNumber() {
		return prevUhidNumber;
	}

	public void setPrevUhidNumber(String prevUhidNumber) {
		this.prevUhidNumber = prevUhidNumber;
	}

	public String getBlockedRemark() {
		return blockedRemark;
	}

	public void setBlockedRemark(String blockedRemark) {
		this.blockedRemark = blockedRemark;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(Integer reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/*
	 * + "slud.reason_id as \"reasonId\", " +
	 * "reason.reason_desc as \"reasonDesc\", " + "slud.remark as \"remark\"
	 */
	public Integer getDependentDetailsId() {
		return dependentDetailsId;
	}

	public void setDependentDetailsId(Integer dependentDetailsId) {
		this.dependentDetailsId = dependentDetailsId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getDobKin() {
		return dobKin;
	}

	public void setDobKin(String dobKin) {
		this.dobKin = dobKin;
	}

	public char getIsNok() {
		return isNok;
	}

	public void setIsNok(char isNok) {
		this.isNok = isNok;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getDependentName() {
		return dependentName;
	}

	public void setDependentName(String dependentName) {
		this.dependentName = dependentName;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getEmpDocId() {
		return empDocId;
	}

	public void setEmpDocId(Integer empDocId) {
		this.empDocId = empDocId;
	}

	public Integer getEmpDepTypeId() {
		return empDepTypeId;
	}

	public void setEmpDepTypeId(Integer empDepTypeId) {
		this.empDepTypeId = empDepTypeId;
	}

	public Integer getEmpDocDepId() {
		return empDocDepId;
	}

	public void setEmpDocDepId(Integer empDocDepId) {
		this.empDocDepId = empDocDepId;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public char getIsVip() {
		return isVip;
	}

	public void setIsVip(char isVip) {
		this.isVip = (isVip == '\u0000') ? 'N' : isVip;
	}

	public String getVipRemark() {
		return vipRemark;
	}

	public void setVipRemark(String vipRemark) {
		this.vipRemark = vipRemark;
	}

	public char getIsBlacklist() {
		return isBlacklist;
	}

	public void setIsBlacklist(char isBlacklist) {
		this.isBlacklist = (isBlacklist == '\u0000') ? 'N' : isBlacklist;
	}

	public Integer getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Integer identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public char getIsUnknownReg() {
		return isUnknownReg;
	}

	public void setIsUnknownReg(char isUnknownReg) {
		this.isUnknownReg = (isUnknownReg == '\u0000') ? 'N' : isUnknownReg;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public Integer getPatientDetailsId() {
		return patientDetailsId;
	}

	public void setPatientDetailsId(Integer patientDetailsId) {
		this.patientDetailsId = patientDetailsId;
	}

	public Integer getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(Integer nationalityId) {
		this.nationalityId = nationalityId;
	}

	public Integer getRaceId() {
		return raceId;
	}

	public void setRaceId(Integer raceId) {
		this.raceId = raceId;
	}

	public Integer getMaritalStatusId() {
		return maritalStatusId;
	}

	public void setMaritalStatusId(Integer maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public Integer getCompanyCountryId() {
		return companyCountryId;
	}

	public void setCompanyCountryId(Integer companyCountryId) {
		this.companyCountryId = companyCountryId;
	}

	public Integer getCompanyStateId() {
		return companyStateId;
	}

	public void setCompanyStateId(Integer companyStateId) {
		this.companyStateId = companyStateId;
	}

	public Integer getCompanyDistrictId() {
		return companyDistrictId;
	}

	public void setCompanyDistrictId(Integer companyDistrictId) {
		this.companyDistrictId = companyDistrictId;
	}

	public Integer getCompanyCityId() {
		return companyCityId;
	}

	public void setCompanyCityId(Integer companyCityId) {
		this.companyCityId = companyCityId;
	}

	public Integer getCompanyAreaId() {
		return companyAreaId;
	}

	public void setCompanyAreaId(Integer companyAreaId) {
		this.companyAreaId = companyAreaId;
	}

	public String getCompanyZipCode() {
		return companyZipCode;
	}

	public void setCompanyZipCode(String companyZipCode) {
		this.companyZipCode = companyZipCode;
	}

	public String getCompanyMobileNumber() {
		return companyMobileNumber;
	}

	public void setCompanyMobileNumber(String companyMobileNumber) {
		this.companyMobileNumber = companyMobileNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public Integer getPermanentCountryId() {
		return permanentCountryId;
	}

	public void setPermanentCountryId(Integer permanentCountryId) {
		this.permanentCountryId = permanentCountryId;
	}

	public Integer getPermanentStateId() {
		return permanentStateId;
	}

	public void setPermanentStateId(Integer permanentStateId) {
		this.permanentStateId = permanentStateId;
	}

	public Integer getPermanentDistrictId() {
		return permanentDistrictId;
	}

	public void setPermanentDistrictId(Integer permanentDistrictId) {
		this.permanentDistrictId = permanentDistrictId;
	}

	public Integer getPermanentCityId() {
		return permanentCityId;
	}

	public void setPermanentCityId(Integer permanentCityId) {
		this.permanentCityId = permanentCityId;
	}

	public Integer getPermanentAreaId() {
		return permanentAreaId;
	}

	public void setPermanentAreaId(Integer permanentAreaId) {
		this.permanentAreaId = permanentAreaId;
	}

	public String getPermanentZipCode() {
		return permanentZipCode;
	}

	public void setPermanentZipCode(String permanentZipCode) {
		this.permanentZipCode = permanentZipCode;
	}

	public String getPermanentPhoneCode() {
		return permanentPhoneCode;
	}

	public void setPermanentPhoneCode(String permanentPhoneCode) {
		this.permanentPhoneCode = permanentPhoneCode;
	}

	public String getPermanentPhoneNumber() {
		return permanentPhoneNumber;
	}

	public void setPermanentPhoneNumber(String permanentPhoneNumber) {
		this.permanentPhoneNumber = permanentPhoneNumber;
	}

	public String getAliseName() {
		return aliseName;
	}

	public void setAliseName(String aliseName) {
		this.aliseName = aliseName;
	}

	public String getIdentificationExpiryDate() {
		return identificationExpiryDate;
	}

	public void setIdentificationExpiryDate(String identificationExpiryDate) {
		this.identificationExpiryDate = identificationExpiryDate;
	}

	public Integer getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Integer occupationId) {
		this.occupationId = occupationId;
	}

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}

	public Integer getPreviousId() {
		return previousId;
	}

	public void setPreviousId(Integer previousId) {
		this.previousId = previousId;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public String getIdentificationName() {
		return identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public char getIsOtcReg() {
		return isOtcReg;
	}

	public void setIsOtcReg(char isOtcReg) {
		this.isOtcReg = (isOtcReg == '\u0000') ? 'N' : isOtcReg;
	}

	public char getIsPreReg() {
		return isPreReg;
	}

	public void setIsPreReg(char isPreReg) {
		this.isPreReg = (isPreReg == '\u0000') ? 'N' : isPreReg;
	}

	public Character getIsExpiryRequired() {
		return isExpiryRequired;
	}

	public void setIsExpiryRequired(Character isExpiryRequired) {
		this.isExpiryRequired = isExpiryRequired;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}

	public String getSmsTemplateMessage() {
		return smsTemplateMessage;
	}

	public void setSmsTemplateMessage(String smsTemplateMessage) {
		this.smsTemplateMessage = smsTemplateMessage;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Character getIsFromER() {
		return isFromER;
	}

	public void setIsFromER(Character isFromER) {
		this.isFromER = isFromER;
	}

	public List<KinDetailsDto> getKinList() {
		return kinList;
	}

	public void setKinList(List<KinDetailsDto> kinList) {
		this.kinList = kinList;
	}

	public String getMpshahPatientId() {
		return mpshahPatientId;
	}

	public void setMpshahPatientId(String mpshahPatientId) {
		this.mpshahPatientId = mpshahPatientId;
	}

	public Character getIsAbsconded() {
		return isAbsconded;
	}

	public void setIsAbsconded(Character isAbsconded) {
		this.isAbsconded = isAbsconded;
	}

	public Integer getTempPatientId() {
		return tempPatientId;
	}

	public void setTempPatientId(Integer tempPatientId) {
		this.tempPatientId = tempPatientId;
	}

	public Integer getPatientid() {
		return patientid;
	}

	public void setPatientid(Integer patientid) {
		this.patientid = patientid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getUhidnumber() {
		return uhidnumber;
	}

	public void setUhidnumber(String uhidnumber) {
		this.uhidnumber = uhidnumber;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public BigInteger getEncounterCount() {
		return encounterCount;
	}

	public void setEncounterCount(BigInteger encounterCount) {
		this.encounterCount = encounterCount;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
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

	public String getFileSequenceNumber() {
		return fileSequenceNumber;
	}

	public void setFileSequenceNumber(String fileSequenceNumber) {
		this.fileSequenceNumber = fileSequenceNumber;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCustomDataPost() {
		return customDataPost;
	}

	public void setCustomDataPost(String customDataPost) {
		this.customDataPost = customDataPost;
	}

	public String getCustomDataDisplay() {
		return customDataDisplay;
	}

	public void setCustomDataDisplay(String customDataDisplay) {
		this.customDataDisplay = customDataDisplay;
	}

	public Integer getCustomDetailsId() {
		return customDetailsId;
	}

	public void setCustomDetailsId(Integer customDetailsId) {
		this.customDetailsId = customDetailsId;
	}

	public CustomDataPostDto getCustomDataPostDto() {
		return customDataPostDto;
	}

	public void setCustomDataPostDto(CustomDataPostDto customDataPostDto) {
		this.customDataPostDto = customDataPostDto;
	}

	public Date getBirthDateNew() {
		return birthDateNew;
	}

	public void setBirthDateNew(Date birthDateNew) {
		this.birthDateNew = birthDateNew;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getVideoCallUrl() {
		return videoCallUrl;
	}

	public void setVideoCallUrl(String videoCallUrl) {
		this.videoCallUrl = videoCallUrl;
	}

	public String getPatientRemark() {
		return patientRemark;
	}

	public void setPatientRemark(String patientRemark) {
		this.patientRemark = patientRemark;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getAllergyTypeDesc() {
		return allergyTypeDesc;
	}

	public void setAllergyTypeDesc(String allergyTypeDesc) {
		this.allergyTypeDesc = allergyTypeDesc;
	}

	public String getEmergencyNumber() {
		return emergencyNumber;
	}

	public void setEmergencyNumber(String emergencyNumber) {
		this.emergencyNumber = emergencyNumber;
	}

	public boolean getIsDataAvailable() {
		return isDataAvailable;
	}

	public void setIsDataAvailable(boolean isDataAvailable) {
		this.isDataAvailable = isDataAvailable;
	}

	public String getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(String followUpDate) {
		this.followUpDate = followUpDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	
	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}

	public String getMysqlOnp() {
		return mysqlOnp;
	}

	public void setMysqlOnp(String mysqlOnp) {
		this.mysqlOnp = mysqlOnp;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}

	public String getHealthNumber() {
		return healthNumber;
	}

	public void setHealthNumber(String healthNumber) {
		this.healthNumber = healthNumber;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public Character getIsAlreadyExist() {
		return isAlreadyExist;
	}

	public void setIsAlreadyExist(Character isAlreadyExist) {
		this.isAlreadyExist = isAlreadyExist;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitEmailId() {
		return unitEmailId;
	}

	public void setUnitEmailId(String unitEmailId) {
		this.unitEmailId = unitEmailId;
	}

	public String getUnitContact() {
		return unitContact;
	}

	public void setUnitContact(String unitContact) {
		this.unitContact = unitContact;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLegacyRegistrationId() {
		return legacyRegistrationId;
	}

	public void setLegacyRegistrationId(String legacyRegistrationId) {
		this.legacyRegistrationId = legacyRegistrationId;
	}

	public Boolean getInitialAssesmentStarted() {
		return initialAssesmentStarted;
	}

	public void setInitialAssesmentStarted(Boolean initialAssesmentStarted) {
		this.initialAssesmentStarted = initialAssesmentStarted;
	}

	public String getAssignmentStartTime() {
		return assignmentStartTime;
	}

	public void setAssignmentStartTime(String assignmentStartTime) {
		this.assignmentStartTime = assignmentStartTime;
	}

	public Integer getAssignmentStartBy() {
		return assignmentStartBy;
	}

	public void setAssignmentStartBy(Integer assignmentStartBy) {
		this.assignmentStartBy = assignmentStartBy;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
}