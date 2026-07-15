package com.param.global.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;
import java.util.List;

import com.param.global.common.GlobalCommonDateUtils;

public class SlotMasterDto {

	private Integer slotId;

	private Integer previousSlotId;

	private Integer organizationId;

	private Integer unitId;

	private Integer billId;

	private Integer specialityId;

	private Integer doctorId;

	private String fromDate;

	private String toDate;

	private String appointmentDate;
	private String appointmentDate1;
	private Date appointmentDateNew;

	private String appointmentDate2;

	private Integer dayId;

	private String day;

	private String fromTimeString;

	private String toTimeString;

	private String fromTimeString2;

	private String toTimeString2;

	private Instant fromTime;

	private Instant toTime;

	private Integer timePerSlot;

	private Instant breakFromTime;

	private Instant breakToTime;

	private Integer slotTypeId;

	private Integer visitTypeId;

	private String visitTypeName;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Character status;

	private String prefixDesc;

	private Integer offset;

	private Integer count;

	private Integer noOfRecordsPerPage;

	private List<Integer> dayArrayList;

	private List<DoctorMasterDto> doctorArrayList;

	private List<SlotMasterDto> slotMasterDtosList;

	private List<TimeSlotsDto> breakSlotsList;
	
	private List<DoctorMasterDto> unitArrayList;

	private String doctorName;

	private String specialityName;

	private Integer modalityId;

	private Integer subSpecialityId;

	private String subSpecialityName;

	private String modalityTypeDesc;

	private String modalityDesc;

	private Integer noOfSlots;

	private String uhidNumber;

	private String remark;

	private Integer appointmentId;

	private Integer patientId;

	private String patientName;

	private String mobileNumber;

	private String email;

	private String address;

	private Integer prefixId;

	private String prefixCode;

	private Integer genderId;

	private String genderCode;

	private String age;

	private Integer appointmentStatusId;

	private Integer slotStatusId;

	private Integer cancelReasonId;

	private Character cancelledBy;

	private String appointmentStatusDesc;

	private String firstName;

	private String middleName;

	private String lastName;

	private String dob;
	
	private String dobForDatePicker;

	private Integer rescheduleReasonId;

	private Character rescheduledBy;

	private Character reSchedule;

	private Integer appointmentTypeId;

	private Integer appointmentListType;

	private List<Integer> specialityIdList;

	private List<Integer> doctorIdList;

	private List<Integer> modalityTypeIdList;

	private List<Integer> modalityIdList;

	private List<Integer> appointmentStatusIdList;

	private List<Integer> visitTypeIdList;

	private Integer admissionId;

	private Integer modalitySpecialityId;

	private Integer year;

	private Integer month;

	private String currentDate; // format should be yyyy-mm-dd

	private Integer encounterId;

	private Date encounterDate;

	private String encounterTime;

	private String encounterNumber;

	private int clinicId;

	private Integer statusId;

	private Integer nursingStationId;

	private Integer userDefinedVisitTypeId;

	private String barCode;

	private String genderDec;

	private Character isConsultationStatus;

	private Character isAssignmentCompStatus;

	private Integer isBlockedUnblocked;

	private String consultationStopTime;

	private String referralTypeName;

	private String referralName;
	
	private Integer referralId;

	private String referralRemark;

	private Integer registrationTypeId;

	private Integer reasonId;

	private String reasonDesc;

	private String blockedRemark;

	private Integer priorityId;

	private Integer allowedOverBookingSlots;

	private Integer ehcPackageId;

	private Integer parentPackageId;

	private String packageName;

	private BigDecimal packageBasePrice;

	private Integer isBlocked;

	private Integer paymentEntitlementTypeId;

	private Integer defaultSelfTariffId;

	private String imagePath;

	private Integer modalitySubSpecialityId;

	private Integer equipmentId;

	private String consultationStartTime;

	private String assignmentStartTime;

	private String assignmentStopTime;

	private Integer assignmentStartBy;

	private Integer consultationStartBy;

	private Integer assignmentStopBy;

	private Integer assignmentStartRoleId;

	private Integer consultationStartRoleId;

	private Integer assignmentStopRoleId;

	private String assignmentStopTimeDate;

	private String assignmentStartTimeDate;

	private String consultationStartTimeDate;

	private String consultationStopTimeDate;

	private Character isFromEncounter;

	private Integer orderDetailsId;

	private Integer contractId;

	private Integer payeeId;

	private String arrivalTime;

	private List<Integer> registrationTypeIdList;

	private String zipCode;

	private Integer countryId;

	private Integer stateId;

	private Integer districtId;

	private Integer cityId;

	private Integer areaId;

	private Integer maritalStatusId;

	private Integer queueNumber;

	private Integer tokenNumber;

	private Character isPriority;

	private Integer encounterDetailsMapperId;

	private String senderId;

	private Integer patientCategoryId;

	private String originalFromTime;

	private Character isCritical;

	private String ageYear;

	private Integer isCriticalOrder;

	private Integer isPriorityOrder;

	private String countryCallingCode;

	private Integer counterId;

	private Integer counterDesc;

	private Character isCalled;

	private Integer cabinId;

	private Character isFromCoversheet;

	private Character isFromKioskToken;

	private String sender;

	private Character generateToken;

	private Integer dummyTokenNumber;

	private Integer callCount;

	private String cardNo;

	private String opt;

	private Character deptWiseAppointment;

	private Integer oldDocId;

