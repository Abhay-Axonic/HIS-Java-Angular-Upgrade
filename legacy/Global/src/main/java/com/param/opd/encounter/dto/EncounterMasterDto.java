package com.param.opd.encounter.dto;

import java.math.BigDecimal;
import java.time.*;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.json.JSONArray;

import com.param.global.billing.dto.BillListResponse;
import com.param.global.billing.dto.ItemDetailsResponse;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.InvestigationOrderMasterNewDto;
import com.param.global.dto.KinDetailsDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.ReferralMasterDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.opd.coversheet.controller.PatientDiagnosisController;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDetailsDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;
import com.param.opd.coversheet.dto.VaccineImmunizationMapperDto;

public class EncounterMasterDto {

	private Integer encounterId;

	private Integer patientId;

	private Integer patientTypeId;

	private Integer contractId;

	private Integer visitTypeId;

	private Integer doctorId;

	private Integer sourceId;

	private Long encounterDate;

	private String encounterDateString;

	private String encounterDateString2;

	private Instant encounterTime;

	private Character isReferal;

	private Integer deptId;

	private Integer subDeptId;

	private String remark;

	private Integer kinDetailsId;

	private Integer paymentEntitlementTypeId;

	private String encounterNumber;

	private Long createdDate;

	private Integer createdBy;

	private Long updatedDate;

	private Integer updatedBy;

	private Character status;

	private Integer unitId;

	private Integer organizationId;

	private Date encounterDateTime;

	private List<KinDetailsDto> listKinDetailsDto;

	private Integer appointmentId;

	private Character isScheduledAppointment;

	private String doctorName;

	private Integer specialityId;

	private String specialityName;

	private Integer subSpecialityId;

	private String subSpecialityName;

	private Integer modalityId;

	private String modalityDesc;

	private Integer appointmentTypeId;

	private String currentDate; // format should be yyyy-mm-dd

	private Integer systemGeneratedVisitTypeId;

	private Integer userDefinedVisitTypeId;

	private Integer reasonIdForChangingVisitType;

	private String remarkForChangingVisitType;

	private String patientName;

	private String genderDec;

	private String age;

	private String uhidNumber;

	private String barCode;

	private Integer encounterTypeId;

	private Integer clinicId;

	private Integer referralTypeId;

	private Integer referralId;

	private String referralRemark;

	private String referralDesc;

	private String companyDesc;

	private String paymentEntitlementDesc;

	private String patientCategory;

	private Integer defaultSelfTariffId;

	private Integer serviceId;

	private Integer payeeId;

	private Character isAppointmentRequired;

	private SlotMasterDto appointmentObject;

	private String billNumber;

	private Double totalBillAmount;

	private Double outstanding;

	private Integer statusId;

	private Integer ehcPackageId;

	private BigDecimal packageBasePrice;

	private Integer genderId;

	private String genderCode;

	private String dob;

	private String ageString;

	private Integer companyId;

	private Integer counterId;

	private String fromDate;

	private String toDate;

	private String appoStatusId;

	private Integer admissionId;

	private Integer billingBedCategoryId;

	private Integer paymentEntitlementId;

	private Integer slotId;

	private Integer docQueueNumber;

	private Integer unitTokenNumber;

	private BigDecimal maxUnitTokenNumber;

	private BigDecimal maxDocQueueNumber;

	private Integer patientCategoryId;

	private Character isPriority;

	private Integer consultationSpecialityId;

	private Integer specialityQueueNumber;

	private BigDecimal maxSpecialityQueueNumber;

	private Character isCritical;

	private String counterDesc;

	private Character isCalled;

	private Integer cabinId;

	private String cabinDesc;

	private String consultationStartTime;

	private String consultationStopTime;

	private Double totalBillAmt;

	private Double paidAmount;

	private String mpesaTxcode;

	private Double taxAmt;

	private Double serviceAmt;

	private Character isPreOrPostBilling;

	private Integer dummyTokenNumber;

	private Integer callCount;

	private Character deptWiseAppointment;

	private Character isNursingAssessmentRequiredForSpeciality;

	private Integer oldDocId;

	private Integer reasonId;

	private Integer patientClassId;

	private Character frontDeskArrival;

	private Character doctorDeskArrival;

	private ReferralMasterDto referralMasterDto;

	private String appoitmentDate;

	private String appoitmentTime;

	private String encounterDt;

	private List<Integer> doctorList;

	private Character isRMO;

	private List<ServiceMasterDto> listServiceMasterDto;

	private Double serviceAmount;

	private String serviceName;

	// added by dinesh for multimode payment on kiosk
	private List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList;

	private Character isTransferToActiveDeposit;

	private Character isDocShare;

	private Character isDocReferral;

	private Integer reviewAgainstAncId;

	private List<Integer> orderDetailsIds;

	private Integer oldSlotId;
	
	private Integer orderDetailsId;

	private String serviceDesc;

	private String serviceSpecialityName;

	private Character isPaidAppointment;

	private String firstName;

	private String middleName;

