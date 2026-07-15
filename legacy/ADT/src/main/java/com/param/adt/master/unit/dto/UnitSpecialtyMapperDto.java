package com.param.adt.master.unit.dto;

import java.util.List;

import javax.persistence.Column;

public class UnitSpecialtyMapperDto 
{
	private int unitSpecialityMapperId;
	
	private Integer unitId;
	
	private Integer specialityId;
	
	private Integer subSpecialityMasterId;
	
	private char status;
	
	private int createdBy;
	
	private String createdDate;

	private int updatedBy;

	private String updatedDate;
	
	private Integer organizationId;
	
	private String specialityName;
	
	private String subSpecialityMasterName;
	
	private String specialityCode;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private Integer doctorId;
	
	private String doctorName;
	

	private char isSurgicalCode;

	private char isClinicalSpeciality;

	private char isRenderingSpeciality;

	private char deptWiseAppointment;
	
	private Character isKioskDept;
	
	private Character isNursingAssessmentRequired;
	
	private Integer allowedOverbookingSlots;
	
	private Integer pddDays;
	
	private Integer userId;
	

	public char getIsSurgicalCode() {
		return isSurgicalCode;
	}

	public void setIsSurgicalCode(char isSurgicalCode) {
		this.isSurgicalCode = isSurgicalCode;
	}

	public char getIsClinicalSpeciality() {
		return isClinicalSpeciality;
	}

	public void setIsClinicalSpeciality(char isClinicalSpeciality) {
		this.isClinicalSpeciality = isClinicalSpeciality;
	}

	public char getIsRenderingSpeciality() {
		return isRenderingSpeciality;
	}

	public void setIsRenderingSpeciality(char isRenderingSpeciality) {
		this.isRenderingSpeciality = isRenderingSpeciality;
	}

	public char getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(char deptWiseAppointment) {
		this.deptWiseAppointment = deptWiseAppointment;
	}

	public Character getIsKioskDept() {
		return isKioskDept;
	}

	public void setIsKioskDept(Character isKioskDept) {
		this.isKioskDept = isKioskDept;
	}

	public Character getIsNursingAssessmentRequired() {
		return isNursingAssessmentRequired;
	}

	public void setIsNursingAssessmentRequired(Character isNursingAssessmentRequired) {
		this.isNursingAssessmentRequired = isNursingAssessmentRequired;
	}

	public Integer getAllowedOverbookingSlots() {
		return allowedOverbookingSlots;
	}

	public void setAllowedOverbookingSlots(Integer allowedOverbookingSlots) {
		this.allowedOverbookingSlots = allowedOverbookingSlots;
	}

	

	public Integer getPddDays() {
		return pddDays;
	}

	public void setPddDays(Integer pddDays) {
		this.pddDays = pddDays;
	}

	public Integer getSubSpecialityMasterId() {
		return subSpecialityMasterId;
	}

	public void setSubSpecialityMasterId(Integer subSpecialityMasterId) {
		this.subSpecialityMasterId = subSpecialityMasterId;
	}


	public String getSubSpecialityMasterName() {
		return subSpecialityMasterName;
	}

	public void setSubSpecialityMasterName(String subSpecialityMasterName) {
		this.subSpecialityMasterName = subSpecialityMasterName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	private List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList;

	
	
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

	public int getUnitSpecialityMapperId() {
		return unitSpecialityMapperId;
	}

	public void setUnitSpecialityMapperId(int unitSpecialityMapperId) {
		this.unitSpecialityMapperId = unitSpecialityMapperId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSpecialityCode() {
		return specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public List<UnitSpecialtyMapperDto> getUnitSpecialtyMapperDtosList() {
		return unitSpecialtyMapperDtosList;
	}

	public void setUnitSpecialtyMapperDtosList(List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList) {
		this.unitSpecialtyMapperDtosList = unitSpecialtyMapperDtosList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	
}
