package com.param.global.dto;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import com.param.global.billing.dto.DepositCollectionReqDto;
import com.param.global.dto.global.VisitorDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;

public class AdmissionDto {
	
	private Integer orderedBy;

	private Date timeOfServiceOrder;

	private Integer cancelReasonId;
	
	private Integer isCriticalOrder;
	
	private Integer isCancelCheckBox;

	private Integer isRecheduleCheckBox;

	private Integer admissionId;

	private Integer admissionNoteId;

	private Integer unitId;

	private  Integer organizationId;

	private Integer patientId;

	private Integer visitTypeId;

	private Integer againstVisitId;

	private Integer doctorId;

	private Integer specialityId;

	private Integer fromBillingBedCategoryId;
	
	private String specialityName;

	private String genderCode;

	private Integer kinDetailsId;

	private String prefixCode;

	private String relationDesc;

	private String identificationName;

	private String kinName;

	private Integer relationId;

	private String mobileNo;

	private String phoneNo;

	private Integer identificationId;

	private String identificationNo;

	private String expiry;

	private Character isGuarantor;
	
	private Character isVip;

	private String address;

	private Integer countryId;

	private Integer stateId;

	private Integer districtId;

	private Integer cityId;

	private Integer areaId;

	private Integer visitorPassTypeId;

	private String visitorPassTypeDesc;

	private Integer offset;

	private Integer noOfRecordsPerPage;

	private Integer triageCategoryId;

	private Integer erBedTypeAllocation;

	private Double age;
	
	private String ageYear;
	
	private Character ageFormat;

	private Character status;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;

	private Integer wardId;

	private List<Integer> wardIds;

	private Integer floorId;

	private Integer roomId;

	private Integer bedId;

	private String doa;

	private String pdd;
	private Date pddNew;

	private Integer mealPrefId;

	private Integer attendentMealPrefId;

	private List<KinDetailsDto> kinArray;

	private Integer bedCategoryId;

	private Integer billingBedCategoryId;

	private Integer paymentEntitlementId;

	private String admissionNumber;

	private Integer admissionDetailsId;

	private Integer admissionTypeId;

	private Character isShortLeave;

	private Character isDischarge;

	private Character isCancel;

	private Integer cancelBy;

	private Date cancelDate;

	private Character isClose;

	private String remark;
	
	private String rremark;

	private Character isHighRisk;

	private Character isCritical;

	private Integer patientCategoryId;

	private Character activeStatus;

	private Integer bedStatusId;

	private String paymentEntitlementDesc;

	private String patientCategory;

	private String bedNumber;

	private String UHIDNumber;

	private Integer requestToDoctorId;

	private Integer doctorSpecialityId;

	private Integer reasonId;

	private String reasonDesc;

	private String dFirstName;

	private String dMiddleName;

	private String dLastName;

	private String pFirstName;

	private String pMiddleName;

	private String pLastName;

	private String dob;

	private Integer preVisitId;

	private String visitTypeName;

	private Character isFlexiableDate;

	private Character admissionStatus;

	private Integer canceledBy;

	private String canceledDate;

	private Integer prefixId;

	private String prefixDesc;

	private Integer genderId;

	private String code;

	private String bedCategoryDesc;

	private Integer toBedCategoryId;

	private String toBedCategoryDesc;

	private Integer unknownPatientId;

	private String accompaniedBy;

	private Integer priorityId;

	private Character isMedicoLegal;

	private String patientName;

	private String tUhid;

	private String genderName;

	private Integer erBedType;

	private Integer pPatientId;

	private Integer tPatientId;

	private Integer id;

	private String ukgenderName;
	
	private String note;
	
	private Integer transferId;
	
	private Integer hierarchyId;

	private Integer transferStatusId;
	
	private Integer toHierarchyId;
	
	private String wardName;
	
	private Integer nursingStationId;
	
	private String nursingStationDesc;
	
	private String futureDischargeDate;
	
	private Integer dischargeId;
	
	private Integer dischargeStatusId;
	
	private String dischargeDesc;
	
	private Integer patientSevicesOrderId;
	
	private Integer serviceId;
	
	private Integer orderStatusId;
	
	private String orderDesc;
	
	private Integer subSpecialityId;
	
	private String subSpecialityName;
	
	private String serviceName;
	
	private String floorName;
	
	private Integer dischargeTypeId;
	
	private String dischargeTypeName;
	
	private String transferStatusDesc;
	
	private Integer toBedId;
	
	private Integer toRoomId;
	
	private Integer toWardId;
	
	private String toBedNumber;
	
	private String toWardname;
	
	private Integer doctorReasonId;
	
	private Integer adtReasonId;
	
	private String doctorNote;
	
	private String adtNote;
	
	private String doctorRejectReason;
	
	private String adtRejectReason;
	
	private String doctorReasonDesc;
	
	private String adtReasonDesc;
	
	private Integer transferRequestId;
	
	private Character patientType;
	
	private String doctorName;
	
	private Integer treatingDoctorId;
	
	private Integer admittingDoctorId;
	
	private Integer admittingSpecialityId;
	
	private Integer toFloorId;
	
	private Integer fromBedId;
	
	private String uhidNumber;

	private Integer medicoLegalId;
	
	private String priorityDesc;
	
	private String encounterDate;
	
	private String noOfHours;
	
	private String remarkDischarge;
	
	private String serviceStandardName;
	
	private Instant fromTime;
	
	private String fromTime2;
	
	private String transferTime;
	
	private Integer scheduleId;
	
	private Integer transferTypeId;
	
	private Integer patientSurgeryOrderId;
	
	private Integer surgeryId;
	
	private String surgeryName;
	
	private Integer fromWardId;
	
	private Integer fromBedCategoryId;
	
	private Integer authorizedBy;
	
	private Integer intensivistRequestId;
	
	private String finalNote;
	
	private Integer finalReasonId;
	
	private Integer workstationReasonId;
	
	private String workstationNote;
	
	private String scheduleDate;
	
	private String modalityDesc;
	
	private Integer modalityId;
	
	private String surgeryTime;
	
	private String surgeryDate;
	
	private BigInteger surgeryCount;
	
	private Integer extendReasonId;
	
	private Character isTransferOfCare;
	
	private Character isIcu;
	
	private Boolean isIcuu;
	
	private Integer waitListNumber;
	
	private Integer bedCategoryWaitingListId;
	
	private Integer type;
	
	private String oldDoa;
	
	private Integer oldBedCategoryId;
	
	private Integer dPatientId;
	
	private String labOrder;
	
	private String ageString;
	
	private BigInteger billStatusCount;
	
	private BigInteger orderDetailsCount;
	
	private BigInteger labCountGreen;
	
	private BigInteger labCountRed;
	
	private BigInteger labCountYellow;
	
	private BigInteger radiologyCountGreen;
	
	private BigInteger radiologyCountRed;
	
	private BigInteger radiologyCountYellow;
	
	private BigInteger otherSerCountGreen;
	
	private BigInteger otherSerCountRed;
	
	private BigInteger otherSerCountYellow;
	
	private BigInteger salesRed;
	
	private BigInteger salesYellow;
	
	private BigInteger salesGreen;
	
	private BigInteger procedureRed;
	
	private BigInteger procedureGreen;
	
	private BigInteger procedureYellow;
	
