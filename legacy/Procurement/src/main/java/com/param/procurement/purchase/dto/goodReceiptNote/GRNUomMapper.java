package com.param.procurement.purchase.dto.goodReceiptNote;

public class GRNUomMapper {
	
	 protected Integer id;
	
	 protected Integer uomTypeId;
	 
	 protected String uomType;
	
	 protected Integer uomUnitId;
	 
	 protected String uomUnit;
	
	 protected Integer conversion;

	 protected Boolean ipUom;

	 protected Boolean opUom;

	 protected Boolean storeUom;
	 
	 protected Integer grnDetailId;
	 
	 public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public GRNUomMapper() {
		super();
	}
	 
	 public GRNUomMapper(Integer id, Integer uomTypeId,String uomType, Integer uomUnitId, String uomUnit,Integer conversion, Boolean ipUom,
			Boolean opUom, Boolean storeUom) {
		super();
		this.id = id;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit =uomUnit;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}



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
	 
	 

}
