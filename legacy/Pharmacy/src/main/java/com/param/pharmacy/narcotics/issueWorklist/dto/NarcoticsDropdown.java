package com.param.pharmacy.narcotics.issueWorklist.dto;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.Status;
import com.param.global.dto.global.VisitType;
import com.param.global.dto.global.Ward;

public class NarcoticsDropdown
{
	private List<VisitType> visitType;
	
	private List<Ward> ward;
	
	private List<Status> status;

	public List<VisitType> getVisitType() {
		return visitType;
	}

	public void setVisitType(List<VisitType> visitType) {
		this.visitType = visitType;
	}

	public List<Ward> getWard() {
		return ward;
	}

	public void setWard(List<Ward> ward) {
		this.ward = ward;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}
	
	public void addVisitType(VisitType visitType) {
		if (this.visitType == null)
			this.visitType = new ArrayList<VisitType>();
		this.visitType.add(visitType);
	}
	
	public void addWard(Ward ward) {
		if (this.ward == null)
			this.ward = new ArrayList<Ward>();
		this.ward.add(ward);
	}
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();
		this.status.add(status);
	}
	
}