	private BigInteger pharmecyRed;
	
	private BigInteger pharmecyGreen;
	
	private BigInteger pharmecyYellow;
	
	private BigInteger visitorPassGreen;
	
	private BigInteger visitorPassRed;
	
	private BigInteger visitorPassYellow;
	
	private Integer dischargeSummaryGreen;
	
	private BigInteger dischargeMedicationRed;
	
	private BigInteger dischargeMedicationGreen;
	
	private BigInteger dischargeMedicationYellow;
		
	private Integer reservedBedCategoryId;
	
	private Integer bedsBillingBedCategoryId;
	
	private Integer referralTypeId;
	
	private Integer referralId;
	
	private String referralRemark;
	
	private Integer contractId;
	
	private Integer payeeId;
	
	private Integer defaultSelfTariffId;
	
	private Integer companyId;
	
	private String primaryDiagnosis;

	private Integer treatingDoctorSpecialityId;
	
	private DepositCollectionReqDto reqDto;
	
	private Character isFinancialClearance;
	
	private Integer totalScore;

	private Integer mewsMapperId;
	
	private String procedureOrder;

	private String imaging;

	private String allergy;
	
	private Integer dischargeSummaryId;

	private String concent;
	
	private Character nursingAcceptance;
	
	private String nursingRejectionNote;
	
	private String nbm;

	private Integer patientClassId;
	
	private Character isFinalBill;
	
	private Integer referalDocId;
	
	private String firNo;
	
	private String policeBuckleNo;
	
	private String policeStationName;
	
	private String mlcContactNo;
	
	private Integer mlcRelationId;

	private String mlcRemark;
	
	private String modeOfArrival;
	
	private ReferralMasterDto referralMasterDto;
	
	private List<Integer> wardList;
	
	private String mpshahPatientId;//Ganesh
	
	private PatientRegistrationDto patientRegistrationDto;
	
	private String createdTime;
	
	private String roomDesc;
	
	private Integer erCategoryId;
	
	private List<Integer> bedList;
	
	private Character isShowPatientByBed;

	private Integer fromRoomId;
	
	private Integer billStatusId;
	
	private Character isRMO;
	
	private String bedCode;
	
	private Integer obgynSpecialityId;
	
	private Character isLaborWard;
	
	private Integer hemodialysisSpecialityId;
	
	private Character ishemodialysisward;
	
	private Integer deliveryFlag;
	
	private String ageStr;

	private String imagePath;
	
	private Integer encounterId;
	
	private Integer userId;
	
	private String relationName;

	private String priorityCode;
	
	private Integer admissionNoteVisitType;

	private List<Integer> orderDetailsIds;
	
	private List<Integer> rmoMappedWardList;
	
	private Character isIp;
	
	private List<OTReservationDto> otReservationList;
	
	private String patientFullName;
	
	private String fromDate;
	
	private String toDate;
	
	private List<Integer> visitList;
	
	private Character skipAdmissionAcceptance;
	
	private Character skipHousekeeping;
	
	private String transaction;
	private String uuid;
	
	private List<KinDetailsDto> kinList;
	
	private List<DiagnosisPatientAppointmentDetailsDto> diagnosisList;
	
	private List<PrescriptionDetailsDto> diagnosisMedicationDetailList;
	
	private List<ServiceOrdDto> diagnosisServiceList;
	
	private List<VisitorDto> listVisiterPassMaster;
	
	private String email;
	
	private Integer defaultDiscountOwnership;
	private Integer ordCancelBy;
	private String ordCancelDatetime;
	private Character ordCancelled;
    private String searchKeyword;
	private boolean isPharmacologicalValidationSkipIp;

    private String referenceNumber;
    
	private Integer biomedicalsChargesServiceId;	
	private Integer hospitalChargesServiceId;	
	private Integer mrdChargesServiceId;
	private Integer referalDetailsId;
	
	private String userName;
	private String pddTime;
    private Integer dischargeTemplateId;
    private Integer labSampleDtlsId;
    
    private Integer bed;
    private String bednumber1;
    
    private Integer ward;
    
    private String ward1;
    
    private String treatingDoctorName;
    private String admittingDoctorName;
    private String admittingSpecialityName;
   private String mlcNo;
   private String cityName;
   private String firstName;
   private String dobNeuron;
    
   private Character isHtml; 
   private String allergyTypeDesc;
   private String emergencyNumber;
   private Integer otStatus;
   private String mobileNumber;
   
   private Integer toBillingBedCategoryId;
   
   private Integer bucketFlag;
   
   private Integer dietTypeId;
   
   private String dietRemarkDesc;

   private String companyName;
   
   private BigInteger count;
	 
	 private String hiType;
	 
	 private JSONArray hiTypes;

     private List<Object> orderDetailsList;


	private boolean fromPatientOrdersWorkList;

	private Integer patientWorkbenchId;

	private Integer recordId;

	private Integer tabId;

	private Integer resultId;

	private Date workbenchAddedDate;

	private Date workbenchEndDate;

	private String workbenchRemark;

	private Integer interventionRequired;
	private Integer interventionCompleted;
	private String dateOfBirth;
	private String legacyVisitId;

	private Integer currentTriage;

	private Character stemi;
	private Character stroke;
	private Character truma;
	private Character sepsis;
	private Character shock;


	private String specialityTriage;
	private String triageDetails;
	private Integer specialityTriageId;


    public String getAdmittingSpecialityName() {
		return admittingSpecialityName;
	}

	public void setAdmittingSpecialityName(String admittingSpecialityName) {
		this.admittingSpecialityName = admittingSpecialityName;
	}

	public String getAdmittingDoctorName() {
		return admittingDoctorName;
	}

	public void setAdmittingDoctorName(String admittingDoctorName) {
		this.admittingDoctorName = admittingDoctorName;
	}

	public String getTreatingDoctorName() {
		return treatingDoctorName;
	}

	public void setTreatingDoctorName(String treatingDoctorName) {
		this.treatingDoctorName = treatingDoctorName;
	}

	private Integer fromFloorId;
	public Integer getFromFloorId() {
		return fromFloorId;
	}

	public void setFromFloorId(Integer fromFloorId) {
		this.fromFloorId = fromFloorId;
	}

	public Integer getDefaultDiscountOwnership() {
		return defaultDiscountOwnership;
	}

	public void setDefaultDiscountOwnership(Integer defaultDiscountOwnership) {
		this.defaultDiscountOwnership = defaultDiscountOwnership;
	}

	public List<VisitorDto> getListVisiterPassMaster() {
		return listVisiterPassMaster;
	}

	public void setListVisiterPassMaster(List<VisitorDto> listVisiterPassMaster) {
		this.listVisiterPassMaster = listVisiterPassMaster;
	}

	public List<DiagnosisPatientAppointmentDetailsDto> getDiagnosisList() {
		return diagnosisList;
	}

	public void setDiagnosisList(List<DiagnosisPatientAppointmentDetailsDto> diagnosisList) {
		this.diagnosisList = diagnosisList;
	}

	public List<PrescriptionDetailsDto> getDiagnosisMedicationDetailList() {
		return diagnosisMedicationDetailList;
	}

	public void setDiagnosisMedicationDetailList(List<PrescriptionDetailsDto> diagnosisMedicationDetailList) {
		this.diagnosisMedicationDetailList = diagnosisMedicationDetailList;
	}

