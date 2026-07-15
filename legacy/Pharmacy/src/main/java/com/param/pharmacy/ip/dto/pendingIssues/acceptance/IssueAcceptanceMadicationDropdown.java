package com.param.pharmacy.ip.dto.pendingIssues.acceptance;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.global.Floor;
import com.param.global.dto.global.VisitType;
import com.param.global.dto.global.Ward;

@JsonInclude(Include.NON_NULL)
public class IssueAcceptanceMadicationDropdown
{
	private List<Ward> ward;
	
	private List<Floor> floor;

	private List<VisitType> visitType;
	
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

	public void addVisitType(VisitType visitType) {
		if (this.visitType == null)
			this.visitType = new ArrayList<VisitType>();
		this.visitType.add(visitType);
	}
}