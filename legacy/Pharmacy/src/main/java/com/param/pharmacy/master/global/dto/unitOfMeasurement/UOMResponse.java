package com.param.pharmacy.master.global.dto.unitOfMeasurement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UOMResponse {

	protected Integer id;
	
	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer conversion;

	protected Boolean ipUom;
	
	protected Boolean opUom;
	
	protected Boolean storeUom;

	protected Boolean status;
	

	
	public UOMResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UOMResponse(Integer uomTypeId, Integer uomUnitId, Integer conversion, Boolean ipUom, Boolean opUom,
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
	
}
/*
 * "uomType":1, "uomUnit":2, "conver":10, "ipUom": false, "opUom": false,
 * "store_uom": false
 */