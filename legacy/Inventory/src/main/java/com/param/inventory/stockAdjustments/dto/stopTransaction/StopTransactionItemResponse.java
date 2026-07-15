package com.param.inventory.stockAdjustments.dto.stopTransaction;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StopTransactionItemResponse {

	protected Integer id;

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer productCategoryId;

	protected String productCategory;

	protected String itemCode;

	protected String itemName;

	protected String uomType;

	protected String uomUnit;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected String batchNo;

	protected Date expiryDate;

	protected Long availableStock;

	protected Double cop;

	protected Double mrp;
	
	protected Integer avavilableQuantity; 
	
	protected Long batchId;
	
	protected Integer itemId;

	public StopTransactionItemResponse() {
		super();
	}

	public StopTransactionItemResponse(Integer id, Integer productCategoryId, String productCategory, String itemCode,
			String itemName, Integer uomTypeId, Integer uomUnitId, Date expiryDate, String batchNo, Double mrp,
			Double cop) {
		this.id = id;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.expiryDate = expiryDate;
		this.batchNo = batchNo;
		this.mrp = mrp;
		this.cop = cop;
	}

	public StopTransactionItemResponse(Integer id, Integer assetCategoryId, String assetCategory,
			Integer productCategoryId, String productCategory, String itemCode, String itemName, Integer uomTypeId,
			Integer uomUnitId, Date expiryDate, String batchNo, Double mrp, Double cop,Integer avavilableQuantity,String uomUnit,String uomType,Long batchId,Integer itemId) {
		this.id = id;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.expiryDate = expiryDate;
		this.batchNo = batchNo;
		this.mrp = mrp;
		this.cop = cop;
		this.avavilableQuantity = avavilableQuantity;
		this.uomUnit = uomUnit;
		this.uomType = uomType;
		this.batchId = batchId;
		this.itemId = itemId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Long getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Long availableStock) {
		this.availableStock = availableStock;
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
	
	public Integer getAvilableQuantity() {
		return avavilableQuantity;
	}

	public void setAvailableStock(Integer avavilableQuantity) {
		this.avavilableQuantity = avavilableQuantity;
	}
	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
}
