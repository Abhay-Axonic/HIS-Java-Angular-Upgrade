package com.param.adt.admission.dto;

import java.util.List;

public class BedMgtDto {

	private Integer wardId;
	
	private String wardName;
	
	private List<BedManagementDto> bedList;

	private Integer roomId;

	private String roomDesc;

	private List<BedManagementDto> roomList;

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public List<BedManagementDto> getBedList() {
		return bedList;
	}

	public void setBedList(List<BedManagementDto> bedList) {
		this.bedList = bedList;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public List<BedManagementDto> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<BedManagementDto> roomList) {
		this.roomList = roomList;
	}
}
