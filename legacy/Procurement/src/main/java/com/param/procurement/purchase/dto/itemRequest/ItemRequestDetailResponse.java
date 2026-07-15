package com.param.procurement.purchase.dto.itemRequest;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestDetailResponse {

	private Integer id;

	private String itemName;

	private Integer manufacturerId;
	
	private String manufacturer;

	private Integer vendorId;
	
	private String vendor;

	private Double expectedPrice;

	private String itemCreationReason;

	private String manufactureTemp;
	
	private String vendorTemp;
	
	private Integer statusId;
	
	private String status;
	
	
	public ItemRequestDetailResponse(Integer id,  String itemName, Integer manufacturerId,
			String manufacturer, Integer vendorId, String vendor, Double expectedPrice, String itemCreationReason,
			String manufactureTemp, String vendorTemp, Integer statusId, String status) {
		this.id = id;
		this.itemName = itemName;
		this.manufacturerId = manufacturerId;
		this.manufacturer = manufacturer;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.expectedPrice = expectedPrice;
		this.itemCreationReason = itemCreationReason;
		this.manufactureTemp = manufactureTemp;
		this.vendorTemp = vendorTemp;
		this.statusId = statusId;
		this.status = status;
	}
	
	public ItemRequestDetailResponse(Integer id, String itemName, Double expectedPrice, String itemCreationReason,
			String manufactureTemp, String vendorTemp, Integer statusId, String status) {
		super();
		this.id = id;
		this.itemName = itemName;
		this.expectedPrice = expectedPrice;
		this.itemCreationReason = itemCreationReason;
		this.manufactureTemp = manufactureTemp;
		this.vendorTemp = vendorTemp;
		this.statusId = statusId;
		this.status = status;
	}



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

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Double getExpectedPrice() {
		return expectedPrice;
	}

	public void setExpectedPrice(Double expectedPrice) {
		this.expectedPrice = expectedPrice;
	}

	public String getItemCreationReason() {
		return itemCreationReason;
	}

	public void setItemCreationReason(String itemCreationReason) {
		this.itemCreationReason = itemCreationReason;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getManufactureTemp() {
		return manufactureTemp;
	}

	public void setManufactureTemp(String manufactureTemp) {
		this.manufactureTemp = manufactureTemp;
	}

	public String getVendorTemp() {
		return vendorTemp;
	}

	public void setVendorTemp(String vendorTemp) {
		this.vendorTemp = vendorTemp;
	}
	
}