	public List<ServiceOrdDto> getDiagnosisServiceList() {
		return diagnosisServiceList;
	}

	public void setDiagnosisServiceList(List<ServiceOrdDto> diagnosisServiceList) {
		this.diagnosisServiceList = diagnosisServiceList;
	}

	public Character getSkipAdmissionAcceptance() {
		return skipAdmissionAcceptance;
	}

	public void setSkipAdmissionAcceptance(Character skipAdmissionAcceptance) {
		this.skipAdmissionAcceptance = skipAdmissionAcceptance;
	}

	public Character getSkipHousekeeping() {
		return skipHousekeeping;
	}

	public void setSkipHousekeeping(Character skipHousekeeping) {
		this.skipHousekeeping = skipHousekeeping;
	}

	public BigInteger getDischargeMedicationRed() {
		return dischargeMedicationRed;
	}

	public void setDischargeMedicationRed(BigInteger dischargeMedicationRed) {
		this.dischargeMedicationRed = dischargeMedicationRed;
	}

	public BigInteger getDischargeMedicationGreen() {
		return dischargeMedicationGreen;
	}

	public void setDischargeMedicationGreen(BigInteger dischargeMedicationGreen) {
		this.dischargeMedicationGreen = dischargeMedicationGreen;
	}

	public BigInteger getDischargeMedicationYellow() {
		return dischargeMedicationYellow;
	}

	public void setDischargeMedicationYellow(BigInteger dischargeMedicationYellow) {
		this.dischargeMedicationYellow = dischargeMedicationYellow;
	}

	public Integer getDischargeSummaryGreen() {
		return dischargeSummaryGreen;
	}

	public void setDischargeSummaryGreen(Integer dischargeSummaryGreen) {
		this.dischargeSummaryGreen = dischargeSummaryGreen;
	}

	public Integer getIsCriticalOrder() {
		return isCriticalOrder;
	}

	public void setIsCriticalOrder(Integer isCriticalOrder) {
		this.isCriticalOrder = isCriticalOrder;
	}

	public Integer getDeliveryFlag() {
		return deliveryFlag;
	}

	public void setDeliveryFlag(Integer deliveryFlag) {
		this.deliveryFlag = deliveryFlag;
	}	
		
