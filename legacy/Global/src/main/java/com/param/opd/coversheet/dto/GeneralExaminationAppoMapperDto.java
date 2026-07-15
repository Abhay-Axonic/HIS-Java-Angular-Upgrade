package com.param.opd.coversheet.dto;

import java.util.List;


public class GeneralExaminationAppoMapperDto {

	private Integer generalExamAppoMapperId;
	private Integer systemId;
	private Integer systemObervationId;
	private Integer systemObervationPropertyId;
	private char isNADValue;
	private String remark;
	private Character status;
	private Integer appointmentId;
	private Integer patientId;
	private Integer unitId;
	private Integer organizationId;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String updatedDate;
	private Integer typeId;
	private Integer genderId;
	private Integer encounterId;
	private Integer admissionId;
	private Integer observationId;

	private String systemName;
	private String fromDate;
	private String toDate;

	private String observationName;
	private String propertyName;
	private String descriptiveExamination;
	private List<ListOfGeneralExaminationAppoMapperDto> listOfGeneralExaminationAppoMapperDto; 
	private String systemCode;
	private String observationCode;
	private List<GeneralExaminationAppoMapperDto> generalExaminationAppoMapperList;
	private List<ListOfGeneralExamSystemMaster> listOfListOfGeneralExamSystemMaster;
	private Character isPropertyRequired;
	private String remarkObs;

	private Integer propertyId;

	private List<GeneralExaminationAppoMapperDto> systemObervationMasterList;

	private String encounterDateString;
	private String encounterNumber;
	private String doctorName;
	private String specialityName;

	private String specialityId;
	private String createdDateString;
	
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getObservationName() {
		return observationName;
	}
	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public List<ListOfGeneralExamSystemMaster> getListOfListOfGeneralExamSystemMaster() {
		return listOfListOfGeneralExamSystemMaster;
	}
	public void setListOfListOfGeneralExamSystemMaster(
			List<ListOfGeneralExamSystemMaster> listOfListOfGeneralExamSystemMaster) {
		this.listOfListOfGeneralExamSystemMaster = listOfListOfGeneralExamSystemMaster;
	}
	public List<ListOfGeneralExaminationAppoMapperDto> getListOfGeneralExaminationAppoMapperDto() {
		return listOfGeneralExaminationAppoMapperDto;
	}
	public void setListOfGeneralExaminationAppoMapperDto(
			List<ListOfGeneralExaminationAppoMapperDto> listOfGeneralExaminationAppoMapperDto) {
		this.listOfGeneralExaminationAppoMapperDto = listOfGeneralExaminationAppoMapperDto;
	}
	
	
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Integer getGeneralExamAppoMapperId() {
		return generalExamAppoMapperId;
	}
	public void setGeneralExamAppoMapperId(Integer generalExamAppoMapperId) {
		this.generalExamAppoMapperId = generalExamAppoMapperId;
	}
	
	public char getIsNADValue() {
		return isNADValue;
	}
	public void setIsNADValue(char isNADValue) {
		this.isNADValue = isNADValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
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
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getSystemId() {
		return systemId;
	}
	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}
	public Integer getSystemObervationId() {
		return systemObervationId;
	}
	public void setSystemObervationId(Integer systemObervationId) {
		this.systemObervationId = systemObervationId;
	}
	public Integer getSystemObervationPropertyId() {
		return systemObervationPropertyId;
	}
	public void setSystemObervationPropertyId(Integer systemObervationPropertyId) {
		this.systemObervationPropertyId = systemObervationPropertyId;
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
	public String getDescriptiveExamination() {
		return descriptiveExamination;
	}
	public void setDescriptiveExamination(String descriptiveExamination) {
		this.descriptiveExamination = descriptiveExamination;
	}
	/*public List<SystemObervationMaster> getSystemObervationMasterList() {
		return systemObervationMasterList;
	}
	public void setSystemObervationMasterList(List<SystemObervationMaster> systemObervationMasterList) {
		this.systemObervationMasterList = systemObervationMasterList;
	}*/
	public Integer getObservationId() {
		return observationId;
	}
	public void setObservationId(Integer observationId) {
		this.observationId = observationId;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getObservationCode() {
		return observationCode;
	}
	public void setObservationCode(String observationCode) {
		this.observationCode = observationCode;
	}
	public List<GeneralExaminationAppoMapperDto> getGeneralExaminationAppoMapperList() {
		return generalExaminationAppoMapperList;
	}
	public void setGeneralExaminationAppoMapperList(
			List<GeneralExaminationAppoMapperDto> generalExaminationAppoMapperList) {
		this.generalExaminationAppoMapperList = generalExaminationAppoMapperList;
	}
	public List<GeneralExaminationAppoMapperDto> getSystemObervationMasterList() {
		return systemObervationMasterList;
	}
	public void setSystemObervationMasterList(List<GeneralExaminationAppoMapperDto> systemObervationMasterList) {
		this.systemObervationMasterList = systemObervationMasterList;
	}
	public Integer getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}
	public Character getIsPropertyRequired() {
		return isPropertyRequired;
	}
	public void setIsPropertyRequired(Character isPropertyRequired) {
		this.isPropertyRequired = isPropertyRequired;
	}
	public String getRemarkObs() {
		return remarkObs;
	}
	public void setRemarkObs(String remarkObs) {
		this.remarkObs = remarkObs;
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
	public String getEncounterDateString() {
		return encounterDateString;
	}
	public void setEncounterDateString(String encounterDateString) {
		this.encounterDateString = encounterDateString;
	}
	public String getEncounterNumber() {
		return encounterNumber;
	}
	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
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

	public String getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(String specialityId) {
		this.specialityId = specialityId;
	}

	public String getCreatedDateString() {
		return createdDateString;
	}
	public void setCreatedDateString(String createdDateString) {
		this.createdDateString = createdDateString;
	}
	
	
}
