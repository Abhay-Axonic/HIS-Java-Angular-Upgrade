package com.param.pharmacy.master.unit.dto.implantItemMaster;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ImplantItemMasterRequest extends ImplantItemMasterResponse {
	
	@JsonInclude(Include.NON_NULL)
	protected Integer id; 
	
	private Integer productCategoryId;
	
	private Integer storageUnitId;
	
	private Integer manufactureId;
	
	private Integer sTaxId;
	
	private Integer pTaxId;
	
	private String itemCode;
	
	private String productCode;
	
	private String itemName;
	
	private String specialityName;
	
	private Boolean isConsignment;
	
	public Boolean getIsBatchRequired() {
		return isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}
	private Boolean isSterilize;
	private Boolean isBatchRequired;
	
	private Boolean isImplant;
	
	protected Boolean status;
	
	protected List<ImplantItemMasterRequestItems> specialeety;
	
	public ImplantItemMasterRequest () {
		super();
	}

	public ImplantItemMasterRequest(Integer id, String specialityName)
	{
		super();
		this.id = id;
		this.specialityName=specialityName;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getStorageUnitId() {
		return storageUnitId;
	}

	public void setStorageUnitId(Integer storageUnitId) {
		this.storageUnitId = storageUnitId;
	}

	public Integer getManufactureId() {
		return manufactureId;
	}

	public void setManufactureId(Integer manufactureId) {
		this.manufactureId = manufactureId;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsSterilize() {
		return isSterilize;
	}

	public void setIsSterilize(Boolean isSterilize) {
		this.isSterilize = isSterilize;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getsTaxId() {
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

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public List<ImplantItemMasterRequestItems> getSpecialeety() {
		return specialeety;
	}
	public void setSpecialeety(List<ImplantItemMasterRequestItems> specialeety) {
		this.specialeety = specialeety;
	}

	
}
