package com.param.global.dto;

import java.util.List;

public class UserCounterMapperDto 
{
	private Integer userCounterMpprId;
	
	private Integer counterId;
	
	private Integer userId;
	
	private String employeeName;
	
	private String employeeCode;
	
	private String counterCode;
	
	private String counterDesc;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private char status;

	private String createdDate;

	private int createdBy;

	private int updatedBy;

	private String updatedDate;
	
	private String empName;
	
	private String name;

	private char isDeleted;
	
	private Integer orgId;
	
	private List<Integer> selectedCounterList;

	public Integer getUserCounterMpprId() {
		return userCounterMpprId;
	}

	public void setUserCounterMpprId(Integer userCounterMpprId) {
		this.userCounterMpprId = userCounterMpprId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;
	}

	public List<Integer> getSelectedCounterList() {
		return selectedCounterList;
	}

	public void setSelectedCounterList(List<Integer> selectedCounterList) {
		this.selectedCounterList = selectedCounterList;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public String getCounterDesc() {
		return counterDesc;
	}

	public void setCounterDesc(String counterDesc) {
		this.counterDesc = counterDesc;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
}
