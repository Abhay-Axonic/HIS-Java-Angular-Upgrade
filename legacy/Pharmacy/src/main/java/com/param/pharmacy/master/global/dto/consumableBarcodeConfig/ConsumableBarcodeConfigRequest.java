package com.param.pharmacy.master.global.dto.consumableBarcodeConfig;

public class ConsumableBarcodeConfigRequest extends ConsumableBarcodeConfigResponse {

	protected Boolean itemName;

	protected Boolean itemCode;
	
	protected Boolean batchNo;
	
	protected Boolean expiryDate;
	
	protected Boolean lotNo;
	
	protected Boolean mrp;
	
	protected Boolean salePrice;
	
	protected Boolean grnNo;
	
	protected Boolean vendorName;
	
	protected Boolean vendorCode;
	
	protected Boolean hospitalName;

	
	
	public ConsumableBarcodeConfigRequest() {
		super();
	}

	public Boolean getItemName() {
		return itemName;
	}

	public void setItemName(Boolean itemName) {
		this.itemName = itemName;
	}

	public Boolean getItemCode() {
		return itemCode;
	}

	public void setItemCode(Boolean itemCode) {
		this.itemCode = itemCode;
	}

	public Boolean getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(Boolean batchNo) {
		this.batchNo = batchNo;
	}

	public Boolean getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Boolean expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getLotNo() {
		return lotNo;
	}

	public void setLotNo(Boolean lotNo) {
		this.lotNo = lotNo;
	}

	public Boolean getMrp() {
		return mrp;
	}

	public void setMrp(Boolean mrp) {
		this.mrp = mrp;
	}

	public Boolean getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Boolean salePrice) {
		this.salePrice = salePrice;
	}

	public Boolean getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(Boolean grnNo) {
		this.grnNo = grnNo;
	}

	public Boolean getVendorName() {
		return vendorName;
	}

	public void setVendorName(Boolean vendorName) {
		this.vendorName = vendorName;
	}

	public Boolean getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(Boolean vendorCode) {
		this.vendorCode = vendorCode;
	}

	public Boolean getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(Boolean hospitalName) {
		this.hospitalName = hospitalName;
	}
	
}
