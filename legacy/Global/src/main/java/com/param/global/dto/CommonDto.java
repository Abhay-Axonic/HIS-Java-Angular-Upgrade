package com.param.global.dto;

public class CommonDto
{
	private Integer id;
	private String name;
	private String code;
	private String label;
	private Character isDefault;
	private Integer organizationId;
	private Integer unitId;
	
	public CommonDto(){
		
	}
	public Character getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Character isDefault) {
		this.isDefault = isDefault;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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

	public CommonDto(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
