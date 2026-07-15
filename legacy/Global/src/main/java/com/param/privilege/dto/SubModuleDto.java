package com.param.privilege.dto;

public class SubModuleDto {
	
	private Integer subModuleId;
	
	private String subModuleName;
	
	private Integer parentId;

	public Integer getSubModuleId() {
		return subModuleId;
	}

	public void setSubModuleId(Integer subModuleId) {
		this.subModuleId = subModuleId;
	}

	public String getSubModuleName() {
		return subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	

}
