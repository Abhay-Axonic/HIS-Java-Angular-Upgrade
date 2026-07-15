package com.param.global.dto;

import java.util.Date;
import java.util.List;

import com.param.global.unit.dto.OTIMSUserRoleMapperDto;
import com.param.global.unit.dto.UserIndetificationDetailsDto;
import com.param.global.unit.dto.UserRoleMapperDto;


public class DoctorMasterDto {
	
	
	private Integer doctorId;

	private Integer userId;

	private Integer doctorNumber;

	private String firstName;

	private String middleName;

	private String lastName;
	
	private String doctorName;
	
	private String prefixCode;

	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer specialityId;

	private String specialityName;
	
	private Integer modalityId;
	
	private Integer modalityTypeId;
	
	private Integer subSpecialityId;
	
	private Integer prefixId;
	
	private String dob;
	
	private Integer genderId;

	private String mobileNumber;
	
	private String countryCallingCode;
			
	private String barCode;
	
	private Character isVip;
	
	private String vipRemark;
	
	private Integer identificationTypeId;
	
	private String identificationNumber;
	
	private String email;
	
	private Integer docCategoryId;
	
	private String aliseName;
	
	private String identificationExpiryDate;

	private Integer occupationId;
	
	private Integer docDesignationId;
	
	private String docCode;
	
	private String dateOfJoining;
	
	private String accessCardNumber;
	
	private Integer allowedOverBookingSlots;
	
	private Integer doctorDetailsId;

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

	private String permanentAddress;

	private Integer permanentCountryId;

	private Integer permanentStateId;

	private Integer permanentDistrictId;

	private Integer permanentCityId;

	private Integer permanentAreaId;

	private String permanentZipCode;

	private String permanentPhoneCode;

	private String permanentPhoneNumber;
	
	private String bankName;
	
	private String accountNumber;
	
	private String bankBranchName;

	private String ifscCode;

	private Integer typeId;
	
	private String docDesignationDescription;
	
	private String docCategoryDescription;
	
	private String ageString;
	
	private String subSpecialityName;
	
	private String identificationName;
	
	private Character isExpiryRequired;
	
	private Integer departmentId;
	
	private String doctorImage;
	
	private String birthDate;
	private Date birthDateNew;
	
	private String registrationNumber;
	
	private Integer doctorCategoryId;
	
	private Integer employeeDesignationId;

	private Integer slotTimeSpan;
	
	private String pwdExpiry;
	
   private Integer qualificationId;
	
	private String qualificationDesc;
	
	private Integer degreeId;
	
	private String degreeDesc; 
	
	private byte[] file;
	
	private String documentFilePath; 
	
	private Integer doctorDocId;
	
    private List<UserRoleMapperDto> selectedHisRoleList;
	
	private List<OTIMSUserRoleMapperDto> selectedOTIMSRoleList;
	
	private List<UserUnitMapperDto> selectedUserUnitList;
	
	private List<DoctorSpecialityMapperDto> doctorSpecialityMapperDtosList;
	
	private List<DoctorSpecialityMapperDto> doctorSubSpecialityMapperDtosList;
	
	private List<UserIndetificationDetailsDto>identificationDetailsList;
	
	private Character isRMO;
	
	private Character processShareAgainstUnsettledBill;
	
	private String doctorPin;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;

	private Character isPatientSearch;
	
	private String fileName;
	
	private String defaultMISRights;
	
	private String defaultCoversheetRights;
	
	private Integer doctorType;
	private Integer serviceId;
	private Integer billingBedCategoryId;
	private Integer visitTypeId;
	private Integer patientTypeId;
	private Integer paymentEntitlementId;
	private Integer serviceDepartmentId;
	private Integer doctorDepartmentId;
	private Integer creditDays;
	private Integer generalLedgerId;
	private String generalLedgerName;
	
	private Integer medallReferalId;
	
   private String areaName;
   
   private String area;
   
	private String permanentAreaName;

	private String pmsUrl;

	public String getPmsUrl() {
		return pmsUrl;
	}