	public Character getIsVip() {
		return isVip;
	}

	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}

	public String getAgeYear() {
		return ageYear;
	}

	public void setAgeYear(String ageYear) {
		this.ageYear = ageYear;
	}

	public Integer getFromRoomId() {
		return fromRoomId;
	}

	public void setFromRoomId(Integer fromRoomId) {
		this.fromRoomId = fromRoomId;
	}

	public ReferralMasterDto getReferralMasterDto() {
		return referralMasterDto;
	}

	public void setReferralMasterDto(ReferralMasterDto referralMasterDto) {
		this.referralMasterDto = referralMasterDto;
	}

	public Integer getReferalDocId() {
		return referalDocId;
	}

	public void setReferalDocId(Integer referalDocId) {
		this.referalDocId = referalDocId;
	}

	public Integer getAdmittingDoctorId() {
		return admittingDoctorId;
	}

	public void setAdmittingDoctorId(Integer admittingDoctorId) {
		this.admittingDoctorId = admittingDoctorId;
	}

	public Integer getAdmittingSpecialityId() {
		return admittingSpecialityId;
	}

	public void setAdmittingSpecialityId(Integer admittingSpecialityId) {
		this.admittingSpecialityId = admittingSpecialityId;
	}

	public Character getIsFinalBill() {
		return isFinalBill;
	}

	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Character getNursingAcceptance() {
		return nursingAcceptance;
	}

	public void setNursingAcceptance(Character nursingAcceptance) {
		this.nursingAcceptance = nursingAcceptance;
	}

	public String getNursingRejectionNote() {
		return nursingRejectionNote;
	}

	public void setNursingRejectionNote(String nursingRejectionNote) {
		this.nursingRejectionNote = nursingRejectionNote;
	}



	public Integer getTreatingDoctorSpecialityId() {
		return treatingDoctorSpecialityId;
	}

	public void setTreatingDoctorSpecialityId(Integer treatingDoctorSpecialityId) {
		this.treatingDoctorSpecialityId = treatingDoctorSpecialityId;
	}

	public String getPrimaryDiagnosis() {
		return primaryDiagnosis;
	}

	public void setPrimaryDiagnosis(String primaryDiagnosis) {
		this.primaryDiagnosis = primaryDiagnosis;
	}

	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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

	public Integer getReferralTypeId() {
		return referralTypeId;
	}

	public void setReferralTypeId(Integer referralTypeId) {
		this.referralTypeId = referralTypeId;
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
	
	public String getrremark() {
		return rremark;
	}

	public void setrremark(String rremark) {
		this.rremark = rremark;
	}

	public Integer getReservedBedCategoryId() {
		return reservedBedCategoryId;
	}

	public void setReservedBedCategoryId(Integer reservedBedCategoryId) {
		this.reservedBedCategoryId = reservedBedCategoryId;
	}

	public Integer getBedsBillingBedCategoryId() {
		return bedsBillingBedCategoryId;
	}

	public void setBedsBillingBedCategoryId(Integer bedsBillingBedCategoryId) {
		this.bedsBillingBedCategoryId = bedsBillingBedCategoryId;
	}

	public BigInteger getOrderDetailsCount() {
		return orderDetailsCount;
	}

	public void setOrderDetailsCount(BigInteger orderDetailsCount) {
		this.orderDetailsCount = orderDetailsCount;
	}

	public BigInteger getVisitorPassGreen() {
		return visitorPassGreen;
	}

	public void setVisitorPassGreen(BigInteger visitorPassGreen) {
		this.visitorPassGreen = visitorPassGreen;
	}

	public BigInteger getVisitorPassRed() {
		return visitorPassRed;
	}

	public void setVisitorPassRed(BigInteger visitorPassRed) {
		this.visitorPassRed = visitorPassRed;
	}

	public BigInteger getVisitorPassYellow() {
		return visitorPassYellow;
	}

	public void setVisitorPassYellow(BigInteger visitorPassYellow) {
		this.visitorPassYellow = visitorPassYellow;
	}

	public BigInteger getLabCountGreen() {
		return labCountGreen;
	}

	public void setLabCountGreen(BigInteger labCountGreen) {
		this.labCountGreen = labCountGreen;
	}

	public BigInteger getLabCountRed() {
		return labCountRed;
	}

	public void setLabCountRed(BigInteger labCountRed) {
		this.labCountRed = labCountRed;
	}

	public BigInteger getLabCountYellow() {
		return labCountYellow;
	}

	public void setLabCountYellow(BigInteger labCountYellow) {
		this.labCountYellow = labCountYellow;
	}

	public BigInteger getRadiologyCountGreen() {
		return radiologyCountGreen;
	}

	public void setRadiologyCountGreen(BigInteger radiologyCountGreen) {
		this.radiologyCountGreen = radiologyCountGreen;
	}

	public BigInteger getRadiologyCountRed() {
		return radiologyCountRed;
	}

	public void setRadiologyCountRed(BigInteger radiologyCountRed) {
		this.radiologyCountRed = radiologyCountRed;
	}

	public BigInteger getRadiologyCountYellow() {
		return radiologyCountYellow;
	}

	public void setRadiologyCountYellow(BigInteger radiologyCountYellow) {
		this.radiologyCountYellow = radiologyCountYellow;
	}

	public BigInteger getOtherSerCountGreen() {
		return otherSerCountGreen;
	}

	public void setOtherSerCountGreen(BigInteger otherSerCountGreen) {
		this.otherSerCountGreen = otherSerCountGreen;
	}

	public BigInteger getOtherSerCountRed() {
		return otherSerCountRed;
	}

	public void setOtherSerCountRed(BigInteger otherSerCountRed) {
		this.otherSerCountRed = otherSerCountRed;
	}

	public BigInteger getOtherSerCountYellow() {
		return otherSerCountYellow;
	}

	public void setOtherSerCountYellow(BigInteger otherSerCountYellow) {
		this.otherSerCountYellow = otherSerCountYellow;
	}

	public BigInteger getSalesRed() {
		return salesRed;
	}

	public void setSalesRed(BigInteger salesRed) {
		this.salesRed = salesRed;
	}

	public BigInteger getSalesYellow() {
		return salesYellow;
	}

	public void setSalesYellow(BigInteger salesYellow) {
		this.salesYellow = salesYellow;
	}

	public BigInteger getSalesGreen() {
		return salesGreen;
	}

	public void setSalesGreen(BigInteger salesGreen) {
		this.salesGreen = salesGreen;
	}

	public BigInteger getProcedureRed() {
		return procedureRed;
	}

	public void setProcedureRed(BigInteger procedureRed) {
		this.procedureRed = procedureRed;
	}

	public BigInteger getProcedureGreen() {
		return procedureGreen;
	}

	public void setProcedureGreen(BigInteger procedureGreen) {
		this.procedureGreen = procedureGreen;
	}

	public BigInteger getProcedureYellow() {
		return procedureYellow;
	}

	public void setProcedureYellow(BigInteger procedureYellow) {
		this.procedureYellow = procedureYellow;
	}

	public BigInteger getPharmecyRed() {
		return pharmecyRed;
	}

	public void setPharmecyRed(BigInteger pharmecyRed) {
		this.pharmecyRed = pharmecyRed;
	}

	public BigInteger getPharmecyGreen() {
		return pharmecyGreen;
	}

	public void setPharmecyGreen(BigInteger pharmecyGreen) {
		this.pharmecyGreen = pharmecyGreen;
	}

	public BigInteger getPharmecyYellow() {
		return pharmecyYellow;
	}

	public void setPharmecyYellow(BigInteger pharmecyYellow) {
		this.pharmecyYellow = pharmecyYellow;
	}
	
	public BigInteger getBillStatusCount() {
		return billStatusCount;
	}

	public void setBillStatusCount(BigInteger billStatusCount) {
		this.billStatusCount = billStatusCount;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	private List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList;
	
	public List<PatientSponsorDetailsDto> getPatientSponsorDetailsDtosList() {
		return patientSponsorDetailsDtosList;
	}

	public void setPatientSponsorDetailsDtosList(List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList) {
		this.patientSponsorDetailsDtosList = patientSponsorDetailsDtosList;
	}
	
	public Integer getdPatientId() {
		return dPatientId;
	}

	public void setdPatientId(Integer dPatientId) {
		this.dPatientId = dPatientId;
	}

	public String getOldDoa() {
		return oldDoa;
	}

	public void setOldDoa(String oldDoa) {
		this.oldDoa = oldDoa;
	}

	public Integer getOldBedCategoryId() {
		return oldBedCategoryId;
	}

	public void setOldBedCategoryId(Integer oldBedCategoryId) {
		this.oldBedCategoryId = oldBedCategoryId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Character getIsIcu() {
		return isIcu;
	}

	public void setIsIcu(Character isIcu) {
		this.isIcu = isIcu;
	}

	public Character getIsTransferOfCare() {
		return isTransferOfCare;
	}

	public void setIsTransferOfCare(Character isTransferOfCare) {
		this.isTransferOfCare = isTransferOfCare;
	}

	public Integer getExtendReasonId() {
		return extendReasonId;
	}

	public void setExtendReasonId(Integer extendReasonId) {
		this.extendReasonId = extendReasonId;
	}

	public BigInteger getSurgeryCount() {
		return surgeryCount;
	}

	public void setSurgeryCount(BigInteger surgeryCount) {
		this.surgeryCount = surgeryCount;
	}

	public String getSurgeryTime() {
		return surgeryTime;
	}

	public void setSurgeryTime(String surgeryTime) {
		this.surgeryTime = surgeryTime;
	}

	public String getSurgeryDate() {
		return surgeryDate;
	}

	public void setSurgeryDate(String surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	public Integer getModalityId() {
		return modalityId;
	}

	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}

	public String getModalityDesc() {
		return modalityDesc;
	}

	public void setModalityDesc(String modalityDesc) {
		this.modalityDesc = modalityDesc;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Integer getFromWardId() {
		return fromWardId;
	}

	public void setFromWardId(Integer fromWardId) {
		this.fromWardId = fromWardId;
	}

	public Integer getFromBedCategoryId() {
		return fromBedCategoryId;
	}

	public void setFromBedCategoryId(Integer fromBedCategoryId) {
		this.fromBedCategoryId = fromBedCategoryId;
	}

	public Integer getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(Integer authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public Integer getIntensivistRequestId() {
		return intensivistRequestId;
	}

	public void setIntensivistRequestId(Integer intensivistRequestId) {
		this.intensivistRequestId = intensivistRequestId;
	}

	public String getFinalNote() {
		return finalNote;
	}

	public void setFinalNote(String finalNote) {
		this.finalNote = finalNote;
	}

	public Integer getFinalReasonId() {
		return finalReasonId;
	}

	public void setFinalReasonId(Integer finalReasonId) {
		this.finalReasonId = finalReasonId;
	}

	public Integer getWorkstationReasonId() {
		return workstationReasonId;
	}

	public void setWorkstationReasonId(Integer workstationReasonId) {
		this.workstationReasonId = workstationReasonId;
	}

	public String getWorkstationNote() {
		return workstationNote;
	}

	public void setWorkstationNote(String workstationNote) {
		this.workstationNote = workstationNote;
	}

	public void setFromTime(Instant fromTime) {
		this.fromTime = fromTime;
	}

	public Integer getPatientSurgeryOrderId() {
		return patientSurgeryOrderId;
	}

	public void setPatientSurgeryOrderId(Integer patientSurgeryOrderId) {
		this.patientSurgeryOrderId = patientSurgeryOrderId;
	}

	public Integer getSurgeryId() {
		return surgeryId;
	}

	public void setSurgeryId(Integer surgeryId) {
		this.surgeryId = surgeryId;
	}

	public String getSurgeryName() {
		return surgeryName;
	}

	public void setSurgeryName(String surgeryName) {
		this.surgeryName = surgeryName;
	}

	public Integer getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(Integer transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}

	public String getFromTime2() {
		return fromTime2;
	}

	public void setFromTime2(String fromTime2) {
		this.fromTime2 = fromTime2;
	}

	public Instant getFromTime() {
		return fromTime;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public Integer getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(Integer orderedBy) {
		this.orderedBy = orderedBy;
	}

	public Date getTimeOfServiceOrder() {
		return timeOfServiceOrder;
	}

	public void setTimeOfServiceOrder(Date timeOfServiceOrder) {
		this.timeOfServiceOrder = timeOfServiceOrder;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getRemarkDischarge() {
		return remarkDischarge;
	}

	public void setRemarkDischarge(String remarkDischarge) {
		this.remarkDischarge = remarkDischarge;
	}

	public String getNoOfHours() {
		return noOfHours;
	}

	public void setNoOfHours(String noOfHours) {
		this.noOfHours = noOfHours;
	}

	public String getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}

	public String getPriorityDesc() {
		return priorityDesc;
	}

	public void setPriorityDesc(String priorityDesc) {
		this.priorityDesc = priorityDesc;
	}

	public Integer getMedicoLegalId() {
		return medicoLegalId;
	}

	public void setMedicoLegalId(Integer medicoLegalId) {
		this.medicoLegalId = medicoLegalId;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getIsCancelCheckBox() {
		return isCancelCheckBox;
	}

	public void setIsCancelCheckBox(Integer isCancelCheckBox) {
		this.isCancelCheckBox=isCancelCheckBox;
	}

	public Integer getIsRecheduleCheckBox() {
		return isRecheduleCheckBox;
	}

	public void setIsRecheduleCheckBox(Integer isRecheduleCheckBox) {
		this.isRecheduleCheckBox = isRecheduleCheckBox;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getAdmissionNoteId() {
		return admissionNoteId;
	}

	public void setAdmissionNoteId(Integer admissionNoteId) {
		this.admissionNoteId = admissionNoteId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public  Integer getOrganizationId() {
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

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getAgainstVisitId() {
		return againstVisitId;
	}

	public void setAgainstVisitId(Integer againstVisitId) {
		this.againstVisitId = againstVisitId;
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

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Integer getKinDetailsId() {
		return kinDetailsId;
	}

	public void setKinDetailsId(Integer kinDetailsId) {
		this.kinDetailsId = kinDetailsId;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public String getRelationDesc() {
		return relationDesc;
	}

	public void setRelationDesc(String relationDesc) {
		this.relationDesc = relationDesc;
	}

	public String getIdentificationName() {
		return identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public String getKinName() {
		return kinName;
	}

	public void setKinName(String kinName) {
		this.kinName = kinName;
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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Integer getIdentificationId() {
		return identificationId;
	}

	public void setIdentificationId(Integer identificationId) {
		this.identificationId = identificationId;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public Character getIsGuarantor() {
		return isGuarantor;
	}

	public void setIsGuarantor(Character isGuarantor) {
		this.isGuarantor = isGuarantor;
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

	public Integer getVisitorPassTypeId() {
		return visitorPassTypeId;
	}

	public void setVisitorPassTypeId(Integer visitorPassTypeId) {
		this.visitorPassTypeId = visitorPassTypeId;
	}

	public String getVisitorPassTypeDesc() {
		return visitorPassTypeDesc;
	}

	public void setVisitorPassTypeDesc(String visitorPassTypeDesc) {
		this.visitorPassTypeDesc = visitorPassTypeDesc;
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

	public Integer getTriageCategoryId() {
		return triageCategoryId;
	}

	public void setTriageCategoryId(Integer triageCategoryId) {
		this.triageCategoryId = triageCategoryId;
	}

	public Integer getErBedTypeAllocation() {
		return erBedTypeAllocation;
	}

	public void setErBedTypeAllocation(Integer erBedTypeAllocation) {
		this.erBedTypeAllocation = erBedTypeAllocation;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public Character getAgeFormat() {
		return ageFormat;
	}

	public void setAgeFormat(Character ageFormat) {
		this.ageFormat = ageFormat;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status =  status;
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

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public List<Integer> getWardIds() {
		return wardIds;
	}

	public void setWardIds(List<Integer> wardIds) {
		this.wardIds = wardIds;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
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

	public Integer getMealPrefId() {
		return mealPrefId;
	}

	public void setMealPrefId(Integer mealPrefId) {
		this.mealPrefId = mealPrefId;
	}

	public Integer getAttendentMealPrefId() {
		return attendentMealPrefId;
	}

	public void setAttendentMealPrefId(Integer attendentMealPrefId) {
		this.attendentMealPrefId = attendentMealPrefId;
	}

	public List<KinDetailsDto> getKinArray() {
		return kinArray;
	}

	public void setKinArray(List<KinDetailsDto> kinArray) {
		this.kinArray = kinArray;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
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

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public Integer getAdmissionDetailsId() {
		return admissionDetailsId;
	}

	public void setAdmissionDetailsId(Integer admissionDetailsId) {
		this.admissionDetailsId = admissionDetailsId;
	}

	public Integer getAdmissionTypeId() {
		return admissionTypeId;
	}

	public void setAdmissionTypeId(Integer admissionTypeId) {
		this.admissionTypeId = admissionTypeId;
	}

	public Character getIsShortLeave() {
		return isShortLeave;
	}

	public void setIsShortLeave(Character isShortLeave) {
		this.isShortLeave = isShortLeave;
	}

	public Character getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Character isDischarge) {
		this.isDischarge = (isDischarge == '\u0000') ? 'N' : isDischarge;
	}

	public Character getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Character isCancel) {
		this.isCancel = (isCancel == '\u0000') ? 'N' : isCancel;
	}

	public Integer getCancelBy() {
		return cancelBy;
	}

	public void setCancelBy(Integer cancelBy) {
		this.cancelBy = cancelBy;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Character getIsClose() {
		return isClose;
	}

	public void setIsClose(Character isClose) {
		this.isClose = isClose;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getIsHighRisk() {
		return isHighRisk;
	}

	public void setIsHighRisk(Character isHighRisk) {
		this.isHighRisk = (isHighRisk == '\u0000') ? 'N' : isHighRisk;
	}

	public Character getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Character isCritical) {
		this.isCritical = (isCritical == '\u0000') ? 'N' : isCritical;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Character getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Character activeStatus) {
		this.activeStatus = (activeStatus == '\u0000') ? 'I' : activeStatus;
	}

	public Integer getBedStatusId() {
		return bedStatusId;
	}

	public void setBedStatusId(Integer bedStatusId) {
		this.bedStatusId = bedStatusId;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public String getUHIDNumber() {
		return UHIDNumber;
	}

	public void setUHIDNumber(String UHIDNumber) {
		this.UHIDNumber = UHIDNumber;
	}

	public Integer getRequestToDoctorId() {
		return requestToDoctorId;
	}

	public void setRequestToDoctorId(Integer requestToDoctorId) {
		this.requestToDoctorId = requestToDoctorId;
	}

	public Integer getDoctorSpecialityId() {
		return doctorSpecialityId;
	}

	public void setDoctorSpecialityId(Integer doctorSpecialityId) {
		this.doctorSpecialityId = doctorSpecialityId;
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

	public Integer getPreVisitId() {
		return preVisitId;
	}

	public void setPreVisitId(Integer preVisitId) {
		this.preVisitId = preVisitId;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public Character getIsFlexiableDate() {
		return isFlexiableDate;
	}

	public void setIsFlexiableDate(Character isFlexiableDate) {
		this.isFlexiableDate = (isFlexiableDate == '\u0000') ? 'N' : isFlexiableDate;
	}

	public Character getAdmissionStatus() {
		return admissionStatus;
	}

	public void setAdmissionStatus(Character admissionStatus) {
		this.admissionStatus = (admissionStatus == '\u0000') ? 'I' : admissionStatus;
	}

	public Integer getCanceledBy() {
		return canceledBy;
	}

	public void setCanceledBy(Integer canceledBy) {
		this.canceledBy = canceledBy;
	}

	public String getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(String canceledDate) {
		this.canceledDate = canceledDate;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public String getPrefixDesc() {
		return prefixDesc;
	}

	public void setPrefixDesc(String prefixDesc) {
		this.prefixDesc = prefixDesc;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBedCategoryDesc() {
		return bedCategoryDesc;
	}

	public void setBedCategoryDesc(String bedCategoryDesc) {
		this.bedCategoryDesc = bedCategoryDesc;
	}

	public Integer getToBedCategoryId() {
		return toBedCategoryId;
	}

	public void setToBedCategoryId(Integer toBedCategoryId) {
		this.toBedCategoryId = toBedCategoryId;
	}

	public String getToBedCategoryDesc() {
		return toBedCategoryDesc;
	}

	public void setToBedCategoryDesc(String toBedCategoryDesc) {
		this.toBedCategoryDesc = toBedCategoryDesc;
	}

	public Integer getUnknownPatientId() {
		return unknownPatientId;
	}

	public void setUnknownPatientId(Integer unknownPatientId) {
		this.unknownPatientId = unknownPatientId;
	}

	public String getAccompaniedBy() {
		return accompaniedBy;
	}

	public void setAccompaniedBy(String accompaniedBy) {
		this.accompaniedBy = accompaniedBy;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Character getIsMedicoLegal() {
		return isMedicoLegal;
	}

	public void setIsMedicoLegal(Character isMedicoLegal) {
		this.isMedicoLegal = (isMedicoLegal == '\u0000') ? 'N' : isMedicoLegal;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String gettUhid() {
		return tUhid;
	}

	public void settUhid(String tUhid) {
		this.tUhid = tUhid;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public Integer getErBedType() {
		return erBedType;
	}

	public void setErBedType(Integer erBedType) {
		this.erBedType = erBedType;
	}

	public Integer getpPatientId() {
		return pPatientId;
	}

	public void setpPatientId(Integer pPatientId) {
		this.pPatientId = pPatientId;
	}

	public Integer gettPatientId() {
		return tPatientId;
	}

	public void settPatientId(Integer tPatientId) {
		this.tPatientId = tPatientId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUkgenderName() {
		return ukgenderName;
	}

	public void setUkgenderName(String ukgenderName) {
		this.ukgenderName = ukgenderName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getTransferId() {
		return transferId;
	}

	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}

	public Integer getHierarchyId() {
		return hierarchyId;
	}

	public void setHierarchyId(Integer hierarchyId) {
		this.hierarchyId = hierarchyId;
	}

	public Integer getTransferStatusId() {
		return transferStatusId;
	}

	public void setTransferStatusId(Integer transferStatusId) {
		this.transferStatusId = transferStatusId;
	}

	public Integer getToHierarchyId() {
		return toHierarchyId;
	}

	public void setToHierarchyId(Integer toHierarchyId) {
		this.toHierarchyId = toHierarchyId;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public String getNursingStationDesc() {
		return nursingStationDesc;
	}

	public void setNursingStationDesc(String nursingStationDesc) {
		this.nursingStationDesc = nursingStationDesc;
	}

	public String getFutureDischargeDate() {
		return futureDischargeDate;
	}

	public void setFutureDischargeDate(String futureDischargeDate) {
		this.futureDischargeDate = futureDischargeDate;
	}

	public Integer getDischargeId() {
		return dischargeId;
	}

	public void setDischargeId(Integer dischargeId) {
		this.dischargeId = dischargeId;
	}

	public Integer getDischargeStatusId() {
		return dischargeStatusId;
	}

	public void setDischargeStatusId(Integer dischargeStatusId) {
		this.dischargeStatusId = dischargeStatusId;
	}

	public String getDischargeDesc() {
		return dischargeDesc;
	}

	public void setDischargeDesc(String dischargeDesc) {
		this.dischargeDesc = dischargeDesc;
	}

	public Integer getPatientSevicesOrderId() {
		return patientSevicesOrderId;
	}

	public void setPatientSevicesOrderId(Integer patientSevicesOrderId) {
		this.patientSevicesOrderId = patientSevicesOrderId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public Integer getDischargeTypeId() {
		return dischargeTypeId;
	}

	public void setDischargeTypeId(Integer dischargeTypeId) {
		this.dischargeTypeId = dischargeTypeId;
	}

	public String getDischargeTypeName() {
		return dischargeTypeName;
	}

	public void setDischargeTypeName(String dischargeTypeName) {
		this.dischargeTypeName = dischargeTypeName;
	}

	public String getTransferStatusDesc() {
		return transferStatusDesc;
	}

	public void setTransferStatusDesc(String transferStatusDesc) {
		this.transferStatusDesc = transferStatusDesc;
	}

	public Integer getToBedId() {
		return toBedId;
	}

	public void setToBedId(Integer toBedId) {
		this.toBedId = toBedId;
	}

	public Integer getToRoomId() {
		return toRoomId;
	}

	public void setToRoomId(Integer toRoomId) {
		this.toRoomId = toRoomId;
	}

	public Integer getToWardId() {
		return toWardId;
	}

	public void setToWardId(Integer toWardId) {
		this.toWardId = toWardId;
	}

	public String getToBedNumber() {
		return toBedNumber;
	}

	public void setToBedNumber(String toBedNumber) {
		this.toBedNumber = toBedNumber;
	}

	public String getToWardname() {
		return toWardname;
	}

	public void setToWardname(String toWardname) {
		this.toWardname = toWardname;
	}

	public Integer getDoctorReasonId() {
		return doctorReasonId;
	}

	public void setDoctorReasonId(Integer doctorReasonId) {
		this.doctorReasonId = doctorReasonId;
	}

	public Integer getAdtReasonId() {
		return adtReasonId;
	}

	public void setAdtReasonId(Integer adtReasonId) {
		this.adtReasonId = adtReasonId;
	}

	public String getDoctorNote() {
		return doctorNote;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public String getAdtNote() {
		return adtNote;
	}

	public void setAdtNote(String adtNote) {
		this.adtNote = adtNote;
	}

	public String getDoctorRejectReason() {
		return doctorRejectReason;
	}

	public void setDoctorRejectReason(String doctorRejectReason) {
		this.doctorRejectReason = doctorRejectReason;
	}

	public String getAdtRejectReason() {
		return adtRejectReason;
	}

	public void setAdtRejectReason(String adtRejectReason) {
		this.adtRejectReason = adtRejectReason;
	}

	public String getDoctorReasonDesc() {
		return doctorReasonDesc;
	}

	public void setDoctorReasonDesc(String doctorReasonDesc) {
		this.doctorReasonDesc = doctorReasonDesc;
	}

	public String getAdtReasonDesc() {
		return adtReasonDesc;
	}

	public void setAdtReasonDesc(String adtReasonDesc) {
		this.adtReasonDesc = adtReasonDesc;
	}

	public Integer getTransferRequestId() {
		return transferRequestId;
	}

	public void setTransferRequestId(Integer transferRequestId) {
		this.transferRequestId = transferRequestId;
	}

	public Character getPatientType() {
		return patientType;
	}

	public void setPatientType(Character patientType) {
		this.patientType = (patientType == '\u0000') ? 'N' : patientType;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getTreatingDoctorId() {
		return treatingDoctorId;
	}

	public void setTreatingDoctorId(Integer treatingDoctorId) {
		this.treatingDoctorId = treatingDoctorId;
	}

	public Integer getToFloorId() {
		return toFloorId;
	}

	public void setToFloorId(Integer toFloorId) {
		this.toFloorId = toFloorId;
	}

	public Integer getFromBedId() {
		return fromBedId;
	}

	public void setFromBedId(Integer fromBedId) {
		this.fromBedId = fromBedId;
	}

	public String getLabOrder() {
		return labOrder;
	}

	public void setLabOrder(String labOrder) {
		this.labOrder = labOrder;
	}

	public DepositCollectionReqDto getReqDto() {
		return reqDto;
	}

	public void setReqDto(DepositCollectionReqDto reqDto) {
		this.reqDto = reqDto;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getMewsMapperId() {
		return mewsMapperId;
	}

	public void setMewsMapperId(Integer mewsMapperId) {
		this.mewsMapperId = mewsMapperId;
	}

	public String getProcedureOrder() {
		return procedureOrder;
	}

	public void setProcedureOrder(String procedureOrder) {
		this.procedureOrder = procedureOrder;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public String getImaging() {
		return imaging;
	}

	public void setImaging(String imaging) {
		this.imaging = imaging;
	}

	public Integer getDischargeSummaryId() {
		return dischargeSummaryId;
	}

	public void setDischargeSummaryId(Integer dischargeSummaryId) {
		this.dischargeSummaryId = dischargeSummaryId;
	}

	public String getConcent() {
		return concent;
	}

	public void setConcent(String concent) {
		this.concent = concent;
	}

	public String getNbm() {
		return nbm;
	}

	public void setNbm(String nbm) {
		this.nbm = nbm;
	}

	public List<Integer> getWardList() {
		return wardList;
	}

	public void setWardList(List<Integer> wardList) {
		this.wardList = wardList;
	}

	public PatientRegistrationDto getPatientRegistrationDto() {
		return patientRegistrationDto;
	}

	public void setPatientRegistrationDto(PatientRegistrationDto patientRegistrationDto) {
		this.patientRegistrationDto = patientRegistrationDto;
	}

	public Integer getFromBillingBedCategoryId() {
		return fromBillingBedCategoryId;
	}

	public void setFromBillingBedCategoryId(Integer fromBillingBedCategoryId) {
		this.fromBillingBedCategoryId = fromBillingBedCategoryId;
	}

	
	public String getPoliceBuckleNo() {
		return policeBuckleNo;
	}

	public String getPoliceStationName() {
		return policeStationName;
	}

	public String getMlcContactNo() {
		return mlcContactNo;
	}

	public Integer getMlcRelationId() {
		return mlcRelationId;
	}


	public void setPoliceBuckleNo(String policeBuckleNo) {
		this.policeBuckleNo = policeBuckleNo;
	}

	public void setPoliceStationName(String policeStationName) {
		this.policeStationName = policeStationName;
	}

	public void setMlcContactNo(String mlcContactNo) {
		this.mlcContactNo = mlcContactNo;
	}

	public void setMlcRelationId(Integer mlcRelationId) {
		this.mlcRelationId = mlcRelationId;
	}

	public String getFirNo() {
		return firNo;
	}

	public void setFirNo(String firNo) {
		this.firNo = firNo;
	}
	public String getMpshahPatientId() {
		return mpshahPatientId;
	}

	public void setMpshahPatientId(String mpshahPatientId) {
		this.mpshahPatientId = mpshahPatientId;
	}
	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public Integer getErCategoryId() {
		return erCategoryId;
	}

	public void setErCategoryId(Integer erCategoryId) {
		this.erCategoryId = erCategoryId;
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

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public Character getIsRMO() {
		return isRMO;
	}

	public void setIsRMO(Character isRMO) {
		this.isRMO = isRMO;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public Integer getObgynSpecialityId() {
		return obgynSpecialityId;
	}

	public void setObgynSpecialityId(Integer obgynSpecialityId) {
		this.obgynSpecialityId = obgynSpecialityId;
	}

	public Character getIsLaborWard() {
		return isLaborWard;
	}

	public void setIsLaborWard(Character isLaborWard) {
		this.isLaborWard = isLaborWard;
	}

	public Integer getHemodialysisSpecialityId() {
		return hemodialysisSpecialityId;
	}

	public void setHemodialysisSpecialityId(Integer hemodialysisSpecialityId) {
		this.hemodialysisSpecialityId = hemodialysisSpecialityId;
	}

	public Character getIshemodialysisward() {
		return ishemodialysisward;
	}

	public void setIshemodialysisward(Character ishemodialysisward) {
		this.ishemodialysisward = ishemodialysisward;
	}

	public String getAgeStr() {
		return ageStr;
	}

	public void setAgeStr(String ageStr) {
		this.ageStr = ageStr;
	}
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<Integer> getOrderDetailsIds() {
		return orderDetailsIds;
	}

	public void setOrderDetailsIds(List<Integer> orderDetailsIds) {
		this.orderDetailsIds = orderDetailsIds;
	}

	public List<Integer> getRmoMappedWardList() {
		return rmoMappedWardList;
	}

	public void setRmoMappedWardList(List<Integer> rmoMappedWardList) {
		this.rmoMappedWardList = rmoMappedWardList;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getPriorityCode() {
		return priorityCode;
	}

	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}

	public Integer getAdmissionNoteVisitType() {
		return admissionNoteVisitType;
	}

	public void setAdmissionNoteVisitType(Integer admissionNoteVisitType) {
		this.admissionNoteVisitType = admissionNoteVisitType;
	}

	public Character getIsIp() {
		return isIp;
	}

	public void setIsIp(Character isIp) {
		this.isIp = isIp;
	}

	public List<OTReservationDto> getOtReservationList() {
		return otReservationList;
	}

	public void setOtReservationList(List<OTReservationDto> otReservationList) {
		this.otReservationList = otReservationList;
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

	public List<Integer> getVisitList() {
		return visitList;
	}

	public void setVisitList(List<Integer> visitList) {
		this.visitList = visitList;
	}

	public String getPatientFullName() {
		return patientFullName;
	}

	public void setPatientFullName(String patientFullName) {
		this.patientFullName = patientFullName;
	}

	public List<KinDetailsDto> getKinList() {
		return kinList;
	}

	public void setKinList(List<KinDetailsDto> kinList) {
		this.kinList = kinList;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getOrdCancelBy() {
		return ordCancelBy;
	}

	public void setOrdCancelBy(Integer ordCancelBy) {
		this.ordCancelBy = ordCancelBy;
	}

	public String getOrdCancelDatetime() {
		return ordCancelDatetime;
	}

	public void setOrdCancelDatetime(String ordCancelDatetime) {
		this.ordCancelDatetime = ordCancelDatetime;
	}

	public Character getOrdCancelled() {
		return ordCancelled;
	}

	public void setOrdCancelled(Character ordCancelled) {
		this.ordCancelled = ordCancelled;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public boolean getIsPharmacologicalValidationSkipIp() {
		return isPharmacologicalValidationSkipIp;
	}

	public void setIsPharmacologicalValidationSkipIp(boolean isPharmacologicalValidationSkipIp) {
		this.isPharmacologicalValidationSkipIp = isPharmacologicalValidationSkipIp;
	}

	public Integer getBiomedicalsChargesServiceId() {
		return biomedicalsChargesServiceId;
	}

	public void setBiomedicalsChargesServiceId(Integer biomedicalsChargesServiceId) {
		this.biomedicalsChargesServiceId = biomedicalsChargesServiceId;
	}

	public Integer getHospitalChargesServiceId() {
		return hospitalChargesServiceId;
	}

	public void setHospitalChargesServiceId(Integer hospitalChargesServiceId) {
		this.hospitalChargesServiceId = hospitalChargesServiceId;
	}

	public Integer getMrdChargesServiceId() {
		return mrdChargesServiceId;
	}

	public void setMrdChargesServiceId(Integer mrdChargesServiceId) {
		this.mrdChargesServiceId = mrdChargesServiceId;
	}

	public Integer getReferalDetailsId() {
		return referalDetailsId;
	}

	public void setReferalDetailsId(Integer referalDetailsId) {
		this.referalDetailsId = referalDetailsId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPddTime() {
		return pddTime;
	}

	public void setPddTime(String pddTime) {
		this.pddTime = pddTime;
	}

	public Integer getDischargeTemplateId() {
		return dischargeTemplateId;
	}

	public void setDischargeTemplateId(Integer dischargeTemplateId) {
		this.dischargeTemplateId = dischargeTemplateId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getBed() {
		return bed;
	}

	public void setBed(Integer bed) {
		this.bed = bed;
	}

	

	public Integer getWard() {
		return ward;
	}

	public void setWard(Integer ward) {
		this.ward = ward;
	}

	

	public String getBednumber1() {
		return bednumber1;
	}

	public void setBednumber1(String bednumber1) {
		this.bednumber1 = bednumber1;
	}

	public String getWard1() {
		return ward1;
	}

	public void setWard1(String ward1) {
		this.ward1 = ward1;
	}

	public String getMlcNo() {
		return mlcNo;
	}

	public void setMlcNo(String mlcNo) {
		this.mlcNo = mlcNo;
	}

	public Date getPddNew() {
		return pddNew;
	}

	public void setPddNew(Date pddNew) {
		this.pddNew = pddNew;
	}
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDobNeuron() {
		return dobNeuron;
	}

	public void setDobNeuron(String dobNeuron) {
		this.dobNeuron = dobNeuron;
	}

	public Character getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(Character isHtml) {
		this.isHtml = isHtml;
	}

	public Boolean getIsIcuu() {
		return isIcuu;
	}

	public void setIsIcuu(Boolean isIcuu) {
		this.isIcuu = isIcuu;
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

	public Integer getToBillingBedCategoryId() {
		return toBillingBedCategoryId;
	}

	public void setToBillingBedCategoryId(Integer toBillingBedCategoryId) {
		this.toBillingBedCategoryId = toBillingBedCategoryId;
	}
	public Integer getOtStatus() {
		return otStatus;
	}

	public void setOtStatus(Integer otStatus) {
		this.otStatus = otStatus;
	}

	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public String getDietRemarkDesc() {
		return dietRemarkDesc;
	}

	public void setDietRemarkDesc(String dietRemarkDesc) {
		this.dietRemarkDesc = dietRemarkDesc;
	}

	public String getMlcRemark() {
		return mlcRemark;
	}

	public void setMlcRemark(String mlcRemark) {
		this.mlcRemark = mlcRemark;
	}
	public String getmodeOfArrival() {
		return modeOfArrival;
	}

	public void setmodeOfArrival(String modeOfArrival) {
		this.modeOfArrival = modeOfArrival;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

    public List<Object> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderDetailsList(List<Object> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }


	public boolean isFromPatientOrdersWorkList() {
		return fromPatientOrdersWorkList;
	}

	public void setFromPatientOrdersWorkList(boolean fromPatientOrdersWorkList) {
		this.fromPatientOrdersWorkList = fromPatientOrdersWorkList;
	}

	public String getWorkbenchRemark() {
		return workbenchRemark;
	}

	public void setWorkbenchRemark(String workbenchRemark) {
		this.workbenchRemark = workbenchRemark;
	}

	public Date getWorkbenchAddedDate() {
		return workbenchAddedDate;
	}

	public void setWorkbenchAddedDate(Date workbenchAddedDate) {
		this.workbenchAddedDate = workbenchAddedDate;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public Integer getTabId() {
		return tabId;
	}

	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getPatientWorkbenchId() {
		return patientWorkbenchId;
	}

	public void setPatientWorkbenchId(Integer patientWorkbenchId) {
		this.patientWorkbenchId = patientWorkbenchId;
	}

	public Integer getInterventionRequired() {
		return interventionRequired;
	}

	public void setInterventionRequired(Integer interventionRequired) {
		this.interventionRequired = interventionRequired;
	}

	public Integer getInterventionCompleted() {
		return interventionCompleted;
	}

	public void setInterventionCompleted(Integer interventionCompleted) {
		this.interventionCompleted = interventionCompleted;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getWorkbenchEndDate() {
		return workbenchEndDate;
	}

	public void setWorkbenchEndDate(Date workbenchEndDate) {
		this.workbenchEndDate = workbenchEndDate;
	}

	public String getLegacyVisitId() {
		return legacyVisitId;
	}

	public void setLegacyVisitId(String legacyVisitId) {
		this.legacyVisitId = legacyVisitId;
	}

	public Integer getCurrentTriage() {
		return currentTriage;
	}

	public void setCurrentTriage(Integer currentTriage) {
		this.currentTriage = currentTriage;
	}

	public Character getSepsis() {
		return sepsis;
	}

	public void setSepsis(Character sepsis) {
		this.sepsis = sepsis;
	}

	public Character getTruma() {
		return truma;
	}

	public void setTruma(Character truma) {
		this.truma = truma;
	}

	public Character getStroke() {
		return stroke;
	}

	public void setStroke(Character stroke) {
		this.stroke = stroke;
	}

	public Character getStemi() {
		return stemi;
	}

	public void setStemi(Character stemi) {
		this.stemi = stemi;
	}

	public Character getShock() {
		return shock;
	}

	public void setShock(Character shock) {
		this.shock = shock;
	}

	public String getTriageDetails() {
		return triageDetails;
	}

	public void setTriageDetails(String triageDetails) {
		this.triageDetails = triageDetails;
	}

	public String getSpecialityTriage() {
		return specialityTriage;
	}

	public void setSpecialityTriage(String specialityTriage) {
		this.specialityTriage = specialityTriage;
	}

	public Integer getSpecialityTriageId() {
		return specialityTriageId;
	}

	public void setSpecialityTriageId(Integer specialityTriageId) {
		this.specialityTriageId = specialityTriageId;
	}
}
