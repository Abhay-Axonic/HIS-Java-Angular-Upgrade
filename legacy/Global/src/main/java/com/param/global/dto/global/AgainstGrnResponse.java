package com.param.global.dto.global;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AgainstGrnResponse {

	protected Integer id;

	protected String grnNo;

	protected Date grnDate;

	protected Integer vendorId;

	protected String vendor;

	protected Integer storeId;

	protected String Store;
	
	protected boolean isPRNRequired;
	
	protected String pharmacyPRNFormat;
	

	protected boolean isManualBarcodeRequired;

	public String getPharmacyPRNFormat() {
		return pharmacyPRNFormat;
	}

	public void setPharmacyPRNFormat(String pharmacyPRNFormat) {
		this.pharmacyPRNFormat = pharmacyPRNFormat;
	}

	public boolean getIsPRNRequired() {
		return isPRNRequired;
	}

	public void setIsPRNRequired(boolean isPRNRequired) {
		this.isPRNRequired = isPRNRequired;
	}

	List<AgainstGrnDetailResponse> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return Store;
	}

	public void setStore(String store) {
		Store = store;
	}

	public List<AgainstGrnDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<AgainstGrnDetailResponse> items) {
		this.items = items;
	}

	public void addItem(AgainstGrnDetailResponse item) {
		if(this.items == null){
			this.items = new ArrayList<>();
		}

		this.items.add(item);
	}

	public boolean getIsManualBarcodeRequired() {
		return isManualBarcodeRequired;
	}

	public void setIsManualBarcodeRequired(boolean isManualBarcodeRequired) {
		this.isManualBarcodeRequired = isManualBarcodeRequired;
	}
}
