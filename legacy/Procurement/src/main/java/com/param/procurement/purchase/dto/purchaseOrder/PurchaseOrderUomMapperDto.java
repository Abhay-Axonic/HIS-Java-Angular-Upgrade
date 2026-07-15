package com.param.procurement.purchase.dto.purchaseOrder;

public class PurchaseOrderUomMapperDto { 
     
     
	private Integer id;
	
	private Integer uomTypeId;
	
	private String uomType;

	private Integer uomUnitId;
	
	private String uomUnit;

	private Integer conversion;

	private Boolean ipUom;

	private Boolean opUom;

	private Boolean storeUom;
	
	private Integer poDetailId;
	
	public PurchaseOrderUomMapperDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PurchaseOrderUomMapperDto(Integer id, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit,
			Integer conversion, Boolean ipUom, Boolean opUom, Boolean storeUom) {
		this.id = id;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
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

	public Integer getPoDetailId() {
		return poDetailId;
	}

	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}
	
	
	

}
