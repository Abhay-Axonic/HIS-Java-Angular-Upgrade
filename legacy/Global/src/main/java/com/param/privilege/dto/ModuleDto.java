package com.param.privilege.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ModuleDto {

	private Integer moduleId;
	
	private String moduleName;
	
	private String actionLink;
	
	private Integer parentId;
	
	private Integer moduleType;
	
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getModuleType() {
		return moduleType;
	}
	public void setModuleType(Integer moduleType) {
		this.moduleType = moduleType;
	}
	public String getActionLink() {
		return actionLink;
	}
	public void setActionLink(String actionLink) {
		this.actionLink = actionLink;
	}
	
	
}
