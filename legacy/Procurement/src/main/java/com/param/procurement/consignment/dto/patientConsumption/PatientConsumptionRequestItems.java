package com.param.procurement.consignment.dto.patientConsumption;

import java.util.List;

import com.param.global.dto.items.BatchResponse;

public class PatientConsumptionRequestItems {

	private Integer id;
	
	private Integer implantIndentDetailId;

	private List<BatchResponse> batchList;

	private Integer statusId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<BatchResponse> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<BatchResponse> batchList) {
		this.batchList = batchList;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getImplantIndentDetailId() {
		return implantIndentDetailId;
	}

	public void setImplantIndentDetailId(Integer implantIndentDetailId) {
		this.implantIndentDetailId = implantIndentDetailId;
	}

}
