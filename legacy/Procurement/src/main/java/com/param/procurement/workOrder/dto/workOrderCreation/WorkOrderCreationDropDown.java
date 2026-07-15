package com.param.procurement.workOrder.dto.workOrderCreation;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.ContractType;
import com.param.global.dto.global.RequestType;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

public class WorkOrderCreationDropDown {
	
	List<Store> store;
	List<Status> status;
	List<ContractType> contractType;
	List<RequestType> requestType;
	
	
	
	public WorkOrderCreationDropDown() {
		super();
	}
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
	public List<ContractType> getContractType() {
		return contractType;
	}
	public void setContractType(List<ContractType> contractType) {
		this.contractType = contractType;
	}
	public List<RequestType> getRequestType() {
		return requestType;
	}
	public void setRequestType(List<RequestType> requestType) {
		this.requestType = requestType;
	}
	
	public void addStore(Store store) {
		if (this.store == null)
			this.store = new ArrayList<Store>();

		this.store.add(store);
	}
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();

		this.status.add(status);
	}
	
	public void addContractType(ContractType contractType) {
		if (this.contractType == null)
			this.contractType = new ArrayList<ContractType>();

		this.contractType.add(contractType);
	}
	
	public void addRequestType(com.param.global.dto.global.RequestType requestType) {
		if (this.requestType == null)
			this.requestType = new ArrayList<RequestType>();

		this.requestType.add(requestType);
	}
	
	

}
