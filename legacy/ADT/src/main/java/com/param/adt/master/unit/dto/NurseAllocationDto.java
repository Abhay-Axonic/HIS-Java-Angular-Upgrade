package com.param.adt.master.unit.dto;

public class NurseAllocationDto {
	
	private Integer nurseAllocationId;
	
	private Integer visitTypeId;
	
	private Integer roleId; 

	private Integer userId; 
	
	private Integer specialityId; 
	
	private Integer doctorId; 
	
	private Integer wardId; 
	
	private Integer nursingStationId;
	
   private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer  offset;
	
	private String specialityName;
	
	private Integer cabinId;
	
	private String doctorName;
	
	private String cabinDesc;
	
	private String wardName;
	
	private String nursingStationDesc;
	
	private String roleName;
	
	private String nurseName;

	public Integer getNurseAllocationId() {
		return nurseAllocationId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNurseAllocationId(Integer nurseAllocationId) {
		this.nurseAllocationId = nurseAllocationId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getCabinDesc() {
		return cabinDesc;
	}

	public String getWardName() {
		return wardName;
	}

	public String getNursingStationDesc() {
		return nursingStationDesc;
	}

	public void setCabinDesc(String cabinDesc) {
		this.cabinDesc = cabinDesc;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public void setNursingStationDesc(String nursingStationDesc) {
		this.nursingStationDesc = nursingStationDesc;
	}

	public String getRoleName() {
		return roleName;
	}

	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	@Override
	public String toString() {
		return "NurseAllocationDto [nurseAllocationId=" + nurseAllocationId + ", visitTypeId=" + visitTypeId
				+ ", roleId=" + roleId + ", userId=" + userId + ", specialityId=" + specialityId + ", doctorId="
				+ doctorId + ", wardId=" + wardId + ", nursingStationId=" + nursingStationId + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", status=" + status + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", unitId=" + unitId + ", organizationId=" + organizationId
				+ ", noOfRecordsPerPage=" + noOfRecordsPerPage + ", offset=" + offset + ", specialityName="
				+ specialityName + ", cabinId=" + cabinId + ", doctorName=" + doctorName + ", cabinDesc=" + cabinDesc
				+ ", wardName=" + wardName + ", nursingStationDesc=" + nursingStationDesc + ", roleName=" + roleName
				+ ", nurseName=" + nurseName + ", getNurseAllocationId()=" + getNurseAllocationId()
				+ ", getVisitTypeId()=" + getVisitTypeId() + ", getRoleId()=" + getRoleId() + ", getUserId()="
				+ getUserId() + ", getSpecialityId()=" + getSpecialityId() + ", getDoctorId()=" + getDoctorId()
				+ ", getWardId()=" + getWardId() + ", getNursingStationId()=" + getNursingStationId()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getCreatedDate()=" + getCreatedDate() + ", getStatus()="
				+ getStatus() + ", getUpdatedBy()=" + getUpdatedBy() + ", getUpdatedDate()=" + getUpdatedDate()
				+ ", getUnitId()=" + getUnitId() + ", getOrganizationId()=" + getOrganizationId()
				+ ", getNoOfRecordsPerPage()=" + getNoOfRecordsPerPage() + ", getOffset()=" + getOffset()
				+ ", getSpecialityName()=" + getSpecialityName() + ", getCabinId()=" + getCabinId()
				+ ", getDoctorName()=" + getDoctorName() + ", getCabinDesc()=" + getCabinDesc() + ", getWardName()="
				+ getWardName() + ", getNursingStationDesc()=" + getNursingStationDesc() + ", getRoleName()="
				+ getRoleName() + ", getNurseName()=" + getNurseName() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

	
	
	
}
