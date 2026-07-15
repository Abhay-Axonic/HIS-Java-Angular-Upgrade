package com.param.global.dto;

import java.math.BigInteger;

public class IssueAcceptanceDto {

	private Integer itemId;
	private String itmName;
	private String itemCode;
	private String uomType;
	private String uomUnit;
	private String storeName;
	private Double mrp;
	private Integer bookedQty;
	private String expiryDate;
	private String genericName;
	private Integer issuedQty;
	private BigInteger batchId;
	private Integer uomTypeId;
	private Integer uomUnitId;
	private String batchNo;
	private String uhidNumber;
	private Integer procedureOrderId;
	private Integer salesTaxId;
	private Integer purchaseTaxId;
	private Double purchaseTaxPercentage;
	private Double salesTaxPercentage;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItmName() {
		return itmName;
	}

	public void setItmName(String itmName) {
		this.itmName = itmName;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Integer getBookedQty() {
		return bookedQty;
	}

	public void setBookedQty(Integer bookedQty) {
		this.bookedQty = bookedQty;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getIssuedQty() {
		return issuedQty;
	}

	public void setIssuedQty(Integer issuedQty) {
		this.issuedQty = issuedQty;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
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

	public BigInteger getBatchId() {
		return batchId;
	}

	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
	}

	public Integer getSalesTaxId() {
		return salesTaxId;
	}

	public void setSalesTaxId(Integer salesTaxId) {
		this.salesTaxId = salesTaxId;
	}

	public Integer getPurchaseTaxId() {
		return purchaseTaxId;
	}

	public void setPurchaseTaxId(Integer purchaseTaxId) {
		this.purchaseTaxId = purchaseTaxId;
	}

	public Double getPurchaseTaxPercentage() {
		return purchaseTaxPercentage;
	}

	public void setPurchaseTaxPercentage(Double purchaseTaxPercentage) {
		this.purchaseTaxPercentage = purchaseTaxPercentage;
	}

	public Double getSalesTaxPercentage() {
		return salesTaxPercentage;
	}

	public void setSalesTaxPercentage(Double salesTaxPercentage) {
		this.salesTaxPercentage = salesTaxPercentage;
	}

}