package com.param.global.otims.dto;

import java.util.List;

public class ProcedureSchedulingDto {

	private Integer procedureScheduleId ;

	private String procedureName;
	
	private String otType;
	
	private String procedureType ;
	
	private String scheduleDate;
	
	private String preferTime;
	
	private Integer totalDuration;
	
	private String department ;
	
	private Integer departmentId;
	
	private Integer proceduerStatusId;
	
	private Character status;
			
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer proReqId;
	
	private String changeProcedureDate;
	
	private String emailId;
	
	private String changeTime;
	
	private String uhidNumber;
	
	private String patientName;
	
	private Integer patientId;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private String preferedDateTime;
	
	private String requestDate;
	
	private String admintionDate;
	
	private String bedNumber;
	
	private List<ProcedureOTScheduleDto> procedureOTScheduleList ;
	
	private List<ProcedureStaffScheduleDto> procedureStaffScheduleList ;
	
	private List<ProcedureEquipmentScheduleDto> procedureEquipmentScheduleList ;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer doctorId;
	
	private String doctorName;
	
	private String genderCode;
	
	private String theatreName;
	
	private String tableName;

	private String age;
	
	private String encounterNumber;
	
	private String admissionNumber;
	
	private String birthDate;
	
	private String wardName;
	
	private String oldDate;
	
	private String oldTime;
	
	private Integer rescheduleOrNot;

	private boolean exportedToOtims;
	
	private String pdd;
	
	private String doa;
	
	private Integer anesthesiaTypeId; 
	
	private Integer visitTypeId;
	
	private String procedureStatusName;
	
	private boolean emergency;
	
	private Integer renderedFromDashboard;
	
	private Integer theatreId;
	
	private Integer otTransferStatus;
	
	private Integer genderId;
	
	private String imagePath;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer otReservationId;
	
	private String otReservationNo;
	
	private Integer scheduleId;

	private Integer reservationStatusId;
	
	private String reasonNote;
	
	private Integer reasonId;
	
	private String prefixCode;
	
	private Integer operationTheatreTypeId;
	
	private String dob;
	
	private Integer patientCategoryId;
	
	private Integer registrationTypeId;
	
	private String surgeonName;
	
	private Integer procedureConfigId;
	
	private Integer specialityId;
	
	private Integer bedBillingCategoryId;
	
	private String postferedTime;
	
	private String patientEmail;
	
	private String genderName;
	
	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public List<ProcedureOTScheduleDto> getProcedureOTScheduleList() {
		return procedureOTScheduleList;
	}

	public void setProcedureOTScheduleList(List<ProcedureOTScheduleDto> procedureOTScheduleList) {
		this.procedureOTScheduleList = procedureOTScheduleList;
	}

	public List<ProcedureStaffScheduleDto> getProcedureStaffScheduleList() {
		return procedureStaffScheduleList;
	}

