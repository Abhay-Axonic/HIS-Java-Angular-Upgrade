package com.param.procurement.consignment.dto.consignment;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class ConsignmentRequestItems {

	protected Integer id;

	protected Integer itemId;
	
	protected Long batchId;
	
	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected String batchNo;

	protected Date expiryDate;
	
	protected Date expiry;

	protected Integer quantity;

	protected Double cop;
	protected Double lcop;

	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	protected Double totalAmount;

	protected Double totalDiscount;

	protected Double discountPercentage;

	protected Double totalTaxAmount;
	
	protected Double totalNetAmount;
	
	protected Double taxAmount;

	protected Double netAmount;

	protected Integer taxId;

	protected Double taxPercentage;

	protected Integer statusId;
	
	protected String remark;

	protected List<UomResponse> uom;

	public ConsignmentRequestItems() {
		super();
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

	public Integer getUomTypeId() {
		return uomTypeId;
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

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public ConsignmentRequestItems(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String batchNo,
			Date expiryDate, Integer quantity, Double cop, Double totalAmount, Double totalDiscount,
			Double discountPercentage, Double totalTaxAmount, Double totalNetAmount, Integer taxId,
			Double taxPercentage) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.discountPercentage = discountPercentage;
		this.totalTaxAmount = totalTaxAmount;
		this.totalNetAmount = totalNetAmount;
		this.taxId = taxId;
		this.taxPercentage = taxPercentage;
	}
	
	public ConsignmentRequestItems(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String batchNo,
			Date expiryDate, Integer quantity, Double cop, Double totalDiscount,
			Double discountPercentage, Double totalTaxAmount, Double totalNetAmount, Integer taxId,
			Double taxPercentage) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.discountPercentage = discountPercentage;
		this.totalTaxAmount = totalTaxAmount;
		this.totalNetAmount = totalNetAmount;
		this.taxId = taxId;
		this.taxPercentage = taxPercentage;
	}

		public ConsignmentRequestItems(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, Long batchId,
			String batchNo, Date expiry, Integer quantity, double cop,double totalAmount, Integer taxId,
			double taxPercentage, double taxAmount, double netAmount, Long availableQuantity)
		{
			super();
			this.id = id;
			this.itemId = itemId;
			this.uomTypeId=uomTypeId;
			this.uomUnitId=uomUnitId;
			this.batchId = batchId;
			this.batchNo = batchNo;
			this.expiry = expiry;
			this.quantity = quantity;
			this.cop = cop;
			this.totalAmount = totalAmount;
			this.taxId = taxId;
			this.taxPercentage = taxPercentage;
			this.taxAmount = taxAmount;
			this.netAmount = netAmount;
				
		}

		public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
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

}
