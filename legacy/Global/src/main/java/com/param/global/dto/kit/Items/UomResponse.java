package com.param.global.dto.kit.Items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UomResponse {

	protected Integer id;

	protected String uomType;

	protected Integer uomId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected String uomUnit;

	protected String uomDetails;

	protected Integer conversion;

	protected Boolean ipUom;

	protected Boolean opUom;

	protected Boolean storeUom;
	
	protected Integer index;

	public UomResponse() {

	}

	public UomResponse(Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, String uomDetails,
			Integer conversion, Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.uomDetails = uomDetails;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	public UomResponse(Integer id, String uomType, Integer uomTypeId, Integer uomUnitId, String uomUnit,
			Integer conversion, Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.id = id;
		this.uomType = uomType;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	public UomResponse(String uomType, Integer uomTypeId, Integer uomUnitId, String uomUnit, Integer conversion,
			Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.uomType = uomType;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
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

	public UomResponse(Integer id, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit,
			String uomDetails, Integer conversion, Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.id = id;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.uomDetails = uomDetails;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

/*	public UomResponse(Integer opbUomMapperId, String uomType, Integer uomTypeId, Integer uomUnitId, String uomUnit, Integer conversion,
			Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.opbUomMapperId= opbUomMapperId;
		this.uomType = uomType;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}*/
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
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

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
}
