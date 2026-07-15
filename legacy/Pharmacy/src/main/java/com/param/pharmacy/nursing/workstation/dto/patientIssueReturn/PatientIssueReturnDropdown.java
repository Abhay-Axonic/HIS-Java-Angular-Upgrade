package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.ec2.model.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.global.Store;
import com.param.global.dto.global.VisitType;

@JsonInclude(Include.NON_NULL)
public class PatientIssueReturnDropdown
{
	private List<VisitType> visitType;
	
	private List<Store> store;
	
	private List<Status> status;
	
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

	public List<Store> getStore() {
		return store;
	}

	public void setStore(List<Store> store) {
		this.store = store;
	}
	
	public void addVisitType(VisitType visitType) {
		if (this.visitType == null)
			this.visitType = new ArrayList<VisitType>();
		this.visitType.add(visitType);
	}
	
	public void addStore(Store store) {
		if (this.store == null)
			this.store = new ArrayList<Store>();
		this.store.add(store);
	}

	public void addStatus(com.param.global.dto.global.Status status) {
		// TODO Auto-generated method stub
		
	}
	
}