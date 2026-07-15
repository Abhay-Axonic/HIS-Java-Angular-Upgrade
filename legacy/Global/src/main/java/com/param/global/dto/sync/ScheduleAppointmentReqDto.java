package com.param.global.dto.sync;

import scala.util.parsing.combinator.testing.Str;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleAppointmentReqDto {
	private Integer appointmentId;
	private Integer appointmentMediaTypeId;
	private Integer patientId;
	private Integer doctorId;
	private Integer visitTypeId;
	private String appointmentDate;
	private String timeSlot;
	private Integer appointmentTypeId;
	private Integer categoryTypeId;
	private Integer departmentId;
	private String mobileNumber;
	private String dateOfBirth;
	private String gender;
	private Integer unitId;
	private String firstName;
	private String middleName;
	private String lastName;
	private Integer hospitalId;
	private String dateTime;
	private Integer appointmentStatusId;
	private String appointmentTime;
	private Integer doctorLocalId;
	private Integer globalUhid;
	private Integer applicationId;
	private String doctorEmailId;
	private String doctorMobNumber;
	private String doctorProfilePicture;
	private String transactionId;
	private String paymentType;
	private String transactionDate;
	private String status;
	private Character isPaid;
	private Double paidAmount;
	private Integer modalityId;
	private String medicalRecordAccessFlag;
	private String doctorAddress;
    private Integer reasonId;
    private Integer patientCategoryId;
    private String appointmentEndTime;
	private String patientAppointmentNote;
	private Integer isVideo;
	private String audioFilePath;
	private String endTimeSlot;
	private String uuid;
    private Integer globalOrgId;
    private Integer globalUnitId;
    private Integer channelId;
    private Integer classificationId;
    private String patSkypeId;
	private String patMailId;
	private String prefixDesc;
	private Character isAdvancePaid;
	private Integer isDoctorAdvanceCharge;
	private String currency;
	private Integer prevAppointmentId;
	private String doctorName;

	private String patAddressDesc;

	private String pinCode;

	private String twitterId;

	private String country;

	private String state;

	private String city;
	
	private String code;
	
	private String whatsappCallingCode;
	
	private String familyId;
	
	 private Integer countryId;

	  private Integer stateId;
	  
	  private Integer cityId;
	  
	  private  String appointmentResheduleStartTime;
	  private  Boolean isReschedule;

	private String appointmentResheduleDate;
	private String nationality;
	private Integer nationalityId;


	public String getPatSkypeId() {
		return patSkypeId;
	}
	public void setPatSkypeId(String patSkypeId) {
		this.patSkypeId = patSkypeId;
	}
	public String getPatMailId() {
		return patMailId;
	}
	public void setPatMailId(String patMailId) {
		this.patMailId = patMailId;
	}
	public Integer getClassificationId() {
		return classificationId;
	}
	public void setClassificationId(Integer classificationId) {
		this.classificationId = classificationId;
	}
	public String getDoctorEmailId() {
		return doctorEmailId;
	}
	public void setDoctorEmailId(String doctorEmailId) {
		this.doctorEmailId = doctorEmailId;
	}
	public String getDoctorMobNumber() {
		return doctorMobNumber;
	}
	public void setDoctorMobNumber(String doctorMobNumber) {
		this.doctorMobNumber = doctorMobNumber;
	}
	public String getDoctorProfilePicture() {
		return doctorProfilePicture;
	}
	public void setDoctorProfilePicture(String doctorProfilePicture) {
		this.doctorProfilePicture = doctorProfilePicture;
	}
	public Integer getDoctorLocalId() {
		return doctorLocalId;
	}
	public void setDoctorLocalId(Integer doctorLocalId) {
		this.doctorLocalId = doctorLocalId;
	}
	public Integer getGlobalUhid() {
		return globalUhid;
	}
	public void setGlobalUhid(Integer globalUhid) {
		this.globalUhid = globalUhid;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}
	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getAppointmentMediaTypeId() {
		return appointmentMediaTypeId;
	}
	public void setAppointmentMediaTypeId(Integer appointmentMediaTypeId) {
		this.appointmentMediaTypeId = appointmentMediaTypeId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public Integer getAppointmentTypeId() {
		return appointmentTypeId;
	}
	public void setAppointmentTypeId(Integer appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}
	public Integer getCategoryTypeId() {
		return categoryTypeId;
	}
	public void setCategoryTypeId(Integer categoryTypeId) {
		this.categoryTypeId = categoryTypeId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Character getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(Character isPaid) {
		this.isPaid = isPaid;
	}
	public Double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public Integer getModalityId() {
		return modalityId;
	}
	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}
	public String getMedicalRecordAccessFlag() {
		return medicalRecordAccessFlag;
	}
	public void setMedicalRecordAccessFlag(String medicalRecordAccessFlag) {
		this.medicalRecordAccessFlag = medicalRecordAccessFlag;
	}
	public String getDoctorAddress() {
		return doctorAddress;
	}
	public void setDoctorAddress(String doctorAddress) {
		this.doctorAddress = doctorAddress;
	}
	public Integer getReasonId() {
		return reasonId;
	}
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}
	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}
	public String getAppointmentEndTime() {
		return appointmentEndTime;
	}
	public void setAppointmentEndTime(String appointmentEndTime) {
		this.appointmentEndTime = appointmentEndTime;
	}
	public String getPatientAppointmentNote() {
		return patientAppointmentNote;
	}
	public void setPatientAppointmentNote(String patientAppointmentNote) {
		this.patientAppointmentNote = patientAppointmentNote;
	}
	public Integer getIsVideo() {
		return isVideo;
	}
	public void setIsVideo(Integer isVideo) {
		this.isVideo = isVideo;
	}
	public String getAudioFilePath() {
		return audioFilePath;
	}
	public void setAudioFilePath(String audioFilePath) {
		this.audioFilePath = audioFilePath;
	}
	public String getEndTimeSlot() {
		return endTimeSlot;
	}
	public void setEndTimeSlot(String endTimeSlot) {
		this.endTimeSlot = endTimeSlot;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Integer getGlobalOrgId() {
		return globalOrgId;
	}
	public void setGlobalOrgId(Integer globalOrgId) {
		this.globalOrgId = globalOrgId;
	}
	public Integer getGlobalUnitId() {
		return globalUnitId;
	}
	public void setGlobalUnitId(Integer globalUnitId) {
		this.globalUnitId = globalUnitId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public String getPrefixDesc() {
		return prefixDesc;
	}
	public void setPrefixDesc(String prefixDesc) {
		this.prefixDesc = prefixDesc;
	}
	public Character getIsAdvancePaid() {
		return isAdvancePaid;
	}
	public void setIsAdvancePaid(Character isAdvancePaid) {
		this.isAdvancePaid = isAdvancePaid;
	}
	public Integer getIsDoctorAdvanceCharge() {
		return isDoctorAdvanceCharge;
	}
	public void setIsDoctorAdvanceCharge(Integer isDoctorAdvanceCharge) {
		this.isDoctorAdvanceCharge = isDoctorAdvanceCharge;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getPrevAppointmentId() {
		return prevAppointmentId;
	}
	public void setPrevAppointmentId(Integer prevAppointmentId) {
		this.prevAppointmentId = prevAppointmentId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getPatAddressDesc() {
		return patAddressDesc;
	}
	public void setPatAddressDesc(String patAddressDesc) {
		this.patAddressDesc = patAddressDesc;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getWhatsappCallingCode() {
		return whatsappCallingCode;
	}
	public void setWhatsappCallingCode(String whatsappCallingCode) {
		this.whatsappCallingCode = whatsappCallingCode;
	}
	public String getFamilyId() {
		return familyId;
	}
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
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
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getAppointmentResheduleStartTime() {
		return appointmentResheduleStartTime;
	}
	public void setAppointmentResheduleStartTime(String appointmentResheduleStartTime) {
		this.appointmentResheduleStartTime = appointmentResheduleStartTime;
	}
	public Boolean getIsReschedule() {
		return isReschedule;
	}
	public void setIsReschedule(Boolean isReschedule) {
		this.isReschedule = isReschedule;
	}

	public String getAppointmentResheduleDate() {
		return appointmentResheduleDate;
	}

	public void setAppointmentResheduleDate(String appointmentResheduleDate) {
		this.appointmentResheduleDate = appointmentResheduleDate;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Integer getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(Integer nationalityId) {
		this.nationalityId = nationalityId;
	}
}
