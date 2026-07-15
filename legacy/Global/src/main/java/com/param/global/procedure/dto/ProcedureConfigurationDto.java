package com.param.global.procedure.dto;

import java.util.List;

import com.param.global.otims.dto.ProcedureConfigurationChecklistDto;
import com.param.global.otims.dto.ProcedureConfigurationConsentDto;
import com.param.global.otims.dto.ProcedureConfigurationEquipmentDto;
import com.param.global.otims.dto.ProcedureConfigurationInventoryItemDto;
import com.param.global.otims.dto.ProcedureConfigurationNoteDto;
import com.param.global.otims.dto.ProcedureConfigurationPatientEducationDto;
import com.param.global.otims.dto.ProcedureConfigurationServiceDto;
import com.param.global.otims.dto.ProcedureConfigurationStaffDto;

public class ProcedureConfigurationDto {
	private Integer procedureConfigId;
	private String cptCode;
	private String cptDescription;
	private int duration;
	private Integer specialityId;
	private Integer serviceId;
	private Integer procedureTypeId;
	private Integer anesthesiaTypeId;
	private Integer operationTheatreTypeId;
	private char status;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private String procedureType;
	private String anesthesiaType;
	private String speciality;
	private String service;
	private String procedureTypeName;
	private String specialityName;
	private String theatreTypeName;
	
	private List<ProcedureConfigurationConsentDto> consentDetailList;
	
	private List<ProcedureConfigurationChecklistDto> checklistDetailList;
	
	private List<ProcedureConfigurationStaffDto> staffDetailList;
	
	private List<ProcedureConfigurationInventoryItemDto> inventoryItemList;
	
	private List<ProcedureConfigurationEquipmentDto> equipmentDetailList;
	
	private List<ProcedureConfigurationServiceDto> serviceList;
	
	private List<ProcedureConfigurationNoteDto> noteList;
	
	private ProcedureConfigurationPatientEducationDto  patientEduObj;
	
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
	public String getAnesthesiaType() {
		return anesthesiaType;
	}
	public void setAnesthesiaType(String anesthesiaType) {
		this.anesthesiaType = anesthesiaType;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}
	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}
	public String getCptCode() {
		return cptCode;
	}
	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}
	public String getCptDescription() {
		return cptDescription;
	}
	public void setCptDescription(String cptDescription) {
		this.cptDescription = cptDescription;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}
	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}
	public Integer getAnesthesiaTypeId() {
		return anesthesiaTypeId;
	}
	public void setAnesthesiaTypeId(Integer anesthesiaTypeId) {
		this.anesthesiaTypeId = anesthesiaTypeId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
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
	public String getProcedureTypeName() {
		return procedureTypeName;
	}
	public void setProcedureTypeName(String procedureTypeName) {
		this.procedureTypeName = procedureTypeName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public List<ProcedureConfigurationConsentDto> getConsentDetailList() {
		return consentDetailList;
	}
	public void setConsentDetailList(List<ProcedureConfigurationConsentDto> consentDetailList) {
		this.consentDetailList = consentDetailList;
	}
	public List<ProcedureConfigurationChecklistDto> getChecklistDetailList() {
		return checklistDetailList;
	}
	public void setChecklistDetailList(List<ProcedureConfigurationChecklistDto> checklistDetailList) {
		this.checklistDetailList = checklistDetailList;
	}
	public List<ProcedureConfigurationStaffDto> getStaffDetailList() {
		return staffDetailList;
	}
	public void setStaffDetailList(List<ProcedureConfigurationStaffDto> staffDetailList) {
		this.staffDetailList = staffDetailList;
	}
	public List<ProcedureConfigurationInventoryItemDto> getInventoryItemList() {
		return inventoryItemList;
	}
	public void setInventoryItemList(List<ProcedureConfigurationInventoryItemDto> inventoryItemList) {
		this.inventoryItemList = inventoryItemList;
	}
	public List<ProcedureConfigurationServiceDto> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<ProcedureConfigurationServiceDto> serviceList) {
		this.serviceList = serviceList;
	}
	public List<ProcedureConfigurationNoteDto> getNoteList() {
		return noteList;
	}
	public void setNoteList(List<ProcedureConfigurationNoteDto> noteList) {
		this.noteList = noteList;
	}
	public Integer getOperationTheatreTypeId() {
		return operationTheatreTypeId;
	}
	public void setOperationTheatreTypeId(Integer operationTheatreTypeId) {
		this.operationTheatreTypeId = operationTheatreTypeId;
	}
	public ProcedureConfigurationPatientEducationDto getPatientEduObj() {
		return patientEduObj;
	}
	public void setPatientEduObj(ProcedureConfigurationPatientEducationDto patientEduObj) {
		this.patientEduObj = patientEduObj;
	}
	public List<ProcedureConfigurationEquipmentDto> getEquipmentDetailList() {
		return equipmentDetailList;
	}
	public void setEquipmentDetailList(List<ProcedureConfigurationEquipmentDto> equipmentDetailList) {
		this.equipmentDetailList = equipmentDetailList;
	}
	public String getTheatreTypeName() {
		return theatreTypeName;
	}
	public void setTheatreTypeName(String theatreTypeName) {
		this.theatreTypeName = theatreTypeName;
	}
	
}