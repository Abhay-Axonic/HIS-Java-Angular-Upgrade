
package com.param.inventory.store.dto.storeStock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class SubstituteStoreStockDashboardDto extends CookieObjectDto{
	private String itemName;
	
	private String storeName;
	
	private String manufactureName;
	
	private String batchNo;

	private String expDate;

	private Integer currentStock;

	private String uom;
	
	private String uomUnit;
	private String uomunit;

	private Double cop;
	
	private Double mrp;
	
	private String location;

	private String shelf;
	
	private Integer storeId;

	private Integer id;
	private Integer itemId;
	
	private Integer genericId;
	private Integer unitId;
	
	private boolean isNarcotic;
	private String dateString;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getManufactureName() {
		return manufactureName;
	}

	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public Integer getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public boolean getNarcotic() {
		return isNarcotic;
	}

	public void setNarcotic(boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUomunit() {
		return uomunit;
	}

	public void setUomunit(String uomunit) {
		this.uomunit = uomunit;
	}
	

}
