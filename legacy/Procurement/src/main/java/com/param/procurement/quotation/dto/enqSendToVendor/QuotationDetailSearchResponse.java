package com.param.procurement.quotation.dto.enqSendToVendor;

public class QuotationDetailSearchResponse {
	
	private Integer id;

	private Integer itemId;
	
	private String itemCode;
	
	private String itemName;
	
	private Integer uomTypeId;
	
	private String uomType;
	
	private Integer uomUnitId;
	
	private String uomUnit;
	
	private Integer qrQuantity;

	public QuotationDetailSearchResponse(Integer id, Integer itemId, String itemCode, String itemName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer qrQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.qrQuantity = qrQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
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

	public Integer getQrQuantity() {
		return qrQuantity;
	}

	public void setQrQuantity(Integer qrQuantity) {
		this.qrQuantity = qrQuantity;
	}
	
}
