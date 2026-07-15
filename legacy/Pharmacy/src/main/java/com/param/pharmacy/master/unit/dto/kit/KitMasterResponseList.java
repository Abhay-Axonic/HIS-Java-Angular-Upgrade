package com.param.pharmacy.master.unit.dto.kit;

import java.math.BigInteger;

public class KitMasterResponseList {
	
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
	
	protected BigInteger availableQty;
	protected Boolean isLasa;
	public KitMasterResponseList()
	{
		super();
	}

	public KitMasterResponseList(Integer id,Integer itemId, String itemName, String itemCode, String genericName, String manufacturerName, Integer uomUnitId,
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
	/*public void addItems(KitItemMasterResponse item) {
		if (this.itemCode == null)
			this.itemCode = new ArrayList<KitItemMasterResponse>();
		
		this.itemCode.add(item);
	}
*/
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigInteger getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(BigInteger availableQty) {
		this.availableQty = availableQty;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}
}
