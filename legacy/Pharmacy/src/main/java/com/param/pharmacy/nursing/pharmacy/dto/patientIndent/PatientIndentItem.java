package com.param.pharmacy.nursing.pharmacy.dto.patientIndent;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class PatientIndentItem
{
	private Integer id;
	
	private Integer itemId;
	
	private String itemCode;
	
	private String itemName;
	
	private String genericName;
	
	private String uomType;
	
	private String uomUnit;
	
	private Integer quantity;
	
	private Integer indentQuantity;
	
	private Integer statusId;
	
	private Integer uomTypeId;
	
	private Integer uomUnitId;
	
	private Integer pendingQuantity;

	private Integer genericId;
	
	private Integer stockAvailable;

	public Integer getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Integer stockAvailable) {
		this.stockAvailable = stockAvailable;
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	
	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}
	
	private List<UomResponse> uom;

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}
	
	private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	public PatientIndentItem(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			String uomType, String uomUnit, Integer quantity, Integer pendingQuantity, Integer genericId, List<UomResponse> uom, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.quantity = quantity;
		this.pendingQuantity = pendingQuantity;
		this.genericId = genericId;
		this.uom = uom;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public PatientIndentItem() {
		super();
	}
	
}