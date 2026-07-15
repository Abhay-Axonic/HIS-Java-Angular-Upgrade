package com.param.global.otims.dto;

public class UomDto {

	private String uomType;
	private String uomUnit;
	private String uomDetails;
	private Integer uomTypeId;
	private Integer uomUnitId;
	private Integer conversion;
	private Boolean ipUom;
	private Boolean opUom;
	private Boolean storeUom;
	
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
	public String getUomDetails() {
		return uomDetails;
	}
	public void setUomDetails(String uomDetails) {
		this.uomDetails = uomDetails;
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
