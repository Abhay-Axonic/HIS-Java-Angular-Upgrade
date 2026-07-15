package com.param.opd.coversheet.dto;

import java.util.List;

public class NursingOrderMapperDto {
	private Integer nursingOrderMapperId;
	
	private Integer nursingOrderId;
	
	private Integer frequencyId;
	
	private String instruction;
	
	private String startDate;
	
	private String stopDate;
	
	private Integer patientId;
	
	private Integer admissionId;
	
	private Integer doctorId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String nursingOrderDesc;
	
	private String frequency;
	private String administrationTimefreq;
	
	private Integer serviceId;
	
	private Integer specialityId;
	
	private Integer subSpecialityId;
	
	private String serviceStandardName;
	
	private String specialityName;
	
	private String subSpecialityName;
	private String color;

	public String getAdministrationTimefreq() {
		return administrationTimefreq;
	}

	public void setAdministrationTimefreq(String administrationTimefreq) {
		this.administrationTimefreq = administrationTimefreq;
	}

	private Boolean checkStatus;
	private String patientName;
	private String uhidNumber;
	private String wardName;
	private String roomDesc;
	private String bedDesc;
	private String floorName;

	private String doctorName;
	private String remark;
	private Character nurseStatus;
	private boolean isDisable;
	private String postBufferAdministrationTime;
	private String preBufferAdministrationTime;
	private String administrationTime;
	private String frequencyDesc;
	private String administrationDate;
	private String startDateParse;
	private String stopDateParse;
	private String userName;
	private String currentTime;

	private List<NursingOrderMapperDto> nursingOrderMapperDtoList;
	private List<NursingOrderMapperDto> nursingOrderAdministeredTimeList;
	private List<NursingOrderMapperDto> orderDateList;
	private List<NursingOrderMapperDto> listSavedPatient;
	
	private Integer reasonId;
	private String reasonDesc;
	
	private Integer pendingNurOrdAdministrationId;
	public Integer getNursingOrderMapperId() {
		return nursingOrderMapperId;
	}

	public void setNursingOrderMapperId(Integer nursingOrderMapperId) {
		this.nursingOrderMapperId = nursingOrderMapperId;
	}

	public Integer getNursingOrderId() {
		return nursingOrderId;
	}

	public void setNursingOrderId(Integer nursingOrderId) {
		this.nursingOrderId = nursingOrderId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}



	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public List<NursingOrderMapperDto> getNursingOrderMapperDtoList() {
		return nursingOrderMapperDtoList;
	}

	public void setNursingOrderMapperDtoList(List<NursingOrderMapperDto> nursingOrderMapperDtoList) {
		this.nursingOrderMapperDtoList = nursingOrderMapperDtoList;
	}

	public String getNursingOrderDesc() {
		return nursingOrderDesc;
	}

	public void setNursingOrderDesc(String nursingOrderDesc) {
		this.nursingOrderDesc = nursingOrderDesc;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Boolean getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Boolean checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public String getBedDesc() {
		return bedDesc;
	}

	public void setBedDesc(String bedDesc) {
		this.bedDesc = bedDesc;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getNurseStatus() {
		return nurseStatus;
	}

	public void setNurseStatus(Character nurseStatus) {
		this.nurseStatus = nurseStatus;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	public List<NursingOrderMapperDto> getNursingOrderAdministeredTimeList() {
		return nursingOrderAdministeredTimeList;
	}

	public void setNursingOrderAdministeredTimeList(List<NursingOrderMapperDto> nursingOrderAdministeredTimeList) {
		this.nursingOrderAdministeredTimeList = nursingOrderAdministeredTimeList;
	}

	public String getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(String postBufferAdministrationTime) {
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	public String getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(String preBufferAdministrationTime) {
		this.preBufferAdministrationTime = preBufferAdministrationTime;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public String getAdministrationDate() {
		return administrationDate;
	}

	public void setAdministrationDate(String administrationDate) {
		this.administrationDate = administrationDate;
	}

	public List<NursingOrderMapperDto> getOrderDateList() {
		return orderDateList;
	}

	public void setOrderDateList(List<NursingOrderMapperDto> orderDateList) {
		this.orderDateList = orderDateList;
	}

	public String getStartDateParse() {
		return startDateParse;
	}

	public void setStartDateParse(String startDateParse) {
		this.startDateParse = startDateParse;
	}

	public String getStopDateParse() {
		return stopDateParse;
	}

	public void setStopDateParse(String stopDateParse) {
		this.stopDateParse = stopDateParse;
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

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<NursingOrderMapperDto> getListSavedPatient() {
		return listSavedPatient;
	}

	public void setListSavedPatient(List<NursingOrderMapperDto> listSavedPatient) {
		this.listSavedPatient = listSavedPatient;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getPendingNurOrdAdministrationId() {
		return pendingNurOrdAdministrationId;
	}

	public void setPendingNurOrdAdministrationId(Integer pendingNurOrdAdministrationId) {
		this.pendingNurOrdAdministrationId = pendingNurOrdAdministrationId;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
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

	

	
}