	public void setProcedureStaffScheduleList(List<ProcedureStaffScheduleDto> procedureStaffScheduleList) {
		this.procedureStaffScheduleList = procedureStaffScheduleList;
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

	public List<ProcedureEquipmentScheduleDto> getProcedureEquipmentScheduleList() {
		return procedureEquipmentScheduleList;
	}

	public void setProcedureEquipmentScheduleList(List<ProcedureEquipmentScheduleDto> procedureEquipmentScheduleList) {
		this.procedureEquipmentScheduleList = procedureEquipmentScheduleList;
	}

	public Integer getProcedureScheduleId() {
		return procedureScheduleId;
	}

	public void setProcedureScheduleId(Integer procedureScheduleId) {
		this.procedureScheduleId = procedureScheduleId;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getOtType() {
		return otType;
	}

	public void setOtType(String otType) {
		this.otType = otType;
	}

	public String getProcedureType() {
		return procedureType;
	}

	public void setProcedureType(String procedureType) {
		this.procedureType = procedureType;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getPreferTime() {
		return preferTime;
	}

	public void setPreferTime(String preferTime) {
		this.preferTime = preferTime;
	}

	public Integer getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Integer totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProceduerStatusId() {
		return proceduerStatusId;
	}

	public void setProceduerStatusId(Integer proceduerStatusId) {
		this.proceduerStatusId = proceduerStatusId;
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

	public String getChangeProcedureDate() {
		return changeProcedureDate;
	}

	public void setChangeProcedureDate(String changeProcedureDate) {
		this.changeProcedureDate = changeProcedureDate;
	}

	public String getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getPreferedDateTime() {
		return preferedDateTime;
	}

	public void setPreferedDateTime(String preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getAdmintionDate() {
		return admintionDate;
	}

	public void setAdmintionDate(String admintionDate) {
		this.admintionDate = admintionDate;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
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

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public String getOldDate() {
		return oldDate;
	}

	public void setOldDate(String oldDate) {
		this.oldDate = oldDate;
	}

	public String getOldTime() {
		return oldTime;
	}

	public void setOldTime(String oldTime) {
		this.oldTime = oldTime;
	}

	public Integer getRescheduleOrNot() {
		return rescheduleOrNot;
	}

	public void setRescheduleOrNot(Integer rescheduleOrNot) {
		this.rescheduleOrNot = rescheduleOrNot;
	}


	public boolean isExportedToOtims() {
		return exportedToOtims;
	}

	public void setExportedToOtims(boolean exportedToOtims) {
		this.exportedToOtims = exportedToOtims;
	}

	public String getPdd() {
		return pdd;
	}

	public void setPdd(String pdd) {
		this.pdd = pdd;
	}

	public String getDoa() {
		return doa;
	}

	public void setDoa(String doa) {
		this.doa = doa;
	}

	public Integer getAnesthesiaTypeId() {
		return anesthesiaTypeId;
	}

	public void setAnesthesiaTypeId(Integer anesthesiaTypeId) {
		this.anesthesiaTypeId = anesthesiaTypeId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getProcedureStatusName() {
		return procedureStatusName;
	}

	public void setProcedureStatusName(String procedureStatusName) {
		this.procedureStatusName = procedureStatusName;
	}

	public boolean getEmergency() {
		return emergency;
	}

	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	public Integer getRenderedFromDashboard() {
		return renderedFromDashboard;
	}

	public void setRenderedFromDashboard(Integer renderedFromDashboard) {
		this.renderedFromDashboard = renderedFromDashboard;
	}

	public Integer getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Integer theatreId) {
		this.theatreId = theatreId;
	}

	public Integer getOtTransferStatus() {
		return otTransferStatus;
	}

	public void setOtTransferStatus(Integer otTransferStatus) {
		this.otTransferStatus = otTransferStatus;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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

	public Integer getOtReservationId() {
		return otReservationId;
	}

	public void setOtReservationId(Integer otReservationId) {
		this.otReservationId = otReservationId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getOtReservationNo() {
		return otReservationNo;
	}

	public void setOtReservationNo(String otReservationNo) {
		this.otReservationNo = otReservationNo;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getReservationStatusId() {
		return reservationStatusId;
	}

	public void setReservationStatusId(Integer reservationStatusId) {
		this.reservationStatusId = reservationStatusId;
	}

	public String getReasonNote() {
		return reasonNote;
	}

	public void setReasonNote(String reasonNote) {
		this.reasonNote = reasonNote;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public Integer getOperationTheatreTypeId() {
		return operationTheatreTypeId;
	}

	public void setOperationTheatreTypeId(Integer operationTheatreTypeId) {
		this.operationTheatreTypeId = operationTheatreTypeId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}

	public String getSurgeonName() {
		return surgeonName;
	}

	public void setSurgeonName(String surgeonName) {
		this.surgeonName = surgeonName;
	}

	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getBedBillingCategoryId() {
		return bedBillingCategoryId;
	}

	public void setBedBillingCategoryId(Integer bedBillingCategoryId) {
		this.bedBillingCategoryId = bedBillingCategoryId;
	}

	public String getPostferedTime() {
		return postferedTime;
	}

	public void setPostferedTime(String postferedTime) {
		this.postferedTime = postferedTime;
	}

	public String getPatientEmail() {
		return patientEmail;
	}

	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}

	public synchronized String getGenderName() {
		return genderName;
	}

	public synchronized void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	
}
