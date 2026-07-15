package com.param.global.otims.dto;

import java.util.List;

import com.param.global.dto.PatientRegistrationDto;

public class OTReservationDto {

	private Integer otReservationId;
	
	private String otReservationNo;
	
	private Integer proReqId;
	
	private Integer patientId;
	
	private Integer admissionId;
	
	private Integer encounterId;

	private Integer visitTypeId;

	private Integer scheduleId;

	private Integer reservationStatusId;
	
	private String reasonNote;
	
	private Integer reasonId;
	
	private Character status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private List<ProcedureOrderMasterDto> procedureOrderMasterDto;
	
	private ProcedureSchedulingDto procedureSchedulingDto;
	
	private PatientRegistrationDto patientRegistrationDto;
	
	private String uhidNumber;
	
	private Integer prefixId;
	
	private String patientName;
	
	private String dob;
	
	private Integer genderId;
	
	private Integer patientCategoryId;
	
	private Integer registrationTypeId;
	
	private String age;
	
	private String genderCode;
	
	private String procedureName;
	
	private String otType;
	
	private String procedureType;
	
	private String preferTime;
	
	private String scheduleDate;
	
	private String tableName;
	
	private String emailId; 
	
	private String prefixCode;
	
	private boolean isSelected;
	
	private String visitNumber;
	
	private String imagePath;
	
	private Integer operationTheatreTypeId;
	
	private List<OTReservationDto>  visitTypeList;
	private List<OTReservationDto> surgeonList;
	
	private String doctorName;

	private String pdd;
	
	private String doa;
	
	private String bedNumber;
	
	private Integer serviceId;
	
	private Integer specialityId;
	
	private Integer subSpecialityId;
	
	private Integer tableId;
	
	private Integer theatreId;
	
	private String theatreName;
	
	private String toTime;
	
	private Integer procedureScheduleId;
	
	private Integer otStatus;

	
	public List<ProcedureOrderMasterDto> getProcedureOrderMasterDto() {
		return procedureOrderMasterDto;
	}

	public void setProcedureOrderMasterDto(List<ProcedureOrderMasterDto> procedureOrderMasterDto) {
		this.procedureOrderMasterDto = procedureOrderMasterDto;
	}

	public ProcedureSchedulingDto getProcedureSchedulingDto() {
		return procedureSchedulingDto;
	}

	public void setProcedureSchedulingDto(ProcedureSchedulingDto procedureSchedulingDto) {
		this.procedureSchedulingDto = procedureSchedulingDto;
	}

	public PatientRegistrationDto getPatientRegistrationDto() {
		return patientRegistrationDto;
	}

	public void setPatientRegistrationDto(PatientRegistrationDto patientRegistrationDto) {
		this.patientRegistrationDto = patientRegistrationDto;
	}

	public Integer getOtReservationId() {
		return otReservationId;
	}

	public void setOtReservationId(Integer otReservationId) {
		this.otReservationId = otReservationId;
	}

	public String getOtReservationNo() {
		return otReservationNo;
	}

	public void setOtReservationNo(String otReservationNo) {
		this.otReservationNo = otReservationNo;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
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

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
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

	public String getPreferTime() {
		return preferTime;
	}

	public void setPreferTime(String preferTime) {
		this.preferTime = preferTime;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(String visitNumber) {
		this.visitNumber = visitNumber;
	}

	public List<OTReservationDto> getVisitTypeList() {
		return visitTypeList;
	}

	public void setVisitTypeList(List<OTReservationDto> visitTypeList) {
		this.visitTypeList = visitTypeList;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Integer getOperationTheatreTypeId() {
		return operationTheatreTypeId;
	}

	public void setOperationTheatreTypeId(Integer operationTheatreTypeId) {
		this.operationTheatreTypeId = operationTheatreTypeId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Integer theatreId) {
		this.theatreId = theatreId;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public List<OTReservationDto> getSurgeonList() {
		return surgeonList;
	}

	public void setSurgeonList(List<OTReservationDto> surgeonList) {
		this.surgeonList = surgeonList;
	}

	public Integer getProcedureScheduleId() {
		return procedureScheduleId;
	}

	public void setProcedureScheduleId(Integer procedureScheduleId) {
		this.procedureScheduleId = procedureScheduleId;
	}

	public Integer getOtStatus() {
		return otStatus;
	}

	public void setOtStatus(Integer otStatus) {
		this.otStatus = otStatus;
	}
	
	
}
