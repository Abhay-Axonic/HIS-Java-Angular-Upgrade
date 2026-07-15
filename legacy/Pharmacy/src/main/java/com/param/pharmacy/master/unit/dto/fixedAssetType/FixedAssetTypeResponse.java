package com.param.pharmacy.master.unit.dto.fixedAssetType;

import com.param.service.global.model.CookieObjectDto;

public abstract class FixedAssetTypeResponse extends CookieObjectDto {

	protected Integer id;

	protected String fixedAssetCode;

	protected String fixedAssetType;

	protected Boolean status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFixedAssetCode() {
		return fixedAssetCode;
	}

	public void setFixedAssetCode(String fixedAssetCode) {
		this.fixedAssetCode = fixedAssetCode;
	}

	public String getFixedAssetType() {
		return fixedAssetType;
	}

	public void setFixedAssetType(String fixedAssetType) {
		this.fixedAssetType = fixedAssetType;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}

/*
 * "id": 1, "fixedAssetCode": "AC1", "fixedAssetType":"Type1", "status": true,
 */