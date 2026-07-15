package com.param.inventory.stock.dto.openingBalance;

public class UomResponse {

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer conversion;

	protected Boolean ipUom;

	protected Boolean opUom;

	protected Boolean storeUom;

	public UomResponse() {
		super();
	}

	public UomResponse(Integer uomTypeId, Integer uomUnitId, Integer conversion, Boolean ipUom, Boolean opUom,
			Boolean storeUom) {
		super();
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
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

	public Integer getConversion() {
		return conversion;
	}

	public void setConversion(Integer conversion) {
		this.conversion = conversion;
	}

	public Boolean getIpUom() {
		return ipUom;
	}

	public void setIpUom(Boolean ipUom) {
		this.ipUom = ipUom;
	}

	public Boolean getOpUom() {
		return opUom;
	}

	public void setOpUom(Boolean opUom) {
		this.opUom = opUom;
	}

	public Boolean getStoreUom() {
		return storeUom;
	}

	public void setStoreUom(Boolean storeUom) {
		this.storeUom = storeUom;
	}

}
/*
 * "uomTypeId": 1, "uomUnitId": 1, "conversion": 10, "ipUom": false, "opUom":
 * false, "storeUom": false
 */
