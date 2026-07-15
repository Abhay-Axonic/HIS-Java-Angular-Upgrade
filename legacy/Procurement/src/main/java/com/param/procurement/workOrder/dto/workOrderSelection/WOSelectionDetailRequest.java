package com.param.procurement.workOrder.dto.workOrderSelection;

import java.util.List;

public class WOSelectionDetailRequest {
	
	private Integer id;

	private Integer quotationRequestDetailId;

	private Integer statusId;

	private List<WOSelectionMapperRequest> mapper;

	public WOSelectionDetailRequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuotationRequestDetailId() {
		return quotationRequestDetailId;
	}

	public void setQuotationRequestDetailId(Integer quotationRequestDetailId) {
		this.quotationRequestDetailId = quotationRequestDetailId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<WOSelectionMapperRequest> getMapper() {
		return mapper;
	}

	public void setMapper(List<WOSelectionMapperRequest> mapper) {
		this.mapper = mapper;
	}
	
}
