package com.param.global.dto.global;

import java.util.List;

public class InstructionMasterDto {

	private Integer instructionId;

	private String instructionCode;

	private String instructionDesc;

	private Character isMedication;

	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private Integer instuctionDeptSpecialityMapperId;
	
	private Integer deptSpecialityId;
	
	private String specialityName;
	
	private Integer  id;
	
	private Integer specialityId;
	
	private List<TranslatedInstructionMasterDto> addedLanguageList;

	private List<InstructionMasterDto> deptSpecilityId;
	private String searchInstruction;

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public String getInstructionCode() {
		return instructionCode;
	}

	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public Character getIsMedication() {
		return isMedication;
	}

	public void setIsMedication(Character isMedication) {
		this.isMedication = isMedication;
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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


	public Integer getInstuctionDeptSpecialityMapperId() {
		return instuctionDeptSpecialityMapperId;
	}

	public void setInstuctionDeptSpecialityMapperId(Integer instuctionDeptSpecialityMapperId) {
		this.instuctionDeptSpecialityMapperId = instuctionDeptSpecialityMapperId;
	}

	public Integer getDeptSpecialityId() {
		return deptSpecialityId;
	}

	public void setDeptSpecialityId(Integer deptSpecialityId) {
		this.deptSpecialityId = deptSpecialityId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public List<InstructionMasterDto> getDeptSpecilityId() {
		return deptSpecilityId;
	}

	public void setDeptSpecilityId(List<InstructionMasterDto> deptSpecilityId) {
		this.deptSpecilityId = deptSpecilityId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public List<TranslatedInstructionMasterDto> getAddedLanguageList() {
		return addedLanguageList;
	}

	public void setAddedLanguageList(List<TranslatedInstructionMasterDto> addedLanguageList) {
		this.addedLanguageList = addedLanguageList;
	}

	public String getSearchInstruction() {
		return searchInstruction;
	}

	public void setSearchInstruction(String searchInstruction) {
		this.searchInstruction = searchInstruction;
	}
	
	
}
