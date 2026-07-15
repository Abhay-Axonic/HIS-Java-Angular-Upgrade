package com.param.procurement.loanInOut.dto;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.UomResponse;

public class LoanInOutRequestItem {
	
	private Integer id;

	private Integer loanInDetailId;
	
	private Integer loanOutDetailId;

	private Integer itemId;

	private Long batchId;

	private Integer uomTypeId;

	private Integer uomUnitId;

	private String batchNo;

	private Date expiryDate;

	private Integer quantity;

	private Integer returnQuantity;

	private Integer receiveQuantity;

	private Integer loanOutQuantity;

	private Double cop;

	private Double totalAmount;

	private String remark;

	private String specification;

	private Integer statusId;

	private Integer approvedQuantity;

	private List<UomResponse> uom;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLoanInDetailId() {
		return loanInDetailId;
	}

	public void setLoanInDetailId(Integer loanInDetailId) {
		this.loanInDetailId = loanInDetailId;
	}

	public Integer getLoanOutDetailId() {
		return loanOutDetailId;
	}

	public void setLoanOutDetailId(Integer loanOutDetailId) {
		this.loanOutDetailId = loanOutDetailId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getReceiveQuantity() {
		return receiveQuantity;
	}

	public void setReceiveQuantity(Integer receiveQuantity) {
		this.receiveQuantity = receiveQuantity;
	}

	public Integer getLoanOutQuantity() {
		return loanOutQuantity;
	}

	public void setLoanOutQuantity(Integer loanOutQuantity) {
		this.loanOutQuantity = loanOutQuantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

}