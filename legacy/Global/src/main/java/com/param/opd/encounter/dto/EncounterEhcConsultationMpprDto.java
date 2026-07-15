package com.param.opd.encounter.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SlotMasterDto;

public class EncounterEhcConsultationMpprDto {

	private Integer consultationMpprId;

	private Integer consultationMasterId;

	private Integer encounterId;

	private Integer serviceId;

	private Integer consultationSpecialityId;

	private Integer packageId;

	private Integer doctorId;

	private Integer unitId;

	private Integer organizationId;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private Character status;

	private Integer appointmentStatusId;

	private Integer orderDetailsId;

	private String serviceStandardName;

	private String serviceStandardCode;

	private String specialityName;

	private List<ServiceMasterDto> specialityList;

	private Integer patientId;

	private Integer patientTypeId;

	private Integer contractId;

	private Integer visitTypeId;

	private Integer sourceId;

	private Long encounterDate;

	private String encounterDateString;

	private String encounterTime;

	private Character isReferal;

	private Integer deptId;

	private Integer subDeptId;

	private String remark;

	private Integer kinDetailsId;

	private Integer paymentEntitlementTypeId;

	private String encounterNumber;

	private Date encounterDateTime;

	private Integer appointmentId;

	private Character isScheduledAppointment;

	private String doctorName;

	private Integer specialityId;

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

	private String referralTypeName;

	private Integer referralId;

	private String referralRemark;

	private String referralDesc;

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

	private Integer slotId;

	private Integer docQueueNumber;

	private Integer unitTokenNumber;

	private BigDecimal maxUnitTokenNumber;

	private BigDecimal maxDocQueueNumber;

	private Integer patientCategoryId;

	private Character isPriority;

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

	private String appoitmentDate;

	private String appoitmentTime;

	private String encounterDt;

	private Double serviceAmount;

	private String serviceName;
	
	private Integer childEncounterId;

	public Integer getConsultationMpprId() {
		return consultationMpprId;
	}

	public void setConsultationMpprId(Integer consultationMpprId) {
		this.consultationMpprId = consultationMpprId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public List<ServiceMasterDto> getSpecialityList() {
		return specialityList;
	}

	public void setSpecialityList(List<ServiceMasterDto> specialityList) {
		this.specialityList = specialityList;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
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

	public String getEncounterDateString() {
		return encounterDateString;
	}

	public void setEncounterDateString(String encounterDateString) {
		this.encounterDateString = encounterDateString;
	}

	public String getEncounterTime() {
		return encounterTime;
	}

	public void setEncounterTime(String encounterTime) {
		this.encounterTime = encounterTime;
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

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
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

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
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

	public Integer getAppointmentTypeId() {
		return appointmentTypeId;
	}

	public void setAppointmentTypeId(Integer appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
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

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getGenderDec() {
		return genderDec;
	}

	public void setGenderDec(String genderDec) {
		this.genderDec = genderDec;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getEncounterTypeId() {
		return encounterTypeId;
	}

	public void setEncounterTypeId(Integer encounterTypeId) {
		this.encounterTypeId = encounterTypeId;
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

	public String getReferralDesc() {
		return referralDesc;
	}

	public void setReferralDesc(String referralDesc) {
		this.referralDesc = referralDesc;
	}

	public Character getIsAppointmentRequired() {
		return isAppointmentRequired;
	}

	public void setIsAppointmentRequired(Character isAppointmentRequired) {
		this.isAppointmentRequired = isAppointmentRequired;
	}

	public SlotMasterDto getAppointmentObject() {
		return appointmentObject;
	}

	public void setAppointmentObject(SlotMasterDto appointmentObject) {
		this.appointmentObject = appointmentObject;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getEhcPackageId() {
		return ehcPackageId;
	}

	public void setEhcPackageId(Integer ehcPackageId) {
		this.ehcPackageId = ehcPackageId;
	}

	public BigDecimal getPackageBasePrice() {
		return packageBasePrice;
	}

	public void setPackageBasePrice(BigDecimal packageBasePrice) {
		this.packageBasePrice = packageBasePrice;
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
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

	public Character getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Character isCritical) {
		this.isCritical = isCritical;
	}

	public String getCounterDesc() {
		return counterDesc;
	}

	public void setCounterDesc(String counterDesc) {
		this.counterDesc = counterDesc;
	}

	public Character getIsCalled() {
		return isCalled;
	}

	public void setIsCalled(Character isCalled) {
		this.isCalled = isCalled;
	}

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
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

	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
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

	public Character getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(Character deptWiseAppointment) {
		this.deptWiseAppointment = deptWiseAppointment;
	}

	public Character getIsNursingAssessmentRequiredForSpeciality() {
		return isNursingAssessmentRequiredForSpeciality;
	}

	public void setIsNursingAssessmentRequiredForSpeciality(Character isNursingAssessmentRequiredForSpeciality) {
		this.isNursingAssessmentRequiredForSpeciality = isNursingAssessmentRequiredForSpeciality;
	}

	public Integer getOldDocId() {
		return oldDocId;
	}

	public void setOldDocId(Integer oldDocId) {
		this.oldDocId = oldDocId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Character getFrontDeskArrival() {
		return frontDeskArrival;
	}

	public void setFrontDeskArrival(Character frontDeskArrival) {
		this.frontDeskArrival = frontDeskArrival;
	}

	public Character getDoctorDeskArrival() {
		return doctorDeskArrival;
	}

	public void setDoctorDeskArrival(Character doctorDeskArrival) {
		this.doctorDeskArrival = doctorDeskArrival;
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

	public String getEncounterDt() {
		return encounterDt;
	}

	public void setEncounterDt(String encounterDt) {
		this.encounterDt = encounterDt;
	}

	public Double getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(Double serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getConsultationMasterId() {
		return consultationMasterId;
	}

	public void setConsultationMasterId(Integer consultationMasterId) {
		this.consultationMasterId = consultationMasterId;
	}

	public String getReferralTypeName() {
		return referralTypeName;
	}

	public void setReferralTypeName(String referralTypeName) {
		this.referralTypeName = referralTypeName;
	}

	public Integer getChildEncounterId() {
		return childEncounterId;
	}

	public void setChildEncounterId(Integer childEncounterId) {
		this.childEncounterId = childEncounterId;
	}
	

}
