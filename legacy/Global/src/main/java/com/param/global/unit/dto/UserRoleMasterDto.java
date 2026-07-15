package com.param.global.unit.dto;

import java.util.List;



public class UserRoleMasterDto {
	
	private int roleId;

	private String roleName;
	
	private Character status;
	
	private Integer organisationId;

	private Integer unitId;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private char isCounterApplicable;
	
	private Integer roleTypeId;
	
	private String roleType;
	
	private Integer doctorTypeId;

	private String doctorType;
	
	private Integer dashboardId;
	
	private String dashboadSate;
	
	private List<Integer> roleIdList;
	
	private String dashboardDesc;
	
	private Character prophylacticVaccinationApplication;
	
	private List<Integer> vaccination;
	
	private List<VaccinationRoleMapperDto> vaccinations;

	
	


	public Character getProphylacticVaccinationApplication() {
		return prophylacticVaccinationApplication;
	}

	public void setProphylacticVaccinationApplication(Character prophylacticVaccinationApplication) {
		this.prophylacticVaccinationApplication = prophylacticVaccinationApplication;
	}

	public String getDashboardDesc() {
		return dashboardDesc;
	}

	public void setDashboardDesc(String dashboardDesc) {
		this.dashboardDesc = dashboardDesc;
	}
	
	public String getDashboadSate() {
		return dashboadSate;
	}

	public void setDashboadSate(String dashboadSate) {
		this.dashboadSate = dashboadSate;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
	public char getIsCounterApplicable() {
		return isCounterApplicable;
	}

	public void setIsCounterApplicable(char isCounterApplicable) {
		this.isCounterApplicable = (isCounterApplicable == '\u0000') ? 'N' : isCounterApplicable;
	}

	public Integer getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(Integer roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public Integer getDoctorTypeId() {
		return doctorTypeId;
	}

	public void setDoctorTypeId(Integer doctorTypeId) {
		this.doctorTypeId = doctorTypeId;
	}

	public String getDoctorType() {
		return doctorType;
	}

	public void setDoctorType(String doctorType) {
		this.doctorType = doctorType;
	}

	public Integer getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(Integer dashboardId) {
		this.dashboardId = dashboardId;
	}

	public List<Integer> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Integer> roleIdList) {
		this.roleIdList = roleIdList;
	}
	
	public List<Integer> getVaccination() {
		return vaccination;
	}

	public void setVaccination(List<Integer> vaccination) {
		this.vaccination = vaccination;
	}

	public List<VaccinationRoleMapperDto> getVaccinations() {
		return vaccinations;
	}

	public void setVaccinations(List<VaccinationRoleMapperDto> vaccinations) {
		this.vaccinations = vaccinations;
	}

}
