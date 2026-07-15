package com.param.global.unit.dto;

import java.util.Date;
import java.util.List;

import com.param.global.dto.global.VisitType;

public class NurseShiftAllocationDto {
	
	private Integer nurseShiftAllocationId;

	private String fromDate;

	private String toDate;

	private Integer wardId;

	private Integer nurseId;

	private Integer unitId;

	private Character status;
	
	private Integer createdBy;
	
	private Date createdDate;

	private Integer updatedBy;

	private Date updatedDate;

	private Integer orgId;
	
	private String nurseName;

	private List<NurseShiftAllocationDetailsDto> nurseShiftAllocationDetailsDto;

	private List<NurseShiftAllocationDetailsDto> shiftList;
	
	private List<NurseShiftAllocationDto> nurseShiftList;
	
	private List<NurseShiftAllocationDto> nurseBedList;
	
	private List<NurseShiftAllocationDto> nurseList;
	
	private Integer nurseShiftAllocationDetailsId;

	private String shiftDate;

	private Integer shiftId;

	private Integer bedId;

	private Character isBedAllocated;
	
	private Integer offset;
	
	private Integer  noOfRecordsPerPage;

	private String shiftName;
	
	private String bedName;
	
	private Integer bedNurseShiftAllocationId;
	
	private String wardName;

	public Integer getNurseShiftAllocationId() {
		return nurseShiftAllocationId;
	}

	public void setNurseShiftAllocationId(Integer nurseShiftAllocationId) {
		this.nurseShiftAllocationId = nurseShiftAllocationId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
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


	public Integer getNurseShiftAllocationDetailsId() {
		return nurseShiftAllocationDetailsId;
	}

	public void setNurseShiftAllocationDetailsId(Integer nurseShiftAllocationDetailsId) {
		this.nurseShiftAllocationDetailsId = nurseShiftAllocationDetailsId;
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

	
	public List<NurseShiftAllocationDetailsDto> getShiftList() {
		return shiftList;
	}

	public void setShiftList(List<NurseShiftAllocationDetailsDto> shiftList) {
		this.shiftList = shiftList;
	}
	public Character getIsBedAllocated() {
		return isBedAllocated;
	}

	public void setIsBedAllocated(Character isBedAllocated) {
		this.isBedAllocated = isBedAllocated;
	}

	public List<NurseShiftAllocationDetailsDto> getNurseShiftAllocationDetailsDto() {
		return nurseShiftAllocationDetailsDto;
	}


	public Integer getOffset() {
		return offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNurseShiftAllocationDetailsDto(List<NurseShiftAllocationDetailsDto> nurseShiftAllocationDetailsDto) {
		this.nurseShiftAllocationDetailsDto = nurseShiftAllocationDetailsDto;
	}


	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public List<NurseShiftAllocationDto> getNurseShiftList() {
		return nurseShiftList;
	}

	public List<NurseShiftAllocationDto> getNurseBedList() {
		return nurseBedList;
	}

	public List<NurseShiftAllocationDto> getNurseList() {
		return nurseList;
	}

	public void setNurseShiftList(List<NurseShiftAllocationDto> nurseShiftList) {
		this.nurseShiftList = nurseShiftList;
	}

	public void setNurseBedList(List<NurseShiftAllocationDto> nurseBedList) {
		this.nurseBedList = nurseBedList;
	}

	public void setNurseList(List<NurseShiftAllocationDto> nurseList) {
		this.nurseList = nurseList;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public String getShiftName() {
		return shiftName;
	}

	public String getBedName() {
		return bedName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public void setBedName(String bedName) {
		this.bedName = bedName;
	}

	public Integer getBedNurseShiftAllocationId() {
		return bedNurseShiftAllocationId;
	}

	public String getWardName() {
		return wardName;
	}

	public void setBedNurseShiftAllocationId(Integer bedNurseShiftAllocationId) {
		this.bedNurseShiftAllocationId = bedNurseShiftAllocationId;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}



	

}
