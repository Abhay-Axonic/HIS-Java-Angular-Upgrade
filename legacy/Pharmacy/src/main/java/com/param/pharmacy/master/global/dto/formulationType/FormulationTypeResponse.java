package com.param.pharmacy.master.global.dto.formulationType;

import com.param.service.global.model.CookieObjectDto;

public abstract class FormulationTypeResponse extends CookieObjectDto{

	protected Integer id;

/*	protected String assetType;

	protected String assetCategory;

	protected String productCategory;*/

	protected String code;

	protected String type;

	protected Boolean status;
	
	protected String unit;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}*/

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}

/*
 * "id":1, "assetType": "Fixed", "assetCategory": "Pharmacy", "productCategory":
 * "Medication", "code": "FORM-1002", "type": "Formulation Type", "status": true
 */