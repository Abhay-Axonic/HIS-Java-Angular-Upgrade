package com.param.adt.master.unit.dto;

import java.util.List;

import com.param.global.dto.MultiSelectDto;

public class WardMasterDto 
{
	private int wardId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String wardCode;
	
	private Integer floorId;
	
	private String wardName;
	
	private char isEr;
	
	private Integer noOfRooms;
	
	private Integer genderId;
	
	private char isIcu;
	
	private char isDayCare;
	
	private char status;
	
	private int createdBy;
	
	private String createdDate;
	
	private int updatedBy;
	
	private String updatedDate;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private Integer storeId;
	
	private Integer nursingStationId;
	
	private String floorName;
	
	private String storeName;

	private String desc;
	
	private String roomDesc;
	
	private Integer roomId;
	
	private Integer erCategoryId;
	
	private Character islaborWard;
	private Integer icuTypeId;
	private String icuTypeName;
	
	private Character isIpWard;
	
	private Character ishemodialysisward;
	
	private List<MultiSelectDto> genderIdList;
	private List<WardMasterDto> wardMasterDtosList;
	
	
	
	public String getIcuTypeName() {
		return icuTypeName;
	}

	public void setIcuTypeName(String icuTypeName) {
		this.icuTypeName = icuTypeName;
	}

	public Integer getIcuTypeId() {
		return icuTypeId;
	}

	public void setIcuTypeId(Integer icuTypeId) {
		this.icuTypeId = icuTypeId;
	}

	public List<MultiSelectDto> getGenderIdList() {
		return genderIdList;
	}

	public void setGenderIdList(List<MultiSelectDto> genderIdList) {
		this.genderIdList = genderIdList;
	}

	public int getWardId() {
		return wardId;
	}

	public void setWardId(int wardId) {
		this.wardId = wardId;
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

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	
	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public char getIsEr() {
		return isEr;
	}

	public void setIsEr(char isEr) {
		this.isEr = isEr;
	}

	public Integer getNoOfRooms() {
		return noOfRooms;
	}

	public void setNoOfRooms(Integer noOfRooms) {
		this.noOfRooms = noOfRooms;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public char getIsIcu() {
		return isIcu;
	}

	public void setIsIcu(char isIcu) {
		this.isIcu = isIcu;
	}

	public char getIsDayCare() {
		return isDayCare;
	}

	public void setIsDayCare(char isDayCare) {
		this.isDayCare = isDayCare;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getErCategoryId() {
		return erCategoryId;
	}

	public void setErCategoryId(Integer erCategoryId) {
		this.erCategoryId = erCategoryId;
	}

	public List<WardMasterDto> getWardMasterDtosList() {
		return wardMasterDtosList;
	}

	public void setWardMasterDtosList(List<WardMasterDto> wardMasterDtosList) {
		this.wardMasterDtosList = wardMasterDtosList;
	}

	public Character getIslaborWard() {
		return islaborWard;
	}

	public void setIslaborWard(Character islaborWard) {
		this.islaborWard = islaborWard;
	}

	public Character getIshemodialysisward() {
		return ishemodialysisward;
	}

	public void setIshemodialysisward(Character ishemodialysisward) {
		this.ishemodialysisward = ishemodialysisward;
	}

	public Character getIsIpWard() {
		return isIpWard;
	}

	public void setIsIpWard(Character isIpWard) {
		this.isIpWard = isIpWard;
	}



	
	
}
