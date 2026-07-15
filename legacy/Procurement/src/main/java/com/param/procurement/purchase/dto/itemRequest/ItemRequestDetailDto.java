package com.param.procurement.purchase.dto.itemRequest;

public class ItemRequestDetailDto {
	
    private Integer id;
	
	private Integer itemRequestId;
	
	private String itemName;
	
	private Integer manufacturerId;

	private Integer vendorId;
	
	private Double expectedPrice;
	
	private String itemCreationReason;
	
	private String manufactureTemp;
	
	private String vendorTemp;
	
	private Integer statusId;
	
	private Integer rejectReasonId;

	private String rejectNote;

	private Integer assetTypeId;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemRequestId() {
		return itemRequestId;
	}

	public void setItemRequestId(Integer itemRequestId) {
		this.itemRequestId = itemRequestId;
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
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

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
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