	public void setPmsUrl(String pmsUrl) {
		this.pmsUrl = pmsUrl;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public String getDefaultMISRights() {
		return defaultMISRights;
	}

	public void setDefaultMISRights(String defaultMISRights) {
		this.defaultMISRights = defaultMISRights;
	}

	public List<DoctorSpecialityMapperDto> getDoctorSubSpecialityMapperDtosList() {
		return doctorSubSpecialityMapperDtosList;
	}

	public void setDoctorSubSpecialityMapperDtosList(List<DoctorSpecialityMapperDto> doctorSubSpecialityMapperDtosList) {
		this.doctorSubSpecialityMapperDtosList = doctorSubSpecialityMapperDtosList;
	}

	public List<DoctorSpecialityMapperDto> getDoctorSpecialityMapperDtosList() {
		return doctorSpecialityMapperDtosList;
	}

	public void setDoctorSpecialityMapperDtosList(List<DoctorSpecialityMapperDto> doctorSpecialityMapperDtosList) {
		this.doctorSpecialityMapperDtosList = doctorSpecialityMapperDtosList;
	}

	public String getPwdExpiry() {
		return pwdExpiry;
	}

	public void setPwdExpiry(String pwdExpiry) {
		this.pwdExpiry = pwdExpiry;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Character getIsExpiryRequired() {
		return isExpiryRequired;
	}

	public void setIsExpiryRequired(Character isExpiryRequired) {
		this.isExpiryRequired = isExpiryRequired;
	}

	private List<DependentDetailsDto> dependentDetailsDtosList;
	
	
	
	public String getIdentificationName() {
		return identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public String getDocDesignationDescription() {
		return docDesignationDescription;
	}

	public void setDocDesignationDescription(String docDesignationDescription) {
		this.docDesignationDescription = docDesignationDescription;
	}

	public String getDocCategoryDescription() {
		return docCategoryDescription;
	}

	public void setDocCategoryDescription(String docCategoryDescription) {
		this.docCategoryDescription = docCategoryDescription;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public List<DependentDetailsDto> getDependentDetailsDtosList() {
		return dependentDetailsDtosList;
	}

	public void setDependentDetailsDtosList(List<DependentDetailsDto> dependentDetailsDtosList) {
		this.dependentDetailsDtosList = dependentDetailsDtosList;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDoctorNumber() {
		return doctorNumber;
	}

	public void setDoctorNumber(Integer doctorNumber) {
		this.doctorNumber = doctorNumber;
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getModalityId() {
		return modalityId;
	}

	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}

	public Integer getModalityTypeId() {
		return modalityTypeId;
	}

	public void setModalityTypeId(Integer modalityTypeId) {
		this.modalityTypeId = modalityTypeId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
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

	public Character getIsVip() {
		return isVip;
	}

	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}

	public String getVipRemark() {
		return vipRemark;
	}

	public void setVipRemark(String vipRemark) {
		this.vipRemark = vipRemark;
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

	public Integer getDocCategoryId() {
		return docCategoryId;
	}

	public void setDocCategoryId(Integer docCategoryId) {
		this.docCategoryId = docCategoryId;
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

	public Integer getDocDesignationId() {
		return docDesignationId;
	}

	public void setDocDesignationId(Integer docDesignationId) {
		this.docDesignationId = docDesignationId;
	}

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public String getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getAccessCardNumber() {
		return accessCardNumber;
	}

	public void setAccessCardNumber(String accessCardNumber) {
		this.accessCardNumber = accessCardNumber;
	}

	public Integer getAllowedOverBookingSlots() {
		return allowedOverBookingSlots;
	}

	public void setAllowedOverBookingSlots(Integer allowedOverBookingSlots) {
		this.allowedOverBookingSlots = allowedOverBookingSlots;
	}

	public Integer getDoctorDetailsId() {
		return doctorDetailsId;
	}

	public void setDoctorDetailsId(Integer doctorDetailsId) {
		this.doctorDetailsId = doctorDetailsId;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDoctorImage() {
		return doctorImage;
	}

	public void setDoctorImage(String doctorImage) {
		this.doctorImage = doctorImage;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Integer getDoctorCategoryId() {
		return doctorCategoryId;
	}

	public void setDoctorCategoryId(Integer doctorCategoryId) {
		this.doctorCategoryId = doctorCategoryId;
	}

	public Integer getEmployeeDesignationId() {
		return employeeDesignationId;
	}

	public void setEmployeeDesignationId(Integer employeeDesignationId) {
		this.employeeDesignationId = employeeDesignationId;
	}

	public Integer getSlotTimeSpan() {
		return slotTimeSpan;
	}

	public void setSlotTimeSpan(Integer slotTimeSpan) {
		this.slotTimeSpan = slotTimeSpan;
	}

	public List<UserRoleMapperDto> getSelectedHisRoleList() {
		return selectedHisRoleList;
	}

	public void setSelectedHisRoleList(List<UserRoleMapperDto> selectedHisRoleList) {
		this.selectedHisRoleList = selectedHisRoleList;
	}

	public List<OTIMSUserRoleMapperDto> getSelectedOTIMSRoleList() {
		return selectedOTIMSRoleList;
	}

	public void setSelectedOTIMSRoleList(List<OTIMSUserRoleMapperDto> selectedOTIMSRoleList) {
		this.selectedOTIMSRoleList = selectedOTIMSRoleList;
	}

	public List<UserUnitMapperDto> getSelectedUserUnitList() {
		return selectedUserUnitList;
	}

	public void setSelectedUserUnitList(List<UserUnitMapperDto> selectedUserUnitList) {
		this.selectedUserUnitList = selectedUserUnitList;
	}

	public Integer getQualificationId() {
		return qualificationId;
	}

	public void setQualificationId(Integer qualificationId) {
		this.qualificationId = qualificationId;
	}

	public String getQualificationDesc() {
		return qualificationDesc;
	}

	public void setQualificationDesc(String qualificationDesc) {
		this.qualificationDesc = qualificationDesc;
	}

	public Integer getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Integer degreeId) {
		this.degreeId = degreeId;
	}

	public String getDegreeDesc() {
		return degreeDesc;
	}

	public void setDegreeDesc(String degreeDesc) {
		this.degreeDesc = degreeDesc;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getDocumentFilePath() {
		return documentFilePath;
	}

	public void setDocumentFilePath(String documentFilePath) {
		this.documentFilePath = documentFilePath;
	}

	public Integer getDoctorDocId() {
		return doctorDocId;
	}

	public void setDoctorDocId(Integer doctorDocId) {
		this.doctorDocId = doctorDocId;
	}


	public Character getIsRMO() {
		return isRMO;
	}

	public void setIsRMO(Character isRMO) {
		this.isRMO = isRMO;
	}

	public String getDoctorPin() {
		return doctorPin;
	}

	public void setDoctorPin(String doctorPin) {
		this.doctorPin = doctorPin;
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

	public Character getIsPatientSearch() {
		return isPatientSearch;
	}

	public void setIsPatientSearch(Character isPatientSearch) {
		this.isPatientSearch = isPatientSearch;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDefaultCoversheetRights() {
		return defaultCoversheetRights;
	}

	public void setDefaultCoversheetRights(String defaultCoversheetRights) {
		this.defaultCoversheetRights = defaultCoversheetRights;
	}

	public Date getBirthDateNew() {
		return birthDateNew;
	}

	public void setBirthDateNew(Date birthDateNew) {
		this.birthDateNew = birthDateNew;
	}

	public Character getProcessShareAgainstUnsettledBill() {
		return processShareAgainstUnsettledBill;
	}

	public void setProcessShareAgainstUnsettledBill(Character processShareAgainstUnsettledBill) {
		this.processShareAgainstUnsettledBill = processShareAgainstUnsettledBill;
	}

	public Integer getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(Integer doctorType) {
		this.doctorType = doctorType;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getServiceDepartmentId() {
		return serviceDepartmentId;
	}

	public void setServiceDepartmentId(Integer serviceDepartmentId) {
		this.serviceDepartmentId = serviceDepartmentId;
	}

	public Integer getDoctorDepartmentId() {
		return doctorDepartmentId;
	}

	public void setDoctorDepartmentId(Integer doctorDepartmentId) {
		this.doctorDepartmentId = doctorDepartmentId;
	}

	public List<UserIndetificationDetailsDto> getIdentificationDetailsList() {
		return identificationDetailsList;
	}

	public void setIdentificationDetailsList(List<UserIndetificationDetailsDto> identificationDetailsList) {
		this.identificationDetailsList = identificationDetailsList;
	}

	public Integer getCreditDays() {
		return creditDays;
	}

	public void setCreditDays(Integer creditDays) {
		this.creditDays = creditDays;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

	public String getGeneralLedgerName() {
		return generalLedgerName;
	}

	public void setGeneralLedgerName(String generalLedgerName) {
		this.generalLedgerName = generalLedgerName;
	}

	public Integer getMedallReferalId() {
		return medallReferalId;
	}

	public void setMedallReferalId(Integer medallReferalId) {
		this.medallReferalId = medallReferalId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getPermanentAreaName() {
		return permanentAreaName;
	}

	public void setPermanentAreaName(String permanentAreaName) {
		this.permanentAreaName = permanentAreaName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	
	
}
