package com.param.pharmacy.master.unit.dto.fixedAsset;

import com.param.service.global.model.CookieObjectDto;

public class FixedAssetResponse extends CookieObjectDto {

	protected Integer id;

	protected Integer assetTypeId;

	protected String assetType;

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer productCategoryId;

	protected String productCategory;

	protected Integer manufactureId;

	protected String manufactureName;

	protected String itemCode;

	protected String productCode;

	protected String name;

	protected Boolean status;

	protected Boolean isCssd;

	protected Boolean isModality;

	protected Boolean isChargeableEqupiment;
	protected Boolean isAccessories;

	protected Boolean isSpares;
	protected Boolean isDialyser;
	protected Boolean isCritical ;

	public Boolean getIsDialyser() {
		return isDialyser;
	}

	public Boolean getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Boolean isCritical) {
		this.isCritical = isCritical;
	}

	public void setIsDialyser(Boolean isDialyser) {
		this.isDialyser = isDialyser;
	}

	public Boolean getIsAccessories() {
		return isAccessories;
	}

	public void setIsAccessories(Boolean isAccessories) {
		this.isAccessories = isAccessories;
	}

	public Boolean getIsSpares() {
		return isSpares;
	}

	public void setIsSpares(Boolean isSpares) {
		this.isSpares = isSpares;
	}

	public FixedAssetResponse() {
		super();
	}

	public FixedAssetResponse(Integer id, Integer assetTypeId, String assetType, Integer assetCategoryId,
			String assetCategory, Integer productCategoryId, String productCategory, Integer manufactureId,
			String manufactureName, String itemCode, String productCode, String name, Boolean status) {
		super();
		this.id = id;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.manufactureId = manufactureId;
		this.manufactureName = manufactureName;
		this.itemCode = itemCode;
		this.productCode = productCode;
		this.name = name;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
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

	public Integer getManufactureId() {
		return manufactureId;
	}

	public void setManufactureId(Integer manufactureId) {
		this.manufactureId = manufactureId;
	}

	public String getManufactureName() {
		return manufactureName;
	}

	public void setManufactureName(String manufactureType) {
		this.manufactureName = manufactureType;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getIsCssd() {
		return isCssd;
	}

	public void setIsCssd(Boolean isCssd) {
		this.isCssd = isCssd;
	}

	public Boolean getIsModality() {
		return isModality;
	}

	public void setIsModality(Boolean isModality) {
		this.isModality = isModality;
	}

	public Boolean getIsChargeableEqupiment() {
		return isChargeableEqupiment;
	}

	public void setIsChargeableEqupiment(Boolean isChargeableEqupiment) {
		this.isChargeableEqupiment = isChargeableEqupiment;
	}

}
