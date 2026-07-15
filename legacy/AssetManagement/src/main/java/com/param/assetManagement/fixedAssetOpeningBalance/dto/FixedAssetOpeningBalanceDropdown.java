package com.param.assetManagement.fixedAssetOpeningBalance.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;
@JsonInclude(Include.NON_NULL)
public class FixedAssetOpeningBalanceDropdown {
	
	private List<Store> store;
	
	private List<Status> status;
	
	private List<PMSFrequencies> PMSFrequencies;
	
	private List<WorkingConditions> workingConditions;

	public List<Store> getStore() {
		return store;
	}

	public void setStore(List<Store> store) {
		this.store = store;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

	public List<PMSFrequencies> getPMSFrequencies() {
		return PMSFrequencies;
	}

	public void setPMSFrequencies(List<PMSFrequencies> pMSFrequencies) {
		PMSFrequencies = pMSFrequencies;
	}
	
	public List<WorkingConditions> getWorkingConditions() {
		return workingConditions;
	}

	public void setWorkingConditions(List<WorkingConditions> workingConditions) {
		this.workingConditions = workingConditions;
	}

	public void addStore(Store store) {
		if (this.store == null)
			this.store = new ArrayList<Store>();
		this.store.add(store);
	}
	
	public void addPMSFrequency(PMSFrequencies PMSFrequencies) {
		if (this.PMSFrequencies == null)
			this.PMSFrequencies = new ArrayList<PMSFrequencies>();
		this.PMSFrequencies.add(PMSFrequencies);
	}
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();
		this.status.add(status);
	}
	
	public void addWorkingConditions(WorkingConditions workingConditions) {
		if (this.workingConditions == null)
			this.workingConditions = new ArrayList<WorkingConditions>();
		this.workingConditions.add(workingConditions);
	}
	
}