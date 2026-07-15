package com.param.adt.master.global.dto;

import java.util.List;

import com.param.global.dto.SpecialityMasterDto;

public class ICPCCodesConfigurationMapperDto {
	
	private Integer icpcCodeConfigurationMpprId;
	
	private Integer icpcSystemId;

	private Integer specialityId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private String systemName;
	
	private String specialityName;
	
	private Integer icpcConfigurationId ;
	private Integer icpcCodeConfigurationId ;
	private List<ICPCCodesConfigurationMapperDto> listOfSpecialityId ;
	
	public Integer getIcpcConfigurationId() {
		return icpcConfigurationId;
	}

	public void setIcpcConfigurationId(Integer icpcConfigurationId) {
		this.icpcConfigurationId = icpcConfigurationId;
	}

	private List<Integer> specialityIdList;
	
	private List<SpecialityMasterDto> specialityNameList;
	
	private List<Integer> icpcCodeConfigurationMpprIds;

	public Integer getIcpcCodeConfigurationMpprId() {
		return icpcCodeConfigurationMpprId;
	}

	public void setIcpcCodeConfigurationMpprId(Integer icpcCodeConfigurationMpprId) {
		this.icpcCodeConfigurationMpprId = icpcCodeConfigurationMpprId;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public List<Integer> getSpecialityIdList() {
		return specialityIdList;
	}

	public void setSpecialityIdList(List<Integer> specialityIdList) {
		this.specialityIdList = specialityIdList;
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

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public List<SpecialityMasterDto> getSpecialityNameList() {
		return specialityNameList;
	}

	public void setSpecialityNameList(List<SpecialityMasterDto> specialityNameList) {
		this.specialityNameList = specialityNameList;
	}

	public List<Integer> getIcpcCodeConfigurationMpprIds() {
		return icpcCodeConfigurationMpprIds;
	}

	public void setIcpcCodeConfigurationMpprIds(List<Integer> icpcCodeConfigurationMpprIds) {
		this.icpcCodeConfigurationMpprIds = icpcCodeConfigurationMpprIds;
	}

	public Integer getIcpcCodeConfigurationId() {
		return icpcCodeConfigurationId;
	}

	public void setIcpcCodeConfigurationId(Integer icpcCodeConfigurationId) {
		this.icpcCodeConfigurationId = icpcCodeConfigurationId;
	}

	public List<ICPCCodesConfigurationMapperDto> getListOfSpecialityId() {
		return listOfSpecialityId;
	}

	public void setListOfSpecialityId(List<ICPCCodesConfigurationMapperDto> listOfSpecialityId) {
		this.listOfSpecialityId = listOfSpecialityId;
	}

	
	
	
}
