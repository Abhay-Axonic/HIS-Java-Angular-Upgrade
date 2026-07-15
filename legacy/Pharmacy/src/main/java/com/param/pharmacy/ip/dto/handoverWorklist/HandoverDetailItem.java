 package com.param.pharmacy.ip.dto.handoverWorklist;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class HandoverDetailItem
{
	private Long id;
	
	private String itemCode;
	
	private String itemName;
	
	private Integer itemId;
	
	private Long prescriptionDetailId;
	
	public HandoverDetailItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String manufacturerName;
	
	private Integer manufacturerId;
	
	private boolean handOverFlag;
	
	private String uomUnit;
	
	private Integer uomUnitId;
	
	private Integer quantityOrdered;
	
	private Double amount;
	
	private String batchNo;
	
	private Date expiryDate;
	
	private Integer quantityIssued;
	
	private Double rate;
	
	private Double totalAmount;
	
	private Double discount;
	
	private Date startDate;
	
	private Date stopDate;
	
	private String frequency;
	
	private Double dose;
	
	private Integer statusId;
	   
	private String status;
	private Integer priorityId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public boolean isHandOverFlag() {
		return handOverFlag;
	}

	public void setHandOverFlag(boolean handOverFlag) {
		this.handOverFlag = handOverFlag;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Integer quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public Integer getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	public HandoverDetailItem(Long id, String itemCode, String itemName, Integer itemId, String manufacturerName,
			Integer manufacturerId, boolean handOverFlag, String uomUnit, Integer uomUnitId, Integer quantityOrdered,
			Double amount, String batchNo, Date expiryDate, Integer quantityIssued, Double rate, Double totalAmount,
			Double discount, Date startDate, Date stopDate, String frequency, Double dose) {
		super();
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemId = itemId;
		this.manufacturerName = manufacturerName;
		this.manufacturerId = manufacturerId;
		this.handOverFlag = handOverFlag;
		this.uomUnit = uomUnit;
		this.uomUnitId = uomUnitId;
		this.quantityOrdered = quantityOrdered;
		this.amount = amount;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantityIssued = quantityIssued;
		this.rate = rate;
		this.totalAmount = totalAmount;
		this.discount = discount;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.frequency = frequency;
		this.dose = dose;
	}

	public Long getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Long prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	
}