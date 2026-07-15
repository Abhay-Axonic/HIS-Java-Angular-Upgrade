package com.param.global.billing.dto;

import java.util.Date;

public class ItemDetailsResponse {

	protected Long id;

	protected Integer itemId;

	protected String itemName;

	protected String code;

	protected String genericName;
	
	protected Integer genericId;

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	protected String manufacturer;

	protected Long batchId;

	protected String batchCode;

	protected Date expiryDate;

	protected Integer uomId;

	protected String uomUnit;

	protected Integer quantity;

	protected Integer quantityPrescribed;

	protected Double rate;

	protected Double totalAmount;

	protected Double discount;

	protected Double taxAmount;

	protected Double netAmount;

	protected String taxName;

	protected Double taxPercent;

	protected Double creditPayable;

	protected Double selfPayable;

	protected Double packageConsumptionAmt;
	protected Double concession;
	protected Double discountPercentage;
	protected Integer uomTypeId;
	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}

	public ItemDetailsResponse() {
		super();
	}

	public ItemDetailsResponse(Long id, Integer itemId, String itemName, String genericName, String manufacturer,
			Long batchId, String batchCode, Date expiryDate, Integer uomId, String uomUnit, Integer quantity,
			Integer quantityPrescribed, Double rate, Double totalAmount, Double discount, Double taxAmount,
			Double netAmount, String taxName, Double taxPercent, Double creditPayable, Double selfPayable,Double packageConsumptionAmt) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.manufacturer = manufacturer;
		this.batchId = batchId;
		this.batchCode = batchCode;
		this.expiryDate = expiryDate;
		this.uomId = uomId;
		this.uomUnit = uomUnit;
		this.quantity = quantity;
		this.quantityPrescribed = quantityPrescribed;
		this.rate = rate;
		this.totalAmount = totalAmount;
		this.discount = discount;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.creditPayable = creditPayable;
		this.selfPayable = selfPayable;
		this.packageConsumptionAmt = packageConsumptionAmt;
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getQuantityPrescribed() {
		return quantityPrescribed;
	}

	public void setQuantityPrescribed(Integer quantityPrescribed) {
		this.quantityPrescribed = quantityPrescribed;
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

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public Double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(Double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

}