	private Character isDirectEncounter;

	private Character isKiosk;

	private String lastVisitDate;

	private String clinicDescription;

	private String mpshahPatientId;

	private String appointmentTime;

	private Integer itemId;

	private String itemName;

	private Integer bedId;

	private String bedName;

	private Character nursingAccepatance;

	private Integer openCoversheetWithEncounterId;

	private Character isVip;

	private Integer encounterTypeId;

	private Integer consultationMasterId;

	private Integer againstEncId;

	private Character isPaidAppointment;

	private Integer serviceRenderId;
	
	private Integer serviceId;

	private String yearSlots;

	private Integer slotPatientDetailsListsSize;

	private BigDecimal serviceAmount;
	
	private Integer serviceMasterId;
	
    private Integer taxId;
	
	private BigDecimal taxPercentage;
	
	private Character isCyclicAppointment;
	
	private OrderDetailsPackageMapperDto orderDetailsPackageMapperDto;
	
	private String serviceCode;
	
	private String serviceStandardName;
	
	private Integer availableQuantity;
	
	private Integer preferredDocId;
	
	private String preferedDocName;
	
	private boolean result;
	
	private String billDate;
	
	private  Integer specialityQueueNumber;
	
	private String encounterDateWithTime;
	private String encounterDateWithTimeString;
	private String consultationStartTimeWithTime;
	
	private String billNumber;
	private String serviceStandardCode;
	private Character isAppointmentRequired;
	
	private Integer preferredDoctorId;
	
	private Character isDoctorShareReq;
	
	private Character override;
	
	private Integer orderId;
	
	private List<String> appointmentDateList;
	
	private Integer reasonForAppointmentId;
	
	private Integer serviceSpecialityId;
	
	private Integer labSpecialityId;
	
	private String docNameTemp;
	
	private Character isFromDischargeSummary;
	
	private Integer againstAdmissionId;

	private Integer isTeleconsultationAppointment;
	
	private String whatsAppCallingNo;
	
	private String whatsAppCallingCode;
	
	private String skypeId;
	
	private String uuid;
	
	private Integer billStatusId;
	
	private Character isAppointmentFromMyLife;
	
	private String otherConsultants;
	
	private String searchString;
	
	private Integer identificationTypeId;

	private String identificationNumber;
	
	private Character isExpiryRequired;
	

	private Integer patientClassId;
	
	private Integer sourceId;
	
	private Integer referalDetailsId;
	
	private Integer referralTypeId;
	
	private String referralDesc;
	
	private String encRemark;
	private String cyclicOrdScheduledDate;
	
	private String fileSequenceNumber;
	
	private String hosPitalVisitAppointmentDate;
	private Date hosPitalVisitAppointmentDateNew;
	
	private String registrationNumber;
	
	private String emailDoctor;
	
	private String videoCallUrl;
	
	private Integer appointmentUniqueId;
	
	private String unitName;
	private String patientRemark;
	
	private Integer unitIdSavedData;
	
	private Boolean latestCall;
	
	private String nursingStationDesc;
	
    private String bloodGroup;

	private String createdTime;
	
	private String pFirstName;
	
	private String dFirstName;
	
	private String allergyTypeDesc;
	
	private String emergencyNumber;
	
	private String labelName;
	
	private boolean isPrnRequired;
	
	private String prnFormat;
	
	private String followUpDate;
	
	private Character billCancelled;
	
	private Integer paymentEntitlementId;
	
	private String associateCompanyDesc;
	
	private String scheduledDate;
	
	private String scheduledDateTime;

	private String searchKeyword;

	private String lastSlotDate;

	private Date lastActiveSlotDate;
	
	private boolean ageNew;
	
	private Timestamp firstEncounterDate;
	
	private boolean isSyncScheduling;

	private Integer userChatDetailsId;
	
	private Integer appointmentIdMyLife;
	
	private String healthNumber;
	
	private String healthId;

	private String userName;

	private Boolean newPatient;

	private Integer appointmentIdOld;

	public Integer getAppointmentIdOld() {
		return appointmentIdOld;
	}

