package com.param.global.otims.dto;

import java.util.Date;
import java.util.List;

public class ProcedureOrderMasterDto {
	private String mongoid;
	private Integer procedureOrderId;
	private Integer serviceId;
	private String preferedDateTime;
	private String preferedTime;
	private String preferedDateTime1;
	private Integer orgId;
	private Integer unitId;
	private Integer patientId;
	private Integer doctorId;
	private Integer admissionId;
	private Integer encounterId;
	private Integer visitTypeId;
	private String orderDateTime;
	private Integer procedureStatusId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private char isPrimarySecondary;
	private Double amount;
	private Double rate;
	private Integer urgency;
	private String requestReason;
	private char isIcuRequired;
	private char isBloodComponentRequired;
	private char isPacRequired;
	private Integer parentProcedureOrderId;
	private String uhidNumber;
	private String patientName;
	private String dob;
	private String doa;
	private String pdd;
	private String bedNumber;
	private String wardName;
	private String cptCode;
	private String service;
	private String procedureType;
	private String fromDate;
	private String toDate;
	private String 	genderCode;
	private String age;
	private Integer duration;
	private Integer paymentEntitlementId;
	private Integer patientTypeId;
	private Integer billingBedCategoryId;
	private String serviceCode;
	private String speciality;
	private Integer proReqId;
	private Integer specialityId;
	private String specialityName;
	private String procedureName;
	private ProcedureReqMasterDto procedureReqMasterDto;
	private Integer subSpecialityId;
	private Integer procedureTypeId;
	private Integer doctorSpecialityId;
	private Integer procedureDuration;
	private Integer payeeId;
	private Integer contractId;
	private Integer patientCategoryId;
	private Boolean exportedToOtims;
	private String serviceName;
	private String status;
	private Integer id;
	private String dischargeDateTime;
	private String imagePath;
	private String doctorName;
	private String primaryDiagnosis;
	private Integer genderId;
	private String email;
	private Integer organizationId;
	private Integer tariffId; 
	private Integer roleId;
	private String fromTime;
	private String toTime;
	private Boolean emergancy;
	private String birthDate;
	private Boolean isEdited;
	private Integer reservationStatusId;
	
	private String postferedTime;
	private List<SurgicalCalenderMapperDto> listCalenderMapperDto;
	
	private List<ProcedureOrderConsentDto> procedureOrderConsentList; 
	
	private List<ProcedureOrderChecklistDto> procedureOrderCheckList;
	
	private List<ProcedureOrderStaffDto> procedureOrderStaffList;
	
	private List<ProcedureOrderServiceDto> procedureOrderServiceList;
	
	private List<ImplantDetailsDto> implantDetailsList;
	
	private List<MedicationDetailsDto> medicationDetailsList;
	
	private List<ProcedureOrderEquipmentDto> procedureOrderEquipmentList;
	
	private ProcedureOrderPatientEducationDto procedureOrderPatientEducationDto;
	
	private String theatreTypeName;
	
	private Integer operationTheatreTypeId;
	
	private Integer equipmentTypeId;
	
	private Integer procedureScheduleId;
	
	private Integer measurementUnitId;
	
	private Integer equipmentId;

	private Integer reasonId;
	
	private String reasonNote;
	
	private String year;
	
	private String month;
	
	private Integer otCompletionTimeSheetId;
	
	private String userName;
	
	private Integer procedureSequence;
	
	private  List<SubSequentDetailsDto> subSequentDetailsList;
	
	private Double originalAmount;
	private Integer surgeryGradeId;
	private String surgeryGrade;
	private Date preferedDateTimeNew;

	private List <ProcedureOrderMasterDto> orderedProcedureList;
	private String addedDate;
	private List<String> dates;

	private String createdDate1;
	

	private String procedureStatus;

	private String uuid;
	private Integer hospitalId;

	public List<ProcedureOrderMasterDto> getOrderedProcedureList() {
		return orderedProcedureList;
	}

	public void setOrderedProcedureList(List<ProcedureOrderMasterDto> orderedProcedureList) {
		this.orderedProcedureList = orderedProcedureList;
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

	public String getCreatedDate1() {
		return createdDate1;
	}

	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}

	public Integer getMeasurementUnitId() {
		return measurementUnitId;
	}
	public void setMeasurementUnitId(Integer measurementUnitId) {
		this.measurementUnitId = measurementUnitId;
	}
	
