package com.param.pharmacy.master.unit.dto.kit;

public class GetKitMasterResponseListByName{
	
	protected Integer id;
	
	protected Integer uomUnitId;
	
	protected Integer uomTypeId;
	
	protected Integer itemId;
	
	protected String itemName;
	
	protected String itemCode;
	
	protected String genericName;
	
	protected String manufacturerName;
	
	protected String uomType;
	
	protected String uomUnit;
	
	protected Integer quantity;
	
	protected Boolean status;
	
	public GetKitMasterResponseListByName(Integer id,Integer itemId, String itemName, String itemCode, String genericName, String manufacturerName, Integer uomUnitId,
			String uomUnit, Integer uomTypeId, String uomType, Boolean status, Integer quantity)
	{
		super();
		this.id = id;
		this.itemId=itemId;
		this.itemName=itemName;
		this.itemCode=itemCode;
		this.genericName=genericName;
		this.manufacturerName=manufacturerName;
		this.uomUnitId=uomUnitId;
		this.uomUnit=uomUnit;
		this.uomTypeId=uomTypeId;
		this.uomType=uomType;
		this.status=status;
		this.quantity=quantity;
	}
	
	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
