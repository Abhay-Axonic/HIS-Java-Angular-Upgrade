package com.param.inventory.store.dto.storeIndent;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class ItemResponse extends ItemRequest {

	protected String itemCode;

	protected String itemName;

	protected String genericName;

	protected String manufacturer;

	protected String uomType;

	protected String uomUnit;

	protected Long stockAvailable;

	protected Long stockInTransit;

	protected Long lastMonthSale;

	protected Long currentMonthSale;

	protected Long maxOrderQuantity;

	/* Material return Fields */

	protected String batchNo;

	protected Long batchId;

	protected Date expiryDate;

	protected Integer returnQuantity;
	
	private boolean isThreeUOMApplicable;
	
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	protected String returnReason;
	protected List<UomResponse> uom;

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public ItemResponse() {
		super();
	}

	public ItemResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer quantity,
			Integer statusId, String itemCode, String itemName, String genericName, String manufacturer, String uomType,
			String uomUnit, Long stockAvailable, Long stockInTransit, Long lastMonthSale, Long currentMonthSale,
			Long maxOrderQuantity, List<UomResponse> uom, boolean isThreeUOMApplicable) {
		super(id, itemId, uomTypeId, uomUnitId, quantity, statusId);
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.manufacturer = manufacturer;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.stockAvailable = stockAvailable;
		this.stockInTransit = stockInTransit;
		this.lastMonthSale = lastMonthSale;
		this.currentMonthSale = currentMonthSale;
		this.maxOrderQuantity = maxOrderQuantity;
		this.uom=uom;
        this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public ItemResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String itemCode,
			String itemName, String genericName, String uomType, String uomUnit, Long stockAvailable, String batchNo,
			Date expiryDate, Integer returnQuantity, String returnReason) {
		super(id, itemId, uomTypeId, uomUnitId);
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.stockAvailable = stockAvailable;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
	}

	public ItemResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String itemCode, 
			String itemName, String uomType, String uomUnit, Long stockAvailable,
			String batchNo, Date expiryDate) {
		super(id, itemId, uomTypeId, uomUnitId);
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.stockAvailable = stockAvailable;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
	}
	
	public ItemResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String itemCode, 
			String itemName, String genericName, String uomType, String uomUnit,
			String batchNo) {
		super(id, itemId, uomTypeId, uomUnitId);
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Long getStockInTransit() {
		return stockInTransit;
	}

	public void setStockInTransit(Long stockInTransit) {
		this.stockInTransit = stockInTransit;
	}

	public Long getLastMonthSale() {
		return lastMonthSale;
	}

	public void setLastMonthSale(Long lastMonthSale) {
		this.lastMonthSale = lastMonthSale;
	}

	public Long getCurrentMonthSale() {
		return currentMonthSale;
	}

	public void setCurrentMonthSale(Long currentMonthSale) {
		this.currentMonthSale = currentMonthSale;
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

}