	public Integer getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	
 	public Integer getDoctorSpecialityId() {

 		return doctorSpecialityId;
	}
	public void setDoctorSpecialityId(Integer doctorSpecialityId) {
		this.doctorSpecialityId = doctorSpecialityId;
	}
	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}
	
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public Integer getProReqId() {
		return proReqId;
	}
	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getPatientTypeId() {
		return patientTypeId;
	}
	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}
	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}
	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	

	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getCptCode() {
		return cptCode;
	}
	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getProcedureType() {
		return procedureType;
	}
	public void setProcedureType(String procedureType) {
		this.procedureType = procedureType;
	}
	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}
	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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
	public String getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	public String getPreferedDateTime() {
		return preferedDateTime;
	}
	public void setPreferedDateTime(String preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}
	public Integer getProcedureStatusId() {
		return procedureStatusId;
	}
	public void setProcedureStatusId(Integer procedureStatusId) {
		this.procedureStatusId = procedureStatusId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	public char getIsPrimarySecondary() {
		return isPrimarySecondary;
	}
	public void setIsPrimarySecondary(char isPrimarySecondary) {
		this.isPrimarySecondary = isPrimarySecondary;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getUrgency() {
		return urgency;
	}
	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}
	public String getRequestReason() {
		return requestReason;
	}
	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}
	public char getIsIcuRequired() {
		return isIcuRequired;
	}
	public void setIsIcuRequired(char isIcuRequired) {
		this.isIcuRequired = isIcuRequired;
	}
	public char getIsBloodComponentRequired() {
		return isBloodComponentRequired;
	}
	public void setIsBloodComponentRequired(char isBloodComponentRequired) {
		this.isBloodComponentRequired = isBloodComponentRequired;
	}
	public char getIsPacRequired() {
		return isPacRequired;
	}
	public void setIsPacRequired(char isPacRequired) {
		this.isPacRequired = isPacRequired;
	}
	public Integer getParentProcedureOrderId() {
		return parentProcedureOrderId;
	}
	public void setParentProcedureOrderId(Integer parentProcedureOrderId) {
		this.parentProcedureOrderId = parentProcedureOrderId;
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
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public ProcedureReqMasterDto getProcedureReqMasterDto() {
		return procedureReqMasterDto;
	}
	public void setProcedureReqMasterDto(ProcedureReqMasterDto procedureReqMasterDto) {
		this.procedureReqMasterDto = procedureReqMasterDto;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public String getPreferedTime() {
		return preferedTime;
	}
	public void setPreferedTime(String preferedTime) {
		this.preferedTime = preferedTime;
	}
	public List<SurgicalCalenderMapperDto> getListCalenderMapperDto() {
		return listCalenderMapperDto;
	}
	public void setListCalenderMapperDto(List<SurgicalCalenderMapperDto> listCalenderMapperDto) {
		this.listCalenderMapperDto = listCalenderMapperDto;
	}
	public Integer getProcedureDuration() {
		return procedureDuration;
	}
	public void setProcedureDuration(Integer procedureDuration) {
		this.procedureDuration = procedureDuration;
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
	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}
	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}
	public Boolean getExportedToOtims() {
		return exportedToOtims;
	}
	public void setExportedToOtims(Boolean exportedToOtims) {
		this.exportedToOtims = (exportedToOtims==null)?false:exportedToOtims;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDischargeDateTime() {
		return dischargeDateTime;
	}
	public void setDischargeDateTime(String dischargeDateTime) {
		this.dischargeDateTime = dischargeDateTime;
	}
	public String getMongoid() {
		return mongoid;
	}
	public void setMongoid(String mongoid) {
		this.mongoid = mongoid;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getPrimaryDiagnosis() {
		return primaryDiagnosis;
	}
	public void setPrimaryDiagnosis(String primaryDiagnosis) {
		this.primaryDiagnosis = primaryDiagnosis;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public List<ProcedureOrderConsentDto> getProcedureOrderConsentList() {
		return procedureOrderConsentList;
	}
	public void setProcedureOrderConsentList(List<ProcedureOrderConsentDto> procedureOrderConsentList) {
		this.procedureOrderConsentList = procedureOrderConsentList;
	}
	public List<ProcedureOrderChecklistDto> getProcedureOrderCheckList() {
		return procedureOrderCheckList;
	}
	public void setProcedureOrderCheckList(List<ProcedureOrderChecklistDto> procedureOrderCheckList) {
		this.procedureOrderCheckList = procedureOrderCheckList;
	}
	public List<ProcedureOrderStaffDto> getProcedureOrderStaffList() {
		return procedureOrderStaffList;
	}
	public void setProcedureOrderStaffList(List<ProcedureOrderStaffDto> procedureOrderStaffList) {
		this.procedureOrderStaffList = procedureOrderStaffList;
	}
	public List<ProcedureOrderServiceDto> getProcedureOrderServiceList() {
		return procedureOrderServiceList;
	}
	public void setProcedureOrderServiceList(List<ProcedureOrderServiceDto> procedureOrderServiceList) {
		this.procedureOrderServiceList = procedureOrderServiceList;
	}
	public List<ImplantDetailsDto> getImplantDetailsList() {
		return implantDetailsList;
	}
	public void setImplantDetailsList(List<ImplantDetailsDto> implantDetailsList) {
		this.implantDetailsList = implantDetailsList;
	}
	public List<MedicationDetailsDto> getMedicationDetailsList() {
		return medicationDetailsList;
	}
	public void setMedicationDetailsList(List<MedicationDetailsDto> medicationDetailsList) {
		this.medicationDetailsList = medicationDetailsList;
	}
	public ProcedureOrderPatientEducationDto getProcedureOrderPatientEducationDto() {
		return procedureOrderPatientEducationDto;
	}
	public void setProcedureOrderPatientEducationDto(ProcedureOrderPatientEducationDto procedureOrderPatientEducationDto) {
		this.procedureOrderPatientEducationDto = procedureOrderPatientEducationDto;
	}
	public List<ProcedureOrderEquipmentDto> getProcedureOrderEquipmentList() {
		return procedureOrderEquipmentList;
	}
	public void setProcedureOrderEquipmentList(List<ProcedureOrderEquipmentDto> procedureOrderEquipmentList) {
		this.procedureOrderEquipmentList = procedureOrderEquipmentList;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getTheatreTypeName() {
		return theatreTypeName;
	}
	public void setTheatreTypeName(String theatreTypeName) {
		this.theatreTypeName = theatreTypeName;
	}
	public Integer getOperationTheatreTypeId() {
		return operationTheatreTypeId;
	}
	public void setOperationTheatreTypeId(Integer operationTheatreTypeId) {
		this.operationTheatreTypeId = operationTheatreTypeId;
	}
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public Integer getEquipmentTypeId() {
		return equipmentTypeId;
	}
	public void setEquipmentTypeId(Integer equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public Boolean getEmergancy() {
		return emergancy;
	}
	public void setEmergancy(Boolean emergancy) {
		this.emergancy = emergancy;
	}
	public Integer getProcedureScheduleId() {
		return procedureScheduleId;
	}
	public void setProcedureScheduleId(Integer procedureScheduleId) {
		this.procedureScheduleId = procedureScheduleId;
	}
	public Integer getReasonId() {
		return reasonId;
	}
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	public String getReasonNote() {
		return reasonNote;
	}
	public void setReasonNote(String reasonNote) {
		this.reasonNote = reasonNote;
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
	public String getBedNumber() {
		return bedNumber;
	}
	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Integer getOtCompletionTimeSheetId() {
		return otCompletionTimeSheetId;
	}
	public void setOtCompletionTimeSheetId(Integer otCompletionTimeSheetId) {
		this.otCompletionTimeSheetId = otCompletionTimeSheetId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getProcedureSequence() {
		return procedureSequence;
	}
	public void setProcedureSequence(Integer procedureSequence) {
		this.procedureSequence = procedureSequence;
	}
	public Boolean getIsEdited() {
		return isEdited;
	}
	public void setIsEdited(Boolean isEdited) {
		this.isEdited = isEdited;
	}
	public List<SubSequentDetailsDto> getSubSequentDetailsList() {
		return subSequentDetailsList;
	}
	public void setSubSequentDetailsList(List<SubSequentDetailsDto> subSequentDetailsList) {
		this.subSequentDetailsList = subSequentDetailsList;
	}
	public Double getOriginalAmount() {
		return originalAmount;
	}
	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getPreferedDateTime1() {
		return preferedDateTime1;
	}

	public void setPreferedDateTime1(String preferedDateTime1) {
		this.preferedDateTime1 = preferedDateTime1;
	}

	public Boolean getEdited() {
		return isEdited;
	}

	public void setEdited(Boolean edited) {
		isEdited = edited;
	}
	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}
	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}
	public String getSurgeryGrade() {
		return surgeryGrade;
	}
	public void setSurgeryGrade(String surgeryGrade) {
		this.surgeryGrade = surgeryGrade;
	}
	public Integer getReservationStatusId() {
		return reservationStatusId;
	}
	public void setReservationStatusId(Integer reservationStatusId) {
		this.reservationStatusId = reservationStatusId;
	}
	public String getPostferedTime() {
		return postferedTime;
	}
	public void setPostferedTime(String postferedTime) {
		this.postferedTime = postferedTime;
	}
	public Date getPreferedDateTimeNew() {
		return preferedDateTimeNew;
	}
	public void setPreferedDateTimeNew(Date preferedDateTimeNew) {
		this.preferedDateTimeNew = preferedDateTimeNew;
	}

	public String getProcedureStatus() {
		return procedureStatus;
	}

	public void setProcedureStatus(String procedureStatus) {
		this.procedureStatus = procedureStatus;
	}
	
	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
