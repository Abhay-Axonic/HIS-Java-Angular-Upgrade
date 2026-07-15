package com.param.pharmacy.op.dto.salesWorkList;

import java.util.ArrayList;
import java.util.List;

import com.param.entity.model.master.ScreenStatus;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.VisitType;

public class SalesWorkListDropdowns {
	
	private List<VisitType> visitType;
	
	private List<Status> status;

	public List<VisitType> getVisitType() {
		return visitType;
	}

	public void setVisitType(List<VisitType> visitType) {
		this.visitType = visitType;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
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
}

