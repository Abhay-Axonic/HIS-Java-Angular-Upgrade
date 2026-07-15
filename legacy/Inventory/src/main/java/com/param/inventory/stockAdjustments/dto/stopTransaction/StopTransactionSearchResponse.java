package com.param.inventory.stockAdjustments.dto.stopTransaction;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.UomResponse;

public class StopTransactionSearchResponse {

	protected Integer itemId;

	protected Long batchId;
	
	protected String batchNo;
	
	protected String itemCode;

	protected String itemName;

	protected Double cop;

	protected Double mrp;

	protected Integer avilableQuantity; 

	//protected Long avilableQuantity; 
	
	protected Date expiryDate;

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer productCategoryId;

	protected String productCategory;

	protected Integer uomUnitId;

	protected String uomUnit;

	protected Integer uomTypeId;

	protected String uomType;

	protected Double totalMrp;
	
	protected Integer storeId;
	
    private List<UomResponse> uom;

    public List<UomResponse> getUom() {
        return uom;
    }

    public void setUom(List<UomResponse> uom) {
        this.uom = uom;
    }
	
	private boolean isThreeUOMApplicable;
	
	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
		
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public Integer getAvilableQuantity() {
		return avilableQuantity;
	}

	public void setAvilableQuantity(Integer avilableQuantity) {
		this.avilableQuantity = avilableQuantity;
	}
	
//	public Long getAvilableQuantity() {
//		return avilableQuantity;
//	}
//
//	public void setAvilableQuantity(Long avilableQuantity) {
//		this.avilableQuantity = avilableQuantity;
//	}


	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Double getTotalMrp() {
		return totalMrp;
	}

	public void setTotalMrp(Double totalMrp) {
		this.totalMrp = totalMrp;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	
}



