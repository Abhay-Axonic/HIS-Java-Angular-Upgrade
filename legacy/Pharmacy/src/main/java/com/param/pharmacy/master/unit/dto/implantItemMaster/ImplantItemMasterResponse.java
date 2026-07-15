package com.param.pharmacy.master.unit.dto.implantItemMaster;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ImplantItemMasterResponse extends CookieObjectDto{

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
	
	protected Integer sTaxId;
	
	protected Integer pTaxId;
	
	protected String pTaxName;
	
	protected String sTaxName;

	protected Boolean status;
	
	protected Integer storageUnitId;
	
	protected String storageUnit;
	
	protected Boolean isConsignment;
	public Boolean getIsBatchRequired() {
		return isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	protected Boolean isBatchRequired;
	
	protected Boolean isSterilized;
	
	protected List<ImplantItemMasterListResponse> speciality;
	
	//protected List<ImplantItemMasterRequestItems> items;
	
	
	public ImplantItemMasterResponse() {
		super();
	}
		
	public ImplantItemMasterResponse(Integer id) {
		super();
		this.id = id;
		}
	
	
	public ImplantItemMasterResponse(Integer id, Integer assetCategoryId, String assetCategory, Integer productCategoryId,String productCategory,
			Integer manufactureId,String manufactureName,	String itemCode, String productCode, String name, Integer sTaxId, 
			Integer pTaxId, String sTaxName, String pTaxName,Integer storageUnitId, String storageUnit, Boolean status,
			Boolean isConsignment, Boolean isSterilized)
	{		super();
		this.id = id;	
		this.assetCategoryId = assetCategoryId;	
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;		
		this.productCategory = productCategory;	
		this.manufactureId = manufactureId;
		this.manufactureName = manufactureName;		
		this.itemCode = itemCode;	
		this.productCode = productCode;	
		this.name = name;
		this.pTaxId=pTaxId;		
		this.sTaxId=sTaxId;	
		this.sTaxName=sTaxName;	
		this.pTaxName=pTaxName;
		this.storageUnitId=storageUnitId;
		this.storageUnit=storageUnit;	
		this.status = status;	
		this.isConsignment=isConsignment;
		this.isSterilized=isSterilized;
	}

	public void add(ImplantItemMasterResponse taxResponse)
	{
		
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

	public Integer gesTaxId() {
		return sTaxId;
	}

	public void setsTaxId(Integer sTaxId) {
		this.sTaxId = sTaxId;
	}

	public Integer getpTaxId() {
		return pTaxId;
	}

	public void setpTaxId(Integer pTaxId) {
		this.pTaxId = pTaxId;
	}

	public String getpTaxName() {
		return pTaxName;
	}

	public void setpTaxName(String pTaxName) {
		this.pTaxName = pTaxName;
	}

	public String getsTaxName() {
		return sTaxName;
	}

	public void setsTaxName(String sTaxName) {
		this.sTaxName = sTaxName;
	}

	public Integer getStorageUnitId() {
		return storageUnitId;
	}

	public void setStorageUnitId(Integer storageUnitId) {
		this.storageUnitId = storageUnitId;
	}

	public String getStorageUnit() {
		return storageUnit;
	}

	public void setStorageUnit(String storageUnit) {
		this.storageUnit = storageUnit;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsSterilized() {
		return isSterilized;
	}

	public void setIsSterilized(Boolean isSterilized) {
		this.isSterilized = isSterilized;
	}
	
	public void addItems(ImplantItemMasterListResponse item) {
		if (this.speciality == null)
			this.speciality = new ArrayList<ImplantItemMasterListResponse>();
		
		this.speciality.add(item);
	}

	public List<ImplantItemMasterListResponse> getSpeciality() {
		return speciality;
	}

	public void setSpeciality(List<ImplantItemMasterListResponse> speciality) {
		this.speciality = speciality;
	}

	public Integer getsTaxId() {
		return sTaxId;
	}

		}
