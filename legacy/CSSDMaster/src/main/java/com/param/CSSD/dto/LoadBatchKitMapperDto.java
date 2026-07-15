package com.param.CSSD.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSD.model.LoadBatch;
import com.param.CSSD.model.PreSterilizationProcess;

@JsonInclude(Include.NON_NULL)
public class LoadBatchKitMapperDto {

	private Integer id;

	private Integer batchNumber;
	
	private LoadBatch  loadBatch;
	
	private Integer transitionNumber;
	
	private PreSterilizationProcess  preSterilizationProcess;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	public LoadBatch getLoadBatch() {
		return loadBatch;
	}

	public void setLoadBatch(LoadBatch loadBatch) {
		this.loadBatch = loadBatch;
	}

	public Integer getTransitionNumber() {
		return transitionNumber;
	}

	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}

	public PreSterilizationProcess getPreSterilizationProcess() {
		return preSterilizationProcess;
	}

	public void setPreSterilizationProcess(PreSterilizationProcess preSterilizationProcess) {
		this.preSterilizationProcess = preSterilizationProcess;
	}
	
	
}