	private String lastName;

	private Integer prefixId;

	private String mobileNumber;

	private String countryCallingCode;

	private Integer registrationTypeId;

	private Integer orderDetailId;

	private Integer childEncounterId;

	private Integer consultationMpprId;

	private BigDecimal docSharePer;

	private BigDecimal refDocSharePer;
	
	private List<PaymentDepositDto> paymentDepositDetailsList;
	
	private Character isCyclicAppointment;
	
	private Integer preferredDocId;
	
	private List<OTReservationDto> otReservationList;
	
	private Character isReshedule;
	
	private Integer rescheduleAppointmentId;
	
	private Integer rescheduleReasonId;
	
	private Character reschedulledBy;
	
	private Integer previousSlotId;
	
	private Character isTariffRateApplicable;
	
	private Double totalDepositConsumption;
	
	private Character isFromAppointmentScheduling; 
	
	private Character isAppointmentFromMyLife;
	
	private Integer billingMasterId;
	
	private Double selfPayable;
		
	private Double creditPayable;
	
	private Double selfDueAmount;
	
	private Double discountAmount;
	
	private String userName;
	
	private String email;
	
	private String patHistory;
	
	private Character isKioskApplicable;
	
	private Integer bedId;
	
	private Integer wardId;
	
	private Integer roomId;
	
	private String followUpDate;
	
	private Integer assoCompanyId;
	
	private PatientRegistrationDto patientRegistrationDto;
	
	private Character checkInFromDoctorApp;
	
	private String searchString;
	
	private Integer identificationTypeId;

	private String identificationNumber;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private Character isFromWebAppointment;
	
	private boolean capExist;
	private Integer admisisonId;

	private String appointmentDate;
	
	private String fromTimeString;
	
	private Integer advisedById;
	
	private String advisedByName;
	private String  visitTime;
	private Boolean doctorLatestCall;
	private Integer dischargeStatusId;

	private List<ComplaintsDto> complaintlistData;
	private List<ComplaintAppointmentDto> complaintlistDataLastVisit;
	private List<ComplaintAppointmentDetailsDto> listComplaintDataSave;
	private List<InvestigationOrderMasterNewDto> labOrderList;
	private List<DiagnosisPatientAppointmentMapperDto> diagnosisListData;
	private List<CoversheetInstructionDto> adviceDataList;
	private List<MedicationDTO> medicationDataList;
	private List<QuickNotesMapperDto> QuickNotesMapperlist;
	private List<EncounterMasterDto> patientRemarklist;
	private List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;
	private List<EncounterMasterDto> careContextList;
	private List<AllergyPatientMapperDto> allergyDataList;
	private List<PatientMedicalHistoryDto> listPatientMedicalHistoryDto;
	List<VaccineImmunizationMapperDto> vacineList;
	private List<PatientFamilyHistoryDto> listPatientFamilyHistoryDto;
	private List<EncounterMasterDto> invoiceList;
	
	private Integer nursingStationId;
	
	 private String bloodGroup;
	 
	 private String patientRemark;
	 
	 private String visitTypeName;
	 
	 private Date visitDateNew;
	 
	 private String visitDate;
	 
	 private String patientFullName;
	 
	 private Integer patientCouponTemplateMapperId;
	 
	 private String  referralTypeName;
	 private String otherConsultants;
	 private String referralName;
	 
	 private boolean isSyncScheduling;
	 
	 List<ProcedureOrderMasterDto> procedureList;
	 
	 private String procedureStatus;
	 
	 private String admissionNumber;
	 
	 private String signtureImg;
	 
	 List<ProcedureOrderMasterDto> procedureListAdmission;
	 
	 List<PatientHabitDetailsDto> listPatientHabitDetailsDto;
	 
	 private String linkCareContext;
	 
	 private List<ObservationDto> observationList;
	 
	 private BigInteger count;
	 
	 private String hiType;
	 
	 private JSONArray hiTypes;
	 
	 private String createdDateOfComplaints;
	 
	 private String careContextTime;
	 
	 private BigInteger saleId;
	 
	 protected List<Object[]> items;

	private Boolean newPatient;

	private Integer appointmentIdOld;

	private Integer adminServiceMasterId;

	private Boolean isAdminService;

	private String  smartOriginalQueAns;
	private String  smartQuestionnaireAiSummary;

	private Boolean paymentCompleted;

	private Integer augmentDoctorId;
	private Integer aiAppointmentId;

	public Boolean getNewPatient() {
		return newPatient;
	}

	public void setNewPatient(Boolean newPatient) {
		this.newPatient = newPatient;
	}

	public Integer getAppointmentIdOld() {
		return appointmentIdOld;
	}

	public void setAppointmentIdOld(Integer appointmentIdOld) {
		this.appointmentIdOld = appointmentIdOld;
	}

	public String getPatientFullName() {
		return patientFullName;
	}

	public void setPatientFullName(String patientFullName) {
		this.patientFullName = patientFullName;
	}

	public boolean isCapExist() {
		return capExist;
	}