	public void setAppointmentIdOld(Integer appointmentIdOld) {
		this.appointmentIdOld = appointmentIdOld;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Character getIsExpiryRequired() {
		return isExpiryRequired;
	}

	public void setIsExpiryRequired(Character isExpiryRequired) {
		this.isExpiryRequired = isExpiryRequired;
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

	
	
	public String getOtherConsultants() {
		return otherConsultants;
	}

	public void setOtherConsultants(String otherConsultants) {
		this.otherConsultants = otherConsultants;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Character getIsAppointmentFromMyLife() {
		return isAppointmentFromMyLife;
	}

	public void setIsAppointmentFromMyLife(Character isAppointmentFromMyLife) {
		this.isAppointmentFromMyLife = isAppointmentFromMyLife;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getWhatsAppCallingCode() {
		return whatsAppCallingCode;
	}

	public void setWhatsAppCallingCode(String whatsAppCallingCode) {
		this.whatsAppCallingCode = whatsAppCallingCode;
	}

	public Integer getAgainstAdmissionId() {
		return againstAdmissionId;
	}

	public void setAgainstAdmissionId(Integer againstAdmissionId) {
		this.againstAdmissionId = againstAdmissionId;
	}

	public Character getIsFromDischargeSummary() {
		return isFromDischargeSummary;
	}

	public void setIsFromDischargeSummary(Character isFromDischargeSummary) {
		this.isFromDischargeSummary = isFromDischargeSummary;
	}

	public String getDocNameTemp() {
		return docNameTemp;
	}

	public void setDocNameTemp(String docNameTemp) {
		this.docNameTemp = docNameTemp;
	}

	public Integer getReasonForAppointmentId() {
		return reasonForAppointmentId;
	}

	public void setReasonForAppointmentId(Integer reasonForAppointmentId) {
		this.reasonForAppointmentId = reasonForAppointmentId;
	}

	public Character getOverride() {
		return override;
	}

	public void setOverride(Character override) {
		this.override = override;
	}

	public String getEncounterDateWithTime() {
		return encounterDateWithTime;
	}

	public void setEncounterDateWithTime(String encounterDateWithTime) {
		this.encounterDateWithTime = encounterDateWithTime;
	}

	public String getConsultationStartTimeWithTime() {
		return consultationStartTimeWithTime;
	}

	public void setConsultationStartTimeWithTime(String consultationStartTimeWithTime) {
		this.consultationStartTimeWithTime = consultationStartTimeWithTime;
	}

	public Integer getPreferredDoctorId() {
		return preferredDoctorId;
	}

	public void setPreferredDoctorId(Integer preferredDoctorId) {
		this.preferredDoctorId = preferredDoctorId;
	}

	public Character getIsAppointmentRequired() {
		return isAppointmentRequired;
	}

	public void setIsAppointmentRequired(Character isAppointmentRequired) {
		this.isAppointmentRequired = isAppointmentRequired;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getPreferedDocName() {
		return preferedDocName;
	}

	public void setPreferedDocName(String preferedDocName) {
		this.preferedDocName = preferedDocName;
	}

	public Integer getPreferredDocId() {
		return preferredDocId;
	}

	public void setPreferredDocId(Integer preferredDocId) {
		this.preferredDocId = preferredDocId;
	}

	public String getYearSlots() {
		return yearSlots;
	}

	public void setYearSlots(String yearSlots) {
		this.yearSlots = yearSlots;
	}

	public Character getIsPaidAppointment() {
		return isPaidAppointment;
	}

	public void setIsPaidAppointment(Character isPaidAppointment) {
		this.isPaidAppointment = isPaidAppointment;
	}

	public Integer getEncounterTypeId() {
		return encounterTypeId;
	}

	public void setEncounterTypeId(Integer encounterTypeId) {
		this.encounterTypeId = encounterTypeId;
	}

	public Character getIsVip() {
		return isVip;
	}

	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}

	public Integer getOpenCoversheetWithEncounterId() {
		return openCoversheetWithEncounterId;
	}

	public void setOpenCoversheetWithEncounterId(Integer openCoversheetWithEncounterId) {
		this.openCoversheetWithEncounterId = openCoversheetWithEncounterId;
	}

	public Character getNursingAccepatance() {
		return nursingAccepatance;
	}

	public void setNursingAccepatance(Character nursingAccepatance) {

		if (nursingAccepatance == 'N' || nursingAccepatance == 'P' || nursingAccepatance == 'R')
			this.nursingAccepatance = 'N';
		else
			this.nursingAccepatance = 'Y';
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getLastVisitDate() {
		return lastVisitDate;
	}

	public void setLastVisitDate(String lastVisitDate) {
		this.lastVisitDate = lastVisitDate;
	}

	public String getClinicDescription() {
		return clinicDescription;
	}

	public void setClinicDescription(String clinicDescription) {
		this.clinicDescription = clinicDescription;
	}

	public Character getIsKiosk() {
		return isKiosk;
	}

	public void setIsKiosk(Character isKiosk) {
		this.isKiosk = isKiosk;
	}

	private Character frontDeskArrival;

	private Character doctorDeskArrival;

	public Character getDoctorDeskArrival() {
		return doctorDeskArrival;
	}

	public void setDoctorDeskArrival(Character doctorDeskArrival) {
		this.doctorDeskArrival = doctorDeskArrival;
	}

	public Character getFrontDeskArrival() {
		return frontDeskArrival;
	}

	public void setFrontDeskArrival(Character frontDeskArrival) {
		this.frontDeskArrival = frontDeskArrival;
	}

	public Character getIsDirectEncounter() {
		return isDirectEncounter;
	}

	public void setIsDirectEncounter(Character isDirectEncounter) {
		this.isDirectEncounter = isDirectEncounter;
	}

	public Integer getOldDocId() {
		return oldDocId;
	}

	public void setOldDocId(Integer oldDocId) {
		this.oldDocId = oldDocId;
	}

	public Character getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(Character deptWiseAppointment) {
		this.deptWiseAppointment = deptWiseAppointment;
	}

	public Integer getDummyTokenNumber() {
		return dummyTokenNumber;
	}

	public void setDummyTokenNumber(Integer dummyTokenNumber) {
		this.dummyTokenNumber = dummyTokenNumber;
	}

	public Integer getCallCount() {
		return callCount;
	}

	public void setCallCount(Integer callCount) {
		this.callCount = callCount;
	}

	public Character getGenerateToken() {
		return generateToken;
	}

	public void setGenerateToken(Character generateToken) {
		this.generateToken = generateToken;
	}

	public Character getIsFromKioskToken() {
		return isFromKioskToken;
	}

	public void setIsFromKioskToken(Character isFromKioskToken) {
		this.isFromKioskToken = isFromKioskToken;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Character getIsFromCoversheet() {
		return isFromCoversheet;
	}

	public void setIsFromCoversheet(Character isFromCoversheet) {
		this.isFromCoversheet = isFromCoversheet;
	}

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
	}

	public Character getIsCalled() {
		return isCalled;
	}

	public void setIsCalled(Character isCalled) {
		this.isCalled = isCalled;
	}

	public Integer getCounterDesc() {
		return counterDesc;
	}

	public void setCounterDesc(Integer counterDesc) {
		this.counterDesc = counterDesc;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}

	public Integer getIsCriticalOrder() {
		return isCriticalOrder;
	}

	public void setIsCriticalOrder(Integer isCriticalOrder) {
		this.isCriticalOrder = isCriticalOrder;
	}

	public Integer getIsPriorityOrder() {
		return isPriorityOrder;
	}

	public void setIsPriorityOrder(Integer isPriorityOrder) {
		this.isPriorityOrder = isPriorityOrder;
	}

	public String getAgeYear() {
		return ageYear;
	}

	public void setAgeYear(String ageYear) {
		this.ageYear = ageYear;
	}

	public Character getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Character isCritical) {
		this.isCritical = isCritical;
	}

	public Integer getSpecialityQueueNumber() {
		return specialityQueueNumber;
	}

	public void setSpecialityQueueNumber(Integer specialityQueueNumber) {
		this.specialityQueueNumber = specialityQueueNumber;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getEncounterDetailsMapperId() {
		return encounterDetailsMapperId;
	}

	public void setEncounterDetailsMapperId(Integer encounterDetailsMapperId) {
		this.encounterDetailsMapperId = encounterDetailsMapperId;
	}

	public Character getIsPriority() {
		return isPriority;
	}

	public void setIsPriority(Character isPriority) {
		this.isPriority = isPriority;
	}

	public Integer getQueueNumber() {
		return queueNumber;
	}

	public void setQueueNumber(Integer queueNumber) {
		this.queueNumber = queueNumber;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public Integer getMaritalStatusId() {
		return maritalStatusId;
	}

	public void setMaritalStatusId(Integer maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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

	public List<Integer> getRegistrationTypeIdList() {
		return registrationTypeIdList;
	}

	public void setRegistrationTypeIdList(List<Integer> registrationTypeIdList) {
		this.registrationTypeIdList = registrationTypeIdList;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
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

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Character getIsFromEncounter() {
		return isFromEncounter;
	}

	public void setIsFromEncounter(Character isFromEncounter) {
		this.isFromEncounter = isFromEncounter;
	}

	public String getAssignmentStopTimeDate() {
		return assignmentStopTimeDate;
	}

	public void setAssignmentStopTimeDate(String assignmentStopTimeDate) {
		this.assignmentStopTimeDate = assignmentStopTimeDate;
	}

	public String getAssignmentStartTimeDate() {
		return assignmentStartTimeDate;
	}

	public void setAssignmentStartTimeDate(String assignmentStartTimeDate) {
		this.assignmentStartTimeDate = assignmentStartTimeDate;
	}

	public String getConsultationStartTimeDate() {
		return consultationStartTimeDate;
	}

	public void setConsultationStartTimeDate(String consultationStartTimeDate) {
		this.consultationStartTimeDate = consultationStartTimeDate;
	}

	public String getConsultationStopTimeDate() {
		return consultationStopTimeDate;
	}

	public void setConsultationStopTimeDate(String consultationStopTimeDate) {
		this.consultationStopTimeDate = consultationStopTimeDate;
	}

	public Integer getConsultationStartBy() {
		return consultationStartBy;
	}

	public void setConsultationStartBy(Integer consultationStartBy) {
		this.consultationStartBy = consultationStartBy;
	}

	public Integer getConsultationStartRoleId() {
		return consultationStartRoleId;
	}

	public void setConsultationStartRoleId(Integer consultationStartRoleId) {
		this.consultationStartRoleId = consultationStartRoleId;
	}

	public String getAssignmentStopTime() {
		return assignmentStopTime;
	}

	public void setAssignmentStopTime(String assignmentStopTime) {
		this.assignmentStopTime = assignmentStopTime;
	}

	public Integer getAssignmentStartBy() {
		return assignmentStartBy;
	}

	public void setAssignmentStartBy(Integer assignmentStartBy) {
		this.assignmentStartBy = assignmentStartBy;
	}

	public Integer getAssignmentStopBy() {
		return assignmentStopBy;
	}

	public void setAssignmentStopBy(Integer assignmentStopBy) {
		this.assignmentStopBy = assignmentStopBy;
	}

	public Integer getAssignmentStartRoleId() {
		return assignmentStartRoleId;
	}

	public void setAssignmentStartRoleId(Integer assignmentStartRoleId) {
		this.assignmentStartRoleId = assignmentStartRoleId;
	}

	public Integer getAssignmentStopRoleId() {
		return assignmentStopRoleId;
	}

	public void setAssignmentStopRoleId(Integer assignmentStopRoleId) {
		this.assignmentStopRoleId = assignmentStopRoleId;
	}

	public String getAssignmentStartTime() {
		return assignmentStartTime;
	}

	public void setAssignmentStartTime(String assignmentStartTime) {
		this.assignmentStartTime = assignmentStartTime;
	}

	public String getConsultationStartTime() {
		return consultationStartTime;
	}

	public void setConsultationStartTime(String consultationStartTime) {
		this.consultationStartTime = consultationStartTime;
	}

	public Integer getModalitySubSpecialityId() {
		return modalitySubSpecialityId;
	}

	public void setModalitySubSpecialityId(Integer modalitySubSpecialityId) {
		this.modalitySubSpecialityId = modalitySubSpecialityId;
	}

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	private List<SlotPatientDetails> slotPatientDetailsList;

	public Integer getPaymentEntitlementTypeId() {
		return paymentEntitlementTypeId;
	}

	public void setPaymentEntitlementTypeId(Integer paymentEntitlementTypeId) {
		this.paymentEntitlementTypeId = paymentEntitlementTypeId;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Integer getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Integer isBlocked) {
		this.isBlocked = isBlocked;
	}

	public Integer getEhcPackageId() {
		return ehcPackageId;
	}

	public void setEhcPackageId(Integer ehcPackageId) {
		this.ehcPackageId = ehcPackageId;
	}

	public Integer getAllowedOverBookingSlots() {
		return allowedOverBookingSlots;
	}

	public void setAllowedOverBookingSlots(Integer allowedOverBookingSlots) {
		this.allowedOverBookingSlots = allowedOverBookingSlots;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getIsBlockedUnblocked() {
		return isBlockedUnblocked;
	}

	public void setIsBlockedUnblocked(Integer isBlockedUnblocked) {
		this.isBlockedUnblocked = isBlockedUnblocked;
	}

	public List<SlotPatientDetails> getSlotPatientDetailsList() {
		return slotPatientDetailsList;
	}

	public void setSlotPatientDetailsList(List<SlotPatientDetails> slotPatientDetailsList) {
		this.slotPatientDetailsList = slotPatientDetailsList;
	}

	public String getConsultationStopTime() {
		return consultationStopTime;
	}

	public void setConsultationStopTime(String consultationStopTime) {
		this.consultationStopTime = consultationStopTime;
	}

	public String getReferralTypeName() {
		return referralTypeName;
	}

	public void setReferralTypeName(String referralTypeName) {
		this.referralTypeName = referralTypeName;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public String getReferralRemark() {
		return referralRemark;
	}

	public void setReferralRemark(String referralRemark) {
		this.referralRemark = referralRemark;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getBlockedRemark() {
		return blockedRemark;
	}

	public void setBlockedRemark(String blockedRemark) {
		this.blockedRemark = blockedRemark;
	}

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;

	}

	public String getCurrentDate() {
		return currentDate;
	}

	public Character getIsConsultationStatus() {
		return isConsultationStatus;
	}

	public void setIsConsultationStatus(Character isConsultationStatus) {
		this.isConsultationStatus = isConsultationStatus;
	}

	public Character getIsAssignmentCompStatus() {
		return isAssignmentCompStatus;
	}

	public void setIsAssignmentCompStatus(Character isAssignmentCompStatus) {
		this.isAssignmentCompStatus = isAssignmentCompStatus;
	}

	public String getGenderDec() {
		return genderDec;
	}

	public void setGenderDec(String genderDec) {
		this.genderDec = genderDec;
	}

	public Integer getUserDefinedVisitTypeId() {
		return userDefinedVisitTypeId;
	}

	public void setUserDefinedVisitTypeId(Integer userDefinedVisitTypeId) {
		this.userDefinedVisitTypeId = userDefinedVisitTypeId;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public int getClinicId() {
		return clinicId;
	}

	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}

	public Integer getStatusId() {
		return statusId;

	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getEncounterTime() {
		return encounterTime;
	}

	public Date getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(Date encounterDate) {
		this.encounterDate = encounterDate;
	}

	public void setEncounterTime(String encounterTime) {
		this.encounterTime = encounterTime;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public String getAppointmentDate2() {
		return appointmentDate2;
	}

	public void setAppointmentDate2(String appointmentDate2) {
		this.appointmentDate2 = appointmentDate2;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getModalitySpecialityId() {
		return modalitySpecialityId;
	}

	public void setModalitySpecialityId(Integer modalitySpecialityId) {
		this.modalitySpecialityId = modalitySpecialityId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public List<Integer> getVisitTypeIdList() {
		return visitTypeIdList;
	}

	public void setVisitTypeIdList(List<Integer> visitTypeIdList) {
		this.visitTypeIdList = visitTypeIdList;
	}

	public List<Integer> getModalityTypeIdList() {
		return modalityTypeIdList;
	}

	public void setModalityTypeIdList(List<Integer> modalityTypeIdList) {
		this.modalityTypeIdList = modalityTypeIdList;
	}

	public List<Integer> getModalityIdList() {
		return modalityIdList;
	}

	public void setModalityIdList(List<Integer> modalityIdList) {
		this.modalityIdList = modalityIdList;
	}

	public List<Integer> getSpecialityIdList() {
		return specialityIdList;
	}

	public void setSpecialityIdList(List<Integer> specialityIdList) {
		this.specialityIdList = specialityIdList;
	}

	public List<Integer> getDoctorIdList() {
		return doctorIdList;
	}

	public void setDoctorIdList(List<Integer> doctorIdList) {
		this.doctorIdList = doctorIdList;
	}

	public List<Integer> getAppointmentStatusIdList() {
		return appointmentStatusIdList;
	}

	public void setAppointmentStatusIdList(List<Integer> appointmentStatusIdList) {
		this.appointmentStatusIdList = appointmentStatusIdList;
	}

	public Integer getAppointmentListType() {
		return appointmentListType;
	}

	public void setAppointmentListType(Integer appointmentListType) {
		this.appointmentListType = appointmentListType;
	}

	public Integer getAppointmentTypeId() {
		return appointmentTypeId;
	}

	public void setAppointmentTypeId(Integer appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}

	public Integer getPreviousSlotId() {
		return previousSlotId;
	}

	public void setPreviousSlotId(Integer previousSlotId) {
		this.previousSlotId = previousSlotId;
	}

	public Integer getRescheduleReasonId() {
		return rescheduleReasonId;
	}

	public void setRescheduleReasonId(Integer rescheduleReasonId) {
		this.rescheduleReasonId = rescheduleReasonId;
	}

	public Character getRescheduledBy() {
		return rescheduledBy;
	}

	public void setRescheduledBy(Character rescheduledBy) {
		this.rescheduledBy = rescheduledBy;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
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

	public String getAppointmentStatusDesc() {
		return appointmentStatusDesc;
	}

	public void setAppointmentStatusDesc(String appointmentStatusDesc) {
		this.appointmentStatusDesc = appointmentStatusDesc;
	}

	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	public Integer getSlotStatusId() {
		return slotStatusId;
	}

	public void setSlotStatusId(Integer slotStatusId) {
		this.slotStatusId = slotStatusId;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public Character getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(Character cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
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

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getFromTimeString2() {
		return fromTimeString2;
	}

	public void setFromTimeString2(String fromTimeString2) {
		this.fromTimeString2 = fromTimeString2;
	}

	public String getToTimeString2() {
		return toTimeString2;
	}

	public void setToTimeString2(String toTimeString2) {
		this.toTimeString2 = toTimeString2;
	}

	public Integer getNoOfSlots() {
		return noOfSlots;
	}

	public void setNoOfSlots(Integer noOfSlots) {
		this.noOfSlots = noOfSlots;
	}

	public String getModalityTypeDesc() {
		return modalityTypeDesc;
	}

	public void setModalityTypeDesc(String modalityTypeDesc) {
		this.modalityTypeDesc = modalityTypeDesc;
	}

	public String getModalityDesc() {
		return modalityDesc;
	}

	public void setModalityDesc(String modalityDesc) {
		this.modalityDesc = modalityDesc;
	}

	public Integer getModalityId() {
		return modalityId;
	}

	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getFromTimeString() {
		return fromTimeString;
	}

	public void setFromTimeString(String fromTimeString) {
		this.fromTimeString = fromTimeString;
	}

	public String getToTimeString() {
		return toTimeString;
	}

	public void setToTimeString(String toTimeString) {
		this.toTimeString = toTimeString;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<TimeSlotsDto> getBreakSlotsList() {
		return breakSlotsList;
	}

	public void setBreakSlotsList(List<TimeSlotsDto> breakSlotsList) {
		this.breakSlotsList = breakSlotsList;
	}

	public List<Integer> getDayArrayList() {
		return dayArrayList;
	}

	public void setDayArrayList(List<Integer> dayArrayList) {
		this.dayArrayList = dayArrayList;
	}

	public List<DoctorMasterDto> getDoctorArrayList() {
		return doctorArrayList;
	}

	public void setDoctorArrayList(List<DoctorMasterDto> doctorArrayList) {
		this.doctorArrayList = doctorArrayList;
	}

	public List<SlotMasterDto> getSlotMasterDtosList() {
		return slotMasterDtosList;
	}

	public void setSlotMasterDtosList(List<SlotMasterDto> slotMasterDtosList) {
		this.slotMasterDtosList = slotMasterDtosList;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		if (fromTime!=null && !fromTime.equals(""))
		{
			LocalTime time = LocalTime.parse(fromTime);
			LocalDate today = LocalDate.now();
			this.fromTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.fromTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		if (toTime!=null && !toTime.equals(""))
		{
			LocalTime time = LocalTime.parse(toTime);
			LocalDate today = LocalDate.now();
			this.toTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.toTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public Integer getTimePerSlot() {
		return timePerSlot;
	}

	public void setTimePerSlot(Integer timePerSlot) {
		this.timePerSlot = timePerSlot;
	}

	public Instant getBreakFromTime() {
		return breakFromTime;
	}

	public void setBreakFromTime(String breakFromTime) {
		if (breakFromTime!=null && !breakFromTime.equals(""))
		{
			LocalTime time = LocalTime.parse(breakFromTime);
			LocalDate today = LocalDate.now();
			this.breakFromTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.breakFromTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public Instant getBreakToTime() {
		return breakToTime;
	}

	public void setBreakToTime(String breakToTime) {
		if (breakToTime!=null && !breakToTime.equals(""))
		{
			LocalTime time = LocalTime.parse(breakToTime);
			LocalDate today = LocalDate.now();
			this.breakToTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.breakToTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public Integer getSlotTypeId() {
		return slotTypeId;
	}

	public void setSlotTypeId(Integer slotTypeId) {
		this.slotTypeId = slotTypeId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getOriginalFromTime() {
		return originalFromTime;
	}

	public void setOriginalFromTime(String originalFromTime) {
		this.originalFromTime = originalFromTime;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getMpshahPatientId() {
		return mpshahPatientId;
	}

	public void setMpshahPatientId(String mpshahPatientId) {
		this.mpshahPatientId = mpshahPatientId;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public String getBedName() {
		return bedName;
	}

	public void setBedName(String bedName) {
		this.bedName = bedName;
	}

	public BigDecimal getPackageBasePrice() {
		return packageBasePrice;
	}

	public void setPackageBasePrice(BigDecimal packageBasePrice) {
		this.packageBasePrice = packageBasePrice;
	}

	public Integer getConsultationMasterId() {
		return consultationMasterId;
	}

	public void setConsultationMasterId(Integer consultationMasterId) {
		this.consultationMasterId = consultationMasterId;
	}

	public Integer getAgainstEncId() {
		return againstEncId;
	}

	public void setAgainstEncId(Integer againstEncId) {
		this.againstEncId = againstEncId;
	}

	public String getPrefixDesc() {
		return prefixDesc;
	}

	public void setPrefixDesc(String prefixDesc) {
		this.prefixDesc = prefixDesc;
	}

	public Integer getServiceRenderId() {
		return serviceRenderId;
	}

	public void setServiceRenderId(Integer serviceRenderId) {
		this.serviceRenderId = serviceRenderId;
	}

	public Integer getSlotPatientDetailsListsSize() {
		return slotPatientDetailsListsSize;
	}

	public void setSlotPatientDetailsListsSize(Integer slotPatientDetailsListsSize) {
		this.slotPatientDetailsListsSize = slotPatientDetailsListsSize;
	}

	public Character getReSchedule() {
		return reSchedule;
	}

	public void setReSchedule(Character reSchedule) {
		this.reSchedule = reSchedule;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public BigDecimal getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(BigDecimal taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Character getIsCyclicAppointment() {
		return (isCyclicAppointment==null)?'N':'Y';
	}

	public void setIsCyclicAppointment(Character isCyclicAppointment) {
		this.isCyclicAppointment=(isCyclicAppointment==null)?'N':'Y';
	}

	public OrderDetailsPackageMapperDto getOrderDetailsPackageMapperDto() {
		return orderDetailsPackageMapperDto;
	}

	public void setOrderDetailsPackageMapperDto(OrderDetailsPackageMapperDto orderDetailsPackageMapperDto) {
		this.orderDetailsPackageMapperDto = orderDetailsPackageMapperDto;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}
	
	public String getDobForDatePicker() {
		return dobForDatePicker;
	}

	public void setDobForDatePicker(String dobForDatePicker) {
		this.dobForDatePicker = dobForDatePicker;
	}

	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public Character getIsDoctorShareReq() {
		return isDoctorShareReq;
	}

	public void setIsDoctorShareReq(Character isDoctorShareReq) {
		this.isDoctorShareReq = isDoctorShareReq;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public List<String> getAppointmentDateList() {
		return appointmentDateList;
	}

	public void setAppointmentDateList(List<String> appointmentDateList) {
		this.appointmentDateList = appointmentDateList;
	}

	public Integer getServiceSpecialityId() {
		return serviceSpecialityId;
	}

	public void setServiceSpecialityId(Integer serviceSpecialityId) {
		this.serviceSpecialityId = serviceSpecialityId;
	}

	public Integer getLabSpecialityId() {
		return labSpecialityId;
	}

	public void setLabSpecialityId(Integer labSpecialityId) {
		this.labSpecialityId = labSpecialityId;
	}

	public Integer getIsTeleconsultationAppointment() {
		return isTeleconsultationAppointment;
	}

	public void setIsTeleconsultationAppointment(Integer isTeleconsultationAppointment) {
		this.isTeleconsultationAppointment = isTeleconsultationAppointment;
	}


	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getWhatsAppCallingNo() {
		return whatsAppCallingNo;
	}

	public void setWhatsAppCallingNo(String whatsAppCallingNo) {
		this.whatsAppCallingNo = whatsAppCallingNo;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public Integer getReferalDetailsId() {
		return referalDetailsId;
	}

	public void setReferalDetailsId(Integer referalDetailsId) {
		this.referalDetailsId = referalDetailsId;
	}

	public Integer getReferralTypeId() {
		return referralTypeId;
	}

	public void setReferralTypeId(Integer referralTypeId) {
		this.referralTypeId = referralTypeId;
	}

	public String getReferralDesc() {
		return referralDesc;
	}

	public void setReferralDesc(String referralDesc) {
		this.referralDesc = referralDesc;
	}

	public String getEncRemark() {
		return encRemark;
	}

	public void setEncRemark(String encRemark) {
		this.encRemark = encRemark;
	}

	public String getCyclicOrdScheduledDate() {
		return cyclicOrdScheduledDate;
	}

	public void setCyclicOrdScheduledDate(String cyclicOrdScheduledDate) {
		this.cyclicOrdScheduledDate = cyclicOrdScheduledDate;
	}
	
	public String getFileSequenceNumber() {
		return fileSequenceNumber;
	}

	public void setFileSequenceNumber(String fileSequenceNumber) {
		this.fileSequenceNumber = fileSequenceNumber;
	}

	public String getHosPitalVisitAppointmentDate() {
		return hosPitalVisitAppointmentDate;
	}

	public void setHosPitalVisitAppointmentDate(String hosPitalVisitAppointmentDate) {
		this.hosPitalVisitAppointmentDate = hosPitalVisitAppointmentDate;
	}

	public Date getAppointmentDateNew() {
		return appointmentDateNew;
	}

	public void setAppointmentDateNew(Date appointmentDateNew) {
		this.appointmentDateNew = appointmentDateNew;
	}

	public Date getHosPitalVisitAppointmentDateNew() {
		return hosPitalVisitAppointmentDateNew;
	}

	public void setHosPitalVisitAppointmentDateNew(Date hosPitalVisitAppointmentDateNew) {
		this.hosPitalVisitAppointmentDateNew = hosPitalVisitAppointmentDateNew;
	}

	public String getEmailDoctor() {
		return emailDoctor;
	}

	public void setEmailDoctor(String emailDoctor) {
		this.emailDoctor = emailDoctor;
	}

	public String getVideoCallUrl() {
		return videoCallUrl;
	}

	public void setVideoCallUrl(String videoCallUrl) {
		this.videoCallUrl = videoCallUrl;
	}

	public Integer getAppointmentUniqueId() {
		return appointmentUniqueId;
	}

	public void setAppointmentUniqueId(Integer appointmentUniqueId) {
		this.appointmentUniqueId = appointmentUniqueId;
	}

	public String getAppointmentDate1() {
		return appointmentDate1;
	}

	public void setAppointmentDate1(String appointmentDate1) {
		this.appointmentDate1 = appointmentDate1;
	}

	public String getEncounterDateWithTimeString() {
		return encounterDateWithTimeString;
	}

	public void setEncounterDateWithTimeString(String encounterDateWithTimeString) {
		this.encounterDateWithTimeString = encounterDateWithTimeString;
	}

	public List<DoctorMasterDto> getUnitArrayList() {
		return unitArrayList;
	}

	public void setUnitArrayList(List<DoctorMasterDto> unitArrayList) {
		this.unitArrayList = unitArrayList;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getPatientRemark() {
		return patientRemark;
	}

	public void setPatientRemark(String patientRemark) {
		this.patientRemark = patientRemark;
	}

	public Integer getUnitIdSavedData() {
		return unitIdSavedData;
	}

	public void setUnitIdSavedData(Integer unitIdSavedData) {
		this.unitIdSavedData = unitIdSavedData;
	}

	public Boolean getLatestCall() {
		return latestCall;
	}

	public void setLatestCall(Boolean latestCall) {
		this.latestCall = latestCall;
	}

	public String getNursingStationDesc() {
		return nursingStationDesc;
	}

	public void setNursingStationDesc(String nursingStationDesc) {
		this.nursingStationDesc = nursingStationDesc;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getpFirstName() {
		return pFirstName;
	}

	public void setpFirstName(String pFirstName) {
		this.pFirstName = pFirstName;
	}

	public String getdFirstName() {
		return dFirstName;
	}

	public void setdFirstName(String dFirstName) {
		this.dFirstName = dFirstName;
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

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public boolean isPrnRequired() {
		return isPrnRequired;
	}

	public void setPrnRequired(boolean isPrnRequired) {
		this.isPrnRequired = isPrnRequired;
	}

	public String getPrnFormat() {
		return prnFormat;
	}

	public void setPrnFormat(String prnFormat) {
		this.prnFormat = prnFormat;
	}

	public String getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(String followUpDate) {
		this.followUpDate = followUpDate;
	}

	public Character getBillCancelled() {
		return billCancelled;
	}

	public void setBillCancelled(Character billCancelled) {
		this.billCancelled = billCancelled;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public String getAssociateCompanyDesc() {
		return associateCompanyDesc;
	}

	public void setAssociateCompanyDesc(String associateCompanyDesc) {
		this.associateCompanyDesc = associateCompanyDesc;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getScheduledDateTime() {
		return scheduledDateTime;
	}

	public void setScheduledDateTime(String scheduledDateTime) {
		this.scheduledDateTime = scheduledDateTime;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getLastSlotDate() {
		return lastSlotDate;
	}

	public void setLastSlotDate(String lastSlotDate) {
		this.lastSlotDate = lastSlotDate;
	}

	public Date getLastActiveSlotDate() {
		return lastActiveSlotDate;
	}

	public void setLastActiveSlotDate(Date lastActiveSlotDate) {
		this.lastActiveSlotDate = lastActiveSlotDate;
	}

	public boolean getAgeNew() {
		return ageNew;
	}

	public void setAgeNew(boolean ageNew) {
		this.ageNew = ageNew;
	}

	public Timestamp getFirstEncounterDate() {
		return firstEncounterDate;
	}

	public void setFirstEncounterDate(Timestamp firstEncounterDate) {
		this.firstEncounterDate = firstEncounterDate;
	}

	public boolean isSyncScheduling() {
		return isSyncScheduling;
	}

	public void setSyncScheduling(boolean isSyncScheduling) {
		this.isSyncScheduling = isSyncScheduling;
	}

	public Integer getUserChatDetailsId() {
		return userChatDetailsId;
	}

	public void setUserChatDetailsId(Integer userChatDetailsId) {
		this.userChatDetailsId = userChatDetailsId;
	}

	public Integer getAppointmentIdMyLife() {
		return appointmentIdMyLife;
	}

	public void setAppointmentIdMyLife(Integer appointmentIdMyLife) {
		this.appointmentIdMyLife = appointmentIdMyLife;
	}

	public String getHealthNumber() {
		return healthNumber;
	}

	public void setHealthNumber(String healthNumber) {
		this.healthNumber = healthNumber;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getNewPatient() {
		return newPatient;
	}

	public void setNewPatient(Boolean newPatient) {
		this.newPatient = newPatient;
	}
}
