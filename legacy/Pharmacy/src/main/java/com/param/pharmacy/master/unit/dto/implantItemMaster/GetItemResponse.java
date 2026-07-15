package com.param.pharmacy.master.unit.dto.implantItemMaster;

import java.util.ArrayList;
import java.util.List;

public class GetItemResponse {
	
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
	
	//protected String pTaxName;
	
	//protected String sTaxName;

	protected Boolean status;
	
	protected Integer storageUnitId;
	
	protected String storageUnit;
	
	protected Boolean isConsignment;
	protected Boolean isBatchRequired;
	
	public Boolean getIsBatchRequired() {
		return isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	protected Boolean isSterilized;
	
	protected List<ImplantItemMasterRequestItems> speciality;	

	public GetItemResponse() {
		super();
	}
	
	public void addItems(ImplantItemMasterRequestItems item) {
		if (this.speciality == null)
			this.speciality = new ArrayList<ImplantItemMasterRequestItems>();
		
		this.speciality.add(item);
	}

	
	public GetItemResponse(Integer id, Integer assetCategoryId, String assetCategory, Integer productCategoryId, 
			String productCategory,Integer manufactureId, String manufactureName, String itemCode, String productCode, String name,
			Integer pTaxId, Integer sTaxId,	Integer storageUnitId, String storageUnit, Boolean status, Boolean isConsignment, Boolean isSterilized) 
	{
		super();
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
		this.storageUnitId=storageUnitId;
		this.storageUnit=storageUnit;
		this.status = status;
		this.isConsignment=isConsignment;
		this.isSterilized=isSterilized;
		
	}
	/*public GetItemResponse(Integer id, Integer assetCategoryId, String assetCategory, Integer productCategoryId, String productCategory,
			Integer manufactureId, String manufactureName, String itemCode, String productCode, String name, Integer pTaxId, Integer sTaxId,
			Integer storageUnitId, String storageUnit, Boolean status, Boolean isConsignment, Boolean isSterilized)
	{
		super(id,assetCategoryId, assetCategory,productCategoryId,productCategory,manufactureId,manufactureName,itemCode,productCode,
				name,pTaxId,sTaxId,storageUnitId,storageUnit,status,isConsignment,isSterilized);
	}*/
	public void add(GetItemResponse taxResponse)
	{
		
	}
	public Integer getId() {
		return id;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public Integer getManufactureId() {
		return manufactureId;
	}

	public String getManufactureName() {
		return manufactureName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public String getName() {
		return name;
	}

	public Integer getsTaxId() {
		return sTaxId;
	}

	public Integer getpTaxId() {
		return pTaxId;
	}

	/*public String getpTaxName() {
		return pTaxName;
	}

	public String getsTaxName() {
		return sTaxName;
	}

	public void setpTaxName(String pTaxName) {
		this.pTaxName = pTaxName;
	}

	public void setsTaxName(String sTaxName) {
		this.sTaxName = sTaxName;
	}*/
	public Boolean getStatus() {
		return status;
	}

	public Integer getStorageUnitId() {
		return storageUnitId;
	}

	public String getStorageUnit() {
		return storageUnit;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public void setManufactureId(Integer manufactureId) {
		this.manufactureId = manufactureId;
	}

	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setsTaxId(Integer sTaxId) {
		this.sTaxId = sTaxId;
	}

	public void setpTaxId(Integer pTaxId) {
		this.pTaxId = pTaxId;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void setStorageUnitId(Integer storageUnitId) {
		this.storageUnitId = storageUnitId;
	}

	public void setStorageUnit(String storageUnit) {
		this.storageUnit = storageUnit;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public void setIsSterilized(Boolean isSterilized) {
		this.isSterilized = isSterilized;
	}
	public Boolean getIsSterilized() {
		return isSterilized;
	}

	public List<ImplantItemMasterRequestItems> getSpeciality() {
		return speciality;
	}

	public void setSpeciality(List<ImplantItemMasterRequestItems> speciality) {
		this.speciality = speciality;
	}

}
