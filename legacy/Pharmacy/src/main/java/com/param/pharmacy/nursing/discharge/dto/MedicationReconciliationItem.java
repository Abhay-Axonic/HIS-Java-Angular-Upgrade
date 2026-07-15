package com.param.pharmacy.nursing.discharge.dto;

import java.math.BigInteger;
import java.util.Date;

public class MedicationReconciliationItem {

	 private Long id;
	 
	 private Integer itemId;
	 
     private String itemName;
     
     private Integer genericId;
     
     private String genericName;
     
     private String roaName;
     
     private Double dose;
     
     private String frequency;
     
     private Date startDate;
     
     private Date stopDate;
     
     private Integer orderedQty;
     
     private Long availableQty;
     private String strengthUnit;
     
     private Integer qtyToOrdered;
     private BigInteger totalIssuedQty;
     private BigInteger consumedQty;
     private Integer returnQty;
     private String batchNo;
     private Date expDate;
     private Integer issueQuantity;
     private Integer acceptedQty;
	
	public Integer getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

	public Integer getAcceptedQty() {
		return acceptedQty;
	}

	public void setAcceptedQty(Integer acceptedQty) {
		this.acceptedQty = acceptedQty;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public BigInteger getConsumedQty() {
		return consumedQty;
	}

	public void setConsumedQty(BigInteger consumedQty) {
		this.consumedQty = consumedQty;
	}

	public MedicationReconciliationItem(Long id, Integer itemId, String itemName, Integer genericId,
			String genericName, String roaName, Double dose, String frequency, Date startDate, Date stopDate,
			Integer orderedQty, Long availableQty, Integer qtyToOrdered, BigInteger totalIssuedQty, BigInteger consumedQty, Integer returnQty ,String strengthUnit) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.roaName = roaName;
		this.dose = dose;
		this.frequency = frequency;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.orderedQty = orderedQty;
		this.availableQty = availableQty;
		this.qtyToOrdered = qtyToOrdered;
		this.totalIssuedQty = totalIssuedQty;
		this.consumedQty = consumedQty;
		this.returnQty= returnQty;
		this.strengthUnit=strengthUnit;
	}
	public MedicationReconciliationItem(Long id, Integer itemId, String itemName, Integer genericId,
			String genericName, String roaName, Double dose, String frequency, Date startDate, Date stopDate,
			Integer orderedQty, Long availableQty, Integer qtyToOrdered, BigInteger totalIssuedQty, BigInteger consumedQty, String batchNo, Date expDate, Integer issueQuantity, Integer acceptedqty) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.roaName = roaName;
		this.dose = dose;
		this.frequency = frequency;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.orderedQty = orderedQty;
		this.availableQty = availableQty;
		this.qtyToOrdered = qtyToOrdered;
		this.totalIssuedQty = totalIssuedQty;
		this.consumedQty = consumedQty;
		this.batchNo = batchNo;
		this.expDate= expDate;
		this.issueQuantity=issueQuantity;
		this.acceptedQty= acceptedqty;
	}
	public BigInteger getTotalIssuedQty() {
		return totalIssuedQty;
	}

	public void setTotalIssuedQty(BigInteger totalIssuedQty) {
		this.totalIssuedQty = totalIssuedQty;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getRoaName() {
		return roaName;
	}

	public void setRoaName(String roaName) {
		this.roaName = roaName;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
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

	public Integer getOrderedQty() {
		return orderedQty;
	}

	public void setOrderedQty(Integer orderedQty) {
		this.orderedQty = orderedQty;
	}

	public Long getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Long availableQty) {
		this.availableQty = availableQty;
	}

	public Integer getQtyToOrdered() {
		return qtyToOrdered;
	}

	public void setQtyToOrdered(Integer qtyToOrdered) {
		this.qtyToOrdered = qtyToOrdered;
	}

	public String getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(String strengthUnit) {
		this.strengthUnit = strengthUnit;
	}
     
     
}
