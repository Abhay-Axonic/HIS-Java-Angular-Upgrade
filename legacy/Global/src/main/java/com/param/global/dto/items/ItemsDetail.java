package com.param.global.dto.items;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemsDetail implements Serializable{

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer productCategoryId;

	protected String productCategory;

	protected Integer genericId;

	protected String genericName;

	protected Integer manufacturerId;

	protected String manufacturerName;

	protected Integer taxId;

	protected String taxName;

	protected Double taxPercent;

	protected Boolean isOtc;

	protected Integer issueQuantity;
	
	protected String formulationType;
	protected Integer formulationTypeId;
	protected String strength;
    protected Long batchId;
    protected String prePrintedBarcode;
	protected Boolean isLasa;

	public ItemsDetail() {
		super();
	}

	public ItemsDetail(Integer itemId, String itemCode, String itemName, Integer genericId, String genericName,
			Integer manufacturerId, String manufacturerName, Integer taxId, String taxName, Double taxPercent,
			Boolean isOtc,Long batchId,String prePrintedBarcode) {
		super();
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.isOtc = isOtc;
		this.batchId = batchId;
		this.prePrintedBarcode = prePrintedBarcode;
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

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
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

	public Boolean getIsOtc() {
		return isOtc;
	}

	public void setIsOtc(Boolean isOtc) {
		this.isOtc = isOtc;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public String getFormulationType() {
		return formulationType;
	}

	public void setFormulationType(String formulationType) {
		this.formulationType = formulationType;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getPrePrintedBarcode() {
		return prePrintedBarcode;
	}

	public void setPrePrintedBarcode(String prePrintedBarcode) {
		this.prePrintedBarcode = prePrintedBarcode;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}
}
