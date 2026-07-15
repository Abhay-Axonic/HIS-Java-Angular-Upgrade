package com.param.pharmacy.master.global.dto.consumableBarcodeConfig;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ConsumableBarcodeConfigResponse extends CookieObjectDto{

    protected Integer id;
	
	protected Boolean status;
	
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

	protected List<ConsumableBarcodeConfigResponse> items;
	
	public ConsumableBarcodeConfigResponse() {
		super();
	}

	
	public ConsumableBarcodeConfigResponse(Integer id, Boolean status, Boolean itemName, Boolean itemCode,
			Boolean batchNo, Boolean expiryDate, Boolean lotNo, Boolean mrp, Boolean salePrice, Boolean grnNo,
			Boolean vendorName, Boolean vendorCode, Boolean hospitalName) {
		super();
		this.id = id;
		this.status = status;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.lotNo = lotNo;
		this.mrp = mrp;
		this.salePrice = salePrice;
		this.grnNo = grnNo;
		this.vendorName = vendorName;
		this.vendorCode = vendorCode;
		this.hospitalName = hospitalName;
	}

	
	public List<ConsumableBarcodeConfigResponse> getItems() {
		return items;
	}


	public void setItems(List<ConsumableBarcodeConfigResponse> items) {
		this.items = items;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public void addItems(ConsumableBarcodeConfigResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ConsumableBarcodeConfigResponse>();
		
		this.items.add(item);
	}
	
}
