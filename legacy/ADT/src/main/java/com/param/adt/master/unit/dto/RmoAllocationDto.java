package com.param.adt.master.unit.dto;

import java.util.List;

public class RmoAllocationDto {
	
	private Integer id;
	
	private Integer rmoAllocationId;
	
	private Integer visitTypeId;
	
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

    private Integer rmoAllocationMapperId;
	
	private Integer rmoDoctorId; 
	
	private String rmoDoctorName;
	
	private List<RmoAllocationDto> doctorList;
	
	private List<RmoAllocationDto> selectedWardList;
	
	private String label;
	
	private List<Integer> doctorIdList;
	
	public Integer getRmoAllocationId() {
		return rmoAllocationId;
	}

	public void setRmoAllocationId(Integer rmoAllocationId) {
		this.rmoAllocationId = rmoAllocationId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
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

	public void setCabinDesc(String cabinDesc) {
		this.cabinDesc = cabinDesc;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getNursingStationDesc() {
		return nursingStationDesc;
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

	public Integer getRmoAllocationMapperId() {
		return rmoAllocationMapperId;
	}

	public void setRmoAllocationMapperId(Integer rmoAllocationMapperId) {
		this.rmoAllocationMapperId = rmoAllocationMapperId;
	}

	public Integer getRmoDoctorId() {
		return rmoDoctorId;
	}

	public void setRmoDoctorId(Integer rmoDoctorId) {
		this.rmoDoctorId = rmoDoctorId;
	}

	public List<RmoAllocationDto> getDoctorList() {
		return doctorList;
	}

	public void setDoctorList(List<RmoAllocationDto> doctorList) {
		this.doctorList = doctorList;
	}

	public String getRmoDoctorName() {
		return rmoDoctorName;
	}

	public void setRmoDoctorName(String rmoDoctorName) {
		this.rmoDoctorName = rmoDoctorName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<RmoAllocationDto> getSelectedWardList() {
		return selectedWardList;
	}

	public void setSelectedWardList(List<RmoAllocationDto> selectedWardList) {
		this.selectedWardList = selectedWardList;
	}

	public List<Integer> getDoctorIdList() {
		return doctorIdList;
	}

	public void setDoctorIdList(List<Integer> doctorIdList) {
		this.doctorIdList = doctorIdList;
	}


}
