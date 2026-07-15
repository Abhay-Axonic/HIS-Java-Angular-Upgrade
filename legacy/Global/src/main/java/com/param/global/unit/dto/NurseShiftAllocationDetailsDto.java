package com.param.global.unit.dto;

import java.util.Date;
import java.util.List;

import com.param.global.dto.global.VisitType;

public class NurseShiftAllocationDetailsDto {
	
	private Integer nurseShiftAllocationDetailsId;

	private Integer nurseShiftAllocationId;

	private String shiftDate;

	private Integer shiftId;
	
	private Integer id;

	private Integer bedId;

	private Integer nurseId;

	private Integer unitId;

	private char status;
	
	private Integer createdBy;

	private Date createdDate;

	private Integer updatedBy;

	public List<NurseShiftAllocationDetailsDto> getBedIdList() {
		return bedIdList;
	}

	public void setBedIdList(List<NurseShiftAllocationDetailsDto> bedIdList) {
		this.bedIdList = bedIdList;
	}

	private Date updatedDate;

	private Integer orgId;
	
	private String label;
	
	private List<NurseShiftAllocationDetailsDto> selectedbedList;
	
	private List<NurseShiftAllocationDetailsDto> bedIdList;
	
	private char isBedAllocated;
	

	public Integer getNurseShiftAllocationDetailsId() {
		return nurseShiftAllocationDetailsId;
	}

	public void setNurseShiftAllocationDetailsId(Integer nurseShiftAllocationDetailsId) {
		this.nurseShiftAllocationDetailsId = nurseShiftAllocationDetailsId;
	}

	public Integer getNurseShiftAllocationId() {
		return nurseShiftAllocationId;
	}

	public void setNurseShiftAllocationId(Integer nurseShiftAllocationId) {
		this.nurseShiftAllocationId = nurseShiftAllocationId;
	}

	public String getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(String shiftDate) {
		this.shiftDate = shiftDate;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getNurseId() {
		return nurseId;
	}

	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
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
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public char getIsBedAllocated() {
		return isBedAllocated;
	}

	public void setIsBedAllocated(char isBedAllocated) {
		this.isBedAllocated = isBedAllocated;
	}

	@Override
	public String toString() {
		return "NurseShiftAllocationDetailsDto [nurseShiftAllocationDetailsId=" + nurseShiftAllocationDetailsId
				+ ", nurseShiftAllocationId=" + nurseShiftAllocationId + ", shiftDate=" + shiftDate + ", shiftId="
				+ shiftId + ", bedId=" + bedId + ", nurseId=" + nurseId + ", unitId=" + unitId + ", status=" + status
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", orgId=" + orgId + ", isBedAllocated=" + isBedAllocated + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<NurseShiftAllocationDetailsDto> getSelectedbedList() {
		return selectedbedList;
	}

	public void setSelectedbedList(List<NurseShiftAllocationDetailsDto> selectedbedList) {
		this.selectedbedList = selectedbedList;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
