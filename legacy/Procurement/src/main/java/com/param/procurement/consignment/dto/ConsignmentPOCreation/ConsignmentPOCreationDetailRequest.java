package com.param.procurement.consignment.dto.ConsignmentPOCreation;

import java.util.List;

public class ConsignmentPOCreationDetailRequest {
	
	
	private Integer itemId;
	
	private Long batchId;
	
	private Integer id;

	private Integer poQuantity;
	
	private Double cop;
	
	private Double mrp;
	
	private Integer taxId;
	
	private Double taxPercentage;
	
	private Double totalAmount;
	
	private Double taxAmount;
	
	private Double netAmount;
	
	private String remark;
	
	private Integer uomUnitId;
	
	private Integer uomTypeId;
	
	private Integer statusId;
	
	private Integer status;
	
	private List<ConsignmentPOCreationPatientListRequest> issueList;
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ConsignmentPOCreationPatientListRequest> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<ConsignmentPOCreationPatientListRequest> issueList) {
		this.issueList = issueList;
	}

	
	

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public ConsignmentPOCreationDetailRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	

	public Integer getPoQuantity() {
		return poQuantity;
	}

	public void setPoQuantity(Integer poQuantity) {
		this.poQuantity = poQuantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	

}
