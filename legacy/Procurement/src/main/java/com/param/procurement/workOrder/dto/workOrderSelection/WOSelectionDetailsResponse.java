package com.param.procurement.workOrder.dto.workOrderSelection;

import java.util.List;

public class WOSelectionDetailsResponse {
	
	private Integer id;

	//private Integer quotationRequestDetailId;

	private Integer statusId;
	
	private String status;
	
	private Integer itemId;

	private String itemCode;

	private String itemName;

	private String specification;
	
	private String inclusionDetails;
	
	private String exclusionDetails;
	
	private String paymentTerms;
	
	private Long batchId;
	
	private String batchNo;

	private List<WOSelectionMapperResponse> mapper;

	public WOSelectionDetailsResponse() {
		super();
	}

	public WOSelectionDetailsResponse(Integer id, Integer itemId,String itemCode,String itemName,String specification,Integer statusId, String status,List<WOSelectionMapperResponse> mapper) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.specification = specification;
		this.statusId = statusId;
		this.status = status;
		this.mapper = mapper;
	}
	public String getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(String inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public String getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(String exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public WOSelectionDetailsResponse(Integer id, Integer itemId,String itemCode,String itemName,String specification,Integer statusId, String status,List<WOSelectionMapperResponse> mapper,
			Long batchId,String batchNo,String inclusionDetails,String exclusionDetails,String paymentTerms) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.specification = specification;
		this.statusId = statusId;
		this.status = status;
		this.mapper = mapper;
		this.inclusionDetails=inclusionDetails;
		this.exclusionDetails=exclusionDetails;
		this.paymentTerms=paymentTerms;
		this.batchId=batchId;
		this.batchNo=batchNo;
		
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<WOSelectionMapperResponse> getMapper() {
		return mapper;
	}

	public void setMapper(List<WOSelectionMapperResponse> mapper) {
		this.mapper = mapper;
	}
	
	
	

}