	public void setCapExist(boolean capExist) {
		this.capExist = capExist;
	}

	private Integer isTeleconsultationAppointment;
	
	private Integer referalDetailsId;
	
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

	public Integer getAssoCompanyId() {
		return assoCompanyId;
	}

	public void setAssoCompanyId(Integer assoCompanyId) {
		this.assoCompanyId = assoCompanyId;
	}

	public PatientRegistrationDto getPatientRegistrationDto() {
		return patientRegistrationDto;
	}

	public void setPatientRegistrationDto(PatientRegistrationDto patientRegistrationDto) {
		this.patientRegistrationDto = patientRegistrationDto;
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

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getPatHistory() {
		return patHistory;
	}

	public void setPatHistory(String patHistory) {
		this.patHistory = patHistory;
	}

	public Character getIsKioskApplicable() {
		return isKioskApplicable;
	}

	public void setIsKioskApplicable(Character isKioskApplicable) {
		this.isKioskApplicable = isKioskApplicable;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getSelfDueAmount() {
		return selfDueAmount;
	}

	public void setSelfDueAmount(Double selfDueAmount) {
		this.selfDueAmount = selfDueAmount;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Character getIsFromAppointmentScheduling() {
		return isFromAppointmentScheduling;
	}

	public void setIsFromAppointmentScheduling(Character isFromAppointmentScheduling) {
		this.isFromAppointmentScheduling = isFromAppointmentScheduling;
	}

	public Double getTotalDepositConsumption() {
		return totalDepositConsumption;
	}

	public void setTotalDepositConsumption(Double totalDepositConsumption) {
		this.totalDepositConsumption = totalDepositConsumption;
	}

	
	public Integer getRescheduleAppointmentId() {
		return rescheduleAppointmentId;
	}

	public void setRescheduleAppointmentId(Integer rescheduleAppointmentId) {
		this.rescheduleAppointmentId = rescheduleAppointmentId;
	}

	public Integer getRescheduleReasonId() {
		return rescheduleReasonId;
	}

	public void setRescheduleReasonId(Integer rescheduleReasonId) {
		this.rescheduleReasonId = rescheduleReasonId;
	}

	public Character getReschedulledBy() {
		return reschedulledBy;
	}

	public void setReschedulledBy(Character reschedulledBy) {
		this.reschedulledBy = reschedulledBy;
	}

	public Integer getPreviousSlotId() {
		return previousSlotId;
	}

	public void setPreviousSlotId(Integer previousSlotId) {
		this.previousSlotId = previousSlotId;
	}

	public Character getIsReshedule() {
		return isReshedule;
	}

	public void setIsReshedule(Character isReshedule) {
		this.isReshedule = isReshedule;
	}

	public BigDecimal getDocSharePer() {
		return docSharePer;
	}

	public void setDocSharePer(BigDecimal docSharePer) {
		this.docSharePer = docSharePer;
	}

	public BigDecimal getRefDocSharePer() {
		return refDocSharePer;
	}

	public void setRefDocSharePer(BigDecimal refDocSharePer) {
		this.refDocSharePer = refDocSharePer;
	}

	public Integer getPreferredDocId() {
		return preferredDocId;
	}

	public void setPreferredDocId(Integer preferredDocId) {
		this.preferredDocId = preferredDocId;
	}

	public Character getIsDocReferral() {
		return isDocReferral;
	}

	public void setIsDocReferral(Character isDocReferral) {
		this.isDocReferral = isDocReferral;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
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

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Character getIsPaidAppointment() {
		return isPaidAppointment;
	}

	public void setIsPaidAppointment(Character isPaidAppointment) {
		this.isPaidAppointment = isPaidAppointment;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getServiceSpecialityName() {
		return serviceSpecialityName;
	}

	public void setServiceSpecialityName(String serviceSpecialityName) {
		this.serviceSpecialityName = serviceSpecialityName;
	}

	private Integer againstEncId;

	private Integer orderDetailsIdEhc;

	private Integer consultationMasterId;

	public Integer getOldSlotId() {
		return oldSlotId;
	}

	public void setOldSlotId(Integer oldSlotId) {
		this.oldSlotId = oldSlotId;
	}

	public Character getIsDocShare() {
		return isDocShare;
	}

	public void setIsDocShare(Character isDocShare) {
		this.isDocShare = isDocShare;
	}

	public Double getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(Double serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public Character getIsNursingAssessmentRequiredForSpeciality() {
		return isNursingAssessmentRequiredForSpeciality;
	}

	public void setIsNursingAssessmentRequiredForSpeciality(Character isNursingAssessmentRequiredForSpeciality) {
		this.isNursingAssessmentRequiredForSpeciality = isNursingAssessmentRequiredForSpeciality;
	}

	public String getAppoitmentDate() {
		return appoitmentDate;
	}

	public void setAppoitmentDate(String appoitmentDate) {
		this.appoitmentDate = appoitmentDate;
	}

	public String getAppoitmentTime() {
		return appoitmentTime;
	}

	public void setAppoitmentTime(String appoitmentTime) {
		this.appoitmentTime = appoitmentTime;
	}

	public ReferralMasterDto getReferralMasterDto() {
		return referralMasterDto;
	}

	public void setReferralMasterDto(ReferralMasterDto referralMasterDto) {
		this.referralMasterDto = referralMasterDto;
	}

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

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
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

	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public Double getServiceAmt() {
		return serviceAmt;
	}

	public void setServiceAmt(Double serviceAmt) {
		this.serviceAmt = serviceAmt;
	}

	public Double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(Double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public Double getTotalBillAmt() {
		return totalBillAmt;
	}

	public void setTotalBillAmt(Double totalBillAmt) {
		this.totalBillAmt = totalBillAmt;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getMpesaTxcode() {
		return mpesaTxcode;
	}

	public void setMpesaTxcode(String mpesaTxcode) {
		this.mpesaTxcode = mpesaTxcode;
	}

	public String getCabinDesc() {
		return cabinDesc;
	}

	public void setCabinDesc(String cabinDesc) {
		this.cabinDesc = cabinDesc;
	}

	public String getConsultationStartTime() {
		return consultationStartTime;
	}

	public void setConsultationStartTime(String consultationStartTime) {
		this.consultationStartTime = consultationStartTime;
	}

	public String getConsultationStopTime() {
		return consultationStopTime;
	}

	public void setConsultationStopTime(String consultationStopTime) {
		this.consultationStopTime = consultationStopTime;
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

	public String getCounterDesc() {
		return counterDesc;
	}

	public void setCounterDesc(String counterDesc) {
		this.counterDesc = counterDesc;
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

	public BigDecimal getMaxSpecialityQueueNumber() {
		return maxSpecialityQueueNumber;
	}

	public void setMaxSpecialityQueueNumber(BigDecimal maxSpecialityQueueNumber) {
		this.maxSpecialityQueueNumber = maxSpecialityQueueNumber;
	}

	public Integer getConsultationSpecialityId() {
		return consultationSpecialityId;
	}

	public void setConsultationSpecialityId(Integer consultationSpecialityId) {
		this.consultationSpecialityId = consultationSpecialityId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Character getIsPriority() {
		return isPriority;
	}

	public void setIsPriority(Character isPriority) {
		this.isPriority = isPriority;
	}

	private String location;

	public BigDecimal getMaxUnitTokenNumber() {
		return maxUnitTokenNumber;
	}

	public void setMaxUnitTokenNumber(BigDecimal maxUnitTokenNumber) {
		this.maxUnitTokenNumber = maxUnitTokenNumber;
	}

	public BigDecimal getMaxDocQueueNumber() {
		return maxDocQueueNumber;
	}

	public void setMaxDocQueueNumber(BigDecimal maxDocQueueNumber) {
		this.maxDocQueueNumber = maxDocQueueNumber;
	}

	public Integer getDocQueueNumber() {
		return docQueueNumber;
	}

	public void setDocQueueNumber(Integer docQueueNumber) {
		this.docQueueNumber = docQueueNumber;
	}

	public Integer getUnitTokenNumber() {
		return unitTokenNumber;
	}

	public void setUnitTokenNumber(Integer unitTokenNumber) {
		this.unitTokenNumber = unitTokenNumber;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
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

	private List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList;
	

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
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

	public String getAppoStatusId() {
		return appoStatusId;
	}

	public void setAppoStatusId(String appoStatusId) {
		this.appoStatusId = appoStatusId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public Integer getEhcPackageId() {
		return ehcPackageId;
	}

	public void setEhcPackageId(Integer ehcPackageId) {
		this.ehcPackageId = ehcPackageId;
	}

	public List<PatientSponsorDetailsDto> getPatientSponsorDetailsDtosList() {
		return patientSponsorDetailsDtosList;
	}

	public void setPatientSponsorDetailsDtosList(List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList) {
		this.patientSponsorDetailsDtosList = patientSponsorDetailsDtosList;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public String getModalityDesc() {
		return modalityDesc;
	}

	public void setModalityDesc(String modalityDesc) {
		this.modalityDesc = modalityDesc;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public String getEncounterDateString2() {
		return encounterDateString2;
	}

	public void setEncounterDateString2(String encounterDateString2) {
		this.encounterDateString2 = encounterDateString2;
	}

	public SlotMasterDto getAppointmentObject() {
		return appointmentObject;
	}

	public void setAppointmentObject(SlotMasterDto appointmentObject) {
		this.appointmentObject = appointmentObject;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getReferralDesc() {
		return referralDesc;
	}

	public void setReferralDesc(String referralDesc) {
		this.referralDesc = referralDesc;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	public Integer getClinicId() {
		return clinicId;
	}

	public void setClinicId(Integer clinicId) {
		this.clinicId = clinicId;
	}

	public Integer getReferralTypeId() {
		return referralTypeId;
	}

	public void setReferralTypeId(Integer referralTypeId) {
		this.referralTypeId = referralTypeId;
	}

	public Character getIsAppointmentRequired() {
		return isAppointmentRequired;
	}

	public void setIsAppointmentRequired(Character isAppointmentRequired) {
		this.isAppointmentRequired = isAppointmentRequired;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public String getReferralRemark() {
		return referralRemark;
	}

	public void setReferralRemark(String referralRemark) {
		this.referralRemark = referralRemark;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGenderDec() {
		return genderDec;
	}

	public void setGenderDec(String genderDec) {
		this.genderDec = genderDec;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getSystemGeneratedVisitTypeId() {
		return systemGeneratedVisitTypeId;
	}

	public void setSystemGeneratedVisitTypeId(Integer systemGeneratedVisitTypeId) {
		this.systemGeneratedVisitTypeId = systemGeneratedVisitTypeId;
	}

	public Integer getUserDefinedVisitTypeId() {
		return userDefinedVisitTypeId;
	}

	public void setUserDefinedVisitTypeId(Integer userDefinedVisitTypeId) {
		this.userDefinedVisitTypeId = userDefinedVisitTypeId;
	}

	public Integer getReasonIdForChangingVisitType() {
		return reasonIdForChangingVisitType;
	}

	public void setReasonIdForChangingVisitType(Integer reasonIdForChangingVisitType) {
		this.reasonIdForChangingVisitType = reasonIdForChangingVisitType;
	}

	public String getRemarkForChangingVisitType() {
		return remarkForChangingVisitType;
	}

	public void setRemarkForChangingVisitType(String remarkForChangingVisitType) {
		this.remarkForChangingVisitType = remarkForChangingVisitType;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getEncounterDateString() {
		return encounterDateString;
	}

	public void setEncounterDateString(String encounterDateString) {
		this.encounterDateString = encounterDateString;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getAppointmentTypeId() {
		return appointmentTypeId;
	}

	public void setAppointmentTypeId(Integer appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}

	public Integer getModalityId() {
		return modalityId;
	}

	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public Long getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(Long encounterDate) {
		this.encounterDate = encounterDate;
	}

	public Instant getEncounterTime() {
		return encounterTime;
	}

	public void setEncounterTime(String encounterTime) {
		if (encounterTime!=null && !encounterTime.equals(""))
		{
			LocalTime time = LocalTime.parse(encounterTime);
			LocalDate today = LocalDate.now();
			this.encounterTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.encounterTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public Character getIsReferal() {
		return isReferal;
	}

	public void setIsReferal(Character isReferal) {
		this.isReferal = isReferal;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getKinDetailsId() {
		return kinDetailsId;
	}

	public void setKinDetailsId(Integer kinDetailsId) {
		this.kinDetailsId = kinDetailsId;
	}

	public Integer getPaymentEntitlementTypeId() {
		return paymentEntitlementTypeId;
	}

	public void setPaymentEntitlementTypeId(Integer paymentEntitlementTypeId) {
		this.paymentEntitlementTypeId = paymentEntitlementTypeId;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public List<KinDetailsDto> getListKinDetailsDto() {
		return listKinDetailsDto;
	}

	public void setListKinDetailsDto(List<KinDetailsDto> listKinDetailsDto) {
		this.listKinDetailsDto = listKinDetailsDto;
	}

	public Date getEncounterDateTime() {
		return encounterDateTime;
	}

	public void setEncounterDateTime(Date encounterDateTime) {
		this.encounterDateTime = encounterDateTime;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Character getIsScheduledAppointment() {
		return isScheduledAppointment;
	}

	public void setIsScheduledAppointment(Character isScheduledAppointment) {
		this.isScheduledAppointment = isScheduledAppointment;
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

	public Integer getEncounterTypeId() {
		return encounterTypeId;
	}

	public void setEncounterTypeId(Integer encounterTypeId) {
		this.encounterTypeId = encounterTypeId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEncounterDt() {
		return encounterDt;
	}

	public void setEncounterDt(String encounterDt) {
		this.encounterDt = encounterDt;
	}

	public List<Integer> getDoctorList() {
		return doctorList;
	}

	public void setDoctorList(List<Integer> doctorList) {
		this.doctorList = doctorList;
	}

	public Character getIsRMO() {
		return isRMO;
	}

	public void setIsRMO(Character isRMO) {
		this.isRMO = isRMO;
	}

	public List<ServiceMasterDto> getListServiceMasterDto() {
		return listServiceMasterDto;
	}

	public void setListServiceMasterDto(List<ServiceMasterDto> listServiceMasterDto) {
		this.listServiceMasterDto = listServiceMasterDto;
	}

	public List<PaymentReceiptDetailsDto> getPaymentReceiptDetailsDtosList() {
		return paymentReceiptDetailsDtosList;
	}

	public void setPaymentReceiptDetailsDtosList(List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList) {
		this.paymentReceiptDetailsDtosList = paymentReceiptDetailsDtosList;
	}

	public Character getIsTransferToActiveDeposit() {
		return isTransferToActiveDeposit;
	}

	public void setIsTransferToActiveDeposit(Character isTransferToActiveDeposit) {
		this.isTransferToActiveDeposit = isTransferToActiveDeposit;
	}

	public Integer getReviewAgainstAncId() {
		return reviewAgainstAncId;
	}

	public void setReviewAgainstAncId(Integer reviewAgainstAncId) {
		this.reviewAgainstAncId = reviewAgainstAncId;
	}

	public List<Integer> getOrderDetailsIds() {
		return orderDetailsIds;
	}

	public void setOrderDetailsIds(List<Integer> orderDetailsIds) {
		this.orderDetailsIds = orderDetailsIds;
	}

	public BigDecimal getPackageBasePrice() {
		return packageBasePrice;
	}

	public void setPackageBasePrice(BigDecimal packageBasePrice) {
		this.packageBasePrice = packageBasePrice;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getAgainstEncId() {
		return againstEncId;
	}

	public void setAgainstEncId(Integer againstEncId) {
		this.againstEncId = againstEncId;
	}

	public Integer getOrderDetailsIdEhc() {
		return orderDetailsIdEhc;
	}

	public void setOrderDetailsIdEhc(Integer orderDetailsIdEhc) {
		this.orderDetailsIdEhc = orderDetailsIdEhc;
	}

	public Integer getConsultationMasterId() {
		return consultationMasterId;
	}

	public void setConsultationMasterId(Integer consultationMasterId) {
		this.consultationMasterId = consultationMasterId;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getChildEncounterId() {
		return childEncounterId;
	}

	public void setChildEncounterId(Integer childEncounterId) {
		this.childEncounterId = childEncounterId;
	}

	public Integer getConsultationMpprId() {
		return consultationMpprId;
	}

	public void setConsultationMpprId(Integer consultationMpprId) {
		this.consultationMpprId = consultationMpprId;
	}

	public List<PaymentDepositDto> getPaymentDepositDetailsList() {
		return paymentDepositDetailsList;
	}

	public void setPaymentDepositDetailsList(List<PaymentDepositDto> paymentDepositDetailsList) {
		this.paymentDepositDetailsList = paymentDepositDetailsList;
	}

	public Character getIsCyclicAppointment() {
		return (isCyclicAppointment==null)?'N':isCyclicAppointment;
	}

	public void setIsCyclicAppointment(Character isCyclicAppointment) {
		this.isCyclicAppointment = (isCyclicAppointment==null)?'N':isCyclicAppointment;
	}

	public List<OTReservationDto> getOtReservationList() {
		return otReservationList;
	}

	public void setOtReservationList(List<OTReservationDto> otReservationList) {
		this.otReservationList = otReservationList;
	}

	public Character getIsTariffRateApplicable() 
	{
	   return (isTariffRateApplicable==null)?'Y':isTariffRateApplicable;
	}

	public void setIsTariffRateApplicable(Character isTariffRateApplicable) {
		this.isTariffRateApplicable = (isTariffRateApplicable==null)?'Y':isTariffRateApplicable;
	}

	public Character getIsAppointmentFromMyLife() {
		return isAppointmentFromMyLife;
	}

	public void setIsAppointmentFromMyLife(Character isAppointmentFromMyLife) {
		this.isAppointmentFromMyLife = isAppointmentFromMyLife;
	}

	public String getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(String followUpDate) {
		this.followUpDate = followUpDate;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public Character getCheckInFromDoctorApp() {
		return checkInFromDoctorApp;
	}

	public void setCheckInFromDoctorApp(Character checkInFromDoctorApp) {
		this.checkInFromDoctorApp = checkInFromDoctorApp;
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

	public Integer getIsTeleconsultationAppointment() {
		return isTeleconsultationAppointment;
	}

	public void setIsTeleconsultationAppointment(Integer isTeleconsultationAppointment) {
		this.isTeleconsultationAppointment = isTeleconsultationAppointment;
	}

	public Integer getReferalDetailsId() {
		return referalDetailsId;
	}

	public void setReferalDetailsId(Integer referalDetailsId) {
		this.referalDetailsId = referalDetailsId;
	}

	public Character getIsFromWebAppointment() {
		return isFromWebAppointment;
	}

	public void setIsFromWebAppointment(Character isFromWebAppointment) {
		this.isFromWebAppointment = isFromWebAppointment;
	}

	public Integer getAdmisisonId() {
		return admisisonId;
	}

	public void setAdmisisonId(Integer admisisonId) {
		this.admisisonId = admisisonId;
	}
	
	

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getFromTimeString() {
		return fromTimeString;
	}

	public void setFromTimeString(String fromTimeString) {
		this.fromTimeString = fromTimeString;
	}

	public Integer getAdvisedById() {
		return advisedById;
	}

	public void setAdvisedById(Integer advisedById) {
		this.advisedById = advisedById;
	}

	public String getAdvisedByName() {
		return advisedByName;
	}

	public void setAdvisedByName(String advisedByName) {
		this.advisedByName = advisedByName;
	}


	public List<ComplaintsDto> getComplaintlistData() {
		return complaintlistData;
	}

	public void setComplaintlistData(List<ComplaintsDto> complaintlistData) {
		this.complaintlistData = complaintlistData;
	}

	public List<InvestigationOrderMasterNewDto> getLabOrderList() {
		return labOrderList;
	}

	public void setLabOrderList(List<InvestigationOrderMasterNewDto> labOrderList) {
		this.labOrderList = labOrderList;
	}

	public List<DiagnosisPatientAppointmentMapperDto> getDiagnosisListData() {
		return diagnosisListData;
	}

	public void setDiagnosisListData(List<DiagnosisPatientAppointmentMapperDto> diagnosisListData) {
		this.diagnosisListData = diagnosisListData;
	}

	public List<CoversheetInstructionDto> getAdviceDataList() {
		return adviceDataList;
	}

	public void setAdviceDataList(List<CoversheetInstructionDto> adviceDataList) {
		this.adviceDataList = adviceDataList;
	}

	public List<MedicationDTO> getMedicationDataList() {
		return medicationDataList;
	}

	public void setMedicationDataList(List<MedicationDTO> medicationDataList) {
		this.medicationDataList = medicationDataList;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public Boolean getDoctorLatestCall() {
		return doctorLatestCall;
	}

	public void setDoctorLatestCall(Boolean doctorLatestCall) {
		this.doctorLatestCall = doctorLatestCall;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public List<QuickNotesMapperDto> getQuickNotesMapperlist() {
		return QuickNotesMapperlist;
	}

	public void setQuickNotesMapperlist(List<QuickNotesMapperDto> quickNotesMapperlist) {
		QuickNotesMapperlist = quickNotesMapperlist;
	}

	public List<EncounterMasterDto> getPatientRemarklist() {
		return patientRemarklist;
	}

	public void setPatientRemarklist(List<EncounterMasterDto> patientRemarklist) {
		this.patientRemarklist = patientRemarklist;
	}

	public String getPatientRemark() {
		return patientRemark;
	}

	public void setPatientRemark(String patientRemark) {
		this.patientRemark = patientRemark;
	}

	public List<AppointmentVitalMapperDto> getListAppointmentVitalMapperDto() {
		return listAppointmentVitalMapperDto;
	}

	public void setListAppointmentVitalMapperDto(List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto) {
		this.listAppointmentVitalMapperDto = listAppointmentVitalMapperDto;
	}
	
	public Integer getDischargeStatusId() {
		return dischargeStatusId;
	}

	public void setDischargeStatusId(Integer dischargeStatusId) {
		this.dischargeStatusId = dischargeStatusId;
	}

	public List<ComplaintAppointmentDetailsDto> getListComplaintDataSave() {
		return listComplaintDataSave;
	}

	public void setListComplaintDataSave(List<ComplaintAppointmentDetailsDto> listComplaintDataSave) {
		this.listComplaintDataSave = listComplaintDataSave;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	
	public Date getVisitDateNew() {
		return visitDateNew;
	}

	public void setVisitDateNew(Date visitDateNew) {
		this.visitDateNew = visitDateNew;
	}

	public void setEncounterTime(Instant encounterTime) {
		this.encounterTime = encounterTime;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public List<ComplaintAppointmentDto> getComplaintlistDataLastVisit() {
		return complaintlistDataLastVisit;
	}

	public void setComplaintlistDataLastVisit(List<ComplaintAppointmentDto> complaintlistDataLastVisit) {
		this.complaintlistDataLastVisit = complaintlistDataLastVisit;
	}

	public Integer getPatientCouponTemplateMapperId() {
		return patientCouponTemplateMapperId;
	}

	public void setPatientCouponTemplateMapperId(Integer patientCouponTemplateMapperId) {
		this.patientCouponTemplateMapperId = patientCouponTemplateMapperId;
	}

	public String getReferralTypeName() {
		return referralTypeName;
	}

	public void setReferralTypeName(String referralTypeName) {
		this.referralTypeName = referralTypeName;
	}

	public String getOtherConsultants() {
		return otherConsultants;
	}

	public void setOtherConsultants(String otherConsultants) {
		this.otherConsultants = otherConsultants;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public boolean isSyncScheduling() {
		return isSyncScheduling;
	}

	public void setSyncScheduling(boolean isSyncScheduling) {
		this.isSyncScheduling = isSyncScheduling;
	}

	public List<EncounterMasterDto> getCareContextList() {
		return careContextList;
	}

	public void setCareContextList(List<EncounterMasterDto> careContextList) {
		this.careContextList = careContextList;
	}

	public List<AllergyPatientMapperDto> getAllergyDataList() {
		return allergyDataList;
	}

	public void setAllergyDataList(List<AllergyPatientMapperDto> allergyDataList) {
		this.allergyDataList = allergyDataList;
	}

	public List<ProcedureOrderMasterDto> getProcedureList() {
		return procedureList;
	}

	public void setProcedureList(List<ProcedureOrderMasterDto> procedureList) {
		this.procedureList = procedureList;
	}

	public String getProcedureStatus() {
		return procedureStatus;
	}

	public void setProcedureStatus(String procedureStatus) {
		this.procedureStatus = procedureStatus;
	}

	public List<PatientMedicalHistoryDto> getListPatientMedicalHistoryDto() {
		return listPatientMedicalHistoryDto;
	}

	public void setListPatientMedicalHistoryDto(List<PatientMedicalHistoryDto> listPatientMedicalHistoryDto) {
		this.listPatientMedicalHistoryDto = listPatientMedicalHistoryDto;
	}

	public List<VaccineImmunizationMapperDto> getVacineList() {
		return vacineList;
	}

	public void setVacineList(List<VaccineImmunizationMapperDto> vacineList) {
		this.vacineList = vacineList;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public List<ProcedureOrderMasterDto> getProcedureListAdmission() {
		return procedureListAdmission;
	}

	public void setProcedureListAdmission(List<ProcedureOrderMasterDto> procedureListAdmission) {
		this.procedureListAdmission = procedureListAdmission;
	}

	public List<PatientHabitDetailsDto> getListPatientHabitDetailsDto() {
		return listPatientHabitDetailsDto;
	}

	public void setListPatientHabitDetailsDto(List<PatientHabitDetailsDto> listPatientHabitDetailsDto) {
		this.listPatientHabitDetailsDto = listPatientHabitDetailsDto;
	}

	public List<ObservationDto> getObservationList() {
		return observationList;
	}

	public void setObservationList(List<ObservationDto> observationList) {
		this.observationList = observationList;
	}

	public String getSigntureImg() {
		return signtureImg;
	}

	public void setSigntureImg(String signtureImg) {
		this.signtureImg = signtureImg;
	}

	public String getLinkCareContext() {
		return linkCareContext;
	}

	public void setLinkCareContext(String linkCareContext) {
		this.linkCareContext = linkCareContext;
	}



	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	public String getHiType() {
		return hiType;
	}

	public void setHiType(String hiType) {
		this.hiType = hiType;
	}

	public JSONArray getHiTypes() {
		return hiTypes;
	}

	public void setHiTypes(JSONArray hiTypes) {
		this.hiTypes = hiTypes;
	}

	public String getCreatedDateOfComplaints() {
		return createdDateOfComplaints;
	}

	public void setCreatedDateOfComplaints(String createdDateOfComplaints) {
		this.createdDateOfComplaints = createdDateOfComplaints;
	}

	public List<PatientFamilyHistoryDto> getListPatientFamilyHistoryDto() {
		return listPatientFamilyHistoryDto;
	}

	public void setListPatientFamilyHistoryDto(List<PatientFamilyHistoryDto> listPatientFamilyHistoryDto) {
		this.listPatientFamilyHistoryDto = listPatientFamilyHistoryDto;
	}

	public String getCareContextTime() {
		return careContextTime;
	}

	public void setCareContextTime(String careContextTime) {
		this.careContextTime = careContextTime;
	}

	public List<EncounterMasterDto> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<EncounterMasterDto> BillListResponse) {
		this.invoiceList =  BillListResponse;
	}

	

	public BigInteger getSaleId() {
		return saleId;
	}

	public void setSaleId(BigInteger saleId) {
		this.saleId = saleId;
	}

	public List<Object[]> getItems() {
		return items;
	}

	public void setItems(List<Object[]> items) {
		this.items = items;
	}

    public Integer getAdminServiceMasterId() {
        return adminServiceMasterId;
    }

    public void setAdminServiceMasterId(Integer adminServiceMasterId) {
        this.adminServiceMasterId = adminServiceMasterId;
    }

	public Boolean getIsAdminService() {
		return isAdminService;
	}

	public void setIsAdminService(Boolean isAdminService) {
		this.isAdminService = isAdminService;
	}

	public String getSmartOriginalQueAns() {
		return smartOriginalQueAns;
	}

	public void setSmartOriginalQueAns(String smartOriginalQueAns) {
		this.smartOriginalQueAns = smartOriginalQueAns;
	}

	public String getSmartQuestionnaireAiSummary() {
		return smartQuestionnaireAiSummary;
	}

	public void setSmartQuestionnaireAiSummary(String smartQuestionnaireAiSummary) {
		this.smartQuestionnaireAiSummary = smartQuestionnaireAiSummary;
	}

	public Boolean getPaymentCompleted() {
		return paymentCompleted;
	}

	public void setPaymentCompleted(Boolean paymentCompleted) {
		this.paymentCompleted = paymentCompleted;
	}

	public Integer getAugmentDoctorId() {
		return augmentDoctorId;
	}

	public void setAugmentDoctorId(Integer augmentDoctorId) {
		this.augmentDoctorId = augmentDoctorId;
	}

	public Integer getAiAppointmentId() {
		return aiAppointmentId;
	}

	public void setAiAppointmentId(Integer aiAppointmentId) {
		this.aiAppointmentId = aiAppointmentId;
	}
}
