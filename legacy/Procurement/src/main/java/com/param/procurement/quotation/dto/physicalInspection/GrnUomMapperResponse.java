package com.param.procurement.quotation.dto.physicalInspection;

public class GrnUomMapperResponse
{
	private Integer id;
	
	private Integer uomTypeId;
	
	private Integer uomUnitId;
	
	private Integer conversions;
	
	private boolean ipUom;
	
	private boolean opUom;
	
	private boolean stockUom;

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

	public Integer getConversions() {
		return conversions;
	}

	public void setConversions(Integer conversions) {
		this.conversions = conversions;
	}

	public boolean isIpUom() {
		return ipUom;
	}

	public void setIpUom(boolean ipUom) {
		this.ipUom = ipUom;
	}

	public boolean isOpUom() {
		return opUom;
	}

	public void setOpUom(boolean opUom) {
		this.opUom = opUom;
	}

	public boolean isStockUom() {
		return stockUom;
	}

	public void setStockUom(boolean stockUom) {
		this.stockUom = stockUom;
	}

	public GrnUomMapperResponse(Integer id, Integer uomTypeId, Integer uomUnitId, Integer conversions, boolean ipUom,
			boolean opUom, boolean stockUom) {
		super();
		this.id = id;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.stockUom = stockUom;
	}
	
}