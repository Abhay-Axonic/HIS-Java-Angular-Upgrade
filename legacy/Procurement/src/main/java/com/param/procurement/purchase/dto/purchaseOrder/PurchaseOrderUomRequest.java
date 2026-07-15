package com.param.procurement.purchase.dto.purchaseOrder;

public class PurchaseOrderUomRequest {
	
	 Integer id;
	
	 Integer uomTypeId;
	
	 Integer uomUnitId;
	
	 Integer conversion;

	 Boolean ipUom;

	 Boolean opUom;

	 Boolean storeUom;
	 
	 
	 
/*
	public PurchaseOrderUomRequest(Integer uomTypeId, Integer uomUnitId, Integer conversion, Boolean ipUom,
			Boolean opUom, Boolean storeUom) {

		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}
*/
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
