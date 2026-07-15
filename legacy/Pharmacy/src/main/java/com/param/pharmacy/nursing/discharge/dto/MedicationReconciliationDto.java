package com.param.pharmacy.nursing.discharge.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.UserNursingStationMapperDto;
import com.param.global.dto.global.Floor;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.VisitType;
import com.param.global.dto.global.Ward;

@JsonInclude(Include.NON_NULL)
public class MedicationReconciliationDto {

	private List<Floor> floor;
	
	private List<Ward> ward;
	
	private List<VisitType> visitType;
	
	private List<Status> status;
	
	private List<UserNursingStationMapperDto> nursingStations;

	public List<Ward> getWard() {
		return ward;
	}

	public void setWard(List<Ward> ward) {
		this.ward = ward;
	}

	public List<Floor> getFloor() {
		return floor;
	}

	public void setFloor(List<Floor> floor) {
		this.floor = floor;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}
	
	public List<VisitType> getVisitType() {
		return visitType;
	}

	public void setVisitType(List<VisitType> visitType) {
		this.visitType = visitType;
	}

	public void addWard(Ward ward) {
		if (this.ward == null)
			this.ward = new ArrayList<Ward>();
		this.ward.add(ward);
	}
	
	public void addFloor(Floor floor) {
		if (this.floor == null)
			this.floor = new ArrayList<Floor>();
		this.floor.add(floor);
	}
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();
		this.status.add(status);
	}
	
	public void addVisitType(VisitType visitType) {
		if (this.visitType == null)
			this.visitType = new ArrayList<VisitType>();
		this.visitType.add(visitType);
	}

	public List<UserNursingStationMapperDto> getNursingStations() {
		return nursingStations;
	}

	public void setNursingStations(List<UserNursingStationMapperDto> nursingStations) {
		this.nursingStations = nursingStations;
	}
	
	
}
