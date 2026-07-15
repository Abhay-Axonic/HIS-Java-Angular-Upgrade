package com.param.opd.appointment.dto;

import java.util.List;

import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;

public class HemodialysisAppointmentMapperDto {
	private Integer patientId;
	private String uhidNumber;
	private Integer prefixId;
	private String prefixCode;
	private String patientName;
	private Integer genderId;
	private String genderCode;
	private String dob;
	private String ageString;
	private Integer encounterId;
	private Integer modalityId;
	private String modalityDesc;
	private Integer doctorId;
	private String doctorName;
	private Integer specialityId;
	private String specialityName;
	private String serviceName;
	private String orderNo;
	private String orderDate;
	private Integer organizationId;
	private Integer unitId;
	private String fromDate;
	private String toDate;
	private Integer serviceId;
	private Integer priorityId;
	private String serviceCode;
	private String priorityName;
	private Integer orderDetailsId;
	private String currentDate;
	private Integer updatedBy;
	private Integer hemodialysisSpecialityId;
	private Integer admissionId;
	private Integer subSpecialityId;
	private Integer admissionIdFrAppointment;
	private Integer serviceRendered;
	private Character status;
	private String accessionNumber;
	private String bedNumber;
	private String deptName;
	private String mobileNumber;
	private String patientMail;
	private Integer visitTypeId;
	private Character isPACSRequired;
	private String identificationNumber;
	private String visitName;
	private String patientCategory;
	private String visitNumber;
	private String countryCode;
	private Integer fromBedId;
	private Integer toBedId;
	private String toBedNumber;
	private String appointmentTime;
	private Integer fromWardId;
	private Integer fromRoomId;
	private Integer toWardId;
	private Integer toRoomId;
	private Integer fromBedCategoryId;
	private Integer fromBillingBedCategoryId;
	private String updatedDate;
	private Integer slotId;
	private String appointmentDate;
	private Character isReviewApplicable;
	private String admissionNumber;
	private String doa;
	private String pdd;
	private Integer encounterTypeId;
	private Character isCritical;
	private Character isVip;
	private Integer  orderDetailsIdForAppoitment;
	private Integer  userId;
	private Integer  reasonId;
	private Character isPreOrPostBilling;
    private Character isCancel;
    private Integer templateTypeId;
    private String location;
    private Integer ordDoctorId;
    private Integer noOfRecordsPerPage;
  	private Integer  offset;
  	private String wardName;
  	private Integer appointmentId;
  	private Character hemodialysisWorkbenchAcceptance;
  	private Character technicianAcceptance;
	private Integer chairId;
	private Integer machineId;
	private Integer technicianId;
	private Integer dialysisAppMapperId;
	private Integer dialysisDoctorId;
	private String chairName;
	private List<Integer> visitList;
	private String technicianName;
	private String machineName;
	private Integer dialysisStatusId;
	private String dialysisStartDate;
	private String dialysisStartTime;
	private String dialysisEndDate;
	private String dialysisEndTime;
	private String dialysisDuration;
	private String admittingDoctorName;
	private List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList;
	private List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList;
	private Integer billingMasterId;
	private String searchKeyword;
	
