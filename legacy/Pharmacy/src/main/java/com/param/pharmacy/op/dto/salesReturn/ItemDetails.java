package com.param.pharmacy.op.dto.salesReturn;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.UomResponse;

public class ItemDetails {

	public ItemDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Long id;

	private Integer itemId;

	private String itemName;

	private String genericName;

	private String batchNo;

	private String expiryDate;

	private String uomType;

	private Integer uomTypeId;

	private Integer quantityIssued;

	private Double rate;

	private Double totalAmount;

	private Double taxPercentage;

	private Double discount;

	private Double taxAmount;

	private Double unitDiscount;

	private Double netAmount;

	private Integer quantityReturned;
	
	private Long batchId;

	private List<UomResponse> uom;
	
	private Date batchExpiryDate;
	
	private Integer saleDetailsId;
	
	private boolean isThreeUOMApplicable;
	
	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
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

	public Double getUnitDiscount() {
		return unitDiscount;
	}

	public void setUnitDiscount(Double unitDiscount) {
		this.unitDiscount = unitDiscount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getQuantityReturned() {
		return quantityReturned;
	}

	public void setQuantityReturned(Integer quantityReturned) {
		this.quantityReturned = quantityReturned;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public ItemDetails(Long id, Integer itemId, String itemName, String genericName, String batchNo, String expiryDate,
			String uomType, Integer uomTypeId, Integer quantityIssued, Double rate, Double totalAmount, Double discount,
			Double taxAmount, Double unitDiscount, Double netAmount, Integer quantityReturned, List<UomResponse> uom,
			Double taxPercentage) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.uomType = uomType;
		this.uomTypeId = uomTypeId;
		this.quantityIssued = quantityIssued;
		this.rate = rate;
		this.totalAmount = totalAmount;
		this.discount = discount;
		this.taxAmount = taxAmount;
		this.unitDiscount = unitDiscount;
		this.netAmount = netAmount;
		this.quantityReturned = quantityReturned;
		this.uom = uom;
		this.taxPercentage = taxPercentage;
	}

	public Date getBatchExpiryDate() {
		return batchExpiryDate;
	}

	public void setBatchExpiryDate(Date batchExpiryDate) {
		this.batchExpiryDate = batchExpiryDate;
	}

	public Integer getSaleDetailsId() {
		return saleDetailsId;
	}

	public void setSaleDetailsId(Integer saleDetailsId) {
		this.saleDetailsId = saleDetailsId;
	}

}