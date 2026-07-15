 package com.param.pharmacy.nursing.narcoticsStore.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;

@JsonInclude(Include.NON_NULL)
public class ReturnEmptyAmpoulesItem
{
	 private Integer id;
	 
	 private Integer itemId;
	 
	 private String itemCode;
	 
     private String itemName;
     
     private Integer genericId;
     private Integer returedQuantity;
     private Integer acceptedQuantity;
     private Integer rejectedQuantity;
     public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	private BigInteger consumedQty;
     
     public Integer getReturedQuantity() {
		return returedQuantity;
	}

	public void setReturedQuantity(Integer returedQuantity) {
		this.returedQuantity = returedQuantity;
	}

	public BigInteger getConsumedQty() {
		return consumedQty;
	}

	public void setConsumedQty(BigInteger consumedQty) {
		this.consumedQty = consumedQty;
	}

	private String genericName;
     
     private String frequency;
     
     private Double dose;
     
     private Date startDate;
     
     private Date stopDate;
     
     private Integer statusId;
     
     private String status;
     
     private boolean interventionFlag;
     
     private Integer quantityOrdered;
     
     private Integer quantityIssued;
     
     private Integer manufacturerId;
     
     private String manufacturerName;
     
     private Long stockAvailable;
     
     private String rackNo;
     
     private List<BatchResponse> batch;
     
     private Long batchId;
     
     private String batchNo;
     
	 private Date expiry;
	 
	 private String uomType;
	 
	 private Integer uomTypeId;
	 
	 private Integer uomUnitId;
	 
	 private String uomUnit;
     
     private Integer taxId;
     
     private String tax;
     
     private Double taxPercentage;
     
     private Integer orderDetailId;
     
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
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

	public boolean isInterventionFlag() {
		return interventionFlag;
	}

	public void setInterventionFlag(boolean interventionFlag) {
		this.interventionFlag = interventionFlag;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Integer quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	
	public Integer getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	
	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
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

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
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

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public ReturnEmptyAmpoulesItem(Integer id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer quantityIssued, Long batchId, String batchNo, Date expiry, String uomType,
			String uomUnit) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.quantityIssued = quantityIssued;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

}