	public List<OrderDetailsPayeeMapperDto> getOrderDetailsPayeeMapperDtosList() {
		return orderDetailsPayeeMapperDtosList;
	}
	public void setOrderDetailsPayeeMapperDtosList(List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList) {
		this.orderDetailsPayeeMapperDtosList = orderDetailsPayeeMapperDtosList;
	}
	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperDtosList() {
		return orderDetailsPackageMapperDtosList;
	}
	public void setOrderDetailsPackageMapperDtosList(List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList) {
		this.orderDetailsPackageMapperDtosList = orderDetailsPackageMapperDtosList;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
	public String getPrefixCode() {
		return prefixCode;
	}
	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
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
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Integer getHemodialysisSpecialityId() {
		return hemodialysisSpecialityId;
	}
	public void setHemodialysisSpecialityId(Integer hemodialysisSpecialityId) {
		this.hemodialysisSpecialityId = hemodialysisSpecialityId;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public Integer getAdmissionIdFrAppointment() {
		return admissionIdFrAppointment;
	}
	public void setAdmissionIdFrAppointment(Integer admissionIdFrAppointment) {
		this.admissionIdFrAppointment = admissionIdFrAppointment;
	}
	public Integer getServiceRendered() {
		return serviceRendered;
	}
	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public String getAccessionNumber() {
		return accessionNumber;
	}
	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}
	public String getBedNumber() {
		return bedNumber;
	}
	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getPatientMail() {
		return patientMail;
	}
	public void setPatientMail(String patientMail) {
		this.patientMail = patientMail;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Character getIsPACSRequired() {
		return isPACSRequired;
	}
	public void setIsPACSRequired(Character isPACSRequired) {
		this.isPACSRequired = isPACSRequired;
	}
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	public String getVisitName() {
		return visitName;
	}
	public void setVisitName(String visitName) {
		this.visitName = visitName;
	}
	public String getPatientCategory() {
		return patientCategory;
	}
	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}
	public String getVisitNumber() {
		return visitNumber;
	}
	public void setVisitNumber(String visitNumber) {
		this.visitNumber = visitNumber;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Integer getFromBedId() {
		return fromBedId;
	}
	public void setFromBedId(Integer fromBedId) {
		this.fromBedId = fromBedId;
	}
	public Integer getToBedId() {
		return toBedId;
	}
	public void setToBedId(Integer toBedId) {
		this.toBedId = toBedId;
	}
	public String getToBedNumber() {
		return toBedNumber;
	}
	public void setToBedNumber(String toBedNumber) {
		this.toBedNumber = toBedNumber;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public Integer getFromWardId() {
		return fromWardId;
	}
	public void setFromWardId(Integer fromWardId) {
		this.fromWardId = fromWardId;
	}
	public Integer getFromRoomId() {
		return fromRoomId;
	}
	public void setFromRoomId(Integer fromRoomId) {
		this.fromRoomId = fromRoomId;
	}
	public Integer getToWardId() {
		return toWardId;
	}
	public void setToWardId(Integer toWardId) {
		this.toWardId = toWardId;
	}
	public Integer getToRoomId() {
		return toRoomId;
	}
	public void setToRoomId(Integer toRoomId) {
		this.toRoomId = toRoomId;
	}
	public Integer getFromBedCategoryId() {
		return fromBedCategoryId;
	}
	public void setFromBedCategoryId(Integer fromBedCategoryId) {
		this.fromBedCategoryId = fromBedCategoryId;
	}
	public Integer getFromBillingBedCategoryId() {
		return fromBillingBedCategoryId;
	}
	public void setFromBillingBedCategoryId(Integer fromBillingBedCategoryId) {
		this.fromBillingBedCategoryId = fromBillingBedCategoryId;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getSlotId() {
		return slotId;
	}
	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public Character getIsReviewApplicable() {
		return isReviewApplicable;
	}
	public void setIsReviewApplicable(Character isReviewApplicable) {
		this.isReviewApplicable = isReviewApplicable;
	}
	public String getAdmissionNumber() {
		return admissionNumber;
	}
	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
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
	public Character getIsVip() {
		return isVip;
	}
	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}
	public Integer getOrderDetailsIdForAppoitment() {
		return orderDetailsIdForAppoitment;
	}
	public void setOrderDetailsIdForAppoitment(Integer orderDetailsIdForAppoitment) {
		this.orderDetailsIdForAppoitment = orderDetailsIdForAppoitment;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getReasonId() {
		return reasonId;
	}
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}
	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}
	public Character getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(Character isCancel) {
		this.isCancel = isCancel;
	}
	public Integer getTemplateTypeId() {
		return templateTypeId;
	}
	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getOrdDoctorId() {
		return ordDoctorId;
	}
	public void setOrdDoctorId(Integer ordDoctorId) {
		this.ordDoctorId = ordDoctorId;
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
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Character getHemodialysisWorkbenchAcceptance() {
		return hemodialysisWorkbenchAcceptance;
	}
	public void setHemodialysisWorkbenchAcceptance(Character hemodialysisWorkbenchAcceptance) {
		this.hemodialysisWorkbenchAcceptance = hemodialysisWorkbenchAcceptance;
	}
	public Character getTechnicianAcceptance() {
		return technicianAcceptance;
	}
	public void setTechnicianAcceptance(Character technicianAcceptance) {
		this.technicianAcceptance = technicianAcceptance;
	}
	public Integer getChairId() {
		return chairId;
	}
	public void setChairId(Integer chairId) {
		this.chairId = chairId;
	}
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	public Integer getTechnicianId() {
		return technicianId;
	}
	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}
	public Integer getDialysisAppMapperId() {
		return dialysisAppMapperId;
	}
	public void setDialysisAppMapperId(Integer dialysisAppMapperId) {
		this.dialysisAppMapperId = dialysisAppMapperId;
	}
	public Integer getDialysisDoctorId() {
		return dialysisDoctorId;
	}
	public void setDialysisDoctorId(Integer dialysisDoctorId) {
		this.dialysisDoctorId = dialysisDoctorId;
	}
	public String getChairName() {
		return chairName;
	}
	public void setChairName(String chairName) {
		this.chairName = chairName;
	}
	public List<Integer> getVisitList() {
		return visitList;
	}
	public void setVisitList(List<Integer> visitList) {
		this.visitList = visitList;
	}
	public String getTechnicianName() {
		return technicianName;
	}
	public void setTechnicianName(String technicianName) {
		this.technicianName = technicianName;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public Integer getDialysisStatusId() {
		return dialysisStatusId;
	}
	public void setDialysisStatusId(Integer dialysisStatusId) {
		this.dialysisStatusId = dialysisStatusId;
	}
	public String getDialysisStartDate() {
		return dialysisStartDate;
	}
	public void setDialysisStartDate(String dialysisStartDate) {
		this.dialysisStartDate = dialysisStartDate;
	}
	public String getDialysisStartTime() {
		return dialysisStartTime;
	}
	public void setDialysisStartTime(String dialysisStartTime) {
		this.dialysisStartTime = dialysisStartTime;
	}
	public String getDialysisEndDate() {
		return dialysisEndDate;
	}
	public void setDialysisEndDate(String dialysisEndDate) {
		this.dialysisEndDate = dialysisEndDate;
	}
	public String getDialysisEndTime() {
		return dialysisEndTime;
	}
	public void setDialysisEndTime(String dialysisEndTime) {
		this.dialysisEndTime = dialysisEndTime;
	}
	public String getDialysisDuration() {
		return dialysisDuration;
	}
	public void setDialysisDuration(String dialysisDuration) {
		this.dialysisDuration = dialysisDuration;
	}
	public String getAdmittingDoctorName() {
		return admittingDoctorName;
	}
	public void setAdmittingDoctorName(String admittingDoctorName) {
		this.admittingDoctorName = admittingDoctorName;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public Integer getBillingMasterId() {
		return billingMasterId;
	}
	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}
  	
  	
}
